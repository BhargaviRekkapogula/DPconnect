package com.x.dpconnect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UserLoginActivity extends AppCompatActivity {

    EditText username,password;
    AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        username=(EditText)findViewById(R.id.et_userName);
        password=(EditText)findViewById(R.id.et_password);
    }
    public void signin(View v)
    {
        final String un=username.getText().toString();
        final String ps=password.getText().toString();

        if(un.trim().length()<3)
            username.setError("Enter a valid User name");
        else
        if(ps.trim().length()<3)
            password.setError("Enter a valid password");
        else
        {

            String url=getResources().getString(R.string.host)+"userlogin.jsp";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Do something with the response
                            if(response.trim().equals("success"))
                            {
                                manager=(AlarmManager) getSystemService(ALARM_SERVICE);
                                Intent its=new Intent("alarm");
                                its.putExtra("user",un);
                                PendingIntent pi=     PendingIntent.getBroadcast(UserLoginActivity.this,123, its,0);
                                manager.setRepeating(AlarmManager.RTC_WAKEUP,1000,1000,pi);

                                Intent it=new Intent(UserLoginActivity.this,UserHome.class);
                                it.putExtra("user",un);
                                startActivity(it);
                                finish();
                            }
                            else
                                Toast.makeText(UserLoginActivity.this,response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                            Toast.makeText(UserLoginActivity.this, "Error ! Try again later", Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map map=new HashMap<String,String>();
                    {
                        map.put("username",un);
                        map.put("password",ps);
                        return  map;
                    }
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);

        }
    }
    public void signup(View v)
    {
        Intent it=new Intent(this,UserRegister.class);
        startActivity(it);


    }
}
