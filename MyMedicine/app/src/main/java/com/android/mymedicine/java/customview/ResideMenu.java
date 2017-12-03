package com.android.mymedicine.java.customview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.mymedicine.R;
import com.android.mymedicine.java.activity.Login;
import com.android.mymedicine.java.activity.MedicineDetails;
import com.android.mymedicine.java.utils.PreferenceUtil;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * User: special Date: 13-12-10 Time: 下午10:44 Mail: specialcyci@gmail.com
 */
public class ResideMenu extends FrameLayout implements View.OnClickListener {
    String TAG = "ResideMenu";
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final int PRESSED_MOVE_HORIZANTAL = 2;
    private static final int PRESSED_DOWN = 3;
    private static final int PRESSED_DONE = 4;
    private static final int PRESSED_MOVE_VERTICAL = 5;

    private ImageView imageViewShadow;
    private ImageView imageViewBackground;
    private LinearLayout layoutLeftMenu;
    private LinearLayout layoutRightMenu;
//    private ScrollView scrollViewLeftMenu;
    private ScrollView scrollViewRightMenu;
    private ScrollView scrollViewMenu;
    /**
     * the activity that view attach to
     */
    private Activity activity;
    /**
     * the decorview of the activity
     */
    private ViewGroup viewDecor;
    /**
     * the viewgroup of the activity
     */
    private TouchDisableView viewActivity;
    /**
     * the flag of menu open status
     */
    private boolean isOpened;
    private GestureDetector gestureDetector;
    private float shadowAdjustScaleX;
    private float shadowAdjustScaleY;
    /**
     * the view which don't want to intercept touch event
     */
    private List<View> ignoredViews;
    // private List<ResideMenuItem> leftMenuItems;
    // private List<ResideMenuItem> rightMenuItems;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;
    private float lastRawX;
    private boolean isInIgnoredView = false;
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState = PRESSED_DOWN;
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    // valid scale factor is between 0.0f and 1.0f.
    private float mScaleValue = 0.5f;
    private LinearLayout ll_residemmenu_profile;
    private LinearLayout ll_residemmenu_notification;
    private LinearLayout ll_residemmenu_logout;
    private LinearLayout ll_residemmenu_language;
    Context context;

    public ResideMenu(Context context) {
        super(context);
        this.context = context;
        initViews(context);

    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu, this);
//        scrollViewLeftMenu = findViewById(R.id.sv_left_menu);
        imageViewShadow = findViewById(R.id.iv_shadow);
        ll_residemmenu_profile = (LinearLayout) findViewById(R.id.ll_residemmenu_profile);
        ll_residemmenu_notification = (LinearLayout) findViewById(R.id.ll_residemmenu_notification);
        ll_residemmenu_logout = (LinearLayout) findViewById(R.id.ll_residemmenu_logout);
        ll_residemmenu_language = (LinearLayout) findViewById(R.id.ll_residemmenu_language);

        ll_residemmenu_profile.setOnClickListener(this);
        ll_residemmenu_notification.setOnClickListener(this);
        ll_residemmenu_logout.setOnClickListener(this);
        ll_residemmenu_language.setOnClickListener(this);
    }

    /**
     * use the method to set up the activity which residemenu need to show;
     *
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        viewDecor.addView(this, 0);
        setViewPadding();
    }

    private void initValue(Activity activity) {
        this.activity = activity;
        // leftMenuItems = new ArrayList<ResideMenuItem>();
        // rightMenuItems = new ArrayList<ResideMenuItem>();
        ignoredViews = new ArrayList<View>();
        viewDecor = (ViewGroup) activity.getWindow().getDecorView();
        viewActivity = new TouchDisableView(this.activity);

        View mContent = viewDecor.getChildAt(0);
        viewDecor.removeViewAt(0);
        viewActivity.setContent(mContent);
        addView(viewActivity);

//        ViewGroup parent = (ViewGroup) scrollViewLeftMenu.getParent();
//        parent.removeView(scrollViewLeftMenu);
//        parent.removeView(scrollViewRightMenu);
    }

    private void setShadowAdjustScaleXByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.034f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.06f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    /**
     * set the menu background picture;
     *
     * @param imageResrouce
     */

    /**
     * the visiblity of shadow under the activity view;
     *
     * @param isVisible
     */
