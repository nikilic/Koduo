package com.prvaci.koduo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

        SharedPreferences sharedPref = this.getSharedPreferences(
                "prvaci-koduo", Context.MODE_PRIVATE);
        boolean firstTime = sharedPref.getBoolean("AFEditorFirstTime",true);
        if(firstTime){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Ovo je act/fun editor.\n\nOvde možeš da menjaš pojedinačne elemente" +
                    " i komande svoje aplikacije.\n\nNe zaboravi da sačuvaš svoje izmene!")
                    .setTitle("Act/fun editor");
            builder.setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("AFEditorFirstTime",false);
            editor.apply();
        }

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
                String newcode = "";
                for(AFEditorItem item : afList){
                    newcode += item.getCode()+"\n";
                }
                code = newcode;
                Acts.put(functionname,"\n"+code);
            }
        }else if(view.getId() == R.id.bUp){
            if(position != 0){
                AFEditorItem temp = afList.get(position);
                afList.set(position,afList.get(position-1));
                afList.set(position-1,temp);
                adapter.notifyDataSetChanged();
                String newcode = "";
                for(AFEditorItem item : afList){
                    newcode += item.getCode()+"\n";
                }
                code = newcode;
                Acts.put(functionname,"\n"+code);
            }
        }else if(view.getId() == R.id.bDelete){
            afList.remove(position);
            adapter.notifyDataSetChanged();
            String newcode = "";
            for(AFEditorItem item : afList){
                newcode += item.getCode()+"\n";
            }
            code = newcode;
            Acts.put(functionname,"\n"+code);
        }else{
            String codebefore = "";
            String codeafter = "";
            BufferedReader codeReader = new BufferedReader(new StringReader(code));
            String line = lineReader(codeReader);
            int i = 0;
            while(!line.equals("error")){
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
                ifcode += "endif";
                item.setCode(ifcode);
                afList.add(item);
            }
            else if(linesplit[0].equals("merge")){
                AFEditorItem item = new AFEditorItem();
                item.setName("MERGE komanda");
                item.setType("merge");
                item.setData(line.substring(6));
                item.setCode(line);
                afList.add(item);
            }
            else if(linesplit[0].equals("background")){
                AFEditorItem item = new AFEditorItem();
                item.setName("Boja pozadine");
                item.setType("background");
                item.setData(line.substring(11));
                item.setCode(line);
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
                }else if(linesplit[1].equals("textfield")){
                    item.setName("Tekstualno polje");
                    item.setType("textfield");
                    item.setData(line.substring(14));
                }else if(linesplit[1].equals("image")){
                    item.setName("Slika");
                    item.setType("image");
                    item.setData(line.substring(10));
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
                ifcode += "endif";
                item.setCode(ifcode);
                afList.add(item);
            }
            else if(linesplit[0].equals("merge")){
                AFEditorItem item = new AFEditorItem();
                item.setName("MERGE komanda");
                item.setType("merge");
                item.setData(line.substring(6));
                item.setCode(line);
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
            else if(linesplit[0].equals("runfun")){
                AFEditorItem item = new AFEditorItem();
                item.setName("Pokreni fun");
                item.setType("runfun");
                item.setData(line.substring(7));
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
        }
        for(String key : Funs.keySet()){
            appcode += "fun "+key;
            appcode += Funs.get(key)+"\n";
            appcode += ";\n\n";
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

    public void addItem(View view){
        if(type.equals("act")) {
            Intent intent = new Intent(this, actadditem.class);
            intent.putExtra("codebefore", code);
            intent.putExtra("Acts", Acts);
            intent.putExtra("Funs", Funs);
            intent.putExtra("functionname", functionname);
            intent.putExtra("type", type);
            intent.putExtra("shortname", shortname);
            intent.putExtra("name", name);
            intent.putExtra("main", main);
            intent.putExtra("description", description);
            intent.putExtra("icon", icon);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, funadditem.class);
            intent.putExtra("codebefore", code);
            intent.putExtra("Acts", Acts);
            intent.putExtra("Funs", Funs);
            intent.putExtra("functionname", functionname);
            intent.putExtra("type", type);
            intent.putExtra("shortname", shortname);
            intent.putExtra("name", name);
            intent.putExtra("main", main);
            intent.putExtra("description", description);
            intent.putExtra("icon", icon);
            startActivity(intent);
        }
    }
}
