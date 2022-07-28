package com.example.myapplication

import android.media.Image
import com.example.myapplication.MenuList as MenuList
import com.example.myapplication.BasketObj as BasketObj

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class MenuFragmentKimbap : Fragment(){
    // 리스트 등록
    val MenuClass = MenuList()
    val kimbapMenu = MenuClass.menuData.kimbapList


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceStage: Bundle?): View? {
        val view = inflater.inflate(R.layout.menu_fragment_kimbap, container, false)

        setTableRowLayout(view)
        btnEventShowOtherMenu(view)

        return view
    }


    fun setTableRowLayout(view: View) {
        val tableRowLayoutOne = view.findViewById<TableRow>(R.id.kimbapmenu_tablerow_one)
        val tableRowLayoutTwo = view.findViewById<TableRow>(R.id.kimbapmenu_tablerow_two)
        val tableRowLayoutThree = view.findViewById<TableRow>(R.id.kimbapmenu_tablerow_three)

        for (index in 0 until 4) {
            // 커스텀뷰 설정
            val customView = layoutInflater.inflate(R.layout.view_menu, tableRowLayoutOne, false)
            val customViewImage = customView.findViewById<ImageView>(R.id.menu_image)

            val valueImage = requireContext().resources.getIdentifier(kimbapMenu[index].image, "drawable", requireContext().packageName) // json 데이터의 텍스트로 설정된 이미지 값을 이미지 값으로 변경

            Glide.with(this).load(valueImage).into(customViewImage)
            customView.findViewById<TextView>(R.id.menu_text).text = kimbapMenu[index].name + "\n" + kimbapMenu[index].price.toString()
            customView.findViewById<Button>(R.id.menu_btn).setOnClickListener {
                // 장바구니 데이터클래스 설정
                val basketObj = BasketObj(valueImage, kimbapMenu[index].name, kimbapMenu[index].price)

                val dataInterFace = context as InterfaceBringBasketObj //인터페이스 객체 설정
                dataInterFace.bringBasketObj(basketObj) //값 보내기
            }

            tableRowLayoutOne.addView(customView) //뷰 추가
        }
        for (index in 4 until 8) {
            val customView = layoutInflater.inflate(R.layout.view_menu, tableRowLayoutTwo, false)
            val customViewImage = customView.findViewById<ImageView>(R.id.menu_image)

            val valueImage = requireContext().resources.getIdentifier(kimbapMenu[index].image, "drawable", requireContext().packageName)

            Glide.with(this).load(valueImage).into(customViewImage)
            customView.findViewById<TextView>(R.id.menu_text).text = kimbapMenu[index].name + "\n" + kimbapMenu[index].price.toString()
            customView.findViewById<Button>(R.id.menu_btn).setOnClickListener {
                val basketObj = BasketObj(valueImage, kimbapMenu[index].name, kimbapMenu[index].price)

                val dataInterFace = context as InterfaceBringBasketObj
                dataInterFace.bringBasketObj(basketObj)
            }

            tableRowLayoutTwo.addView(customView)
        }
        for (index in 8 until 12) {
            val customView = layoutInflater.inflate(R.layout.view_menu, tableRowLayoutThree, false)
            val customViewImage = customView.findViewById<ImageView>(R.id.menu_image)

            val valueImage = requireContext().resources.getIdentifier(kimbapMenu[index].image, "drawable", requireContext().packageName)

            Glide.with(this).load(valueImage).into(customViewImage)
            customView.findViewById<TextView>(R.id.menu_text).text = kimbapMenu[index].name + "\n" + kimbapMenu[index].price.toString()
            customView.findViewById<Button>(R.id.menu_btn).setOnClickListener {
                val basketObj = BasketObj(valueImage, kimbapMenu[index].name, kimbapMenu[index].price)

                val dataInterFace = context as InterfaceBringBasketObj
                dataInterFace.bringBasketObj(basketObj)
            }

            tableRowLayoutThree.addView(customView)
        }
    }

    fun btnEventShowOtherMenu(myView: View) {
        val dataInterface = context as InterfaceChangeFragment

        val btnTteokbokki = myView?.findViewById<Button>(R.id.btnTteokbokki)
        btnTteokbokki.setOnClickListener {dataInterface.changeFragmentLogic(2)}
        val btnDonkkaseu = myView?.findViewById<Button>(R.id.btnDonkkaseu)
        btnDonkkaseu.setOnClickListener {dataInterface.changeFragmentLogic(3)}
        val btnLamen = myView?.findViewById<Button>(R.id.btnLamen)
        btnLamen.setOnClickListener {dataInterface.changeFragmentLogic(4)}
    }
}