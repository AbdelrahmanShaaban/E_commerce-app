package com.example.onlineshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopping.R;
import com.example.onlineshopping.model.ProductData;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.myViewHolder> implements Filterable {
    ArrayList<ProductData> productData;
    Context cont;
    private ItemOnClickListener listener;
    ArrayList<ProductData> filterProductData;

    public AllProductAdapter(ArrayList<ProductData> productData, Context cont, ItemOnClickListener listener) {
        this.productData = productData;
        this.cont = cont;
        this.filterProductData = productData ;
        this.listener = listener;
    }

    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position) {
        holder.name.setText(filterProductData.get(position).getName());
        holder.category.setText(filterProductData.get(position).getCategory());
        holder.color.setText(filterProductData.get(position).getColor());
        holder.price.setText(String.valueOf(filterProductData.get(position).getPrice()));
        Picasso.get().load(filterProductData.get(position).getImageId()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return filterProductData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String key = charSequence.toString();
                if (key.isEmpty())
                {
                    filterProductData = productData ;
                }else{

                    ArrayList<ProductData> filteredList = new ArrayList<>();
                    for (ProductData data : productData)
                    {
                        if (data.getName().toLowerCase().contains(key.toLowerCase()))
                        {
                            filteredList.add(data);
                        }
                        if (data.getBarcode().toLowerCase().contains(key.toLowerCase()))
                        {
                            filteredList.add(data);
                        }
                    }
                    filterProductData = filteredList ;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterProductData ;
                return filterResults ;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                filterProductData = (ArrayList<ProductData>)results.values ;
                notifyDataSetChanged();
            }
        };
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView name, category, color, price;
        ImageView imageView;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameOfProduct);
            category = itemView.findViewById(R.id.categoryOfProduct);
            color = itemView.findViewById(R.id.colorOfProduct);
            price = itemView.findViewById(R.id.priceOfProduct);
            imageView = itemView.findViewById(R.id.imageOfProduct);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null) {
                        int position = getAdapterPosition();
                        String name = filterProductData.get(position).getName();
                        for (int i = 0; i < productData.size(); i++) {
                            if (name.equals(productData.get(i).getName())) {
                                position = i;
                                break;
                            }
                        }
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public interface ItemOnClickListener {
        void onItemClick(int position);
    }
}
