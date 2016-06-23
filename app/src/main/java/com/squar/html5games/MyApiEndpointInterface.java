package com.squar.html5games;


import retrofit2.http.GET;
import rx.Observable;

public interface MyApiEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("_ah/api/gameapi/v1/collectionresponse_game")
    Observable<Items> gameList();

}

