package com.example.e0046644.adproteam6.clerkactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e0046644.adproteam6.MainActivity;
import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Disbursement;
import com.example.e0046644.adproteam6.data.RequestDept;

import java.util.ArrayList;
import java.util.List;

public class ProcessRequest1Activity extends Activity {
    String[] result;
    SharedPreferences pref;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request1);
        pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         token=pref.getString("role","")+":"+pref.getString("token","");

        Button btnowe=(Button)findViewById(R.id.btnowe);
        Button btnnew=(Button)findViewById(R.id.btnnew);

        String role1 = pref.getString("role", "");
        String token1 = pref.getString("token", "");
        if (token1 != null && !token1.equals("") && role1.equals("storeclerk")) {
        btnowe.setOnClickListener(new View.OnClickListener() {


                  @Override
                  public void onClick(View v) {
                      refreshowe();
                  }
              }
            );

        btnnew.setOnClickListener(new View.OnClickListener() {


                                      @Override
                                      public void onClick(View v) {
                                         refreshnew();
                                      }
                                  }
        );
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                finish();
                Intent intent = new Intent(ProcessRequest1Activity.this,ReportDiscrepancy1Activity.class);
                this.startActivity(intent);
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

                            Toast d=Toast.makeText(ProcessRequest1Activity.this,"No Deliveries",Toast.LENGTH_SHORT);
                            d.show();

                        }
                        else
                        {
                            finish();
                            Intent intent2 = new Intent(ProcessRequest1Activity.this, DeliverOrder1Activity.class);
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
                            Intent intent1 = new Intent(ProcessRequest1Activity.this, ProcessRequest1Activity.class);
                            startActivity(intent1);

                        }
                        else
                        {
                            Toast d=Toast.makeText(ProcessRequest1Activity.this,"No Requisitions",Toast.LENGTH_SHORT);
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

public void refreshnew()
{
    try {
        new AsyncTask<Void, Void, String[]>() {
            protected String[] doInBackground(Void... params) {
                String[] result1 = RequestDept.getuniqueitems(token);
                List<String> resultwrap = new ArrayList<String>();
                for (int i = 0; i < result1.length; i++) {
                    List<RequestDept> requestdeptresult = RequestDept.getrequestdeptstatus(result1[i], token);
                    if (!requestdeptresult.isEmpty()) {
                        resultwrap.add(result1[i]);
                    }

                }
                String[] je = new String[resultwrap.size()];
                for (int j = 0; j < resultwrap.size(); j++) {
                    je[j] = resultwrap.get(j);
                }
                result = je;
                return result;
            }

            protected void onPostExecute(String[] result) {
                if (result == null || result.length == 0) {

                    new AlertDialog.Builder(ProcessRequest1Activity.this)
                            .setTitle("Sorry")
                            .setMessage("No New Request now.")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

                    Intent in = new Intent(ProcessRequest1Activity.this, ProcessRequest2Activity.class);
                    in.putExtra("request", "new");
                    in.putExtra("requestitem", result);
                    startActivity(in);
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
        new AsyncTask<Void, Void, String[]>() {
            protected String[] doInBackground(Void... params) {
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
                String [] je=new String[resultwrap.size()];
                for(int j=0;j<resultwrap.size();j++)
                {
                    je[j]=resultwrap.get(j);
                }
                result=je;
                return result;
            }

            protected void onPostExecute(String[] result2) {
                if (result2==null||result2.length==0) {

                    new AlertDialog.Builder(ProcessRequest1Activity.this)
                            .setTitle("Sorry")
                            .setMessage("No Owe Request now.")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

                    Intent in=new Intent(ProcessRequest1Activity.this,ProcessRequest2Activity.class);
                    in.putExtra("request","owe");
                    in.putExtra("requestitem",result);
                    startActivity(in);
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



