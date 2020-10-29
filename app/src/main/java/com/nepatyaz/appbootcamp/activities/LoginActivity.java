package com.nepatyaz.appbootcamp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nepatyaz.appbootcamp.MainActivity;
import com.nepatyaz.appbootcamp.R;
import com.nepatyaz.appbootcamp.models.ApiResult;
import com.nepatyaz.appbootcamp.models.Login;
import com.nepatyaz.appbootcamp.utilities.Const;
import com.nepatyaz.appbootcamp.utilities.DialogUtility;
import com.nepatyaz.appbootcamp.utilities.interfaces.UserApiService;
import com.raywenderlich.android.validatetor.ValidateTor;

import es.dmoral.toasty.Toasty;
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
    private TextInputEditText inputUsername, inputPassword;
    private Button btnLogin, btnRegister;
    private TextInputLayout layoutUsername, layoutPassword;

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

        layoutUsername = findViewById(R.id.layout_username);
        layoutPassword = findViewById(R.id.layout_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: button login ditekan ");
                userName = inputUsername.getText().toString();
                password = inputPassword.getText().toString();
                Login login = new Login(userName, password);

                validateForm(userName, password, login);


                Log.i(TAG, "isi username : " + userName);
                Log.i(TAG, "isi password : " + password);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
    }


    private void validateForm(String username, String password, Login loginBody) {
        ValidateTor validateTor = new ValidateTor();

        if (validateTor.isEmpty(username)) {
            layoutUsername.setError("Inputkan Username Anda");
            return;
        }

        layoutUsername.setError(null);

        if (validateTor.isEmpty(password)) {
            layoutPassword.setError("Inputkan Password Anda");
            return;
        }

        layoutPassword.setError(null);

        if (!validateTor.isEmpty(username) && !validateTor.isEmpty(password)) {
            sendLogin(loginBody);
        }


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
        DialogUtility.showDialog(R.raw.paperplane, "Loading", LoginActivity.this);

        UserApiService userApiService = retrofit.create(UserApiService.class);  //instansiasi interfacenya ke retrofit
        Call<ApiResult> result = userApiService.userLogin(loginBody);   // call method interfacenya

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult apiResponse = response.body();
                boolean success = apiResponse.isSuccess();
                if (success) {
                    Toasty.success(LoginActivity.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
                    toMainActivity();
                } else {
                    Toasty.error(LoginActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

                DialogUtility.closeAllDialog();
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toasty.error(LoginActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                DialogUtility.closeAllDialog();
            }
        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}