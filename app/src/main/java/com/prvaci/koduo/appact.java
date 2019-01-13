package com.prvaci.koduo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class appact extends AppCompatActivity {

    HashMap<String, String> SysVar, UserVar, Acts, Funs;
    LinearLayout parent;
    boolean first;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        Intent start = getIntent();
        SysVar = (HashMap<String, String>) start.getSerializableExtra("SysVar");
        UserVar = (HashMap<String, String>) start.getSerializableExtra("UserVar");
        Acts = (HashMap<String, String>) start.getSerializableExtra("Acts");
        Funs = (HashMap<String, String>) start.getSerializableExtra("Funs");
        code = start.getStringExtra("code");

        super.onCreate(savedInstanceState);
        runcode(code,getApplicationContext(),1);
        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(parent,linLayoutParam);
    }

    public void runcode(String code, Context context, int use){
        //1 - act code, 2 - fun code
        if(use == 1){
            parent = new LinearLayout(context);
            parent.setOrientation(LinearLayout.VERTICAL);
        }
        BufferedReader codeReader = new BufferedReader(new StringReader(code));
        String line = lineReader(codeReader);
        while(!line.equals("error")){
            String[] linesplit = line.split(" ");
            if(linesplit[1].equals("set")){
                UserVar.put(line.split(" ")[0],getVar(line,2));
            }
            else if(linesplit[0].equals("if")){
                String var1;
                String var2;
                if(linesplit[1].toCharArray()[0] == '\"'){
                    var1 = line.split("\"")[1];
                    if(countSubstring("\"",line) == 4){
                        var2 = line.split("\"")[3];
                    }else{
                        var2 = getVar(line.split("\"")[2],2);
                    }
                }else{
                    var1 = getVar(linesplit[1],0);
                    if(countSubstring("\"",line) == 2){
                        var2 = line.split("\"")[1];
                    }else{
                        var2 = getVar(linesplit[3],0);
                    }
                }
                line = lineReader(codeReader);
                String ifcode = "";
                if(var1.equals(var2)){
                    while(!line.equals("else")){
                        ifcode += line + "\n";
                        line = lineReader(codeReader);
                    }
                    while(!line.equals("endif")){
                        line = lineReader(codeReader);
                    }
                    runcode(ifcode,context,use);
                }else{
                    while(!line.equals("else")){
                        line = lineReader(codeReader);
                    }
                    line = lineReader(codeReader);
                    while(!line.equals("endif")){
                        ifcode += line + "\n";
                        line = lineReader(codeReader);
                    }
                    runcode(ifcode,context,use);
                }
            }
            else if(use == 1){
                if(linesplit[0].equals("add")) {
                    if (linesplit[1].equals("text")) {
                        String parameters = line.substring(9, line.length());
                        int format = countSubstring("\"", parameters);
                        String text_text = "", text_color = " ", text_size = "";
                        if (format == 0) {
                            text_text = getVar(parameters.split(",")[0], 0);
                            text_color = getVar(parameters.split(",")[1], 0);
                            text_size = getVar(parameters.split(",")[2], 0);
                        } else if (format == 2) {
                            if (parameters.toCharArray()[0] == '\"') {
                                text_text = parameters.split("\"")[1];
                                text_color = getVar(parameters.split("\"")[2].split(",")[1], 0);
                                text_size = getVar(parameters.split("\"")[2].split(",")[2], 0);
                            } else if (parameters.split(",")[1].toCharArray()[0] == '\"') {
                                text_text = getVar(parameters.split(",")[0], 0);
                                text_color = parameters.split(",")[1].split("\"")[1];
                                text_size = getVar(parameters.split("\"")[2].split(",")[1], 0);
                            } else if (parameters.split(",")[2].toCharArray()[0] == '\"') {
                                text_text = getVar(parameters.split(",")[0], 0);
                                text_color = getVar(parameters.split(",")[1], 0);
                                text_size = parameters.split(",")[2].split("\"")[1];
                            }
                        } else if (format == 4) {
                            if (parameters.toCharArray()[0] == '\"' && parameters.split(",")[1].toCharArray()[0] == '\"') {
                                text_text = parameters.split("\"")[1];
                                text_color = parameters.split("\"")[3];
                                text_size = getVar(parameters.split("\"")[4].split(",")[1], 0);
                            } else if (parameters.toCharArray()[0] != '\"') {
                                text_text = getVar(parameters.split(",")[0], 0);
                                text_color = parameters.split(",")[1].split("\"")[1];
                                text_size = parameters.split("\"")[3];
                            } else {
                                text_text = parameters.split("\"")[1];
                                text_color = getVar(parameters.split("\"")[2].split(",")[1], 0);
                                text_size = parameters.split("\"")[2].split(",")[2].split("\"")[1];
                            }
                        } else if (format == 6) {
                            text_text = parameters.split("\"")[1];
                            text_color = parameters.split("\"")[3];
                            text_size = parameters.split("\"")[5];
                        }
                        TextView textView = new TextView(context);
                        textView.setText(text_text);
                        textView.setTextColor(Color.parseColor(text_color));
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.valueOf(text_size));
                        LinearLayout.LayoutParams layoutparamstext = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutparamstext.setMargins(32, 32, 32, 0);
                        layoutparamstext.gravity = Gravity.CENTER;
                        textView.setLayoutParams(layoutparamstext);
                        parent.addView(textView);
                    } else if (linesplit[1].equals("button")) {
                        String parameters = line.substring(11, line.length());
                        int format = countSubstring("\"", parameters);
                        String button_text = "", button_fun = "";
                        if (format == 0) {
                            button_text = getVar(parameters.split(",")[0], 0);
                            button_fun = parameters.split(",")[1];
                        } else if (format == 2) {
                            button_text = parameters.split("\"")[1];
                            button_fun = parameters.split("\"")[2].split(",")[1];
                        }
                        Button button = new Button(context);
                        button.setText(button_text);
                        final String finalcode = Funs.get(button_fun);
                        final Context button_context = context;
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Log.i("KDFUN", "START");
                                runcode(finalcode, button_context, 2);
                            }
                        });
                        LinearLayout.LayoutParams layoutparamsbutton = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutparamsbutton.setMargins(32, 32, 32, 0);
                        layoutparamsbutton.gravity = Gravity.CENTER;
                        button.setLayoutParams(layoutparamsbutton);
                        button.setGravity(Gravity.CENTER);
                        parent.addView(button);
                    }
                }
            }else if(use == 2){
                if(line.split(" ")[0].equals("message")){
                    Toast.makeText(context, getVar(line.substring(8,line.length()),0), Toast.LENGTH_LONG).show();
                }
                else if(line.split(" ")[0].equals("run")){
                    Intent intent = new Intent(this, appact.class);
                    intent.putExtra("SysVar",SysVar);
                    intent.putExtra("UserVar",UserVar);
                    intent.putExtra("Acts",Acts);
                    intent.putExtra("Funs",Funs);
                    intent.putExtra("code",Acts.get(line.split(" ")[1]));
                    startActivity(intent);
                }
                else if(line.split(" ")[0].equals("call")){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getVar(line,1)));
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "Nije data dozvola za pozive. Dodaj dozvolu u podešavanjima i pokušaj ponovo", Toast.LENGTH_LONG).show();
                    }else{
                        startActivity(intent);
                    }
                }
                else if(line.split(" ")[0].equals("web")){
                    String url = getVar(line,1);
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
                else if(line.split(" ")[0].equals("dialog")){
                    String title = "",message = "";
                    if(countSubstring("\"",line) == 0){
                        title = getVar(line.split(",")[1],0);
                        message = getVar(line.split(",")[2],0);
                    }else if(countSubstring("\"",line) == 2){
                        if(line.split(",")[1].toCharArray()[0] == '"'){
                            title = line.split("\"")[1];
                            message = getVar(line.split("\"")[2].split(",")[1],0);
                        }else{
                            title = getVar(line.split(",")[1],0);
                            message = line.split("\"")[1];
                        }
                    }else if(countSubstring("\"",line) == 4){
                        title = line.split("\"")[1];
                        message = line.split("\"")[3];
                    }

                    final String finalcode = Funs.get(line.substring(7).split(",")[0]);
                    final Context button_context = context;

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(message)
                            .setTitle(title);
                    builder.setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            runcode(finalcode, button_context, 2);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
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
