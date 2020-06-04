package com.sincar.customer.sy_rentcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.sincar.customer.R;

public class Rental_list extends AppCompatActivity {
    private ConstraintLayout mImagePhoto[] = new ConstraintLayout[8];
    int con[] = {R.id.rent_list_con1, R.id.rent_list_con2, R.id.rent_list_con3, R.id.rent_list_con4
    , R.id.rent_list_con5, R.id.rent_list_con6, R.id.rent_list_con7, R.id.rent_list_con8};

    CustomDialog cd;
    int dlgCheck = 0;
    String start_date, start_time, return_date, return_time, curAddress;
    ImageView ivImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list);

        ivImage = (ImageView)findViewById(R.id.rent_list_iv1);
        Glide.with(this).load("https://www.sincar.co.kr/upload/program/goods/list/201912241503214320.jpg")
                .into(ivImage);

        Intent intent = getIntent(); //Maps_rent_mainfrag에서 넘어온 데이터 수신
        start_date = intent.getStringExtra("start_date");
        start_time = intent.getStringExtra("start_time");
        return_date = intent.getStringExtra("return_date");
        return_time = intent.getStringExtra("return_time");
        curAddress = intent.getStringExtra("current_Address");

        ImageButton ibBack = (ImageButton)findViewById(R.id.ibBack1);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btFilter = (Button)findViewById(R.id.btFilter);

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Rental_list_filter.class);
                startActivity(intent);
            }
        });

        final Button btSort = (Button)findViewById(R.id.btn_rentalCar_Sort);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        cd = new CustomDialog(this);
        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();
        wm.copyFrom(cd.getWindow().getAttributes());
        wm.width = width / 5 * 4;


        btSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.setDialogItem(dlgCheck);
                cd.show();
                cd.setDialogResult(new CustomDialog.OnMyDialogResult() {
                    @Override
                    public void finish(int result) {
                        switch(result) {
                            case 0:
                                btSort.setText("거리순");
                                Toast.makeText(getApplicationContext(), "거리순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 0;
                                break;
                            case 1:
                                btSort.setText("가격순");
                                Toast.makeText(getApplicationContext(), "가격순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 1;
                                break;
                            case 2:
                                btSort.setText("인기순");
                                Toast.makeText(getApplicationContext(), "인기순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 2;
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });

        for(int i = 0; i < 8; i++) {
            mImagePhoto[i] = (ConstraintLayout)findViewById(con[i]);

            mImagePhoto[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //상세페이지 이동
                    Intent intent = new Intent(getApplicationContext(), Rental_list_detail.class);
                    intent.putExtra("start_date", start_date);
                    intent.putExtra("start_time", start_time);
                    intent.putExtra("return_date", return_date);
                    intent.putExtra("return_time", return_time);
                    intent.putExtra("current_Address", curAddress);
                    startActivity(intent);
                }
            });
        }
    }
}
