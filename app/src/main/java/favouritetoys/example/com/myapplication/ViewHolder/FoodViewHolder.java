package favouritetoys.example.com.myapplication.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import favouritetoys.example.com.myapplication.Interface.ItemClickListener;
import favouritetoys.example.com.myapplication.R;

/**
 * Created by Divya Gupta on 11-04-2018.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView food_name;
    public ImageView food_image, fav_image;
    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_name = (TextView) itemView.findViewById(R.id.food_name);
        food_image = (ImageView) itemView.findViewById(R.id.food_image);
        fav_image = (ImageView) itemView.findViewById(R.id.fav);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}
