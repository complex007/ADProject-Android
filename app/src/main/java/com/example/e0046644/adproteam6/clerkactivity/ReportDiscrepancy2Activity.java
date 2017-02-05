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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.e0046644.adproteam6.MainActivity;
import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.AdjustmentItem;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.RequestDept;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by e0046758 on 1/26/2017.
 */

public class ReportDiscrepancy2Activity extends Activity implements Serializable {

     String token;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_discrepancy);
         pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String role1 = pref.getString("role", "");
        String token1 = pref.getString("token", "");
        if (token1 != null && !token1.equals("") && role1.equals("storeclerk")) {

        token=pref.getString("role","")+":"+pref.getString("token","");
        Bundle extras= getIntent().getExtras( );
        final List<HashMap<String, String>> adjustmentItems =
                (ArrayList<HashMap<String, String>>)extras.getSerializable("adjust");
        ListView lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(new SimpleAdapter(this,adjustmentItems,R.layout.row,
                new String[]{"Itemcode","Suppliercode","Quantity","Reason"},
                new int[]{R.id.textView16,R.id.textView15,R.id.textView17,R.id.textView18}));
        Button report = (Button) findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    List<AdjustmentItem> adjust = new ArrayList<AdjustmentItem>();
                    for (HashMap<String, String> hm : adjustmentItems) {

                        String itemcode = hm.get("Itemcode");
                        String supplier = hm.get("Suppliercode");
                        String quantity = hm.get("Quantity");
                        String reason = hm.get("Reason");
                        AdjustmentItem i = new AdjustmentItem(supplier, itemcode, quantity, reason);
                        adjust.add(i);
                    }
                    Log.i("back", adjust.toString());
                    new AsyncTask<List<AdjustmentItem>, Void, Void>() {
                        @Override
                        protected Void doInBackground(List<AdjustmentItem>... params) {
                            Log.i("back", String.valueOf(params[0].size()));
                            Log.i("typeback", params[0].getClass().getName());
                            Log.i("typeinside", params[0].get(0).toString());
                            AdjustmentItem.createAdjustment(params[0], token);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {

                            finish("success");
                        }
                    }.execute(adjust);
                }
                catch(Exception ex)
                {
                    Toast d = Toast.makeText(ReportDiscrepancy2Activity.this, "Please Try Again.", Toast.LENGTH_SHORT);
                    d.show();
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuforclerk, menu);
        return true;
    }
    public void finish(String suc)
    {
        Intent data=new Intent();
        setResult(RESULT_OK);
        super.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(ReportDiscrepancy2Activity.this,ReportDiscrepancy1Activity.class);
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

                            Toast d=Toast.makeText(ReportDiscrepancy2Activity.this,"No Deliveries",Toast.LENGTH_SHORT);
                            d.show();

                        }
                        else
                        {
                            finish();
                            Intent intent2 = new Intent(ReportDiscrepancy2Activity.this, DeliverOrder1Activity.class);
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
                            Intent intent1 = new Intent(ReportDiscrepancy2Activity.this, ProcessRequest1Activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast d=Toast.makeText(ReportDiscrepancy2Activity.this,"No Requisitions",Toast.LENGTH_SHORT);
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
