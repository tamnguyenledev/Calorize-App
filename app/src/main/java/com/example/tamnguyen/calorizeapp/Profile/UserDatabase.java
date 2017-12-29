package com.example.tamnguyen.calorizeapp.Profile;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by hoangdung on 12/29/17.
 */

public class UserDatabase {
    private static final UserDatabase ourInstance = new UserDatabase();

    public static UserDatabase getInstance() {
        return ourInstance;
    }

    private UserDatabase() {
    }
    private static final String path = "users";
    private DatabaseReference userDbRef = FirebaseDatabase.getInstance().getReference().child(path);

    /**
     * This method is to push user data onto database
     * This method can also be used for updating data
     * @param profile
     * @param eventListener
     */
    public void  addUser(Profile profile, OnCompleteListener<Void> eventListener){
        userDbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(profile)
                .addOnCompleteListener(eventListener);
    }
}
