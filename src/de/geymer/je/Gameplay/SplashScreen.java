package de.geymer.je.Gameplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;

import com.example.bubblerubble.R;

public class SplashScreen extends Activity {
public static boolean finish=false;
private boolean mBackPressed;
Handler handler = new Handler();
Runnable r=new Runnable() {
	
	

	@Override
	public void run() {
//		while(!finish)
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		Log.d("State", "Repeat");
		if(finish&&!mBackPressed){
			startActivity(n);
		}else handler.postDelayed(r, 1000);
	}
};
private Intent n;
public void onBackPressed() {
	mBackPressed=true;
	finish();
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		n = this.getIntent();
		n.setClass(this, Game.class);
		handler.postDelayed(r, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
