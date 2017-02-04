package com.example.e0046644.adproteam6.clerkactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.RequestDept;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e0046644 on 2/3/2017.
 */

public class StoreClerkList extends Activity implements AdapterView.OnItemClickListener {
    String[] result;
//    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//    final String token=pref.getString("role","")+":"+pref.getString("token","");
     String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token=pref.getString("role","")+":"+pref.getString("token","");
        Log.i("token",token);
        setContentView(R.layout.storeclerklist);
        String[] values = {"Report stock discrepancy", "Confirm order", "Process request"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.scrow, R.id.textView1, values);
        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getAdapter().getItem(position);
        if(position == 0) {
            Intent intent = new Intent(StoreClerkList.this,ReportDiscrepancy1Activity.class);
            this.startActivity(intent);
        } else if (position == 1) {
            new AsyncTask<Void, Void, List<String>>() {
                protected List<String> doInBackground(Void... params) {
                    Log.i("token2",token);
                    List<String> result = Disbursement.listallcollectionpoint(token);
                    return result;
                }

                protected void onPostExecute(List<String> result) {
                    if (result.size()==0)
                    {

                        Toast d=Toast.makeText(StoreClerkList.this,"No Deliveries",Toast.LENGTH_SHORT);
                        d.show();

                    }
                    else
                    {
                        Intent intent2 = new Intent(StoreClerkList.this, DeliverOrder1Activity.class);
                       startActivity(intent2);
                    }

                }
            }.execute();


        } else if (position == 2) {

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
                        Intent intent1 = new Intent(StoreClerkList.this, ProcessRequest1Activity.class);
                        startActivity(intent1);
                    }
                    else
                    {
                        Toast d=Toast.makeText(StoreClerkList.this,"No Requisitions",Toast.LENGTH_SHORT);
                        d.show();
                    }
                }
            }.execute();



        }
    }
}
