package com.prvaci.koduo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;

public class codeeditor extends AppCompatActivity {

    private EditTextSaRedovima txt;
    String name,shortname,main,code,description,icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codeeditor);

        Intent start = getIntent();
        name = start.getStringExtra("name");
        shortname = start.getStringExtra("shortname");
        main = start.getStringExtra("main");
        code = start.getStringExtra("code");
        description = start.getStringExtra("description");
        icon = start.getStringExtra("icon");

        txt = findViewById(R.id.editText);

        txt.setText(code);
    }

    public void saveChanges(View view){
        Intent intent = new Intent(this,updateapp.class);
        intent.putExtra("shortname",shortname);
        intent.putExtra("name",name);
        intent.putExtra("main",main);
        intent.putExtra("description",description);
        intent.putExtra("code",txt.getText().toString());
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
