//package com.palteam.shabbik.ui.adapters;
//
//import java.util.List;
//
//import com.palteam.shabbik.beans.FacebookFriendsListItem;
//import com.palteam.shabbik.beans.FacebookFriendsListItemInt;
//import com.palteam.shabbik.beans.FacebookFriendsListSection;
//import com.palteam.shabbik.provider.FacebookProvider;
//import com.palteam.shabbik.utils.IConstants;
//import com.palteam.shabbik.utils.ImageLoaderManager;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Typeface;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.palteam.shabbik.R;
//public class FacebookFriendsAdapter  extends BaseAdapter {
//
//    private final String TAG = FacebookFriendsAdapter.class.getSimpleName();
//
//    private Activity activity;
//    private Context context;
//    private List<FacebookFriendsListItemInt> listData;
//
//    private ImageLoaderManager imageLoaderManager;
//
//    private boolean isLastSection = true;
//
//    private OnFriendSelected onFriendSelected;
//
//    public FacebookFriendsAdapter(Activity activity,
//                                  List<FacebookFriendsListItemInt> listData) {
//        super();
//
//        this.activity = activity;
//        this.context = activity.getApplicationContext();
//        this.listData = listData;
//
//        imageLoaderManager = new ImageLoaderManager();
//
//        onFriendSelected = (OnFriendSelected) activity;
//    }
//
//    @Override
//    public int getCount() {
//        return listData.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return listData.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView,
//                        ViewGroup viewGroup) {
//
//        ViewHolderItem viewHolderItem = null;
//        ViewHolderSection viewHolderSection = null;
//
//        boolean isItemSection = listData.get(position).isSection();
//
//        if (convertView == null || isItemSection != isLastSection
//                || isItemSection
//                && convertView.getTag() instanceof ViewHolderItem
//                || !isItemSection
//                && convertView.getTag() instanceof ViewHolderSection) {
//
//            // Change occur, inflate new layout
//            if (isItemSection) {
//
//                convertView = View.inflate(viewGroup.getContext(),
//                        R.layout.facebook_friend_list_item_section, null);
//
//                viewHolderSection = new ViewHolderSection();
//
//                viewHolderSection.titleTextView = (TextView) convertView
//                        .findViewById(R.id.textViewForSectionTitle);
//
//                // Set font on section text-view.
//                Typeface sectionTitleTypeface = Typeface.createFromAsset(
//                        context.getAssets(),
//                        IConstants.Fonts.ROBOTO_CONDENSED_BOLD);
//                viewHolderSection.titleTextView
//                        .setTypeface(sectionTitleTypeface);
//
//                convertView.setTag(viewHolderSection);
//
//                isLastSection = true;
//
//            } else {
//
//                convertView = View.inflate(viewGroup.getContext(),
//                        R.layout.facebook_friend_list_item_layout, null);
//
//                viewHolderItem = new ViewHolderItem();
//
//                viewHolderItem.nameTextView = (TextView) convertView
//                        .findViewById(R.id.textViewForFacebookFriendName);
//
//                Typeface nameTypeface = Typeface.createFromAsset(
//                        context.getAssets(),
//                        IConstants.Fonts.ROBOTO_CONDENSED_REGULAR);
//                viewHolderItem.nameTextView.setTypeface(nameTypeface);
//
//                viewHolderItem.friendProfilePictureImageView = (ImageView) convertView
//                        .findViewById(R.id.imageViewForFriendProfilePicture);
//
//                viewHolderItem.invitePlayImageView = (ImageView) convertView
//                        .findViewById(R.id.imageViewForInvitePlayButton);
//
//                convertView.setTag(viewHolderItem);
//
//                isLastSection = false;
//            }
//
//        } else {
//
//            if (isItemSection) {
//
//                viewHolderSection = (ViewHolderSection) convertView.getTag();
//
//            } else {
//
//                viewHolderItem = (ViewHolderItem) convertView.getTag();
//
//            }
//
//        }
//
//        if (!listData.get(position).isSection()) {
//
//            FacebookFriendsListItem item = (FacebookFriendsListItem) listData
//                    .get(position);
//
//            viewHolderItem.nameTextView.setText(item.getName());
//
//            imageLoaderManager.loadImage(
//                    viewHolderItem.friendProfilePictureImageView,
//                    FacebookProvider.getFacebookUserProfileURI(item.getId()));
//
//            switch (item.getType()) {
//
//                case PLAYER:
//                    viewHolderItem.invitePlayImageView
//                            .setImageResource(R.drawable.button_start_play_with_friend_selector);
//
//                    break;
//
//                case NOT_PLAYER:
//                    viewHolderItem.invitePlayImageView
//                            .setImageResource(R.drawable.button_invite_friend_selector);
//                    break;
//
//                default:
//                    break;
//            }
//
//            viewHolderItem.invitePlayImageView
//                    .setOnClickListener(new OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//
//                            onFriendSelected.onFriendSelected(position);
//                        }
//                    });
//        } else {
//
//            viewHolderSection.titleTextView
//                    .setText(((FacebookFriendsListSection) listData
//                            .get(position)).getTitle());
//        }
//
//        return convertView;
//    }
//
//    /**
//     * Hold the views of friends list item.
//     *
//     * @author Ahmad Abu Rjeila.
//     */
//    static class ViewHolderItem {
//
//        TextView nameTextView;
//        ImageView friendProfilePictureImageView;
//        ImageView invitePlayImageView;
//    }
//
//    /**
//     * Hold the views of friends list section.
//     *
//     * @author Ahmad Abu Rjeila.
//     */
//    static class ViewHolderSection {
//        TextView titleTextView;
//    }
//
//    /**
//     * Handle selected friend.
//     *
//     * @author Ahmad Abu Rjeila.
//     */
//    public interface OnFriendSelected {
//
//        public void onFriendSelected(int position);
//    }
//}
