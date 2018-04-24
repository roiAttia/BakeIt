package roiattia.com.bakeit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.bakeit.R;
import roiattia.com.bakeit.models.Ingredient;

public class IngredientsAdpater extends
        RecyclerView.Adapter<IngredientsAdpater.IngredientsAdpaterViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredients;

    public IngredientsAdpater(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public IngredientsAdpaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item_ingredient;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new IngredientsAdpater.IngredientsAdpaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdpaterViewHolder holder, int position) {
        if(null != mIngredients.get(position)) {
            // set the format for the quantity of the ingredient, make it
            // decimal only if needed
            NumberFormat nf = new DecimalFormat("##.###");
            double quantity = mIngredients.get(position).quantity();
            // set the ingredient name, quantity and measurement and make it lower case
            holder.ingredient.setText(String.format("%s %s %s", nf.format(quantity),
                    mIngredients.get(position).measure().toLowerCase(),
                    mIngredients.get(position).name().toLowerCase()));
        }
    }

    @Override
    public int getItemCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    public class IngredientsAdpaterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient) TextView ingredient;

        IngredientsAdpaterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * this method sets the ingredients list to use
     *@param ingredients the ingredients list for the adapter
     **/
    public void setIngredients(List<Ingredient> ingredients){
        mIngredients = ingredients;
    }
}
