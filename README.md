# AutoPlayRecyclerView
标签分组自动横向滚动，并且可手动滑动

## 用法
```
 final List<String> strings = new ArrayList<>();
 strings.add("标签1");
 strings.add("标签2");
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
```

## 效果
![图片效果](/temp/photo.jpg)
(https://github.com/lwanghaol/MultiAutoPlayRecyclerView/blob/master/temp/video.mp4)