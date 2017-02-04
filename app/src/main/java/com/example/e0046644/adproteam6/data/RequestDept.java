package com.example.e0046644.adproteam6.data;

import android.util.Log;

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e0046644 on 2/1/2017.
 */

public class RequestDept extends java.util.HashMap<String,String>  {
    final static String host = "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";


    public RequestDept(String bin,String itemdescription,String quantityonhand, String requisitionid,String deptname,String deptneededquantity, String allocatedquantity,String itemcode)
    {
        put("Bin", bin);
        put("Itemdescription", itemdescription);
        put("Quantityonhand",quantityonhand);
        put("Requisitionid",requisitionid);

        put("Deptname", deptname);
        put("Deptneededquantity",deptneededquantity);
        put("Allocatedquantity",allocatedquantity);
        put("Itemcode", itemcode);
    }

    public RequestDept() {
    }

    public static List<RequestDept> getrequestdeptstatus2(String item,String token)
    {
        String url="/getrequestdeptstatus2/";
        List<RequestDept> result=getrequestdept(url,item,token);
        return result;
    }
    public static List<RequestDept> getrequestdeptstatus(String item,String token)
    {
        String url="/getrequestdeptstatus/";
        List<RequestDept> result=getrequestdept(url,item,token);
        return result;
    }
    public static List<RequestDept> getrequestdept(String url, String item,String token)
    {
        List<RequestDept> result=new ArrayList<RequestDept>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl((host + url + item),token);
            for (int i = 0; i < a.length(); i++) {
                RequestDept dis = new RequestDept();
                JSONObject jsonObject = a.getJSONObject(i);
                dis.put("Bin", jsonObject.getString("Bin"));
                dis.put("Itemdescription", jsonObject.getString("Itemdescription"));
                dis. put("Quantityonhand",Integer.toString(jsonObject.getInt("Quantityonhand")));
                dis.put("Requisitionid",Integer.toString(jsonObject.getInt("Requisitionid")));

                dis.put("Deptname", jsonObject.getString("Deptname"));
                dis.put("Deptneededquantity",Integer.toString(jsonObject.getInt("Deptneededquantity")));
                dis.put("Allocatedquantity",Integer.toString(jsonObject.getInt("Allocatedquantity")));

                dis.put("Itemcode", jsonObject.getString("Itemcode"));
                result.add(dis);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static  void sendRequestDepts(List<RequestDept> ds,String token){
        ArrayList<JSONObject> objects = new ArrayList<JSONObject>();
        try{
            for (RequestDept i : ds)
            {
                JSONObject  object=new JSONObject();
                object.put("Allocatedquantity",Integer.parseInt(i.get("Allocatedquantity")));
                object.put("Bin",i.get("Bin"));
                object.put("Deptname",i.get("Deptname"));
                object.put("Deptneededquantity",Integer.parseInt(i.get("Deptneededquantity")));
                object.put("Itemcode",i.get("Itemcode"));
                object.put("Itemdescription",i.get("Itemdescription"));
                object.put("Quantityonhand",Integer.parseInt(i.get("Quantityonhand")));
                object.put("Requisitionid",Integer.parseInt(i.get("Requisitionid")));
                Log.i("jsonobject",object.get("Itemcode").toString());
                objects.add(object);
            }
            Log.i("objects",objects.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = JSONParser.postStream(host+ "/getrequestdeptstatus/send",objects.toString(),token);
    }

    public static String[] getuniqueitems(String token) {
        List<String> list = new ArrayList<>();

        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl((host + "/getuniqueitems"),token);
            for (int i = 0; i < a.length(); i++) {
                String c = a.getString(i);
                list.add(c);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] result=new String[list.size()];
        for (int i=0;i<list.size();i++)
        {
            result[i]=list.get(i);
        }
        return result;
    }

    public static String[] getuniqueitems2(String token) {
        List<String> list = new ArrayList<>();

        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl((host + "/getuniqueitems2"),token);
            for (int i = 0; i < a.length(); i++) {
                String c = a.getString(i);
                list.add(c);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] result=new String[list.size()];
        for (int i=0;i<list.size();i++)
        {
            result[i]=list.get(i);
        }
        return result;
    }
}
