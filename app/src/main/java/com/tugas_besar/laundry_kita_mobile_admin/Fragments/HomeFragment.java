package com.tugas_besar.laundry_kita_mobile_admin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tugas_besar.laundry_kita_mobile_admin.Activities.JasActivity;
import com.tugas_besar.laundry_kita_mobile_admin.Activities.KarpetActivity;
import com.tugas_besar.laundry_kita_mobile_admin.Activities.KiloanActivity;
import com.tugas_besar.laundry_kita_mobile_admin.Activities.SepatuActivity;
import com.tugas_besar.laundry_kita_mobile_admin.Activities.TasActivity;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.Slide_Home.FragmentSlider;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.Slide_Home.SliderIndicator;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.Slide_Home.SliderPagerAdapter;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.Slide_Home.SliderView;
import com.tugas_besar.laundry_kita_mobile_admin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bagicode on 12/04/17.
 */

public class HomeFragment extends Fragment {

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;
    private LinearLayout menu_tas, menu_sepatu, menu_jas, menu_karpet, menu_kiloan;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView   = inflater.inflate(R.layout.fragment_home, container, false);
        sliderView      = (SliderView) rootView.findViewById(R.id.sliderView);
        mLinearLayout   = (LinearLayout) rootView.findViewById(R.id.pagesContainer);
        menu_tas        = (LinearLayout) rootView.findViewById(R.id.home_tas);
        menu_sepatu     = (LinearLayout) rootView.findViewById(R.id.home_sepatu);
        menu_jas        = (LinearLayout) rootView.findViewById(R.id.home_jas);
        menu_karpet     = (LinearLayout) rootView.findViewById(R.id.home_karpet);
        menu_kiloan     = (LinearLayout) rootView.findViewById(R.id.home_kiloan);


        menu_tas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "klik me", Toast.LENGTH_SHORT).show();


                Intent goPindah = new Intent(getActivity(), TasActivity.class);
                startActivity(goPindah);

            }
        });

        menu_sepatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "klik me", Toast.LENGTH_SHORT).show();


                Intent goPindah = new Intent(getActivity(), SepatuActivity.class);
                startActivity(goPindah);

            }
        });

        menu_jas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "klik me", Toast.LENGTH_SHORT).show();


                Intent goPindah = new Intent(getActivity(), JasActivity.class);
                startActivity(goPindah);

            }
        });
        menu_karpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "klik me", Toast.LENGTH_SHORT).show();


                Intent goPindah = new Intent(getActivity(), KarpetActivity.class);
                startActivity(goPindah);

            }
        });

        menu_kiloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "klik me", Toast.LENGTH_SHORT).show();


                Intent goPindah = new Intent(getActivity(), KiloanActivity.class);
                startActivity(goPindah);

            }
        });

        setupSlider();

        //jika internet aktif
        setupSliderOfLine();

        return rootView;
    }
    private void setupSlider() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-1.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-2.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-3.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-4.jpg"));

        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }

    private void setupSliderOfLine () {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("slide1"));
        fragments.add(FragmentSlider.newInstance("slide2"));
        fragments.add(FragmentSlider.newInstance("slide1"));
        fragments.add(FragmentSlider.newInstance("slide2"));

        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }
}