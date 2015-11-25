package com.test.material.supitsara.materialnavigationtest;

import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by supitsara on 1/11/2558.
 */
public interface ServiceAPI {
    @GET("get_news.php")
    Call<NewsObject[]> getNews();

    class NewsObject {
        public int news_id;
        public String news_thumbnail;
        public String news_headline;
        public String news_body;
        public String start_date;
        public String end_date;
        public String update;
    }

    @GET("get_booth_count.php")
    Call<CountObject[]> getBoothCount();

    class CountObject {
        public String catID;
        public int count;
    }

    @FormUrlEncoded
    @POST("get_booths.php")
    Call<BoothObject[]> sendData(@Field("catID") String catID, @Field("sortBy") String sortBy, @Field("latitude") double latitude, @Field("longitude") double longitude);

    class BoothObject {
        public boolean success;
        public String boothID;
        public String boothName;
        public String boothDescription;
        public String boothDetail;
        public String boothLoc;
        public double boothLat;
        public double boothLong;
        public String boothEmail;
        public String boothTel;
        @SerializedName("boothThumbnail")
        public String thumbnailUrl;
        public double rating;
        public int review;
        public double distance;
    }

    @FormUrlEncoded
    @POST("get_search.php")
    Call<BoothObject[]> search(@Field("keyword") String keyword, @Field("sortBy") String sortBy, @Field("latitude") double latitude, @Field("longitude") double longitude);

    @FormUrlEncoded
    @POST("get_user.php")
    Call<UserObject[]> getUser(@Field("username") String username, @Field("password") String password);

    class UserObject {
        public boolean success;
        public String message;
        @SerializedName("memberID")
        public String id;
        @SerializedName("memberUsername")
        public String username;
        @SerializedName("memberPassword")
        public String password;
        @SerializedName("memberEmail")
        public String email;
        @SerializedName("memberFname")
        public String fname;
        @SerializedName("memberLname")
        public String lname;
        public String profile_img;
        @SerializedName("memberJoindate")
        public String join_date;
    }

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginObject[]> login(@Field("username") String username, @Field("password") String password);

    class LoginObject {
        public boolean success;
        public String id;
        public String message;
    }

    @FormUrlEncoded
    @POST("get_review.php")
    Call<ReviewObject[]> getReview(@Field("boothID") String BoothID);

    class ReviewObject {
        public int review_id;
        public String user_id;
        public String booth_id;
        public int rating;
        public String review_header;
        public String review_body;
        public String review_datetime;
        public String fullname;
        public String profile_img;
    }

    @FormUrlEncoded
    @POST("insert_review.php")
    Call<InsertObject[]> insert(@Field("user_id") String user_id, @Field("booth_id") String booth_id, @Field("star") int star, @Field("title") String title, @Field("body") String body);

    class InsertObject {
        public boolean success;
    }

    @FormUrlEncoded
    @POST("update_review.php")
    Call<UpdateObject[]> update(@Field("user_id") String user_id, @Field("booth_id") String booth_id, @Field("star") int star, @Field("title") String title, @Field("body") String body);

    class UpdateObject {
        public boolean success;
    }

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterObject[]> register(@Field("username") String username, @Field("password") String password, @Field("email") String email, @Field("fname") String fname, @Field("lname") String lname);

    class RegisterObject {
        public boolean success;
        public String message;
        @SerializedName("memberID")
        public String id;
        @SerializedName("memberUsername")
        public String username;
        @SerializedName("memberPassword")
        public String password;
        @SerializedName("memberEmail")
        public String email;
        @SerializedName("memberFname")
        public String fname;
        @SerializedName("memberLname")
        public String lname;
        @SerializedName("memberJoindate")
        public String join_date;
    }

    @FormUrlEncoded
    @POST("get_photo.php")
    Call<PhotoObject[]> getPhoto(@Field("boothID") String BoothID);

    class PhotoObject {
        public int img_ID;
        public String boothID;
        public String imgUrl;
    }

    @Multipart
    @POST("/upload.php")
    Call<String> upload(
            @Part("myfile\"; filename=\"image.png\" ") RequestBody file,
            @Part("description") String description);

}
