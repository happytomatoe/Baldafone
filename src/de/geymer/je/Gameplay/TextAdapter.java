package de.geymer.je.Gameplay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.KeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author babkamen
 */
public class TextAdapter extends BaseAdapter implements Serializable {
	// mumber of rows
	private int numRows = 5;
	// mumber of columns
	private int numCols = 5;
	private int mCellWidth, mCellHeight;
	private int TextSize;
	// last edited cell
	transient private int last = -1;
	transient private Context context;
	transient GridView gv;
	transient MyTextView et;
	private static Set<Integer> usedPositions=new HashSet();

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Game context) {
		this.context = context;
	}

	/**
	 * @return the et
	 */
	public MyTextView getEt() {
		return et;
	}

	/**
	 * @param et
	 *            the et to set
	 */
	public void setEt(MyTextView et) {
		this.et = et;
	}

	private char[] letters;
	private static transient MyTextView activeTextView;

	/**
	 * @return the activeTextView
	 */
	public static MyTextView getActiveTextView() {
		return activeTextView;
	}

	public boolean isFull() {
		return usedPositions.size() == 25;
	}

	/**
	 * @param activeTextView
	 *            the activeTextView to set
	 */
	public static void setActiveTextView(MyTextView activeTextView) {
		TextAdapter.activeTextView = activeTextView;
	}

	public TextAdapter(Context context) {
		this.context = context;
		letters = new char[25];
		Arrays.fill(letters, ' ');
		usedPositions = new HashSet<Integer>();

	}

	public TextAdapter(Context context, GridView gv, String s) {
		this.context = context;
		this.gv = gv;
		letters = new char[25];
		Arrays.fill(letters, ' ');
		usedPositions = new HashSet<Integer>();
		for (int i = 10; i < 15; i++) {
			letters[i] = s.charAt(i - 10);
			usedPositions.add(i);
		}

	}

	public boolean IsEnabled(int position) {
		return (position < 24 && letters[position + 1] != ' ' || position > 0
				&& letters[position - 1] != ' ' || position + 5 < 24
				&& letters[position + 5] != ' ' || position - 5 >= 0
				&& letters[position - 5] != ' ')
				&& !usedPositions.contains(position);
	}

	public void useWord(ArrayList<Integer> letterPositions) {
		usedPositions.addAll(letterPositions);
		last = -1;
	}

	public void setLetter(int position, char c) {
		if (!usedPositions.contains(position)) {
			letters[position] = c;
			if (last != -1 && last != position) {
				letters[last] = ' ';
				((TextView) gv.getChildAt(last)).setText("");
			}
			last = position;
		}

	}

	public String toString() {
		return Arrays.toString(letters) + " Used " + usedPositions.toString();
	}

	/**
	 * @return the last
	 */
	public int getLast() {
		return last;
	}

	public char getLetter(int position) {
		return letters[position];
	}

	/**
	 * @return the gv
	 */
	public GridView getGv() {
		return gv;
	}

	/**
	 * @param gv the gv to set
	 */
	public void setGv(GridView gv) {
		this.gv = gv;
	}

	public int getCount() {
		return 25;
	}

	public Object getItem(int position) {
		return letters[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
			mCellWidth = gv.getWidth() / numCols - 2;
			mCellHeight = gv.getHeight() / numRows - 2;
			TextSize = mCellWidth < mCellHeight ? mCellWidth : mCellHeight;
			et = new MyTextView(context, position);
			et.setLayoutParams(new GridView.LayoutParams(mCellWidth,
					mCellHeight));
		et.setFocusable(false);
		et.setFocusableInTouchMode(false);
		et.setClickable(false);
		et.setTextColor(Color.BLACK);
		et.setBackgroundColor(Color.TRANSPARENT);
		et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (TextSize * 0.8));
		et.setGravity(Gravity.CENTER_HORIZONTAL);
		if (letters[position] != ' ')
			et.setText(String.valueOf(letters[position]));

		return et;
	}
}