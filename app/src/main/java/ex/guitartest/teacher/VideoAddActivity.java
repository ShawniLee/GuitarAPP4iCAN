package ex.guitartest.teacher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.R;
import ex.guitartest.util.FileUtils;

/**
 * Created by lenovo on 2018/4/12.
 */

public class VideoAddActivity extends AppCompatActivity {
    private Uri fileUri = null;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_add);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    private File createMediaFile() throws IOException {

            File mediaStorageDir = FileUtils.getDir("video");
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "VID_" + timeStamp;
            String suffix = ".mp4";
            File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            return mediaFile;

    }

    @OnClick(R.id.iv_record)
    public void record(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {
           fileUri = Uri.fromFile(createMediaFile()); // create a file to save the video
        } catch (IOException e) {
            e.printStackTrace();
        }
       // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                //Display the video
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }


}
