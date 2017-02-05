package com.example.e0046644.adproteam6.clerkactivity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.e0046644.adproteam6.MainActivity;
import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.Item;
import com.example.e0046644.adproteam6.data.RequestDept;

import java.util.ArrayList;
import java.util.List;

public class ProcessRequest2Activity extends ListActivity {

    String request;
    String[] resultitem;
     String token;
    String[] result;
    String[] resultowe;
    String[] resultnew;
    SharedPreferences pref;
    Item item;
    Intent i;
    List<RequestDept> requestdeptresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = pref.getString("role", "") + ":" + pref.getString("token", "");
        request = getIntent().getExtras().getString("request");
        resultitem = getIntent().getExtras().getStringArray("requestitem");
        String role1 = pref.getString("role", "");
        String token1 = pref.getString("token", "");
        if (token1 != null && !token1.equals("") && role1.equals("storeclerk")) {
        try {

            refresh(resultitem);

        } catch (Exception ex) {

            switch (request) {
                case "new":

                    refreshnew();
                    break;
                case "owe":
                    refreshowe();
                    break;
                default:
                    break;
            }
        }
    }
        else
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            finish();
        }
    }

    protected void onListItemClick(ListView l, View v, int position , long id)
    {
        try
        {
            item = (Item)getListAdapter().getItem(position);
             i = new Intent(getApplicationContext(),ProcessRequest3Activity.class);
            i.putExtra("item",item.get("Itemcode"));
            i.putExtra("request",request);
            switch(request){
                case  "new":
                    startActivityForResult(i,5667);
                    break;
                case "owe":
                    startActivityForResult(i,5666);
                    break;
                default:
                    break;
            }

        }
        catch(Exception ex)
        {
            switch(request){
                case  "new":

                    refreshnew();
                    break;
                case "owe":
                    refreshowe();
                    break;
                default:
                    break;
            }

        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuforclerk, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {

            switch(requestCode){
                case  5667:
                    refreshnew();
                    break;
                case 5666:
                    Log.i("r7","here");
                    refreshowe();
                    break;
                default:
                    break;
            }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(ProcessRequest2Activity.this,ReportDiscrepancy1Activity.class);
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

                            Toast d=Toast.makeText(ProcessRequest2Activity.this,"No Deliveries",Toast.LENGTH_SHORT);
                            d.show();

                        }
                        else
                        {
                            finish();
                            Intent intent2 = new Intent(ProcessRequest2Activity.this, DeliverOrder1Activity.class);
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
                            Intent intent1 = new Intent(ProcessRequest2Activity.this, ProcessRequest1Activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast d=Toast.makeText(ProcessRequest2Activity.this,"No Requisitions",Toast.LENGTH_SHORT);
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
    public void refresh(String[] initem )
    {

        new AsyncTask<String[], Void, List<Item>>() {
            @Override
            protected List<Item> doInBackground(String[]... params) {
                Log.i("refresh back","here");
                String[] ilist=params[0];
                Log.i("refresh back",String.valueOf(params[0].length));
                Log.i("ilist",String.valueOf(ilist.length));
                List<Item> reitem=new ArrayList<Item>();
                for(int i=0;i<ilist.length;i++)
                {
                    Item it=Item.findItemByItemcode(ilist[i],token);
                    reitem.add(it);
                }
                return reitem;
            }
            @Override
            protected void onPostExecute(List<Item> reitem) {
                if(reitem.size()==0)
                {
                    finish();
                }
                else
                {
                    setListAdapter(new SimpleAdapter(getApplicationContext()
                            ,reitem,R.layout.activity_process_request2,new String[]{"Bin","Itemdescription","Itemcode"},new int[]{R.id.binnum,R.id.des}));
                }
            }
        }.execute(initem);


    }
    public void refreshnew()
    {
try{
        new AsyncTask<Void, Void, List<Item>>() {

            protected List<Item> doInBackground(Void... params) {
                String[] result1 = RequestDept.getuniqueitems(token);
                List<String>  resultwrap=new ArrayList<String>();
                for(int i=0;i<result1.length;i++)
                {
                    List<RequestDept>  requestdeptresult= RequestDept.getrequestdeptstatus(result1[i],token);
                    if(!requestdeptresult.isEmpty())
                    {
                        resultwrap.add(result1[i]);
                    }

                }
                List<Item> reitem=new ArrayList<Item>();
                for(int j=0;j<resultwrap.size();j++)
                {
                    Item it=Item.findItemByItemcode(resultwrap.get(j),token);
                    reitem.add(it);
                }


                return reitem;
            }

            protected void onPostExecute(List<Item> reitem) {
                if(reitem.size()==0)
                {
                    finish();
                }
                else
                {
                    setListAdapter(new SimpleAdapter(getApplicationContext()
                            ,reitem,R.layout.activity_process_request2,new String[]{"Bin","Itemdescription","Itemcode"},new int[]{R.id.binnum,R.id.des}));
                }
            }

        }.execute();
}
catch(Exception ex)
{
    finish();
}
    }
    public void refreshowe()
    {
try{
        new AsyncTask<Void, Void, List<Item>>() {

            protected List<Item> doInBackground(Void... params) {
                String[] result1 = RequestDept.getuniqueitems2(token);
                List<String>  resultwrap=new ArrayList<String>();
                for(int i=0;i<result1.length;i++)
                {
                    List<RequestDept>  requestdeptresult= RequestDept.getrequestdeptstatus2(result1[i],token);
                    if(!requestdeptresult.isEmpty())
                    {
                        resultwrap.add(result1[i]);
                    }

                }
                List<Item> reitem=new ArrayList<Item>();
                for(int j=0;j<resultwrap.size();j++)
                {
                    Item it=Item.findItemByItemcode(resultwrap.get(j),token);
                    reitem.add(it);
                }


                return reitem;
            }

            protected void onPostExecute(List<Item> reitem) {
                if(reitem.size()==0)
                {
                    finish();
                }
                else
                {
                    setListAdapter(new SimpleAdapter(getApplicationContext()
                            ,reitem,R.layout.activity_process_request2,new String[]{"Bin","Itemdescription","Itemcode"},new int[]{R.id.binnum,R.id.des}));
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
