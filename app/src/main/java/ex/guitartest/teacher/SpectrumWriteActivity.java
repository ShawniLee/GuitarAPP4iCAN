package ex.guitartest.teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ex.guitartest.BlueTooth.RxBle;
import ex.guitartest.BlueTooth.RxBleService;
import ex.guitartest.R;
import ex.guitartest.bean.NumberClickModel;
import ex.guitartest.bean.deleteModel;
import ex.guitartest.util.SharedPreferencesUtil;
import ex.guitartest.util.SizeSwitch;
import ex.guitartest.viewutils.MusicNote;
import ex.guitartest.viewutils.MusicNoteLayout;

/**
 *
 * Created by qyxlx on 2018/3/15.
 */

public class SpectrumWriteActivity extends AppCompatActivity {
    @BindView(R.id.button7)
    Button button7;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.button5)
    Button button5;
    @BindView(R.id.button6)
    Button button6;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button1)
    Button button1;
    private int pitch = 0; // 音高,1为高音，-1为低音
    private int offset = 0; // 升降
    private boolean chord = false;//默认0为指弹，1为和弦
    private int num = 1;
    private int splitNoteNum = 0;
    private String SplitData[] = new String[8];
    RxBle RxbleS = RxBle.getInstance();
    private String[] chordMusicStrings = new String[]{"C", "C#", "D", "Em", "E", "F","G","F#", "Ab", "A", "Bb", "B"};//Em为Eb,G和F#调换了位置
    private MusicNoteLayout musicNoteLayout;
    private RelativeLayout inputLayout;
    public RelativeLayout extraLayout;
    private RelativeLayout buttonLayout;
    private int inputBigMusicIndex = 0;
    private float defaultHight;
    private int checkSpeed = 0;
    private int checkPitch = 2;
    int[] menu_image_array = {R.drawable.ic_menu_delete,
            R.drawable.ic_menu_save, R.drawable.ic_menu_preferences,
            R.drawable.ic_menu_help, R.drawable.ic_menu_info_details,
            R.drawable.ic_menu_favorite};
    //菜单文字
    String[] menu_name_array = {"清空","单个音符", "乐曲慢弹", "乐曲连弹", "暂停", "停止" };
    AlertDialog menuDialog;// menu菜单Dialog
    AlertDialog promptDialog; // 提示对话框
    AlertDialog helpDialog;
    GridView menuGrid;
    View menuView;
    String sendMusic="";

    private SharedPreferences settings;
    private boolean firstStart = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectrum_write);
        ButterKnife.bind(this);
        Intent intent = new Intent(this, RxBleService.class);
        this.startService(intent);
        initResources();
        initView();
        RxbleS.setTargetDevice("Smart_guitar");
        RxbleS.scanBleDevices(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("menu");// 必须创建一项
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menuDialog == null) {
            menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
        } else {
            menuDialog.show();
        }
        return false;// 返回为true 则显示系统menu
    }

    private void initResources() {
        settings = getSharedPreferences("test", MODE_PRIVATE);
        firstStart = settings.getBoolean("firstStart", true);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putBoolean("firstStart", false);
        prefEditor.apply();


        defaultHight = SizeSwitch.sp2px(SpectrumWriteActivity.this, 50);
        musicNoteLayout = new MusicNoteLayout((50 + 20) / 2, this);// 尺寸每个音符的宽度（基准尺寸为50）

        inputLayout = findViewById(R.id.inputLayout);
        extraLayout = findViewById(R.id.extraLayout);
        buttonLayout = findViewById(R.id.buttonLayout);
    }

    private void initView() {


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, chordMusicStrings);
        adapter.setDropDownViewResource(R.layout.myspinner_dropdown);

        RadioGroup radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(onCheckedChangeListener);
        RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup2.setOnCheckedChangeListener(onCheckedChangeListener);
        RadioGroup radioGroup3 = findViewById(R.id.radioGroup3);
        radioGroup3.setOnCheckedChangeListener(onCheckedChangeListener);

        Button backSpacebButton = findViewById(R.id.buttonBackSpace);
        backSpacebButton.setOnClickListener(new backSpaceOnClickListener());

        menuView = View.inflate(this, R.layout.my_menu, null);
        // 创建AlertDialog
        menuDialog = new AlertDialog.Builder(this).create();
        // 设置透明度
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        window.setAttributes(lp);
        menuDialog.setView(menuView);
        menuDialog.setOnKeyListener(new NewKeyListener());
        menuGrid = menuView.findViewById(R.id.gridview);
        menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
        /** 监听menu选项 **/
        menuGrid.setOnItemClickListener(new NewItemClickListener());

        // 下面是提示信息的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息！");
        builder.setMessage("该操作将会清空当前数据，继续？");
        builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                musicNoteLayout.cleanAll();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        promptDialog = builder.create();
        Window promptDialogWindow = promptDialog.getWindow();
        WindowManager.LayoutParams lp_promptDialog = promptDialogWindow.getAttributes();
        lp_promptDialog.alpha = 0.8f;
        promptDialogWindow.setAttributes(lp);
        // 下面是帮助对话框
        AlertDialog.Builder builder_help = new AlertDialog.Builder(this);
        builder_help.setTitle("帮助信息！");
        builder_help.setMessage("1. 在左上角选择输入简谱的调号\n2. 使用软键盘输入音符\n3. 选择不同的调号即可完成转调\n");
        builder_help.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        helpDialog = builder_help.create();
    }



    private class NewItemSelectedListener1 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   int position, long arg3) {
            ArrayList<Integer> standardNums = musicNoteLayout.getStandardNums();
            if (standardNums != null) {
                for (int i = 0; i < standardNums.size(); i++) {
                    MusicNote musicNote = new MusicNote(standardNums.get(i));
                    TextView offsetTextView = inputLayout
                            .findViewById(i * 6 + 1);
                    TextView bigMusciStringTextView = inputLayout
                            .findViewById(i * 6 + 2);
                    TextView pitchUpTextView = inputLayout
                            .findViewById(i * 6 + 3);
                    TextView pitchDownTextView = inputLayout
                            .findViewById(i * 6 + 4);
                    TextView checkPitchTextView=inputLayout
                            .findViewById(i* 6 + 5);
                    TextView checkSpeedTextView=inputLayout
                            .findViewById(i* 6 + 6);

                    offsetTextView.setText(musicNote
                            .getoffsetString(inputBigMusicIndex));
                    bigMusciStringTextView.setText(musicNote
                            .getMusicString(inputBigMusicIndex));
                    pitchUpTextView.setText(musicNote
                            .getPitchUp(inputBigMusicIndex));
                    pitchDownTextView.setText(musicNote
                            .getPitchDown(inputBigMusicIndex));
                    checkPitchTextView.setText(musicNote.
                            getCheckPlayStrings(checkPitch));
                    checkSpeedTextView.setText(musicNote.
                            getCheckPlayStrings(checkSpeed));
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio0:
                    pitch = 1;
                    break;
                case R.id.radio1:
                    pitch = 0;
                    break;
                case R.id.radio2:
                    pitch = -1;
                    break;
                case R.id.radio3:
                    offset = 1;
                    break;
                case R.id.radio4:
                    offset = 0;
                    break;
                case R.id.radio5:
                    offset = -1;
                    break;
                case R.id.radio6:
                    chord = false;
                    button1.setText("1");
                    button2.setText("2");
                    button3.setText("3");
                    button4.setText("4");
                    button5.setText("5");
                    button6.setText("6");
                    button7.setText("7");
                    break;
                case R.id.radio7:
                    chord = true;
                    button1.setText(chordMusicStrings[0]);
                    button2.setText(chordMusicStrings[1]);
                    button3.setText(chordMusicStrings[2]);
                    button4.setText(chordMusicStrings[3]);
                    button5.setText(chordMusicStrings[4]);
                    button6.setText(chordMusicStrings[5]);
                    button7.setText(chordMusicStrings[6]);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "pitch或offset错误", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    };




    private class backSpaceOnClickListener implements View.OnClickListener {//删除音符表中的字符
        @Override
        public void onClick(View v) {
            new deleteModel().setMusicNoteLayout(musicNoteLayout, inputLayout, SpectrumWriteActivity.this);
            MusicNote.deleteObject();
        }
    }

    private class NewKeyListener implements DialogInterface.OnKeyListener {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
                dialog.dismiss();
            return false;
        }
    }

    private class NewItemClickListener implements
            AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            menuDialog.hide();
            switch (arg2) {
                case 0:// 清空
                    promptDialog.show();
                MusicNote.cleanAll();
                    break;
                case 1:// 单个音符

                   // Toast.makeText(SpectrumWriteActivity.this, "抱歉，暂不支持保存功能！",
                   //         Toast.LENGTH_SHORT).show();
                    sendMusic="@TS"+ MusicNote.StoreMusicNote +"#";
                    RxbleS.sendData(sendMusic.getBytes(),1000);
                    break;
                case 2:// 乐曲慢弹
                    // musicNoteLayout.update(40,4);
                    //Toast.makeText(SpectrumWriteActivity.this, "您可以在主界面上设置音符尺寸与模式",
                    //      Toast.LENGTH_SHORT).show();
                    sendMusic="@T"+ MusicNote.StoreMusicNote  +"#";
                    connectAndWrite(sendMusic);
                    break;
                case 3:// 乐曲连弹
                    //helpDialog.show();
                    sendMusic="@T"+ MusicNote.StoreMusicNote  +"#";
                    connectAndWrite(sendMusic);
                    break;
                case 4:// 暂停
//                    Intent intent = new Intent();
//                    intent.setClass(SpectrumWriteActivity.this, DetailActivity.class);
//                    SpectrumWriteActivity.this.startActivity(intent);
                    RxbleS.sendData("@Tpause#".getBytes(),1000);
                    break;
                case 5:// 停止
                    RxbleS.sendData("@Tstop#".getBytes(),1000);
//                    appWallManager.show();
                    break;
            }
        }
    }

    private SimpleAdapter getMenuAdapter(String[] menuNameArray,
                                         int[] imageResourceArray) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < menuNameArray.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", imageResourceArray[i]);
            map.put("itemText", menuNameArray[i]);
            data.add(map);
        }
        SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
                R.layout.my_menu_items,
                new String[]{"itemImage", "itemText"}, new int[]{
                R.id.item_image, R.id.item_text});
        return simperAdapter;
    }

    public void numClick(View view) {
        switch (Integer.parseInt((String) view.getTag())) {
            case 0:
                num = 0;
                break;
            case 1:
                num = 1;
                break;
            case 2:
                num = 2;
                break;
            case 3:
                num = 3;
                break;
            case 4:
                num = 4;
                break;
            case 5:
                num = 5;
                break;
            case 6:
                num = 6;
                break;
            case 7:
                num = 7;
                break;
            default:
                Toast.makeText(getApplicationContext(), "num错误", Toast.LENGTH_SHORT).show();
                break;
        }


        int row = (int) ((inputLayout.getHeight() - defaultHight) / (31 * musicNoteLayout
                .getSize()));
        //if (musicNoteLayout.getStandardNums().size() >= musicNoteLayout
         //       .getLine() * row) {
            // Toast.makeText(MainActivity.this, "窗口已满！",
            // Toast.LENGTH_SHORT).show();
            // System.out.println("line"+musicNoteLayout.getInputLayout().getHeight());
        setMusicNote(inputBigMusicIndex,num,offset,pitch,chord,checkSpeed,checkPitch);
    }
    private void connectAndWrite(final String TotalData) {
        splitNoteNum =0;
        if (TotalData.length()>=20)
        {
            for (int j=0;j<TotalData.length();j+=19, splitNoteNum++)
            {
                if(j+19<TotalData.length())
                    SplitData[splitNoteNum]=TotalData.substring(j,j+19);
                else {
                    SplitData[splitNoteNum] = TotalData.substring(j, TotalData.length());
                    break;
                }
            }
        }
        else SplitData[0]=TotalData;
        Runnable myRunnable= new Runnable() {
            public void run() {
                for (int j = 0; j<= splitNoteNum; j++)
                {
                    try {
                        RxbleS.sendData(SplitData[j].getBytes(),1000);
                        Thread.sleep(1000);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        RxbleS.sendData("@Tstop#".getBytes(),500);
    }
    private void setMusicNote(int inputBigMusicIndex,int num,int offset,int pitch,boolean chord,int checkSpeed,int checkPitch)
    {
        MusicNote musicNote = new MusicNote(inputBigMusicIndex, num,
                offset, pitch,chord);
        musicNoteLayout.drawMusicNoteInput(musicNote, inputBigMusicIndex,checkSpeed,checkPitch);// 输入的调

        if (musicNoteLayout.getStandardNums().size() > 1)
        {
            new NumberClickModel().setMusicNoteLayout(musicNoteLayout, inputLayout);
        }
    }
    }
