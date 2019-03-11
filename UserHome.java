package com.x.dpconnect;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class UserHome extends AppCompatActivity {

    String uid;
    List<Appointment> apts;
    ListView lv;

    AppointmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appoints);


        uid = getIntent().getStringExtra("user");
        apts=new Vector<Appointment>();
        lv=(ListView)findViewById(R.id.lv);
        adapter=new AppointmentAdapter(this,apts);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Appointment appointment=apts.get(i);
                AlertDialog.Builder adb=new AlertDialog.Builder(UserHome.this);
                adb.setTitle("Action");
                adb.setMessage(appointment.getUid());

                if(appointment.getStatus()==0 || appointment.getStatus()==1) {


                    adb.setNegativeButton("Cancel Appointment", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateAppointment(appointment.getId(), 2);
                        }
                    });


                }
                else
                    if(appointment.getStatus()==4)

                {

                adb.setMessage(appointment.getSubj()+" \n\n"+appointment.getSymp()+" \n\n"+appointment.getPres());

                }
                adb.create().show();

            }
        });
    }

    private void updateAppointment(final int id, final int i) {

        String url=getResources().getString(R.string.host)+"updateApp.jsp";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response

                        loadAppsForUser();
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map=new HashMap<String,String>();
                {

                    map.put("id",String.valueOf(id));
                    map.put("st",i+"");
                    return  map;
                }
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);



    }


    @Override
    protected void onResume() {
        super.onResume();
        loadAppsForUser();
    }

    private void loadAppsForUser() {


        apts.clear();



        String url=getResources().getString(R.string.host)+"getAppointsOfDoctorForUser.jsp?uid="+uid;

        JsonArrayRequest req = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i=0;i<response.length();i++)
                        {
                            try {
                                JSONObject ja = response.getJSONObject(i);

                                Appointment it=new Appointment();
                                it.setId(ja.getInt("id"));
                                it.setAmasterid(ja.getInt("amasterid"));
                                it.setUid(ja.getString("uid"));
                                it.setStatus(ja.getInt("status"));
                                it.setSubj(ja.getString("subj"));
                                it.setSymp(ja.getString("symp"));
                                it.setPres(ja.getString("pres"));
                                apts.add(it);
                                adapter.notifyDataSetChanged();



                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });


        Volley.newRequestQueue(this).add(req);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.usermenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent it=new Intent(this,DoctorSearchActivity.class);
it.putExtra("user",uid);
        startActivity(it);
        return super.onOptionsItemSelected(item);
    }
}
