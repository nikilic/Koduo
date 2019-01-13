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

public class deleteapp extends AppCompatActivity {

    String shortname,author;
    EditText etPassword;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteapp);
        Intent start = getIntent();
        shortname = start.getStringExtra("shortname");
        etPassword = findViewById(R.id.etPassword);

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        author = user.getUsername();
    }

    public void deleteApp(View view){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("author", author);
            request.put("password",etPassword.getText().toString());
            request.put("shortname", shortname);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("KODUOJSON",request.toString());
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, "https://api.in.rs/koduo/deleteapp.php", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 0) {
                                appDeleteOk();
                                Log.i("KODUODELETE",response.getString("deletemess"));
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

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    public void appDeleteOk(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aplikacija obrisana uspešno")
                .setTitle("Brisanje aplikacije");
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
