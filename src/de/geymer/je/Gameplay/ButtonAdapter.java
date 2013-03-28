/**
 *
 */
package de.geymer.je.Gameplay;

import com.example.bubblerubble.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @author babkamen
 */
public class ButtonAdapter extends BaseAdapter {
    private String text = "יצףךוםדרשחץפ³גאןנמכהז÷ ÿקסלטעüב‏";
    Context context;
    private int numRows = 3, numCols = 11;
    GridView gv;
 
    private TextAdapter ta;
	private int mCellWidth;
	private int mCellHeight;

    /**
     * @return the activeTextView
     */
   
    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {

        return text.length();
    }

    public ButtonAdapter(Context context, GridView gv, TextAdapter ta) {
        this.context = context;
        this.ta = ta;
        this.gv = gv;
        mCellWidth = gv.getMeasuredWidth() / numCols - 2;
        mCellHeight = gv.getMeasuredHeight() / numRows - 10;
		ViewGroup.LayoutParams layoutParams = gv.getLayoutParams();
		layoutParams.height =200 ; //this is in pixels
		gv.setLayoutParams(layoutParams);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#gebuttoniew(int android.view.View android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 22) return new View(context);
        Button button;
        mCellWidth = gv.getMeasuredWidth() / numCols - 2;
        mCellHeight = gv.getMeasuredHeight() / numRows - 10;
        button = new Button(context);
        button.setTextColor(Color.BLACK);
        button.setBackgroundDrawable(((Game)context).getResources().getDrawable(R.drawable.button));
        button.setLayoutParams(new GridView.LayoutParams(mCellWidth,
                mCellHeight));
        
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	GridView gridview =(GridView)((Game)context).findViewById(R.id.gridview);
                CharSequence s = ((Button) v).getText();
                MyTextView tv=TextAdapter.getActiveTextView();
                if(tv==null)return ;
                tv.setText(s);
                ta.setLetter(tv.getPosition(), s.charAt(0));
                gv.setVisibility(View.INVISIBLE);
                //((Game) context).refreshGrid();
                tv.refreshBack();
                Log.d("Grid Change","2 Size "+gridview.getWidth()+","+gridview.getWidth());
            }
        });
        button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        button.setText(String.valueOf(text.charAt(position)));
        return button;
    }

}
