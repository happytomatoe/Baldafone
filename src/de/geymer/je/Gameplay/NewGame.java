package de.geymer.je.Gameplay;

import com.example.bubblerubble.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class NewGame extends Activity {
   

   

Intent n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_game);
        n=new Intent(this,Game.class);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_new_game, menu);
        return true;
    }

    public void pvp(View view) {
    	Intent n=new Intent(this, NewGame2.class);
    	n.putExtra("bot",false);
        this.startActivity(n);
       finish();
    }

    public void pve(View view) {
    	Intent n=new Intent(this, NewGame2.class);
    	n.putExtra("bot",true);
        this.startActivity(new Intent(this, NewGame2.class));
        finish();
    }

    public void back(View view) {
        finish();

    }
}
