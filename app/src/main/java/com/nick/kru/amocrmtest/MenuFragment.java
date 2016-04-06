package com.nick.kru.amocrmtest;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Kru on 01.04.2016.
 */
public class MenuFragment extends ListFragment {
    private LeadsArrayAdapter adapter;
    private ArrayList<Lead> leadList = new ArrayList<Lead>() ;
    private ArrayList<AccountInfo> accInfoList;

    public void setList(ArrayList <Lead> leadList){
        this.leadList = leadList;
        adapter.addAll(leadList);
        adapter.notifyDataSetChanged();
    }

    public void setAccInfo(ArrayList <AccountInfo> accInfoList){
        this.accInfoList = accInfoList;
    }

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, null) ;
        adapter = new LeadsArrayAdapter(getActivity(), leadList);
        setListAdapter(adapter);
        return view;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        InfoFragment infoFrag = new InfoFragment();
        infoFrag.setContent(leadList.get(position),accInfoList);
        FragmentManager myFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = myFragmentManager
                .beginTransaction();
        fragmentTransaction.addToBackStack("MenuFragment");
        fragmentTransaction.replace(R.id.container, infoFrag);
        fragmentTransaction.commit();

    }
}
