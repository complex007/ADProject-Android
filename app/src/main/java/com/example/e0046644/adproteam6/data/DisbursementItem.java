package com.example.e0046644.adproteam6.data;

import android.util.Log;

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e0046485 on 1/25/2017.
 */

public class DisbursementItem extends java.util.HashMap<String,String> {
    final static String host = "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";

    public DisbursementItem(String Actualquantity, String Allocatedquantity, String Disbursementid, String Itemcode,String Supplier1,String Supplier2, String Supplier3) {
        put("Actualquantity", Actualquantity);
        put("Allocatedquantity", Allocatedquantity);
        put("Disbursementid", Disbursementid);
        put("Itemcode", Itemcode);
        put("Supplier1", Supplier1);
        put("Supplier2", Supplier2);
        put("Supplier3", Supplier3);
    }

    public DisbursementItem() {
    }

    public static List<DisbursementItem> FindAllById(String id,String token) {
        List<DisbursementItem> list = new ArrayList<DisbursementItem>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl((host + "/disbursement/" + id),token);
            for (int i = 0; i < a.length(); i++) {
                DisbursementItem di = new DisbursementItem();
                JSONObject jsonObject = a.getJSONObject(i);
                di.put("Itemcode",jsonObject.getString("Itemcode"));
                di.put("Actualquantity",Integer.toString(jsonObject.getInt("Actualquantity")));
                di.put("Allocatedquantity",Integer.toString(jsonObject.getInt("Allocatedquantity")));
                di.put("Disbursementid",Integer.toString( jsonObject.getInt("Disbursementid")));
                di.put("Supplier1",jsonObject.getString("Supplier1"));
                di.put("Supplier2",jsonObject.getString("Supplier2"));
                di.put("Supplier3",jsonObject.getString("Supplier3"));
                list.add(di);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  list;
    }
    public static  void updateDisbursementInformation(List<DisbursementItem> ds,String token){
        ArrayList<JSONObject> objects = new ArrayList<JSONObject>();
        try{
            for (DisbursementItem i : ds)
            {
                JSONObject  object=new JSONObject();
                object.put("Actualquantity",Integer.parseInt(i.get("Actualquantity")));
                object.put("Itemcode",i.get("Itemcode"));
                object.put("Allocatedquantity",Integer.parseInt(i.get("Allocatedquantity")));
                object.put("Disbursementid",Integer.parseInt(i.get("Disbursementid")));
                object.put("Supplier1",i.get("Supplier1"));
                object.put("Supplier2",i.get("Supplier2"));
                object.put("Supplier3",i.get("Supplier3"));
                Log.i("jsonobject",object.get("Itemcode").toString());
                objects.add(object);
            }
            Log.i("objects",objects.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = JSONParser.postStream(host+ "/disbursement/update",objects.toString(),token);
    }
}
