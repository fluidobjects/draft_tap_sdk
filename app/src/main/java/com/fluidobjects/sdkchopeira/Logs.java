package com.fluidobjects.sdkchopeira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Logs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
    }

    public void pesquisar(View v){
        TableLayout tl = (TableLayout) findViewById(R.id.tabela);
        TextView tv = new TextView(this);
        tv.setText("boa1");
        TableRow tr = new TableRow(this);
        tr.addView(tv);
        tl.addView(tr);
    }

}
