package com.example.e0046644.adproteam6.clerkactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e0046644.adproteam6.MainActivity;
import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.AdjustmentItem;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.Item;
import com.example.e0046644.adproteam6.data.RequestDept;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportDiscrepancy1Activity extends Activity {

    List<AdjustmentItem> alist = new ArrayList<AdjustmentItem>();
    String token;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_dis);
         pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String role1 = pref.getString("role", "");
        String token1 = pref.getString("token", "");
        if (token1 != null && !token1.equals("") && role1.equals("storeclerk")) {
        token=pref.getString("role","")+":"+pref.getString("token","");

       final TextView v3 = (TextView) findViewById(R.id.textView10);
        Button seacrch = (Button) findViewById(R.id.button);
        seacrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    EditText itemcode = (EditText) findViewById(R.id.editText1);
                    String icode = itemcode.getText().toString();
                    new AsyncTask<String, Void, Item>() {
                        @Override
                        protected Item doInBackground(String... params) {
                            return Item.findItemByItemcode(params[0],token);
                        }

                        @Override
                        protected void onPostExecute(Item result) {
                            try
                            {
                            TextView v1 = (TextView) findViewById(R.id.textView8);
                            v1.setText(result.get("Category"));
                            TextView v2 = (TextView) findViewById(R.id.textView9);
                            v2.setText(result.get("Itemdescription"));

                            v3.setText(result.get("Quantityonhand"));

                            String[] suppliers = new String[]{result.get("Supplier1"), result.get("Supplier2"), result.get("Supplier3")};
                            ArrayAdapter<String> dataAdapter =
                                    new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, suppliers);
                            Spinner s1 = (Spinner) findViewById(R.id.spinner1);
                            s1.setAdapter(dataAdapter);
                            }
                            catch(Exception ex)
                            {
                                Toast d=Toast.makeText(ReportDiscrepancy1Activity.this,"Incorrect Itemcode",Toast.LENGTH_SHORT);
                                d.show();
                            }

                        }
                    }.execute(icode);


            }
        });

        Button add = (Button) findViewById(R.id.button2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText itemcode = (EditText) findViewById(R.id.editText1);
                    String icode = itemcode.getText().toString();

                    Spinner s1 = (Spinner) findViewById(R.id.spinner1);
                    String suppliercode = s1.getSelectedItem().toString();


                    EditText adjust = (EditText) findViewById(R.id.editText3);
                    EditText reason = (EditText) findViewById(R.id.editText4);
                    String adj = adjust.getText().toString();
                    String rea = reason.getText().toString();
                    int aj = Integer.parseInt(adj);
                    int qtyonhand = Integer.parseInt(v3.getText().toString());
                    if (aj <= 0 && Math.abs(aj) > qtyonhand) {
                        Toast d = Toast.makeText(ReportDiscrepancy1Activity.this, "Incorrect Adjust", Toast.LENGTH_SHORT);
                        d.show();
                    } else if (aj > 10000000) {
                        Toast d = Toast.makeText(ReportDiscrepancy1Activity.this, "Incorrect Adjust", Toast.LENGTH_SHORT);
                        d.show();
                    } else {

                        AdjustmentItem i = new AdjustmentItem();
                        i.put("Itemcode", icode);
                        i.put("Quantity", adj);
                        i.put("Reason", rea);
                        i.put("Suppliercode", suppliercode);

                        if(alist.isEmpty())
                        {
                            alist.add(i);
                        }
                        else
                        {
                            boolean test=true;
                            for (AdjustmentItem j : alist) {
                                if (j.get("Itemcode").equals(icode) && j.get("Suppliercode").equals(suppliercode)) {
                                    int qty = Integer.parseInt(i.get("Quantity"));
                                    int addqty = Integer.parseInt(adj);
                                    int sumsdjqty = qty + addqty;
                                    if (sumsdjqty < 0 && Math.abs(sumsdjqty) > qtyonhand) {
                                        Toast d = Toast.makeText(ReportDiscrepancy1Activity.this, "Incorrect Adjust", Toast.LENGTH_SHORT);
                                        d.show();
                                    }
                                    else
                                    {
                                        j.put("Quantity", String.valueOf(sumsdjqty));
                                        test=false;
                                        break;
                                    }
                                }
                            }
                            if(test)
                            {
                                alist.add(i);
                            }
                        }


                    }
                }catch(Exception ex)
                {
                    Toast d = Toast.makeText(ReportDiscrepancy1Activity.this, "Please Try Again.", Toast.LENGTH_SHORT);
                    d.show();
                }

            }
        });

        Button submit = (Button) findViewById(R.id.button3);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReportDiscrepancy2Activity.class);
                intent.putExtra("adjust", (Serializable) alist);
                startActivityForResult(intent,5667);
            }
        });
    }
    else
    {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        finish();
    }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if(requestCode==5667&&resultCode==RESULT_OK)
        {
           alist.clear();
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuforclerk, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(ReportDiscrepancy1Activity.this,ReportDiscrepancy1Activity.class);
                this.startActivity(intent);
                finish();
                return true;
            case R.id.item2:
                new AsyncTask<Void, Void, List<String>>() {
                    protected List<String> doInBackground(Void... params) {
                        List<String> result = Disbursement.listallcollectionpoint(token);
                        return result;
                    }

                    protected void onPostExecute(List<String> result) {
                        if (result.size()==0)
                        {

                            Toast d=Toast.makeText(ReportDiscrepancy1Activity.this,"No Deliveries",Toast.LENGTH_SHORT);
                            d.show();

                        }
                        else
                        {
                            finish();
                            Intent intent2 = new Intent(ReportDiscrepancy1Activity.this, DeliverOrder1Activity.class);
                            startActivity(intent2);
                        }

                    }
                }.execute();

                return true;
            case R.id.item3:
                new AsyncTask<Void, Void, String[]>() {
                    protected String[] doInBackground(Void... params) {
                        String [] result1= RequestDept.getuniqueitems2(token);
                        String[]  result2=RequestDept.getuniqueitems(token);
                        String[] result;
                        List<String>  resultwrap2=new ArrayList<String>();
                        List<String>  resultwrap1=new ArrayList<String>();
                        for(int i=0;i<result2.length;i++)
                        {
                            List<RequestDept>  requestdeptresult= RequestDept.getrequestdeptstatus(result2[i],token);
                            if(!requestdeptresult.isEmpty())
                            {
                                resultwrap2.add(result2[i]);
                            }

                        }
                        for(int i=0;i<result1.length;i++)
                        {
                            List<RequestDept>  requestdeptresult= RequestDept.getrequestdeptstatus2(result1[i],token);
                            if(!requestdeptresult.isEmpty())
                            {
                                resultwrap1.add(result1[i]);
                            }
                        }



                        if(resultwrap1==null&&resultwrap2!=null)
                        {
                            result=new String[]{"no"};
                        }
                        else
                        {
                            result=new String[]{"have"};
                        }
                        return result;
                    }

                    protected void onPostExecute(String[] result) {
                        if(result[0]=="have")
                        {
                            finish();
                            Intent intent1 = new Intent(ReportDiscrepancy1Activity.this, ProcessRequest1Activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast d=Toast.makeText(ReportDiscrepancy1Activity.this,"No Requisitions",Toast.LENGTH_SHORT);
                            d.show();
                        }
                    }
                }.execute();
                return true;
            case R.id.LogOut:
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



