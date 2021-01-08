package com.palteam.shabbik.ui.adapters;

import java.util.ArrayList;

import com.palteam.shabbik.beans.CorrectWord;

import com.palteam.shabbik.R;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class CorrectWordsListAdapter extends BaseAdapter {

    private ArrayList<CorrectWord> correctWords;
    private int selectedItem;

    public CorrectWordsListAdapter(ArrayList<CorrectWord> correctWords) {
        this.correctWords = correctWords;

    }

    public void setSelectedItem(int position) {
        selectedItem = position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return correctWords.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return correctWords.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Log.e("position", "" + position);
        View v = convertView;

        if (v == null) {

            v = View.inflate(parent.getContext(),
                    R.layout.correct_word_list_item, null);

        }

        // set values from array list
        try {

            TextView pointsValue = (TextView) v
                    .findViewById(R.id.textViewForScoreValueListView);
            if (correctWords.get(position).getScores() == 0) {
                pointsValue.setText("");
            } else {
                pointsValue
                        .setText("" + correctWords.get(position).getScores());
            }

            TextView wordValue = (TextView) v
                    .findViewById(R.id.textViewForCorrectWordListView);
            wordValue.setText(correctWords.get(position).getWord());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}
