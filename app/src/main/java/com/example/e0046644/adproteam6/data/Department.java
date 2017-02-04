package com.example.e0046644.adproteam6.data;

import android.util.Log;

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONArray;

/**
 * Created by E0045042 on 1/26/2017.
 */

public class Department extends java.util.HashMap<String, String>
{
    final static String host =  "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";

    public Department(String deptcode, String deptname, String contactname, String phoneno, String faxno, String collectionpoint, String delegatecode, String startdate, String enddate, String del)
    {
        put("Deptcode", deptcode );
        put("Deptname", deptname );
        put("Contactname", contactname );
        put("Phoneno", phoneno );
        put("Faxno", faxno );
        put("Collectionpoint", collectionpoint );
        put("Delegatecode", delegatecode );
        put("Startdate", startdate );
        put("Enddate", enddate );
        put("Del", del );
    }

    public Department(){}

    public static String findCurrentCollectionPoint (String headcode,String token)
    {
        String currentcollpoint = null;
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(host + "/employee/currentcollectionpoint/" + headcode,token);
            Log.i("colll",String.valueOf(a.length()));

            for (int i = 0; i<a.length(); i++)
            {
                currentcollpoint = a.getString(0);
            }



        }catch(Exception e)
        {
            e.printStackTrace();
        }


            return currentcollpoint;

    }
    public static void updateCollectionPoint (String [] a,String token)
    {
       String   b = "";

        try
        {
            b= "[\""+a[0]+"\" , \""+a[1]+"\" ]";

        }
        catch (Exception e)
        {e.printStackTrace();}
        Log.i("in",b.toString());
        String result = JSONParser.postStream(host + "/employee/updatecollectionpoint", b,token);


    }







}
