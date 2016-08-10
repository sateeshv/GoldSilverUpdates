package sateesh.com.goldsilverupdates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button gold_icon, chart_icon, search_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gold_icon = (Button) findViewById(R.id.gold_image);
        gold_icon.setOnClickListener(this);


        chart_icon = (Button) findViewById(R.id.chart_image);
        chart_icon.setOnClickListener(this);

        search_icon = (Button) findViewById(R.id.search_image);
        search_icon.setOnClickListener(this);


        FetchSheetTask task = null;
        FetchSheetTask_City city_task = null;

        try {
            city_task = new FetchSheetTask_City(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        city_task.execute();

        try {
            task = new FetchSheetTask(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        task.execute();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gold_image:
                Intent data_gold = new Intent(MainActivity.this, DataActivity.class);
                startActivity(data_gold);
                finish();
                break;

//            case R.id.silver_image:
//                Intent data_silver = new Intent(MainActivity.this, DataSilver.class);
//                startActivity(data_silver);
//
//                break;

            case R.id.chart_image:
                Intent charts = new Intent(MainActivity.this, ChartsActivity.class);
                startActivity(charts);
                finish();
                break;

            case R.id.search_image:
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);
                finish();
                break;

        }
    }
}
