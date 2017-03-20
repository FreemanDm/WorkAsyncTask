package com.freeman.asynctask.classworkasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTxt, countTxt;
    private ProgressBar myProgressBar, myProgressBarLine;
    private Button startTaskBtn, stopTaskBtn, getTaskResultBtn;
    private MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTxt = (TextView) findViewById(R.id.result_txt);
        countTxt = (TextView) findViewById(R.id.count_txt);
        myProgressBar = (ProgressBar) findViewById(R.id.my_progress);
        myProgressBarLine = (ProgressBar) findViewById(R.id.my_progress_line);
        startTaskBtn = (Button) findViewById(R.id.start_task_btn);
        stopTaskBtn = (Button) findViewById(R.id.stop_task_btn);
        getTaskResultBtn = (Button) findViewById(R.id.get_task_result_btn);
        myProgressBar.setVisibility(View.INVISIBLE);
        myProgressBarLine.setVisibility(View.INVISIBLE);
        startTaskBtn.setOnClickListener(this);
        stopTaskBtn.setOnClickListener(this);
        getTaskResultBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_task_btn:
                myTask = new MyTask();
                TaskWork taskWork = new TaskWork(myTask);
                myTask.execute(60);
                taskWork.start();
                break;
            case R.id.stop_task_btn:
                myTask.cancel(true);
                break;
            case R.id.get_task_result_btn:
                try {
//                    String result = myTask.get();
                    String result = myTask.get(3, TimeUnit.SECONDS);
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    class MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            myProgressBar.setVisibility(View.VISIBLE);
            myProgressBarLine.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values.length > 0){
                countTxt.setText(String.valueOf(values[0]));
                myProgressBarLine.setProgress(values[0]);
            }
        }

        @Override
        protected String doInBackground(Integer... params) {
            int size = 1;
            if (params.length > 0 && params[0] > size) {
                size = params[0];
            }

                for (int i = 0; i < size; i++) {
                    Log.d("MyTag", "Iteration #" + (i + 1));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(i);
                    if (isCancelled()){
                        return "Thread was stopped by User";
                    }
                }

            return "Work done";
        }

        @Override
        protected void onPostExecute(String result) {
            myProgressBar.setVisibility(View.INVISIBLE);
            myProgressBarLine.setVisibility(View.INVISIBLE);
            resultTxt.setText(result);
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "My task was cancel", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled(String s) {
            myProgressBar.setVisibility(View.INVISIBLE);
            myProgressBarLine.setVisibility(View.INVISIBLE);
            resultTxt.setText(s);
        }
    }
}
