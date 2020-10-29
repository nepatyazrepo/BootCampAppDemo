package com.nepatyaz.appbootcamp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nepatyaz.appbootcamp.R;
import com.nepatyaz.appbootcamp.models.ApiResult;
import com.nepatyaz.appbootcamp.models.Register;
import com.nepatyaz.appbootcamp.utilities.RetrofitUtility;
import com.nepatyaz.appbootcamp.utilities.interfaces.UserApiService;
import com.raywenderlich.android.validatetor.ValidateTor;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private String TAG = "Register Activity";
    private String username, firstName, lastName, email, password, password2;
    private TextInputEditText inputUserName, inputFirstName, inputLastName, inputEmail, inputPassword, inputPassword2;
    private TextInputLayout layoutUsername, layoutFirstName, layoutLastName, layoutEmail, layoutPassword, layoutPassword2;
    private MaterialButton btnRegister;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        retrofit = RetrofitUtility.initializeRetrofit();
    }

    private void initView() {

        inputUserName = findViewById(R.id.input_username);
        inputFirstName = findViewById(R.id.input_firstname);
        inputLastName = findViewById(R.id.input_lastname);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputPassword2 = findViewById(R.id.input_password2);
        btnRegister = findViewById(R.id.btn_register);

        layoutUsername = findViewById(R.id.layout_username);
        layoutFirstName = findViewById(R.id.layout_first_name);
        layoutLastName = findViewById(R.id.layout_last_name);
        layoutEmail = findViewById(R.id.layout_email);
        layoutPassword = findViewById(R.id.layout_password);
        layoutPassword2 = findViewById(R.id.layout_password2);

        btnRegister.setOnClickListener(v -> {
            Log.i(TAG, "onClick: register");
            username = inputUserName.getText().toString();
            firstName = inputFirstName.getText().toString();
            lastName = inputLastName.getText().toString();
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            password2 = inputPassword2.getText().toString();

            checkForm(username, firstName, lastName, email, password, password2);
            setErrorLayout(username, firstName, lastName, email, password, password2);
        });
//
//
//        inputPassword2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                password = inputPassword.getText().toString();
//                password2 = inputPassword2.getText().toString();
//                if (!password2.equals(password)) {
//                    Toast.makeText(RegisterActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private void checkForm(String username, String firstName, String lastName, String email, String password, String password2) {
        ValidateTor validateTor = new ValidateTor();

        if (!validateTor.isAtleastLength(username, 4)) {
            Toasty.warning(this, "Minimal 4 karakter untuk username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validateTor.isEmpty(username) ||
                validateTor.isEmpty(firstName) ||
                validateTor.isEmpty(lastName) ||
                validateTor.isEmpty(email) ||
                validateTor.isEmpty(password) ||
                validateTor.isEmpty(password2)) {
            Toasty.warning(this, "Mohon lengkapi inputan anda", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateTor.isAtleastLength(password, 6)) {
            Toasty.warning(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateTor.isEmail(email)) {
            Toasty.warning(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(password2)) {
            Toasty.warning(RegisterActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateTor.isEmpty(username) ||
                !validateTor.isEmpty(firstName) ||
                !validateTor.isEmpty(lastName) ||
                !validateTor.isEmpty(email) ||
                !validateTor.isEmpty(password) ||
                !validateTor.isEmpty(password2)) {
            Log.i(TAG, "checkForm: OK");
            List<String> role = new ArrayList<>();
            role.add("user");

            Register register = new Register(username, password, email, firstName, lastName, role);
            registerUser(register);
        }


    }

    private void setErrorLayout(String username, String firstName, String lastName, String email, String password, String password2) {
        ValidateTor validateTor = new ValidateTor();

        //set username layout
        if (validateTor.isEmpty(username)) {
            layoutUsername.setError("Mohon Inputkan Username Anda");
        } else {
            layoutUsername.setError(null);
        }

        //set firstname layout
        if (validateTor.isEmpty(firstName)) {
            layoutFirstName.setError("Mohon Inputkan Nama Depan Anda");
        } else {
            layoutFirstName.setError(null);
        }

        //set lastname layout
        if (validateTor.isEmpty(lastName)) {
            layoutLastName.setError("Mohon Inputkan Nama Belakang Anda");
        } else {
            layoutLastName.setError(null);
        }

        //set email layout
        if (validateTor.isEmpty(email)) {
            layoutEmail.setError("Mohon Inputkan Email Anda");
        } else if (!validateTor.isEmail(email)) {
            layoutEmail.setError("Mohon Inputkan Valid Email");
        } else {
            layoutEmail.setError(null);
        }


        //set password layout
        if (validateTor.isEmpty(password)) {
            layoutPassword.setError("Mohon Inputkan Password Anda");
        } else {
            layoutPassword.setError(null);
        }

        //set password2 layout
        if (validateTor.isEmpty(password)) {
            layoutPassword2.setError("Mohon Inputkan Password Anda");
        } else {
            layoutPassword2.setError(null);
        }


    }


    private void registerUser(Register registerBody) {

        UserApiService userApiService = retrofit.create(UserApiService.class);  //instansiasi interfacenya ke retrof it
        Call<ApiResult> result = userApiService.userRegister(registerBody);   // call method interfacenya

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                Log.i(TAG, "onResponse: " + response.body().toString());
                ApiResult apiResult = response.body();

                if (apiResult.isSuccess()) {
                    Toasty.success(RegisterActivity.this, apiResult.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toasty.error(RegisterActivity.this, apiResult.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toasty.error(RegisterActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}