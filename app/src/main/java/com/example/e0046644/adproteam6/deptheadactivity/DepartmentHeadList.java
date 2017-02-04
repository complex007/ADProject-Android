package com.example.e0046644.adproteam6.deptheadactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.e0046644.adproteam6.R;

public class DepartmentHeadList extends Activity implements AdapterView.OnItemClickListener {
String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_head_list);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        token=pref.getString("role","")+":"+pref.getString("token","");
        String []values = {"Approve/Reject Request","Assign Representative","Set Collection Point"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.dhrow, R.id.textView1, values);
        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getAdapter().getItem(position);
        if(position == 0) {
            Intent intent = new Intent(this, ApproveRejectActivity.class);
            this.startActivity(intent);
        } else if (position == 1) {
            Intent intent2 = new Intent(this, AssignRepresentativeActivity.class);
            this.startActivity(intent2);

        } else if (position == 2) {
            Intent intent1 = new Intent(this, SetCollectionPointActivity.class);
            this.startActivity(intent1);
        }

    }
}

