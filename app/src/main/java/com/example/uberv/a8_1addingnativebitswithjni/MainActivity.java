package com.example.uberv.a8_1addingnativebitswithjni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInfoText = (TextView) findViewById(R.id.text_info);
        getInfo();
    }

    private void getInfo() {
        String text = String.format("%s CPU with %d core(s)",
                NativeLib.getCpuFamily(),
                NativeLib.getCpuCount());
        mInfoText.setText(text);
    }
}
