package com.nick.kru.amocrmtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kru on 04.04.2016.
 */
public class InfoFragment extends Fragment {

    private Lead leadContent;
    private ArrayList<AccountInfo> accInfoList;

    void setContent(Lead content, ArrayList<AccountInfo> accInfoList) {
        this.leadContent = content;
        this.accInfoList = accInfoList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, null);
        StringBuilder str = new StringBuilder();
        str.append("name = " + leadContent.id).append("\n")
                .append("price = " + leadContent.price).append("\n")
                .append("date created = " + new Date(leadContent.lastModified * 1000));
        for (AccountInfo accInf : accInfoList) {
            str.append("\n").append(accInf.name);
        }
        TextView txtView = (TextView) view.findViewById(R.id.LeadInfoView);
        txtView.setText(str);
        return view;
    }


}
