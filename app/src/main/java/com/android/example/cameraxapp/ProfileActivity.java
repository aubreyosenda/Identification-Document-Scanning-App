package com.android.example.cameraxapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private Button changePhotoButton;
    private TextView fullname;
    private TextView phone_number;
    private TextView work_id;
    private TextView company_name;
    private TextView building_name;
    private Uri imageUri;
    private ProgressBar progressBar;
    private Button updateProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Find views by their IDs
        profileImage = findViewById(R.id.profileImage);
        fullname = findViewById(R.id.fullname);
        phone_number = findViewById(R.id.phone_number);
        work_id = findViewById(R.id.work_id);
        company_name = findViewById(R.id.company_name);
        building_name = findViewById(R.id.building_name);
        changePhotoButton = findViewById(R.id.changePhotoButton);
        progressBar = findViewById(R.id.Progressbar);
//        updateProfileButton = findViewById(R.id.updateProfileButton);

        // Set onClickListener for the change photo button
        changePhotoButton.setOnClickListener(v -> openFileChooser());

        // Load user profile from SharedPreferences
        loadUserProfile();
    }

    @SuppressLint("SetTextI18n")
    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("SecurityPersonnelPrefs", MODE_PRIVATE);

        // Retrieve and set user details
        String fullName = sharedPreferences.getString("fullName", "Name");
        long phoneNumberLong = sharedPreferences.getLong("phoneNumber", 0L); // Default to 0 if not found
        String phoneNumber = String.valueOf(phoneNumberLong); // Convert to String for display
        String workId = sharedPreferences.getString("workIdNumber", "Work ID");
        String companyName = sharedPreferences.getString("companyName", "Company Name");
        String buildingName = sharedPreferences.getString("buildingName", "Building Name");
        String profileImageUri = sharedPreferences.getString("profileImageUri", null);

        // Set text views
        fullname.setText(fullName);
        phone_number.setText(phoneNumber);
        work_id.setText(workId);
        company_name.setText(companyName);
        building_name.setText(buildingName);

        // Load profile image if it exists
        if (profileImageUri != null) {
            Glide.with(this).load(profileImageUri).into(profileImage);
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

    private void uploadImageToFirebase(String profileImageUri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profile_images/" + UUID.randomUUID().toString());
        storageReference.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageReference.getDownloadUrl().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Uri downloadUri = task1.getResult();
//                        updateUserProfile(profileImageUri = downloadUri.toString());
                    } else {
                        setInProgress(false);
                        Toast.makeText(ProfileActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                setInProgress(false);
                Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile(String profileImageUri) {
        // Assuming you have the logic to update the profile in SharedPreferences or Firestore
        // After update, refresh the view or notify the user
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
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
