package design.appstudio.BudgetCtrl.customfonts.eras;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by roger on 07/07/2016.
 */
public class ErasMediumTextView extends android.support.v7.widget.AppCompatTextView {

    public ErasMediumTextView(Context context, AttributeSet attrs) {
    super(context,attrs);
    this.setTypeface(Typeface.createFromAsset(context.getAssets(),
            "fonts/eras/ERASMD.TTF"));
    }
}
