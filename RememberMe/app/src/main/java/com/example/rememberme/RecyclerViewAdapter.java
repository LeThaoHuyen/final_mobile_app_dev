package com.example.rememberme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.example.rememberme.Models.Product;
import com.example.rememberme.activities.EditProductActivity;
import com.example.rememberme.activities.HomeActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<Product> productList;
    Context context;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    public RecyclerViewAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }
    final SingletonClass singletonClass = SingletonClass.getInstance();

    public RecyclerViewAdapter(List<Product> productList) {
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
        holder.itemName.setText(productList.get(position).getName());
        holder.itemExpDate.setText(productList.get(position).getDate());
        Glide.with(this.context).load(productList.get(position).getImageURL()).into(holder.itemImage);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick) {
                    Toast.makeText(context, "Long Click: " + productList.get(position), Toast.LENGTH_SHORT).show();
                    singletonClass.setCurrentProduct(productList.get(position));
                    deleteAnObject();
                    Intent intent =  new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                }

                else {
                    singletonClass.setCurrentProduct(productList.get(position));
                    Intent intent =  new Intent(context, EditProductActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    private void deleteAnObject() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("products");
        int id = singletonClass.getProductID();

        //remove that item in database
        reference.child(singletonClass.getUserID()).child(String.valueOf(id)).removeValue();
        singletonClass.getProductList().remove(singletonClass.getCurrentProduct());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemName;
        TextView itemExpDate;

        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.product_image);
            itemName = itemView.findViewById(R.id.product_name);
            itemExpDate = itemView.findViewById(R.id.expiry_date);

            itemView.setOnClickListener((View.OnClickListener) this);
            itemView.setOnLongClickListener((View.OnLongClickListener) this);
        }

        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
    }
}

interface ItemClickListener {
    void onClick(View view, int position,boolean isLongClick);
}
