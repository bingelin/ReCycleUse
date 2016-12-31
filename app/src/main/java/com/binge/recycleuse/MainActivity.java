package com.binge.recycleuse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private CycleRotationView cycleRotationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cycleRotationView = (CycleRotationView) findViewById(R.id.cycleRotationView);

        String[] urls = {"http://p1.so.qhmsg.com/t01514641c357a98c81.jpg", "http://p4.so.qhmsg.com/t01244e62a3f44edf24.jpg", "http://p4.so.qhmsg.com/t01f017b2c06cc1124e.jpg"};
        cycleRotationView.setUrls(urls);

        // 点击事件
        cycleRotationView.setOnItemClickListener(new CycleRotationView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Click = " + position, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
