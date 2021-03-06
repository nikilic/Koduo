package com.prvaci.koduo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;

public class viseditor extends AppCompatActivity implements EditorAFAdapter.ItemClickListener {

    String shortname,name,main,code,description,icon;
    String[] actlist,funlist;
    HashMap<String, String> Acts, Funs;
    RecyclerView recyclerActs;
    EditorAFAdapter actsAdapter;
    RecyclerView.LayoutManager actsLayoutManager;
    EditText etName,etDescription,etIcon;
    Spinner spMain;
    int pos;
    ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viseditor);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences sharedPref = this.getSharedPreferences(
                "prvaci-koduo", Context.MODE_PRIVATE);
        boolean firstTime = sharedPref.getBoolean("VisEditorFirstTime",true);
        if(firstTime){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Ovo je vizuelni editor.\n\nOvde možeš da upravljaš svim delovima" +
                    " svoje aplikacije. Aplikacije se sastoje iz act-ova i fun-ova.\nAct je funkcija koja" +
                    " će po svom izvršenju prikazati novi ekran.\nFun je funkcija koja će po svom" +
                    " izvršenju obaviti neku radnju.\n\nViše o ovome možeš pročitati u uputstvu!")
                    .setTitle("Vizuelni editor");
            builder.setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.setNeutralButton("Otvori upustvo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(viseditor.this,tutorial.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("VisEditorFirstTime",false);
            editor.apply();
        }

        Intent start =  getIntent();
        shortname = start.getStringExtra("shortname");
        name = start.getStringExtra("name");
        main = start.getStringExtra("main");
        code = start.getStringExtra("code");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");
        ivIcon = findViewById(R.id.ivIcon);
        ImageRequest ir = new ImageRequest(icon, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // callback
                ivIcon.setImageBitmap(response);
            }
        }, 1024, 1024, null, null);
        VolleySingleton.getInstance(this).addToRequestQueue(ir);

        TextView tvShortname = findViewById(R.id.tvShortname);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etIcon = findViewById(R.id.etIcon);
        spMain = findViewById(R.id.spMain);

        tvShortname.setText(shortname);
        etName.setText(name);
        etDescription.setText(description);
        etIcon.setText(icon);

        Acts = new HashMap<>();
        Funs = new HashMap<>();
        initcode(code);
        actlist = new String[Acts.size()];
        funlist = new String[Funs.size()];
        int i = 0;
        for (String act : Acts.keySet()) {
            if(act.replaceAll("\\s+","").equals(main.replaceAll("\\s+",""))) {
                pos = i;
                //Log.i("KODUOACT",act);
            }
            actlist[i] = act;
            i++;
        }
        i = 0;
        for (String fun : Funs.keySet()) {
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
            intent.putExtra("functionname",actlist[position]);
            intent.putExtra("type","act");
        }else{
            intent.putExtra("functionname",funlist[position-actlist.length]);
            intent.putExtra("type","fun");
        }
        intent.putExtra("Acts",Acts);
        intent.putExtra("Funs",Funs);
        intent.putExtra("shortname",shortname);
        intent.putExtra("name",etName.getText().toString());
        intent.putExtra("main",spMain.getSelectedItem().toString());
        intent.putExtra("description",etDescription.getText().toString());
        intent.putExtra("icon",etIcon.getText().toString());
        startActivity(intent);
    }

    public void updateApp(View view){
        if(!etName.getText().toString().trim().equals("")) {
            if(!etDescription.getText().toString().trim().equals("")) {
                Intent intent = new Intent(this, updateapp.class);
                intent.putExtra("shortname", shortname);
                intent.putExtra("name", etName.getText().toString());
                intent.putExtra("main", spMain.getSelectedItem().toString());
                intent.putExtra("description", etDescription.getText().toString());
                intent.putExtra("code", code);
                intent.putExtra("icon", etIcon.getText().toString());
                startActivity(intent);
            }else{
                etDescription.setError("Opis aplikacije ne može biti prazan");
            }
        }else{
            etName.setError("Ime aplikacije ne može biti prazno");
        }
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
        intent.putExtra("icon",etIcon.getText().toString());
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
        intent.putExtra("icon",etIcon.getText().toString());
        intent.putExtra("Acts",Acts);
        intent.putExtra("Funs",Funs);
        startActivity(intent);
    }
}
