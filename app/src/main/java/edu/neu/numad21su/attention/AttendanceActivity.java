package edu.neu.numad21su.attention;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
//import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;


public class AttendanceActivity extends AppCompatActivity {

  Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
          buttonStopPlayingRecording ;
  String AudioSavePathInDevice = null;
  MediaRecorder mediaRecorder ;
  Random random ;
  String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
  public static final int RequestPermissionCode = 1;
  MediaPlayer mediaPlayer ;

  private ImageView iv_mic;
  private TextView tv_Speech_to_text;
  private static final int REQUEST_CODE_SPEECH_INPUT = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_attendance);

    // voice recognizer
    iv_mic = findViewById(R.id.iv_mic);
    tv_Speech_to_text = findViewById(R.id.tv_speech_to_text);

    iv_mic.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v)
      {
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

        try {
          startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e) {
          Toast
                  .makeText(AttendanceActivity.this, " " + e.getMessage(),
                          Toast.LENGTH_SHORT)
                  .show();
        }
      }
    });
    //

    buttonStart = (Button) findViewById(R.id.button);
    buttonStop = (Button) findViewById(R.id.button2);
    buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
    buttonStopPlayingRecording = (Button)findViewById(R.id.button4);

    buttonStop.setEnabled(false);
    buttonPlayLastRecordAudio.setEnabled(false);
    buttonStopPlayingRecording.setEnabled(false);

    random = new Random();

    buttonStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if(checkPermission()) {

          AudioSavePathInDevice =
                  Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                          CreateRandomAudioFileName(5) + "AudioRecording.3gp";


          //
          try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
              root.mkdirs();
            }
            File gpxfile = new File(root, "testfile");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append("hello world");
            writer.flush();
            writer.close();
            Toast.makeText(AttendanceActivity.this, "Saved", Toast.LENGTH_SHORT).show();
          } catch (IOException e) {
            e.printStackTrace();
          }

          //
          //
          //java.io.File AudioSavePathInDevice = new java.io.File(Environment
          //        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
          //        + "/AudioRecording.3gp");

          //java.io.File AudioSavePathInDevice = new java.io.File((AttendanceActivity.this
          //        .getApplicationContext().getFileStreamPath("AudioRecording.3gp")
          //        .getPath()));

          //
          /*
          File directory = new File(AudioSavePathInDevice).getParentFile();
          if (!directory.exists() && !directory.mkdirs()) {
            try {
              throw new IOException("Path to file could not be created.");
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          */
          //

          MediaRecorderReady();

          //
          String state = android.os.Environment.getExternalStorageState();
          if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
            try {
              throw new IOException("SD Card is not mounted.  It is " + state + ".");
            } catch (IOException e) {
              e.printStackTrace();
            }
          }

          //

          try {
            mediaRecorder.prepare();
            mediaRecorder.start();
          } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          buttonStart.setEnabled(false);
          buttonStop.setEnabled(true);

          Toast.makeText(AttendanceActivity.this, "Recording started",
                  Toast.LENGTH_LONG).show();
        } else {
          requestPermission();
        }

      }
    });

    buttonStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (mediaRecorder != null) {
          try {
            mediaRecorder.stop();
            mediaRecorder.release();
          } catch(RuntimeException ex){
            //Ignore
          }
        }
        //mediaRecorder.stop();
        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(true);
        buttonStart.setEnabled(true);
        buttonStopPlayingRecording.setEnabled(false);

        Toast.makeText(AttendanceActivity.this, "Recording Completed",
                Toast.LENGTH_LONG).show();
      }
    });

    buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) throws IllegalArgumentException,
              SecurityException, IllegalStateException {

        buttonStop.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        try {
          mediaPlayer.setDataSource(AudioSavePathInDevice);
          mediaPlayer.prepare();
        } catch (IOException e) {
          e.printStackTrace();
        }

        mediaPlayer.start();
        Toast.makeText(AttendanceActivity.this, "Recording Playing",
                Toast.LENGTH_LONG).show();
      }
    });

    buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        buttonStop.setEnabled(false);
        buttonStart.setEnabled(true);
        buttonStopPlayingRecording.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(true);

        if(mediaPlayer != null){
          mediaPlayer.stop();
          mediaPlayer.release();
          MediaRecorderReady();
        }
      }
    });

  }

  //voice recognizer
  @Override
  protected void onActivityResult(int requestCode, int resultCode,
                                  @Nullable Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
      if (resultCode == RESULT_OK && data != null) {
        ArrayList<String> result = data.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS);
        tv_Speech_to_text.setText(
                Objects.requireNonNull(result).get(0));
      }
    }
  }

  //

  public void MediaRecorderReady(){
    mediaRecorder=new MediaRecorder();
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
    mediaRecorder.setOutputFile(AudioSavePathInDevice);
  }

  public String CreateRandomAudioFileName(int string){
    StringBuilder stringBuilder = new StringBuilder( string );
    int i = 0 ;
    while(i < string ) {
      stringBuilder.append(RandomAudioFileName.
              charAt(random.nextInt(RandomAudioFileName.length())));

      i++ ;
    }
    return stringBuilder.toString();
  }

  private void requestPermission() {
    ActivityCompat.requestPermissions(AttendanceActivity.this, new
            String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE/*, STORAGE_SERVICE*/}, RequestPermissionCode);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case RequestPermissionCode:
        if (grantResults.length > 0) {
          boolean WritePermission = grantResults[0] ==
                  PackageManager.PERMISSION_GRANTED;
          boolean RecordPermission = grantResults[1] ==
                  PackageManager.PERMISSION_GRANTED;
          boolean ReadPermission = grantResults[2] ==
                  PackageManager.PERMISSION_GRANTED;
          //boolean StoragePermission = grantResults[3] ==
          //        PackageManager.PERMISSION_GRANTED;

          if (/*StoragePermission && */RecordPermission && ReadPermission && WritePermission) {
            Toast.makeText(AttendanceActivity.this, "Permission Granted",
                    Toast.LENGTH_LONG).show();
          } else {
            Toast.makeText(AttendanceActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
          }
        }
        break;
    }
  }

  public boolean checkPermission() {
    int result = ContextCompat.checkSelfPermission(getApplicationContext(),
            WRITE_EXTERNAL_STORAGE);
    int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
            RECORD_AUDIO);
    int result2 = ContextCompat.checkSelfPermission(getApplicationContext(),
            READ_EXTERNAL_STORAGE);
    //int result3 = ContextCompat.checkSelfPermission(getApplicationContext(),
    //        STORAGE_SERVICE);
    return result == PackageManager.PERMISSION_GRANTED &&
            result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
            //&& result3 == PackageManager.PERMISSION_GRANTED;
  }
}

  //}
//}
