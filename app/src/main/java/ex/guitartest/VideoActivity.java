package ex.guitartest;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qyxlx on 2018/3/1.
 * 视频播放界面
 */

public class VideoActivity extends AppCompatActivity {
    @BindView(R.id.videView)
    VideoView mVideoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        MediaController mController = new MediaController(VideoActivity.this);
        mVideoView.setMediaController(mController);
        mController.setMediaPlayer(mVideoView);
        mVideoView.requestFocus();
        mVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.shipin));
        mVideoView.start();
    }

    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }
}
