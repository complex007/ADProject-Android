package com.example.e0046644.adproteam6.clerkactivity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.e0046644.adproteam6.MainActivity;
import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.RequestDept;

import java.util.ArrayList;
import java.util.List;

public class DeliverOrder1Activity extends ListActivity {

     String token;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String role1 = pref.getString("role", "");
        String token1 = pref.getString("token", "");
        if (token1 != null && !token1.equals("") && role1.equals("storeclerk")) {


        token=pref.getString("role","")+":"+pref.getString("token","");

          refresh();

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

    protected void onListItemClick(ListView l, View v, int position , long id)
    {
        try{
            String item = (String)getListAdapter().getItem(position);
            String newitem = (String)getListAdapter().getItem(position);
            Intent i = new Intent(this,DeliverOrder2Activity.class);
            i.putExtra("colpoint",newitem);
            startActivityForResult(i,566);
        }
        catch(Exception ex)
        {
            refresh();
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==566)
        {
            recreate();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(DeliverOrder1Activity.this,ReportDiscrepancy1Activity.class);
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

                            Toast d=Toast.makeText(DeliverOrder1Activity.this,"No Deliveries",Toast.LENGTH_SHORT);
                            d.show();

                        }
                        else
                        {
                            finish();
                            Intent intent2 = new Intent(DeliverOrder1Activity.this, DeliverOrder1Activity.class);
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
                            Intent intent1 = new Intent(DeliverOrder1Activity.this, ProcessRequest1Activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast d=Toast.makeText(DeliverOrder1Activity.this,"No Requisitions",Toast.LENGTH_SHORT);
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

    public void refresh()
    {
        try {
            new AsyncTask<Void, Void, List<String>>() {
                protected List<String> doInBackground(Void... params) {
                    List<String> result = Disbursement.listallcollectionpoint(token);
                    return result;
                }

                protected void onPostExecute(List<String> result) {
                    if (result.size() == 0) {

                        finish();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listcollectionpoint, R.id.textView11, result);
                        setListAdapter(adapter);
                    }

                }
            }.execute();
        }
            catch(Exception ex)
            {
                finish();
            }
    }
    }


