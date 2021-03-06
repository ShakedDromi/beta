package com.example.beta;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBref {
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refUsersB= FBDB.getReference("UserB");
    public static DatabaseReference refUsersP= FBDB.getReference("UserP");
    public static DatabaseReference refJobOffer= FBDB.getReference("JobOffer");
    public static DatabaseReference refPlaces= FBDB.getReference("Places");

    public static FirebaseStorage FBST = FirebaseStorage.getInstance();
    public static StorageReference refStor=FBST.getReference();
}
