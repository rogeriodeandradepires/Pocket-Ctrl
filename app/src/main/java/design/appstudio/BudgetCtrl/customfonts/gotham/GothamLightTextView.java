package design.appstudio.BudgetCtrl.customfonts.gotham;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by roger on 07/07/2016.
 */
public class GothamLightTextView extends android.support.v7.widget.AppCompatTextView {

    public GothamLightTextView(Context context, AttributeSet attrs) {
    super(context,attrs);
    this.setTypeface(Typeface.createFromAsset(context.getAssets(),
            "fonts/gotham/Gotham-Light.otf"));
    }
}
