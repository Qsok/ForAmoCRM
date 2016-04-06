package com.nick.kru.amocrmtest;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FragmentManager myFragmentManager;
    FragmentTransaction fragmentTransaction;
    MenuFragment menuFrag = new MenuFragment();

    /**
     * Обработка интентов из NetworkService после успешных/неуспешных запросов к серверу
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "kru.amocrm.LoginSuccess":
                    fragmentTransaction = myFragmentManager
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.container, menuFrag, "MenuFragment");
                    fragmentTransaction.commit();
                    break;
                case "kru.amocrm.LoginFailed":
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Login Error!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case "kru.amocrm.GetLeads":
                    Bundle leadData = intent.getExtras();
                    ArrayList<Lead> leadList = (ArrayList) leadData.getParcelableArrayList("Leads");
                    menuFrag.setList(leadList);
                    break;
                case "kru.amocrm.GetAccountInfo":
                    Bundle accountData = intent.getExtras();
                    ArrayList<AccountInfo> accInfoList = (ArrayList) accountData.getParcelableArrayList("AccInfo");
                    menuFrag.setAccInfo(accInfoList);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFragmentManager = getSupportFragmentManager();
        fragmentTransaction = myFragmentManager
                .beginTransaction();

        LoginFragment loginFrag = new LoginFragment();
        fragmentTransaction.add(R.id.container, loginFrag, "LOGIN_FRAG");
        fragmentTransaction.commit();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("kru.amocrm.LoginSuccess");
        intentFilter.addAction("kru.amocrm.LoginFailed");
        intentFilter.addAction("kru.amocrm.GetLeads");
        intentFilter.addAction("kru.amocrm.GetAccountInfo");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);

    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

}
