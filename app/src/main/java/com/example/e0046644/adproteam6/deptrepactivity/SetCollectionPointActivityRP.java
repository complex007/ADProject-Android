package com.example.e0046644.adproteam6.deptrepactivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Department;

public class SetCollectionPointActivityRP extends Activity {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
   String headcode;
    String token;
    TextView collpt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_collection_pointrp);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        token=pref.getString("role","")+":"+pref.getString("token","");
        headcode=pref.getString("usercode","");

        spinner = (Spinner) findViewById(R.id.spinner2);
        collpt = (TextView) findViewById(R.id.textView4);

       refresh();
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
                        Toast t = Toast.makeText(SetCollectionPointActivityRP.this, "Collection Point Updated", Toast.LENGTH_SHORT);
                        t.show();
                        refresh();

                    }
                }.execute(submit);


                }
                catch(Exception da)
                {
                    refresh();
                }


            }




        });

    }

public void refresh()
{
    new AsyncTask<String, Void, String>()
    {
        @Override
        protected String doInBackground (String...params){
            Log.i("do back",params[0]);
            String po= Department.findCurrentCollectionPoint(params[0],token);
            Log.i("rp poi",po);
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


