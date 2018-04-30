package ex.guitartest.teacher.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.BlueTooth.RxBle;
import ex.guitartest.R;
import ex.guitartest.bean.SpectrumBean;
import ex.guitartest.teacher.SpectrumListAdapter;
import ex.guitartest.teacher.SpectrumWriteActivity;
import ex.guitartest.viewutils.SideLetterBar;

/**
 *
 * Created by qyxlx on 2018/3/6 0006.
 */

public class SpectrumFragment extends Fragment {

    private static final String MAC = "DD:7F:9E:76:7B:8B";

    private static final UUID UUID_SERVICE_CHANNEL
            = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    private static final UUID UUID_CHARACTERISTIC_CHANNEL
            = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    @BindView(R.id.list_spectrum)
    ListView listSpectrum;
    @BindView(R.id.tv_letter_overlay)
    TextView overlay;
    @BindView(R.id.side_letter_bar)
    SideLetterBar mLetterBar;
    @BindView(R.id.ClockDown)
    TextView clockDown;
    private List<SpectrumBean> SepetrumData = new ArrayList<>();
    RxBle RxbleS=RxBle.getInstance();
    final String items[] = {"单个音符", "乐曲慢弹", "乐曲连弹"};
    private SpectrumListAdapter spectrumListAdapter;
    String CheckName;
    private int splitNoteNum = 0;
    private String SplitData[] = new String[8];
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spectrum,null);
        ButterKnife.bind(this,view);
        initView();

        return view;
    }
    private void initView(){
        SepetrumData.add(new SpectrumBean("爱的初体验","aidechutiyan"));
        SepetrumData.add(new SpectrumBean("ABCDE","Abcde"));
        SepetrumData.add(new SpectrumBean("宝贝","baobei"));
        SepetrumData.add(new SpectrumBean("贝加尔湖畔","beijiaerhupan"));
        SepetrumData.add(new SpectrumBean("童年","tongnian"));
        SepetrumData.add(new SpectrumBean("小星星","xiaoxingxing"));

        spectrumListAdapter = new SpectrumListAdapter(getActivity(),SepetrumData);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("播放模式选择")//设置对话框的标题
                .setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), items[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BLEwriteData(which);
                               Toast.makeText(getActivity(), "已发送到设备中，3s后开始", Toast.LENGTH_SHORT).show();
                               countDownTimer.start();
                        dialog.dismiss();
                    }
                }).create();

        spectrumListAdapter.setOnItemClickListener(new SpectrumListAdapter.OnItemClickListener() {
            @Override
            public void onClick(String name) {
                    CheckName=name;
                    dialog.show();
                }

        });
        listSpectrum.setAdapter(spectrumListAdapter);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = spectrumListAdapter.getLetterPosition(letter);
                listSpectrum.setSelection(position);
            }
        });
    }

    @OnClick(R.id.iv_add)
    public void addSpectrum(){
        Intent intent = new Intent(getActivity(),SpectrumWriteActivity.class);
        startActivity(intent);
    }
    public void BLEwriteData(int PlayModuleResult)
    {
        String TotalString="";
    String preCode="@T";
    switch (PlayModuleResult)
    {
        case 1:
            break;
        case 2:
            break;
        case 3:
            break;
        default:
            break;
    }
    switch (CheckName)
    {
        case "小星星":
            TotalString=preCode+"starlet"+"#";
            break;
        case "童年":
            TotalString=preCode+"childhood"+"#";
            break;
        case "贝加尔湖畔":
            Intent intent = new Intent(getActivity(),SpectrumWriteActivity.class);
            startActivity(intent);
            //TODO:重构该部分跳转
    }
        connectAndWrite(TotalString);
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
    CountDownTimer countDownTimer=new CountDownTimer(3000,500) {
        @Override
        public void onTick(long millisUntilFinished) {
            if(getActivity()!=null)
            clockDown.setText(millisUntilFinished/1000+"秒");
        }

        @Override
        public void onFinish() {
            clockDown.setText("");
        }
    };
    @Override
    public void onDestroy()
    {
        if (countDownTimer!=null)
        {
            countDownTimer.cancel();
            countDownTimer=null;
        }
        super.onDestroy();
    }
}
