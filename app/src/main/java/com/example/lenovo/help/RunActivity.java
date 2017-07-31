package com.example.lenovo.help;


import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static android.media.AudioManager.FLAG_PLAY_SOUND;
import static com.example.lenovo.help.RootCmd.execRootCmd;
import static com.example.lenovo.help.RootCmd.upgradeRootPermission;

public class RunActivity extends AppCompatActivity {

    private ImageButton btn_play;
    private AssetManager aManager;
    private SoundPool mSoundPool = null;
    int hit;
    private AudioTrack at;
    private AudioManager am;
    String result;
    String block;
    private TextView textView;
    boolean judgement = false;
    int flag = 0;
    private ImageButton btn_record;

    private static final String LOG_TAG = "AudioRecordTest";

    //语音保存路径
    private String FileName = null;

    //语音操作对象
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        btn_play = (ImageButton) findViewById(R.id.play);
        btn_record = (ImageButton) findViewById(R.id.record_btn);
        textView = (TextView) findViewById(R.id.textView);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_SYSTEM);

        btn_record = (ImageButton) findViewById(R.id.record_btn);
        //设置sdcard的路径
        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/audiorecordtest.3gp";

        btn_play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!judgement) {
                    String re = result;
                    re += execRootCmd("ls");
//                if (re.length() > 10) {
//                    block = re.substring(0, re.indexOf(" /framework"));
//                    block = block.substring(block.lastIndexOf("\n") + 1);
//                }
//                Log.i("", re);
                    textView.setText(re + "\r\n" + "..." + block + "...");
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), FLAG_PLAY_SOUND);
                    mSoundPool.play(hit, 1, 1, 0, -1, 1);
                    judgement = true;
                } else {
                    mSoundPool.release();
                    judgement = false;
                }
            }
        });

        mSoundPool = new SoundPool(5, AudioManager.STREAM_ALARM, 5);
        hit = mSoundPool.load(this, R.raw.notice, 1);

        if (!upgradeRootPermission(getPackageCodePath())) {
            result = "获取root权限失败";
            Toast.makeText(this, "获取root权限失败", Toast.LENGTH_SHORT).show();
        }

        //开始录音
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setOutputFile(FileName);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                    mRecorder.start();
                } else if (flag == 1) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
                flag = (flag + 1) % 2;
            }
        });

    }
}
