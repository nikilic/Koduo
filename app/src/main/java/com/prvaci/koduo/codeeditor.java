package com.prvaci.koduo;

import android.content.Intent;
import android.graphics.Color;
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
        /*txt.addTextChangedListener(new TextWatcher() {
            boolean spejs = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i1 == i2) spejs = true;
                else spejs = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!spejs) {
                    String str = editable.toString();
                    final SpannableString text = new SpannableString(str);
                    for(int i = 0; i < reci.length; i++) {
                        String rec = reci[i];
                        int len = rec.length();

                        int index = str.indexOf(rec);
                        while (index >= 0) {
                            boolean b1 = (index == 0) || Character.isWhitespace(str.charAt(index - 1)) || str.charAt(index - 1) == '\n' || str.charAt(index - 1) == '\r';
                            boolean b2 = (index == str.length() - len) || Character.isWhitespace(str.charAt(index + len)) || str.charAt(index + len) == '\n' || str.charAt(index + len) == '\r';

                            if (b1 && b2) {
                                text.setSpan(new ForegroundColorSpan(Color.parseColor(boje[i])), index, index + len, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            }
                            index = str.indexOf(rec, index + 1);
                        }
                    }
                    int sel = txt.getSelectionStart();
                    txt.setText(text);
                    txt.setSelection(sel);
                }

            }
        });*/
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
}
