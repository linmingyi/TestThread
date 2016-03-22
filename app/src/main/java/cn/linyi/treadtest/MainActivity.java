package cn.linyi.treadtest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    private SeekBar progress;
    private ServiceConnection sc;
    private Handler handler;
    private Intent service;
    private MyService.MyBinder myService;
    private boolean isrun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (SeekBar)findViewById(R.id.progress);
        service = new Intent("Myservice");

       bindService(service, sc = new ServiceConnection() {
           @Override
           public void onServiceConnected(ComponentName name, IBinder service) {
               myService = (MyService.MyBinder) service;
               Thread t =  new Thread(){
                   @Override
                   public void run() {
                       while (progress.getProgress()<=300 && isrun){
                           try {
                              Thread.sleep(300);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           Log.i("LIN",myService.getProgress()+"             progress");
                           progress.setProgress(myService.getProgress());
                           Log.i("LIN", progress.getProgress() + "             111progress");
                       }
                   }
               };
               t.setDaemon(true);
               t.start();
           }

           @Override
           public void onServiceDisconnected(ComponentName name) {

           }
       }, BIND_AUTO_CREATE);



    }

    public void play(View view) {
        service.putExtra("action",1);
        startService(service);
    }

    public void pasue(View view) {
        service.putExtra("action",2);
        startService(service);
    }


    public void resume(View view) {
        service.putExtra("action",3);
        startService(service);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isrun = true;
    }

    @Override
    protected void onStop() {
        isrun = false;
        Log.i("LIN","activity is stop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbindService(sc);
        super.onDestroy();
    }
}
