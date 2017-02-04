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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.RequestDept;

import java.util.ArrayList;
import java.util.List;

public class DeliverOrder2Activity extends ListActivity {
     String colpoint;

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         token=pref.getString("role","")+":"+pref.getString("token","");
        colpoint = getIntent().getExtras().getString("colpoint");
       try{
           refresh();
       }catch(Exception ex)
       {
           refresh();
       }

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuforclerk,menu);
        return true;
    }
    protected void onListItemClick(ListView l, View v, int position , long id)
    {
try {
    Disbursement newitem = (Disbursement) getListAdapter().getItem(position);

    Intent i = new Intent(getApplicationContext(), ConfirmDeliveryActivity.class);

    i.putExtra("Deptcode", newitem.get("Deptcode"));
    i.putExtra("Collection", colpoint);
    startActivityForResult(i, 567);
}catch(Exception ex)
    {
        refresh();
    }
    }
        @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==567)
        {
            recreate();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(DeliverOrder2Activity.this,ReportDiscrepancy1Activity.class);
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

                            Toast d=Toast.makeText(DeliverOrder2Activity.this,"No Deliveries",Toast.LENGTH_SHORT);
                            d.show();

                        }
                        else
                        {
                            finish();
                            Intent intent2 = new Intent(DeliverOrder2Activity.this, DeliverOrder1Activity.class);
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
                            Intent intent1 = new Intent(DeliverOrder2Activity.this, ProcessRequest1Activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast d=Toast.makeText(DeliverOrder2Activity.this,"No Requisitions",Toast.LENGTH_SHORT);
                            d.show();
                        }
                    }
                }.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refresh()
    {
        new AsyncTask<String,Void,List<Disbursement>>(){
            protected List<Disbursement>doInBackground(String...params){
                List<Disbursement> ditem=Disbursement.ListAllByColloctionpoint(params[0],token);
                return ditem;
            }
            protected  void onPostExecute(List<Disbursement> disbursementList){
                if(disbursementList.size()==0)
                {
                    finish();
                }
                else
                {
                    List<Disbursement> uniqitem= new ArrayList<Disbursement>();
                    uniqitem.add(disbursementList.get(0));
                    for(Disbursement i:disbursementList)
                    {
                        String dept1=i.get("Deptcode");
                        for (Disbursement j : uniqitem)
                        {
                            String dept2=j.get("Deptcode");
                            if(!dept1.equals(dept2))
                            {
                                uniqitem.add(i);
                            }
                        }
                    }
                    setListAdapter(new SimpleAdapter(getApplicationContext()
                            ,uniqitem, R.layout.activity_row_disbursement,new String[]{"Deptcode","Representativecode","Disbursementid"},new int[]{R.id.textView,R.id.textView9}));
                }
            }


        }.execute(colpoint);
    }

}
