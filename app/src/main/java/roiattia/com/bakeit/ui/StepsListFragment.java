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

import java.util.ArrayList;
import java.util.List;

import roiattia.com.bakeit.R;
import roiattia.com.bakeit.adapters.StepsAdapter;
import roiattia.com.bakeit.models.Step;

public class StepsListFragment extends Fragment {

    private static final String STEP = "step";

    public StepsListFragment(){
    }

    List<Step> mStepList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);

        if(savedInstanceState != null){
            mStepList = savedInstanceState.getParcelableArrayList(STEP);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView mRecyclerView = rootView.findViewById(R.id.rv_steps);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        StepsAdapter stepsAdapter = new StepsAdapter(getContext(), (StepsAdapter.StepAdapterOnClickHandler) getActivity());
        stepsAdapter.setStepList(mStepList);
        mRecyclerView.setAdapter(stepsAdapter);

        return rootView;
    }

    public void setStepList(List<Step> stepList){
        mStepList = stepList;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP, (ArrayList<? extends Parcelable>) mStepList);
    }
}
