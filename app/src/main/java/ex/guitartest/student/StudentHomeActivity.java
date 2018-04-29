package ex.guitartest.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.R;
import ex.guitartest.teacher.fragment.HomeFragment;
import ex.guitartest.teacher.fragment.MyFragment;
import ex.guitartest.teacher.fragment.SpectrumFragment;

/**
 *
 * Created by qyxlx on 2018/4/14.
 */

public class StudentHomeActivity extends AppCompatActivity {
    @BindView(R.id.frame_content)
    FrameLayout frameContent;
    @BindView(R.id.img_home)
    ImageView img_home;
    @BindView(R.id.tv_home)
    TextView tv_home;

    @BindView(R.id.img_spectrum_look)
    ImageView img_spectrum_look;
    @BindView(R.id.tv_spectrum_look)
    TextView tv_spectrum_look;

    @BindView(R.id.iv_spectrum_write)
    ImageView iv_spectrum_write;
    @BindView(R.id.tv_spectrum_write)
    TextView tv_spectrum_write;

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
        setContentView(R.layout.activity_student_home);
        ButterKnife.bind(this);

        initView();
    }
    //初始化界面
    private void initView(){
        selectColor = getResources().getColor(R.color.tab_selected);
        unSelectColor = getResources().getColor(R.color.tab_unselect);
        fragments=new Fragment[5];
        fragments[0] = new HomeFragment();
        fragments[1] = new SpectrumLookFragment();
        fragments[2] = new SpectrumFragment();
        fragments[3] = new MyFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content,fragments[0]).commit();
        img_home.setImageResource(R.drawable.hoempage);
        tv_home.setTextColor(selectColor);
    }


    @OnClick({R.id.layout_home,R.id.layout_spectrum_look,
            R.id.layout_spectrum_write,R.id.layout_my})
    public void changeItem(View view){
        switch (view.getId()){
            case R.id.layout_home:
                index=0;
                setTabs(index);
                break;
            case R.id.layout_spectrum_look:
                index=1;
                setTabs(index);
                break;
            case R.id.layout_spectrum_write:
                index=2;
                setTabs(index);
                break;
            case R.id.layout_my:
                index=3;
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
                img_spectrum_look.setImageResource(R.drawable.look_select);
                tv_spectrum_look.setTextColor(selectColor);
                break;
            case 2:
                iv_spectrum_write.setImageResource(R.drawable.write);
                tv_spectrum_write.setTextColor(selectColor);
                break;
            case 3:
                img_my.setImageResource(R.drawable.data);
                tv_my.setTextColor(selectColor);
                break;
        }
    }

    private void resetColor(){
        img_home.setImageResource(R.drawable.ic_maps_homepage);
        tv_home.setTextColor(unSelectColor);
        img_spectrum_look.setImageResource(R.drawable.look);
        tv_spectrum_look.setTextColor(unSelectColor);
        iv_spectrum_write.setImageResource(R.drawable.ic_maps_write);
        tv_spectrum_write.setTextColor(unSelectColor);
        img_my.setImageResource(R.drawable.ic_maps_data);
        tv_my.setTextColor(unSelectColor);
    }
}
