package com.example.greenybox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    // add more functions

    /**
     * Called when the user taps the About Team Eleven-Seven button
     *
     * @param view
     * @author SoJung
     */
    public void goToAboutUs(View view) {
        // Do something in response to button
        //Intent intent = new Intent(this, AboutUs.class);
        //startActivity(intent);
    }
}
