package com.example.android.tripIntent.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.tripIntent.R;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.delay;

/* Main page inside the camera tab. Has methods for taking pictures from default camera and selecting pictures from the photo gallery */
public class SQLiteDemoActivity extends Activity {
    Button addImage;
    ArrayList<Contact> imageArry = new ArrayList<Contact>();
    ContactImageAdapter imageAdapter;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;
    ListView dataList;
    byte[] imageName;
    int imageId;
    Bitmap theImage;
    DataBaseHandler db;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_section);
        dataList = (ListView) findViewById(R.id.list);
        /**
         * create DatabaseHandler object
         */

        FirebaseStorage storage = FirebaseStorage.getInstance();



        db = new DataBaseHandler(this);
        /**
         * Reading and getting all records from database
         */
        List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) {
            String log = "ID:" + cn.getID() + " Name: " + cn.getName()
                    + " ,Image: " + cn.getImage();

            // Writing Contacts to log
            Log.d("Result: ", log);
            // add contacts data in arrayList
            imageArry.add(cn);

        }
        /**
         * Set Data base Item into listview
         */
        imageAdapter = new ContactImageAdapter(this, R.layout.screen_list_camera,
                imageArry);
        dataList.setAdapter(imageAdapter);
        /**
         * go to next activity for detail image
         */
        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                imageName = imageArry.get(position).getImage();
                imageId = imageArry.get(position).getID();

                Log.d("Before Send:****", imageName + "-" + imageId);
                // convert byte to bitmap
                ByteArrayInputStream imageStream = new ByteArrayInputStream(
                        imageName);
                theImage = BitmapFactory.decodeStream(imageStream);
                Intent intent = new Intent(SQLiteDemoActivity.this,
                        DisplayImageActivity.class);
                intent.putExtra("imagename", theImage);
                intent.putExtra("imageid", imageId);
                startActivity(intent);

            }
        });
        /**
         * open dialog for choose camera/gallery
         */

        final String[] option = new String[] { "Take from Camera",
                "Select from Gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Creating the option menu when the Add Image button is clicked.
        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Log.e("Selected Item", String.valueOf(which));
                if (which == 0) {
                    callCamera();
                }
                if (which == 1) {
                    callGallery();
                }

            }
        });
        final AlertDialog dialog = builder.create();

        addImage = (Button) findViewById(R.id.btnAdd);

        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });




    }

    public void callCamera() {

        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 0);
        cameraIntent.putExtra("aspectY", 0);
        cameraIntent.putExtra("outputX", 200);
        cameraIntent.putExtra("outputY", 150);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);






    }

    /**
     * open gallery method
     */

    public void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);


        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY);

    }

    /**
     * On activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CAMERA_REQUEST:

                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap yourImage = extras.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    Log.e("output b4 conversion", imageInByte.toString());
                    // Inserting Contacts
                    Log.d("Insert: ", "Inserting ..");
                    db.addContact(new Contact("Android", imageInByte));
                    Intent i = new Intent(SQLiteDemoActivity.this,
                            SQLiteDemoActivity.class);
                    startActivity(i);
                    finish();

                }
                break;
            case PICK_FROM_GALLERY:
                Bundle extras2 = data.getExtras();


                if (extras2 != null) {
                    Bitmap yourImage = extras2.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    Log.e("output b4 conversion", imageInByte.toString());
                    // Inserting Contacts
                    Log.d("Insert: ", "Inserting ..");
                    db.addContact(new Contact("Android", imageInByte));
                    Intent i = new Intent(SQLiteDemoActivity.this,
                            SQLiteDemoActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Log.d("extras2: ", "extra2 bundle is null ..");
                }

                break;
        }
    }



    @Override
    protected void onStop() {




        super.onStop();
    }


}