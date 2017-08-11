package design.appstudio.BudgetCtrl.customfonts.daxpro;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by roger on 07/07/2016.
 */
public class DaxProLittleBoldTextView extends android.support.v7.widget.AppCompatTextView {

    public DaxProLittleBoldTextView(Context context, AttributeSet attrs) {
    super(context,attrs);
    this.setTypeface(Typeface.createFromAsset(context.getAssets(),
            "fonts/daxpro/DaxPro-Regular.otf"));
    }
}
