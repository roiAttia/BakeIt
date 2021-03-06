package roiattia.com.bakeit.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import roiattia.com.bakeit.R;
import roiattia.com.bakeit.utils.TextFontUtils;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // get intent and extra bundle
        String recipeName = getIntent().getStringExtra(RecipesListActivity.RECIPE_ITEM);
        String multimedia = getIntent().getStringExtra(RecipeActivity.MULTIMEDIA);

        if(null != recipeName){
            // set the toolbar's title, font and add back button
            TextView toolbarTextview = TextFontUtils.setTextFont(this, recipeName);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |
                    ActionBar.DISPLAY_HOME_AS_UP);
            getSupportActionBar().setCustomView(toolbarTextview);
        }


        // only create StepFragment if there is none exist
        if (savedInstanceState == null) {
            String multimediaUrl = getIntent().getStringExtra(RecipeActivity.STEP_VIDEO);
            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (multimedia) {
                case RecipeActivity.VIDEO:
                    VideoFragment videoFragment = new VideoFragment();
                    videoFragment.setVideoUrl(multimediaUrl);
                    fragmentManager.beginTransaction()
                            .add(R.id.video_container, videoFragment)
                            .commit();
                    break;

                case RecipeActivity.IMAGE:
                    ImageFragment imageFragment = new ImageFragment();
                    imageFragment.setThumbnailUrl(multimediaUrl);
                    fragmentManager.beginTransaction()
                            .add(R.id.video_container, imageFragment)
                            .commit();
                    break;
            }
        }
    }
}
