package fr.android.watermelon.controller.retrofit;

import java.util.List;

import fr.android.watermelon.controller.AccessToken;
import fr.android.watermelon.controller.Page;
import fr.android.watermelon.controller.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DataService {

    @GET("home")
    Call<List<Page>> getHome();

    @GET("users")
    Call<List<User>> getUsers(@Header("x-auth-token") String token);

    @FormUrlEncoded
    @POST("users")
    Call<User> postUsers(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("password") String password, @Field("is_admin") boolean is_admin);

    @FormUrlEncoded
    @PUT("users")
    Call<User> putUsers(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("password") String password, @Field("is_admin") boolean is_admin);


    @FormUrlEncoded
    @POST("login")
    Call<AccessToken> postLogin(@Field("email") String email, @Field("password") String password);
}