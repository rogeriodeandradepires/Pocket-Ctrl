package cn.pedant.SweetAlert.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by roger on 07/07/2016.
 */
public class MyCustomTextView extends android.support.v7.widget.AppCompatTextView {

    public MyCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                "fonts/daxpro/DaxPro-Light.otf"));
//        "fonts/gotham/Gotham-Book.otf"));
    }
}
