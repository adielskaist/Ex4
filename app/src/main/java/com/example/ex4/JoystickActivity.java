package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener{

    String IP;
    String port;
    TcpClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        IP = getIntent().getStringExtra("IP");
        port = getIntent().getStringExtra("port");
        new ConnectTask().execute("");

        JoystickView jv = new JoystickView(this);
        setContentView(jv);
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {


        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            }, IP, Integer.parseInt(port));
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }


    }
    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        Log.d("Main Method", "X percent: " + xPercent + " Y percent: " + yPercent);
        mTcpClient.sendMessage("set /controls/flight/elevator " + -yPercent + "\r\n");
        mTcpClient.sendMessage("set /controls/flight/aileron " + xPercent + "\r\n");
    }
}
