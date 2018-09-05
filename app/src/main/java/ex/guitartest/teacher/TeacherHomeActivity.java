package ex.guitartest.teacher;

import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.BlueTooth.RxBle;
import ex.guitartest.R;
import ex.guitartest.teacher.fragment.HomeFragment;
import ex.guitartest.teacher.fragment.MyFragment;
import ex.guitartest.teacher.fragment.MyStudentFragment;
import ex.guitartest.teacher.fragment.RecordFragment;
import ex.guitartest.teacher.fragment.SpectrumFragment;

/**
 * Created by qyxlx on 2018/4/6.
 * 教师端主页
 */

public class TeacherHomeActivity extends AppCompatActivity{
    @BindView(R.id.frame_content)
    FrameLayout frameContent;
    @BindView(R.id.layout_home)
    RelativeLayout layout_home;
    @BindView(R.id.img_home)
    ImageView img_home;
    @BindView(R.id.tv_home)
    TextView tv_home;

    @BindView(R.id.layout_record)
    RelativeLayout layout_record;
    @BindView(R.id.img_record)
    ImageView img_record;
    @BindView(R.id.tv_record)
    TextView tv_record;

    @BindView(R.id.layout_spectrum)
    RelativeLayout layout_spectrum;
    @BindView(R.id.img_spectrum)
    ImageView img_spectrum;
    @BindView(R.id.tv_spectrum)
    TextView tv_spectrum;

    @BindView(R.id.layout_student)
    RelativeLayout layout_student;
    @BindView(R.id.img_student)
    ImageView img_student;
    @BindView(R.id.tv_student)
    TextView tv_student;

    @BindView(R.id.layout_my)
    RelativeLayout layout_my;
    @BindView(R.id.img_my)
    ImageView img_my;
    @BindView(R.id.tv_my)
    TextView tv_my;

    private int selectColor;
    private int unSelectColor;
    private Fragment[] fragments;
    private int currentIndex ;
    private int index ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        ButterKnife.bind(this);
        initView();
        RxBle RxbleS = RxBle.getInstance();
        RxbleS.openBle(this);
        RxbleS.setTargetDevice("Smart_guitar");
        RxbleS.scanBleDevices(true);
    }

    //初始化界面
    private void initView(){
        selectColor = getResources().getColor(R.color.tab_selected);
        unSelectColor = getResources().getColor(R.color.tab_unselect);
        fragments=new Fragment[5];
        fragments[0] = new HomeFragment();
        fragments[1] = new RecordFragment();
        fragments[2] = new SpectrumFragment();
        fragments[3] = new MyStudentFragment();
        fragments[4] = new MyFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content,fragments[0]).commit();
        img_home.setImageResource(R.drawable.hoempage);
        tv_home.setTextColor(selectColor);
    }


    @OnClick({R.id.layout_home,R.id.layout_record,
            R.id.layout_spectrum,R.id.layout_student,R.id.layout_my})
    public void changeItem(View view){
        switch (view.getId()){
            case R.id.layout_home:
                index=0;
                setTabs(index);
                break;
            case R.id.layout_record:
                index=1;
                setTabs(index);
                break;
            case R.id.layout_spectrum:
                index=2;
                setTabs(index);
                break;
            case R.id.layout_student:
                index=3;
                setTabs(index);
                break;
            case R.id.layout_my:
                index=4;
                setTabs(index);
                break;
        }

        if(currentIndex!=index){
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.hide(fragments[currentIndex]);
            if(!fragments[index].isAdded()){
                ft.add(R.id.frame_content,fragments[index]);
            }
            ft.show(fragments[index]).commit();
        }
        currentIndex=index;
    }

    public void setTabs(int pos) {
        resetColor();
        switch (pos) {
            case 0:
                img_home.setImageResource(R.drawable.hoempage);
                tv_home.setTextColor(selectColor);
                break;
            case 1:
                img_record.setImageResource(R.drawable.complete);
                tv_record.setTextColor(selectColor);
                break;
            case 2:
                img_spectrum.setImageResource(R.drawable.write);
                tv_spectrum.setTextColor(selectColor);
                break;
            case 3:
                img_student.setImageResource(R.drawable.friends);
                tv_student.setTextColor(selectColor);
                break;
            case 4:
                img_my.setImageResource(R.drawable.data);
                tv_my.setTextColor(selectColor);
                break;
        }
    }

    private void resetColor(){
        img_home.setImageResource(R.drawable.ic_maps_homepage);
        tv_home.setTextColor(unSelectColor);
        img_record.setImageResource(R.drawable.ic_maps_complete);
        tv_record.setTextColor(unSelectColor);
        img_spectrum.setImageResource(R.drawable.ic_maps_write);
        tv_spectrum.setTextColor(unSelectColor);
        img_student.setImageResource(R.drawable.ic_maps_friends);
        tv_student.setTextColor(unSelectColor);
        img_my.setImageResource(R.drawable.ic_maps_data);
        tv_my.setTextColor(unSelectColor);
    }
}
