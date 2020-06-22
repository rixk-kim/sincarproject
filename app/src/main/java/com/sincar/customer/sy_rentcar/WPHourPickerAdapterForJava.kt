package com.sincar.customer.sy_rentcar

import com.super_rabbit.wheel_picker.WheelAdapter

//시간 넘버피커를 위한 아답터
class WPHourPickerAdapterForJava : WheelAdapter {
    override fun getValue(position: Int): String {
        return when (position) {
            0 -> "00:00"
            1 -> "01:00"
            2 -> "02:00"
            3 -> "03:00"
            4 -> "04:00"
            5 -> "05:00"
            6 -> "06:00"
            7 -> "07:00"
            8 -> "08:00"
            9 -> "09:00"
            10 -> "10:00"
            11 -> "11:00"
            12 -> "12:00"
            13 -> "13:00"
            14 -> "14:00"
            15 -> "15:00"
            16 -> "16:00"
            17 -> "17:00"
            18 -> "18:00"
            19 -> "19:00"
            20 -> "20:00"
            21 -> "21:00"
            22 -> "22:00"
            23 -> "23:00"
            else -> ""
        }
    }

    override fun getPosition(vale: String): Int {
        return when (vale) {
            "00:00" -> 0
            "01:00" -> 1
            "02:00" -> 2
            "03:00" -> 3
            "04:00" -> 4
            "05:00" -> 5
            "06:00" -> 6
            "07:00" -> 7
            "08:00" -> 8
            "09:00" -> 9
            "10:00" -> 10
            "11:00" -> 11
            "12:00" -> 12
            "13:00" -> 13
            "14:00" -> 14
            "15:00" -> 15
            "16:00" -> 16
            "17:00" -> 17
            "18:00" -> 18
            "19:00" -> 19
            "20:00" -> 20
            "21:00" -> 21
            "22:00" -> 22
            "23:00" -> 23
            else -> 0
        }
    }

    override fun getTextWithMaximumLength(): String {
        return "00"
    }

    override fun getMaxIndex(): Int {
        return 23
    }

    override fun getMinIndex(): Int {
        return 0
    }
}
