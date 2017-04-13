package com.example.android.tripIntent.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.tripIntent.R;

/**
 * Created by BT on 10/25/16.
 */

/*The page comes when we select on a particular image. Gives the option to delete it*/

public class DisplayImageActivity extends Activity {
    Button btnDelete;
    ImageView imageDetail;
    int imageId;
    Bitmap theImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteimage_camera);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        imageDetail = (ImageView) findViewById(R.id.imageView1);
        /**
         * getting intent data from search and previous screen
         */
        Intent intnt = getIntent();
        theImage = (Bitmap) intnt.getParcelableExtra("imagename");
        imageId = intnt.getIntExtra("imageid", 20);
        Log.d("Image ID:****", String.valueOf(imageId));
        imageDetail.setImageBitmap(theImage);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * create DatabaseHandler object
                 */
                DataBaseHandler db = new DataBaseHandler(
                        DisplayImageActivity.this);
                /**
                 * Deleting records from database
                 */
                Log.d("Delete Image: ", "Deleting.....");
                db.deleteContact(new Contact(imageId));
                // /after deleting data go to main_sos page
                Intent i = new Intent(DisplayImageActivity.this,
                        SQLiteDemoActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}