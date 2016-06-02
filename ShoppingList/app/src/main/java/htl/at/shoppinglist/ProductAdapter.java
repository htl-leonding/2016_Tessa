package htl.at.shoppinglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pautzi on 15.05.16.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,pieces;
        public ImageButton delete_button;
        public RecyclerView recyclerView;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            pieces = (TextView) view.findViewById(R.id.pieces);
            delete_button = (ImageButton) view.findViewById(R.id.delete_btn);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });

        }
    }

    private List<Product> productList;
    private List<Product> filteredList;
    private String filter = "";

    public ProductAdapter() {
        this.productList = new LinkedList<>();
        this.filteredList = new LinkedList<>();
    }

    //Todo:Delete from DB
    public Product removeItem(int position) {
        Product product = filteredList.remove(position);
        productList.remove(product);
        notifyItemRemoved(position);
        return product;
    }

    public void addItem(Product product){
        productList.add(product);
        updateFilteredList();
        notifyItemInserted(productList.size() - 1);
    }

    //Todo:Add to DB
    public void addItem(int position, Product product) {
        productList.add(position, product);
        updateFilteredList();
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Product product = productList.remove(fromPosition);
        productList.add(toPosition, product);
        updateFilteredList();
        notifyItemMoved(fromPosition, toPosition);
    }

    public Product getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(filteredList.size() > position) {
            holder.itemView.setVisibility(View.VISIBLE);
            Product product = filteredList.get(position);
            holder.title.setText(product.getTitle());
            holder.pieces.setText(product.getPieces());
        } else {
            holder.itemView.setVisibility(View.GONE);
            if (holder.itemView.getParent() instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) holder.itemView.getParent();
                System.out.println();;
            }
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setFilter(String filter) {
        this.filter = filter;
        updateFilteredList();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void updateFilteredList() {
        filteredList.clear();
        for (Product product : productList) {
            final String title = product.getTitle().toLowerCase();
            if (title.contains(filter.toLowerCase())) {
                filteredList.add(product);
            }
        }
    }
}
