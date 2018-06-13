package roiattia.com.bakeit.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roiattia.com.bakeit.IdlingResource.SimpleIdlingResource;
import roiattia.com.bakeit.R;
import roiattia.com.bakeit.adapters.RecipesAdapter;
import roiattia.com.bakeit.models.Recipe;
import roiattia.com.bakeit.rest.ApiClient;
import roiattia.com.bakeit.rest.ApiInterface;
import roiattia.com.bakeit.utils.TextFontUtils;

public class RecipesListActivity extends AppCompatActivity
    implements RecipesAdapter.RecipesAdapterOnClickHandler{

    private static final String TAG = RecipesListActivity.class.getSimpleName();
    public static final String RECIPE_ITEM = "RECIPE_ITEM";
    private List<Recipe> mRecipes;

    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        ButterKnife.bind(this);

        // set the toolbar title and font
        TextView toolbarTextview = TextFontUtils.setTextFont(this, getString(R.string.app_name));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(toolbarTextview);

        mRecipes = new ArrayList<>();

        // Make api request with retrofit
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // set mIdlingResource idle state to false to indicate recipes not loaded yet
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        // Get recipes list from json file
        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mRecipes.addAll(response.body());

                // check if there are recipes to show
                if(mRecipes.size() > 0){
                    initRecipesList();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(RecipesListActivity.this, "There was a problem showing recipes. please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method initialize the recyclerview with the recipes fetched
     * from the json file
     */
    private void initRecipesList() {
        RecipesAdapter mRecipesAdapter = new RecipesAdapter(RecipesListActivity.this, RecipesListActivity.this);
        mRecipesAdapter.setRecipesData(mRecipes);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                    getResources().getInteger(R.integer.number_of_columns),
                    LinearLayoutManager.VERTICAL, false);
        mRecipesRecyclerView.setLayoutManager(gridLayoutManager);
        mRecipesRecyclerView.setHasFixedSize(true);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);

        // set mIdlingResource to idle to indicate recipes loaded to screen
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    /**
     * This method handles recipe onStepClick method from the RecipesAdapter
     * @param recipeIndex the index of the selected recipe
     */
    @Override
    public void onClick(int recipeIndex) {
        Intent intent = new Intent(RecipesListActivity.this, RecipeActivity.class);
        // add the selected recipe to the intent
        intent.putExtra(RECIPE_ITEM, mRecipes.get(recipeIndex));
        startActivity(intent);
    }
}
