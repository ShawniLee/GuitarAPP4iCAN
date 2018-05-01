package ex.guitartest.audio;

/**
 * Created by qyxlx on 2018/5/2.
 */
import android.media.MediaRecorder;
import android.util.Log;

import ex.guitartest.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
public class RecordUtil {

    private static final String TAG = "RecorderUtil";

    private String mFileName = null;
    private MediaRecorder mRecorder = null;
    private long startTime;
    private long timeInterval;
    private boolean isRecording;

    public void  RecorderUtil() {
        mFileName = FileUtils.getAppDir().toString() + "tempRecord";
        //TODO:文件名定义？
    }

    /**
     * 开始录音
     */
    public void startRecording() {
        if (mFileName == null) return;
        if (isRecording){
            mRecorder.release();
            mRecorder = null;
        }
        //由于权限原因，需要加入异常处理
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            startTime = System.currentTimeMillis();
            try {
                mRecorder.prepare();
                mRecorder.start();
                isRecording = true;
            } catch (Exception e){
                Log.e(TAG, "prepare() failed");
            }
        }catch(RuntimeException e){
            //出现异常，starttime置0
            startTime = 0;
            //做出处理，让语音不发送
            Log.e(TAG, "由于权限原因音频录制初始化失败 failed");
        }finally {

        }

    }


    /**
     * 停止录音
     */
    public void stopRecording() {
        if (mFileName == null) return;
        long stopTime = System.currentTimeMillis();
        if(startTime == 0){
            //如果开始录制的时间为0 ，说明录制出现问题，最常见的是由于权限引起的问题
            startTime = stopTime;
        }
        timeInterval = stopTime - startTime;
        try{
            if (timeInterval>1000){
                mRecorder.stop();
            }
            mRecorder.release();
            mRecorder = null;
            isRecording =false;
        }catch (Exception e){
            Log.e(TAG, "release() failed");
        }

    }

    /**
     * 取消语音
     */
    public synchronized void cancelRecording() {

        if (mRecorder != null) {
            try {
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(mFileName);
            file.deleteOnExit();
        }

        isRecording =false;
    }

    /**
     * 获取录音文件
     */
    public byte[] getDate() {
        if (mFileName == null) return null;
        try{
            return readFile(new File(mFileName));
        }catch (IOException e){
            Log.e(TAG, "read file error" + e);
            return null;
        }
    }

    /**
     * 获取录音文件地址
     */
    public String getFilePath(){
        return mFileName;
    }


    /**
     * 获取录音时长,单位秒
     */
    public long getTimeInterval() {
        return timeInterval/1000;
    }


    /**
     * 将文件转化为byte[]
     *
     * @param file 输入文件
     */
    private static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }



}