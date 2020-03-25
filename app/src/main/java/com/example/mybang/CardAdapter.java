package com.example.mybang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Card> mCardList;

    public CardAdapter(List<Card> cardList){
        mCardList = cardList;
    }
    // 定义内部类 ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        View cardView; //为点击事件
        //ImageView fruitImage;
        TextView cardNameView;

        // ViewHolder的构造函数 传入View参数 view通常是Recycler子项的最外层布局
        public ViewHolder(View view){
            super(view);
            cardView = view;
            // fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            cardNameView = (TextView) view.findViewById(R.id.fruit_name);
        }
    }


    // 重写onCreateViewHolder() 、onBindViewHolder()、getItemCount()
    // 创建ViewHolder对象的函数
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        //为点击事件改变***********
        // 给子项里面的 整体和图片 分别都注册点击事件
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ // 点击显示提醒弹窗
                int position = holder.getAdapterPosition();
                Card card = mCardList.get(position);
                Toast.makeText(v.getContext(),"you clicked view "+card.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        /*
        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext(),"you clicked image "+fruit.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

         */
        //为点击事件改变***********
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Card card = mCardList.get(position);
        // holder.fruitImage.setImageResource(fruit.getImageId());
        holder.cardNameView.setText(card.getName());
    }

    @Override
    public int getItemCount(){
        return mCardList.size();
    }

}
