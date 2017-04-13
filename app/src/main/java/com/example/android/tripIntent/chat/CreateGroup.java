package com.example.android.tripIntent.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.android.tripIntent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;

public class CreateGroup extends AppCompatActivity {
private ImageButton mSelectImage;
    private static final int GALLERY_REQUEST = 1;
    private EditText mPostTitle;
    private EditText mPostDesc;

    private Button mCreateBtn;

    private Uri mImageUri= null;

    private StorageReference mStorage;

    private ProgressDialog mProgress;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;


    //private CropImageView mCropImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mSelectImage = (ImageButton)findViewById(R.id.imageSelect);
        mPostTitle = (EditText)findViewById(R.id.titleField);
        mPostDesc = (EditText)findViewById(R.id.descField);
        mCreateBtn = (Button) findViewById(R.id.createBtn);

        mStorage = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Group");

        //Cropped image view initialization
        //mCropImageView = (CropImageView)findViewById(R.id.cropImageView);

        mSelectImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Gallery access for group image
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startCreating();

            }
        });
    }

    private void startCreating() {

        //Progress bar
        mProgress.setMessage("Creating Group..");


        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null) {
            //database
            mProgress.show();
// Add aws code to store all details thats stored in code below
            //this will return the name of the image
            StorageReference filepath = mStorage.child("Group_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    final DatabaseReference newGroup = mDatabase.push();

                    //get date from system
                    java.text.DateFormat date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date1 = new Date();
                    final String currentDate = date.format(date1);



                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newGroup.child("title").setValue(title_val);
                            newGroup.child("desc").setValue(desc_val);
                            newGroup.child("image").setValue(downloadUrl.toString());
                            newGroup.child("uid").setValue(mCurrentUser.getUid()); //stores uid inside database
                            newGroup.child("date").setValue(currentDate);
                            //newGroup.child("location").setValue();
                            newGroup.child("Username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {


                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {

                                        startActivity(new Intent(CreateGroup.this,MainActivity.class));

                                    }
                                }
                            });



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {




                        }
                    });



                    mProgress.dismiss();



                }
            });


        }


    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){


            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //.setCropShape(CropImageView.CropShape.OVAL)
                    .setMinCropResultSize(200,200)
                    .setAspectRatio(16,9)
                    .start(this);

        }

        //This return the cropped image and we set it
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                mSelectImage.setImageURI(mImageUri);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
