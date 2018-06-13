package roiattia.com.bakeit.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import roiattia.com.bakeit.R;

public class ImageFragment extends Fragment {

    private String mThumbnailUrl;

    public ImageFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ImageView stepImage = rootView.findViewById(R.id.iv_step);
        try {
            Glide.with(getContext()).load(mThumbnailUrl).into(stepImage);
        } catch (Exception e){
            Toast.makeText(getContext(), "Image cannot be loaded for this step", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    public void setThumbnailUrl(String thumbnailUrl){
        mThumbnailUrl = thumbnailUrl;
    }
}
