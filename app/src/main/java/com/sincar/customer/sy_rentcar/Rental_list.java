package com.sincar.customer.sy_rentcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sincar.customer.R;

public class Rental_list extends AppCompatActivity {
    private ConstraintLayout mImagePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list);

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

        btSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] sortType = new String[] {"거리순", "가격순", "인기순"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(Rental_list.this);
                dlg.setTitle("정렬 기준");
                dlg.setItems(sortType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btSort.setText(sortType[which]);
                    }
                });
                dlg.setPositiveButton("취소", null);
                dlg.show();
            }
        });

        mImagePhoto = findViewById(R.id.rent_list_con1);
        mImagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //상세페이지 이동
                Intent intent = new Intent(getApplicationContext(), Rental_list_detail.class);
                startActivity(intent);
            }
        });

    }
}
