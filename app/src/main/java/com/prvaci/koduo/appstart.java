package com.prvaci.koduo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class appstart extends AppCompatActivity {

    HashMap<String, String> SysVar, UserVar, Acts, Funs;
    String appcode,name,author,description,icon,runs,main;

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
        initcode(appcode);
        setContentView(R.layout.activity_appstart);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvRuns = findViewById(R.id.tvRuns);
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
                    Acts.put(linesplit[1],Acts.get(linesplit[1])+"\n"+line);
                    line = lineReader(codeReader);
                }
            }else if(linesplit[0].equals("fun")){
                Funs.put(linesplit[1],"");
                line = lineReader(codeReader);
                while(!line.equals(";")){
                    Funs.put(linesplit[1],Funs.get(linesplit[1])+"\n"+line);
                    line = lineReader(codeReader);
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
        }
        return "error";
    }

    public String getVar(String line, int pos){
        String[] linesplit = line.split(" ");
        if((""+linesplit[pos].toCharArray()[0]+linesplit[pos].toCharArray()[1]).equals("__")){
            return SysVar.get(linesplit[pos])+"";
        }
        else if((""+linesplit[pos].toCharArray()[0]).equals("\"")){
            return line.split("\"")[1];
        }
        else{
            return UserVar.get(linesplit[pos])+"";
        }
    }

    public static int countSubstring(String subStr, String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i).startsWith(subStr)) {
                count++;
            }
        }
        return count;
    }
}