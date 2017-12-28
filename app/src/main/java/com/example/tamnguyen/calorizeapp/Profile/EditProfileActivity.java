package com.example.tamnguyen.calorizeapp.Profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tamnguyen.calorizeapp.R;

import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private Profile profile;
    private EditText editTextName,editTextWeight,editTextHeight;
    private Spinner spinnerGender,spinnerTypeWeight,spinnerTypeHeight;
    private ImageView imageViewAvatar;
    private TextView textViewTitle,textViewBirthDay;
    private ImageButton btnBack,btnImageAvatar,btnBirthDay;
    private Button btnSave;
    private boolean tempTypeWeight,tempTypeHeight;
    private float tempOldWeight,tempNewWeight,tempOldHeight,tempNewHeight;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int RESULT_CODE_EDIT = 101;
    private String selectedImageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        doInitControl();

        profile = getIntent().getParcelableExtra("profile-edit");


        doSetView(profile);

        doOnChangeListener();


    }

    public void doOnChangeListener(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                // se hoi nguoi dung muon save lai hay khong
            }
        });

        btnSave.setText("Save");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.setFullName(editTextName.getText().toString());
                profile.setGender(spinnerGender.getSelectedItem().toString());
                profile.setDateOfBirth(textViewBirthDay.getText().toString());
                profile.setiWeight(Float.parseFloat(editTextWeight.getText().toString()));
                profile.setiHeight(Float.parseFloat(editTextHeight.getText().toString()));
                if(spinnerTypeHeight.getSelectedItemPosition()==0)
                    profile.setbHeightType(false);
                else profile.setbHeightType(true);
                if(spinnerTypeWeight.getSelectedItemPosition()==0)
                    profile.setbWeightType(false);
                else profile.setbWeightType(true);
                if(selectedImageUri!=null &&!selectedImageUri.isEmpty())
                    profile.setUrlAvatar(selectedImageUri);

                Intent retIntent = new Intent();
                retIntent.putExtra("profile-edit-result",profile);
                setResult(RESULT_CODE_EDIT,retIntent);

                finish();
            }
        });
        textViewTitle.setText(R.string.edit_name_toolbar);

        btnImageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lay anh moi tư gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btnBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // goi calendar de cho chon ngay
                // roi set view cho textview birthDay
                final int day,month,year;
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH) + 1;
                year = calendar.get(Calendar.YEAR);

                @SuppressLint({"NewApi", "LocalSuppress"}) DatePickerDialog datePicker = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int iYear, int iMonth, int iDay) {
                        if(iYear <= year && iMonth <= month && iDay <= day )
                            textViewBirthDay.setText(String.format(Locale.ENGLISH,"%02d/%02d/%d",iMonth+1,iDay,iYear));
                    }
                },year,month,day);

                datePicker.show();
            }
        });

        spinnerTypeHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        if(tempTypeHeight) {
                            tempTypeHeight = false;
                            if(tempOldHeight!=
                                    profile.convertFeet2Cm(Float.parseFloat(editTextHeight.getText().toString()))) {
                                tempOldHeight = tempNewHeight;
                                tempNewHeight =
                                        profile.convertFeet2Cm(Float.parseFloat(editTextHeight.getText().toString()));
                                editTextHeight.setText(String.format(Locale.ENGLISH
                                        , "%.2f"
                                        , tempNewHeight));
                                break;
                            }
                            editTextHeight.setText(String.format(Locale.ENGLISH
                                    , "%.2f"
                                    , tempOldHeight));
                        }
                        break;
                    case 1:
                        if(!tempTypeHeight) {
                            tempTypeHeight = true;
                            if(tempNewHeight!=
                                    profile.convertCm2Feet(Float.parseFloat(editTextHeight.getText().toString()))) {
                                tempNewHeight = tempOldHeight;
                                tempOldHeight =
                                        profile.convertCm2Feet(Float.parseFloat(editTextHeight.getText().toString()));
                                editTextHeight.setText(String.format(Locale.ENGLISH
                                        , "%.2f"
                                        , tempOldHeight));
                                break;
                            }
                            editTextHeight.setText(String.format(Locale.ENGLISH
                                    , "%.2f"
                                    , tempNewHeight));
                        }
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTypeWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        if(tempTypeWeight) {
                            tempTypeWeight = false;
                            if(tempOldWeight!=
                                    profile.convertPound2Kilogram(Float.parseFloat(editTextWeight.getText().toString()))) {
                                tempOldWeight = tempNewWeight;
                                tempNewWeight =
                                        profile.convertPound2Kilogram(Float.parseFloat(editTextWeight.getText().toString()));
                                editTextWeight.setText(String.format(Locale.ENGLISH
                                        , "%.2f"
                                        , tempNewWeight));
                                break;
                            }
                            editTextWeight.setText(String.format(Locale.ENGLISH
                                    , "%.2f"
                                    , tempOldWeight));
                        }
                        break;
                    case 1:
                        if(!tempTypeWeight) {
                            tempTypeWeight = true;
                            if(tempNewWeight!=
                                    profile.convertKilogram2Pound(Float.parseFloat(editTextWeight.getText().toString()))) {
                                tempNewWeight = tempOldWeight;
                                tempOldWeight =
                                        profile.convertKilogram2Pound(Float.parseFloat(editTextWeight.getText().toString()));
                                editTextWeight.setText(String.format(Locale.ENGLISH
                                        , "%.2f"
                                        , tempOldWeight));
                                break;
                            }
                            editTextWeight.setText(String.format(Locale.ENGLISH
                                    , "%.2f"
                                    , tempNewWeight));
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void doSetView(Profile profile){
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        ArrayAdapter<CharSequence> adapterTypeWeight = ArrayAdapter.createFromResource(this,
                R.array.Type_Weight, android.R.layout.simple_spinner_item);
        adapterTypeWeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeWeight.setAdapter(adapterTypeWeight);

        ArrayAdapter<CharSequence> adapterTypeHeight = ArrayAdapter.createFromResource(this,
                R.array.Type_Height, android.R.layout.simple_spinner_item);
        adapterTypeHeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeHeight.setAdapter(adapterTypeHeight);

        if(profile!=null){
            tempOldHeight = profile.getiHeight();
            tempOldWeight = profile.getiWeight();

            Glide.with(this)
                    .load(profile.getUrlAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewAvatar);

            editTextName.setText(profile.getFullName());

            if(profile.getGender().equals("male"))
                spinnerGender.setSelection(0);
            else
                spinnerGender.setSelection(1);

            textViewBirthDay.setText(profile.getDateOfBirth());


            if(profile.isbWeightType()) {
                spinnerTypeWeight.setSelection(1); // Pound
                tempNewWeight =
                        profile.convertPound2Kilogram(tempOldWeight); // prepare for changing
            }
            else {
                spinnerTypeWeight.setSelection(0); // Kg
                tempNewWeight =
                        profile.convertKilogram2Pound(tempOldWeight);
            }
            if(profile.isbHeightType()) {
                spinnerTypeHeight.setSelection(1); // Foot
                tempNewHeight =
                        profile.convertFeet2Cm(tempOldHeight); // prepare for changing
            }else {
                spinnerTypeHeight.setSelection(0); // Cm
                tempNewHeight =
                        profile.convertCm2Feet(tempOldHeight); // prepare for changing
            }

            editTextWeight.setText(String.format(Locale.ENGLISH,"%.2f",profile.getiWeight()));
            editTextHeight.setText(String.format(Locale.ENGLISH,"%.2f",profile.getiHeight()));

            tempTypeWeight = profile.isbWeightType();
            tempTypeHeight = profile.isbHeightType();


        }
    }

    public void doInitControl(){
        editTextName   = findViewById(R.id.nameInput);
        editTextWeight = findViewById(R.id.weightInput);
        editTextHeight = findViewById(R.id.heightInput);

        spinnerGender  = findViewById(R.id.genderSelectorSpinner);
        spinnerTypeWeight = findViewById(R.id.weightTypeSpinner);
        spinnerTypeHeight = findViewById(R.id.heightTypeSpinner);

        textViewBirthDay = findViewById(R.id.dobDate);
        textViewTitle = findViewById(R.id.toolbar_title);

        btnBack = findViewById(R.id.ibBack);
        btnImageAvatar = findViewById(R.id.image_select_avatar);
        btnBirthDay = findViewById(R.id.dobBtn);

        imageViewAvatar = findViewById(R.id.profile_image);

        btnSave = findViewById(R.id.btn_save_next);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            selectedImageUri = data.getData().toString();
            Glide.with(this).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(imageViewAvatar);
        }
    }

}
