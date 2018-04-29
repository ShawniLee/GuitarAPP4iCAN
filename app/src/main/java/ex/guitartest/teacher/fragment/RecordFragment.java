package ex.guitartest.teacher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.R;
import ex.guitartest.teacher.RecordAdapter;
import ex.guitartest.teacher.VideoAddActivity;

/**
 * Created by qyxlx on 2018/4/6.
 * 录制界面
 */

public class RecordFragment extends Fragment {
    @BindView(R.id.grid_record)
    GridView gridRecord;
    private RecordAdapter recordAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record,null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView(){
        recordAdapter = new RecordAdapter(getActivity());
        gridRecord.setAdapter(recordAdapter);
    }

    @OnClick(R.id.iv_add)
    public void addVideo(){

        Intent intent = new Intent(getActivity(),VideoAddActivity.class);
        startActivity(intent);
    }
}
