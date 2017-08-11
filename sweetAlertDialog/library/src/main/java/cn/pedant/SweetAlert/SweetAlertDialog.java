package cn.pedant.SweetAlert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.wangjie.wheelview.WheelView;

import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.util.MyCustomTextView;

public class SweetAlertDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String[] CourseNames = new String[]{};
    private static final String[] CourseTypes = new String[]{"Técnico", "Superior"};
    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    public static final int CUSTOM_PROGRESS_TYPE = 6;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout spinner_disciplinas;
    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private MyCustomTextView mProgressText;
    private String mTitleText;
    private String mStringArray[];
    private Spinner mSpinnerArray[];
    private List<String> mListStringTerms, mListStringSubjects;
    private String mContentText;
    private boolean mShowCancel;
    private boolean mShowContent;
    private String mCancelText;
    private String mConfirmText;
    private int mAlertType;
    private FrameLayout mErrorFrame;
    private FrameLayout mSuccessFrame;
    private FrameLayout mProgressFrame;
    private ConstraintLayout mCustomProgressFrame;
    private SuccessTickView mSuccessTick;
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;
    private Button mConfirmButton;
    private Button mCancelButton;
    private ProgressHelper mProgressHelper;
    private FrameLayout mWarningFrame;
    private FrameLayout mCustomImageFrame;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private boolean mCloseFromCancel;
    private WheelView wv;
    private View layout_error_report;
    private View classView;//, layout_stepper_form;
    private EditText edt_message;
    private ArrayAdapter<String> dataAdapterTerm, dataAdapterSubject;
    private Spinner spinnerTerm, spinnerSubject;
    //    private EditText edt_error_log, edt_message;
    private Button btn_send_error, btn_cancel;

    public SweetAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public SweetAlertDialog(Context context, int alertType) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mProgressHelper = new ProgressHelper(context);
        mAlertType = alertType;
        mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet)OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            List<Animation> childAnims = mErrorXInAnim.getAnimations();
            int idx = 0;
            for (;idx < childAnims.size();idx++) {
                if (childAnims.get(idx) instanceof AlphaAnimation) {
                    break;
                }
            }
            if (idx < childAnims.size()) {
                childAnims.remove(idx);
            }
        }
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet)OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            SweetAlertDialog.super.cancel();
                        } else {
                            SweetAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

//        classView = this.mDialogView

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = (TextView)findViewById(R.id.title_text);
        mContentTextView = (TextView)findViewById(R.id.content_text);
        mProgressText = (MyCustomTextView) findViewById(R.id.text_progress);
        mErrorFrame = (FrameLayout)findViewById(R.id.error_frame);
        mErrorX = (ImageView)mErrorFrame.findViewById(R.id.error_x);
        mSuccessFrame = (FrameLayout)findViewById(R.id.success_frame);
        mProgressFrame = (FrameLayout)findViewById(R.id.progress_dialog);
        mCustomProgressFrame = (ConstraintLayout) findViewById(R.id.custom_progress_dialog);
        mSuccessTick = (SuccessTickView)mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mCustomImageFrame = (FrameLayout)findViewById(R.id.custom_image_frame);
        mWarningFrame = (FrameLayout)findViewById(R.id.warning_frame);
        mConfirmButton = (Button)findViewById(R.id.confirm_button);
        mCancelButton = (Button)findViewById(R.id.cancel_button);
        mProgressHelper.setProgressWheel((ProgressWheel)findViewById(R.id.progressWheel));
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

//        mButton = (ImageButton) findViewById(R.id.addSubjectToList);
//        lstVstepper_spinner = (ListView) findViewById(R.id.stepper_spinner_listView);

        wv = (WheelView) findViewById(R.id.sweetAlert_wheel_view_wv);
        layout_error_report = findViewById(R.id.error_report_include);
//        layout_stepper_form = findViewById(R.id.stepperFormInclude);

//        layout_stepper_form.setVisibility(View.VISIBLE);
//        Toast.makeText(getContext(), "Setou visivel", Toast.LENGTH_SHORT).show();

//        wv.setOffset(2);
//        wv.setItems(Arrays.asList(CourseTypes));
//        wv.setSeletion(3);
//        wv.setOn
//
// ViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
////                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//            }
//        });
        setWheelData(mStringArray);
        setSpinnersData(mListStringTerms, mListStringSubjects, "visible");
        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        changeAlertType(mAlertType, true);
        setProgressText(null);

    }

    private void restore () {
        if (mCustomImage!=null) {
            mCustomImage.setVisibility(View.GONE);
        }
        mErrorFrame.setVisibility(View.GONE);
        mSuccessFrame.setVisibility(View.GONE);
        mWarningFrame.setVisibility(View.GONE);
        mCustomImageFrame.setVisibility(View.GONE);

        mProgressFrame.setVisibility(View.GONE);
        mConfirmButton.setVisibility(View.VISIBLE);

        mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
        mErrorFrame.clearAnimation();
        mErrorX.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
    }

    private void playAnimation () {
        if (mAlertType == ERROR_TYPE) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore();
            }
            switch (mAlertType) {
                case ERROR_TYPE:
                    mErrorFrame.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS_TYPE:
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    // initial rotate layout of success mask
                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                    break;
                case WARNING_TYPE:
                    mConfirmButton.setBackgroundResource(R.drawable.green_button_background);
                    mWarningFrame.setVisibility(View.VISIBLE);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    mConfirmButton.setBackgroundResource(R.drawable.green_button_background);
                    mCustomImageFrame.setVisibility(View.VISIBLE);
                    mCustomImage = (ImageView)mCustomImageFrame.findViewById(R.id.custom_image);
                    setCustomImage(mCustomImgDrawable);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
                    break;
                case CUSTOM_PROGRESS_TYPE:
                    mCustomProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
                    break;
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    public int getAlerType () {
        return mAlertType;
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
    }

    public String getTitleText () {
        return mTitleText;
    }

    public SweetAlertDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    public String getProgressText() {

        return mProgressText.getText().toString();
    }

    public SweetAlertDialog setProgressText(String text) {
        if (mProgressText != null && text != null) {
            mProgressText.setText(text);
        }
        return this;
    }

    public MyCustomTextView getmProgressTextView() {
        return mProgressText;
    }

    public String[] getWheelData() {
        return mStringArray;
    }

    public SweetAlertDialog setWheelData(String[] data) {
        mStringArray = data;

        if (wv != null && mStringArray != null) {
            wv.setVisibility(View.VISIBLE);
            wv.setOffset(1);
            wv.setItems(Arrays.asList(mStringArray));
            wv.setSeletion(0);
//            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//                @Override
//                public void onSelected(int selectedIndex, String item) {
////                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                }
//            });
        }

        return this;
    }

//    public ImageButton getMButton() {
//        return mButton;
//    }

//    public ListView getLstVstepper_spinner() {
//        return lstVstepper_spinner;
//    }

    public Spinner[] getSpinners() {
        return mSpinnerArray;
    }

    public SweetAlertDialog setSpinnersData(List<String> terms, List<String> subjects, String visibility) {

        layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mListStringTerms = terms;
        mListStringSubjects = subjects;

        return this;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) != null) {
            String item = parent.getItemAtPosition(position).toString();
//            if (parent.getId() == spinnerPlantaId) {
//                plantaIndex = position;
//            }
//
//            if (parent.getId() == spinnerPragaId) {
//                pragaIndex = position;
//            }
//
////                    Toast.makeText(parent.getContext(), "Praga Selecionada: " + item, Toast.LENGTH_SHORT).show();
//            if (parent.getItemAtPosition(position).toString().equals("Nova PlantaTalhao")) {
//                addPlanta(inflatePlanta());
//            }
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public EditText[] getMessages() {
        EditText[] editTexts = new EditText[2];

//        layout_error_report = findViewById(R.id.error_report_include);
//
////        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        popupView = layoutInflater.inflate(popupType, null);
//
//        edt_error_log = (EditText)layout_error_report.findViewById(R.id.sweetAlert_errorLog);
//        edt_message = (EditText)layout_error_report.findViewById(R.id.sweetAlert_error_newLargeMessage);

//        editTexts[0] = edt_error_log;
        editTexts[0] = edt_message;

        return editTexts;
    }

    public Button[] getButtons() {
        Button[] buttons = new Button[2];

        buttons[0] = btn_cancel;
        buttons[1] = btn_send_error;

        return buttons;
    }

    public SweetAlertDialog setErrorLog(String visibility) {

        if (visibility.equals("visible")) {
            if (layout_error_report != null) {
                layout_error_report.setVisibility(View.VISIBLE);
//            edt_error_log = (EditText) layout_error_report.findViewById(R.id.sweetAlert_errorLog);
                edt_message = (EditText) layout_error_report.findViewById(R.id.sweetAlert_error_newLargeMessage);
//            btn_cancel = (Button) layout_error_report.findViewById(R.id.btn_cancel_report);
//            btn_send_error = (Button) layout_error_report.findViewById(R.id.btn_send_report);
//            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//                @Override
//                public void onSelected(int selectedIndex, String item) {
////                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                }
//            });
            }
        } else {
            if (layout_error_report != null) {
                layout_error_report.setVisibility(View.GONE);
//            edt_error_log = (EditText) layout_error_report.findViewById(R.id.sweetAlert_errorLog);
//                edt_message = (EditText) layout_error_report.findViewById(R.id.sweetAlert_error_newLargeMessage);
//            btn_cancel = (Button) layout_error_report.findViewById(R.id.btn_cancel_report);
//            btn_send_error = (Button) layout_error_report.findViewById(R.id.btn_send_report);
//            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//                @Override
//                public void onSelected(int selectedIndex, String item) {
////                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                }
//            });
            }
        }


        return this;
    }

    public WheelView getWheel() {

        return wv;
//        return this;
    }

    public SweetAlertDialog cleanWheelData() {

        if (wv != null) {
            wv.setVisibility(View.GONE);
//            wv.setItems(null);
        }

        return this;
    }

    public SweetAlertDialog setCustomImage (Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public SweetAlertDialog setCustomImage (int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId));
    }

    public String getContentText () {
        return mContentText;
    }

    public SweetAlertDialog setContentText (String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    public SweetAlertDialog showCancelButton (boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowContentText () {
        return mShowContent;
    }

    public SweetAlertDialog showContentText (boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText () {
        return mCancelText;
    }

    public SweetAlertDialog setCancelText (String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    public SweetAlertDialog setConfirmText (String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public SweetAlertDialog setCancelClickListener (OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public SweetAlertDialog setConfirmClickListener (OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(SweetAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(SweetAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

    public ProgressHelper getProgressHelper () {
        return mProgressHelper;
    }

    public interface OnSweetClickListener {
        void onClick(SweetAlertDialog sweetAlertDialog);
    }
}