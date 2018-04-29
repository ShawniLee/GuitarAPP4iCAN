package ex.guitartest.teacher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import butterknife.OnClick;
import ex.guitartest.R;
import ex.guitartest.teacher.MessageActivity;
import ex.guitartest.teacher.StudentListAdapter;

/**
 *
 * Created by mcp1993 on 2018/3/8 1008.
 */

public class MyStudentFragment extends Fragment {
    private GridView grid_student;
    private StudentListAdapter studentListAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mystudent,null);
        grid_student = view.findViewById(R.id.grid_student);
        initView();
        return view;
    }
    private void initView(){
        studentListAdapter = new StudentListAdapter(getActivity());
        grid_student.setAdapter(studentListAdapter);
    }
    @OnClick(R.id.iv_message)
    public void message(){
        Intent intent =  new Intent(getActivity(),MessageActivity.class);
        startActivity(intent);
    }
}
