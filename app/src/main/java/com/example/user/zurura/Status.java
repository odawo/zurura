package com.example.user.zurura;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by paul on 12/06/2017.
 */

public class Status extends Fragment implements OnConnectionFailedListener {
    ImageButton imageButton;
    ImageView imageView;
    ImageView imageView2;
    DatabaseReference firebaseDatabase;
    FirebaseUser user;
    Button postButton;
    Button placeButton;
    EditText descText;
    TextView locText;
    FirebaseAuth firebaseAuth;


    Bitmap imageBitmap;
    private GoogleApiClient mGoogleApiClient;
    GoogleMap map;
    MapView mapView;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    int PLACE_PICKER_REQUEST = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_status, container, false);
        imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        descText = (EditText) rootView.findViewById((R.id.descText));
        locText = (TextView) rootView.findViewById((R.id.locText));
        postButton = (Button) rootView.findViewById(R.id.post);
        placeButton = (Button) rootView.findViewById(R.id.places);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
            }
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(descText.getText().toString().trim()) && !TextUtils.isEmpty(locText.getText().toString().trim())) {


                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReference();
                    final String filename = System.currentTimeMillis() + user.getUid();
                    StorageReference mountainsRef = storageRef.child(filename);
//                imageView.setDrawingCacheEnabled(true);
//                imageView.buildDrawingCache();
                    Bitmap bitmap = imageBitmap;
                    if (bitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Date date = new Date(System.currentTimeMillis());
                                DateFormat formatter = new SimpleDateFormat("HH:mm");
                                String dateFormatted = formatter.format(date);
                                Post newPost = new Post(descText.getText().toString(), locText.getText().toString(), dateFormatted, filename, firebaseAuth.getCurrentUser().getEmail());
                                firebaseDatabase.child("posts").push().setValue(newPost);
                                Toast.makeText(getActivity(), "Status Posted", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else{
                        Date date = new Date(System.currentTimeMillis());
                        DateFormat formatter = new SimpleDateFormat("HH:mm");
                        String dateFormatted = formatter.format(date);
                        Post newPost = new Post(descText.getText().toString(), locText.getText().toString(), dateFormatted, null, firebaseAuth.getCurrentUser().getEmail());
                        firebaseDatabase.child("posts").push().setValue(newPost);
                        Toast.makeText(getActivity(), "Status Posted", Toast.LENGTH_SHORT).show();
                    }
                    } else {
                        Toast.makeText(getActivity(), "Kindly fill in all fields", Toast.LENGTH_SHORT).show();
                    }

            }
        });
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setBackground(null);
            imageView.setImageBitmap(imageBitmap);
        }
       else if (requestCode == PLACE_PICKER_REQUEST) {
          if (resultCode == RESULT_OK) {
              Place place = PlacePicker.getPlace(data, getActivity());
              locText.setText(place.getName());
          }
    }

    }



    }

