package com.wh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wh.autoplay.AutoPlayAdapter;
import com.wh.autoplay.MultiAutoPlayRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> strings = new ArrayList<>();
        strings.add("标签1");
        strings.add("标签2");
        strings.add("标签标签3");
        strings.add("标签4");
        strings.add("标签标签标签标签5");
        strings.add("标签标签6");
        strings.add("标签7");
        strings.add("标签标签标签8");

        strings.add("标签标签标签9");
        strings.add("标签标签标签10");
        strings.add("标签标签标签11");
        strings.add("标签标签标签12");
        strings.add("标签标签标签13");
        strings.add("标签标签标签14");
        strings.add("标签标签标签15");
        strings.add("标签标签标签16");
        strings.add("标签标签标签17");
        strings.add("标签标签标签18");
        strings.add("标签标签标签19");
        strings.add("标签标签标签20");
        strings.add("标签标签标签21");
        strings.add("标签标签标签22");
        strings.add("标签标签标签23");
        strings.add("标签标签标签24");
        strings.add("标签标签标签25");
        strings.add("标签标签标签26");
        strings.add("标签标签标签27");
        strings.add("标签标签标签28");
        strings.add("标签标签标签29");
        strings.add("标签标签标签30");

        strings.add("标签标签标签31");
        strings.add("标签标签标签32");
        strings.add("标签标签标签33");
        strings.add("标签标签标签34");


        final MultiAutoPlayRecyclerView recyclerView = (MultiAutoPlayRecyclerView) findViewById(R.id.recycler);
        recyclerView.setData(strings, 4);
        recyclerView.setOnAutoPlayClickListener(new AutoPlayAdapter.OnAutoPlayClickListener() {
            @Override
            public void onClick(View view, int position, String data) {

                strings.set(position, "选中标签" + (position + 1));//更改数据
                recyclerView.notifyDataSetChanged();//刷新ui
                Toast.makeText(view.getContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
