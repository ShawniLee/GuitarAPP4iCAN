package ex.guitartest.teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import butterknife.BindView;
import butterknife.ButterKnife;
import ex.guitartest.BlueTooth.RxBle;
import ex.guitartest.BlueTooth.RxBleService;
import ex.guitartest.R;
import ex.guitartest.bean.NumberClickModel;
import ex.guitartest.bean.deleteModel;
import ex.guitartest.util.SizeSwitch;
import ex.guitartest.viewutils.AudioCheckNote;
import ex.guitartest.viewutils.AudioNote;
import ex.guitartest.viewutils.MusicNote;
import ex.guitartest.viewutils.MusicNoteLayout;

/**
 * Created by qyxlx on 2018/3/15.
 */
//TODO:低音降do有bug，会导致程序崩溃
public class SpectrumWriteActivity extends AppCompatActivity {
    private static final String TAG = "Spectrum";
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
    @BindView(R.id.SpeedBar)
    SeekBar SpeedBar;
    private int pitch = 0; // 音高,1为高音，-1为低音
    private int offset = 0; // 升降
    private boolean chord = false;//默认0为指弹，1为和弦
    private final double speedCheckAccuracy=2;
    private int num = 1;
    final int ERROR_NUM = 999;
    int currentMusicNoteIndex = 0;
    int currentAudioNoteIndex = 0;
    RxBle RxbleS = RxBle.getInstance();
    private String[] chordMusicStrings = MusicNote.getChordMusicStrings();//Em为Eb,G和F#调换了位置
    private MusicNoteLayout musicNoteLayout;
    private RelativeLayout inputLayout;
    public RelativeLayout extraLayout;
    private RelativeLayout buttonLayout;
    private float defaultHight;
    AudioDispatcher dispatcher;
    AudioProcessor audioProcessor;
    Thread audioThread;
    private int numOfMusic = 0;
    //菜单文字

    String sendMusic = "";
    private int speed;

    //更新UI
    private int numOfText = 0;

