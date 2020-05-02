package com.example.instashop;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instashop.Database.Database;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder>
{
    private List<Product> listData = new ArrayList<>();
    private int count = 1;
    private Context context;

    public CartListAdapter(List<Product> listData, Context context)
    {
        this.listData = listData;
        this.context = context;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType)
    {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getCost()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.total.setText(fmt.format(price));
        holder.name.setText(listData.get(position).getName());
        holder.cost.setText(listData.get(position).getCost());
        holder.quantity.setText(listData.get(position).getQuantity());
        holder.cart_plus_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(count > 0)
                {
                    count++;
                    holder.quantity.setText(String.valueOf(count));
                    Product p = listData.get(position);
                    p.setQuantity(String.valueOf(count));
                    new Database(context).updateCart(p);
                    notifyDataSetChanged();
                }
            }
        });
        holder.cart_minus_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(count > 1)
                {
                    count--;
                    holder.quantity.setText(String.valueOf(count));
                    Product p = listData.get(position);
                    p.setQuantity(String.valueOf(count));
                    new Database(context).updateCart(p);
                    notifyDataSetChanged();
                }
            }
        });
        /*
        holder.img_deleteitem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listData.remove(position);
                notifyDataSetChanged();
            }
        });
        */
         Picasso.get().load(listData.get(position).getImage()).into(holder.ProductPic);
    }

    @Override
    public int getItemCount()
    {
        return listData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
    {
        TextView name, cost, quantity, total;
        ImageView ProductPic;
        ImageView cart_minus_img, cart_plus_img, img_deleteitem;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            cost = (TextView) itemView.findViewById(R.id.tv_cost);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            total = (TextView) itemView.findViewById(R.id.tv_total);
            ProductPic = (ImageView) itemView.findViewById(R.id.tv_image);
            cart_minus_img=(ImageView) itemView.findViewById(R.id.cart_icon_minus);
            cart_plus_img=(ImageView) itemView.findViewById(R.id.cart_icon_plus);
            img_deleteitem=(ImageView) itemView.findViewById(R.id.cart_icon_trash);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Toast.makeText(context," is clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            menu.setHeaderTitle("Select Action");
            menu.add(0,0, getAdapterPosition(), "DELETE");
            Toast.makeText(context,"CONTEXT MENU is clicked", Toast.LENGTH_SHORT).show();
        }
    }
}

