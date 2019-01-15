package com.prvaci.koduo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class appact extends AppCompatActivity {

    HashMap<String, String> SysVar, UserVar, Acts, Funs;
    LinearLayout parent;
    boolean first;
    String code,savevar;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        Intent start = getIntent();
        SysVar = (HashMap<String, String>) start.getSerializableExtra("SysVar");
        UserVar = (HashMap<String, String>) start.getSerializableExtra("UserVar");
        Acts = (HashMap<String, String>) start.getSerializableExtra("Acts");
        Funs = (HashMap<String, String>) start.getSerializableExtra("Funs");
        code = start.getStringExtra("code");

        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        super.onCreate(savedInstanceState);
        try {
            runcode(code, getApplicationContext(), 1);
        }catch(Exception e){
            appError();
        }
        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parent.setLayoutParams(linLayoutParam);
        parent.setPadding(0,0,0,240);
        scrollView.addView(parent);
        setContentView(scrollView);
    }

    public void runcode(String code, Context context, int use) throws Exception{
        //1 - act code, 2 - fun code, 3 - if act, 4 - if fun
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
                        if(!line.equals("error")) {
                            ifcode += line + "\n";
                            line = lineReader(codeReader);
                        }else{
                            appError();
                            break;
                        }
                    }
                    while(!line.equals("endif")){
                        if(!line.equals("error")) {
                            line = lineReader(codeReader);
                        }else{
                            appError();
                            break;
                        }
                    }
                    if(use != 3 && use != 4)
                        runcode(ifcode,context,use+2);
                    else
                        runcode(ifcode,context,use);
                }else{
                    while(!line.equals("else")){
                        if(!line.equals("error")) {
                            line = lineReader(codeReader);
                        }else{
                            appError();
                            break;
                        }
                    }
                    line = lineReader(codeReader);
                    while(!line.equals("endif")){
                        if(!line.equals("error")) {
                            ifcode += line + "\n";
                            line = lineReader(codeReader);
                        }else{
                            appError();
                            break;
                        }
                    }
                    if(use != 3 && use != 4)
                        runcode(ifcode,context,use+2);
                    else
                        runcode(ifcode,context,use);
                }
            }
            else if(linesplit[0].equals("merge")){
                String value = getVar(linesplit[1].split(",")[1],0);
                UserVar.put(linesplit[1].split(",")[0],UserVar.get(linesplit[1].split(",")[0])+value);
            }
            else if(use == 1 || use == 3){
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
                        textView.setGravity(Gravity.CENTER);
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
                                try {
                                    runcode(finalcode, button_context, 2);
                                }catch (Exception e){
                                    appError();
                                }
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
                    } else if (linesplit[1].equals("textfield")){
                        savevar = linesplit[2];
                        EditText  editText = new EditText(context);
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                UserVar.put(savevar,s.toString());
                            }
                        });
                        LinearLayout.LayoutParams layoutparamsbutton = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutparamsbutton.setMargins(32, 32, 32, 0);
                        layoutparamsbutton.gravity = Gravity.CENTER;
                        editText.setLayoutParams(layoutparamsbutton);
                        parent.addView(editText);
                    } else if (linesplit[1].equals("image")){
                        String source;
                        if(linesplit[2].toCharArray()[0] == '\"'){
                            source = line.split("\"")[1];
                        }else{
                            source = getVar(linesplit[0],0);
                        }
                        final ImageView imageView = new ImageView(context);
                        ImageRequest ir = new ImageRequest(source, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                imageView.setImageBitmap(response);
                            }
                        }, 1024, 1024, null, null);
                        VolleySingleton.getInstance(this).addToRequestQueue(ir);
                        LinearLayout.LayoutParams layoutparamsbutton = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutparamsbutton.setMargins(32, 32, 32, 0);
                        layoutparamsbutton.gravity = Gravity.CENTER;
                        imageView.setLayoutParams(layoutparamsbutton);
                        parent.addView(imageView);
                    }
                }else if(linesplit[0].equals("background")){
                    scrollView.setBackgroundColor(Color.parseColor(getVar(linesplit[1],0)));
                }
            }else if(use == 2 || use == 4){
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
                else if(line.split(" ")[0].equals("runfun")){
                    String finalcode = Funs.get(line.split(" ")[1]);
                    try {
                        runcode(finalcode, context, 2);
                    }catch (Exception e){
                        appError();
                    }
                }
                else if(line.split(" ")[0].equals("call")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getVar(line,1)));
                    startActivity(intent);
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
                            try {
                                runcode(finalcode, button_context, 2);
                            }catch (Exception e){
                                appError();
                            }
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

    public void appError(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("error",1);
        startActivity(intent);
        finish();
    }
}
