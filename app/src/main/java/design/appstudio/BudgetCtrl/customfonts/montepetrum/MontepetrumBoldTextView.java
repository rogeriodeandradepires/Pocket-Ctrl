package design.appstudio.BudgetCtrl.customfonts.montepetrum;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by roger on 07/07/2016.
 */
public class MontepetrumBoldTextView extends android.support.v7.widget.AppCompatTextView {

    public MontepetrumBoldTextView(Context context, AttributeSet attrs) {
    super(context,attrs);
    this.setTypeface(Typeface.createFromAsset(context.getAssets(),
            "fonts/montepetrum/Montepetrum bold.ttf"));
    }
}
