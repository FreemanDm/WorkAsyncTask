package com.freeman.asynctask.classworkasynctask;

import android.util.Log;

import java.util.concurrent.ExecutionException;

/**
 * Created by Freeman on 09.01.2017.
 */

public class TaskWork extends Thread {
    MainActivity.MyTask myTask;

    public TaskWork(MainActivity.MyTask myTask){
        this.myTask = myTask;
    }

    @Override
    public void run() {
        try {
            String result = myTask.get();
            Log.d("THREAD", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
