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

    //MapsActivity에 날짜,시간(설정에 따라 예약 또는 반납) 데이터를 넘기기위한 클래스
    private var onDateNTimeSetListener: OnDateNTimeSetListener? = null
    //현재 화면이 예약인지 반납인짖 구분 짓기 위한 변수
    private var rCodeCheck: rCodeCheck? = null

    //onDateNTimeSetListener를 사용하기 위한 초기화 메소드
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
        view.findViewById<WheelPicker>(R.id.nPick2).scrollTo(23) //초기화 중 시간 넘버피커의 마지막 아이템으로 이동

        val dateFormat = SimpleDateFormat("MMM d일 (E)")
        val timeFormat = SimpleDateFormat("HH")

        var context: Context? = container!!.context

        //예약,반납 시간 설정을 위한 변수 선언
        var rCode = 0
        var start_dateCheck: String? = null
        var start_timeCheck: String? = null
        var start_timeCheckInt: Int //예약시간 인트화(0 ~ 23)
        var return_dateCheck: String?
        var return_timeCheckInt: Int

        var dateCheck: String? = null
        var timeCheck: Int = 0

        //번들로부터 넘어온 데이터를 변수에 대입
        if (getArguments() != null) {
            start_dateCheck = arguments!!.getString("start_date")
            start_timeCheck = arguments!!.getString("start_time")
            start_timeCheckInt = arguments!!.getInt("start_timeInt")
            return_dateCheck = arguments!!.getString("return_date")
            return_timeCheckInt = arguments!!.getInt("return_timeInt")
            rCode = arguments!!.getInt("reOrRe") //현재 화면이 예약인지 반납인지 구분 짓기 위한 변수
            if (rCode == 1) {
                dateCheck = start_dateCheck
                timeCheck = start_timeCheckInt
            } else if(rCode == 2){
                dateCheck = return_dateCheck
                timeCheck = return_timeCheckInt
            }
            rCodeCheck?.rCodechk(rCode)

            //변수에 대입된 데이터를 Date변수로 변환
            val dateCheck_date = dateFormat.parse(dateCheck)
            val date_now_date = dateFormat.parse(dateFormat.format(curDate))
            //예약날짜와 현재 날짜의 일수 차이를 계산
            val dffday = (dateCheck_date.time - date_now_date.time) / (24 * 60 * 60 * 1000)

            //계산된 수 만큼 날짜 넘버피커의 스크롤 이동(날짜 넘버피커는 초기화시 현재 날짜기준으로 설정됨)
            view.findViewById<WheelPicker>(R.id.nPick1).scrollTo(dffday.toInt())
            //인트화 된 시간으로 스크롤 이동
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

            //예약 프래그먼트일 경우
            if (rCode == 1) {
                if (now_dffdayCheck == 0) {
                    //예약날짜가 현재시간과 같고 예약시간이 현재시간과 같거나 작을 경우 화면 전환 하지 않음
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
                //반납 프래그먼트일 경우
            } else if(rCode == 2) {
                //반납날짜가 예약날짜보다 빠를 경우
                if (resToret_DayCheck < 0) {
                    var strMent = "예약 날짜보다 반납 날짜가 더 빠릅니다.\n예약 시간은 "
                    Toast.makeText(context, strMent + start_dateCheck + start_timeCheck + "입니다", Toast.LENGTH_LONG).show();

                    //반납날짜가 예약날짜와 같을 경우 시간을 비교하여 화면 전환 결정
                } else if ( resToret_DayCheck == 0){
                    if (resToret_TimCheck <= 0) {
                        var strMent = "예약 시간으로부터 최소한 1시간 경과된 시간으로 설정하십시오.\n예약 시간은 "
                        Toast.makeText(context, strMent + start_dateCheck + start_timeCheck + "입니다" , Toast.LENGTH_LONG).show();

                    }
                    else {
                        onDateNTimeSetListener?.onDateNTimePickerSet(date, time, now_dfftimeCheck)
                        (activity as MapsActivity?)!!.replaceFragment(1)
                    }
                } else {
                    onDateNTimeSetListener?.onDateNTimePickerSet(date, time, now_dfftimeCheck)
                    (activity as MapsActivity?)!!.replaceFragment(1)
                }
            }
            rCodeCheck?.rCodechk(0); //메인 프래그먼트로 넘어가므로 rCode를 다시 0으로 전환
        }
        return view
    }
}

//날짜 넘버피커를 위한 아답터
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

//시간 넘버피커를 위한 아답터
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
