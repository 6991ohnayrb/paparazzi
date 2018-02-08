package com.bryanho.paparazzi.services;

import com.bryanho.paparazzi.requests.LoginRequest;
import com.bryanho.paparazzi.responses.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GameService {

    @POST("/login")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);
}
