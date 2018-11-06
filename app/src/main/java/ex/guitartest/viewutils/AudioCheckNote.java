package ex.guitartest.viewutils;


import android.widget.RelativeLayout;



public  class AudioCheckNote {
    public static final int truth=0;
    public static final int high=1;
    public static final int low=2;
    public static final int fast=3;
    public static final int slow=4;
    private static String checkPlayStrings[]=new String[]{" ","↑","↓","→","←"};
    int checkPitch;

    public static String getCheckPlayStrings(int whichError) {
        if (whichError==10)return checkPlayStrings[truth];
        return checkPlayStrings[whichError];
    }
}
