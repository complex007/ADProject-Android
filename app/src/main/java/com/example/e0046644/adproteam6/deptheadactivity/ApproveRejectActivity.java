package com.example.e0046644.adproteam6.deptheadactivity;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.RequisitionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanje on 1/25/2017.
 */

public class ApproveRejectActivity extends Activity {


    private List<RequisitionItem> reqlist = new ArrayList<RequisitionItem>();
    ListView items;
    String token;
    String headcode;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approve_reject);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        token=pref.getString("role","")+":"+pref.getString("token","");
        headcode=pref.getString("usercode","");
        items = (ListView) findViewById(R.id.list);

        refresh();

        Button reject = (Button) findViewById(R.id.button2);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected Void doInBackground(String...params) {
                            List<String> rid = new ArrayList<String>();

                            for(RequisitionItem e: reqlist) {
                                Log.i("dd", e.get("Requisitionid").toString());
                                if(!rid.contains(e.get("Requisitionid").toString()))
                                {
                                    rid.add(e.get("Requisitionid").toString());
                                    Log.i("ee", rid.toString());
                                }

                            }
                            for(String f: rid)
                            {RequisitionItem.rejectRequisition(f,token);}
                            return null;

                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            Toast t = Toast.makeText(ApproveRejectActivity.this, "Requested Items Rejected", Toast.LENGTH_SHORT);
                            t.show();
                            finish();

                        }
                    }.execute();
                }

                catch (Exception ex)
                {
                    refresh();
                }


            }


        });
        Button approve = (Button) findViewById(R.id.button);
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

try {
    new AsyncTask<String[], Void, Void>() {
        @Override
        protected Void doInBackground(String[]... params) {
            List<String[]> rid = new ArrayList<String[]>();

            for (RequisitionItem e : reqlist) {
                Log.i("approve", e.get("Requisitionid").toString());

                String[] submit = new String[2];
                submit[0] = e.get("Requisitionid");
                submit[1] = headcode;

                if (!rid.contains(submit)) {
                    rid.add(submit);
                    Log.i("ee", rid.toString());
                }

            }
            for (String[] f : rid) {
                RequisitionItem.approveRequisition(f, token);
            }
            //break;
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            Toast t = Toast.makeText(ApproveRejectActivity.this, "Requested Items Approved", Toast.LENGTH_SHORT);
            t.show();
            finish();

        }
    }.execute();
}

            catch (Exception ex)
            {
                refresh();
            }

            }


        });








    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menufordh,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Approve_reject:
                new AsyncTask<String, Void, List<RequisitionItem>>()
                {
                    @Override
                    protected List<RequisitionItem> doInBackground (String...params){
                        reqlist = RequisitionItem.findRequisitionItems(params[0],token);
                        Log.i("cc", reqlist.toString());
                        return reqlist;

                    }
                    @Override
                    protected void onPostExecute (List<RequisitionItem> reqlist)
                    {
                        if (reqlist.size() == 0)
                        {
                            Toast.makeText(ApproveRejectActivity.this, "NO PENDING REQUEST", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            finish();
                            Intent i2 = new Intent(ApproveRejectActivity.this,ApproveRejectActivity.class);
                            startActivity(i2);
                        }
                    }
                }.execute(headcode);


                return true;

            case R.id.AssignRepresentative:
                finish();
                Intent intent1 = new Intent(this, AssignRepresentativeActivity.class);
                this.startActivity(intent1);
                return true;


            case R.id.Collection_Point:
                finish();
                Intent intent2 = new Intent(this, SetCollectionPointActivity.class);
                this.startActivity(intent2);
                return true;


            default:
                return super.onOptionsItemSelected(item);


        }
    }


    public void refresh()
    {
        new AsyncTask<String, Void, List<RequisitionItem>>()
        {
            @Override
            protected List<RequisitionItem> doInBackground (String...params){
                Log.i("aa", params[0]);
                reqlist = RequisitionItem.findRequisitionItems(params[0],token);
                Log.i("cc", reqlist.toString());
                return reqlist;

            }
            @Override
            protected void onPostExecute (List<RequisitionItem> reqlist)
            {
                if (reqlist.size() == 0)
                {

                    Toast.makeText(ApproveRejectActivity.this, "NO PENDING REQUEST", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    items.setAdapter(new SimpleAdapter(ApproveRejectActivity.this, reqlist, R.layout.approve_reject_row, new String[]{"Itemdescription", "Quantity"}, new int[]{R.id.textView5, R.id.textView6}));
                }
            }
        }.execute(headcode);
    }
}
