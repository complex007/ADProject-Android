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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e0046644.adproteam6.R;
import com.example.e0046644.adproteam6.data.Employee;
import com.example.e0046644.adproteam6.data.RequisitionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanje on 1/25/2017.
 */

public class AssignRepresentativeActivity extends Activity{

    String headcode;
    private List<Employee> emplist = new ArrayList<Employee>();
    private List<RequisitionItem> reqlist = new ArrayList<RequisitionItem>();
    ListView items;
    TextView currep;
    Spinner spinner;
String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_representative);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        token=pref.getString("role","")+":"+pref.getString("token","");
        headcode=pref.getString("usercode","");
         currep = (TextView) findViewById(R.id.CurrentRepresentative);
          spinner = (Spinner) findViewById(R.id.spinner2);

         refresh();

        Button b = (Button) findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {

                Spinner spinner = (Spinner) findViewById(R.id.spinner2);
                String newrep = spinner.getSelectedItem().toString();
                Log.i("aa", newrep);
                Employee employee = new Employee();
                Log.i("bb", emplist.toString());
                for (Employee e : emplist)
                {
                    if (newrep.equals( e.get("Employeename")))
                    {
                        employee.put("Employeecode", e.get("Employeecode"));
                        employee.put("Employeename", e.get("Employeename"));
                        employee.put("Employeeemail", e.get("Employeeemail"));
                        Log.i("dd", e.get("Employeeemail".toString()));
                        employee.put("Deptcode", e.get("Deptcode"));
                        employee.put("Role", e.get("Role"));
                        employee.put("Del", e.get("Del"));
                        break;}
                }

                    new AsyncTask<Employee, Void, Void>() {
                        @Override
                        protected Void doInBackground(Employee...params) {
                            Log.i("e", params[0].toString());
                            Employee.setRepresentative(params[0],token);
                            return null;

                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            Toast t = Toast.makeText(AssignRepresentativeActivity.this, "New Representative Updated", Toast.LENGTH_SHORT);
                            t.show();
                            finish();

                        }
                    }.execute(employee);

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
                            Toast.makeText(AssignRepresentativeActivity.this, "NO PENDING REQUEST", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            finish();
                            Intent i2 = new Intent(AssignRepresentativeActivity.this,ApproveRejectActivity.class);
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
        new AsyncTask<String, Void, String>()
        {
            @Override
            protected String doInBackground (String...params){
                return Employee.findcurrentRepresentative(params[0],token);

            }
            @Override
            protected void onPostExecute (String currentrepresentative)
            {
                currep.setText(currentrepresentative);
            }


        }.execute(headcode);



        new AsyncTask<String, Void, List<Employee>>()
        {
            @Override
            protected List<Employee> doInBackground (String...params){
                emplist = Employee.populateEmployee(params[0],token);
                Log.i("emplist", emplist.toString());
                return emplist;
            }
            @Override
            protected void onPostExecute (List<Employee> employeeList)
            {
                List<String> result = new ArrayList<String>();
                for (Employee e: employeeList)
                {result.add(e.get("Employeename"));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, result);
                spinner.setAdapter(dataAdapter);
            }
        }.execute(headcode);
    }
}
