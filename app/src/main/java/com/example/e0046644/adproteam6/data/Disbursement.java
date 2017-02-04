package com.example.e0046644.adproteam6.data;

/**
 * Created by e0046485 on 1/24/2017.
 */

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Disbursement extends java.util.HashMap<String,String> {
    final static String host = "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";

    public Disbursement(String collectionpoint, String Deptcode, String Representativecode, String itemcode, String allocatedquantity, String actualquantity , String Disbursementid ,String Collectiondate) {
        put("Deptcode", Deptcode);
        put("Representativecode", Representativecode);
        put("Disbursementid",Disbursementid);
        put("Collectiondate",Collectiondate);
    }

    public Disbursement() {
    }

    public static List<String> listallcollectionpoint(String token) {
        List<String> list = new ArrayList<>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl((host + "/collectionpoint"),token);
            for (int i = 0; i < a.length(); i++) {
                String c = a.getString(i);
                list.add(c);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Disbursement> ListAllByColloctionpoint(String colpoint,String token) {
        List<Disbursement> list = new ArrayList<Disbursement>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl((host + "/collectionpoint/" + colpoint),token);
            for (int i = 0; i < a.length(); i++) {
                Disbursement dis = new Disbursement();
                JSONObject jsonObject = a.getJSONObject(i);
                dis.put("Deptcode", jsonObject.getString("Deptcode"));
                dis.put("Representativecode", Integer.toString(jsonObject.getInt("Representativecode")));
                dis.put("Disbursementid",Integer.toString(jsonObject.getInt("Disbursementid")));
                list.add(dis);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
