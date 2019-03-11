package com.x.dpconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    EditText et_name,et_password,et_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
   et_name=(EditText)findViewById(R.id.et_userName);
   et_password=(EditText)findViewById(R.id.et_password);
   et_email=(EditText)findViewById(R.id.et_email);
    }
    public void signin(View v)
    {

        finish();
    }
    public void signup(View v)
    {
        final String un=et_name.getText().toString();
        final String ps=et_password.getText().toString();
        final String email=et_email.getText().toString();

        if(un.trim().length()<3)
            et_name.setError("Enter a valid User name");
        else
        if(ps.trim().length()<3)
            et_password.setError("Enter a valid password");
        else
        {
            String url=getResources().getString(R.string.host)+"userregister.jsp";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                                Toast.makeText(UserRegister.this,response, Toast.LENGTH_SHORT).show();
                        finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                            Log.e("showoff",error.toString());
                            Toast.makeText(UserRegister.this, "Error ! Try again later", Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map map=new HashMap<String,String>();
                    {
                        map.put("username",un);
                        map.put("password",ps);

                        map.put("email",email);

                        return  map;
                    }
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);

        }

    }
}
