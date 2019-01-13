package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class afitemeditor extends AppCompatActivity {

    String prop1,prop2,prop3,shortname,name,main,description,icon,functionname,type;
    String codebefore,codeafter,itemcode,itemtype;
    TextView tvCommand,tvProperty1,tvProperty2,tvProperty3,tvProperty4;
    EditText etProperty1,etProperty2,etProperty3,etProperty4;
    HashMap<String, String> Acts, Funs;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afitemeditor);

        tvCommand = findViewById(R.id.tvCommand);
        tvProperty1 = findViewById(R.id.tvProperty1);
        etProperty1 = findViewById(R.id.etProperty1);
        tvProperty2 = findViewById(R.id.tvProperty2);
        etProperty2 = findViewById(R.id.etProperty2);
        tvProperty3 = findViewById(R.id.tvProperty3);
        etProperty3 = findViewById(R.id.etProperty3);
        tvProperty4 = findViewById(R.id.tvProperty4);
        etProperty4 = findViewById(R.id.etProperty4);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        Intent start = getIntent();
        codebefore = start.getStringExtra("codebefore");
        codeafter = start.getStringExtra("codeafter");
        Log.i("CODEBEFORE",codebefore);
        Log.i("CODEAFTER",codeafter);
        itemcode = start.getStringExtra("itemcode");
        itemtype = start.getStringExtra("itemtype");
        shortname = start.getStringExtra("shortname");
        name = start.getStringExtra("name");
        main = start.getStringExtra("main");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");
        functionname = start.getStringExtra("functionname");
        type = start.getStringExtra("type");
        Acts = (HashMap<String, String>) start.getSerializableExtra("Acts");
        Funs = (HashMap<String, String>) start.getSerializableExtra("Funs");

        String vars = "";

        switch (itemtype){
            case "text":
                tvCommand.setText("Tekst");
                tvProperty1.setText("Tekst");
                tvProperty2.setText("Boja");
                tvProperty3.setText("Veličina");
                vars = itemcode.substring(9);
                count = 3;
                break;
            case "button":
                tvCommand.setText("Dugme");
                tvProperty1.setText("Tekst");
                tvProperty2.setText("Fun koji se pokreće");
                vars = itemcode.substring(11);
                count = 2;
                break;
            case "message":
                tvCommand.setText("Poruka");
                tvProperty1.setText("Tekst");
                vars = itemcode.substring(8);
                count = 1;
                break;
            case "run":
                tvCommand.setText("Pokreni act");
                tvProperty1.setText("Act");
                vars = itemcode.substring(4);
                count = 1;
                break;
            case "if":
                tvCommand.setText("IF komanda");
                tvProperty1.setText("Promenljiva 1");
                tvProperty2.setText("Promenljiva 2");
                tvProperty3.setText("Ispunjen uslov");
                tvProperty4.setText("Neispunjen uslov");
                etProperty1.setText("");
                //TODO if komanda
                break;
            case "set":
                tvCommand.setText("SET komanda");
                tvProperty1.setText("Promenljiva 1");
                tvProperty2.setText("Promenljiva 2");
                etProperty1.setText(itemcode.split(" ")[0]);
                if(itemcode.split(" ")[2].toCharArray()[0] == '\"'){
                    etProperty2.setText("\""+itemcode.split("\"")[1]+"\"");
                }else{
                    etProperty2.setText(itemcode.split(" ")[2]);
                }
                tvProperty3.setAlpha(0);
                etProperty3.setAlpha(0);
                etProperty3.setInputType(InputType.TYPE_NULL);
                tvProperty4.setAlpha(0);
                etProperty4.setAlpha(0);
                etProperty4.setInputType(InputType.TYPE_NULL);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.bSave,ConstraintSet.TOP,R.id.etProperty2,ConstraintSet.BOTTOM,8);
                constraintSet.applyTo(constraintLayout);
                break;
            case "call":
                tvCommand.setText("Pozovi");
                tvProperty1.setText("Broj");
                vars = itemcode.substring(5);
                count = 1;
                break;
            case "web":
                tvCommand.setText("Otvori web stranicu");
                tvProperty1.setText("URL");
                vars = itemcode.substring(4);
                count = 1;
                break;
        }

        if(!itemtype.equals("set") && !itemtype.equals("if")){
            separate(count,vars);
            tvProperty4.setAlpha(0);
            etProperty4.setAlpha(0);
            switch (count){
                case 3:
                    etProperty1.setText(prop1);
                    etProperty2.setText(prop2);
                    etProperty3.setText(prop3);
                    ConstraintSet constraintSet3 = new ConstraintSet();
                    constraintSet3.clone(constraintLayout);
                    constraintSet3.connect(R.id.bSave,ConstraintSet.TOP,R.id.etProperty3,ConstraintSet.BOTTOM,8);
                    constraintSet3.applyTo(constraintLayout);
                    break;
                case 2:
                    tvProperty3.setAlpha(0);
                    etProperty3.setAlpha(0);
                    etProperty3.setInputType(InputType.TYPE_NULL);
                    etProperty1.setText(prop1);
                    etProperty2.setText(prop2);
                    ConstraintSet constraintSet2 = new ConstraintSet();
                    constraintSet2.clone(constraintLayout);
                    constraintSet2.connect(R.id.bSave,ConstraintSet.TOP,R.id.etProperty2,ConstraintSet.BOTTOM,8);
                    constraintSet2.applyTo(constraintLayout);
                    break;
                case 1:
                    tvProperty3.setAlpha(0);
                    etProperty3.setAlpha(0);
                    etProperty3.setInputType(InputType.TYPE_NULL);
                    tvProperty2.setAlpha(0);
                    etProperty2.setAlpha(0);
                    etProperty2.setInputType(InputType.TYPE_NULL);
                    etProperty1.setText(prop1);
                    ConstraintSet constraintSet1 = new ConstraintSet();
                    constraintSet1.clone(constraintLayout);
                    constraintSet1.connect(R.id.bSave,ConstraintSet.TOP,R.id.etProperty1,ConstraintSet.BOTTOM,8);
                    constraintSet1.applyTo(constraintLayout);
                    break;
            }
        }
    }

    public void separate(int n,String line){
        int num = countSubstring("\"",line);
        switch (num){
            case 0:
                if(n == 1)
                    prop1 = line;
                else if(n == 2){
                    prop1 = line.split(",")[0];
                    prop2 = line.split(",")[1];
                }else{
                    prop1 = line.split(",")[0];
                    prop2 = line.split(",")[1];
                    prop3 = line.split(",")[2];
                }
                break;
            case 2:
                if(n == 1){
                    prop1 = line;
                }else if(n == 2){
                    if(line.toCharArray()[0] == '\"'){
                        prop1 = "\""+line.split("\"")[1]+"\"";
                        prop2 = line.split("\"")[2].split(",")[1];
                    }else{
                        prop1 = line.split(",")[0];
                        prop2 = line.split(",")[1];
                    }
                }else{
                    if(line.toCharArray()[0] == '\"'){
                        prop1 = "\""+line.split("\"")[1]+"\"";
                        prop2 = line.split("\"")[2].split(",")[1];
                        prop2 = line.split("\"")[2].split(",")[2];
                    }else if(line.split(",")[1].toCharArray()[0] == '\"'){
                        prop1 = line.split(",")[0];
                        prop2 = "\""+line.split("\"")[1]+"\"";
                        prop3 = line.split("\"")[2].split(",")[1];
                    }else{
                        prop1 = line.split(",")[0];
                        prop2 = line.split(",")[1];
                        prop3 = line.split(",")[2];
                    }
                }
                break;
            case 4:
                if(n == 2){
                    prop1 = "\""+line.split("\"")[1]+"\"";
                    prop2 = "\""+line.split("\"")[3]+"\"";
                }else{
                    if(line.toCharArray()[0] == '\"'){
                        if(line.split("\"")[2].split(",")[1].toCharArray()[0] == '\"'){
                            prop1 = "\""+line.split("\"")[1]+"\"";
                            prop2 = "\""+line.split("\"")[3]+"\"";
                            prop3 = line.split("\"")[4].split(",")[1];
                        }else{
                            prop1 = "\""+line.split("\"")[1]+"\"";
                            prop2 = line.split("\"")[2].split(",")[1];
                            prop3 = "\""+line.split("\"")[3]+"\"";
                        }
                    }else{
                        prop1 = line.split(",")[0];
                        prop2 = "\""+line.split("\"")[1]+"\"";
                        prop3 = "\""+line.split("\"")[3]+"\"";
                    }
                }
                break;
            case 6:
                prop1 = "\""+line.split("\"")[1]+"\"";
                prop2 = "\""+line.split("\"")[3]+"\"";
                prop3 = "\""+line.split("\"")[5]+"\"";
                break;
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

    public void saveChanges(View view){
        String functioncode = "\n"+codebefore;
        switch (itemtype){
            case "text":
                functioncode += "add text "+etProperty1.getText()+","+etProperty2.getText()+","+etProperty3.getText()+"\n";
                break;
            case "button":
                functioncode += "add button "+etProperty1.getText()+","+etProperty2.getText()+"\n";
                break;
            case "message":
                functioncode += "message "+etProperty1.getText()+"\n";
                break;
            case "run":
                functioncode += "run "+etProperty1.getText()+"\n";
                break;
            case "if":
                //TODO if komanda
                break;
            case "set":
                functioncode += etProperty1.getText()+" set "+etProperty2.getText()+"\n";
                break;
            case "call":
                functioncode += "call "+etProperty1.getText()+"\n";
                break;
            case "web":
                functioncode += "web "+etProperty1.getText()+"\n";
                break;
        }
        functioncode += codeafter;
        Log.i("FUNCTIONCODE",functioncode);
        if(type.equals("act"))
            Acts.put(functionname,functioncode);
        else
            Funs.put(functionname,functioncode);

        Intent intent = new Intent(this,afeditor.class);
        intent.putExtra("shortname",shortname);
        intent.putExtra("name",name);
        intent.putExtra("main",main);
        intent.putExtra("description",description);
        intent.putExtra("icon",icon);
        intent.putExtra("Acts",Acts);
        intent.putExtra("Funs",Funs);
        intent.putExtra("functionname",functionname);
        intent.putExtra("type",type);
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
