package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class funadditem extends AppCompatActivity {

    String shortname,name,main,description,icon,functionname,type;
    String codebefore,codeafter,itemcode,itemtype;
    HashMap<String, String> Acts, Funs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funadditem);

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

        Button bMessage = findViewById(R.id.bMessage);
        Button bRun = findViewById(R.id.bRun);
        Button bCall = findViewById(R.id.bCall);
        Button bWeb = findViewById(R.id.bWeb);
        Button bIf = findViewById(R.id.bIf);
        Button bSet = findViewById(R.id.bSet);
        Button bMerge = findViewById(R.id.bMerge);
        Button bRunfun = findViewById(R.id.bRunfun);
        bMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "message \"Promeni me\"";
                itemtype = "message";
                runItemEditor();
            }
        });
        bRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "run PromeniMe";
                itemtype = "run";
                runItemEditor();
            }
        });
        bCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "call \"Promeni me\"";
                itemtype = "call";
                runItemEditor();
            }
        });
        bWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "web \"Promeni me\"";
                itemtype = "web";
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
        bMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "merge var1,var2";
                itemtype = "merge";
                runItemEditor();
            }
        });
        bRunfun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemcode = "runfun fun1";
                itemtype = "runfun";
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
