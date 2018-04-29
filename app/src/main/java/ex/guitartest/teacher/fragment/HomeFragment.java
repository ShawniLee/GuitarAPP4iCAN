package ex.guitartest.teacher.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.R;
import ex.guitartest.teacher.MessageActivity;
import ex.guitartest.teacher.WorksAdapter;

/**
 * Created by mcp1993 on 2018/4/6 0006.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.list_works)
    ListView listWorks;
    private WorksAdapter worksAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        ButterKnife.bind(this,view);
        initList();
        return view;
    }
    private void initList(){
        worksAdapter = new WorksAdapter(getActivity());
        listWorks.setAdapter(worksAdapter);
    }

    @OnClick(R.id.iv_message)
    public void message(){
        Intent intent =  new Intent(getActivity(),MessageActivity.class);
        startActivity(intent);
    }
}
