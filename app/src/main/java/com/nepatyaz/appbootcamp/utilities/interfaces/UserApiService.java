package com.nepatyaz.appbootcamp.utilities.interfaces;

import com.nepatyaz.appbootcamp.models.ApiResult;
import com.nepatyaz.appbootcamp.models.Login;
import com.nepatyaz.appbootcamp.models.Register;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {

    @POST("login")
    Call<ApiResult> userLogin(@Body Login loginBody);

    @POST("register")
    Call<ApiResult> userRegister(@Body Register registerBody);

}
