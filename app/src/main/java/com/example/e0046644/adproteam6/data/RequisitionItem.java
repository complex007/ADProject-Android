package com.example.e0046644.adproteam6.data;

import android.util.Log;

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e0045042 on 1/31/2017.
 */

public class RequisitionItem  extends java.util.HashMap<String, String> {

    final static String host =  "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";

    public RequisitionItem(String requisitionid, String itemcode, String itemdescription, String quantity, String status)
    {
        put("Requisitionid", requisitionid );
        put("Itemcode", itemcode );
        put("Itemdescription", itemdescription );
        put("Quantity", quantity );
        put("Status", status );


    }

    public RequisitionItem(){}

    public static List<RequisitionItem> findRequisitionItems (String headcode,String token)
    {
        List <RequisitionItem> d = new ArrayList<RequisitionItem>();

        try{
            JSONArray f = JSONParser.getJSONArrayFromUrl(host + "/requisitionitems/" + headcode,token);


            for (int i =0; i<f.length(); i++)
            {
                RequisitionItem g = new RequisitionItem();
                JSONObject jsonObject = f.getJSONObject(i);

                g.put("Requisitionid", Integer.toString(jsonObject.getInt("Requisitionid")));
                g.put("Itemcode", jsonObject.getString("Itemcode"));
                g.put("Itemdescription", jsonObject.getString("Itemdescription"));
                g.put("Quantity", Integer.toString(jsonObject.getInt("Quantity")));
                g.put("Status", Integer.toString(jsonObject.getInt("Status")));
                d.add(g);
            }

        }catch (Exception e){ e.printStackTrace();}

        Log.i("aa", d.toString());
        return d;

    }
    public static void rejectRequisition (String rid,String token)
    {
        String result = JSONParser.postStream(host + "/requisitionitems/reject", rid,token);

    }
    public static void approveRequisition (String [] a,String token)
    {
        String   b = "";

        try
        {
            b= "[\""+a[0]+"\" , \""+a[1]+"\" ]";

        }
        catch (Exception e)
        {e.printStackTrace();}
        Log.i("in",b.toString());
        String result = JSONParser.postStream(host + "/requisitionitems/approve", b,token);

    }
}
