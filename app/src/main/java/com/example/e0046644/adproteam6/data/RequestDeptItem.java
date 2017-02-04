package com.example.e0046644.adproteam6.data;

/**
 * Created by e0046644 on 2/1/2017.
 */

public class RequestDeptItem extends java.util.HashMap<String,String>  {

    final static String host = "http://10.10.2.73/Team6ADProjecttest/adtest/WCF/Service.svc";


    public RequestDeptItem(String deptname,String deptneededquantity, String allocatedquantity,String itemcode)
    {


        put("Deptname", deptname);
        put("Deptneededquantity",deptneededquantity);
        put("Allocatedquantity",allocatedquantity);
        put("Itemcode", itemcode);
    }

    public RequestDeptItem() {
    }



}
