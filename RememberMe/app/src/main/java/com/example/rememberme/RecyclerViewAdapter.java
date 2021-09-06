package com.example.rememberme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.example.rememberme.Models.Product;
import com.example.rememberme.activities.EditProductActivity;
import com.example.rememberme.activities.HomeActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<Product> productList;
    Context context;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    public RecyclerViewAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }
    final SingletonClass singletonClass = SingletonClass.getInstance();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
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
}*/

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List <Product> productList;
    final SingletonClass singletonClass = SingletonClass.getInstance();
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    public RecyclerViewAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    Context context;
    public RecyclerViewAdapter(List<Product> productList) {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        MyViewHolder holder=new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemName.setText(productList.get(position).getName());
        holder.itemExpDate.setText(productList.get(position).getTimeLeft());
        Glide.with(this.context).load(productList.get(position).getImageURL()).into(holder.itemImage);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick) {
                    singletonClass.setCurrentProduct(productList.get(position));
                    //Toast.makeText(context, "Long Click: " + productList.get(position), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("You want to delete " + singletonClass.getCurrentProduct().getName() + "?");
                    builder.setMessage("This cannot be undone");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAnObject();
                                    Intent intent =  new Intent(context, HomeActivity.class);
                                    context.startActivity(intent);
                                }
                            });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

                else {
                    singletonClass.setCurrentProduct(productList.get(position));
                    //Toast.makeText(context, "Short Click: " + productList.get(position), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (context, EditProductActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    private void deleteAnObject() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Products");
        reference.child(singletonClass.getUserID()).child(String.valueOf(singletonClass.getProductID())).removeValue();
        /*Product c1 = new Product(1, "G-Dragon", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1", "1");

        Product c2 = new Product(2, "Daesung", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1", "1");
        Product c4 = new Product(4, "TOP", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1", "1");
        Product c5 = new Product(5, "Seungri", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1", "1");
        Product c6 = new Product(6, "Lisa", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg", "1");
        Product c7 = new Product(7, "Rose", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg", "1");
        Product c8 = new Product(8, "Jennie", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg", "1");
        Product c9 = new Product(9, "Jisoo", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg", "1");
        reference.child(singletonClass.getUserID()).child(String.valueOf(c1.getId())).setValue(c1);
        reference.child(singletonClass.getUserID()).child(String.valueOf(c2.getId())).setValue(c2);
        reference.child(singletonClass.getUserID()).child(String.valueOf(c4.getId())).setValue(c4);
        reference.child(singletonClass.getUserID()).child(String.valueOf(c5.getId())).setValue(c5);
        reference.child(singletonClass.getUserID()).child(String.valueOf(c6.getId())).setValue(c6);
        reference.child(singletonClass.getUserID()).child(String.valueOf(c7.getId())).setValue(c7);
        reference.child(singletonClass.getUserID()).child(String.valueOf(c8.getId())).setValue(c8);
        reference.child(singletonClass.getUserID()).child(String.valueOf(c9.getId())).setValue(c9);*/

        //Product x = new Product(4, "TOP", "10/2/2000", "URLSave", "seriSave");
        //reference.child(singletonClass.getUserID()).child(String.valueOf(x.getId())).setValue(x);

        singletonClass.removeItem(singletonClass.getCurrentProduct());
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,View.OnLongClickListener{
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
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }

}