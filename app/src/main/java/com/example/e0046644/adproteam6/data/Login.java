package com.example.e0046644.adproteam6.data;

import android.util.Log;

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by e0046644 on 1/31/2017.
 */

public class Login extends java.util.HashMap<String,String> {
    final static String host = "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";
    public Login(String usercode, String password)
    {
        put("Usercode", usercode);
        put("Password", password);
        //put("Token",token);
    }

    public Login() {
    }

    public static String getLoginInfo(Login i,String token)
    {
        JSONObject object = new JSONObject();
        token="156213579894";
        try {

            object.put("Usercode",Integer.parseInt(i.get("Usercode")));
            object.put("Password",i.get("Password"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("result out",object.toString());
        String result = JSONParser.postStream(host+ "/login",object.toString(),token);

        return result;

    }

}
