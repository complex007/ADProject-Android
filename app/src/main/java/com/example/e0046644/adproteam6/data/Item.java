package com.example.e0046644.adproteam6.data;

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONObject;

/**
 * Created by e0046758 on 1/26/2017.
 */

public class Item extends java.util.HashMap<String,String> {

    final static String host="http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";

    public Item(){}

    public Item(String itemcode, String category,String itemdescription,String bin,String quantityonhand,
                String reorderlevel,String reorderquantity, String unitofmeasure, String supplier1, String supplier2, String supplier3, String del){

        put("Itemcode", itemcode);
        put("Category", category);
        put("Itemdescription", itemdescription);
        put("Bin", bin);
        put("Quantityonhand", quantityonhand);
        put("Reorderlevel", reorderlevel);
        put("Reorderquantity", reorderquantity);
        put("Unitofmeasure", unitofmeasure);
        put("Supplier1", supplier1);
        put("Supplier2", supplier2);
        put("Supplier3", supplier3);
        put("Del", del);
    }

    public static Item findItemByItemcode(String itemcode,String token){
        Item item = null;
        try{
            JSONObject i = JSONParser.getJSONFromUrl((host+"/item/"+itemcode),token);
            item = new Item(i.getString("Itemcode"),
                    i.getString("Category"),
                    i.getString("Itemdescription"),
                    i.getString("Bin"),
                    i.getString("Quantityonhand"),
                    i.getString("Reorderlevel"),
                    i.getString("Reorderquantity"),
                    i.getString("Unitofmeasure"),
                    i.getString("Supplier1"),
                    i.getString("Supplier2"),
                    i.getString("Supplier3"),
                    i.getString("Del"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return item;
    }






}
