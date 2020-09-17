package com.conagra.hardware.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.conagra.hardware.model.FetalMovementListBakeModel;
import com.conagra.mvp.fragment.Quickening2Fragment;

import java.util.List;


/**
 * Created by yedongqin on 2016/10/21.
 */

public class Quickening2Adapter extends FragmentStatePagerAdapter {

    private List<FetalMovementListBakeModel> fetalMovementList;

    public Quickening2Adapter(FragmentManager fm, List<FetalMovementListBakeModel> fetalMovementList) {
        super(fm);
        this.fetalMovementList = fetalMovementList;
    }

    @Override
    public Fragment getItem(int position) {

        Quickening2Fragment fragment = new Quickening2Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",fetalMovementList.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if(fetalMovementList == null){
            return 0;
        }
        return fetalMovementList.size();
    }
}
