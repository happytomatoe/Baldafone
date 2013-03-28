/**
 *
 */
package de.geymer.je.Gameplay;

import java.util.Arrays;

import android.view.View.OnFocusChangeListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;

/**
 * @author babkamen
 */
public class MyTextView extends TextView {
    /**
     * @return the position
     */

    public int getPosition() {
        return position;

    }
    public void refreshBack() {
        setTextColor(Color.BLACK);
        setBackgroundColor(Color.TRANSPARENT);
    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int right, left, bottom, top;
        if (position % 5 == 4) right = canvas.getClipBounds().right - 3;
        else right = canvas.getClipBounds().right;
        left = canvas.getClipBounds().left;
        if (position > 19) bottom = canvas.getClipBounds().bottom - 3;
        else bottom = canvas.getClipBounds().bottom;

        top = canvas.getClipBounds().top;
        int thickness = 1;
        Paint mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(thickness);

        //left border
        canvas.drawLine(left - thickness / 2, top, left - thickness / 2, bottom, mLinePaint);
        //top border
        canvas.drawLine(left, top - thickness / 2, right, top - thickness / 2, mLinePaint);
        //right border
        canvas.drawLine(right + thickness / 2, top, right + thickness / 2, bottom, mLinePaint);
        //bottom border
        canvas.drawLine(left, bottom + thickness / 2, right, bottom + thickness / 2, mLinePaint);
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    int position;
    public MyTextView(Context context, int position) {
        super(context);
        this.position = position;
        // TODO Auto-generated constructor stub
    }


}
