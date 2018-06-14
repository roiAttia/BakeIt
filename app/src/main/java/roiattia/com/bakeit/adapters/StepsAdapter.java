package roiattia.com.bakeit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.bakeit.R;
import roiattia.com.bakeit.models.Step;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterNewViewHolder> {

    private List<Step> mStepList;
    private Context mContext;
    private final StepAdapterOnClickHandler mClickHandler;

    public StepsAdapter(Context context, StepAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    public interface StepAdapterOnClickHandler{
        void onStepClick(int stepIndex, View Step);
    }

    @NonNull
    @Override
    public StepsAdapterNewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item_step;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new StepsAdapter.StepsAdapterNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepsAdapterNewViewHolder holder, final int position) {
        if(null != mStepList.get(position)) {
            // set the short description of the step
            holder.shortDescription.setText(mStepList.get(position).shortDescription());
            String description = mStepList.get(position).description();
            // check for numbering at the beginning of the description to delete it
            Pattern pattern = Pattern.compile("^[\\d.]*\\s*");
            Matcher matcher = pattern.matcher(description);
            if (matcher.find()) {
                String number = matcher.group(0);
                holder.description.setText(description.substring(number.length(), description.length()));
            }
            // check for video for this step, if exist then set the videoImage to VISIBLE
            if (!mStepList.get(position).videoUrl().equals("")) {
                holder.videoImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_videocam_black_24dp));
                holder.videoImage.setVisibility(View.VISIBLE);
            } else if(!mStepList.get(position).thumbnailUrl().equals("")) {
                holder.videoImage.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ic_image_black_24dp));
                holder.videoImage.setVisibility(View.VISIBLE);
            } else {
                holder.videoImage.setVisibility(View.GONE);
            }

            // add numbering to the step in the list
            holder.stepStatue.setText(String.format("%s.", String.valueOf(position + 1)));
            // set the listeners for the checkbox
            holder.stepStatue.setOnCheckedChangeListener(null);
            holder.stepStatue.setChecked(mStepList.get(position).isChecked());
            holder.stepStatue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mStepList.get(position).setChecked(isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mStepList == null ? 0 : mStepList.size();
    }

    public class StepsAdapterNewViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        @BindView(R.id.cb_statue) CheckBox stepStatue;
        @BindView(R.id.tv_short_description) TextView shortDescription;
        @BindView(R.id.tv_description) TextView description;
        @BindView(R.id.iv_multimedia) ImageView videoImage;

        StepsAdapterNewViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View stepCard) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onStepClick(adapterPosition, stepCard);
        }
    }

    public void setStepList(List<Step> stepList){
        mStepList = stepList;
    }
}
