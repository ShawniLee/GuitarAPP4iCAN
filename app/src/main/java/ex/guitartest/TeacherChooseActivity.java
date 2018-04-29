package ex.guitartest;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ex.guitartest.video.Code;
import ex.guitartest.video.FileOperator;

public class TeacherChooseActivity extends AppCompatActivity {

    @BindView(R.id.button2)
    Button button2;
    String filPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_choose);
        ButterKnife.bind(this);
        button2.setOnClickListener(new View.OnClickListener() {
            private Intent takeVideoIntent;

            @Override
            public void onClick(View V) {
                takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);// 创建一个请求视频的意图
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);// 设置视频的质量，值为0-1，
               // takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);// 设置视频的录制长度，s为单位
               // takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 20 * 1024 * 1024L);// 设置视频文件大小，字节为单位
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(takeVideoIntent, Code.VIDEO_RECORD_REQUEST);// 设置请求码，在onActivityResult()方法中接收结果}
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case Code.VIDEO_RECORD_REQUEST:
                if (null != data) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    } else {
                        Cursor c = getContentResolver().query(uri,
                                new String[]{MediaStore.MediaColumns.DATA},
                                null, null, null);
                        if (c != null && c.moveToFirst()) {
                            filPaths = c.getString(0);
                            showUploadVideoDialog();
                        }
                        c.close();
                    }
                }
                break;
            case Code.LOCAL_VIDEO_REQUEST:
                if (resultCode == Code.LOCAL_VIDEO_RESULT && data != null) {
                    filPaths = data.getStringExtra("path");
                    showUploadVideoDialog();
                }
                break;
            default:
                break;
        }
    }

    private void showUploadVideoDialog() {
        File file=new File(filPaths);
        boolean a=file.exists();
        Toast.makeText(getBaseContext(),Boolean.toString(a),Toast.LENGTH_SHORT).show();
        String descFile= "/sdcard/guitar";
        FileOperator.moveFile(filPaths,descFile);
        File file2=new File("/storage/5E57-0EEF/DCIM/Camera/20180301_233231.mp4");
        a=file2.delete();
        Toast.makeText(getBaseContext(),Boolean.toString(a),Toast.LENGTH_SHORT).show();

    }

}