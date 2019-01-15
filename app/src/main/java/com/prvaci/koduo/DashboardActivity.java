package com.prvaci.koduo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements RecyclerViewDashboardAdapter.ItemClickListener {
    private SessionHandler session;
    ProgressDialog pDialog;
    JSONObject[] apps;

    RecyclerViewDashboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences sharedPref = this.getSharedPreferences(
                "prvaci-koduo", Context.MODE_PRIVATE);
        boolean firstTime = sharedPref.getBoolean("DashboardFirstTime",true);
        if(firstTime){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Zdravo!\n\nDobrodošao u korisnički panel\n\nOvde možeš napraviti svoju " +
                    "sopstvenu aplikaciju, ili da promeniš postojeću. Sve promene možeš vršiti " +
                    "pomoću vizuelnog editora ili preko koda.\n\nPreporučujemo ti da pogledaš uputstvo!")
                    .setTitle("Korisnički panel");
            builder.setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.setNeutralButton("Otvori upustvo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DashboardActivity.this,tutorial.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("DashboardFirstTime",false);
            editor.apply();
        }

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        TextView tvHello = findViewById(R.id.tvHello);
        tvHello.setText("Zdravo " + user.getUsername());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Učitavanje...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        JSONObject request = new JSONObject();
        try {
            request.put("author", user.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, "https://api.in.rs/koduo/getauthor.php", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt("status") == 0) {
                                apps = new JSONObject[Integer.parseInt(response.getString("appno"))];
                                for(int i = 0; i < apps.length;i++){
                                    apps[i] = (JSONObject) response.get(String.valueOf(i));
                                }
                                RecyclerSetup();
                            } else if (response.getInt("status") == 1) {
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);

        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Učitavanje...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        User user = session.getUserDetails();

        JSONObject request = new JSONObject();
        try {
            request.put("author", user.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, "https://api.in.rs/koduo/getauthor.php", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt("status") == 0) {
                                apps = new JSONObject[Integer.parseInt(response.getString("appno"))];
                                for(int i = 0; i < apps.length;i++){
                                    apps[i] = (JSONObject) response.get(String.valueOf(i));
                                }
                                RecyclerSetup();
                            } else if (response.getInt("status") == 1) {
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    public void RecyclerSetup(){
        ArrayList<AppDashboard> appList = new ArrayList<>();
        for(int i = 0; i < apps.length; i++){
            try {
                AppDashboard appDash = new AppDashboard();
                appDash.setName(apps[i].getString("name"));
                appDash.setRuns("Broj pokretanja: "+apps[i].getString("runs"));
                appDash.setShortname(apps[i].getString("shortname"));
                appList.add(appDash);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewDashboardAdapter(this, appList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            Intent intent =  new Intent(this,DashboardApp.class);
            intent.putExtra("shortname",apps[position].getString("shortname"));
            startActivity(intent);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void addNew(View view){
        Intent intent = new Intent(this,addnewapp.class);
        startActivity(intent);
    }

    public void tutorial(View view){
        Intent intent = new Intent(this,tutorial.class);
        startActivity(intent);
    }
}
