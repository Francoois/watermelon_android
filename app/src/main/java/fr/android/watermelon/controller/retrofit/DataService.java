package fr.android.watermelon.controller.retrofit;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.android.watermelon.controller.AccessToken;
import fr.android.watermelon.controller.Card;
import fr.android.watermelon.controller.Page;
import fr.android.watermelon.controller.Pay;
import fr.android.watermelon.controller.PayIns;
import fr.android.watermelon.controller.PayOuts;
import fr.android.watermelon.controller.Transfers;
import fr.android.watermelon.controller.User;
import fr.android.watermelon.controller.Wallet;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataService {

    @FormUrlEncoded
    @POST("login")
    Call<AccessToken> postLogin(@Field("email") String email, @Field("password") String password);

    @GET("users")
    Call<List<User>> getUsers(@Header("x-auth-token") String token);

    @FormUrlEncoded
    @POST("users")
    Call<User> postUsers(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("password") String password, @Field("is_admin") boolean is_admin);

    @FormUrlEncoded
    @PUT("users/{id}")
    Call<User> putUsers(@Header("x-auth-token") String token, @Path("id") int id, @Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("password") String password);

    @DELETE("users/{id}")
    Call<ResponseBody> deleteUsers(@Header("x-auth-token") String token, @Path("id") int id);

    @GET("cards")
    Call<List<Card>> getCards(@Header("x-auth-token") String token);

    @FormUrlEncoded
    @POST("cards")
    Call<Card> postCards(@Header("x-auth-token") String token, @Field("user_id") int user_id, @Field("last_4") String last_4, @Field("brand") String brand, @Field("expired_at") String expired_at);

    @DELETE("cards/{id}")
    Call<ResponseBody> deleteCards(@Header("x-auth-token") String token, @Path("id") int id);

    @GET("payins")
    Call<List<PayIns>> getPayIns(@Header("x-auth-token") String token);

    @FormUrlEncoded
    @POST("payins")
    Call<PayIns> postPayIns(@Header("x-auth-token") String token, @Field("wallet_id") int wallet_id, @Field("amount") int amount);

    @GET("payouts")
    Call<List<PayOuts>> getPayOuts(@Header("x-auth-token") String token);

    @FormUrlEncoded
    @POST("payouts")
    Call<PayOuts> postPayOuts(@Header("x-auth-token") String token, @Field("wallet_id") int wallet_id, @Field("amount") int amount);

    @GET("wallets")
    Call<List<Wallet>> getWallets(@Header("x-auth-token") String token);

    @GET("transfers")
    Call<List<Transfers>> getTransfers(@Header("x-auth-token") String token);

    @FormUrlEncoded
    @POST("transfers")
    Call<Transfers> postTransfers(@Header("x-auth-token") String token, @Field("debited_wallet_id") int debited_wallet_id, @Field("credited_wallet_id") int credited_wallet_id, @Field("amount") int amount);

}