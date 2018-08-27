package ex.guitartest.viewutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ex.guitartest.R;

/**
 *
 * Created by qyxlx on 2018/4/15.
 */

public class MusicNoteLayout {
    private int indexCursorLine = 0;
    private int indexCursorRow = 0;
    private int row;
    private int line;
    private float size;
    private Activity activity;
    private int windowHeight;
    private int windowWidth;
    private float x;
    private float y;
    private boolean isCursorExisted = false;
    private int inputBigMusicIndex = 0;
    private float defaultHight;
    private ArrayList<Integer> standardNums = new ArrayList<Integer>();

    public MusicNoteLayout(float size, Activity activity) {

        this.size = dip2px(activity, size / 50)*(float)2;// 得到的size是相对于50px的倍数
        this.activity = activity;

        Display display = this.activity.getWindowManager().getDefaultDisplay();
        Point phoneSize = new Point();
        display.getSize(phoneSize);
        windowHeight = phoneSize.y;
        windowWidth = phoneSize.x;
        x = dip2px(activity, 1);
        defaultHight = sp2px(activity, 50)/2;
        y = defaultHight;

        line = (int) ((windowWidth) / (26 * this.size));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param context
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */

    public static float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param context
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public ArrayList<Integer> getStandardNums() {
        return standardNums;
    }
    public int getStandardNumsSize()
    {
        return  standardNums.size();
    }

    public void setStandardNums(ArrayList<Integer> standardNums) {
        this.standardNums = standardNums;
    }

    public int getIndexCursorLine() {
        return indexCursorLine;
    }

    public void setIndexCursorLine(int indexCursorLine) {
        this.indexCursorLine = indexCursorLine;
    }

    public int getIndexCursorRow() {
        return indexCursorRow;
    }

    public void setIndexCursorRow(int indexCursorRow) {
        this.indexCursorRow = indexCursorRow;
    }

    public float getSize() {
        return size;
    }

    int getWindowHeight() {
        return windowHeight;
    }

    int getWindowWidth() {
        return windowWidth;
    }

    void setSize(int size) {
        this.size = size;
    }

    public void setX(float x) {
        this.x = x;
    }

    public int getLine() {
        return line;
    }

    int getRow() {
        return row;
    }

    void setLine(int line) {
        this.line = line;
    }

    void setRow(int row) {
        this.row = row;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getInputBigMusicIndex() {
        return inputBigMusicIndex;
    }

    public void setInputBigMusicIndex(int inputBigMusicIndex) {
        this.inputBigMusicIndex = inputBigMusicIndex;
    }


    RelativeLayout getInputLayout() {
        return (RelativeLayout) activity.findViewById(R.id.inputLayout);
    }


    public void drawMusicNoteInput(MusicNote musicNote) {

        int inputColor = 0x0000ff00;
        // int inputColor = 0x77ffffff;
        int numColor = 0xff001111;
        RelativeLayout layout = activity
                .findViewById(R.id.inputLayout);

        TextView offsetTextView = new TextView(activity);
        offsetTextView.setId((indexCursorLine + indexCursorRow * line) * 6 + 1);
        offsetTextView.setX(x+3*size);
        offsetTextView.setY(y);
        offsetTextView.setWidth((int) (10 * size));
        offsetTextView.setHeight((int) (30 * size));
        offsetTextView.setTextSize(px2sp(activity, 8 * size));
        if (musicNote.getoffsetString() == "#") {
            offsetTextView.setTextSize(px2sp(activity, 8 * size));
        }
        offsetTextView.setText(musicNote.getoffsetString());
        offsetTextView.setBackgroundColor(inputColor);
        offsetTextView.setTextColor(Color.BLUE);
        offsetTextView.setGravity(Gravity.CENTER);
        layout.addView(offsetTextView);

        TextView pitchUpTextView = new TextView(activity);
        pitchUpTextView
                .setId((indexCursorLine + indexCursorRow * line) * 6 + 3);
        pitchUpTextView.setX(x + 10 * size);
        pitchUpTextView.setY(y);
        pitchUpTextView.setWidth((int) (15 * size));
        pitchUpTextView.setHeight((int) (10 * size));
        pitchUpTextView.setTextSize(px2sp(activity, 4 * size));
        pitchUpTextView.setBackgroundColor(inputColor);
        pitchUpTextView.setGravity(Gravity.CENTER);
        pitchUpTextView.setText(musicNote.getPitchUp());
        layout.addView(pitchUpTextView);

        TextView pitchDownTextView = new TextView(activity);
        pitchDownTextView
                .setId((indexCursorLine + indexCursorRow * line) * 6 + 4);
        pitchDownTextView.setX(x + 10 * size);
        pitchDownTextView.setY(y + 20 * size);
        pitchDownTextView.setWidth((int) (15 * size));
        pitchDownTextView.setHeight((int) (10 * size));
        pitchDownTextView.setTextSize(px2sp(activity, 4 * size));
        pitchDownTextView.setBackgroundColor(inputColor);
        pitchDownTextView.setGravity(Gravity.CENTER);
        pitchDownTextView.setText(musicNote.getPitchDown());
        layout.addView(pitchDownTextView);

        TextView bigMusciStringTextView = new TextView(activity);
        bigMusciStringTextView
                .setId((indexCursorLine + indexCursorRow * line) * 6 + 2);
        bigMusciStringTextView.setX(x + 10 * size);
        bigMusciStringTextView.setY(y);
        bigMusciStringTextView.setWidth((int) (15 * size));
        bigMusciStringTextView.setHeight((int) (30 * size));
        bigMusciStringTextView.setTextSize(px2sp(activity, 16 * size));
        if(musicNote.getChord())
        {
            bigMusciStringTextView.setTextSize(px2sp(activity, 10 * size));
            if (musicNote
                    .getMusicString().equals("Cm"))
                bigMusciStringTextView.setTextSize(px2sp(activity, 8 * size));
        }
        bigMusciStringTextView.setText(musicNote
                .getMusicString());
        bigMusciStringTextView.setBackgroundColor(inputColor);
        bigMusciStringTextView.setGravity(Gravity.CENTER);
        bigMusciStringTextView.setTextColor(numColor);
        layout.addView(bigMusciStringTextView);

        TextView checkPitchUpView = new TextView(activity);
        checkPitchUpView.setId((indexCursorLine + indexCursorRow * line) * 6 + 5);
        checkPitchUpView.setX(x+20*size); //音符升标右边5个尺寸
        checkPitchUpView.setY(y-10*size);
        checkPitchUpView.setWidth((int) (20 * size));
        checkPitchUpView.setHeight((int) (40 * size));
        checkPitchUpView.setTextSize(px2sp(activity, 10 * size));

//        checkPitchUpView.setText(musicNote.getCheckPlayStrings(checkPitch));
        checkPitchUpView.setBackgroundColor(inputColor);
        checkPitchUpView.setTextColor(Color.RED);
        checkPitchUpView.setGravity(Gravity.CENTER);
        checkPitchUpView.setVisibility(View.INVISIBLE);
        layout.addView(checkPitchUpView);

        TextView checkSpeedOKView = new TextView(activity);
        checkSpeedOKView.setId((indexCursorLine + indexCursorRow * line) * 6 + 6);
        checkSpeedOKView.setX(x+20*size); //音符升标右边5个尺寸
        checkSpeedOKView.setY(y+3*size);
        checkSpeedOKView.setWidth((int) (20 * size));
        checkSpeedOKView.setHeight((int) (40 * size));
        checkSpeedOKView.setTextSize(px2sp(activity, 10 * size));
//        checkSpeedOKView.setText(musicNote.getCheckPlayStrings(checkSpeed));
        checkSpeedOKView.setBackgroundColor(inputColor);
        checkSpeedOKView.setTextColor(Color.RED);
        checkSpeedOKView.setGravity(Gravity.CENTER);
        checkSpeedOKView.setVisibility(View.INVISIBLE);
        layout.addView(checkSpeedOKView);

        standardNums.add(line * indexCursorRow + indexCursorLine,
                musicNote.getStandardNum());
        indexCursorLine = indexCursorLine + 1;
        x = x + (25 + 1) * size;
        if (indexCursorLine % line == 0) {
            x = dip2px(activity, 1);
            y = y + (30 + 1) * size;
            indexCursorLine = 0;
            indexCursorRow = indexCursorRow + 1;
        }
        drawCursor();
    }


    @SuppressLint("ResourceType")
    public void drawCursor() {
        RelativeLayout layout = activity
                .findViewById(R.id.inputLayout);
        if (isCursorExisted == true) {
            TextView textView = layout.findViewById(123456);
            layout.removeView(textView);
        }
        TextView cursorTextView = new TextView(activity);
        cursorTextView.setId(123456);
        cursorTextView.setX(indexCursorLine * 26 * size);
        cursorTextView.setY(indexCursorRow * 31 * size + defaultHight);
        cursorTextView.setWidth((int) (2 * size));
        cursorTextView.setHeight((int) (30 * size));
        cursorTextView.setBackgroundColor(Color.BLUE);
        cursorTextView.setTextColor(Color.BLUE);
        layout.addView(cursorTextView);
        isCursorExisted = true;
    }

    public void cleanAll() {// 其实也不是完全clean了all
        cleanView();
        indexCursorLine = 0;
        indexCursorRow = 0;
        x = dip2px(activity, 1);
        y = defaultHight;
        standardNums = new ArrayList<Integer>();
        drawCursor();
        MusicNote.cleanAll();
    }

    void cleanView() {
        RelativeLayout inputLayout = activity
                .findViewById(R.id.inputLayout);

        inputLayout.removeAllViews();

    }

}
