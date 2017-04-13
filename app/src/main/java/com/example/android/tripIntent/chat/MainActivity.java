package com.example.android.tripIntent.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.tripIntent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mGroupList;

    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                }

            }



        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Group");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseUsers.keepSynced(true);


        mGroupList = (RecyclerView)findViewById(R.id.group_list);
        mGroupList.setHasFixedSize(false);
        mGroupList.setLayoutManager(new LinearLayoutManager(this));

        checkUserExist();

    }


    @Override
    protected void onStart() {

        super.onStart();

        checkUserExist();


        if(mAuth.getCurrentUser() != null) {

            //This runs only if the user has logged in
            final String user_id = mAuth.getCurrentUser().getUid();


            mAuth.addAuthStateListener(mAuthListener);

            FirebaseRecyclerAdapter<Group, GroupViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Group, GroupViewHolder>(
                    Group.class,
                    R.layout.group_row,
                    GroupViewHolder.class,
                    mDatabase.orderByChild("uid").equalTo(user_id)
            ) {
                @Override
                protected void populateViewHolder(final GroupViewHolder viewHolder, Group model, int position) {

//                //get current user id
                    // final String user_id = mAuth.getCurrentUser().getUid();
//
//                mDatabase = FirebaseDatabase.getInstance().getReference().child("Group");
//////
//                if (model.getUid().equals(user_id)) {
//                    //final String post_key = getRef(position).getKey();
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                    viewHolder.setDate(model.getDate());
//
//
//                }
//                else{
//
////                    viewHolder.mView.setVisibility(View.INVISIBLE);
////                    Picasso.with(getApplicationContext()).cancelRequest(model.getImage());
//                    viewHolder.mView.setVisibility(View.GONE);
//
////                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams;
////                    viewHolder.mView.setLayoutParams()
////                    viewHolder.mView.setMinimumWidth(0);
//
//                }
////                if(mDatabase.child("uid").getKey().trim().equals(user_id)){
////                    final String post_key = getRef(position).getKey();
////                    viewHolder.setTitle(model.getTitle());
////                    viewHolder.setDesc(model.getDesc());
////                    viewHolder.setImage(getApplicationContext(),model.getImage());
////                    viewHolder.setDate(model.getDate());


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                        Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_LONG).show();

                            Intent singleGroupIntent = new Intent(MainActivity.this, com.example.android.tripIntent.MainActivity.class);
                            singleGroupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(singleGroupIntent);


                        }
                    });


                }
            };


            mGroupList.setAdapter(firebaseRecyclerAdapter);
        }
    }


    //To check if the user exists in the database
    private void checkUserExist() {

        if(mAuth.getCurrentUser() != null) {

            //This runs only if the user has logged in
            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                //Returns result
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(user_id)) {

                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public GroupViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }


        public void setDesc(String desc){
             TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }


        public void setDate(String date){

            TextView post_date = (TextView)mView.findViewById(R.id.post_date);
            post_date.setText(date);


        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add)
        {
            startActivity(new Intent(MainActivity.this,CreateGroup.class));
        }

        if(item.getItemId() == R.id.action_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.signOut();
    }
}
