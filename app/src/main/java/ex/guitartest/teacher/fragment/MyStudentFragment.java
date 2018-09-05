package ex.guitartest.teacher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ex.guitartest.R;
import ex.guitartest.teacher.ChatActivity;
import ex.guitartest.teacher.MessageActivity;
import ex.guitartest.teacher.SpectrumListAdapter;
import ex.guitartest.teacher.StudentListAdapter;
import ex.guitartest.teacher.TeacherHomeActivity;

import static android.app.Activity.RESULT_OK;


/**
 * Created by qyxlxr on 2018/3/8 1008.
 */

public class MyStudentFragment extends Fragment {

    GridView gridStudent;
    private GridView grid_student;
    private StudentListAdapter studentListAdapter;
    String name[] = {"李湘宁", "黄庙林", "黄茵绮", "江家宝", "张禹舜", "韩方圆", "李爽", "李随星", "印象"};
    boolean notify[] = {true, false};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mystudent, null);
        grid_student = view.findViewById(R.id.grid_student);
        initData();
        initView();
        return view;
    }

    private void initView() {
        studentListAdapter = new StudentListAdapter(getActivity(), name, notify);
        grid_student.setAdapter(studentListAdapter);
        grid_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent Backintent=new Intent(getActivity(),ChatActivity.class);
                startActivityForResult(Backintent,1);
            }
    });
    }

    @OnClick(R.id.iv_message2)
    public void message() {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        startActivity(intent);
    }

    void initData() {
    }

    public void UpdateData() {
        notify[0] = false;
        studentListAdapter = new StudentListAdapter(getActivity(), name, notify);
        grid_student.setAdapter(studentListAdapter);
        studentListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                UpdateData();
                break;
            default:
        }
    }


}
