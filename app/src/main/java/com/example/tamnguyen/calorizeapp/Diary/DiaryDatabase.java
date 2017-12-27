package com.example.tamnguyen.calorizeapp.Diary;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by hoangdung on 12/27/17.
 */

public class DiaryDatabase {
    private static final DiaryDatabase ourInstance = new DiaryDatabase();

    public static DiaryDatabase getInstance() {
        return ourInstance;
    }

    private static String DiaryPath = "diary";
    private static String DiaryMetaPath = "diary_meta";
    private static String FoodSubPath = "food";
    private DatabaseReference diaryRef = FirebaseDatabase.getInstance().getReference().child(DiaryPath)
            .child(FirebaseAuth.getInstance().getUid());
    private DatabaseReference diaryMetaRef = FirebaseDatabase.getInstance().getReference().child(DiaryMetaPath)
            .child(FirebaseAuth.getInstance().getUid());
    /**
     * Callback Interface for Query Completion
     */
    public interface OnCompleteListener{
        void onSuccess(Diary diary);
        void onFailure(int code);
    }
    private DiaryDatabase() {
    }

    /**
     * Helper method for geting Diary Database of current user
     * @param calendar : Specify date to get data
     * @param listener: OnCompleteListener to listen to query operation
     */
    public void getDiary(Calendar calendar, final OnCompleteListener listener){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String key = sdf.format(calendar.getTime());
        diaryRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Diary diary = new Diary();
                if(dataSnapshot.exists())
               {
                   diary.fromSnapshot(dataSnapshot);
               }
               listener.onSuccess(diary);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getCode());
            }
        });
    }

}
