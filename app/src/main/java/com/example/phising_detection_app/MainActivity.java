package com.example.phising_detection_app;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button tabURL, tabEmail, tabSMS, tabText, btnPaste, btnScan;
    EditText inputText;
    Retrofit retrofit;
    ApiService apiService;

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
        inputText = findViewById(R.id.inputText);

        setActive(tabURL);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        tabURL.setOnClickListener(v -> setActive(tabURL));
        tabEmail.setOnClickListener(v -> setActive(tabEmail));
        tabSMS.setOnClickListener(v -> setActive(tabSMS));
        tabText.setOnClickListener(v -> setActive(tabText));
        btnPaste.setOnClickListener(v -> {});
        btnScan.setOnClickListener(v -> sendToBackend());
    }

    private void setActive(Button selected) {
        List<Button> tabs = Arrays.asList(tabURL, tabEmail, tabSMS, tabText);

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

    private void sendToBackend() {
        Log.d("SCAN", "Scan button clicked!");
        String userInput = inputText.getText().toString().trim();

        if (userInput.isEmpty()) {
            inputText.setError("Please enter text");
            return;
        }

        MessageRequest request = new MessageRequest(userInput);

        apiService.detectPhishing(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("SCAN", "Response received: " + response.code());
                if (!response.isSuccessful() || response.body() == null) {
                    showError("Backend returned empty response");
                    return;
                }

                try {
                    // backend returns stringified JSON
                    JSONObject json = new JSONObject(response.body().response);

                    String result = json.getString("result");
                    String explanation = json.getString("explanation");

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Scan Result")
                            .setMessage("Result: " + result + "\n\nExplanation: " + explanation)
                            .setPositiveButton("OK", null)
                            .show();

                } catch (Exception e) {
                    showError("JSON parsing error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("SCAN", "Error: " + t.getMessage());
                showError("Cannot connect to backend: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
