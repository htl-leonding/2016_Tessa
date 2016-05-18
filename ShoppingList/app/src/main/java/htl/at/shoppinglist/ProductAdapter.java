package htl.at.shoppinglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
                    removeAt(getPosition());
                }
            });
        }


    }

    public void removeAt(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
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



    @Override
    public int getItemCount() {
        return productList.size();
    }
}
