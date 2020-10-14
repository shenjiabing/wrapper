package com.sayweee.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sayweee.app2.launch.LaunchActivity;
import com.sayweee.app2.test.TestActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                startActivity(new Intent(this, TestActivity.class));
                break;
            case R.id.btn_launch:
                startActivity(new Intent(this, LaunchActivity.class));
                break;
        }
    }
}
