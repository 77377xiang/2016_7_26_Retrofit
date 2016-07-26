package com.erhu.code_net;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.erhu.response.UserResponse;
import com.erhu.util.ConstUtil;
import com.erhu.util.MD5Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.task).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                login();
                break;
            case R.id.task:
                getTask();
                break;
        }
    }

    private void login() {
        //死的不用改变
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)//添加打印拦截器
                .connectTimeout(30, TimeUnit.SECONDS)//设置请求超时时间
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .build();
//获得 retrofit并与baseUrl 进行跑拼接，
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstUtil.WEB_URL).client(httpClient).addConverterFactory(GsonConverterFactory.create()).build();
        //  Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstUtil.WEB_URL).addConverterFactory(GsonConverterFactory.create()).build();
        HttpService httpService = retrofit.create(HttpService.class);
        //需要请求的东西
        String account = "sc";
        String passwordKey = MD5Utils.getMD5Str("123456");
        //向服务器发送请求，
        Call<UserResponse> call = httpService.getUserByLogin(account, passwordKey);
        call.enqueue(new Callback<UserResponse>() {
            @Override//请求成功
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                token = response.body().getResult().getJSESSIONID();
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                // Toast.makeText(MainActivity.this, "respone" + response.body().getData().getUser().getImagpath(), Toast.LENGTH_LONG).show();
            }

            @Override//请求失败
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }

    private void getTask() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)//添加打印拦截器
                .connectTimeout(30, TimeUnit.SECONDS)//设置请求超时时间
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstUtil.WEB_URL).client(httpClient).addConverterFactory(GsonConverterFactory.create()).build();
        //  Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstUtil.WEB_URL).addConverterFactory(GsonConverterFactory.create()).build();
        HttpService httpService = retrofit.create(HttpService.class);

        Call<UserResponse> call = httpService.getTaskList("JSESSIONID=" + token, "1", "1", "3", "0");
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                // Toast.makeText(MainActivity.this, "respone" + response.body().getData().getUser().getImagpath(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }
}
