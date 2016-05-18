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

import java.util.List;

/**
 * Created by pautzi on 15.05.16.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<Product> productList;
    public List<Product> getProductList() {
        return productList;
    }

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
                    removeItem(getPosition());
                }
            });

        }


    }

    public Product removeItem(int position) {
        Product product = productList.get(position);
        productList.remove(position);
        notifyItemRemoved(position);
        return product;
    }

    public void addItem(int position, Product product) {
        productList.add(position, product);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Product product = productList.remove(fromPosition);
        productList.add(toPosition, product);
        notifyItemMoved(fromPosition, toPosition);
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getTitle());
        holder.pieces.setText(product.getPieces());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
