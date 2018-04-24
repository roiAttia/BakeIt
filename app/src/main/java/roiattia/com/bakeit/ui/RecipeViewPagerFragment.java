package roiattia.com.bakeit.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import roiattia.com.bakeit.R;
import roiattia.com.bakeit.models.Recipe;

public class RecipeViewPagerFragment extends Fragment {

    public static final String INGREDIENTS = "INGREDIENTS";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_viewpager, container, false);

        // get recipe from bundle
        Recipe recipe = getArguments().getParcelable(RecipesListActivity.RECIPE_ITEM);

        if (null != recipe) {
            // initialize IngredientsListFragment and StepsListFragment
            final IngredientsListFragment ingredientsListFragment = new IngredientsListFragment();
            ingredientsListFragment.setDetails(recipe.ingredients(), recipe.servings());
            final StepsListFragment stepsListFragment = new StepsListFragment();
            stepsListFragment.setStepList(recipe.steps());
            ViewPager viewPager = rootView.findViewById(R.id.viewPager);
            viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return position == 0 ? ingredientsListFragment : stepsListFragment;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return position == 0 ? getString(R.string.ingredients_headline) : getString(R.string.stepsfragment_headline);
                }

                @Override
                public int getCount() {
                    return 2;
                }
            });

            TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);
            TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
            tabOne.setText(getString(R.string.ingredients_headline));
            tabOne.setTextColor(getResources().getColor(R.color.colorTabSelected));
            tabLayout.getTabAt(0).setCustomView(tabOne);
            TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
            tabTwo.setText(getString(R.string.stepsfragment_headline));
            tabTwo.setTextColor(getResources().getColor(R.color.colorTabUnselected));
            tabLayout.getTabAt(1).setCustomView(tabTwo);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View view = tab.getCustomView();
                    TextView textView = view.findViewById(R.id.tab);
                    textView.setTextColor(getResources().getColor(R.color.colorTabSelected));
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View view = tab.getCustomView();
                    TextView textView = view.findViewById(R.id.tab);
                    textView.setTextColor(getResources().getColor(R.color.colorTabUnselected));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });
        }

        return rootView;
    }

}
