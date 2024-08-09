package com.android.example.cameraxapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.example.cameraxapp.Model.UserModel;
import com.android.example.cameraxapp.utils.FirebaseUtil;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private EditText usernameInput, fullNameInput, emailInput, phoneNumberInput;
    private Button changePhotoButton, updateProfileButton;
    private ProgressBar progressBar;
    private Uri imageUri;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layoutprofile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profileImage = findViewById(R.id.profileImage);
        usernameInput = findViewById(R.id.username);
        fullNameInput = findViewById(R.id.fullname);
        emailInput = findViewById(R.id.email);
        phoneNumberInput = findViewById(R.id.phone_number);
        changePhotoButton = findViewById(R.id.changePhotoButton);
        updateProfileButton = findViewById(R.id.update_profile);
        progressBar = findViewById(R.id.Progressbar);

        loadUserProfile();

        changePhotoButton.setOnClickListener(v -> openFileChooser());
        updateProfileButton.setOnClickListener(v -> updateProfile());
    }

    private void loadUserProfile() {
        setInProgress(true);
        DocumentReference userRef = FirebaseUtil.currentUserDetails();
        if (userRef != null) {
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    setInProgress(false);
                    if (task.isSuccessful() && task.getResult() != null) {
                        userModel = task.getResult().toObject(UserModel.class);
                        if (userModel != null) {
                            usernameInput.setText(userModel.getUsername());
                            fullNameInput.setText(userModel.getFullName());
                            emailInput.setText(userModel.getEmail());
                            phoneNumberInput.setText(userModel.getPhone());
                            if (userModel.getProfileImageUri() != null) {
                                Glide.with(ProfileActivity.this).load(userModel.getProfileImageUri()).into(profileImage);
                            }
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            setInProgress(false);
            Toast.makeText(ProfileActivity.this, "Failed to get user details", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(profileImage);
        }
    }

    private void updateProfile() {
        String username = usernameInput.getText().toString();
        String fullName = fullNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String phone = phoneNumberInput.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            usernameInput.setError("Username length should be at least 3 chars");
            return;
        }

        setInProgress(true);

        if (imageUri != null) {
            uploadImageToFirebase(username, fullName, email, phone);
        } else {
            updateUserProfile(username, fullName, email, phone, null);
        }
    }

    private void uploadImageToFirebase(String username, String fullName, String email, String phone) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profile_images/" + UUID.randomUUID().toString());
        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                updateUserProfile(username, fullName, email, phone, downloadUri.toString());
                            } else {
                                setInProgress(false);
                                Toast.makeText(ProfileActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    setInProgress(false);
                    Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserProfile(String username, String fullName, String email, String phone, String profileImageUri) {
        DocumentReference userRef = FirebaseUtil.currentUserDetails();

        if (userRef == null) {
            Toast.makeText(ProfileActivity.this, "Failed to get user details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creating a map to hold the fields to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("username", username);
        updates.put("fullName", fullName);
        updates.put("email", email);
        updates.put("phone", phone);
        if (profileImageUri != null) {
            updates.put("profileImageUri", profileImageUri);
        }

        userRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            updateProfileButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            updateProfileButton.setVisibility(View.VISIBLE);
        }
    }
}
