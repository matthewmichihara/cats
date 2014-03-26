package com.chojima;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface ImgurService {

    @Headers("Authorization: Client-ID " + SecretKeys.IMGUR_CLIENT_ID)
    @GET("/3/gallery/r/{subreddit}/time/{page}")
    public void getImages(@Path("subreddit") String subreddit, @Path("page") int page, Callback<ImageSearchResponse> callback);
}
