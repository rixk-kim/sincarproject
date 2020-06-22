package com.sincar.customer.sy_rentcar

import com.super_rabbit.wheel_picker.WheelAdapter
import java.text.SimpleDateFormat
import java.util.*

//날짜 넘버피커를 위한 아답터
class WPDayPickerAdapterForJava : WheelAdapter {

    val curDate = Date(System.currentTimeMillis())
    val simpleDateFormat = SimpleDateFormat("MMM d일 (E)")
    val rightNow = Calendar.getInstance()

    override fun getMaxIndex(): Int {
        return 90
    }

    override fun getMinIndex(): Int {
        return 0
    }

    override fun getPosition(vale: String): Int {
        return 0
    }

    override fun getTextWithMaximumLength(): String {
        return "Mmm 00 000"
    }

    override fun getValue(position: Int): String {
        rightNow.time = curDate
        rightNow.add(Calendar.DATE, position)
        return simpleDateFormat.format(rightNow.time)
    }
}