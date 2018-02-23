package com.bryanho.paparazzi.services;

import com.bryanho.paparazzi.requests.CreateGameRequest;
import com.bryanho.paparazzi.requests.GetGamesRequest;
import com.bryanho.paparazzi.requests.GetMessagesRequest;
import com.bryanho.paparazzi.requests.JoinGameRequest;
import com.bryanho.paparazzi.requests.LoginRequest;
import com.bryanho.paparazzi.requests.PlayerExistsRequest;
import com.bryanho.paparazzi.requests.RateImageRequest;
import com.bryanho.paparazzi.requests.SendMessageRequest;
import com.bryanho.paparazzi.responses.CreateGameResponse;
import com.bryanho.paparazzi.responses.GamesResponse;
import com.bryanho.paparazzi.responses.GetMessagesResponse;
import com.bryanho.paparazzi.responses.JoinGameResponse;
import com.bryanho.paparazzi.responses.LoginResponse;
import com.bryanho.paparazzi.responses.PlayerExistsResponse;
import com.bryanho.paparazzi.responses.RateImageResponse;
import com.bryanho.paparazzi.responses.SendMessageResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GameService {

    @POST("login")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("playerexists")
    Observable<PlayerExistsResponse> playerExists(@Body PlayerExistsRequest playerExistsRequest);

    @POST("getgames")
    Observable<GamesResponse> getGames(@Body GetGamesRequest getGamesRequest);

    @POST("getmessages")
    Observable<GetMessagesResponse> getMessages(@Body GetMessagesRequest getMessagesRequest);

    @POST("sendmessage")
    Observable<SendMessageResponse> sendMessage(@Body SendMessageRequest message);

    @POST("creategame")
    Observable<CreateGameResponse> createGame(@Body CreateGameRequest createGameRequest);

    @POST("joingame")
    Observable<JoinGameResponse> joinGame(@Body JoinGameRequest joinGameRequest);

    @POST("rateimage")
    Observable<RateImageResponse> rateImage(@Body RateImageRequest rateImageRequest);
}
