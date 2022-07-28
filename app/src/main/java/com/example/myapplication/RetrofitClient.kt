package com.example.myapplication

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

object RetrofitClient {  //object로 생성해주면 객체를 생성해주지 않아도 바로 멤버변수로 접근이 가능하다. 하지만,
                         //객체를 데이터로써 사용할 수 없다. (1개보다 더 많이 생성 불가)

    var instance: Retrofit? = null

    // 기본적인 retrofit 설정 ( connect의 역할 )
    fun initRetrofit() : Retrofit {
        if (instance == null) {
            instance = Retrofit
                .Builder()
                .baseUrl("http://3.39.66.6:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return instance!! //
    }
}

// 로그인 및 회원가입 관련 서버로부터 받을 데이터 클래스
data class ResponseFromStartData(
    var message: String,
    var success: Boolean
)
// 카테고리 가져오기
data class CategoryObj(
    var category_name: String
)
data class ResponseFromBringCategory(
    var message: String,
    var success: Boolean,
    var data: List<CategoryObj>
)
//메뉴 가져오기
data class MenuDataObj(
    var menu_name: String,
    var menu_price: Int,
    var menu_image: String
)
data class  ResponseFromMenu(
    var message: String,
    var success: Boolean,
    var data: List<MenuDataObj>
)
//주문 내용 넣기
data class Order(
    var id: String,
    var order_list: List<OrderObj>,
    var total_price: Int
)
data class ResponseFromSendOrder(
    var message: String,
    var success: Boolean
)
//주문 기록 가져오기
data class OrderObj(
    var name: String,
    var count: Int,
    var sum_price: Int
)
data class OrderListObj(
    var order_list: List<OrderObj>,
    var total_price: Int
)
data class ResponseFromBringOrderList(
    var message: String,
    var success: Boolean,
    var data: List<OrderListObj>
)

//백엔드와 접촉
interface RetrofitService {
    @GET("/account/login") fun getResponseFromLogin( //로그인
        @Query("id") id: String,
        @Query("pw") pw: String
    ) : Call<ResponseFromStartData>

    @POST("/account") fun getResponseFromSignup( //회원가입
        @Body signupData: HashMap<String, String>) : Call<ResponseFromStartData>

    @GET("/account/overlap") fun getResponseFromIdOverlap( //아이디 중복체크
        @Query("id") id: String
    ) : Call<ResponseFromStartData>

    @GET("/category") fun getResponseFromCategoryData( //카테고리 가져오기
        @Query("lang") lang: String
    ) : Call<ResponseFromBringCategory>

    @GET("/category/menu") fun getResponseFromMenuData( //메뉴 가져오기
       @Query("category_name") category_name: String,
       @Query("lang") lang: String
    ) : Call<ResponseFromMenu>

    @POST("/order") fun getResponseFromSendOrder(
        @Body data: HashMap<String,Any>
    ) : Call<ResponseFromSendOrder>

    @GET("/order") fun getResponseFromBringOrderList(
        @Query("id") id: String
    ) : Call<ResponseFromBringOrderList>
}
