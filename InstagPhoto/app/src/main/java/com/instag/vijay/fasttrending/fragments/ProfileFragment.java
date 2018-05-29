package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EditProfile;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.Keys;
import com.instag.vijay.fasttrending.MainActivity;
import com.instag.vijay.fasttrending.PermissionCheck;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.activity.CreateBusinessPageActivity;
import com.instag.vijay.fasttrending.activity.CreateGroupActivity;
import com.instag.vijay.fasttrending.activity.PostActivity;
import com.instag.vijay.fasttrending.activity.SeeAllFriendsActivity;
import com.instag.vijay.fasttrending.adapter.FriendsGridAdapter;
import com.instag.vijay.fasttrending.adapter.GroupPageListAdapter;
import com.instag.vijay.fasttrending.adapter.PostsGridAdapter;
import com.instag.vijay.fasttrending.model.BusinessPageModel;
import com.instag.vijay.fasttrending.model.PostModelMain;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.instag.vijay.fasttrending.view.ExpandableHeightGridView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.instag.vijay.fasttrending.EditProfile.getFilePathFromURI;
import static com.instag.vijay.fasttrending.EditProfile.getRealPathFromURI;

/**
 * Created by vijay on 21/11/17.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static ProfileFragment profileFragment;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private Activity activity;
    private TextView txtPostsCount, txtFollowersCount, txtFolloweringCount;
    private ProgressBar pbProfilePhoto;
    private ProgressBar pbCoverPhoto;
    private PreferenceUtil preferenceUtil;
    //    private View llGender;
//    private View llState;
//    private View llCountry;
//    private TextView txtGenderValue;
    private TextView txtFriendsCount;
    private TextView badge_followers;
    private TextView badge_following;
    private TextView badge_friends;
    private TextView txtLabelProfilePosts;
    private View viewYou;
    private View viewFriends;
    private View viewLikes;
    private ExpandableHeightGridView gv_friends_list;
    private ExpandableHeightGridView gv_following_list;
    private ExpandableHeightGridView gv_followers_list;
    private ExpandableHeightGridView gv_post_list;
    private boolean isCoverPhoto;
    private String imagePath;
    private String coverimagePath;
    private ArrayList<Posts> yourPosts;
    private ArrayList<Posts> friendsPosts;
    private ArrayList<Posts> likedPosts;

    public static ProfileFragment getInstance() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        return profileFragment;
    }

    private ImageView ivProfile1;
    private ImageView ivCoverPhoto;
    private TextView txtProfileLocation;
    private TextView txtBiography;
    private TextView txtUserName;
    private TextView txt_see_all_friends;
    private TextView txt_see_all_followings;
    private TextView txt_see_all_followers;
    private TextView txtProfileWebInfo;
    private TextView txtProfileMobile;
    private TextView txtProfileEmail;
    private ListView listview_business_pages;
    private ListView listview_Groups;
    private View fab_post;
    private View txtProfilePagesLable;
    private View txtProfileGroupsLable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profilefragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        gv_friends_list = view.findViewById(R.id.gv_friends_list);
        gv_following_list = view.findViewById(R.id.gv_following_list);
        gv_followers_list = view.findViewById(R.id.gv_followers_list);
        gv_post_list = view.findViewById(R.id.gv_post_list);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        txtProfileLocation = view.findViewById(R.id.txtProfileLocation);
        txtFriendsCount = view.findViewById(R.id.txtFriendsCount);
        badge_friends = view.findViewById(R.id.badge_friends);
        badge_following = view.findViewById(R.id.badge_following);
        badge_followers = view.findViewById(R.id.badge_followers);
        txtProfileWebInfo = view.findViewById(R.id.txtProfileWebInfo);
        txtProfileMobile = view.findViewById(R.id.txtProfileMobile);
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail);
        listview_business_pages = view.findViewById(R.id.listview_business_pages);
        listview_Groups = view.findViewById(R.id.listview_Groups);
        fab_post = view.findViewById(R.id.fab_post);
        txtProfilePagesLable = view.findViewById(R.id.txtProfilePagesLable);
        txtProfileGroupsLable = view.findViewById(R.id.txtProfileGroupsLable);

        viewYou = view.findViewById(R.id.viewYou);
        viewFriends = view.findViewById(R.id.viewFriends);
        viewLikes = view.findViewById(R.id.viewLikes);

        txtBiography = view.findViewById(R.id.txtBiography);
        txtUserName = view.findViewById(R.id.txtUserName);
        txt_see_all_friends = view.findViewById(R.id.txt_see_all_friends);
        txt_see_all_followings = view.findViewById(R.id.txt_see_all_followings);
        txt_see_all_followers = view.findViewById(R.id.txt_see_all_followers);
        txtPostsCount = view.findViewById(R.id.txtPostsCount);
        txtFollowersCount = view.findViewById(R.id.txtFollowersCount);
        txtFolloweringCount = view.findViewById(R.id.txtFolloweringCount);
        txtLabelProfilePosts = view.findViewById(R.id.txtLabelProfilePosts);
//        llGender = view.findViewById(R.id.llGender);
//        llState = view.findViewById(R.id.llState);
//        llCountry = view.findViewById(R.id.llCountry);
//        txtGenderValue = view.findViewById(R.id.txtGenderValue);
//        imgGrid = view.findViewById(R.id.btnGrid);
        view.findViewById(R.id.editCoverPhoto).setOnClickListener(this);
        view.findViewById(R.id.editProfilePhoto).setOnClickListener(this);
        view.findViewById(R.id.editProfileDetail).setOnClickListener(this);
        view.findViewById(R.id.btnBusinessPage).setOnClickListener(this);
        view.findViewById(R.id.btnBusinessGroup).setOnClickListener(this);

        txt_see_all_friends.setOnClickListener(this);
        txt_see_all_followings.setOnClickListener(this);
        txt_see_all_followers.setOnClickListener(this);

        fab_post.setOnClickListener(this);
        viewYou.setOnClickListener(this);
        viewFriends.setOnClickListener(this);
        viewLikes.setOnClickListener(this);
//        btnlist = view.findViewById(R.id.btnlist);
//        imgGrid.setOnClickListener(this);
//        btnlist.setOnClickListener(this);
        //  view.findViewById(R.id.btnEditProfile).setOnClickListener(this);
//        viewInfo = view.findViewById(R.id.txtContactInfo);
//        gridView = view.findViewById(R.id.gridview);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewContact);
        pbCoverPhoto = view.findViewById(R.id.pbCoverPhoto);
        pbProfilePhoto = view.findViewById(R.id.pbProfilePhoto);
//        recyclerView.setVisibility(View.VISIBLE);
//        gridView.setVisibility(View.GONE);
        preferenceUtil = new PreferenceUtil(getActivity());
        ivProfile1 = view.findViewById(R.id.ivProfile1);
        ivCoverPhoto = view.findViewById(R.id.ivCoverPhoto);
        txtUserName.setText(" # " + preferenceUtil.getUserName());

        String profileImage = preferenceUtil.getMyProfile();
        if (!TextUtils.isEmpty(profileImage) && profileImage.contains("http://")) {
            Glide.with(activity)
                    .load(profileImage)
                    .asBitmap()
                    .into(ivProfile1);
        }


        String location = preferenceUtil.getUserState();
        if (!TextUtils.isEmpty(location)) {
            location = location + ", ";
        }
        if (!TextUtils.isEmpty(preferenceUtil.getUserCountry())) {
            location = location + preferenceUtil.getUserCountry();
        }
        if (!TextUtils.isEmpty(preferenceUtil.getUserContactNumber())) {
            txtProfileMobile.setText(preferenceUtil.getUserContactNumber());
        }
        if (!TextUtils.isEmpty(preferenceUtil.getUserMailId())) {
            txtProfileEmail.setText(preferenceUtil.getUserMailId());
        }
        if (!TextUtils.isEmpty(location))
            txtProfileLocation.setText(Html.fromHtml(location));

        txtBiography.setText(preferenceUtil.getUserAboutMe());
        txtProfileWebInfo.setText(preferenceUtil.getWebInfo());


        setPallette();
        //toolbarTextAppernce();

        getMyPosts();
    }

    private void setPallette() {
        String coverimage = preferenceUtil.getMyCoverPhoto();
        if (!TextUtils.isEmpty(coverimage) && coverimage.contains("http://")) {
            Glide.with(activity)
                    .load(coverimage).asBitmap()
                    .into(new SimpleTarget<Bitmap>(300, 300) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            ivCoverPhoto.setImageBitmap(resource);

                          /*  Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    Palette.Swatch dominant = palette.getDominantSwatch();

                                    //this will be used to get the dominant colour from the image

                                    collapsingToolbarLayout.setStatusBarScrimColor(dominant.getRgb());
                                    collapsingToolbarLayout.setContentScrimColor(dominant.getRgb());

                                    //setting the dominant colour to the toolbar

                                    if (Build.VERSION.SDK_INT >= 21) {// used only in lolipop and above devices
                                        Window window = activity.getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.setStatusBarColor(dominant.getRgb());
                                        //Setting the dominant color to the status bar


                                    }
                                }
                            });*/

                        }
                    });


        }
    }

    private void updatePic() {
        if (CommonUtil.isNetworkAvailable(activity)) {

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            MultipartBody.Part aboutmemul =
                    MultipartBody.Part.createFormData("aboutme", preferenceUtil.getUserAboutMe());

            MultipartBody.Part statemul =
                    MultipartBody.Part.createFormData("state", preferenceUtil.getUserState());

            MultipartBody.Part gendermul =
                    MultipartBody.Part.createFormData("gender", preferenceUtil.getUserGender());


            MultipartBody.Part countrymul =
                    MultipartBody.Part.createFormData("country", preferenceUtil.getUserCountry());
            MultipartBody.Part privacyOn =
                    MultipartBody.Part.createFormData("privacyOn", String.valueOf(preferenceUtil.getMyPrivacySettings()));
            MultipartBody.Part contactmul =
                    MultipartBody.Part.createFormData("contact_number", preferenceUtil.getUserContactNumber());

            final MultipartBody.Part profileName =
                    MultipartBody.Part.createFormData("profileName", preferenceUtil.getProfileName());
            MultipartBody.Part userName =
                    MultipartBody.Part.createFormData("username", preferenceUtil.getUserName());

            MultipartBody.Part email =
                    MultipartBody.Part.createFormData("email", preferenceUtil.getUserMailId());
            MultipartBody.Part password =
                    MultipartBody.Part.createFormData("password", preferenceUtil.getUserPassword());
            MultipartBody.Part profile_image = MultipartBody.Part.createFormData("profile_image", preferenceUtil.getMyProfile());
            MultipartBody.Part cover_image = MultipartBody.Part.createFormData("cover_image", preferenceUtil.getMyCoverPhoto());


            MultipartBody.Part pstatusmul =
                    MultipartBody.Part.createFormData("profile_status", preferenceUtil.getUserProfileStatus());

            MultipartBody.Part webInfomul =
                    MultipartBody.Part.createFormData("web_info", preferenceUtil.getWebInfo());

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part uploadimage = null;
            if (imagePath != null) {
                pbProfilePhoto.setVisibility(View.VISIBLE);
                File file = new File(imagePath);
                if (file.exists()) {
                    RequestBody requestFile =
                            RequestBody.create(
                                    null,
                                    file
                            );
                    uploadimage = MultipartBody.Part.createFormData("uploadimage", file.getName(), requestFile);
                }
            }
            MultipartBody.Part uploadCoverimage = null;
            if (coverimagePath != null) {
                pbCoverPhoto.setVisibility(View.VISIBLE);
                File file = new File(coverimagePath);
                if (file.exists()) {
                    RequestBody requestFile =
                            RequestBody.create(
                                    null,
                                    file
                            );
                    uploadCoverimage = MultipartBody.Part.createFormData("uploadCoverimage", file.getName(), requestFile);
                }
            }


            // finally, execute the request
            Call<EventResponse> call = apiService.update_profile(uploadimage, uploadCoverimage, profileName, userName, email, password, profile_image, cover_image, aboutmemul, statemul, countrymul, contactmul, gendermul, privacyOn, pstatusmul, webInfomul);
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {
                    pbProfilePhoto.setVisibility(View.GONE);
                    pbCoverPhoto.setVisibility(View.GONE);
                    EventResponse sigInResponse = response.body();
                    if (sigInResponse != null) {
                        if (sigInResponse.getResult().equals("success")) {
                            if (!TextUtils.isEmpty(sigInResponse.getMessage()))
                                Toast.makeText(activity, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            preferenceUtil.putString(Keys.PROFILE_IMAGE, sigInResponse.getServerimage());
                            preferenceUtil.putString(Keys.COVER_IMAGE, sigInResponse.getCoverimage());
                            if (!TextUtils.isEmpty(imagePath) && MainActivity.mainActivity != null) {
                                MainActivity.mainActivity.refresh();
                            }
                            if (!TextUtils.isEmpty(coverimagePath) && MainActivity.mainActivity != null) {
                                MainActivity.mainActivity.refresh();
                            }
                            setPallette();
                        } else {
                            Toast.makeText(activity, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    pbProfilePhoto.setVisibility(View.GONE);
                    pbCoverPhoto.setVisibility(View.GONE);
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            pbProfilePhoto.setVisibility(View.GONE);
            pbCoverPhoto.setVisibility(View.GONE);
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getMyPosts() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                final String usermail = preferenceUtil.getUserMailId();
                Call<PostModelMain> call = service.getpostsnew(usermail);
                call.enqueue(new Callback<PostModelMain>() {
                    @Override
                    public void onResponse(Call<PostModelMain> call, Response<PostModelMain> response) {
                        PostModelMain postModelMain = response.body();
                        if (postModelMain != null) {
//                            postsArrayList = postModelMain.getPostsArrayList();
                            txtPostsCount.setText(String.valueOf(postModelMain.getTotalposts()));
                            txtFolloweringCount.setText(String.valueOf(postModelMain.getTotal_followering()));
                            txtFollowersCount.setText(String.valueOf(postModelMain.getTotal_followers()));
                            txtFriendsCount.setText(String.valueOf(postModelMain.getFriendsCount()));
                            if (postModelMain.getFrindsList().size() > 0) {
                                badge_friends.setVisibility(View.VISIBLE);
                                badge_friends.setText(String.valueOf(postModelMain.getFrindsList().size()));
                            } else {
                                badge_friends.setVisibility(View.GONE);
                            }
                            if (postModelMain.getFollowingList().size() > 0) {
                                badge_following.setVisibility(View.VISIBLE);
                                badge_following.setText(String.valueOf(postModelMain.getFollowingList().size()));
                            } else {
                                badge_following.setVisibility(View.GONE);
                            }
                            if (postModelMain.getFollowersList().size() > 0) {
                                badge_followers.setVisibility(View.VISIBLE);
                                badge_followers.setText(String.valueOf(postModelMain.getFollowersList().size()));
                            } else {
                                badge_followers.setVisibility(View.GONE);
                            }
                            if (postModelMain.getAboutme() != null)
                                txtBiography.setText(postModelMain.getAboutme());
                            String location = postModelMain.getState();
                            if (!TextUtils.isEmpty(location)) {
                                location = location + ", ";
                            }
                            if (!TextUtils.isEmpty(postModelMain.getCountry())) {
                                location = location + postModelMain.getCountry();
                            }
                            if (!TextUtils.isEmpty(postModelMain.getContact_number())) {
                                txtProfileMobile.setText(preferenceUtil.getUserContactNumber());
                            }
                            if (!TextUtils.isEmpty(preferenceUtil.getUserMailId())) {
                                txtProfileEmail.setText(preferenceUtil.getUserMailId());
                            }
                            if (!TextUtils.isEmpty(location))
                                txtProfileLocation.setText(Html.fromHtml(location));
                            txtProfileWebInfo.setText(postModelMain.getWebInfo());


                            FriendsGridAdapter friendsGridAdapter = new FriendsGridAdapter(activity, postModelMain.getFrindsList());
                            gv_friends_list.setAdapter(friendsGridAdapter);
                            FriendsGridAdapter followingGridAdapter = new FriendsGridAdapter(activity, postModelMain.getFollowingList());
                            gv_following_list.setAdapter(followingGridAdapter);
                            FriendsGridAdapter followersGridAdapter = new FriendsGridAdapter(activity, postModelMain.getFollowersList());
                            gv_followers_list.setAdapter(followersGridAdapter);

                            ArrayList<BusinessPageModel> bList = postModelMain.getBusinessPagesList();
                            if (bList == null || bList.size() <= 0) {
                                txtProfilePagesLable.setVisibility(View.VISIBLE);
                            } else {
                                txtProfilePagesLable.setVisibility(View.GONE);
                                GroupPageListAdapter arrayAdapter = new GroupPageListAdapter(activity, bList);
                                listview_business_pages.setAdapter(arrayAdapter);
                                setListViewHeightBasedOnChildren(listview_business_pages);
                            }
                            ArrayList<BusinessPageModel> gList = postModelMain.getGroupsList();
                            if (gList.size() <= 0) {
                                txtProfileGroupsLable.setVisibility(View.VISIBLE);
                            } else {
                                txtProfileGroupsLable.setVisibility(View.GONE);
                                GroupPageListAdapter arrayAdapter = new GroupPageListAdapter(activity, gList);
                                listview_Groups.setAdapter(arrayAdapter);
                                setListViewHeightBasedOnChildren(listview_Groups);
                            }
                            yourPosts = postModelMain.getYourPostsList();

                            viewYou.callOnClick();
                        }
                        // showGrid();
                    }

                    @Override
                    public void onFailure(Call<PostModelMain> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Post available";
                        } else {
                            message = "Failed";
                        }
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void showGrid() {
//        imgGrid.setColorFilter(ContextCompat.getColor(activity, R.color.button_background));
//        btnlist.setColorFilter(ContextCompat.getColor(activity, R.color.grey));
//        gridView.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.GONE);
//        viewInfo.setVisibility(View.GONE);
////        imageAdapter = new ImageAdapter(activity, postsArrayList);
//        MyAdapter imageAdapter = new MyAdapter(activity, postsArrayList);
//        gridView.setAdapter(imageAdapter);
////        recyclerView.setHasFixedSize(true);
////        EqualSpaceItemDecoration itemDecoration = new EqualSpaceItemDecoration(1);
////
////        recyclerView.addItemDecoration(itemDecoration);
////        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 3);
////        recyclerView.setLayoutManager(layoutManager);
//        if (postsArrayList.size() == 0) {
//            showView(1, "No Posts available");
//        } else {
//            imageAdapter.notifyDataSetChanged();
//            setGridViewHeightBasedOnChildren(gridView, 3);
//            progressBar.setVisibility(View.GONE);
//            viewInfo.setVisibility(View.GONE);
//        }
//
//    }

    public void setGridViewHeightBasedOnChildren(ExpandableHeightGridView gridView, int columns) {
        PostsGridAdapter listAdapter = (PostsGridAdapter) gridView.getAdapter();
        if (listAdapter == null || listAdapter.getCount() == 0) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 2);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + listItem.getMeasuredHeight();
        gridView.setLayoutParams(params);

    }

    public void setListViewHeightBasedOnChildren(ListView listview) {
        ArrayAdapter listAdapter = (ArrayAdapter) listview.getAdapter();
        if (listAdapter == null || listAdapter.getCount() == 0) {
            // pre-condition
            return;
        }

        int totalHeight;
        int items = listAdapter.getCount();
        int rows;

        View listItem = listAdapter.getView(0, null, listview);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = items;
        rows = (int) (x);
        totalHeight *= rows;

        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listItem.getMeasuredHeight() / 4);
        listview.setLayoutParams(params);

    }


    @Override
    public void onResume() {
        super.onResume();
        try {
//            String profileImage = preferenceUtil.getMyProfile();
//            if (!TextUtils.isEmpty(profileImage) && profileImage.contains("http://")) {
//                Glide.with(activity)
//                        .load(profileImage)
//                        .asBitmap()
//                        .into(ivProfile1);
//            }
//            String coverimage = preferenceUtil.getMyCoverPhoto();
//            if (!TextUtils.isEmpty(coverimage) && coverimage.contains("http://")) {
//                Glide.with(activity)
//                        .load(coverimage)
//                        .asBitmap()
//                        .into(ivCoverPhoto);
//            }
//            Typeface font = Typeface.createFromAsset(activity.getAssets(), "fontawesome-webfont.ttf");
//            txtBiography.setTypeface(font);
//            if (TextUtils.isEmpty(preferenceUtil.getUserAboutMe())) {
//                txtBiography.setVisibility(View.GONE);
//            } else {
//                txtBiography.setVisibility(View.VISIBLE);
//                txtBiography.setText(preferenceUtil.getUserAboutMe());
//            }
           /* if (TextUtils.isEmpty(preferenceUtil.getUserGender())) {
                llGender.setVisibility(View.GONE);
            } else {
                llGender.setVisibility(View.VISIBLE);
                String caps = preferenceUtil.getUserGender().substring(0, 1).toUpperCase() + preferenceUtil.getUserGender().substring(1, preferenceUtil.getUserGender().length());
                txtGenderValue.setText(caps);
            }

            if (TextUtils.isEmpty(preferenceUtil.getUserState())) {
                llState.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(preferenceUtil.getUserCountry())) {
                    llState.setVisibility(View.VISIBLE);
                    txtCountryValue.setText(preferenceUtil.getUserCountry());
                }
            } else {
                llState.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(preferenceUtil.getUserCountry())) {
                    txtStateCity.setText(preferenceUtil.getUserState());
                } else {
                    txtStateCity.setText(preferenceUtil.getUserState() + ", " + preferenceUtil.getUserCountry());
                }
            }*/

//            if (TextUtils.isEmpty(preferenceUtil.getUserCountry())) {
//            llCountry.setVisibility(View.GONE);
//            } else {
//                llCountry.setVisibility(View.VISIBLE);
//                txtCountryValue.setText(preferenceUtil.getUserCountry());
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setList() {
//        try {
//            imgGrid.setColorFilter(ContextCompat.getColor(activity, R.color.grey));
//            btnlist.setColorFilter(ContextCompat.getColor(activity, R.color.button_background));
//            gridView.setVisibility(View.GONE);
//            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
//            viewInfo.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//
//            postAdapter = new PostAdapter(activity, postsArrayList);
//            recyclerView.setAdapter(postAdapter);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            postAdapter.notifyDataSetChanged();
//            recyclerView.setNestedScrollingEnabled(false);
//            if (postsArrayList.size() == 0) {
//                showView(1, "No Posts available");
//            } else {
//                progressBar.setVisibility(View.GONE);
//                viewInfo.setVisibility(View.GONE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//    public void showView(int item, String text) {
//        try {
//            switch (item) {
//                case 0:
//                    progressBar.setVisibility(View.VISIBLE);
//                    viewInfo.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    break;
//                case 1:
//                    if (progressBar != null) {
//                        progressBar.setVisibility(View.GONE);
//                        viewInfo.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                        viewInfo.setText(text);
//                    }
//                    break;
//                case 2:
//                    progressBar.setVisibility(View.GONE);
//                    viewInfo.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    setList();
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.txt_see_all_friends:
                    Intent intent = new Intent(activity, SeeAllFriendsActivity.class);
                    intent.putExtra("action", "friends");
                    activity.startActivity(intent);
                    break;
                case R.id.txt_see_all_followings:
                    intent = new Intent(activity, SeeAllFriendsActivity.class);
                    intent.putExtra("action", "following");
                    activity.startActivity(intent);
                    break;
                case R.id.txt_see_all_followers:
                    intent = new Intent(activity, SeeAllFriendsActivity.class);
                    intent.putExtra("action", "follower");
                    activity.startActivity(intent);
                    break;
                case R.id.editCoverPhoto:
                    ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                    if (pendingPermissions.size() > 0) {
                        PermissionCheck.requestPermission(activity, pendingPermissions, 301);
                        return;
                    }
                    isCoverPhoto = true;
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(16, 9)
                            .start(getContext(), this);
                    break;
                case R.id.editProfilePhoto:
                    pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
                    if (pendingPermissions.size() > 0) {
                        PermissionCheck.requestPermission(activity, pendingPermissions, 301);
                        return;
                    }
                    isCoverPhoto = false;
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(16, 16)
                            .start(getContext(), this);
                    break;
                case R.id.editProfileDetail:
                    Intent in = new Intent(activity, EditProfile.class);
                    startActivity(in);
                    break;
                case R.id.btnBusinessGroup:
                    in = new Intent(activity, CreateGroupActivity.class);
                    startActivity(in);
                    break;
                case R.id.btnBusinessPage:
                    in = new Intent(activity, CreateBusinessPageActivity.class);
                    startActivity(in);
                    break;
                case R.id.fab_post:
                    in = new Intent(activity, PostActivity.class);
                    startActivity(in);
                    break;
                case R.id.viewYou:
                    currentTab = 0;
                    viewYou.setBackgroundResource(R.drawable.bottomlinecolor);
                    viewFriends.setBackgroundResource(0);
                    viewLikes.setBackgroundResource(0);

                    PostsGridAdapter postGridAdapter = new PostsGridAdapter(activity, yourPosts);
                    gv_post_list.setAdapter(postGridAdapter);
                    setGridViewHeightBasedOnChildren(gv_post_list, 4);
                    if (yourPosts.size() > 0) {
                        gv_post_list.setVisibility(View.VISIBLE);
                        txtLabelProfilePosts.setVisibility(View.GONE);
                    } else {
                        gv_post_list.setVisibility(View.GONE);
                        txtLabelProfilePosts.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.viewFriends:
                    currentTab = 1;
                    viewYou.setBackgroundResource(0);
                    viewFriends.setBackgroundResource(R.drawable.bottomlinecolor);
                    viewLikes.setBackgroundResource(0);
                    showFriendsPost();
                    getFriendsPost();
                    break;
                case R.id.viewLikes:
                    currentTab = 2;
                    viewYou.setBackgroundResource(0);
                    viewFriends.setBackgroundResource(0);
                    viewLikes.setBackgroundResource(R.drawable.bottomlinecolor);
                    showLikedPosts();
                    getLikedPosts();
                    break;
                /*case R.id.btnGrid:

                    //showGrid();
                    break;
                case R.id.btnlist:
//                    setList();
//                    recyclerView.setVisibility(View.VISIBLE);
                    break;*/
                case R.id.ivEdit:
                case R.id.btnEditProfile:
                    in = new Intent(activity, EditProfile.class);
                    startActivity(in);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int currentTab = 0;

    private void getLikedPosts() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
                String usermail = preferenceUtil.getUserMailId();
                Call<PostModelMain> call = service.get_liked_post(usermail);
                call.enqueue(new Callback<PostModelMain>() {
                    @Override
                    public void onResponse(Call<PostModelMain> call, Response<PostModelMain> response) {
                        PostModelMain postModelMain = response.body();
                        if (postModelMain != null) {
                            likedPosts = postModelMain.getPostsArrayList();
                            showLikedPosts();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModelMain> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Newsfeed available";
                        } else {
                            message = "Failed";
                        }
                        showLikedPosts();
                        //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                showLikedPosts();
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLikedPosts() {
        if (currentTab == 2)
            try {
                if (likedPosts == null) {
                    likedPosts = new ArrayList<>();
                }
                PostsGridAdapter postGridAdapter = new PostsGridAdapter(activity, likedPosts);
                gv_post_list.setAdapter(postGridAdapter);
                setGridViewHeightBasedOnChildren(gv_post_list, 4);

                if (likedPosts.size() > 0) {
                    gv_post_list.setVisibility(View.VISIBLE);
                    txtLabelProfilePosts.setVisibility(View.GONE);
                } else {
                    gv_post_list.setVisibility(View.GONE);
                    txtLabelProfilePosts.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void getFriendsPost() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

                String usermail = preferenceUtil.getUserMailId();
                Call<PostModelMain> call = service.get_friends_post(usermail);
                call.enqueue(new Callback<PostModelMain>() {
                    @Override
                    public void onResponse(Call<PostModelMain> call, Response<PostModelMain> response) {
                        PostModelMain postModelMain = response.body();
                        if (postModelMain != null) {
                            friendsPosts = postModelMain.getPostsArrayList();
                            showFriendsPost();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModelMain> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Newsfeed available";
                        } else {
                            message = "Failed";
                        }
                        showFriendsPost();
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                showFriendsPost();
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFriendsPost() {
        if (currentTab == 1)
            try {
                if (friendsPosts == null) {
                    friendsPosts = new ArrayList<>();
                }
                PostsGridAdapter postGridAdapter = new PostsGridAdapter(activity, friendsPosts);
                gv_post_list.setAdapter(postGridAdapter);
                setGridViewHeightBasedOnChildren(gv_post_list, 4);
                if (friendsPosts.size() > 0) {
                    gv_post_list.setVisibility(View.VISIBLE);
                    txtLabelProfilePosts.setVisibility(View.GONE);
                } else {
                    gv_post_list.setVisibility(View.GONE);
                    txtLabelProfilePosts.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = result.getUri();
                try {
                    String filePath = getRealPathFromURI(activity, selectedImageUri);
                    if (filePath == null) {
                        filePath = getFilePathFromURI(activity, selectedImageUri);
                    }
                    if (!TextUtils.isEmpty(filePath)) {
                        File destination = new File(Environment.getExternalStorageDirectory(),
                                getString(R.string.app_name) + "/" + System.currentTimeMillis() + ".jpg");
                        copy(new File(filePath), destination);
                        if (isCoverPhoto) {
                            coverimagePath = destination.getPath();
                            imagePath = null;
                            Glide.with(activity)
                                    .load(new File(coverimagePath))
                                    .asBitmap()
                                    .into(ivCoverPhoto);
                        } else {
                            imagePath = destination.getPath();
                            coverimagePath = null;
                            Glide.with(activity)
                                    .load(new File(imagePath))
                                    .asBitmap()
                                    .into(ivProfile1);
                        }
                        updatePic();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

//    public class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {
//
//        private final int mSpaceHeight;
//
//        public EqualSpaceItemDecoration(int mSpaceHeight) {
//            this.mSpaceHeight = mSpaceHeight;
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
//                                   RecyclerView.State state) {
//            outRect.bottom = mSpaceHeight;
//            outRect.top = mSpaceHeight;
//            outRect.left = mSpaceHeight;
//            outRect.right = mSpaceHeight;
//        }
//    }


//    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
//
//        private int mItemOffset;
//
//        public ItemOffsetDecoration(int itemOffset) {
//
//            mItemOffset = itemOffset;
//
//        }
//
//
//        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
//
//            this(context.getResources().getDimensionPixelSize(itemOffsetId));
//
//        }
//
//        @Override
//
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
//
//                                   RecyclerView.State state) {
//
//            super.getItemOffsets(outRect, view, parent, state);
//
//            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
//
//        }
//
//    }

//    public class MyAdapter extends BaseAdapter implements View.OnClickListener {
//
//        private List<Posts> originalList;
//        int width;
//
//        public MyAdapter(Context c, List<Posts> originalList) {
//            this.originalList = originalList;
//            Display display = activity.getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            width = size.x;
//        }
//
//        @Override
//        public int getCount() {
//            return originalList.size();
//        }
//
//        @Override
//        public Posts getItem(int arg0) {
//            return originalList.get(arg0);
//        }
//
//        @Override
//        public long getItemId(int arg0) {
//            return arg0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//
//            View grid = convertView;
//            if (originalList.size() > 0) {
//                if (grid == null) {
//                    LayoutInflater inflater = activity.getLayoutInflater();
//                    grid = inflater.inflate(R.layout.grid_image_layout, parent, false);
//                }
//                grid.getLayoutParams().width = width / 3;
//                ImageView postImg = grid.findViewById(R.id.postImg);
//                View rlgrid = grid.findViewById(R.id.rlgrid);
//                View ibPlay = grid.findViewById(R.id.ibPlay);
//                rlgrid.getLayoutParams().width = width / 3;
//                postImg.getLayoutParams().width = width / 3;
//                Posts post = originalList.get(position);
//                rlgrid.setOnClickListener(this);
//                ibPlay.setOnClickListener(this);
//                ibPlay.setTag(post);
//                rlgrid.setTag(post);
//                if (post.getFileType().equalsIgnoreCase("2")) {
//                    if (post.getVideoThumb() != null && !post.getVideoThumb().isEmpty()) {
//                        byte[] decodedString = Base64.decode(post.getVideoThumb(), Base64.DEFAULT);
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        if (bitmap != null)
//                            postImg.setImageBitmap(bitmap);
//                        ibPlay.setVisibility(View.VISIBLE);
//                    } else if (post.getImage() != null && !post.getImage().isEmpty()) {
//                        ibPlay.setVisibility(View.GONE);
//                        Glide.with(activity).load(post.getImage()).centerCrop()
//                                .thumbnail(0.5f)
//                                .crossFade()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(postImg);
//                    }
//
//                } else {
//                    ibPlay.setVisibility(View.GONE);
//                    if (post.getImage() != null && !post.getImage().isEmpty()) {
//
//                        Glide.with(activity).load(post.getImage()).centerCrop()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(postImg);
//                    }
//                }
//            }
//            return grid;
//        }
//
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.ibPlay:
//                case R.id.rlgrid:
//                    Object object = view.getTag();
//                    if (object instanceof Posts) {
//                        Posts post = (Posts) object;
//                        Intent intent = new Intent(activity, PostView.class);
//                        intent.putExtra("postId", post.getPost_id());
//                        activity.startActivity(intent);
//                    }
//                    break;
//            }
//        }
//    }
}
