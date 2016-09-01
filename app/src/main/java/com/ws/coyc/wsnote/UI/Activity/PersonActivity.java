package com.ws.coyc.wsnote.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.Person;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.UI.Adapter.PersonInfoAdapter;

import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity {

    private ArrayList<Person> persons;
    private PersonInfoAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        DataManager.getInstance().initPersonInfo();
        persons = DataManager.getInstance().persons;

        adapter = new PersonInfoAdapter(persons,getBaseContext());

        listView = (ListView) findViewById(R.id.lv_list);
        listView.setAdapter(adapter);
    }
}
