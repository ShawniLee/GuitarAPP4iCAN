package ex.guitartest.teacher.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.LoginActivity;
import ex.guitartest.R;

/**
 * Created by qyxlx on 2018/4/30 0024.
 */

public class MyFragment extends Fragment {
    PopupMenu popupMenu;
    @BindView(R.id.Name)
    TextView Name;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.birth)
    TextView birth;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;


    private int judgeClick = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);
        popupMenu = new PopupMenu(getActivity(), ivSetting);
        popupMenu.getMenuInflater().inflate(R.menu.menuinfo, popupMenu.getMenu());

        return view;
    }

    @OnClick(R.id.iv_setting)
    public void onViewClicked() {
        // View当前PopupMenu显示的相对View的位置

        // menu布局
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Logout:
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.FeedBack:
                        break;
                    case R.id.editinfo:
                        judgeClick = 1;
                        Toast.makeText(getActivity(), "单击需要修改信息的位置，即可修改相应的信息", Toast.LENGTH_SHORT).show();

                        break;
                }
                return false;
            }


        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });

        popupMenu.show();
    }





    @OnClick({R.id.Name, R.id.sex, R.id.birth, R.id.location, R.id.email})
    public void onViewClicked(View view) {
        if (judgeClick == 0) {
            return;
        }
        switch (view.getId()) {
            case R.id.Name:
                break;
            case R.id.sex:
                break;
            case R.id.birth:
                break;
            case R.id.location:
                break;
            case R.id.email:
                break;
        }
    }
}
