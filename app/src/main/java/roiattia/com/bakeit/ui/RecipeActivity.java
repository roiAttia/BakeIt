package roiattia.com.bakeit.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import roiattia.com.bakeit.R;
import roiattia.com.bakeit.RecipeWidgetProvider;
import roiattia.com.bakeit.adapters.StepsAdapter;
import roiattia.com.bakeit.models.Recipe;
import roiattia.com.bakeit.utils.TextFontUtils;

public class RecipeActivity extends AppCompatActivity
        implements StepsAdapter.StepAdapterOnClickHandler {

    public static final String TAG = RecipeActivity.class.getSimpleName();
    public static final String STEP = "STEP";
    private static final String VIDEO_FRAGMENT = "VIDEO_FRAGMENT";

    private View mLastStep;
    private Recipe mRecipe;
    private boolean mTwoPane;
    private VideoFragment mVideoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        /* get intent from calling activity */
        Bundle data = getIntent().getExtras();

        // Check to see if it's a Tablet layout and set mTwoPane accordingly
        mTwoPane = getResources().getBoolean(R.bool.is_tablet);

        if (data != null) {
            mRecipe = data.getParcelable(RecipesListActivity.RECIPE_ITEM);
            // set the toolbar's title, font and add back button
            TextView toolbarTextview = TextFontUtils.setTextFont(this, mRecipe.name());
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |
                    ActionBar.DISPLAY_HOME_AS_UP);
            getSupportActionBar().setCustomView(toolbarTextview);
        }

        // create new fragments when there is no previously saved state
        if (savedInstanceState == null) {
            // create FragmentManager and RecipeViewPagerFragment for Smartphone
            // and Tablet modes
            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeViewPagerFragment viewPagerFragment = new RecipeViewPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipesListActivity.RECIPE_ITEM, mRecipe);
            viewPagerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.viewpager_container, viewPagerFragment)
                    .commit();

            //if it's a Tablet view then add mVideoFragment
            if (mTwoPane) {
                mVideoFragment = new VideoFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.video_container, mVideoFragment)
                        .commit();
            }
        } else {
            mVideoFragment = (VideoFragment) getSupportFragmentManager().getFragment(savedInstanceState, VIDEO_FRAGMENT);
        }
    }

    @Override
    public void onClick(int stepIndex, View step) {
        // check if there is a video for this step
        if (!mRecipe.steps().get(stepIndex).videoUrl().equals("")) {
            // Smartphone mode
            if (!mTwoPane) {
                Intent intent = new Intent(RecipeActivity.this, VideoActivity.class);
                intent.putExtra(STEP, mRecipe.steps().get(stepIndex).videoUrl());
                intent.putExtra(RecipesListActivity.RECIPE_ITEM, mRecipe.name());
                startActivity(intent);
                // Tablet mode
            } else {
                mVideoFragment.releasePlayer();
                mVideoFragment.setVideoUrl(mRecipe.steps().get(stepIndex).videoUrl());
                // check if there is a step stored
                if(null != mLastStep) {
                    // get the cardview of the step
                    CardView cardView = mLastStep.findViewById(R.id.cv_step);
                    // set the step's cardview background color back to white
                    cardView.setCardBackgroundColor(Color.WHITE);
                }
                CardView cardView = step.findViewById(R.id.cv_step);
                // set the step's cardview background color to selected color
                cardView.setCardBackgroundColor(getResources()
                        .getColor(R.color.colorSelectedStepCardviewBackground));
                // save the step in mLastStep to set it color back to white
                // when other step is selected
                mLastStep = step;
            }
            // video not exist for this step
        } else{
            Toast.makeText(this, R.string.video_not_available_toast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_add_widget){
            Toast.makeText(this, R.string.widget_added_toast, Toast.LENGTH_SHORT).show();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
            //Trigger data update to handle the GridView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_text);
            //Now update all widgets
            RecipeWidgetProvider.updateRecipeWidget(this, appWidgetManager, mRecipe, appWidgetIds);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(getResources().getBoolean(R.bool.is_tablet)) {
            getSupportFragmentManager().putFragment(outState, VIDEO_FRAGMENT, mVideoFragment);
        }
    }
}
