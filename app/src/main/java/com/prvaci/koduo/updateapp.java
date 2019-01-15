package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class updateapp extends AppCompatActivity {

    String code,shortname,name,description,icon,main,author;
    EditText etPassword;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateapp);
        Intent start = getIntent();
        code = start.getStringExtra("code");
        shortname = start.getStringExtra("shortname");
        name = start.getStringExtra("name");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");
        main = start.getStringExtra("main");
        etPassword = findViewById(R.id.etPassword);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        author = user.getUsername();
    }

    public void updateApp(View view){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("author", author);
            request.put("password", etPassword.getText());
            request.put("code", code);
            request.put("name", name);
            request.put("description", description);
            request.put("icon", icon);
            request.put("main", main);
            request.put("shortname", shortname);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("KODUOJSON",request.toString());
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, "https://api.in.rs/koduo/updateapp.php", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("updatemess").equals("ok")) {
                                Log.i("KODUOMESS",response.getString("updatemess"));
                                appUpdateOk();
                            }else{
                                Toast.makeText(getApplicationContext(), "Pogrešan password", Toast.LENGTH_SHORT).show();
                                Log.i("KODUOSTATUS",response.getInt("status")+" "+response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("KODUOSTATUS","ERROR");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    public void appUpdateOk(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aplikacija ažurirana uspešno")
                .setTitle("Ažuriranje");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                goBack();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goBack(){
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, viseditor.class);
        intent.putExtra("shortname", shortname);
        intent.putExtra("name", name);
        intent.putExtra("main", main);
        intent.putExtra("description", description);
        intent.putExtra("code", code);
        intent.putExtra("icon", icon);
        startActivity(intent);
    }
}
