package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class actadditem extends AppCompatActivity {

    String shortname,name,main,description,icon,functionname,type;
    String codebefore,codeafter,itemcode,itemtype;
    HashMap<String, String> Acts, Funs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actadditem);

        Intent start = getIntent();
        codebefore = start.getStringExtra("codebefore");
        codeafter = "";
        shortname = start.getStringExtra("shortname");
        name = start.getStringExtra("name");
        main = start.getStringExtra("main");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");
        functionname = start.getStringExtra("functionname");
        type = start.getStringExtra("type");
        Acts = (HashMap<String, String>) start.getSerializableExtra("Acts");
        Funs = (HashMap<String, String>) start.getSerializableExtra("Funs");

        Button bText = findViewById(R.id.bText);
        Button bButton = findViewById(R.id.bButton);
        Button bIf = findViewById(R.id.bIf);
        Button bSet = findViewById(R.id.bSet);
        bText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "add text \"Promeni me\",\"Promeni me\",\"Promeni me\"";
                itemtype = "text";
                runItemEditor();
            }
        });
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "add button \"Promeni me\",\"Promeni me\"";
                itemtype = "button";
                runItemEditor();
            }
        });
        bIf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "if var1 is var2\nelse\nendif";
                itemtype = "if";
                runItemEditor();
            }
        });
        bSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "var1 set \"Promeni me\"";
                itemtype = "set";
                runItemEditor();
            }
        });
    }

    public void runItemEditor(){
        Intent intent = new Intent(this,afitemeditor.class);
        intent.putExtra("codebefore",codebefore+"\n");
        intent.putExtra("codeafter",codeafter);
        intent.putExtra("itemcode",itemcode);
        intent.putExtra("itemtype",itemtype);
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
