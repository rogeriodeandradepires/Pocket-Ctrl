package design.appstudio.BudgetCtrl.customfonts.gotham;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by roger on 07/07/2016.
 */
public class GothamBoldestTextView extends android.support.v7.widget.AppCompatTextView {

    public GothamBoldestTextView(Context context, AttributeSet attrs) {
    super(context,attrs);
    this.setTypeface(Typeface.createFromAsset(context.getAssets(),
            "fonts/gotham/Gotham-Bold.otf"));
    }
}
