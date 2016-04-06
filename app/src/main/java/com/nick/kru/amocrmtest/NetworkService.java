package com.nick.kru.amocrmtest;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Kru on 01.04.2016.
 */
public class NetworkService extends IntentService {

    public static final String commandTypeGetLeads = "GetLeads";
    public static final String commandTypeLogin = "Login";
    public static final String commandTypeGetAccountInfo = "AccInfo";

    CookieManager cookieManager;

    public NetworkService() {
        super("NetworkService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        switch (intent.getAction()) {
            case "Login":
                loginOperation(intent.getStringExtra("Login"), intent.getStringExtra("Password"));
            case "GetLeads":
                getLeads();
                getAccInfo();
                break;
        }
    }

    //Операция включает в себя создание  JSON для логина,передача и получение данных от сервера, парсинг полученного JSON
    void loginOperation(String login, String pass) {
        JSONObject resultJson = new JSONObject();
        try {
            resultJson.put("USER_LOGIN", login);
            resultJson.put("USER_HASH", pass);
            String str = networkMethods(new URL("https://new56fa84a718bd8.amocrm.ru/private/api/auth.php?type=json"),
                    "POST",
                    resultJson.toString());
            parseJson(str, commandTypeLogin);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException f) {
            f.printStackTrace();
        }

    }


    void getLeads() {
        try {
            String str = networkMethods(new URL("https://new56fa84a718bd8.amocrm.ru/private/api/v2/json/leads/list"),
                    "GET",
                    "");
            parseJson(str, commandTypeGetLeads);
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    void getAccInfo() {
        try {
            String str = networkMethods(new URL("https://new56fa84a718bd8.amocrm.ru/private/api/v2/json/accounts/current"),
                    "GET",
                    "");
            parseJson(str, commandTypeGetAccountInfo);
        } catch (IOException f) {
            f.printStackTrace();
        }
    }


    /**
     * @param url
     * @param requestMethod
     * @param strWithJSON   Формирует запрос к серверу (POST/GET методы), сохраняет и добавляет к запросу куки
     *                      Возвращает ответ сервера в виде строки
     */
    String networkMethods(URL url, String requestMethod, String strWithJSON) {
        StringBuffer inputStrBuff = new StringBuffer();
        BufferedReader rd;
        String inputLine = "";
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);

            if (cookieManager.getCookieStore().getCookies().size() > 0) {
                connection.setRequestProperty("Set-Cookie",
                        TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
            }
            connection.connect();

            switch (requestMethod) {
                case "POST":

                    OutputStream os = connection.getOutputStream();
                    os.write(strWithJSON.getBytes());
                    os.flush();

                    if (cookieManager.getCookieStore()
                            .getCookies()
                            .size() == 0) {
                        Map<String, List<String>> headerFields = connection.getHeaderFields();
                        List<String> cookiesHeader = headerFields.get("Set-Cookie");
                        if (cookiesHeader != null) {
                            for (String cookie : cookiesHeader) {
                                cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                            }
                        }
                    }

                    rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    while ((inputLine = rd.readLine()) != null) {
                        inputStrBuff.append(inputLine);
                    }

                    return inputStrBuff.toString();

                case "GET":

                    rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    while ((inputLine = rd.readLine()) != null) {
                        inputStrBuff.append(inputLine);
                    }

                    return inputStrBuff.toString();
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * @param str
     * @param commandType
     * Принимает строку формата json и парсит по указанному типу команды(Login,Get Lead, Get Accoount Info)
     *                    Передает через LocalBroadcastReceiver интенты для выполнения в MainActivity
     */
    void parseJson(String str, String commandType) {
        try {
            Intent intent;
            JSONArray jsonArr;
            JSONObject response = new JSONObject();
            if (str != "") {
                response = new JSONObject(str);
                response = new JSONObject(response.getString("response"));
            } else {
                switch (commandType) {
                    case commandTypeLogin:
                        intent = new Intent("kru.amocrm.LoginFailed");
                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                }

            }
            switch (commandType) {
                case commandTypeGetAccountInfo:
                    response = new JSONObject(response.getString("account"));
                    jsonArr = new JSONArray(response.getString("leads_statuses"));
                    AccountInfo accountInfo;
                    List<AccountInfo> accInfoList = new ArrayList<AccountInfo>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        accountInfo = new AccountInfo(jsonArr.getJSONObject(i).getString("name"),
                                jsonArr.getJSONObject(i).getString("id"),
                                jsonArr.getJSONObject(i).getString("color"),
                                jsonArr.getJSONObject(i).getString("editable"));
                        accInfoList.add(accountInfo);
                    }
                    intent = new Intent("kru.amocrm.GetAccountInfo");
                    intent.putParcelableArrayListExtra("AccInfo", (ArrayList) accInfoList);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                    break;
                case commandTypeGetLeads:
                    jsonArr = new JSONArray(response.getString("leads"));
                    Lead lead;
                    List<Lead> leadList = new ArrayList<Lead>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        lead = new Lead(jsonArr.getJSONObject(i).getString("id"),
                                jsonArr.getJSONObject(i).getString("name"),
                                jsonArr.getJSONObject(i).getString("created_user_id"),
                                Double.valueOf(jsonArr.getJSONObject(i).getString("price")),
                                Long.valueOf(jsonArr.getJSONObject(i).getString("last_modified")),
                                jsonArr.getJSONObject(i).getString("status_id"));
                        leadList.add(lead);
                    }
                    intent = new Intent("kru.amocrm.GetLeads");
                    intent.putParcelableArrayListExtra("Leads", (ArrayList) leadList);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                    break;
                case commandTypeLogin:
                    Intent authResult;
                    if (response.getString("auth") == "true") {
                        authResult = new Intent("kru.amocrm.LoginSuccess");
                    } else {
                        authResult = new Intent("kru.amocrm.LoginFailed");
                    }
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(authResult);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    public IBinder onBind(Intent intent) {

        return null;
    }


}













































