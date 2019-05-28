package com.fajarproject.searchingapp.Controller;

import com.fajarproject.searchingapp.Rest.JSONResponse;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Auth {
    @GET("users")
    Call<JsonElement> data_all_user(@Query("since") String since);

}
