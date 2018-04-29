package ex.guitartest.util;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 *
 * Created by qyxlx on 2018/3/12.
 */


public class MusicNote {
    private byte pitch;
    private byte speed;
    private byte chord;
    final static byte[] listP=new byte[]{0,1,2,3,4,5};
    final static byte[] listS=new byte[]{1,2,3,4,5};
    final static byte[] listC=new byte[]{2,3,4,5,6};

    public void ListToNote(int p,int s,int c)
    {
        SetMusicNote(listP[p],listS[s],listC[c]);
    }

    private void SetMusicNote(byte pitch,byte speed,byte chord)
    {
        this.pitch=pitch;
        this.speed=speed;
        this.chord=chord;
    }

    public byte getPitch() {
        return pitch;
    }

    public byte getChord() {
        return chord;
    }

    public byte getSpeed() {
        return speed;
    }
    public String GetModuleInit(int a) {

        switch (a) {
            case 1:
                return "@TS";
            case 2:
                return "@T002";
            case 3:
                return "@T";
                default:
                    Log.d(TAG, "GetModuleInit: Error");
                    return "Error";
        }
    }
    public String GetMusic(MusicFile a)
    {
        switch (a)
        {
            case LittleStarMelody:
                return "@T10420113113073073063063075083083093093103103115073073083083093093105073073083083093093105113113073073063063075083083093093103103115#";
            case LittleStarChord:
                return "@T10280345345345345345345485345485345525345345485345525345485345525345345485345485345525345004#";
            case ChildHood:
                return "@T10160606516436486606516436486606516376486606436486606004#";
                default:
                    return "Error";
        }

    }

}
