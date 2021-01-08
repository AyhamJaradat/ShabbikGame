package com.palteam.shabbik.ui.adapters;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.palteam.shabbik.R;
import com.palteam.shabbik.beans.ChallengeListItemModel;


import java.util.List;

import com.palteam.shabbik.beans.ChallengeListItemModel;
import com.palteam.shabbik.beans.ChallengeListItemModel.ChallengeType;
import com.palteam.shabbik.utils.ImageLoaderManager;



import android.content.Context;
//import android.support.v4.app.Fragment;


import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class ChallengesAdapter extends BaseAdapter {
    private static final String TAG = ChallengesAdapter.class.getSimpleName();
    private Context context;
    private List<ChallengeListItemModel> challenges;

    private ImageLoaderManager imageLoaderManager;

    private Fragment fragment;
    private OnStartGamePressed onStartGamePressedInterface;

    public ChallengesAdapter(Context context,
                             List<ChallengeListItemModel> challenges, Fragment fragment) {
        super();
        this.context = context;
        this.challenges = challenges;
        this.fragment = fragment;

        imageLoaderManager = new ImageLoaderManager();
    }

    @Override
    public int getCount() {
        return challenges.size();
    }

    @Override
    public Object getItem(int position) {
        return challenges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.challenge_list_item,
                    null);

            viewHolder = new ViewHolder();
            viewHolder.contactImageView = (ImageView) convertView
                    .findViewById(R.id.imageViewForContactNameChallengeActivity);
            viewHolder.contactNameTextView = (TextView) convertView
                    .findViewById(R.id.textViewForContactNameChallengeActivity);
            viewHolder.roundNumberTextView = (TextView) convertView
                    .findViewById(R.id.textViewForRoundNumberChallengeActivity);
            viewHolder.roundTimeTextView = (TextView) convertView
                    .findViewById(R.id.textViewForRoundTimeChallengeActivity);
            viewHolder.statusMessageTextView = (TextView) convertView
                    .findViewById(R.id.textViewForStatusMessageChallengeActivity);
            viewHolder.startPlayImageViewButton = (ImageView) convertView
                    .findViewById(R.id.imageViewForStartPlayButtonChallengeActivity);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChallengeListItemModel challenge = challenges.get(position);

        // Contact image.
        imageLoaderManager.loadImage(viewHolder.contactImageView,
                challenge.getImageUri());

        viewHolder.contactNameTextView.setText(challenge.getName());

        viewHolder.roundNumberTextView.setText(challenge.getRoundNumber());

        viewHolder.roundTimeTextView.setText(challenge.getTimeAgo());

        viewHolder.statusMessageTextView.setText(challenge.getStatusMessage());

        switch (challenge.getType()) {

            case DONE_CHALLENGES:

//			viewHolder.startPlayImageViewButton.setVisibility(View.VISIBLE);
//			viewHolder.startPlayImageViewButton
//					.setImageResource(R.drawable.button_start_game_from_done_challenge);
//			setListenerOnStartPlayButton(viewHolder, position,
//					challenge.getType());

                break;

            case FRIENDS_CHALLENGES:

                break;

            case MY_CHALLENGE:

                viewHolder.startPlayImageViewButton.setVisibility(View.VISIBLE);
                setListenerOnStartPlayButton(viewHolder, position,
                        challenge.getType());

                break;

            default:
                break;

        }

        return convertView;
    }

    /**
     * Hold views for challenge list item. Decrease loading time.
     *
     * @author Ahmad Abu Rjeila.
     */
    private class ViewHolder {

        ImageView contactImageView;
        TextView contactNameTextView;
        TextView roundNumberTextView;
        TextView roundTimeTextView;
        TextView statusMessageTextView;
        ImageView startPlayImageViewButton;
    }

    private void setListenerOnStartPlayButton(ViewHolder viewHolder,
                                              final int position, final ChallengeListItemModel.ChallengeType challengeType) {

        viewHolder.startPlayImageViewButton
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        onStartGamePressedInterface = (OnStartGamePressed) fragment;
                        onStartGamePressedInterface.onStartGamePressed(
                                position, challengeType);

                    }
                });

    }

    public interface OnStartGamePressed {

        public void onStartGamePressed(int position, ChallengeType challengeType);
    }
}
