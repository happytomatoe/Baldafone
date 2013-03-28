package de.geymer.je.Gameplay;

import com.example.bubblerubble.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class NewGame2 extends Activity {
	private int time;
	View TimerDialogView;
	Builder TimerDialog;
	/**
	 * @return the time
	

	/**
	 * @param time
	 *            the time to set
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_game2);
		
//		CheckBox timerEnabled = (CheckBox) findViewById(R.id.checkBox1);
//		timerEnabled.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				Button timerChooser = (Button) findViewById(R.id.timechoose);
//				if (arg1)
//					timerChooser.setVisibility(View.VISIBLE);
//				else
//					timerChooser.setVisibility(View.GONE);
//
//			}
//
//		});
		// start(this.findViewById(R.id.Button01));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game2, menu);
		return true;
	}

	public void cancel(View view) {
		finish();
	}

	public void choseTime(View view) {
		LayoutInflater inflater = getLayoutInflater();
		TimerDialogView = inflater.inflate(R.layout.timerdialog, null);
		TimerDialog = new AlertDialog.Builder(this)
		.setTitle("Timer")
		.setView(TimerDialogView)
		.setPositiveButton("Set", null);
		TimerDialog.show();
		
	}

	public void start(View view) {
		EditText et1 = (EditText) findViewById(R.id.player1);
		EditText et2 = (EditText) findViewById(R.id.player2);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(et2.getWindowToken(), 0);
//			EditText min = (EditText) TimerDialogView
//					.findViewById(R.id.timeMin);
//			int minutes = Integer.parseInt((min).getText().toString());
//			int sec = Integer.parseInt(((EditText) TimerDialogView
//					.findViewById(R.id.timeSec)).getText().toString());
//			time = (minutes * 60 + sec) * 1000;
//			Log.d("Time", "" + time);
		Intent n=new Intent(this, Game.class);
		n.putExtra("bot",getIntent().getBooleanExtra("bot", false));
		//n.putExtra("time", time);
		n.putExtra("name1", et1.getText().toString());
		n.putExtra("name2", et2.getText().toString());
		n.putExtra("newGame", true);
		this.startActivity(n);
		finish();
	}

}
