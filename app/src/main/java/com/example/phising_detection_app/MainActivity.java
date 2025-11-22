package com.example.phising_detection_app;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button tabURL, tabEmail, tabSMS, tabText, btnPaste, btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabURL = findViewById(R.id.tabURL);
        tabEmail = findViewById(R.id.tabEmail);
        tabSMS = findViewById(R.id.tabSMS);
        tabText = findViewById(R.id.tabText);
        btnPaste = findViewById(R.id.btnPaste);
        btnScan = findViewById(R.id.btnScan);


        // Default tab
        setActive(tabURL);
        setActive(btnScan);

        tabURL.setOnClickListener(v -> setActive(tabURL));
        tabEmail.setOnClickListener(v -> setActive(tabEmail));
        tabSMS.setOnClickListener(v -> setActive(tabSMS));
        tabText.setOnClickListener(v -> setActive(tabText));
        btnPaste.setOnClickListener(v->setActive(btnPaste));
        btnScan.setOnClickListener(v->setActive(btnScan));

    }

    private void setActive(Button selected) {
        List<Button> tabs = Arrays.asList(tabURL, tabEmail, tabSMS, tabText,btnPaste, btnScan);

        for (Button b : tabs) {
            if (b == selected) {
                b.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_active));
                b.setTextColor(Color.WHITE);
            } else {
                b.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_inactive));
                b.setTextColor(Color.BLACK);
            }
        }
    }
}
