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

public class addnewapp extends AppCompatActivity {

    String author;
    EditText etShortname,etPassword;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewapp);
        etShortname = findViewById(R.id.etShortname);
        etPassword = findViewById(R.id.etPassword);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        author = user.getUsername();
    }

    public void createNew(View view){
        JSONObject request = new JSONObject();
        try {
            request.put("author", author);
            request.put("password", etPassword.getText().toString());
            request.put("shortname", etShortname.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, "https://api.in.rs/koduo/addapp.php", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("updatemess").equals("ok")) {
                                appCreateOk();
                            }else if(response.getString("status").equals("0")){
                                Toast.makeText(getApplicationContext(), "Skraćeno ime aplikacije zauzeto", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Pogrešan password", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    public void appCreateOk(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aplikacija kreirana uspešno")
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
}
