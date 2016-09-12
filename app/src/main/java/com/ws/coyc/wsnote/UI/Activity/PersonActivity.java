package com.ws.coyc.wsnote.UI.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.Person;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.UI.Adapter.PersonInfoAdapter;

import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity {

    private ArrayList<Person> persons;
    private PersonInfoAdapter adapter;
    private ListView listView;

    private ImageButton ib_chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        l.l("2233");
        Toast.makeText(getBaseContext(),"q",Toast.LENGTH_SHORT).show();


        DataManager.getInstance().initPersonInfo();
        persons = DataManager.getInstance().persons;

        adapter = new PersonInfoAdapter(persons,getBaseContext());

        listView = (ListView) findViewById(R.id.lv_list);
        listView.setAdapter(adapter);
        ib_chart = (ImageButton) findViewById(R.id.ib_chart);
        ib_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonActivity.this,BarChartActivity.class);
                startActivity(intent);
            }
        });
    }

}
