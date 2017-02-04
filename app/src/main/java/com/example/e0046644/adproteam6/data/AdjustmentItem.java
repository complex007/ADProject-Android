package com.example.e0046644.adproteam6.data;

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e0046758 on 1/26/2017.
 */

public class AdjustmentItem extends java.util.HashMap<String,String>  {

    final static String host = "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";
    public AdjustmentItem(){}

    public AdjustmentItem(String suppliercode, String itemcode, String quantity, String reason){
        put("Suppliercode", suppliercode);
        put("Itemcode", itemcode);
        put("Quantity", quantity);
        put("Reason", reason);
    }

    public static void createAdjustment(List<AdjustmentItem> adjitems,String token){
        ArrayList<JSONObject> objects = new ArrayList<JSONObject>();

        try{
            for(AdjustmentItem i : adjitems)
            {
                JSONObject  adjustmentitem = new JSONObject();
                adjustmentitem.put("Itemcode", i.get("Itemcode"));
                adjustmentitem.put("Quantity", Integer.parseInt(i.get("Quantity")));
                adjustmentitem.put("Reason", i.get("Reason"));
                adjustmentitem.put("Suppliercode",i.get("Suppliercode"));
                objects.add(adjustmentitem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = JSONParser.postStream(host+ "/adjustmentitems/create",objects.toString(),token);
    }

}
