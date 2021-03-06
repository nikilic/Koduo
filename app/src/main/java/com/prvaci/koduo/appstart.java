package com.prvaci.koduo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class appstart extends AppCompatActivity {

    HashMap<String, String> SysVar, UserVar, Acts, Funs;
    String appcode,name,author,description,icon,runs,main;
    ImageView ivIcon;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent start = getIntent();
        appcode = start.getStringExtra("code");
        name = start.getStringExtra("name");
        author = start.getStringExtra("author");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");
        runs = start.getStringExtra("runs");
        main = start.getStringExtra("main");
        SysVar = new HashMap<>();
        UserVar = new HashMap<>();
        Acts = new HashMap<>();
        Funs = new HashMap<>();

        SysVar.put("__black__","#000000");
        SysVar.put("__white__","#ffffff");
        SysVar.put("__red__","#ff0000");
        SysVar.put("__blue__","#0000ff");
        SysVar.put("__green__","#00ff00");
        SysVar.put("__yellow__","#ffff00");
        SysVar.put("__cyan__","#00ffff");
        SysVar.put("__magenta__","#ff00ff");
        SysVar.put("__textsmall__","#16");
        SysVar.put("__textnormal__","#20");
        SysVar.put("__textlarge__","24");
        SysVar.put("__appname__",name);
        initcode(appcode);
        setContentView(R.layout.activity_appstart);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvRuns = findViewById(R.id.tvRuns);
        ivIcon = findViewById(R.id.ivIcon);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Učitavanje...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        ImageRequest ir = new ImageRequest(icon, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                pDialog.dismiss();
                ivIcon.setImageBitmap(response);
            }
        }, 1024, 1024, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(ir);
        tvName.setText(name);
        tvAuthor.setText("Autor aplikacije: " + author);
        tvDescription.setText(description);
        tvRuns.setText("Broj pokretanja: "+runs);
    }

    public void runapp(View view){
        Intent intent = new Intent(this, appact.class);
        intent.putExtra("SysVar",SysVar);
        intent.putExtra("UserVar",UserVar);
        intent.putExtra("Acts",Acts);
        intent.putExtra("Funs",Funs);
        intent.putExtra("code",Acts.get(main));
        startActivity(intent);
    }

    public void initcode(String code){
        BufferedReader codeReader = new BufferedReader(new StringReader(code));
        String line = lineReader(codeReader);
        while(!line.equals("error")){
            String[] linesplit = line.split(" ");
            if(linesplit[0].equals("act")){
                Acts.put(linesplit[1],"");
                line = lineReader(codeReader);
                while(!line.equals(";")){
                    if(!line.equals("error")) {
                        Acts.put(linesplit[1], Acts.get(linesplit[1]) + "\n" + line);
                        line = lineReader(codeReader);
                    }else{
                        appError();
                        break;
                    }
                }
            }else if(linesplit[0].equals("fun")){
                Funs.put(linesplit[1],"");
                line = lineReader(codeReader);
                while(!line.equals(";")){
                    if(!line.equals("error")) {
                        Funs.put(linesplit[1], Funs.get(linesplit[1]) + "\n" + line);
                        line = lineReader(codeReader);
                    }else{
                        appError();
                        break;
                    }
                }
            }
            line = lineReader(codeReader);
        }
    }

    static String lineReader(BufferedReader reader){
        String line;
        try {
            while(true) {
                line = reader.readLine();
                if (line != null) {
                    if (!line.trim().isEmpty()) {
                        return line;
                    }
                }else{
                    return "error";
                }
            }
        } catch(IOException e) {
            Log.e("IO Exception","ERROR 1 - IO EXCEPTION");
            return "error";
        }
    }

    public void appError(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("error",1);
        startActivity(intent);
        finish();
    }
}