//    public void setShadowVisible(boolean isVisible) {
//        if (isVisible)
//            imageViewShadow.setImageResource(R.drawable.shadow);
//        else
//            imageViewShadow.setImageBitmap(null);
//    }

    /**
     * if you need to do something on the action of closing or opening menu, set
     * the listener here.
     *
     * @return
     */
    public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public OnMenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * we need the call the method before the menu show, because the padding of
     * activity can't get at the moment of onCreateView();
     */
    private void setViewPadding() {
        this.setPadding(viewActivity.getPaddingLeft(),
                viewActivity.getPaddingTop(), viewActivity.getPaddingRight(),
                viewActivity.getPaddingBottom());
    }

    /**
     * show the reside menu;
     */
    public void openMenu(int direction) {
//        imageViewBackground.setImageResource(imageResrouceOrginal);
//        imageViewShadow.setImageResource(R.drawable.shadow);
//        imageViewShadow.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.shadow));
        setScaleDirection(direction);

        isOpened = true;
        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity,
                mScaleValue, mScaleValue);
        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
                mScaleValue + shadowAdjustScaleX, mScaleValue
                        + shadowAdjustScaleY);
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 1.0f);
        scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.start();

    }

    /**
     * close the reslide menu;
     */
    public void closeMenu() {

        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity,
                1.0f, 1.0f);
        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow,
                1.0f, 1.0f);
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 0.0f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleUp_activity.playTogether(alpha_menu);
        scaleUp_activity.start();
    }

    public void logoutMenu() {

    }

    @Deprecated
    public void setDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }

    public void setSwipeDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }

    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }

    private void setScaleDirection(int direction) {

        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT) {
//            scrollViewMenu = scrollViewLeftMenu;
            pivotX = screenWidth * 1.5f;
        } else {
            scrollViewMenu = scrollViewRightMenu;
            pivotX = screenWidth * -0.5f;
        }

        ViewHelper.setPivotX(viewActivity, pivotX);
        ViewHelper.setPivotY(viewActivity, pivotY);
        ViewHelper.setPivotX(imageViewShadow, pivotX);
        ViewHelper.setPivotY(imageViewShadow, pivotY);
        scaleDirection = direction;
    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened())
                closeMenu();
        }
    };

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                showScrollViewMenu();
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                imageViewShadow.setImageDrawable(null);
                imageViewShadow.setBackgroundDrawable(null);
//                imageViewBackground.setImageDrawable(null);
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                hideScrollViewMenu();
                if (menuListener != null)
                    menuListener.closeMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * a helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target,
                                                float targetScaleX, float targetScaleY) {

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY));

        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * a helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX,
                                              float targetScaleY) {

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY));

        scaleUp.setDuration(250);
        return scaleUp;
    }

    private AnimatorSet buildMenuAnimation(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(ObjectAnimator.ofFloat(target, "alpha",
                alpha));

        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

    /**
     * if there ware some view you don't want reside menu to intercept their
     * touch event,you can use the method to set.
     *
     * @param v
     */
    public void addIgnoredView(View v) {
        ignoredViews.add(v);
    }

    /**
     * olio_offer_chat_head_remove the view from ignored view list;
     *
     * @param v
     */
    public void removeIgnoredView(View v) {
        ignoredViews.remove(v);
    }

    /**
     * clear the ignored view list;
     */
    public void clearIgnoredViewList() {
        ignoredViews.clear();
    }

    /**
     * if the motion evnent was relative to the view which in ignored view
     * list,return true;
     *
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    private void setScaleDirectionByRawX(float currentRawX) {
        if (currentRawX < lastRawX)
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? -scaleFloatX
                : scaleFloatX;

        float targetScale = ViewHelper.getScaleX(viewActivity) - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }

    private float lastActionDownX, lastActionDownY;


    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public interface OnMenuListener {

        /**
         * the method will call on the finished time of opening menu's
         * animation.
         */
        public void openMenu();

        /**
         * the method will call on the finished time of closing menu's animation
         * .
         */
        public void closeMenu();


    }

    private void showScrollViewMenu() {
        if (scrollViewMenu != null && scrollViewMenu.getParent() == null) {
            addView(scrollViewMenu);
        }
    }

    private void hideScrollViewMenu() {
        if (scrollViewMenu != null && scrollViewMenu.getParent() != null) {
            removeView(scrollViewMenu);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_residemmenu_profile:
                Intent in = new Intent(context, MedicineDetails.class);
                //from 1 means it gowes from side menue so we dont go its adapter click
                in.putExtra("choose_address", false);
                in.putExtra("isSetResult", false);
                in.putExtra("from", "side_menu");
                context.startActivity(in);

                closeMenu();
                break;

            case R.id.ll_residemmenu_logout:
                PreferenceUtil preferenceUtil = new PreferenceUtil(context);
                preferenceUtil.setLogout();
                Intent loginActivity = new Intent(context, Login.class);
                context.startActivity(loginActivity);
                closeMenu();
                break;
        }


    }

}
