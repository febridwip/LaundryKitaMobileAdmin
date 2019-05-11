package com.tugas_besar.laundry_kita_mobile_admin.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tugas_besar.laundry_kita_mobile_admin.Models.Admin;
import com.tugas_besar.laundry_kita_mobile_admin.R;

public class SignUpActivity extends AppCompatActivity {

    ImageView ImgUserPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    private EditText userEmail, userPassword1, userPassword2, userName, userPhone;
    private ProgressBar loadingProgress;
    private Button regBtn, btnSignIn, btnResetPassword;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Inisiasi Views
        userName            = findViewById(R.id.regName);
        userEmail           = findViewById(R.id.regEmail);
        userPhone           = findViewById(R.id.regPhone);
        userPassword1       = findViewById(R.id.regPassword1);
        userPassword2       = findViewById(R.id.regPassword2);
        ImgUserPhoto        = findViewById(R.id.regUserPhoto);
        loadingProgress     = findViewById(R.id.progressBar);
        loadingProgress.setVisibility(View.INVISIBLE);
        regBtn              = findViewById(R.id.regBtn);
        btnSignIn           = findViewById(R.id.btn_sign_in);
        btnResetPassword    = findViewById(R.id.btn_reset_password);
        mAuth               = FirebaseAuth.getInstance();

        databaseReference   = FirebaseDatabase.getInstance().getReference("Admin");

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                String name       = userName.getText().toString();
                String email      = userEmail.getText().toString();
                String phone      = userPhone.getText().toString();
                String password1  = userPassword1.getText().toString();
                String password2  = userPassword2.getText().toString();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password1.isEmpty() || password2.isEmpty() || !password1.equals(password2)) {
                    //something goes wro ng : all fields must be filled
                    //we need to display an error message
                    showMessage("Please verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else {
                    // everything is ok and all fields are filled now we can start creating user account
                    // CreateUserAccount method will try to create the user if the email is valid
                    CreateUserAccount(name, email, phone, password1);
                }
            }
        });

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 26) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        }) ;

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    private void CreateUserAccount(final String name, final String email, final String phone, final String password1) {
        // this method create user account with email and password
        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            com.tugas_besar.laundry_kita_mobile_admin.Models.Admin information = new Admin(
                                    name,
                                    email,
                                    phone,
                                    password1
                            );
                            FirebaseDatabase.getInstance().getReference("Admin")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SignUpActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                    // user account created successfully
                                    //  showMessage("Account created");
                                    // after we created user account we need to update his profile picture and name
                                    updateUserInfo(name, phone, pickedImgUri, mAuth.getCurrentUser());
                                }
                            });

                        } else {
                            //account creation failed
                            showMessage("account creation failed" + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    // update user photo and name
    private void updateUserInfo(final String name, String phone, Uri pickedImgUri, final FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get uri

        StorageReference mStorage             = FirebaseStorage.getInstance().getReference().child("admin_photos");
        final StorageReference imageFilePath  = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded successfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // URL CONTAIN USER, IMAGE URI

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //user info updated successfully
                                            showMessage("Sign Up Completed");
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), Home.class);
        startActivity(homeActivity);
        finish();
    }

    //simple method to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        //TODO : open gallery intent and wait for user to pick an image !
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(SignUpActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(SignUpActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            // the user has successfully picked an image
            // we need save its reference to a Uri variable
            pickedImgUri = data.getData();
         //   CropImageView cropImageView;
         //   Rect rect = ImgUserPhoto.getCropRect();
            ImgUserPhoto.setImageURI(pickedImgUri);
            CropImage.activity(pickedImgUri)
                //    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ImgUserPhoto.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}