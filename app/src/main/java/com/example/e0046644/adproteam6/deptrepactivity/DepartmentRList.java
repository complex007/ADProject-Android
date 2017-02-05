package com.example.e0046644.adproteam6.deptrepactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.e0046644.adproteam6.MainActivity;
import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.deptheadactivity.ApproveRejectActivity;
import com.example.e0046644.adproteam6.deptheadactivity.AssignRepresentativeActivity;
import com.example.e0046644.adproteam6.deptheadactivity.SetCollectionPointActivity;

public class DepartmentRList extends Activity implements AdapterView.OnItemClickListener {
String token;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_head_list);
         pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String role1=pref.getString("role","");
        String token1=pref.getString("token","");
        if(token1!=null&&!token1.equals("")&&role1.equals("departmentrepresentative")){
            token = pref.getString("role", "") + ":" + pref.getString("token", "");
            String[] values = { "Set Collection Point","Log Out"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.dhrow, R.id.textView1, values);
            ListView list = (ListView) findViewById(R.id.listView1);
            list.setAdapter(adapter);
            list.setOnItemClickListener(this);
        }
        else
        {

            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            finish();

        }
    }
    @Override
    public void finish()
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        super.finish();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
            Intent intent1 = new Intent(this, SetCollectionPointActivityRP.class);
            this.startActivity(intent1);
        }
        else
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            finish();
        }

    }
}

