package com.sincar.customer.sy_rentcar

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.sincar.customer.MapsActivity
import com.sincar.customer.R
import com.super_rabbit.wheel_picker.WheelAdapter
import com.super_rabbit.wheel_picker.WheelPicker
import kotlinx.android.synthetic.main.activity_maps_rentcar_time.*
import java.text.SimpleDateFormat
import java.util.*


val curDate = Date(System.currentTimeMillis())

class Maps_rent_time : Fragment() {
    var btnCheck: Button? = null


    private var onDateNTimeSetListener: OnDateNTimeSetListener? = null
    private var rCodeCheck: rCodeCheck? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDateNTimeSetListener) {
            onDateNTimeSetListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        onDateNTimeSetListener = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_maps_rentcar_time, container, false)
        btnCheck = view.findViewById<View>(R.id.btnCheck) as Button
        view.findViewById<WheelPicker>(R.id.nPick1).setAdapter(WPDayPickerAdapter())
        view.findViewById<WheelPicker>(R.id.nPick2).setAdapter(WPHourPickerAdapter())
        view.findViewById<WheelPicker>(R.id.nPick1).setUnselectedTextColor(R.color.agent_color_1Opp)
        view.findViewById<WheelPicker>(R.id.nPick2).setUnselectedTextColor(R.color.agent_color_1Opp)
        view.findViewById<WheelPicker>(R.id.nPick2).scrollTo(23)

        val dateFormat = SimpleDateFormat("MMM d일 (E)")
        val timeFormat = SimpleDateFormat("HH")

        var context: Context? = container!!.context

        var rCode = 0
        var start_dateCheck: String? = null
        var start_timeCheck: String? = null
        var start_timeCheckInt: Int
        var return_dateCheck: String?
        var return_timeCheckInt: Int

        var dateCheck: String? = null
        var timeCheck: Int = 0

        if (getArguments() != null) {
            start_dateCheck = arguments!!.getString("start_date")
            start_timeCheck = arguments!!.getString("start_time")
            start_timeCheckInt = arguments!!.getInt("start_timeInt")
            return_dateCheck = arguments!!.getString("return_date")
            return_timeCheckInt = arguments!!.getInt("return_timeInt")
            rCode = arguments!!.getInt("reOrRe")
            if (rCode == 1) {
                dateCheck = start_dateCheck
                timeCheck = start_timeCheckInt
            } else if(rCode == 2){
                dateCheck = return_dateCheck
                timeCheck = return_timeCheckInt
            }
            rCodeCheck?.rCodechk(rCode)

            val dateCheck_date = dateFormat.parse(dateCheck)
            val date_now_date = dateFormat.parse(dateFormat.format(curDate))
            val dffday = (dateCheck_date.time - date_now_date.time) / (24 * 60 * 60 * 1000)

            view.findViewById<WheelPicker>(R.id.nPick1).scrollTo(dffday.toInt())
            view.findViewById<WheelPicker>(R.id.nPick2).scrollTo(timeCheck)
        }

        btnCheck!!.setOnClickListener {

            var date = nPick1.getCurrentItem()
            var time = nPick2.getCurrentItem()

            val date_check = dateFormat.parse(date)
            val time_check = timeFormat.parse(time)

            //현재 날짜,시간과 체크된 날짜,시간 차이 비교
            var date_now = dateFormat.parse(dateFormat.format(curDate))
            var now_dffdayCheck = ((date_check.time - date_now.time) / (24 * 60 * 60 * 1000)).toInt()
            var time_now = timeFormat.parse(timeFormat.format(curDate))
            var now_dfftimeCheck = ((time_check.time - time_now.time) / (60 * 60 * 1000)).toInt()


            //예약시간 날짜,시간과 체크된 날짜,시간 차이 비교
            var date_reserve = dateFormat.parse(start_dateCheck)
            var resToret_DayCheck = ((date_check.time - date_reserve.time) / (24 * 60 * 60 * 1000)).toInt()
            var time_reserve = timeFormat.parse(start_timeCheck)
            var resToret_TimCheck = ((time_check.time - time_reserve.time) / (60 * 60 * 1000)).toInt()

            if (rCode == 1) {
                if (now_dffdayCheck == 0) {
                    if (now_dfftimeCheck < 0)
                        Toast.makeText(context, "현재 시간 이후의 시간으로 설정하십시오.", Toast.LENGTH_LONG).show();
                    else {
                        onDateNTimeSetListener?.onDateNTimePickerSet(date, time, now_dfftimeCheck)
                        (activity as MapsActivity?)!!.replaceFragment(1)
                    }
                } else {
                    onDateNTimeSetListener?.onDateNTimePickerSet(date, time, now_dfftimeCheck)
                    (activity as MapsActivity?)!!.replaceFragment(1)
                }
            } else if(rCode == 2) {
                if (resToret_DayCheck < 0) {
                    Toast.makeText(context, "예약 날짜보다 반납 날짜가 더 빠릅니다.", Toast.LENGTH_LONG).show();

                } else if ( resToret_DayCheck == 0){
                    if (resToret_TimCheck <= 0)
                        Toast.makeText(context, "예약 시간으로부터 최소한 1시간 경과된 시간으로 설정하십시오.", Toast.LENGTH_LONG).show();
                    else {
                        onDateNTimeSetListener?.onDateNTimePickerSet(date, time, now_dfftimeCheck)
                        (activity as MapsActivity?)!!.replaceFragment(1)
                    }
                } else {
                    onDateNTimeSetListener?.onDateNTimePickerSet(date, time, now_dfftimeCheck)
                    (activity as MapsActivity?)!!.replaceFragment(1)
                }
            }
            rCodeCheck?.rCodechk(0);
        }
        return view
    }
}

class WPDayPickerAdapter : WheelAdapter {

    val simpleDateFormat = SimpleDateFormat("MMM d일 (E)")
    val rightNow = Calendar.getInstance()

    //get item value based on item position in wheel
    override fun getValue(position: Int): String {
        rightNow.time = curDate;
        rightNow.add(Calendar.DATE, position)
        return simpleDateFormat.format(rightNow.time)
    }

    //get item position based on item string value
    override fun getPosition(vale: String): Int {
        return 0
    }

    //return a string with the approximate longest text width, for supporting WRAP_CONTENT
    override fun getTextWithMaximumLength(): String {
        return "Mmm 00 000"
    }
    override fun getMaxIndex() : Int {
        return 90
    }
    override fun getMinIndex() : Int {
        return 0
    }
}

class WPHourPickerAdapter : WheelAdapter {
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
