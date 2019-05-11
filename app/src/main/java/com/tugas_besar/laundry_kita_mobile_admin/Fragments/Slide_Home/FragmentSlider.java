package com.tugas_besar.laundry_kita_mobile_admin.Fragments.Slide_Home;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tugas_besar.laundry_kita_mobile_admin.R;

/**
 * Created by bagicode on 12/04/17.
 */

public class FragmentSlider extends Fragment {

    private static final String ARG_PARAM1 = "params";

    private String imageUrl;

    public FragmentSlider() {
    }

    public static FragmentSlider newInstance(String params) {
        FragmentSlider fragment = new FragmentSlider();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageUrl = getArguments().getString(ARG_PARAM1);

        Uri uri = Uri.parse("android.resource://com.tugas_besar.laundry_kita_mobile.Fragments/drawable/"+imageUrl);
        View view = inflater.inflate(R.layout.fragment_slider_item, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.img);
        Glide.with(this.getActivity())
                .load(imageUrl)
                .placeholder(R.drawable.slide1)
                .into(img);
        return view;
    }
}