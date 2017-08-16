package com.example.ahmedessam.livehealthysales.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.ahmedessam.livehealthysales.fragments.DemoObjectFragment;
import com.example.ahmedessam.livehealthysales.models.Clinic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed essam on 24/05/2017.
 */

public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
    List<Clinic> clinicList;
    int doctorId;
    public DemoCollectionPagerAdapter(FragmentManager fm,List<Clinic> clinics ,int doctor) {
        super(fm);
        clinicList = new ArrayList<>();
       this.clinicList=clinics;
        this.doctorId = doctor;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DemoObjectFragment();
        Bundle args = new Bundle();
        args.putInt(DemoObjectFragment.ARG_OBJECT, clinicList.get(i).getClinicID());
        args.putInt(DemoObjectFragment.ARG_doctor,doctorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return clinicList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Clinic" + (position + 1);
    }
}
