package com.example.phising_detection_app;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/detect_phishing")
    Call<ApiResponse> detectPhishing(@Body MessageRequest request);
}
