package com.prvaci.koduo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ExploreActivity extends AppCompatActivity {

    String code,name,author,description,icon,runs,main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
    }

    public void startBasic(View view){
        runApp("probnaapp");
    }

    public void startQuiz(View view){
        runApp("quizapp");
    }

    public void startPres(View view){
        runApp("presapp");
    }

    public void runApp(String ime){
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
                                    Toast.makeText(ExploreActivity.this, "Aplikacija nije pronađena", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Greška na serveru", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),
                                    "Greška na mreži", Toast.LENGTH_SHORT).show();
                        }
                    });
            VolleySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
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

    public void restartTutorial(View view){
        SharedPreferences sharedPref = this.getSharedPreferences(
                "prvaci-koduo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IsFirstTimeLaunch",true);
        editor.putBoolean("DashboardFirstTime",true);
        editor.putBoolean("VisEditorFirstTime",true);
        editor.putBoolean("AFEditorFirstTime",true);
        editor.commit();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