    AlertDialog godDialog;
    EditText sendMessageEditText;

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
        initData();
        RxbleS.setTargetDevice("Smart_guitar");
        RxbleS.scanBleDevices(true);
        Log.e(TAG, "onCreate: dispatcher is running");
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100, 2048, 0);
        dispatcher.addAudioProcessor(audioProcessor);
        audioThread = new Thread(dispatcher, "Audio Dispatcher");
        audioThread.start();
        SpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed=i+1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spectrumwrite, menu);
        return true;
    }


    private void initResources() {
        settings = getSharedPreferences("AudioInput", MODE_PRIVATE);
        firstStart = settings.getBoolean("firstStart", true);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putBoolean("firstStart", false);
        prefEditor.apply();
        sendMessageEditText =new EditText(this);

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

        //上帝模式输入框，默认框内内容是上一次发送的内容
        godDialog=new AlertDialog.Builder(this).setView(sendMessageEditText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        godDialog.dismiss();
                    }
                })
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMusic= sendMessageEditText.getText().toString();
                        connectAndWrite(sendMusic);
                        godDialog.dismiss();
                    }
                }).create();



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
    }

    private void initData() {
        AudioNote.AudioGetMusicNoteStored.clear();
        MusicNote.MusicNoteStored.clear();
        currentMusicNoteIndex = 0;
        currentAudioNoteIndex = 0;
        speed=3;
        audioProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, 44100, 2048, new PitchDetectionHandler() {

            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    AudioEvent audioEvent) {
                float pitchInHz = pitchDetectionResult.getPitch();

                int IndexNote = ERROR_NUM;
                for (int i = 0; i < MusicNote.getStandardNoteHz(); i++) {
                    if (pitchInHz > MusicNote.getStandardNoteHz(i) - 3 && pitchInHz < MusicNote.getStandardNoteHz(i) + 3) {
                        IndexNote = i - 12;
                        break;
                    }
                    if (pitchInHz < MusicNote.getStandardNoteHz(i)) {
                        break;
                    }
                }
                if (IndexNote == ERROR_NUM) {
                    return;
                }
                if (currentMusicNoteIndex < MusicNote.MusicNoteStored.size()
                        && MusicNote.MusicNoteStored.size() != 0) {
//音符数量还没到已经存储的上限
                    if (AudioNote.AudioGetMusicNoteStored.size() > 0 &&
                            (AudioNote.getLastAudioListStandardNum() != IndexNote
                                    ||currentMusicNoteIndex == 0 || MusicNote.getMusicStanardNum(currentMusicNoteIndex)
                                    == MusicNote.getMusicStanardNum(currentMusicNoteIndex - 1))
                            || AudioNote.AudioGetMusicNoteStored.size() == 0) {
                        AudioNote.AudioGetMusicNoteStored.add(
                                new AudioNote(IndexNote, AudioNote.calcLastCurrentTimeDiff()));

                        Log.d("AudioNote", String.valueOf(AudioNote.getLastAudioListStandardNum()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (MusicNote.MusicNoteStored.size() > currentMusicNoteIndex) {
                                    int tempMusicStandard = MusicNote.
                                            deCodeStoreMusicPitch2Standard(currentMusicNoteIndex);
                                    if (AudioNote.AudioGetMusicNoteStored.get(currentAudioNoteIndex).standardNum == tempMusicStandard) {
                                        //弹对了,就是下一个音
                                        if (currentMusicNoteIndex == 0 || Math.abs(MusicNote.getMusicStandardTime(currentMusicNoteIndex) -
                                                AudioNote.getAudioStandardTime(currentAudioNoteIndex)) < MusicNote.getMusicStandardTime(currentMusicNoteIndex) / speedCheckAccuracy) {
                                            TextView textMusicNote = findViewById(currentMusicNoteIndex * 6 + 2);
                                            textMusicNote.setTextColor(Color.GREEN);
                                            Log.d("AudioMusic", "That's right");
                                            //时间也没问题
                                        } else {
                                            //时间有问题
                                            TextView textMusicNote = findViewById(currentMusicNoteIndex * 6 + 2);
                                            textMusicNote.setTextColor(Color.GREEN);
                                            TextView checkSpeedTextView = inputLayout.findViewById(currentMusicNoteIndex * 6 + 6);
                                            checkSpeedTextView.setVisibility(View.VISIBLE);
                                            if (MusicNote.getMusicStandardTime(currentMusicNoteIndex) >
                                                    AudioNote.getAudioStandardTime(currentAudioNoteIndex)) {
                                                //当前时间长了，弹慢了
                                                checkSpeedTextView.setText(AudioCheckNote.getCheckPlayStrings(AudioCheckNote.slow));
                                            } else {
                                                checkSpeedTextView.setText(AudioCheckNote.getCheckPlayStrings(AudioCheckNote.fast));
                                            }

                                        }
                                    } else if (MusicNote.MusicNoteStored.size() > currentMusicNoteIndex + 1
                                            && AudioNote.AudioGetMusicNoteStored.get(currentAudioNoteIndex).standardNum ==
                                            MusicNote.deCodeStoreMusicPitch2Standard(currentMusicNoteIndex + 1)) {
                                        Log.d("AudioMusic", "Is  the Next Music Note?");
                                        //是下下个音
                                        TextView textMusicNote = findViewById(currentMusicNoteIndex * 6 + 2);
                                        textMusicNote.setTextColor(Color.RED);
                                        currentMusicNoteIndex++;
                                        if (Math.abs(MusicNote.getMusicStandardTime(currentMusicNoteIndex) -
                                                AudioNote.getAudioStandardTime(currentAudioNoteIndex)) < MusicNote.getMusicStandardTime(currentMusicNoteIndex)/speedCheckAccuracy) {
                                            textMusicNote = findViewById(currentMusicNoteIndex * 6 + 2);
                                            textMusicNote.setTextColor(Color.GREEN);
                                            //时间也没问题
                                        } else {
                                            //时间有问题
                                            textMusicNote = findViewById(currentMusicNoteIndex * 6 + 2);
                                            textMusicNote.setTextColor(Color.GREEN);
                                            TextView checkSpeedTextView = inputLayout.findViewById(currentMusicNoteIndex * 6 + 6);
                                            checkSpeedTextView.setVisibility(View.VISIBLE);
                                            if (MusicNote.getMusicStandardTime(currentMusicNoteIndex) >
                                                    AudioNote.getAudioStandardTime(currentAudioNoteIndex)) {
                                                //当前时间长了，弹慢了
                                                checkSpeedTextView.setText(AudioCheckNote.getCheckPlayStrings(AudioCheckNote.slow));
                                            } else {
                                                checkSpeedTextView.setText(AudioCheckNote.getCheckPlayStrings(AudioCheckNote.fast));
                                            }
                                        }
                                    } else {
                                        //没找到这个音
                                        if (Math.abs(MusicNote.getMusicStandardTime(currentMusicNoteIndex) -
                                                AudioNote.getAudioStandardTime(currentAudioNoteIndex)) < MusicNote.getMusicStandardTime(currentMusicNoteIndex)/speedCheckAccuracy) {
                                            //时间没问题
                                            TextView textMusicNote = findViewById(currentMusicNoteIndex * 6 + 2);
                                            textMusicNote.setTextColor(Color.GREEN);
                                            TextView checkPitchTextView = inputLayout.findViewById(currentMusicNoteIndex * 6 + 5);
                                            checkPitchTextView.setVisibility(View.VISIBLE);
                                            if (MusicNote.getMusicStanardNum(currentMusicNoteIndex) >
                                                    AudioNote.getAudioStandardNum(currentAudioNoteIndex)) {
                                                checkPitchTextView.setText(AudioCheckNote.getCheckPlayStrings(AudioCheckNote.high));
                                            } else {
                                                checkPitchTextView.setText(AudioCheckNote.getCheckPlayStrings(AudioCheckNote.low));
                                            }
                                        } else {
                                            //时间也有问题
                                            TextView textMusicNote = findViewById(currentMusicNoteIndex * 6 + 2);
                                            textMusicNote.setTextColor(Color.RED);
                                            currentMusicNoteIndex--;

                                        }
                                    }
                                    currentMusicNoteIndex++;
                                    currentAudioNoteIndex++;
                                }

                            }
                        });
                    }
                }
            }
        });
    }


    private class NewItemSelectedListener1 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   int position, long arg3) {
            ArrayList<Integer> standardNums = musicNoteLayout.getStandardNums();
            if (standardNums != null) {
                for (int i = 0; i < standardNums.size(); i++) {
                    //6个一组，1是升降调，2音符，3高音上标，4低音下标，5高低音检查，6快慢检查
                    MusicNote musicNote = new MusicNote(standardNums.get(i));
                    TextView offsetTextView = inputLayout
                            .findViewById(i * 6 + 1);
                    TextView bigMusciStringTextView = inputLayout
                            .findViewById(i * 6 + 2);
                    TextView pitchUpTextView = inputLayout
                            .findViewById(i * 6 + 3);
                    TextView pitchDownTextView = inputLayout
                            .findViewById(i * 6 + 4);


                    offsetTextView.setText(musicNote
                            .getoffsetString());
                    bigMusciStringTextView.setText(musicNote
                            .getMusicString());
                    pitchUpTextView.setText(musicNote
                            .getPitchUp());
                    pitchDownTextView.setText(musicNote
                            .getPitchDown());
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    //监听左侧按键选择
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


    //删除音符表中的字符
    private class backSpaceOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new deleteModel().setMusicNoteLayout(musicNoteLayout, inputLayout, SpectrumWriteActivity.this);
            MusicNote.deleteObject();
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
                case R.id.cleanAll:// 清空
                    MusicNote.cleanAll();
                    break;
                case R.id.ANote:// 单个音符

                    // Toast.makeText(SpectrumWriteActivity.this, "抱歉，暂不支持保存功能！",
                    //         Toast.LENGTH_SHORT).show();
                    sendMusic = "@TS" + MusicNote.getMusicNoteStored2String() + "#";
                    RxbleS.sendData(sendMusic.getBytes(), 1000);
                    break;
                case R.id.slowMusic:// 乐曲慢弹
                    // musicNoteLayout.update(40,4);
                    //Toast.makeText(SpectrumWriteActivity.this, "您可以在主界面上设置音符尺寸与模式",
                    //      Toast.LENGTH_SHORT).show();
                    sendMusic = "@T"+String.format("%03d",MusicNote.MusicNoteStored.size())+"0" + MusicNote.getMusicNoteStored2String()  + "#";
                    //TODO:默认音频速度是首音符速度
                    connectAndWrite(sendMusic);
                    break;
                case R.id.normalMusic:// 乐曲连弹
                    //helpDialog.show();
                    sendMusic = "@T"+String.format("%03d",MusicNote.MusicNoteStored.size())+"0" + MusicNote.getMusicNoteStored2String() + "#";
                    connectAndWrite(sendMusic);
                    break;
                case R.id.pause:// 暂停 改隐藏控件
                    RxbleS.sendData("@Tpause#".getBytes(), 1000);
                    buttonLayout.setVisibility(View.INVISIBLE);
                    numOfMusic = musicNoteLayout.getStandardNumsSize();
                    for (int i = 0; i < numOfMusic; i++) {
                        TextView pitchText = findViewById(i * 6 + 5);
                        TextView speedText = findViewById(i * 6 + 6);
                        pitchText.setVisibility(View.INVISIBLE);
                        speedText.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.stop:// 停止
                    numOfMusic = musicNoteLayout.getStandardNumsSize();
                    RxbleS.sendData("@Tstop#".getBytes(), 1000);
                    break;
            case R.id.godMode:
                godDialog.show();
                break;
            }
        sendMessageEditText.setText(sendMusic);
        return super.onOptionsItemSelected(item);
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
        setMusicNote(num, offset, pitch, chord, speed);
    }

    private void connectAndWrite(final String TotalData) {
        final int DelayTime=1200;
        RxbleS.sendData(TotalData.getBytes(), DelayTime);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, PracticeResultActivity.class);
        startActivity(intent);
        dispatcher.removeAudioProcessor(audioProcessor);
        dispatcher.stop();
        finish();
        //RxbleS.sendData("@Tstop#".getBytes(),500);

    }

    private void setMusicNote(int num, int offset, int pitch, boolean chord, int speed) {
        MusicNote musicNote = new MusicNote(num, offset, pitch, chord, speed);

        musicNoteLayout.drawMusicNoteInput(musicNote);// 输入的调

        if (musicNoteLayout.getStandardNums().size() > 1) {
            new NumberClickModel().setMusicNoteLayout(musicNoteLayout, inputLayout);
        }
    }


}
