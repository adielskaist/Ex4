package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Connect.
     * what happand when we press connect.
     * @param view
     */
    public void Connect(View view) {
        /*getting the ip and port from the text box*/
        EditText editTextIp = findViewById(R.id.IP);
        EditText editTextPort = findViewById(R.id.Port);
        String IP = editTextIp.getText().toString();
        String port = editTextPort.getText().toString();

        Intent intent = new Intent(this, JoystickActivity.class);
        intent.putExtra("IP", IP);
        intent.putExtra("port", port);

        startActivity(intent);
    }
}
