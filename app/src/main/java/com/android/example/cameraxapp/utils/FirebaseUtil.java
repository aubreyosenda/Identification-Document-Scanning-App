package com.android.example.cameraxapp.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {

    // Get the current user's ID
    public static String currentUserId() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    // Check if the user is logged in
    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    // Get a reference to the current user's Firestore document
    public static DocumentReference currentUserDetails() {
        String userId = currentUserId();
        if (userId != null) {
            return FirebaseFirestore.getInstance().collection("users").document(userId);
        } else {
            // Handle the case where userId is null, possibly return null or throw an exception
            return null;
        }
    }
//    public static boolean isLoggedIn() {
//
//        if (currentUserId() != null){
//            return true;
//        }
//        return false;
//    }
}
