package com.example.e0046644.adproteam6.deptheadactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e0046644.adproteam6.MainActivity;
import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.clerkactivity.DeliverOrder2Activity;
import com.example.e0046644.adproteam6.data.Department;
import com.example.e0046644.adproteam6.data.RequisitionItem;

import java.util.ArrayList;
import java.util.List;

public class SetCollectionPointActivity extends Activity {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
   String headcode;
    String token;
    private List<RequisitionItem> reqlist = new ArrayList<RequisitionItem>();
    TextView collpt;
    ListView items;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_collection_point);
        pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String role1=pref.getString("role","");
        String token1=pref.getString("token","");
        if(token1!=null&&!token1.equals("")&&role1.equals("departmenthead")){
            token = pref.getString("role", "") + ":" + pref.getString("token", "");
            headcode = pref.getString("usercode", "");

            spinner = (Spinner) findViewById(R.id.spinner2);
            collpt = (TextView) findViewById(R.id.textView4);
            try
            {
                refresh();
            }
            catch(Exception ex)
            {
                finish();
            }

        Button b = (Button) findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try
                {
                Spinner spinner = (Spinner)findViewById(R.id.spinner2);
                String collpoint = spinner.getSelectedItem().toString();
                String[] submit = new String [2];
                submit[0] =collpoint;
                submit[1] = headcode;


                new AsyncTask<String[], Void, Void>(){
                    @Override
                    protected Void doInBackground (String[]...params){
                        Log.i("array",params[0][0].toString());
                        Log.i("array",params[0][1].toString());
                        Department.updateCollectionPoint(params[0],token);
                        return null;

                    }
                    @Override
                    protected void onPostExecute(Void result){
                        Toast t = Toast.makeText(SetCollectionPointActivity.this, "Collection Point Updated", Toast.LENGTH_SHORT);
                        t.show();
                        finish();

                    }
                }.execute(submit);

                }
                catch (Exception ex)
                {
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
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
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
                            Toast.makeText(SetCollectionPointActivity.this, "NO PENDING REQUEST", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            finish();
                            Intent i2 = new Intent(SetCollectionPointActivity.this,ApproveRejectActivity.class);
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
        new AsyncTask<String, Void, String>()
        {
            @Override
            protected String doInBackground (String...params){
                String po= Department.findCurrentCollectionPoint(params[0],token);
                return po;


            }
            @Override
            protected void onPostExecute (String collectionpoint)
            {
                collpt.setText(collectionpoint);
            }


        }.execute(headcode);
    }
}


