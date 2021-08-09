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
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class AttendanceActivity extends AppCompatActivity {

  Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
          buttonStopPlayingRecording ;
  String AudioSavePathInDevice = null;
  MediaRecorder mediaRecorder ;
  Random random ;
  String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
  public static final int RequestPermissionCode = 1;
  MediaPlayer mediaPlayer ;
  //public final int[] RANGE = new int[] { 40, 80, 120, 180, 300, 500, 700, 900};
  public final int[] RANGE = new int[] { 40, 60, 80, 100, 500, 900, 910, 950};
  //public final int[] RANGE = new int[] { 40, 60, 80, 90, 100, 900, 910, 950};
  private static final int FUZ_FACTOR = 2;

  //voice Recognizer
    /*
  private ImageView iv_mic;
  private TextView tv_Speech_to_text;
  private static final int REQUEST_CODE_SPEECH_INPUT = 1;

     */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_attendance);

    // voice recognizer
      /*
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

       */
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

          //AudioSavePathInDevice =
          //        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
          //                CreateRandomAudioFileName(5) + "AudioRecording.3gp";

          //AudioSavePathInDevice =
          //        view.getContext().getExternalFilesDir(null).getAbsolutePath() + "/" +
          //                CreateRandomAudioFileName(5) + "AudioRecording.3gp";

          AudioSavePathInDevice =
                  view.getContext().getExternalFilesDir(null).getAbsolutePath() + "/" + "AudioRecording.3gp";


          // checking if a file can be created
          /*
          try {

            File root = new File(view.getContext().getExternalFilesDir(null), "Notes");
            if (!root.exists()) {
              Log.i("file creation","I am trying to create directory");
              root.mkdirs();
            }

            if (!root.exists()) {
              Log.i("file creation","Could not create a directory!!");
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

           */



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

          //play pre-recorded audio here
          /*
          mediaPlayer = new MediaPlayer();
          try {
            mediaPlayer.setDataSource(view.getContext().getExternalFilesDir(null).getAbsolutePath() + "/" + "AudioRecording_music1.3gp");
            mediaPlayer.prepare();
          } catch (IOException e) {
            e.printStackTrace();
          }

          mediaPlayer.start();


           */



          //
          final Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              if (mediaRecorder != null) {
                try {
                  afterRecording();
                } catch(RuntimeException ex){
                  //Ignore
                }
              }

              if (mediaPlayer != null) {

                  mediaPlayer.stop();
                  mediaPlayer.release();

              }

            }
          }, 9000);


          //

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

        // printing binary
        try {
          byte[] encoded = Files.readAllBytes(Paths.get(AudioSavePathInDevice));
          Log.i("audio string",Arrays.toString(encoded));
        } catch (IOException e) {

        }

        // try in decimal
        StringBuilder sb = new StringBuilder();
        int maxNum = 0;
        try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(AudioSavePathInDevice))) {
          for (int i; (i = is.read()) != -1;) {
            //String temp = Integer.toHexString(i).toUpperCase();
            String temp = Integer.toString(i).toUpperCase();
            //if (temp.length() == 1) {
            //  sb.append('0');
            //}
            sb.append(temp).append(' ');
            if (Integer.valueOf(temp) > maxNum) maxNum = Integer.valueOf(temp);
          }
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        Log.i("hex output",sb.toString());
        Log.i("maxVal",String.valueOf(maxNum));
        Log.i("size",String.valueOf(sb.length()));
        Log.i("file location", AudioSavePathInDevice);
        //File myObj = new File("/storage/emulated/0/Android/data/edu.neu.numad21su.attention/files/OMIFGAudioRecording.3gp");

        //myObj.delete();

        // Modify the values in string buffer to normalize and change polarities if needed
        StringBuffer sbNorm = new StringBuffer();
        String[] sbArray = sb.toString().split(" ");
        for (int idx =0; idx < sbArray.length; idx++) {
          double tempDouble = ((double)((double) Integer.valueOf(sbArray[idx]) /maxNum)) - 0.5;
          sbNorm.append(String.valueOf(tempDouble));
          sbNorm.append(" ");

        }


        try {

          File root = new File(view.getContext().getExternalFilesDir(null), "Notes");
          if (!root.exists()) {
            Log.i("file creation","I am trying to create directory");
            root.mkdirs();
          }

          if (!root.exists()) {
            Log.i("file creation","Could not create a directory!!");
          }

          File gpxfile = new File(root, "inputfile");
          //File gpxfile = new File(root, "hello");
          gpxfile.delete();
          FileWriter writer = new FileWriter(gpxfile);
          writer.append(sbNorm.toString());
          writer.flush();
          writer.close();
          //Reading
          Scanner myReader = new Scanner(gpxfile);
          //while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            Log.i("Reading file data",data);
          //}
          myReader.close();
          // create test for hello
          gpxfile = new File(root, "hello");
          myReader = new Scanner(gpxfile);
          //while (myReader.hasNextLine()) {
            String hello = myReader.nextLine();
            //Log.i("Reading file data",hello);
          // Print saved hello data

          gpxfile = new File(root, "hello");
          myReader = new Scanner(gpxfile);
          hello = myReader.nextLine();
          int maxLogSize = 900;
          for(int i = 0; i <= hello.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > hello.length() ? hello.length() : end;


            //Log.d("good saved long data", hello.substring(start, end));
          }

          // apply filter on hello data
          filterOut(hello);

          //}
          myReader.close();
          double helloTest = computeFFT(hello,data);
          Log.i("Hello Test result", String.valueOf(helloTest));

         /*
          // create test for good
          gpxfile = new File(root, "good");
          myReader = new Scanner(gpxfile);
          //while (myReader.hasNextLine()) {
          String good = myReader.nextLine();
          Log.i("Reading file data",good);
          //}
          myReader.close();
          double goodTest = computeFFT(good,data);
          Log.i("Good Test result", String.valueOf(goodTest));


          // create test for class
          gpxfile = new File(root, "class");
          myReader = new Scanner(gpxfile);
          //while (myReader.hasNextLine()) {
          String class_ = myReader.nextLine();
          Log.i("Reading file data",class_);
          //}
          myReader.close();
          double classTest = computeFFT(class_,data);
          Log.i("Class Test result", String.valueOf(classTest));

        */
          Toast.makeText(AttendanceActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
          e.printStackTrace();
        }


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
    /*
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

     */

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
            String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, RequestPermissionCode);
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

    boolean result = (ContextCompat.checkSelfPermission(this,
            WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

    boolean result1 = (ContextCompat.checkSelfPermission(this,
            RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);

    boolean result2 = (ContextCompat.checkSelfPermission(this,
            READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
      Log.i("result", String.valueOf(result));
      Log.i("result1", String.valueOf(result1));
      Log.i("result2", String.valueOf(result2));
      return result && result1 && result2;


  }

  private List<String> extractCenter(String [] inputArray) {
    List<String> outputList = new ArrayList<String>();
    int idxSt = 0;
    int idxEn = inputArray.length;

    if (inputArray.length > 400) {
      idxSt = 0;
      idxEn = inputArray.length;
    }



    for (int i = idxSt; i < idxEn; i++) {
      outputList.add(inputArray[i]);
    }
    return  outputList;
  }

  public String computePreFFT(String inputSignal) {

    StringBuilder outputSignal = new StringBuilder();
    String [] inputArray = inputSignal.split(" ");
    //double output = 0.0;

    for (int fo = 100; fo <= 3000; fo = fo + 100) {
      for (int idx = 0; idx < inputArray.length; idx++) {
        inputArray[idx]  = String.valueOf(Double.valueOf(inputArray[idx])
                * Math.sin(2 * Math.PI * fo * idx * 0.000022675));

      }
    }
    //X[n] = Math.sin(2*Math.PI*fo*n*ts)

    for (int idx = 0; idx < inputArray.length; idx++) {
      outputSignal.append(inputArray[idx]);
      outputSignal.append(" ");
    }



    return outputSignal.toString();
  }

  private double getSum(String inputStr) {
    double allSum = 0;
    String [] inputArr = inputStr.split(" ");
    for (int i = 0; i < inputArr.length; i ++) {
      allSum += Double.valueOf(inputArr[i]);
    }
    return allSum;
  }

  public double computeFFT(String model, String test) {
    List<String> modelParsed = new ArrayList<String>();
    List<String> testParsed = new ArrayList<String>();

    String [] modelParsedArray = model.split(" ");
    String [] testParsedArray = test.split(" ");

    modelParsed = extractCenter(modelParsedArray);
    testParsed = extractCenter(testParsedArray);


    //Log.i("debugParsed",modelParsed[10]);
    //Log.i("debugParsed",modelParsed[10]);
    Log.d("length of old model file",String.valueOf(modelParsed.size()));
    Log.d("length of file",String.valueOf(testParsed.size()));

    int idx = 0;
    double sumOfInnerProduct = 0.0;
    double finalSumOfInnerProduct = -999999999999.0;
    while (idx < modelParsed.size()) {
      int jdx = 0;
      int jdxModel = idx;
      while (jdx < (testParsed.size()/2) && jdxModel < modelParsed.size()) {
        sumOfInnerProduct += (Double.valueOf(testParsed.get(jdx)) * Double.valueOf(modelParsed.get(jdxModel)));
        jdx++;
        jdxModel++;
      }
      if (sumOfInnerProduct > finalSumOfInnerProduct) {
        finalSumOfInnerProduct = sumOfInnerProduct;
      }
      sumOfInnerProduct = 0.0;
      idx++;
      break;
    }
    return finalSumOfInnerProduct;
  }

  public String filterOut(String inputArray) {
    Log.d("started debug", "DEBUG");
    String [] inputArrayString = inputArray.split(" ");
    double [] doubleArray = new double[inputArrayString.length];

    for (int idx = 0; idx  < inputArrayString.length; idx++) {
      doubleArray[idx] = Double.valueOf(inputArrayString[idx]);
    }

    //HighPassFilter filter = new HighPassFilter(100,44100, HighPassFilter.PassType.Highpass,1);
    HighPassFilter filter = new HighPassFilter(30,44100, HighPassFilter.PassType.Highpass,1);
    StringBuilder outputSb = new StringBuilder();
    for (int i = 0; i < doubleArray.length; i++)
    {
      filter.Update(doubleArray[i]);
      doubleArray[i] = filter.getValue();
      outputSb.append(filter.getValue());
      outputSb.append(" ");
    }
    // Lets print

    String letsPrint = outputSb.toString();

    int maxLogSize = 900;
    for(int i = 0; i <= letsPrint.length() / maxLogSize; i++) {
      int start = i * maxLogSize;
      int end = (i + 1) * maxLogSize;
      end = end > letsPrint.length() ? letsPrint.length() : end;

      //Log.d("hello saved long data", letsPrint.substring(start, end));
    }

    return letsPrint;
  }

  public int getIndex(int freq) {
    int i = 0;
    while (i < 7 && RANGE[i] < freq)
      i++;
    return i;
  }

  private long hash(long p1, long p2, long p3, long p4) {
    return (p4 - (p4 % FUZ_FACTOR)) * 100000000 + (p3 - (p3 % FUZ_FACTOR))
            * 100000 + (p2 - (p2 % FUZ_FACTOR)) * 100
            + (p1 - (p1 % FUZ_FACTOR));
  }


  public List<Complex []> fullFFT(String audioStr) {
    String [] audioArr = audioStr.split(" ");
    Log.d("size of audioArr", String.valueOf(audioArr.length));
    List<Double> audio = new ArrayList<>();

    for (int idx = 0; idx < audioArr.length; idx++) {
      audio.add(Double.valueOf(audioArr[idx]));
    }

    int totalSize = audio.size();
    Log.d("totalsize", String.valueOf(totalSize));
    int chunkSize = 1024;
    int sampledChunkSize = totalSize/chunkSize;
    //Log.d("sampledchunksize", String.valueOf(sampledChunkSize));
    //Complex result[sampledChunkSize][chunkSize];
    //Complex result[][];
    List<Complex []> result = new ArrayList<>();

    for(int j = 0; j < sampledChunkSize; j++) {
      List<Complex> chunkList = new ArrayList<>();
      //Log.d("fft result2", "inside first loop"+ String.valueOf(j));
      for(int i = 0; i < chunkSize; i++) {
        //complexArray[i] = Complex(audio[(j*chunkSize)+i], 0);
        chunkList.add(new Complex(audio.get((j * chunkSize) + i),0));
        //Log.d("fft result3", "inside second loop"+ String.valueOf(i));
      }
      Complex [] complexArray = new Complex[chunkList.size()];
      chunkList.toArray(complexArray);
      //Log.d("fft result4", "outside second loop");
      //result[j] = FFT.fft(complexArray);
      result.add(FFT.fft(complexArray));
      //Log.d("fft result5", "outside second loopppp");
      //Log.d("fft result", String.valueOf(result.get(j).length));
      //Log.d("fft result", String.valueOf(result.get(j)[100].abs()));
  }

  //Log.d("fft result", String.valueOf(result.get(0).length));
  Log.d("fft result2", "returning");

    return result;

}

  private int nearestPowerOfTwo (int number) {
    int i = 2;
    int prevI = i;
    while (i <= number) {
      prevI = i;
      i = i * i;
    }
    return prevI;
  }

  public List<String> postFFTcalculations(List<Complex []> result) {
    //Log.d("result size", String.valueOf(result.size()));
    //Log.d("result 1st row size", String.valueOf(result.get(0).length));
    Double [][] highscores = new Double[result.size()][8];
    int [][] points = new int[result.size()][8];
    List<Long> hashedValues = new ArrayList<>();
    List<String> retunrStr = new ArrayList<>();

    //Initialize highscores
    //Log.d("postfft", "line 724");
    for (int idx = 0; idx < highscores.length; idx++) {
      for (int jdx = 0; jdx < 8; jdx++) {
        highscores[idx][jdx] = 0.0;
      }
    }
    //Initializing points
    for (int idx = 0; idx < points.length; idx++) {
      for (int jdx = 0; jdx < 8; jdx++) {
        points[idx][jdx] = 0;
      }
    }

    //Log.d("postfft", "line 730");
    //try {
      for (int t = 0; t < result.size(); t++) {
        for (int freq = 40; freq < 1024; freq++) {
          // Get the magnitude:
          //Log.d("postfft", "line 734");
          //double mag = Math.log(result.get(t)[freq].abs() + 1);
          double mag = result.get(t)[freq].abs();
          //Log.d("postfft", "line 736");
          // Find out which range we are in:
          int index = getIndex(freq);
          //int index = 0;
          /*
          Log.d("t value", String.valueOf(t));
          Log.d("freq value", String.valueOf(freq));
          Log.d("index value", String.valueOf(index));
          Log.d("postfftMag", String.valueOf(mag));

           */
          // Save the highest magnitude and corresponding frequency:
          if (mag > highscores[t][index]) {
            //Log.d("postfft", "inside comparison");
            //Log.d("postfftMag", String.valueOf(mag));
            points[t][index] = freq;
            highscores[t][index] = mag;


          }
          //Log.d("postfft", "line 744");
        }
        //Log.d("postfft", "line 745");
        // form hash tag
        //hashedValues.add(hash(points[t][0], points[t][1], points[t][2], points[t][3]));
        retunrStr.add(String.valueOf(points[t][0]) + " " + String.valueOf(points[t][1])
                + " " + String.valueOf(points[t][2]) + " " +String.valueOf(points[t][3])
                +" " +String.valueOf(points[t][4]) +" " +String.valueOf(points[t][5])
                +" " +String.valueOf(points[t][6]) +" " +String.valueOf(points[t][7]));
        //Log.d("postfft", "line 748");
      }
    //}
    /*catch (ArrayIndexOutOfBoundsException ex) {
      Log.d("Exception", "Exception occurred");
    }*/
    //Log.d("postfft", "line 749");
    //return hashedValues;
    return retunrStr;
  }

  public void afterRecording() {
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

    // printing binary
    try {
      byte[] encoded = Files.readAllBytes(Paths.get(AudioSavePathInDevice));
      //Log.i("audio string",Arrays.toString(encoded));
    } catch (IOException e) {

    }

    // try in decimal
    StringBuilder sb = new StringBuilder();
    int maxNum = 0;
    try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(AudioSavePathInDevice))) {
      for (int i; (i = is.read()) != -1;) {
        //String temp = Integer.toHexString(i).toUpperCase();
        String temp = Integer.toString(i).toUpperCase();

        sb.append(temp).append(' ');
        if (Integer.valueOf(temp) > maxNum) maxNum = Integer.valueOf(temp);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //Log.i("hex output",sb.toString());
    //Log.i("maxVal",String.valueOf(maxNum));
    Log.i("size",String.valueOf(sb.length()));
    //Log.i("file location", AudioSavePathInDevice);

    // Modify the values in string buffer to normalize and change polarities if needed
    StringBuffer sbNorm = new StringBuffer();
    String[] sbArray = sb.toString().split(" ");
    // making sure that size is even
    int arraySize = sbArray.length;
    //arraySize = nearestPowerOfTwo(arraySize);

    for (int idx =0; idx < arraySize; idx++) {
      double tempDouble = ((double)((double) Integer.valueOf(sbArray[idx]) /maxNum)) - 0.5;
      sbNorm.append(String.valueOf(tempDouble));
      sbNorm.append(" ");

    }


    try {

      View view = findViewById(android.R.id.content);
      File root = new File(view.getContext().getExternalFilesDir(null), "Notes");
      if (!root.exists()) {
        Log.i("file creation","I am trying to create directory");
        root.mkdirs();
      }

      if (!root.exists()) {
        Log.i("file creation","Could not create a directory!!");
      }

      File gpxfile = new File(root, "music1");
      //File gpxfile = new File(root, "hello");
      gpxfile.delete();
      FileWriter writer = new FileWriter(gpxfile);
      writer.append(sbNorm.toString());
      writer.flush();
      writer.close();
      //Reading
      Scanner myReader = new Scanner(gpxfile);

      String data = myReader.nextLine();
      Log.i("Reading file data",data);
      //myReader.close();
      // create test for hello


      List<String> hashedValues = new ArrayList<>();
      List<Complex []> returnedList = fullFFT(data);
      hashedValues = postFFTcalculations(returnedList);

      gpxfile = new File(root, "music1");
      myReader = new Scanner(gpxfile);
      String a = myReader.nextLine();

      // checking if FFt worked;
      //Log.d("WHOLE FILE", a);

      /*
      List<Long> hashedValues = new ArrayList<>();
      List<Complex []> returnedList = fullFFT(a);
      hashedValues = postFFTcalculations(returnedList);

       */


      /*
      StringBuilder tb = new StringBuilder();
      for(int idx = 0; idx < returnedList.get(1).length; idx++) {
        tb.append(returnedList.get(1)[idx].abs());
        tb.append(" ");
      }

       */
      //Log.d("fullFFT result for row 0", tb.toString());
      Log.d("RETURNED", "I RETURED");
      // checking hashed values
      for (int idx = 0; idx < hashedValues.size(); idx++) {
        Log.d("hashValue", hashedValues.get(idx));
      }

      //

      //
      int maxLogSize = 900;
      for(int i = 0; i <= a.length() / maxLogSize; i++) {
        int start = i * maxLogSize;
        int end = (i + 1) * maxLogSize;
        end = end > a.length() ? a.length() : end;

        //Log.d("good saved long data", hello.substring(start, end));
      }

      // apply filter on hello data
      //filterOut(a);

      myReader.close();

      gpxfile = new File(root, "music1");
      myReader = new Scanner(gpxfile);
      String b = myReader.nextLine();

      /*
      gpxfile = new File(root, "music6");
      myReader = new Scanner(gpxfile);
      String c = myReader.nextLine();

       */

      /*
      double aTest = computeFFT(filterOut(a),filterOut(data));

      Log.d("uuuuuuuuuuuu", "!!!!!!!!!!!!!!!!!!!");
      Log.d("a Test result", String.valueOf(aTest));

      double bTest = computeFFT(filterOut(b),filterOut(data));

      Log.d("b Test result", String.valueOf(bTest));

      double cTest = computeFFT(filterOut(c),filterOut(data));

      Log.d("c Test result", String.valueOf(cTest));

       */

      Toast.makeText(AttendanceActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}

