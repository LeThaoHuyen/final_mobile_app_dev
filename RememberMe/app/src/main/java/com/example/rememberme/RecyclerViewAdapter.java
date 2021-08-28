package com.example.rememberme;

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<FragmentItem> ItemList;

    public RecyclerViewAdapter(List<FragmentItem> itemList, Context context) {
        ItemList = itemList;
        this.context = context;
    }

    Context context;
    public RecyclerViewAdapter(List<FragmentItem> itemList) {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemName.setText(ItemList.get(position).getName());
        holder.itemExpDate.setText(ItemList.get(position).getDate());
        Glide.with(this.context).load(ItemList.get(position).getImageURL()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemName;
        TextView itemExpDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.product_view);
            itemName = itemView.findViewById(R.id.product_name);
            itemExpDate = itemView.findViewById(R.id.expiry_date);

        }
    }
}