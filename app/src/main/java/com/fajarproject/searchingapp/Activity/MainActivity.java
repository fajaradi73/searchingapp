package com.fajarproject.searchingapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.fajarproject.searchingapp.Adapter.UserAdapter;
import com.fajarproject.searchingapp.Controller.Auth;
import com.fajarproject.searchingapp.Model.UserModel;
import com.fajarproject.searchingapp.R;
import com.fajarproject.searchingapp.Rest.ApiClient;
import com.fajarproject.searchingapp.Rest.JSONResponse;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<UserModel> userModelList = new ArrayList<>();
    UserModel userModel;
    UserAdapter userAdapter;
    FloatingSearchView searchView;
    RecyclerView rv_user;
    Auth mApiInterface;
    String nama,id,image;
    JSONArray statusobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv_user     = findViewById(R.id.rv_search);
        searchView  = findViewById(R.id.floating_search_view);
        mApiInterface   = ApiClient.getClient().create(Auth.class);

        searchdata();
        rv_user.setVisibility(View.GONE);
        searchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                rv_user.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                rv_user.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFocusCleared() {

            }
        });
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                rv_user.setVisibility(View.VISIBLE);
                userAdapter.getFilter(newQuery).filter(newQuery);
            }
        });
    }

    private void searchdata(){
        Call<JsonElement> call = mApiInterface.data_all_user("500");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("sukses",response.code()+"");
                if (response.isSuccessful()){
                    JsonElement jsonElement = response.body();
                    try {
                        statusobject    = new JSONArray(String.valueOf(jsonElement.getAsJsonArray()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0 ; i < statusobject.length();i++){
                        try {
                            nama    = statusobject.getJSONObject(i).getString("login");
                            id      = String.valueOf(statusobject.getJSONObject(i).getInt("id"));
                            image   = statusobject.getJSONObject(i).getString("avatar_url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        userModel   = new UserModel();
                        userModel.setId(id);
                        userModel.setImage(image);
                        userModel.setNama(nama);
                        userModelList.add(userModel);
                    }
                    userAdapter = new UserAdapter(userModelList,MainActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rv_user.setLayoutManager(layoutManager);
                    rv_user.setAdapter(userAdapter);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("gagal",t.toString());
            }
        });
    }
}
