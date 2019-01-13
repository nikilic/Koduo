package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class viseditor extends AppCompatActivity implements EditorAFAdapter.ItemClickListener {

    String shortname,name,main,code,description,icon;
    String[] actlist,funlist;
    HashMap<String, String> Acts, Funs;
    RecyclerView recyclerActs;
    EditorAFAdapter actsAdapter;
    RecyclerView.LayoutManager actsLayoutManager;
    EditText etName,etDescription;
    Spinner spMain;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viseditor);

        Intent start =  getIntent();
        shortname = start.getStringExtra("shortname");
        name = start.getStringExtra("name");
        main = start.getStringExtra("main");
        Log.i("KODUOMAIN",main);
        code = start.getStringExtra("code");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");
        Log.i("KODUOCODE",code);
        Log.i("SHORTNAME",shortname);

        TextView tvShortname = findViewById(R.id.tvShortname);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        spMain = findViewById(R.id.spMain);

        tvShortname.setText(shortname);
        etName.setText(name);
        etDescription.setText(description);

        Acts = new HashMap<>();
        Funs = new HashMap<>();
        initcode(code);
        Log.i("ACTS",Acts.toString());
        Log.i("FUNS",Funs.toString());
        actlist = new String[Acts.size()];
        funlist = new String[Funs.size()];
        int i = 0;
        for (String act : Acts.keySet()) {
            Log.i("KODUOACT",act);
            if(act.replaceAll("\\s+","").equals(main.replaceAll("\\s+",""))) {
                pos = i;
                //Log.i("KODUOACT",act);
            }
            actlist[i] = act;
            i++;
        }
        i = 0;
        for (String fun : Funs.keySet()) {
            Log.i("KODUOFUN",fun);
            funlist[i] = fun;
            i++;
        }

        recyclerActs = findViewById(R.id.recyclerActs);
        recyclerActs.setHasFixedSize(true);
        actsLayoutManager = new LinearLayoutManager(this);
        recyclerActs.setLayoutManager(actsLayoutManager);
        String[] allList = new String[actlist.length+funlist.length];
        for(int j = 0; j < actlist.length; j++){
            allList[j] = "Act: " + actlist[j];
        }
        for(int j = 0; j < funlist.length; j++){
            allList[j+actlist.length] = "Fun: " + funlist[j];
        }
        actsAdapter = new EditorAFAdapter(this,Arrays.asList(allList));
        actsAdapter.setClickListener(this);
        recyclerActs.setAdapter(actsAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        actlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMain.setAdapter(adapter);
        spMain.setSelection(pos);
    }

    public void initcode(String code){
        BufferedReader codeReader = new BufferedReader(new StringReader(code));
        String line = lineReader(codeReader);
        while(!line.equals("error")){
            Log.i("KODUOLINE",line);
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

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this,afeditor.class);
        if (position+1<=actlist.length){
            Log.i("KODUO?","acts");
            intent.putExtra("functionname",actlist[position]);
            intent.putExtra("type","act");
        }else{
            Log.i("KODUO?","funs");
            intent.putExtra("functionname",funlist[position-actlist.length]);
            intent.putExtra("type","fun");
        }
        intent.putExtra("Acts",Acts);
        intent.putExtra("Funs",Funs);
        intent.putExtra("shortname",shortname);
        intent.putExtra("name",name);
        intent.putExtra("main",main);
        intent.putExtra("description",description);
        intent.putExtra("icon",icon);
        startActivity(intent);
    }

    public void updateApp(View view){
        Intent intent = new Intent(this,updateapp.class);
        intent.putExtra("shortname",shortname);
        intent.putExtra("name",etName.getText().toString());
        intent.putExtra("main",spMain.getSelectedItem().toString());
        intent.putExtra("description",etDescription.getText().toString());
        intent.putExtra("code",code);
        intent.putExtra("icon",icon);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Da li si siguran da želiš da izađeš bez da ažuriraš aplikaciju?")
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

    public void addAct(View view){
        Intent intent = new Intent(this,addaf.class);
        intent.putExtra("type","act");
        intent.putExtra("shortname",shortname);
        intent.putExtra("name",etName.getText().toString());
        intent.putExtra("main",spMain.getSelectedItem().toString());
        intent.putExtra("description",etDescription.getText().toString());
        intent.putExtra("code",code);
        intent.putExtra("icon",icon);
        intent.putExtra("Acts",Acts);
        intent.putExtra("Funs",Funs);
        startActivity(intent);
    }

    public void addFun(View view){
        Intent intent = new Intent(this,addaf.class);
        intent.putExtra("type","fun");
        intent.putExtra("shortname",shortname);
        intent.putExtra("name",etName.getText().toString());
        intent.putExtra("main",spMain.getSelectedItem().toString());
        intent.putExtra("description",etDescription.getText().toString());
        intent.putExtra("code",code);
        intent.putExtra("icon",icon);
        intent.putExtra("Acts",Acts);
        intent.putExtra("Funs",Funs);
        startActivity(intent);
    }
}
