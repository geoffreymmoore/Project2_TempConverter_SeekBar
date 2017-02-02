package com.moore.project2_tempconverter_seekbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;


public class TempConverterActivity extends AppCompatActivity
        implements TextView.OnEditorActionListener, View.OnClickListener{

    //define variables for the widgets
    private TextView fahrenheitTV;
    private SeekBar fahrenheitSeekBar;
    private TextView celciusTV;
    private Button resetButton;

    //define the SharedPreferences object
    private SharedPreferences savedValues;

    //define instance variable that should be saved
    private String fahrenheitString = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_converter);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.weather_few_clouds);


        //get references to widgets
        fahrenheitTV = (TextView) findViewById(R.id.fahrenheitTV);
        fahrenheitSeekBar = (SeekBar) findViewById(R.id.fahrenheitSeekBar);
        celciusTV = (TextView) findViewById(R.id.celciusTV);
        resetButton = (Button) findViewById(R.id.resetButton);

        //set the listeners
        resetButton.setOnClickListener(this);
        fahrenheitSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        //get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues",MODE_PRIVATE);
    }

    public void calculateAndDisplay(){

        //get Fahrenheit
        fahrenheitString = fahrenheitTV.getText().toString();
        float fahrenheit;
        float celciusTemp;
        if(fahrenheitString.equals("")){
            fahrenheit = 0;
            fahrenheitTV.setText("0");

        } else {
            fahrenheit = Float.parseFloat(fahrenheitString);
        }

        if(fahrenheit==0){
            celciusTemp = 0;
        }else {
            celciusTemp = (fahrenheit - 32) * 5 / 9;
        }

        if(celciusTemp < 0){
            celciusTV.setTextColor(Color.BLUE);
        }else if (celciusTemp > 0){
            celciusTV.setTextColor(Color.RED);
        }else{
            celciusTV.setTextColor(Color.BLACK);
        }


        //display temperature on layout
        NumberFormat celcius = NumberFormat.getNumberInstance();
        celciusTV.setText(celcius.format(celciusTemp));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.resetButton){
            fahrenheitTV.setText("");
            fahrenheitSeekBar.setProgress(50);
            calculateAndDisplay();
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    protected void onPause() {
        //save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("fahrenheitString",fahrenheitString);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //get the instance variables
        fahrenheitString = savedValues.getString("fahrenheitString","");

        //set the fahrenheit
        fahrenheitTV.setText(fahrenheitString);;


        //calculate and display
        calculateAndDisplay();
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            fahrenheitTV.setText(progress - 50 + "");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (progress - 50 < 0) {
                    fahrenheitSeekBar.getProgressDrawable().setTint(Color.BLUE);
                    fahrenheitTV.setTextColor(Color.BLUE);
                    //fahrenheitSeekBar.setProgressDrawable(Color.BLUE);
                } else if (progress - 50 > 0) {
                    fahrenheitSeekBar.getProgressDrawable().setTint(Color.RED);
                    fahrenheitTV.setTextColor(Color.RED);
                    // fahrenheitSeekBar.setTextColor(Color.RED);
                } else {
                    fahrenheitSeekBar.getProgressDrawable().setTint(Color.BLACK);
                    fahrenheitTV.setTextColor(Color.BLACK);
                    // fahrenheitSeekBar.setTextColor(Color.BLACK);
                }
            } else{
                if (progress - 50 < 0) {

                    fahrenheitTV.setTextColor(Color.BLUE);
                    //fahrenheitSeekBar.setProgressDrawable(Color.BLUE);
                } else if (progress - 50 > 0) {
                    fahrenheitTV.setTextColor(Color.RED);
                    // fahrenheitSeekBar.setTextColor(Color.RED);
                } else {
                    fahrenheitTV.setTextColor(Color.BLACK);
                    // fahrenheitSeekBar.setTextColor(Color.BLACK);
                }
            }


            calculateAndDisplay();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
