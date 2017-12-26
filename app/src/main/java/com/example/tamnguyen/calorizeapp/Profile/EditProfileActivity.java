package com.example.tamnguyen.calorizeapp.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tamnguyen.calorizeapp.R;

public class EditProfileActivity extends AppCompatActivity {
    private Profile profile;
    private EditText editTextName,editTextGender,editTextDateOfBirth,editTextWeight
            ,editTextHeight,editTextTypeWeight,editTextTypeHeight;
    private ImageView imageViewAvatar;
    private Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        doInitControl();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        //create back button on top-left of toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile = getIntent().getParcelableExtra("profile");
        doSetView(profile);

    }

    
    public void doSetView(Profile profile){
        if(profile!=null){
            editTextName.setText(profile.getFullName());
            editTextDateOfBirth.setText(profile.getDateOfBirth());
            editTextGender.setText(profile.getGender());
            editTextWeight.setText(profile.getiWeight()+"");
            editTextHeight.setText(profile.getiHeight()+"");
            Glide.with(this).load(profile.getUrlAvatar()).apply(RequestOptions.circleCropTransform()).into(imageViewAvatar);

            if(profile.isbWeightType())
                editTextTypeWeight.setText(R.string.pound);
            else
                editTextTypeWeight.setText(R.string.kilogram);
            if(profile.isbHeightType())
                editTextTypeHeight.setText(R.string.centimeter);
            else
                editTextTypeHeight.setText(R.string.foot);
        }
    }
    public void doInitControl(){
        editTextName = findViewById(R.id.profile_edit_name);
        editTextDateOfBirth = findViewById(R.id.profile_edit_dateofbirth);
        editTextGender = findViewById(R.id.profile_edit_gender);
        editTextHeight = findViewById(R.id.profile_edit_height);
        editTextWeight = findViewById(R.id.profile_edit_weight);
        editTextTypeHeight = findViewById(R.id.profile_edit_type_height);
        editTextTypeWeight = findViewById(R.id.profile_edit_type_weight);
        imageViewAvatar = findViewById(R.id.profile_image);
        btnSave = findViewById(R.id.btn_saveProfile);
    }
}
