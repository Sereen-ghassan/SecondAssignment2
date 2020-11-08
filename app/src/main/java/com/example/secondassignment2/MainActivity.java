package com.example.secondassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Height;
    private EditText Weigth;
    private Spinner Gender;
    private TextView bmiTxt;
    private CheckBox checkBox;
    private Button timerButton;
    private Button bmiButton;


    private static final String  savedName = "Name";
    private static final String  savedHeight = "Height";
    private static final String  savedWeigth = "Weidth";
    private static final String  savedGender = "Gender";
    private static final String  CHECKED = "Checked";
    private static final String  BMI = "BMI";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerButton=findViewById(R.id.button_timer);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onClickTimer();
            }
        });
        bmiButton=findViewById(R.id.button_bmi);
        bmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBmi();
            }
        });
        setupViews();
        setupSharedPrefs();
        checkPrefs();
        populateSpinner();


    }
    private void populateSpinner() {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Female");
        spinnerArray.add("Male");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Gender.setAdapter(adapter);



    }
    @SuppressLint("CommitPrefEdits")
    private void setupSharedPrefs(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor=prefs.edit();
    }
    private void setupViews(){
        Name = findViewById(R.id.name);
        Height = findViewById(R.id.height);
        Weigth = findViewById(R.id.weigth);
        Gender = findViewById(R.id.gender);
        bmiTxt = findViewById(R.id.bmi_txt);
        checkBox = findViewById(R.id.check);

    }
    private void checkPrefs(){
        boolean Check = prefs.getBoolean(CHECKED , false);
        if(Check){
            String name = prefs.getString(savedName,"");
            String height = prefs.getString(savedHeight,"");
            String weidth = prefs.getString(savedWeigth,"");
//            int gender = prefs.getInt(savedGender,0);
            String bmi = prefs.getString(BMI,"");

            Name.setText(name);
            Height.setText(height);
            Weigth.setText(weidth);
//            Gender.setSelection(gender);
            checkBox.setChecked(true);
            bmiTxt.setText(bmi);

        }
    }
    public void calculateBmi(){
        String name = Name.getText().toString();
        String height = Height.getText().toString();
        String weigth = Weigth.getText().toString();
        String gender = Gender.getSelectedItem().toString();
        if (height != null && !"".equals(height) && weigth != null && !"".equals(weigth)){
            float heightValue = Float.parseFloat(height) / 100;
            float weightValue = Float.parseFloat(weigth);

            float bmi = weightValue / (heightValue * heightValue);
            BMIRESULTS(bmi);
        }

        String Bmi = bmiTxt.getText().toString();

        if (checkBox.isChecked()){
            editor.putString(savedName,name);
            editor.putString(savedHeight, String.valueOf(height));
            editor.putString(savedWeigth, String.valueOf(weigth));
            editor.putString(savedGender,gender);
            editor.putString(BMI,Bmi);
            editor.putBoolean(CHECKED,true);
            editor.commit();
        }
    }
    public void BMIRESULTS (float bmi){
        String bmiLabel = "";

        if (  Float.compare(bmi, 18.5f) <= 0) {
            bmiLabel = getString(R.string.underweight);
        } else if (Float.compare(bmi, 18.5f) > 0  &&  Float.compare(bmi, 25f) <= 0) {
            bmiLabel = getString(R.string.normal);
        } else if (Float.compare(bmi, 25f) > 0  &&  Float.compare(bmi, 30f) <= 0) {
            bmiLabel = getString(R.string.overweight);
        } else if (Float.compare(bmi, 30f) > 0 ) {
            bmiLabel = getString(R.string.obese_class_i);
        }

        bmiLabel = bmi + "\n" + bmiLabel;
        bmiTxt.setText(bmiLabel);
    }

    public void onClickTimer() {
        Intent intent = new Intent( this , Timer.class);
        startActivity(intent);
    }
}