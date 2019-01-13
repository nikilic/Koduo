package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class addaf extends AppCompatActivity {

    String type,shortname,name,main,code,description,icon;
    EditText etName;
    HashMap<String, String> Acts, Funs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaf);
        etName = findViewById(R.id.etName);
        TextView tvTitle = findViewById(R.id.tvTitle);
        Intent start = getIntent();
        type = start.getStringExtra("type");
        shortname = start.getStringExtra("shortname");
        name = start.getStringExtra("name");
        main = start.getStringExtra("main");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");
        code = start.getStringExtra("code");
        Acts = (HashMap<String, String>) start.getSerializableExtra("Acts");
        Funs = (HashMap<String, String>) start.getSerializableExtra("Funs");
        if(type.equals("act")){
            tvTitle.setText("Ukucaj ime novog act-a");
        }else{
            tvTitle.setText("Ukucaj ime novog fun-a");
        }
    }

    public void add(View view){
        if(!etName.getText().toString().equals("")){
            if(type.equals("act")){
                if(!Acts.containsKey(etName.getText().toString().toLowerCase().replaceAll("\\s+",""))) {
                    code += "\nact " + etName.getText().toString().toLowerCase().replaceAll("\\s+", "") + "\n;\n";
                    Intent intent = new Intent(this,viseditor.class);
                    intent.putExtra("shortname",shortname);
                    intent.putExtra("name",name);
                    intent.putExtra("main",main);
                    intent.putExtra("description",description);
                    intent.putExtra("code",code);
                    intent.putExtra("icon",icon);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Ovaj act već postoji", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(!Funs.containsKey(etName.getText().toString().toLowerCase().replaceAll("\\s+",""))) {
                    code += "\nfun " + etName.getText().toString().toLowerCase().replaceAll("\\s+", "") + "\n;\n";
                    Intent intent = new Intent(this,viseditor.class);
                    intent.putExtra("shortname",shortname);
                    intent.putExtra("name",name);
                    intent.putExtra("main",main);
                    intent.putExtra("description",description);
                    intent.putExtra("code",code);
                    intent.putExtra("icon",icon);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Ovaj fun već postoji", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this, "Ime ne može biti prazno", Toast.LENGTH_SHORT).show();
        }
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
