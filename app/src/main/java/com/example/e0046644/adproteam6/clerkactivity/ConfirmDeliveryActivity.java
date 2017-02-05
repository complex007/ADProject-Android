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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e0046644.adproteam6.MainActivity;
import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.DisbursementItem;
import com.example.e0046644.adproteam6.data.RequestDept;

import java.util.ArrayList;
import java.util.List;

public class ConfirmDeliveryActivity extends Activity {
    ListView items;
    String deptcode;
    String colpoint;
    String token;
    String usercode;
    String input;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmdelivery);
        Log.i("enter", "in");
         pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String role1 = pref.getString("role", "");
        String token1 = pref.getString("token", "");
        if (token1 != null && !token1.equals("") && role1.equals("storeclerk")) {

        token = pref.getString("role", "") + ":" + pref.getString("token", "");
        usercode = pref.getString("usercode", "");
        items = (ListView) findViewById(R.id.list);
        deptcode = getIntent().getExtras().getString("Deptcode");
        colpoint = getIntent().getExtras().getString("Collection");input = deptcode + "," + colpoint;

            refresh();

        Button b =(Button)findViewById(R.id.confirm);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                  List<DisbursementItem> results = getAllValues();
                    boolean result=true;
                    for(DisbursementItem i : results)
                    {
                        int al=Integer.parseInt(i.get("Allocatedquantity"));
                        int ac=Integer.parseInt(i.get("Actualquantity"));
                        int minus=al-ac;

                        if(minus<0)
                        {

                            Toast d=Toast.makeText(ConfirmDeliveryActivity.this,"Incorrect Actualquantity",Toast.LENGTH_SHORT);
                            d.show();
                            result=false;
                            break;
                        }
                        else if(minus>0)
                        {
                            int supplierid=((RadioGroup)findViewById(R.id.RadioGroup)).getCheckedRadioButtonId();
                            String supplier=((RadioButton)findViewById(supplierid)).getText().toString();
                            if(supplier.equals("N/A"))
                            {
                                Toast d=Toast.makeText(ConfirmDeliveryActivity.this,"Insufficient Suppplier Informantion",Toast.LENGTH_SHORT);
                                d.show();
                                result=false;
                                break;
                            }
                        }
                    }
                    if(result)
                    {
                        new AsyncTask<List<DisbursementItem>, Void, Void>(){
                            @Override
                            protected Void doInBackground(List<DisbursementItem>... params) {
                                Log.i("ditem result",params[0].toArray().toString());
                                DisbursementItem.updateDisbursementInformation(params[0],token);
                                return null;
                            }
                            protected  void  onPostExecute(Void result){
                                finish();

                            }
                        }.execute(results);
                    }
                    }
                catch( Exception ex)
                {

                    Toast d=Toast.makeText(ConfirmDeliveryActivity.this,"Incorrect Actualquantity",Toast.LENGTH_SHORT);
                    d.show();
                    refresh();
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuforclerk,menu);
        return true;
    }

    public List<DisbursementItem> getAllValues() {
        View parentView = null;
        List<DisbursementItem> results=new ArrayList<DisbursementItem>();
        for (int i = 0; i < items.getCount(); i++) {
            parentView = getViewByPosition(i, items);
            String actual= ((EditText) parentView
                    .findViewById(R.id.editText)).getText().toString();
            String allocate=((TextView)parentView
                    .findViewById(R.id.textView14)).getText().toString();
            String itemcode=((TextView)parentView
                    .findViewById(R.id.textView2)).getText().toString();
            int supplierid=((RadioGroup)parentView
                    .findViewById(R.id.RadioGroup)).getCheckedRadioButtonId();
            String supplier=((RadioButton)parentView
                    .findViewById(supplierid)).getText().toString();
            String disid=((TextView)parentView
                    .findViewById(R.id.textView10)).getText().toString();

            DisbursementItem item= new DisbursementItem(actual,allocate,disid,itemcode,supplier,usercode,"");
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(ConfirmDeliveryActivity.this,ReportDiscrepancy1Activity.class);
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

                            Toast d=Toast.makeText(ConfirmDeliveryActivity.this,"No Deliveries",Toast.LENGTH_SHORT);
                            d.show();

                        }
                        else
                        {
                            finish();
                            Intent intent2 = new Intent(ConfirmDeliveryActivity.this, DeliverOrder1Activity.class);
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
                            Intent intent1 = new Intent(ConfirmDeliveryActivity.this, ProcessRequest1Activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast d=Toast.makeText(ConfirmDeliveryActivity.this,"No Requisitions",Toast.LENGTH_SHORT);
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
        try
        {


        new AsyncTask<String, Void, List<DisbursementItem>>() {
            protected List<DisbursementItem> doInBackground(String... params) {
                Log.i("getdisburses", params[0].toString());
                List<DisbursementItem> ditems = DisbursementItem.FindAllById(params[0], token);
                Log.i("getdisburses", String.valueOf(ditems.size()));
                return ditems;
            }

            protected void onPostExecute(List<DisbursementItem> itemList) {
                items.setAdapter(new SimpleAdapter(ConfirmDeliveryActivity.this, itemList, R.layout.activity_row_disbursementitem,
                        new String[]{"Disbursementid", "Itemcode", "Allocatedquantity", "Actualquantity", "Supplier1", "Supplier2", "Supplier3"},
                        new int[]{R.id.textView10, R.id.textView2, R.id.textView14, R.id.editText, R.id.radioButton4, R.id.radioButton5, R.id.radioButton6}));
            }
        }.execute(input);
        }
        catch (Exception ex)
        {
            finish();
        }
    }
}
