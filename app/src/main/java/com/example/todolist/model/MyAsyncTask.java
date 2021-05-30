package com.example.todolist.model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.todolist.R;

public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
    private Activity context;

    public MyAsyncTask(Activity context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 1; i <= 100; i ++){
            SystemClock.sleep(100);
            publishProgress(i);

            if (isCancelled()) break;
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressBar bar = context.findViewById(R.id.progressCat);
        int num = values[0];
        bar.setProgress(num);
        TextView tv = context.findViewById(R.id.tvMeow);
        tv.setText(num + "%");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Button btn = context.findViewById(R.id.moreCatBtn);
        btn.setEnabled(true);
    }
}
