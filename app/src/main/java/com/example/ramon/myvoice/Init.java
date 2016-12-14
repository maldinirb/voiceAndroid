package com.example.ramon.myvoice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class Init extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    TextView tv1;
    MediaRecorder recorder;
    MediaPlayer player;
    File archivo;
    boolean grabando;
    boolean reproduciendo;
    private static String TAG = "PermissionDemo";
    private static final int RECORD_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequest();
        }

        tv1 = (TextView) this.findViewById(R.id.info_text);
        //
        //
        grabando = false;
        reproduciendo = false;

        File path = new File(this.getFilesDir().getPath());
        try {
            archivo = File.createTempFile("temporal", ".3gp", path);
        } catch (IOException e) {
        }

    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_REQUEST_CODE);
    }

    public void grabar() {

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recorder.setOutputFile(archivo.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
        }
        recorder.start();
        tv1.setText("Grabando");
        grabando = true;
    }

    public void detener() {

        recorder.stop();
        recorder.release();

        tv1.setText("Detenido");
        grabando = false;
    }

    public void reproducir() {

        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        try {
            player.setDataSource(archivo.getAbsolutePath());
        } catch (IOException e) {
        }
        try {
            player.prepare();
        } catch (IOException e) {}

        player.start();
        tv1.setText("Reproduciendo");
        reproduciendo = true;
    }

    public void onCompletion(MediaPlayer mp) {

        tv1.setText("Listo");
        if(player.isPlaying())
            player.stop();
        player.reset();

        reproduciendo = false;
    }

    public void record(View view){

        if(grabando) {
            detener();
            return;
        }else{
            if(reproduciendo){
                return;
            }else{
                grabar();
            }
        }
    }

    public void play(View view){

        if(grabando) {
            return;
        }else{
            if(reproduciendo){
                return;
            }else{
                reproducir();
            }
        }
    }
}
