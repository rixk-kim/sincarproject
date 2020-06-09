package com.sincar.customer.sy_rentcar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sincar.customer.R;

//렌탈 리스트 디테일 액티비티에서 사용된 딜리버리 시스템 선택 다이얼로그
//Custom Dialog, Custom ListView가 적용됨
public class CustomDialogInListDetail extends DialogFragment {

    ListView list;
    Rental_list_detail_deli_listAdapter adapter;
    OnMySpinnerDialogResult mDialogResult; //다이얼로그 종료시 선택된 아이템의 정보를 전달하기 위한 클래스
    TextView tvCancel;
    int dlgListCheck; //선택된 아이템 체크를 위한 변수

    public CustomDialogInListDetail() {
    }

    public static CustomDialogInListDetail getInstance() {
        CustomDialogInListDetail e = new CustomDialogInListDetail();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_dialog_in_list_detail, container);
        //다이얼로그의 테두리를 둥글게 만들기 위한 background 설정
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        list = (ListView) v.findViewById(R.id.rental_list_deli_list);
        tvCancel = (TextView) v.findViewById(R.id.list_deli_cancel);
        //다이얼로그의 화면 밖을 클릭 해도 다이얼로그 닫힘 금지 설정
        setCancelable(false);

        //선택된 아이템 체크 변수
        if(getArguments() != null)
            dlgListCheck = getArguments().getInt("dlgSpinnerCheck");

//        ArrayList<String> dlivery_title = new ArrayList<>();
//        dlivery_title.add("지점방문"); //ArrayList에 내가 스피너에 보여주고싶은 값 셋팅
//        dlivery_title.add("왕복 딜리버리");
//        dlivery_title.add("배차시 딜리버리");
//        dlivery_title.add("반납시 딜리버리");

//        spinner.setPrompt("딜리버리");
        //커스텀 리스트뷰아답터
        adapter = new Rental_list_detail_deli_listAdapter();
        adapter.addItem("지점방문", getResources().getDrawable(R.drawable.check_red));
        adapter.addItem("왕복 딜리버리", getResources().getDrawable(R.drawable.check_red));
        adapter.addItem("배차시 딜리버리", getResources().getDrawable(R.drawable.check_red));
        adapter.addItem("반납시 딜리버리", getResources().getDrawable(R.drawable.check_red));

        list.setAdapter(adapter); //어댑터연결
        list.setItemChecked(dlgListCheck, true);   //선택값 지정

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Rental_list_detail_deli_listview_item item = (Rental_list_detail_deli_listview_item) parent.getItemAtPosition(position);

                //선택된 아이템 정보를 DialogResult메소드에 전달 후 다이얼로그 종료
                if(mDialogResult != null) {
                    mDialogResult.finish(position);
                    dismiss();
                }
            }
        });

        //취소 버튼
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    //다이얼로그 종료후 선택된 아이템 정보를 전달하는 DialogResult를 위한 메소드
    public void setDialogResult(OnMySpinnerDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMySpinnerDialogResult {
        void finish(int result);
    }
}
