package com.prvaci.koduo;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    String code,name,author,description,icon,runs,main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void StartApp(View view){
        code = "";
        EditText editime = findViewById(R.id.imeApp);
        String ime = editime.getText().toString();
        if(!ime.equals("")) {
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
                                    runapp();
                                } else if (response.getInt("status") == 1) {
                                    Toast.makeText(MainActivity.this, "Aplikacija nije pronađena", Toast.LENGTH_SHORT).show();
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
        }else{
            Toast.makeText(this, "Ime aplikacije ne može biti prazno", Toast.LENGTH_SHORT).show();
        }
    }

    void runapp(){
        Intent intent = new Intent(this,appstart.class);
        intent.putExtra("code",code);
        intent.putExtra("name",name);
        intent.putExtra("author",author);
        intent.putExtra("description",description);
        intent.putExtra("icon",icon);
        intent.putExtra("runs",runs);
        intent.putExtra("main",main);

        startActivity(intent);
    }

    public void login(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
