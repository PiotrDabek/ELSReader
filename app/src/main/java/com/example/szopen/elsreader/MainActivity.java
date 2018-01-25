package com.example.szopen.elsreader;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ELSViewer {
    private TextView textView2;
    private TextView textView4;
    private TextView textView6;
    private TextView textView8;
    private TextView textView10;
    private ELSReader elsReader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView2 = (TextView) findViewById(R.id.textView2);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView10 = (TextView) findViewById(R.id.textView10);

        elsReader = new ELSReader(this.getApplicationContext(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        elsReader.nfcAdapter.enableReaderMode(this, elsReader,
                NfcAdapter.FLAG_READER_NFC_A |
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null);
    }

    @Override
    public void onPause() {
        super.onPause();
        elsReader.nfcAdapter.disableReaderMode(this);
    }


    @Override
    public void ELSStarted(String s) {

    }

    @Override
    public void onCardConnected() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    StudentInfo si = elsReader.readEFELS();
                    textView2.setText(si.names);
                    textView4.setText(si.surname);
                    textView6.setText(si.index);
                    textView8.setText(si.pesel);
                    textView10.setText(si.university);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


}
