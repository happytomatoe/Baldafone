package de.geymer.je.Gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.example.bubblerubble.R;

public class MainActivity extends Activity {
	Button[] buttons = new Button[5];
	private SharedPreferences sp;
	private AlphaAnimation anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		if (!sp.getBoolean("поле", false))
			new Task().execute();
		else SplashScreen.finish=true;
		buttons[0] = (Button) findViewById(R.id.new_game);
		buttons[1] = (Button) findViewById(R.id.cont);
		buttons[2] = (Button) findViewById(R.id.sett);
		buttons[3] = (Button) findViewById(R.id.about);
		buttons[4] = (Button) findViewById(R.id.exit);
		fadeIn();

	}

	private void fadeIn() {
		int delay = 0;
		for (int i = 0; i < buttons.length; i++) {
			anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(1000);
			anim.setStartOffset(delay);
			buttons[i].startAnimation(anim);
			delay += 500;
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void exit(View view) {
		// fadeOut();
		finish();
	}

	public void cont(View view) {
		Intent n = new Intent(this, Game.class);
		this.startActivity(n);
		// fadeOut();
	}

	public void about(View view) {
		new AlertDialog.Builder(this).setMessage("BubbleRubble -це ").show();
	}

	public void newGame(View view) {
	    startActivity(new Intent(MainActivity.this, NewGame.class));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		fadeIn();
		super.onResume();
	}

	public void settings(View view) {
		// fadeOut();
		// this.startActivity(new Intent(this, SettingsActivity.class));
	}

	private class Task extends AsyncTask<Void, Void, Void> {
		private int i=0;
        private long start;
		protected void onPreExecute() {

		}

private void copyFile(String filename) {
    AssetManager assetManager = getAssets();
    InputStream in = null;
    OutputStream out = null;
    String newFolderName = "/data/data/" + getPackageName() + "/" + filename.substring(0,filename.indexOf("/"));
    File dir=new File(newFolderName);
    if(!dir.exists())dir.mkdir();
    try {
        in = assetManager.open(filename);
        String newFileName = "/data/data/" + getPackageName() + "/" + filename;
        out = new FileOutputStream(newFileName);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
    } catch (Exception e) {
        Log.e("tag", e.getMessage());
    }

}
		protected Void doInBackground(Void... unused) {
		  copyAssets();
			return null;
		}
//		private  void createAddDict() {
//SharedPreferences sp=getSharedPreferences("uk_UA", Activity.MODE_PRIVATE);
//SharedPreferences sp2=getSharedPreferences("AddDict", Activity.MODE_PRIVATE);
//SharedPreferences.Editor edit=sp2.edit();
//edit.clear().commit();
//Map<String,String> addDict=(Map<String, String>) sp2.getAll();	  
//for (String key : sp.getAll().keySet()) {
//    if(key.length()==2){
//        String part1 = key.substring(1), part2 = key.substring(0, 1), part3 = key
//            .substring(0, key.length() - 1), part4 = key.substring(key
//            .length() - 1);
//
//    if (addDict.containsKey("." + part1))
//        addDict.put("." + part1, addDict.get("." + part1) + " " + part2);
//    else
//        addDict.put("." + part1, part2);}
//    else{
//    String part1="."+key.substring(1,key.length()-1)+".";
//    String part2=key.charAt(0)+"-"+key.charAt(key.length()-1);
//    if (addDict.containsKey(part1))
//        addDict.put(part1, addDict.get(part1)+ " " + part2);
//    else
//        addDict.put(part1, part2);
//    }
//}
//  
//for(String s:addDict.keySet())
//    edit.putString(s, addDict.get(s));
//edit.commit();
//	        }
		/**
         * 
         */
        private void copyAssets() {
           start = System.currentTimeMillis();
            copyFile("shared_prefs/uk_UA.xml");
           //createAddDict(); 
           copyFile("shared_prefs/addDict.xml");
            copyFile("databases/mydb");
            
        }

        protected void onPostExecute(Void unused) {
            Log.d("Finish", "Finish"+(System.currentTimeMillis()-start)/1000);
		}
	}

}
