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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.RequestDept;
import com.example.e0046644.adproteam6.data.RequestDeptItem;

import java.util.ArrayList;
import java.util.List;

public class ProcessRequest3Activity extends Activity  {
    String item;
    String request;
    List<RequestDept> requestdeptresult;
    ListView items;
   String token;
    int  sumneeded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request3);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token=pref.getString("role","")+":"+pref.getString("token","");
        final TextView totalneeded=(TextView ) findViewById(R.id.qtyneedednum);
        final TextView  qtyonhand=(TextView ) findViewById(R.id.qtyreceivednum);
         items=(ListView)findViewById(R.id.list);

        item=getIntent().getExtras().getString("item");
        Log.i("item",item);
        request=getIntent().getExtras().getString("request");
        Log.i("itemcode",item);

        switch (request)
        {
            case "owe":
                new AsyncTask<String,Void,List<RequestDept>>(){
                    protected List<RequestDept> doInBackground(String...params){
                        Log.i("rede",params[0]);
                        requestdeptresult= RequestDept.getrequestdeptstatus2(params[0],token);
                        Log.i("result",String.valueOf(requestdeptresult.size()));
                        return requestdeptresult;
                    }
                    protected  void onPostExecute(List<RequestDept> result){
                        if(result.size()!=0)
                        {

                            items.setAdapter(new SimpleAdapter(ProcessRequest3Activity.this,result,R.layout.activity_row_process_request3,
                                    new String[]{"Deptname","Deptneededquantity","Allocatedquantity"},new int[]{R.id.dept,R.id.needed,R.id.allocate}));
                            int sumneeded=0;
                            for(RequestDept i : result)
                            {
                                sumneeded+=Integer.parseInt(i.get("Deptneededquantity"));
                            }
                            totalneeded.setText(String.valueOf(sumneeded));
                            String sumhand=result.get(0).get("Quantityonhand");
                            qtyonhand.setText(sumhand);
                        }
                       else
                        {

                            Toast d=Toast.makeText(ProcessRequest3Activity.this,"No Enough Stock",Toast.LENGTH_SHORT);
                            d.show();

                        }
                    }

                }.execute(item);
                break;
            case "new":
                new AsyncTask<String,Void,List<RequestDept>>(){
                    protected List<RequestDept> doInBackground(String...params){
                        requestdeptresult= RequestDept.getrequestdeptstatus(params[0],token);
                        Log.i("result",String.valueOf(requestdeptresult.size()));
                        return requestdeptresult;
                    }
                    protected  void onPostExecute(List<RequestDept> result){
                        items.setAdapter(new SimpleAdapter(ProcessRequest3Activity.this,result,R.layout.activity_row_process_request3,
                                new String[]{"Deptname","Deptneededquantity","Allocatedquantity"},new int[]{R.id.dept,R.id.needed,R.id.allocate}));
                         sumneeded=0;
                        for(RequestDept i : result)
                        {
                            sumneeded+=Integer.parseInt(i.get("Deptneededquantity"));
                        }
                        totalneeded.setText(String.valueOf(sumneeded));
                        String sumhand=result.get(result.size()-1).get("Quantityonhand");
                        qtyonhand.setText(sumhand);
                    }
                }.execute(item);
                break;
            default:
                break;


        }

        Button b =(Button)findViewById(R.id.approverequest);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RequestDeptItem> results = getAllValues();
                boolean result=true;
                int sumal=0;
                int quantityonhand=0;
                int sneed=Integer.parseInt(totalneeded.getText().toString());
                try {


                    for (RequestDeptItem i : results) {
                        int al = Integer.parseInt(i.get("Allocatedquantity"));
                        int dn = Integer.parseInt(i.get("Deptneededquantity"));
                        int minus = dn - al;
                        String imm = requestdeptresult.get(requestdeptresult.size() - 1).get("Quantityonhand");
                        quantityonhand = Integer.parseInt(imm);

                        if (minus < 0 || al > quantityonhand || al < 0) {
                            Toast d = Toast.makeText(ProcessRequest3Activity.this, "Incorrect Allocated", Toast.LENGTH_SHORT);
                            d.show();
                            result = false;
                            break;
                        }
                        sumal += al;
                    }
                    if (sumal > sneed || sumal > quantityonhand) {
                        result = false;
                    }
                    if (result) {
                        for (RequestDeptItem i : results) {
                            for (RequestDept j : requestdeptresult)
                                if (i.get("Deptname") == j.get("Deptname")) {
                                    i.put("Allocatedquantity", j.get("Allocatedquantity"));
                                }
                        }

                        new AsyncTask<List<RequestDept>, Void, Void>() {
                            @Override
                            protected Void doInBackground(List<RequestDept>... params) {
                                Log.i("ritem back", params[0].get(0).toString());
                                RequestDept.sendRequestDepts(params[0], token);
                                Log.i("ritem back out", params[0].get(0).toString());
                                return null;
                            }

                            protected void onPostExecute(Void result) {
                                Log.i("approve", "success");
                                finish();
                            }
                        }.execute(requestdeptresult);
                    } else {
                        Toast d = Toast.makeText(ProcessRequest3Activity.this, "Incorrect Allocated", Toast.LENGTH_SHORT);
                        d.show();
                    }
                }
                catch(Exception  ex)
                {
                    Toast d = Toast.makeText(ProcessRequest3Activity.this, "Incorrect Allocated", Toast.LENGTH_SHORT);
                    d.show();
                }

            }
        });
    }
    public List<RequestDeptItem> getAllValues() {
        View parentView = null;
        List<RequestDeptItem> results=new ArrayList<RequestDeptItem>();
        for (int i = 0; i < items.getCount(); i++) {
            parentView = getViewByPosition(i, items);
            String deptname= ((TextView) parentView
                    .findViewById(R.id.dept)).getText().toString();
            String allocateqty=((EditText)parentView
                    .findViewById(R.id.allocate)).getText().toString();
            String neededqty=((TextView)parentView
                    .findViewById(R.id.needed)).getText().toString();
            RequestDeptItem item= new RequestDeptItem(deptname,neededqty,allocateqty,"");
            results.add(item);
        }
        Log.i("results",results.toString());
        return results;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;
        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
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
                Intent intent = new Intent(ProcessRequest3Activity.this,ReportDiscrepancy1Activity.class);
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

                            Toast d=Toast.makeText(ProcessRequest3Activity.this,"No Deliveries",Toast.LENGTH_SHORT);
                            d.show();

                        }
                        else
                        {
                            finish();
                            Intent intent2 = new Intent(ProcessRequest3Activity.this, DeliverOrder1Activity.class);
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
                            Intent intent1 = new Intent(ProcessRequest3Activity.this, ProcessRequest1Activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast d=Toast.makeText(ProcessRequest3Activity.this,"No Requisitions",Toast.LENGTH_SHORT);
                            d.show();
                        }
                    }
                }.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
