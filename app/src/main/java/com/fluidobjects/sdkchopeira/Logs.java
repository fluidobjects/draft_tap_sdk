package com.fluidobjects.sdkchopeira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fluidobjects.drafttapcontroller.DraftTapController;
import com.fluidobjects.drafttapcontroller.LogObj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logs extends AppCompatActivity {

    int initialFator = 5000;
    String ip = "192.168.0.128";
    DraftTapController chopeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
    }

    public void pesquisar(View v){
        ArrayList<LogObj> logs;
        EditText inicio = findViewById(R.id.inicio);
        EditText fim = findViewById(R.id.fim);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date1 = dateFormat.parse(inicio.getText().toString());
            Date date2 = dateFormat.parse(fim.getText().toString());
            logs = DraftTapController.getLogs(getApplicationContext(), date1,date2);
        } catch (ParseException e) {
            logs = DraftTapController.getLogs(getApplicationContext());
        }
        TableLayout tl = findViewById(R.id.tabela);
        tl.removeAllViews();
        tl.setColumnStretchable(0, true);
        tl.setColumnStretchable(1, true);
        tl.setColumnStretchable(2, true);
        for (LogObj l: logs) {
            TableRow tr = new TableRow(this);
            TextView sv = new TextView(this);
            sv.setText(String.valueOf(l.servedVolume));
            tr.addView(sv);
            TextView pf = new TextView(this);
            pf.setText(String.valueOf(l.pulseFactor));
            tr.addView(pf);
            TextView dt = new TextView(this);
            String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(l.date);
            dt.setText(String.valueOf(dateStr));
            tr.addView(dt);
            tr.setDividerPadding(20);
            tl.addView(tr);
        }
    }

}
