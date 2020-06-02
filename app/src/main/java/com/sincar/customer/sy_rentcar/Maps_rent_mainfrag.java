package com.sincar.customer.sy_rentcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sincar.customer.MapsActivity;
import com.sincar.customer.R;
import com.sincar.customer.ReservationAddressActivity;

import java.util.Map;

public class Maps_rent_mainfrag extends Fragment {

    androidx.constraintlayout.widget.ConstraintLayout tvAddress, reserveTime, returnTime;
    TextView tvReserveDate, tvReserveTime, tvReturnDate, tvReturnTime, currentAddress;
    Button btnCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps_rentcar_mainfrag, container, false);

        tvAddress = (androidx.constraintlayout.widget.ConstraintLayout) view.findViewById(R.id.btnReserveAddress);
        reserveTime = (androidx.constraintlayout.widget.ConstraintLayout) view.findViewById(R.id.btnReserveDate);
        returnTime = (androidx.constraintlayout.widget.ConstraintLayout) view.findViewById(R.id.btnReturnDate);
        btnCheck = (Button) view.findViewById(R.id.btnCheck);
        currentAddress = (TextView)view.findViewById(R.id.current_address);

        tvReserveDate = (TextView) view.findViewById(R.id.reserve_date);
        tvReserveTime = (TextView) view.findViewById(R.id.reserve_time);
        tvReturnDate = (TextView) view.findViewById(R.id.return_date);
        tvReturnTime = (TextView) view.findViewById(R.id.return_time);

        if (getArguments() != null) {
            String start_date = getArguments().getString("start_date");
            String start_time = getArguments().getString("start_time");
            String return_date = getArguments().getString("return_date");
            String return_time = getArguments().getString("return_time");
            String curAddress = getArguments().getString("current_Address");
            tvReserveDate.setText(start_date);
            tvReserveTime.setText(start_time);
            tvReturnDate.setText(return_date);
            tvReturnTime.setText(return_time);
            currentAddress.setText(curAddress);
        }

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReservationAddressActivity.class);
                getActivity().startActivityForResult(intent, ((MapsActivity)getActivity()).MAP_REQ_CODE);
            }
        });

        reserveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity) getActivity()).start_reserveDate();
            }
        });

        returnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity) getActivity()).start_returnDate();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Rental_list.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }


    public void AddressChange() {
        String curAddress = getArguments().getString("current_Address");
        if(curAddress != null && currentAddress != null)
            currentAddress.setText(curAddress);
    }
}
