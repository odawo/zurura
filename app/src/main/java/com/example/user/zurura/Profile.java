package com.example.user.zurura;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser firebaseUser;

    ImageView userprofile_view;
   // ImageButton upload_btn;
    ImageButton save_btn;
    //ImageButton edit_btn;
    //TextView username_text;
    TextView email_text;

    ProgressDialog progressDialog;

    Uri imageHoldurl = null;

    private static final  int REQUEST_CAMERA = 1;
    private static final  int SELECT_IMAGE =1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab1_profile, container, false);

        userprofile_view = (ImageView) rootView.findViewById(R.id.user_profile_photo);
        //upload_btn = (ImageButton) rootView.findViewById(R.id.add_photo);
        save_btn = (ImageButton) rootView.findViewById(R.id.user_usersavebtn);
        //edit_btn = (ImageButton) rootView.findViewById(R.id.menu_edit);
        //username_text = (TextView) rootView.findViewById(R.id.user_username);
        email_text = (TextView) rootView.findViewById(R.id.user_useremail);

        progressDialog = new ProgressDialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(firebaseAuth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //display current user's email on profile page
        email_text.setText(firebaseAuth.getCurrentUser().getEmail());
       // username_text.setText(firebaseAuth.getCurrentUser().getDisplayName());



        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveuserprofile();
            }
        });

        userprofile_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setprofpic();
            }
        });

        return rootView;
    }

    private void saveuserprofile(){

            if(imageHoldurl != null){

                progressDialog.setMessage("loading");
                progressDialog.show();

                StorageReference childStorageReference = storageReference.child("profile").child(imageHoldurl.getLastPathSegment());
                String picurl = imageHoldurl.getLastPathSegment();

                childStorageReference.putFile(imageHoldurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        final Uri imageurl = taskSnapshot.getDownloadUrl();

                        databaseReference.child("userid").setValue(firebaseAuth.getCurrentUser().getUid());
                        databaseReference.child("imageurl").setValue(imageurl.toString());

                        progressDialog.dismiss();
                    }
                });
            }else{
                Toast.makeText(getActivity(), "Please select a profile picture", Toast.LENGTH_LONG).show();
            }
    }

    private void setprofpic(){
        //dialog to choose pic source
        final CharSequence[] items = {"Take Photo","Choose from Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload Photo");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(items[item].equals("Take Photo")){
                    camIntent();
                }else if(items[item].equals("Choose from Gallery")){
                    galleryIntent();
                }else if(items[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //take cam pic
    private void camIntent(){
        try{
            Log.d("data","entered here");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //take img from gallery
    private void galleryIntent(){
        try {
            Log.d("data","entered here");
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("img/*");
            startActivityForResult(intent, SELECT_IMAGE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK){
            Uri selectedimg = data.getData();
            CropImage.activity(selectedimg).
                    setGuidelines(CropImageView.Guidelines.ON).
                    setAspectRatio(1,1).start(getActivity());
        }
        else if(requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
            Uri selectedimg = data.getData();
            CropImage.activity(selectedimg).
                    setGuidelines(CropImageView.Guidelines.ON).
                     setAspectRatio(1,1).start(getActivity());
        }

        //image crop
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                imageHoldurl = activityResult.getUri();
                userprofile_view.setImageURI(imageHoldurl);

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception erException = activityResult.getError();
            }
        }
    }
}
