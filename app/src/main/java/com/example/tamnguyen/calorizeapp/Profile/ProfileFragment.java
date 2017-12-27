package com.example.tamnguyen.calorizeapp.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tamnguyen.calorizeapp.R;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HUYNHXUANKHANH on 12/26/2017.
 */

public class ProfileFragment extends Fragment {
    private Profile profile;
    private TextView textViewName,textViewAge,textViewHeight,textViewWeight,textViewGender,textViewBirthDay;
    private FloatingActionButton floatingActionButtonEdit;
    private ImageView imageViewAvatar;
    private int REQUEST_EDIT = 100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView;
        doGetProfile();
        retView = inflater.inflate(R.layout.fragment_profile,container,false);
        doInitControl(retView);
        setView();
        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditProfileActivity.class);
                intent.putExtra("profile-edit",profile);
                startActivityForResult(intent,REQUEST_EDIT);
            }
        });

    }
    public void doGetProfile(){
        profile = new Profile(
                getActivity().getIntent().getStringExtra("name"),
                getActivity().getIntent().getStringExtra("gender"),
                getActivity().getIntent().getStringExtra("birthday"),
                getActivity().getIntent().getStringExtra("picture"),
                0,
                69,
                175,
                false,
                false);
        profile.generateAge();
    }
    public void setView(){
        textViewName.setText(profile.getFullName());
        textViewAge.setText(String.format(Locale.ENGLISH,"%d years old",profile.getiAge()));
        textViewGender.setText(profile.getGender().substring(0, 1).toUpperCase() + profile.getGender().substring(1));
        textViewBirthDay.setText(profile.getDateOfBirth());
        Glide.with(getActivity()).load(profile.getUrlAvatar()).apply(RequestOptions.circleCropTransform()).into(imageViewAvatar);

        if(profile.getiWeight()!=0){
            if (!profile.isbWeightType()) {
                textViewWeight.setText(String.format(Locale.ENGLISH,"%.2f kg",profile.getiWeight()));
            }
            else{
                textViewWeight.setText(String.format(Locale.ENGLISH,"%.2f pound",profile.getiWeight()));
            }
        }
        else
            textViewWeight.setText("null");

        if(profile.getiHeight()!=0){
            if (!profile.isbHeightType()) {
                textViewHeight.setText(String.format(Locale.ENGLISH,"%.2f cm",profile.getiHeight()));
            }
            else{
                textViewHeight.setText(String.format(Locale.ENGLISH,"%.2f foot",profile.getiHeight()));
            }
        }
        else
            textViewHeight.setText("null");
    }
    public void doInitControl(View view){
        textViewName = view.findViewById(R.id.profile_name);
        textViewAge = view.findViewById(R.id.profile_yearold);
        textViewHeight = view.findViewById(R.id.profile_height);
        textViewWeight = view.findViewById(R.id.profile_weight);
        textViewGender = view.findViewById(R.id.profile_gender);
        textViewBirthDay = view.findViewById(R.id.profile_dateofbirth);
        floatingActionButtonEdit = view.findViewById(R.id.profile_edit);
        imageViewAvatar = view.findViewById(R.id.profile_image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_EDIT){
            if(resultCode==101) // edit
            {
                profile = data.getParcelableExtra("profile-edit-result");
                setView();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();

    }

}
