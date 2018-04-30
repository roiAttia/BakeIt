package roiattia.com.bakeit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.bakeit.R;
import roiattia.com.bakeit.models.Recipe;

/**
 * Created by roiat on 21-03-18.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipes;
    private final RecipesAdapterOnClickHandler mClickHandler;


    public RecipesAdapter(Context context, RecipesAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public interface RecipesAdapterOnClickHandler{
        void onClick(int recipeIndex);
    }

    @NonNull
    @Override
    public RecipesAdapter.RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item_recipe;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.RecipesAdapterViewHolder holder, int position) {
        if(null != mRecipes.get(position)) {
            // set the name of the recipe
            holder.mName.setText(mRecipes.get(position).name());

            // load recipe image if available
            if(!mRecipes.get(position).image().equals("")){
                Glide.with(mContext).load(mRecipes.get(position).image()).into(holder.mRecipeImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.tv_recipe_name) TextView mName;
        @BindView(R.id.cv_recipe) CardView mRecipeCard;
        @BindView(R.id.iv_recipe) ImageView mRecipeImage;

        RecipesAdapterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    /**
     * Sets the recipesData to mRecipes
     * @param recipesData the recipes list to set
     */
    public void setRecipesData(List<Recipe> recipesData) {
        mRecipes = recipesData;
        notifyDataSetChanged();
    }
}
