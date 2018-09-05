package ex.guitartest.viewutils;

import android.util.Log;

import java.util.ArrayList;

/**
 *
 * Created by qyxlx on 2018/4/30.
 */
public class MusicNote {
	public static ArrayList<String> MusicNoteStored =new ArrayList<>();//存储乐谱
	public static ArrayList<Long> StandardTimeSpeed=new ArrayList<>();
	public static final int OneBeatTime=250;
	private int standardNum; // 每个音符的唯一标识  标准数的范围为：-12 --- -1 低音 0 延长 1 --- 12 中音 13 --- 24 高音
	private boolean isProlong = false;
	private static  int[] bigMusicNum = { 0, 2, 4, 5, 7, 9, 11 };// 全全半全全全半
//	final static public int[] NoteHzRange = {
//			131,139,147,156,165,175,185,196,208,220,233,247,//-12 --- -1
//			262,277,294,311,330,349,370,392,415,440,466,494,//0 --- 11
//			523,587,622,660,699,740,784,831,880,932,988,1047//12 --- 23
//	};
	//TODO：针对吉他的特殊优化，未编写变调部分
	final static public int[] NoteHzRange = {
			65 ,69 ,73 ,78 ,82 ,87 ,93 ,98 ,104,110,117,124,//-12 --- -1
			131,139,147,156,165,175,185,196,208,220,233,247,//0 --- 11
			262,277,294,311,330,349,370,392,415,440,466,494,//12 --- 23
	};
														// 这里数字以半音为一个单位,数组里是相差
	private static String bigMusicStrings[] = new String[] { "1", "1", "2", "3", "3",
			"4", "4", "5", "6", "6", "7", "7" };
    private static String[] chordMusicStrings = new String[]{"C", "Cm7", "Dm", "Em", "Em", "F","G","F#", "Ab", "A", "Bb", "B"};//Em为Eb,G和F#调换了位置
	private String offsetStrings[] = new String[] { " ", "#", " ", "b", " ",
			" ", "#", " ", "b", " ", "b", " " };


	public MusicNote( int num, int offset, int pitch,boolean chord,int speed) {
		// inputBigMusicIndex为该大调在数组中的位置(0-11) num为简谱的数字(1-7)
		// offset为-1 0 1 代表升降调 pitch为-1 0 1 代表低音 中音 高音
		if (num == 0) {
			isProlong = true;
			standardNum = -100;
		} else {
			this.standardNum = normalNum2StandardNum(num,offset,pitch,chord);
			setStoreMusicNote(num,pitch,speed);
		}
	}// 2+4 0+6

	public MusicNote(int standardNum) {
		this.standardNum = standardNum;
		if (standardNum == -100) {
			isProlong = true;
		}
	}

	public static String getMusicNoteStored2String() {
		String tmpResult =MusicNoteStored.toString().substring(1,MusicNoteStored.toString().length()-1);
		StringBuffer result= new StringBuffer();//多线程不安全
		for (int i=0;i<tmpResult.length();i++)
		{
			if (tmpResult.charAt(i)!=','&&tmpResult.charAt(i)!=' ')
				result.append(tmpResult.charAt(i));
		}

		return result.toString();
	}

	public String getPitchUp() {
		if (isProlong) {
			return " ";
		} else {
			return standardNum < 0 ? " " : standardNum < 12 ? " " : "●";
		}
	}

	public String getPitchDown() {
		if (isProlong) {
			return " ";
		} else {
			return standardNum < 0 ? "●" : standardNum < 12 ? " " : " ";
		}
	}

	public int getPitch() {
		if (isProlong) {
			return 0;
		} else {
			return standardNum< 0 ? -1 : standardNum < 12 ? 0 : 1;
		}
	}

	public String getMusicString() {
		if (isProlong) {
			return "-";
		} else {
			int num = standardNum- 12 * this.getPitch();
			if (getChord()) {
				return chordMusicStrings[standardNum-49];//和弦从49号开始
			} else {
				return bigMusicStrings[num];
			}
		}
	}

	public String getoffsetString() {
		if (isProlong) {
			return " ";
		} else {
			int num = standardNum - 12
					* this.getPitch();
			if (getChord()) {
				return " ";

			} else {
				return offsetStrings[num];
			}
		}
	}


	public static boolean deleteObject()
	{
		if (MusicNoteStored.size()==0)
			return false;
		else {
				MusicNoteStored.remove(MusicNoteStored.size() - 1);
			return true;
		}
	}
	public static void cleanAll()
	{
		MusicNoteStored.clear();
	}
	public void setStoreMusicNote(int num,int pitch,int speed)
	{
        if (getChord())
		{
			pitch=100;
			switch (num)
			{
				case 1:
					MusicNoteStored.add("43"+ speed);
					break;
				case 3:
					MusicNoteStored.add("48"+ speed);
					break;
				case 4:
					MusicNoteStored.add("50"+ speed);
					break;
				case 7:
					MusicNoteStored.add("60"+ speed);
					break;
				default:
					Log.d("MusicNoteChord", "Chord Error");
					break;
			}
		}
		switch (pitch)
		{
			case 1:
				MusicNoteStored.add("0"+String.valueOf(5-num)+ speed);
				break;
			case 0:
				if (num>2)
					MusicNoteStored.add("0"+String.valueOf(12-num)+ speed);
				else
					MusicNoteStored.add(String.valueOf(12-num)+ speed);
				break;
			case -1:
				MusicNoteStored.add(String.valueOf(19-num)+ speed);
				break;
			default:
				break;
		}
		if (StandardTimeSpeed.size()>0){
			StandardTimeSpeed.add(StandardTimeSpeed.get(StandardTimeSpeed.size()-1)+OneBeatTime*(long)Math.pow(2,speed));
			//每个音的时间记录都是相对于第一个音来说的
		}
        else{
        	StandardTimeSpeed.add((long)0);
			StandardTimeSpeed.add((long)OneBeatTime*(long)Math.pow(2,speed));//第一个元素其实是第二个音的开头时间
		}
	}
	public static int deCodeStoreMusicPitch2Standard(int index){
		String temp=MusicNoteStored.get(index);
		int NormalNum=Integer.parseInt(temp.substring(0,2));
		int pitch=(18-NormalNum)/7 -1;
		int num=(18-NormalNum)%7+1;
		return normalNum2StandardNum(num,0,pitch,false);
	}
    public int getStandardNum() {
        return standardNum;
    }
	public boolean getChord()
    {
		return standardNum > 48;
	}
    static public String[] getChordMusicStrings()
    {
        return chordMusicStrings;
    }
    static public int getStandardNoteHz(){
		return NoteHzRange.length;
	}
	static public int getStandardNoteHz(int index){
		return NoteHzRange[index];
	}
	private static int normalNum2StandardNum(int num, int offset, int pitch,boolean chord){
		return chord ?  48 + num : bigMusicNum[num - 1] + offset + 12 * pitch;
		//和弦是48以后，之前是标准数
	}
	public static long getMusicStandardTime(int index){
		if (index<StandardTimeSpeed.size()){
			return StandardTimeSpeed.get(index);
		}else{
			return StandardTimeSpeed.get(StandardTimeSpeed.size()-1);
		}

	}
	public static int getMusicStanardNum(int index){
		return deCodeStoreMusicPitch2Standard(index);
	}
}