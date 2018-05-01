package ex.guitartest.viewutils;

import android.util.Log;

import java.util.ArrayList;

/**
 *
 * Created by qyxlx on 2018/4/30.
 */
public class MusicNote {
	public static ArrayList<String> StoreMusicNote=new ArrayList<>();
	public int standardNum; // 每个音符的唯一标识
	public boolean isProlong = false;
	public int[] bigMusicNum = { 0, 2, 4, 5, 7, 9, 11 };// 全全半全全全半
														// 这里数字以半音为一个单位,数组里是相差
	static public String bigMusicStrings[] = new String[] { "1", "1", "2", "3", "3",
			"4", "4", "5", "6", "6", "7", "7" };
    static public String[] chordMusicStrings = new String[]{"C", "Cm7", "Dm", "A7", "Em", "F","G","F#", "Ab", "A", "Bb", "B"};//Em为Eb,G和F#调换了位置
	public String offsetStrings[] = new String[] { " ", "#", " ", "b", " ",
			" ", "#", " ", "b", " ", "b", " " };
	public String checkPlayStrings[]=new String[]{"↑","↓","→","←"," "};
	private final int tempSpeedFlag=3;
	private boolean chord;

	public MusicNote(int inputBigMusicIndex, int num, int offset, int pitch,boolean chord) {
		// inputBigMusicIndex为该大调在数组中的位置(0-11) num为简谱的数字(1-7)
		// offset为-1 0 1 代表升降调 pitch为-1 0 1 代表低音 中音 高音
		if (num == 0) {
			isProlong = true;
			standardNum = -100;
		} else {
			this.standardNum = inputBigMusicIndex + bigMusicNum[num - 1]
					+ offset + 12 * pitch;
			this.chord=chord;
			setStoreMusicNote(num,pitch,chord);
		}
		// 得出的标准数的范围为：-13 --- 35
	}// 2+4 0+6

	public MusicNote(int standardNum) {
		this.standardNum = standardNum;
		if (standardNum == -100) {
			isProlong = true;
		}
	}

	public String getPitchUp(int outputBigMusicIndex) {
		if (isProlong) {
			return " ";
		} else {
			return standardNum - outputBigMusicIndex < 0 ? " " : standardNum
					- outputBigMusicIndex < 12 ? " " : "●";
		}
	}

	public String getPitchDown(int outputBigMusicIndex) {
		if (isProlong) {
			return " ";
		} else {
			return standardNum - outputBigMusicIndex < 0 ? "●" : standardNum
					- outputBigMusicIndex < 12 ? " " : " ";
		}
	}

	public int getPitch(int outputBigMusicIndex) {
		if (isProlong) {
			return 0;
		} else {
			return standardNum - outputBigMusicIndex < 0 ? -1 : standardNum
					- outputBigMusicIndex < 12 ? 0 : 1;
		}
	}

	public String getMusicString(int outputBigMusicIndex) {
		if (isProlong) {
			return "-";
		} else {
			int num = standardNum - outputBigMusicIndex - 12
					* this.getPitch(outputBigMusicIndex);
			if (chord) {
				return chordMusicStrings[outputBigMusicIndex-1];
			} else {
				return bigMusicStrings[num];
			}
		}
	}

	public String getoffsetString(int outputBigMusicIndex) {
		if (isProlong) {
			return " ";
		} else {
			int num = standardNum - outputBigMusicIndex - 12
					* this.getPitch(outputBigMusicIndex);
			if (chord) {
				return " ";

			} else {
				return offsetStrings[num];
			}
		}
	}

	public String getCheckPlayStrings(int whichError) {
		if (whichError==10||isProlong)return checkPlayStrings[4];
		return checkPlayStrings[whichError];

	}

	public static boolean deleteObject()
	{
		if (StoreMusicNote.size()<3)
			return false;
		else {
			for (int i = 0; i > 3; i++)
				StoreMusicNote.remove(StoreMusicNote.size() - 1);
			return true;
		}
	}
	public static void cleanAll()
	{
		StoreMusicNote.clear();
	}
	public void setStoreMusicNote(int num,int pitch,boolean chord)
	{
		if (chord)
		{
			pitch=100;
			switch (num)
			{
				case 1:
					StoreMusicNote.add("43"+tempSpeedFlag);
					break;
				case 3:
					StoreMusicNote.add("48"+tempSpeedFlag);
					break;
				case 4:
					StoreMusicNote.add("51"+tempSpeedFlag);
					break;
				case 7:
					StoreMusicNote.add("60"+tempSpeedFlag);
					break;
				default:
					Log.d("MusicNoteChord", "Chord Error");
					break;
			}
		}
		switch (pitch)
		{
			case 1:
				StoreMusicNote.add("0"+String.valueOf(5-num)+tempSpeedFlag);
				break;
			case 0:
				if (num>2)
					StoreMusicNote.add("0"+String.valueOf(12-num)+tempSpeedFlag);
				else
					StoreMusicNote.add(String.valueOf(12-num)+tempSpeedFlag);
				break;
			case -1:
				StoreMusicNote.add(String.valueOf(19-num)+tempSpeedFlag);
				break;
			default:
				break;
		}
	}

	public int getStandardNum() {
		return standardNum;
	}
	public boolean getChord()
    {
        return chord;
    }
    static public String[] getChordMusicStrings()
    {
        return chordMusicStrings;
    }
}