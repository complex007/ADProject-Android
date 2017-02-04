package com.example.e0046644.adproteam6;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e0046644.adproteam6.clerkactivity.StoreClerkList;
import com.example.e0046644.adproteam6.data.Login;
import com.example.e0046644.adproteam6.deptheadactivity.DepartmentHeadList;
import com.example.e0046644.adproteam6.deptrepactivity.SetCollectionPointActivityRP;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    SharedPreferences pref;
    EditText username, password;
    String uname,pword;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username=(EditText)findViewById(R.id.txtusername);
            password=(EditText)findViewById(R.id.txtpassword);
            username.setText(pref.getString("usercode",""));
            password.setText(pref.getString("password",""));
            Button login=(Button)findViewById(R.id.btnlogin);
            login.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View v) {

                                             SharedPreferences.Editor editor = pref.edit();
                                             editor.putString("usercode", username.getText().toString());
                                             editor.putString("password", password.getText().toString());
                                             editor.commit();
                                             uname=username.getText().toString();
                                             pword=password.getText().toString();
                                             Login info=new Login(uname,pword);
                                             Log.i("logininfo",info.toString());
                                             new AsyncTask<Login,Void,String>(){
                                                 @Override
                                                 protected String doInBackground(Login... params) {
                                                     Log.i("logininfo back",params[0].toString());
                                                     user=Login.getLoginInfo(params[0],"1321354651321");
                                                     Log.i("login result",user.toString());
                                                     return user;
                                                 }

                                                 protected  void onPostExecute(String user){
                                                     String in="\"\"\n";

                                                     if(user.equals(in))
                                                     {

                                                         Toast d=Toast.makeText(MainActivity.this,"Incorrect Login Infomation",Toast.LENGTH_SHORT);
                                                         d.show();
                                                     }
                                                     else
                                                     {
                                                         List<String> infolist = Arrays.asList(user.split(":"));
                                                         String role=infolist.get(2);
                                                         Log.i("role",role);
                                                         String token= infolist.get(3);
                                                         Log.i("token",token);
                                                         SharedPreferences.Editor editor = pref.edit();
                                                         editor.putString("token", token);
                                                         editor.putString("role", role);
                                                         editor.commit();
                                                         switch(role)
                                                         {
                                                             case "storeclerk":
                                                                 Intent i = new Intent(getApplicationContext(),StoreClerkList.class);
                                                                 startActivityForResult(i,567);
                                                                 break;
                                                             case "departmenthead":
                                                                 Intent i2 = new Intent(getApplicationContext(),DepartmentHeadList.class);
                                                                 startActivityForResult(i2,568);
                                                                 break;

                                                             case  "departmentrepresentative" :
                                                                 Intent i3 = new Intent(getApplicationContext(),SetCollectionPointActivityRP.class);
                                                                 startActivityForResult(i3,569);
                                                                 break;
                                                             default:
                                                             {
                                                                 break;
                                                             }

                                                         }


                                                         Log.i("getuserinfo",pref.toString());
                                                     }


                                                 }
                                             }.execute(info);

                                             Log.i("pass",pref.getAll().toString());
                                             finish();
                                         }
                                     }
            );


    }
}
