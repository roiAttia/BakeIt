package roiattia.com.bakeit.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.bakeit.R;
import roiattia.com.bakeit.adapters.IngredientsAdpater;
import roiattia.com.bakeit.models.Ingredient;

public class IngredientsListFragment extends Fragment {

    private static final String INGREDIENT = "ingredient";
    private static final String SERVINGS = "SERVINGS";

    public IngredientsListFragment(){ }

    List<Ingredient> mIngredientList;
    int mServings;
    @BindView(R.id.tv_servings) TextView mServingsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
        ButterKnife.bind(this, rootView);

        if(savedInstanceState != null){
            mIngredientList = savedInstanceState.getParcelableArrayList(INGREDIENT);
            mServings = savedInstanceState.getInt(SERVINGS);
        }

        // set the servings of the recipe
        mServingsTextView.setText(String.format("%s %s", getString(R.string.servings), String.valueOf(mServings)));

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_ingredients);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        IngredientsAdpater ingredientsAdpater = new IngredientsAdpater(getContext());
        ingredientsAdpater.setIngredients(mIngredientList);
        recyclerView.setAdapter(ingredientsAdpater);

        return rootView;
    }

    public void setDetails(List<Ingredient> ingredients, int servings){
        mIngredientList = ingredients;
        mServings = servings;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(INGREDIENT, (ArrayList<? extends Parcelable>) mIngredientList);
        outState.putInt(SERVINGS, mServings);
    }
}
