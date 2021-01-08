package com.palteam.shabbik.ui.fragments;


import java.util.List;

import com.palteam.shabbik.activities.ChallengeActivity;
import com.palteam.shabbik.beans.ChallengeListItemModel;
import com.palteam.shabbik.beans.ChallengeListItemModel.ChallengeType;
import com.palteam.shabbik.ui.adapters.ChallengesAdapter;
import com.palteam.shabbik.ui.adapters.ChallengesAdapter.OnStartGamePressed;

import android.os.Bundle;
//import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

public class ChallengeFragment extends ListFragment implements
        OnStartGamePressed  {
    private final String TAG = ChallengeFragment.class.getSimpleName();

    private static final String ARG_FRAGMENT_INDEX = "fragment_index";

    private int fragmentIndex;
    private List<ChallengeListItemModel> challenges;

    private ChallengesAdapter challegesAdapter;
    private int position;

    public static ChallengeFragment newInstance(int position,
                                                List<ChallengeListItemModel> challenges) {

        ChallengeFragment fragment = new ChallengeFragment();

        fragment.position = position;

        // Save fragment index into bundle.
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_FRAGMENT_INDEX, position);
        fragment.setArguments(bundle);

        fragment.challenges = challenges;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentIndex = getArguments().getInt(ARG_FRAGMENT_INDEX);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setFragmentAdapter();
    }

    private void setFragmentAdapter() {

        if (challenges != null) {
            challegesAdapter = new ChallengesAdapter(getActivity()
                    .getApplicationContext(), challenges, this);

            setListAdapter(challegesAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (this.position == 0) {

            // Done challenge
            onStartGamePressed(position, ChallengeType.DONE_CHALLENGES);

        } else if (this.position == 1) {

            // Friends challenge
            onStartGamePressed(position, ChallengeType.FRIENDS_CHALLENGES);
        }

    }

    @Override
    public void onStartGamePressed(int position, ChallengeType challengeType) {

        switch (challengeType) {

            case DONE_CHALLENGES:
                ((ChallengeActivity) getActivity()).startChalleng(position,
                        challenges.get(position));
                break;

            case FRIENDS_CHALLENGES:
                ((ChallengeActivity) getActivity()).startChalleng(position,
                        challenges.get(position));
                break;

            case MY_CHALLENGE:
                ((ChallengeActivity) getActivity()).startChalleng(position,
                        challenges.get(position));
                break;
            default:
                break;

        }

    }

    public void refresh() {

        if (challegesAdapter != null) {
            challegesAdapter.notifyDataSetChanged();
        }
    }

}
