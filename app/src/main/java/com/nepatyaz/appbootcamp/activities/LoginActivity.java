package com.nepatyaz.appbootcamp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nepatyaz.appbootcamp.MainActivity;
import com.nepatyaz.appbootcamp.R;
import com.nepatyaz.appbootcamp.models.ApiResult;
import com.nepatyaz.appbootcamp.models.Login;
import com.nepatyaz.appbootcamp.utilities.Const;
import com.nepatyaz.appbootcamp.utilities.interfaces.UserApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";
    private String userName, password;
    private EditText inputUsername, inputPassword;
    private Button btnLogin, btnRegister;

    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initRetrofit();
    }

    private void initView() {
        inputUsername = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: button login ditekan ");
                userName = inputUsername.getText().toString();
                password = inputPassword.getText().toString();
                Login login = new Login(userName, password);
                sendLogin(login);

                Log.i(TAG, "isi username : " + userName);
                Log.i(TAG, "isi password : " + password);
            }
        });
    }

    private void initRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(); //intercept semua log http
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private void sendLogin(Login loginBody) {
        UserApiService userApiService = retrofit.create(UserApiService.class);  //instansiasi interfacenya ke retrofit
        Call<ApiResult> result = userApiService.userLogin(loginBody);   // call method interfacenya

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult apiResponse = response.body();
                boolean success = apiResponse.isSuccess();
                if (success) {
                    Toast.makeText(LoginActivity.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
                    toMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void toMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }




}