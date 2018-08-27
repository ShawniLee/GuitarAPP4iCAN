package ex.guitartest.viewutils;

import java.util.ArrayList;

public class AudioNote {
    public static ArrayList<AudioNote> AudioGetMusicNoteStored=new ArrayList<>();//存储麦克风接收到的声音,多线程不安全
    public int standardNum;
    public long currentTimeInterval;//和第一个音的时间间隔
    private Callback mCallback;

    public interface Callback{
        public abstract void changeUI();
    }

    public void setmCallback(Callback callback){
        this.mCallback=callback;
    }

    public void doWhat(){
        mCallback.changeUI();
    }

   public AudioNote(int standardNum, long TimeDiffence){
        this.standardNum=standardNum;
        if (AudioGetMusicNoteStored.size()==0){
            this.currentTimeInterval=TimeDiffence;
        }
        else {
            this.currentTimeInterval=TimeDiffence;
        }
    }
    public static int getLastAudioListStandardNum(){
        return AudioGetMusicNoteStored.get(AudioGetMusicNoteStored.size() - 1).standardNum;
    }
    public static long getAudioStandardTime(int index){
        return AudioGetMusicNoteStored.get(index).currentTimeInterval;
    }
    public static int getAudioStandardNum(int index){
        return AudioGetMusicNoteStored.get(index).standardNum;
    }
    public static long calcLastCurrentTimeDiff(){
       return AudioGetMusicNoteStored.size()==0? System.currentTimeMillis():System.currentTimeMillis()-AudioNote.getAudioStandardTime(0);
    }
}
