package com.example.e0046644.adproteam6.data;

import com.example.e0046644.adproteam6.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e0046644 on 1/31/2017.
 */

public class Employee extends java.util.HashMap<String,String> {
    final static String host = "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";
    public Employee( String employeecode, String employeename, String employeeemail,String deptcode, String role,String del)
    {
       put("Employeecode",employeecode);
       put("Employeename", employeename);
       put("Employeeemail",employeeemail);
        put("Deptcode",deptcode) ;
        put("Role", role);
        put("Del", del);
    }

    public Employee(){}

    public static String findcurrentRepresentative (String headcode,String token)
    {
        String currentrep = null;
        try
        {
            JSONObject c = JSONParser.getJSONFromUrl(host+ "/employee/currentrepresentative/" + headcode,token);


            currentrep = c.getString("Employeename");

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

        return currentrep;

    }


    public static List<Employee> populateEmployee (String headcode,String token)
    {
        List <Employee> d = new ArrayList<Employee>();

        try {
            JSONArray f = JSONParser.getJSONArrayFromUrl(host + "/employee/" + headcode, token);

            for (int i = 0; i < f.length(); i++) {
                Employee g = new Employee();
                JSONObject jsonObject = f.getJSONObject(i);
                g.put("Employeename", jsonObject.getString("Employeename"));
                g.put("Deptcode", jsonObject.getString("Deptcode"));
                g.put("Employeeemail", jsonObject.getString("Employeeemail"));
                g.put("Employeecode", Integer.toString(jsonObject.getInt("Employeecode")));
                g.put("Role", jsonObject.getString("Role"));
                g.put("Del", Integer.toString(jsonObject.getInt("Del")));
                d.add(g);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return d;
    }

    public static void setRepresentative (Employee newrep,String token)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("Employeename", newrep.get("Employeename"));
            jsonObject.put("Deptcode", newrep.get("Deptcode"));
            jsonObject.put("Employeeemail", newrep.get("Employeeemail"));
            jsonObject.put("Employeecode", Integer.parseInt(newrep.get("Employeecode")));
            jsonObject.put("Role", newrep.get("Role"));
            jsonObject.put("Del", Integer.parseInt(newrep.get("Del")));


        }catch (Exception e)
        {e.printStackTrace();}

        String result = JSONParser.postStream(host + "/employee/setrepresentative", jsonObject.toString(),token);

    }
}
