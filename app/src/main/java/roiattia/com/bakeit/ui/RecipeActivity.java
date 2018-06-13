package roiattia.com.bakeit.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
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


import java.util.List;

import roiattia.com.bakeit.R;
import roiattia.com.bakeit.models.Ingredient;
import roiattia.com.bakeit.widget.RecipeWidgetProvider;
import roiattia.com.bakeit.adapters.StepsAdapter;
import roiattia.com.bakeit.models.Recipe;
import roiattia.com.bakeit.utils.TextFontUtils;

public class RecipeActivity extends AppCompatActivity
        implements StepsAdapter.StepAdapterOnClickHandler {

    public static final String TAG = RecipeActivity.class.getSimpleName();
    public static final String STEP_VIDEO = "STEP_VIDEO";
    private static final String VIDEO_FRAGMENT = "VIDEO_FRAGMENT";
    private static final String IMAGE_FRAGMENT = "IMAGE_FRAGMENT";
    public static final String IMAGE = "IMAGE";
    public static final String VIDEO = "VIDEO";
    public static final String MULTIMEDIA = "MULTIMEDIA";

    private View mLastStep;
    private Recipe mRecipe;
    private boolean mTwoPane;
    private VideoFragment mVideoFragment;
    private ImageFragment mImageFragment;
    private FragmentManager mFragmentManager;

    public static List<Ingredient> mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // get intent from calling activity
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

        mFragmentManager = getSupportFragmentManager();
        // create new fragments if there are no fragment saved in savedInstanceState
        if (savedInstanceState == null) {
            // create FragmentManager and RecipeViewPagerFragment for Smartphone
            // and Tablet modes
            RecipeViewPagerFragment viewPagerFragment = new RecipeViewPagerFragment();
            mVideoFragment = new VideoFragment();
            mImageFragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipesListActivity.RECIPE_ITEM, mRecipe);
            viewPagerFragment.setArguments(bundle);
            mFragmentManager.beginTransaction()
                    .add(R.id.viewpager_container, viewPagerFragment)
                    .commit();
        }
        // get videoFragment due to it being a member variable and so will
        // get created on screen rotation and will be null even if savedInstanceState is not null
        else {
            if(mVideoFragment != null) {
                mVideoFragment = (VideoFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, IMAGE_FRAGMENT);
            } else mVideoFragment = new VideoFragment();
            if(mImageFragment != null) {
                mImageFragment = (ImageFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, VIDEO_FRAGMENT);
            } else mImageFragment = new ImageFragment();
        }
    }

    @Override
    public void onStepClick(int stepIndex, View stepCard) {
        // check if there is a video for this step
        if (!mRecipe.steps().get(stepIndex).videoUrl().equals("")) {
            loadVideo(stepIndex, stepCard);
        } else if (!mRecipe.steps().get(stepIndex).thumbnailUrl().equals("")){
            loadImage(stepIndex, stepCard);
        } else {
            Toast.makeText(this, R.string.video_not_available_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage(int stepIndex, View stepCard) {
        // Smartphone mode
        if (!mTwoPane) {
            openVideoActivity(stepIndex, IMAGE);
        }
        // Tablet mode
        else {
            mImageFragment.setThumbnailUrl(mRecipe.steps().get(stepIndex).thumbnailUrl());
            mFragmentManager.beginTransaction()
                    .replace(R.id.video_container, mImageFragment)
                    .commit();
            checkStepcardColor(stepCard);
        }
    }

    private void loadVideo(int stepIndex, View stepCard) {
        // Smartphone mode
        if (!mTwoPane) {
            openVideoActivity(stepIndex, VIDEO);
        }
        // Tablet mode
        else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.video_container, mVideoFragment)
                    .commit();
            mVideoFragment.releasePlayer();
            mVideoFragment.setVideoUrl(mRecipe.steps().get(stepIndex).videoUrl());
            checkStepcardColor(stepCard);
        }
        // video not exist for this step
    }

    /**
     * Handles intent creating and navigation to VideoActivity
     * @param stepIndex the index of the step
     * @param multimedia the type of the step: video or image
     */
    private void openVideoActivity(int stepIndex, String multimedia) {
        Intent intent = new Intent(RecipeActivity.this, VideoActivity.class);
        switch (multimedia){
            case VIDEO:
                intent.putExtra(STEP_VIDEO, mRecipe.steps().get(stepIndex).videoUrl());
                intent.putExtra(MULTIMEDIA, VIDEO);
                break;

            case IMAGE:
                intent.putExtra(STEP_VIDEO, mRecipe.steps().get(stepIndex).thumbnailUrl());
                intent.putExtra(MULTIMEDIA, IMAGE);
                break;
        }
        intent.putExtra(RecipesListActivity.RECIPE_ITEM, mRecipe.name());
        startActivity(intent);
    }

    private void checkStepcardColor(View stepCard) {
        // check if there is a step stored
        if(null != mLastStep) {
            // get the cardview of the step
            CardView cardView = mLastStep.findViewById(R.id.cv_step);
            // set the step's cardview background color back to white
            cardView.setCardBackgroundColor(Color.WHITE);
        }
        CardView cardView = stepCard.findViewById(R.id.cv_step);
        // set the step's cardview background color to selected color
        cardView.setCardBackgroundColor(getResources()
                .getColor(R.color.colorSelectedStepCardviewBackground));
        // save the step in mLastStep to set it color back to white
        // when other step is selected
        mLastStep = stepCard;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_add_widget){
            mIngredients = mRecipe.ingredients();
            Toast.makeText(this, R.string.widget_added_toast, Toast.LENGTH_SHORT).show();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
            //Trigger data update to force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_text);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_listview);
            //Now update all widgets
            RecipeWidgetProvider.updateIngredientWidgets(this, appWidgetManager, mRecipe, appWidgetIds);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(getResources().getBoolean(R.bool.is_tablet)) {
            if(mVideoFragment.isAdded()) {
                getSupportFragmentManager().putFragment(outState, VIDEO_FRAGMENT, mVideoFragment);
            }
            if(mImageFragment.isAdded()) {
                getSupportFragmentManager().putFragment(outState, IMAGE_FRAGMENT, mImageFragment);
            }
        }
    }
}
