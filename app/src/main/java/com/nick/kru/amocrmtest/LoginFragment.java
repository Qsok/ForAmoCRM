package com.nick.kru.amocrmtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by Kru on 29.03.2016.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    EditText loginTxt;
    EditText passTxt;


    @Override
    public void onClick(View v) {

        String login = loginTxt.getText().toString();
        String passTxt = "d2acb269a541ba1bdffc7251b2d53d3c";
        Intent intent = new Intent(getActivity().getApplicationContext(), NetworkService.class)
                .setAction("Login")
                .putExtra("Login", login)
                .putExtra("Password", passTxt);
        getActivity().getApplicationContext().startService(intent);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, null);
        loginTxt = (EditText) view.findViewById(R.id.editTxtLgn);
        passTxt = (EditText) view.findViewById(R.id.editTxtPass);
        Button loginBtn = (Button) view.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        return view;
    }
}
