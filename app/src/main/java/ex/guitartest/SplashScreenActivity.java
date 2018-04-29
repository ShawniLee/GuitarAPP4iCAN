package ex.guitartest;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import ex.guitartest.video.CustomVideoView;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener{

    private CustomVideoView videoview;
    private Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        btn_start =  findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        videoview = findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.splash));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }, 10);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//获得触摸的坐标
        float x = event.getX();
        float y = event.getY(); switch (event.getAction())
        {
//触摸屏幕时刻
            case MotionEvent.ACTION_DOWN:
                Log.d("x",Float.toString(x));
                Log.d("y",Float.toString(y));
                if (x>690&&y<370){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;}
//触摸并移动时刻
            case MotionEvent.ACTION_MOVE:
                break;
//终止触摸时刻
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

}