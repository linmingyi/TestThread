package cn.linyi.treadtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private int progress=0;
    private boolean ispasue=false;
    public MyService() {
    }

    class MyBinder extends Binder {
            public int getProgress(){
                return  progress;
            }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra("action",0);

        switch (action){
            case 1:
                play();
                break;
            case 2:
                pasue();
                break;
            case 3:
                resume();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void play() {

        Thread t = new Thread(){
            @Override
            public void run() {
                synchronized (MyService.this){
                    while(progress<=300){
                    if(ispasue){
                        try {
                            MyService.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progress++;
                        Log.i("LIN",progress+"       ");
                        super.run();
                    }
                }
            }
        };
        t.start();
    }
    public void   pasue() {

            Log.i("LIN","pasuepasuepasuepasue");
            ispasue = true;

    }
    public void resume() {
        synchronized (MyService.this){
        ispasue = false;
        notify();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {


        return new MyBinder();
    }
}
