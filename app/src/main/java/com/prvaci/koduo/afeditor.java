package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class afeditor extends AppCompatActivity implements AFEditorAdapter.ItemClickListener {

    List<AFEditorItem> afList;
    HashMap<String, String> Acts, Funs;
    AFEditorAdapter adapter;
    String code,shortname,name,main,description,icon,functionname,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afeditor);

        Intent start = getIntent();
        functionname = start.getStringExtra("functionname");
        type = start.getStringExtra("type");
        Acts = (HashMap<String, String>) start.getSerializableExtra("Acts");
        Funs = (HashMap<String, String>) start.getSerializableExtra("Funs");
        shortname = start.getStringExtra("shortname");
        name = start.getStringExtra("name");
        main = start.getStringExtra("main");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");

        afList = new ArrayList<AFEditorItem>();
        if(type.equals("act")) {
            code = Acts.get(functionname);
            actinit();
        }else {
            code = Funs.get(functionname);
            funinit();
        }
        Log.i("CODEAFEDITOR",code);

        RecyclerView recycler = findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearLayoutManager);
        adapter = new AFEditorAdapter(this,afList);
        adapter.setClickListener(this);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("KODUOPROBA",String.valueOf(view.getId())+" "+position);
        if(view.getId() == R.id.bDown){
            if(position+1 != afList.size()){
                AFEditorItem temp = afList.get(position);
                afList.set(position,afList.get(position+1));
                afList.set(position+1,temp);
                adapter.notifyDataSetChanged();
            }
        }else if(view.getId() == R.id.bUp){
            if(position != 0){
                AFEditorItem temp = afList.get(position);
                afList.set(position,afList.get(position-1));
                afList.set(position-1,temp);
                adapter.notifyDataSetChanged();
            }
        }else if(view.getId() == R.id.bDelete){
            afList.remove(position);
            adapter.notifyDataSetChanged();
        }else{
            String codebefore = "";
            String codeafter = "";
            BufferedReader codeReader = new BufferedReader(new StringReader(code));
            String line = lineReader(codeReader);
            int i = 0;
            while(!line.equals("error")){
                Log.i("KODUOLINE",line);
                String[] linesplit = line.split(" ");
                if(i < position)
                    codebefore += line + "\n";
                else if(i>position)
                    codeafter += line +"\n";
                if(linesplit[0].equals("if")){
                    while(!line.equals("endif")){
                        line = lineReader(codeReader);
                        if(i < position)
                            codebefore += line + "\n";
                        else if(i>position)
                            codeafter += line +"\n";
                    }
                }
                i++;
                line = lineReader(codeReader);
            }
            Intent intent = new Intent(this,afitemeditor.class);
            intent.putExtra("codebefore",codebefore);
            intent.putExtra("codeafter",codeafter);
            intent.putExtra("itemcode",afList.get(position).getCode());
            intent.putExtra("itemtype",afList.get(position).getType());
            intent.putExtra("Acts",Acts);
            intent.putExtra("Funs",Funs);
            intent.putExtra("functionname",functionname);
            intent.putExtra("type",type);
            intent.putExtra("shortname",shortname);
            intent.putExtra("name",name);
            intent.putExtra("main",main);
            intent.putExtra("description",description);
            intent.putExtra("icon",icon);
            startActivity(intent);
        }
    }

    public void actinit(){
        BufferedReader codeReader = new BufferedReader(new StringReader(code));
        String line = lineReader(codeReader);
        while(!line.equals("error")){
            String[] linesplit = line.split(" ");
            if(linesplit[1].equals("set")){
                AFEditorItem item = new AFEditorItem();
                item.setName("SET komanda");
                item.setType("set");
                String data = linesplit[0] + " = ";
                for(int i = 2; i < linesplit.length; i++){
                    data += linesplit[i] + " ";
                }
                item.setData(data);
                item.setCode(line);
                afList.add(item);
            }
            else if(linesplit[0].equals("if")){
                AFEditorItem item = new AFEditorItem();
                item.setName(line);
                item.setType("if");
                item.setData("Klikni za pregled koda...");
                String ifcode = line + "\n";
                line = lineReader(codeReader);
                while(!line.equals("endif")){
                    ifcode += line + "\n";
                    line = lineReader(codeReader);
                }
                item.setCode(ifcode);
                afList.add(item);
            }
            else if(linesplit[0].equals("add")){
                AFEditorItem item = new AFEditorItem();
                if(linesplit[1].equals("text")){
                    item.setName("Tekst");
                    item.setType("text");
                    item.setData(line.substring(9));
                }else if(linesplit[1].equals("button")){
                    item.setName("Dugme");
                    item.setType("button");
                    item.setData(line.substring(11));
                }
                item.setCode(line);
                afList.add(item);
            }
            line = lineReader(codeReader);
        }
    }

    public void funinit(){
        BufferedReader codeReader = new BufferedReader(new StringReader(code));
        String line = lineReader(codeReader);
        while(!line.equals("error")){
            String[] linesplit = line.split(" ");
            if(linesplit[1].equals("set")){
                AFEditorItem item = new AFEditorItem();
                item.setName("SET komanda");
                item.setType("set");
                String data = linesplit[0] + " = ";
                for(int i = 2; i < linesplit.length; i++){
                    data += linesplit[i] + " ";
                }
                item.setData(data);
                item.setCode(line);
                afList.add(item);
            }
            else if(linesplit[0].equals("if")){
                AFEditorItem item = new AFEditorItem();
                item.setName(line);
                item.setType("if");
                item.setData("Klikni za pregled koda...");
                String ifcode = line + "\n";
                line = lineReader(codeReader);
                while(!line.equals("endif")){
                    ifcode += line + "\n";
                    line = lineReader(codeReader);
                }
                item.setCode(ifcode);
                afList.add(item);
            }
            else if(linesplit[0].equals("message")){
                AFEditorItem item = new AFEditorItem();
                item.setName("Poruka");
                item.setType("message");
                item.setData(line.substring(8));
                item.setCode(line);
                afList.add(item);
            }
            else if(linesplit[0].equals("run")){
                AFEditorItem item = new AFEditorItem();
                item.setName("Pokreni act");
                item.setType("run");
                item.setData(line.substring(4));
                item.setCode(line);
                afList.add(item);
            }
            else if(linesplit[0].equals("call")){
                AFEditorItem item = new AFEditorItem();
                item.setName("Pozovi");
                item.setType("call");
                item.setData(line.substring(5));
                item.setCode(line);
                afList.add(item);
            }
            else if(linesplit[0].equals("web")){
                AFEditorItem item = new AFEditorItem();
                item.setName("Otvori web stranicu");
                item.setType("web");
                item.setData(line.substring(4));
                item.setCode(line);
                afList.add(item);
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

    public void saveChanges(View view){
        String appcode = "";
        for(String key : Acts.keySet()){
            appcode += "act "+key;
            appcode += Acts.get(key)+"\n";
            appcode += ";\n\n";
            Log.i("KODUO?ACT",key);
        }
        for(String key : Funs.keySet()){
            appcode += "fun "+key;
            appcode += Funs.get(key)+"\n";
            appcode += ";\n\n";
            Log.i("KODUO?FUN",key);
        }
        Intent intent = new Intent(this,viseditor.class);
        intent.putExtra("shortname",shortname);
        intent.putExtra("name",name);
        intent.putExtra("main",main);
        intent.putExtra("description",description);
        intent.putExtra("icon",icon);
        intent.putExtra("code",appcode);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Da li si siguran da želiš da izađeš bez sačuvanih promena?")
                .setTitle("Povratak u meni");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                goBack();
            }
        });
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goBack(){
        super.onBackPressed();
    }
}
