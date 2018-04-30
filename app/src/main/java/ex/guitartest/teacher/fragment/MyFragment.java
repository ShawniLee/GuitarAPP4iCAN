package ex.guitartest.teacher.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.LoginActivity;
import ex.guitartest.R;

/**
 *
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
    @BindView(R.id.nickname)
    TextView nickName;

    int whichClick = 0;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    @BindView(R.id.occupation)
    TextView occupation;
    private int judgeClick = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);

       initMenuAndDialog();
        return view;
    }

    private void initMenuAndDialog() {
        final EditText et = new EditText(getActivity());
        popupMenu = new PopupMenu(getActivity(), ivSetting);
        popupMenu.getMenuInflater().inflate(R.menu.menuinfo, popupMenu.getMenu());
        progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("上传中...");
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("修改信息")//设置对话框的标题
                .setView(et)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.show();

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();

                        switch (whichClick) {
                            case 1:
                                Name.setText(et.getText().toString());
                                break;
                            case 2:
                                sex.setText(et.getText().toString());
                                break;
                            case 3:
                                birth.setText(et.getText().toString());
                                break;
                            case 4:
                                location.setText(et.getText().toString());
                                break;
                            case 5:
                                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(et.getText().toString()).matches())
                                {
                                    Toast.makeText(getActivity(), "请输入正确的Email", Toast.LENGTH_SHORT).show();
                                }
                                else
                                email.setText(et.getText().toString());
                                break;
                            case 6:
                                occupation.setText(et.getText().toString());
                                break;
                            case 7:
                                Toast.makeText(getActivity(), "谢谢您的反馈", Toast.LENGTH_SHORT).show();
                                whichClick = 0;
                                judgeClick = 0;
                                alertDialog.setTitle("修改信息");
                                return;
                            case 8:
                                nickName.setText(et.getText().toString());
                                break;
                            default:
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                        whichClick = 0;
                        judgeClick = 0;
                                    }
                                }, 2000);
                        dialog.dismiss();
                    }
                }).create();
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
                        alertDialog.setTitle("意见反馈");
                        alertDialog.show();
                        whichClick=7;
                        //et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        break;
                    case R.id.editinfo:
                        if (judgeClick==0)
                        judgeClick = 1;
                        else judgeClick=0;
                        Toast.makeText(getActivity(),
                                "单击需要修改信息的位置，即可修改相应的信息,再次单击该菜单选项退出修改模式",
                                Toast.LENGTH_LONG).show();

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


    @OnClick({R.id.Name, R.id.sex, R.id.birth, R.id.location, R.id.email,R.id.occupation,R.id.nickname})
    public void onViewClicked(View view) {
        if (judgeClick == 0) {
            return;
        }
        switch (view.getId()) {
            case R.id.Name:
                whichClick = 1;
                break;
            case R.id.sex:
                whichClick = 2;
                break;
            case R.id.birth:
                whichClick = 3;
                break;
            case R.id.location:
                whichClick = 4;
                break;
            case R.id.email:
                whichClick = 5;
                break;
            case R.id.occupation:
                whichClick=6;
                break;
            case R.id.nickname:
                whichClick=8;
                break;
        }
        alertDialog.show();
    }

}
