package com.example.myapplication

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class MenuList {

    val jsonData = "{'kimbapList' : [" +
            "{'image' : 'kimbap1', 'name' : '원조김밥', 'price' : 2000}," +
            "{'image' : 'kimbap2', 'name' : '야채김밥', 'price' : 3000}," +
            "{'image' : 'kimbap3', 'name' : '고추김밥', 'price' : 3000}," +
            "{'image' : 'kimbap4', 'name' : '참치김밥', 'price' : 3500}," +
            "{'image' : 'kimbap5', 'name' : '김치김밥', 'price' : 3000}," +
            "{'image' : 'kimbap6', 'name' : '소고기김밥', 'price' : 3500}," +
            "{'image' : 'kimbap7', 'name' : '날치알김밥', 'price' : 3500}," +
            "{'image' : 'kimbap8', 'name' : '돈까스김밥', 'price' : 3500}," +
            "{'image' : 'kimbap9', 'name' : '새우김밥', 'price' : 3500}," +
            "{'image' : 'kimbap10', 'name' : '멸추김밥', 'price' : 3500}," +
            "{'image' : 'kimbap11', 'name' : '제육김밥', 'price' : 3500}," +
            "{'image' : 'kimbap12', 'name' : '모듬김밥', 'price' : 3000}]," +
            "'tteokbokkiList' : [" +
            "{'image' : 'tteokbokki1', 'name' : '떡볶이', 'price' : 3500}," +
            "{'image' : 'tteokbokki2', 'name' : '라볶이', 'price' : 3500}," +
            "{'image' : 'tteokbokki3', 'name' : '쫄볶이', 'price' : 3500}," +
            "{'image' : 'tteokbokki4', 'name' : '치즈라볶이', 'price' : 4000}," +
            "{'image' : 'tteokbokki5', 'name' : '치즈떡볶이', 'price' : 4000}," +
            "{'image' : 'tteokbokki6', 'name' : '치즈쫄볶이', 'price' : 4000}]," +
            "'donkkaseuList' : [" +
            "{'image' : 'donkkaseu1', 'name' : '돈까스', 'price' : 7000}," +
            "{'image' : 'donkkaseu2', 'name' : '치킨까스', 'price' : 7500}," +
            "{'image' : 'donkkaseu3', 'name' : '생선까스', 'price' : 7500}," +
            "{'image' : 'donkkaseu4', 'name' : '새우까스', 'price' : 7500}," +
            "{'image' : 'donkkaseu5', 'name' : '치즈돈까스', 'price' : 8000}," +
            "{'image' : 'donkkaseu6', 'name' : '카레돈까스', 'price' : 7500}," +
            "{'image' : 'donkkaseu7', 'name' : '고구마돈까스', 'price' : 7500}," +
            "{'image' : 'donkkaseu8', 'name' : '갈릭스테이크', 'price' : 7500}]," +
            "'lamenList' : [" +
            "{'image' : 'lamen1', 'name' : '라면', 'price' : 3000}," +
            "{'image' : 'lamen2', 'name' : '떡라면', 'price' : 3500}," +
            "{'image' : 'lamen3', 'name' : '치즈라면', 'price' : 3500}," +
            "{'image' : 'lamen4', 'name' : '만두라면', 'price' : 3500}," +
            "{'image' : 'lamen5', 'name' : '김치라면', 'price' : 3500}," +
            "{'image' : 'lamen6', 'name' : '짬뽕라면', 'price' : 4000}," +
            "{'image' : 'lamen7', 'name' : '콩나물라면', 'price' : 3500}," +
            "{'image' : 'lamen8', 'name' : '짜파구리', 'price' : 3500}] }"

    data class Menu(
        val kimbapList: ArrayList<MenuObj>,
        val tteokbokkiList: ArrayList<MenuObj>, // array 같은 경우에 값을 미리 정해놓는 특징을 가지고 있으므로 어레이리스트로 잡는다.
        val donkkaseuList: ArrayList<MenuObj>,
        val lamenList: ArrayList<MenuObj>
    )

    data class MenuObj(
        val image: String,
        val name: String,
        val price: Int
    )

    val gson = GsonBuilder().setPrettyPrinting().create()

    val menuData = gson.fromJson(jsonData, Menu::class.java)

}





