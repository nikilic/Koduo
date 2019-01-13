package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardApp extends AppCompatActivity {

    String code,name,author,description,icon,runs,shortname,main;
    boolean okay = false;
    ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_app);

        Intent start = getIntent();
        String ime = start.getStringExtra("shortname");
        shortname = ime;

        JSONObject request = new JSONObject();
        try {
            request.put("shortname", ime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, "https://api.in.rs/koduo/getapp.php", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 0) {
                                code = response.getString("code");
                                name = response.getString("name");
                                author = response.getString("author");
                                description = response.getString("description");
                                icon = response.getString("icon");
                                runs = response.getString("runs");
                                main = response.getString("main");
                                loadIcon();
                                DisplayApp();
                                okay = true;
                            } else if (response.getInt("status") == 1) {
                                Toast.makeText(DashboardApp.this, "Aplikacija nije pronađena", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    public void loadIcon(){
        ivIcon = findViewById(R.id.ivIcon);
        ImageRequest ir = new ImageRequest(icon, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // callback
                ivIcon.setImageBitmap(response);
            }
        }, 1024, 1024, null, null);
        MySingleton.getInstance(this).addToRequestQueue(ir);
    }

    public void DisplayApp(){
        TextView tvName = findViewById(R.id.tvName);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvRuns = findViewById(R.id.tvRuns);
        tvName.setText(name);
        tvDescription.setText(description);
        tvRuns.setText("Broj pokretanja: " + runs);
    }

    public void visedit(View view){
        if(okay){
            Intent intent = new Intent(this,viseditor.class);
            intent.putExtra("shortname",shortname);
            intent.putExtra("code",code);
            intent.putExtra("name",name);
            intent.putExtra("description",description);
            intent.putExtra("icon",icon);
            intent.putExtra("main",main);
            startActivity(intent);
        }
    }

    public void codeedit(View view){
        if(okay){
            Intent intent = new Intent(this,codeeditor.class);
            intent.putExtra("shortname",shortname);
            intent.putExtra("code",code);
            intent.putExtra("name",name);
            intent.putExtra("description",description);
            intent.putExtra("icon",icon);
            intent.putExtra("main",main);
            startActivity(intent);
        }
    }

    public void deleteapp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Brisanje aplikacije")
                .setMessage("Da li si siguran da želiš da obrišeš aplikaciju?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                opendelete();
            }
        });
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void opendelete(){
        Intent intent = new Intent(this,deleteapp.class);
        intent.putExtra("shortname",shortname);
        startActivity(intent);
    }
}
