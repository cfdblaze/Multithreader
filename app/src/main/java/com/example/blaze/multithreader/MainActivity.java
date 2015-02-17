package com.example.blaze.multithreader;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;


/**
 * Info from site: http://developer.android.com/training/basics/data-storage/files.html
 * Collaborated with John Decker
 */

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createClick(View view) {
        Thread createThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String filename = "numbers.txt";
                Context context = MainActivity.this;
                File file = new File(context.getFilesDir(), filename);
                BufferedWriter fout = null;
                try {
                    fout = new BufferedWriter(new FileWriter(file));
                } catch (Exception e) {
                    System.out.println("couldn't open the file");
                }
                ProgressBar pBar;
                pBar = (ProgressBar) findViewById(R.id.progressBar);
                String chunk;
                for (int i=1; i <= 10; i++) {
                    try {
                        chunk = (" " + i + " ");
                        fout.write(chunk);
                        fout.newLine();
                        pBar.setProgress(i * 10);
                        System.out.println(chunk);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            System.out.println("I don't wanna sleep");
                        }
                    } catch (IOException e) {
                        System.out.println("loop didn't work");
                    }
                }
                try {
                    fout.close();
                } catch (IOException e) {
                    System.out.println("one can never have enough try/catch");
                    e.printStackTrace();
                }
            }
        });
        createThread.start();
    }

    public void loadClick (View view) {
        Thread loadThread = new Thread (new Runnable() {
            @Override
            public void run() {
                String filename = "numbers.txt";
                Context context = MainActivity.this;
                File file = new File(context.getFilesDir(), filename);
                BufferedReader fin = null;
                try {
                    fin = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException e) {
                    System.out.println("man I really don't think this program has enough try/catches");
                    e.printStackTrace();
                }
                String line;
                ArrayList<String> myStringArray = new ArrayList<>();
                int i = 0;
                ProgressBar pBar;
                pBar = (ProgressBar) findViewById(R.id.progressBar);
                pBar.setProgress(0);
                try {
                    while ((line = fin.readLine()) != null) {
                        System.out.println("I read a line:" + line);
                        myStringArray.add(line);
                        i++;
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            System.out.println("I don't wanna sleep");
                        }
                        pBar.setProgress(i*10);
                    }
                } catch (IOException e) {
                    System.out.println("lines didn't read");
                    e.printStackTrace();
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, myStringArray);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListView writeScreen = (ListView) findViewById(R.id.listView);
                        writeScreen.setAdapter(adapter);
                    }
                });
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loadThread.start();
    }

    public void clearClick (View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        adapter.clear();
        ListView clearScreen = (ListView) findViewById(R.id.listView);
        clearScreen.setAdapter(adapter);
        ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);
        pBar.setProgress(0);
    }
}
