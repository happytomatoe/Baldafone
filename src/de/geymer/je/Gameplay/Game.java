package de.geymer.je.Gameplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bubblerubble.R;

import de.geymer.je.database.Database;

public class Game extends Activity implements OnTouchListener {
	private TextAdapter ta;
	private boolean withBot;
	private TextView statusBar, timer;
	private String word = "";
	private static HashSet<String> map=new HashSet<String>();
 
    private static HashMap<String,String> addDict=new HashMap<String,String>();
	private Handler handler = new Handler();
	private ImageButton accept, decline;
	private ArrayList<Integer> wordPositions = new ArrayList<Integer>();
	private GridView gridview, keyboard;
	private int position = 0, turn = 1, time;
	private ArrayList<String> usedWords = new ArrayList<String>();
	private SharedPreferences sp;
	private static Player player1, player2;
	private AlertDialog newTurnDialog;
	CountDownTimer cdt;
	private static final String FILE_NAME = "save1";
	private static final String DIR_NAME = "saves";
	private boolean  newGame;
	public static boolean loading;
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {

			clearWord();
		}
	};
	private File path;
	private File file;
	private TextView tv2;
	private TextView tv4;
	private TextView tv1;
	private TextView tv3;

	   /**
     * @return the map
     */
    public static HashSet<String> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public static void setMap(HashSet<String> map) {
        Game.map = map;
    }

    /**
     * @return the addDict
     */
    public static HashMap<String, String> getAddDict() {
        return addDict;
    }

    /**
     * @param addDict the addDict to set
     */
    public static void setAddDict(HashMap<String, String> addDict) {
        Game.addDict = addDict;
    }

	public  boolean exists(String word) {
		if (sp.getBoolean(word, false))
			return true;	
			return false;
		
	}

	private void finishGame() {
		String message = player1.getScore() > player2.getScore() ? player1
				.getName() : player2.getName();
		new AlertDialog.Builder(this)
			
		.setTitle("Кінець гри ")
				.setPositiveButton("Вихід",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						})
				.setNegativeButton("Нова гра",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent n = new Intent(ta.getContext(),
										Game.class);
								n.putExtra("bot", withBot);
								n.putExtra("time", time);
								n.putExtra("name1", player1.getName());
								n.putExtra("name2", player2.getName());
								startActivity(n);
								finish();
							}
						}).setMessage(message + " переміг").show();
	}

	public void newTurn() {
		clearWord();
		if (ta.isFull())
			finishGame();
		turn = turn == 1 ? 0 : 1;
		initializeDialog();

	}

	private void initializeDialog() {
		if (time == 0)
			return;
		newTurnDialog = new AlertDialog.Builder(this).setTitle("Новий хід ")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						newTurnDialog.dismiss();

						if (time != 0)
							cdt.start();
					}

				}).create();
		if (turn == 1)
			newTurnDialog.setMessage("Хід гравця " + player2.getName());
		else
			newTurnDialog.setMessage("Хід гравця " + player1.getName());
		newTurnDialog.show();

	}

	private void showKeyboard(int arg2) {
		if (arg2 >= 10 && arg2 < 15)
			return;
		keyboard.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game);
		getIntentVariables();
		
		sp=getSharedPreferences("uk_UA",Activity.MODE_PRIVATE);
		long start=System.currentTimeMillis();
		Set s=sp.getAll().keySet();
		System.out.println(s.contains("яблоко"));
		System.out.println("Time "+(System.currentTimeMillis()-start));
		gridview = (GridView) findViewById(R.id.gridview);
		statusBar = (TextView) findViewById(R.id.statusBar);
		keyboard = (GridView) findViewById(R.id.keyboard);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv4 = (TextView) findViewById(R.id.tv4);
		initGridView();
		initTimer();
		initAceptDeclineButtons();
		refreshStats();
		tv1 = (TextView) findViewById(R.id.tv1);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
				(float) (tv1.getMeasuredHeight() * 0.8));
		tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
				(float) (tv3.getMeasuredHeight() * 0.8));
		timer = (TextView) findViewById(R.id.timer);
		timer.setText("" + time);
		path = new File(getFilesDir(), DIR_NAME);
		file = new File(path, FILE_NAME);
//		if (dict.size() == 0)
//			new Task().execute();
//		else
			startGame();

	}

	@Override
	protected void onPause() {
		save();
		
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		save();
		Log.d("State","Destroyed");
		super.onDestroy();
	}

	private void initTimer() {
		if (time != 0) {
			cdt = new CountDownTimer(time, 1000) {
				public void onTick(long millisUntilFinished) {
					timer.setText((int) (millisUntilFinished / 1000 / 60)
							+ ": " + millisUntilFinished / 1000 % 60);
				}

				public void onFinish() {
					newTurn();
				}
			};
		}
		;
	}

	/**
	 * 
	 */
	private void initAceptDeclineButtons() {
		accept = (ImageButton) findViewById(R.id.accept);
		decline = (ImageButton) findViewById(R.id.decline);
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				usedWords.add(word);
				if (turn == 1) {

					player1.adjustScore(word.length());
				} else {

					player2.adjustScore(word.length());

				}
				refreshStats();
				accept.setVisibility(ImageButton.INVISIBLE);
				decline.setVisibility(ImageButton.INVISIBLE);
				word = "";
				// refreshGrid();
				ta.useWord(wordPositions);
				clearWord();
				if (cdt != null)
					cdt.cancel();
				newTurn();
			}
		});
		decline.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				accept.setVisibility(ImageButton.INVISIBLE);
				decline.setVisibility(ImageButton.INVISIBLE);
				// refreshGrid();
				clearWord();
			}
		});
	}

	/**
	 * 
	 */
	private void initGridView() {
		gridview.setOnTouchListener(this);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (ta.IsEnabled(arg2)) {
					TextAdapter.setActiveTextView((MyTextView) arg1);
					showKeyboard(arg2);

				}
				// else refreshGrid();

			}

		});
	}

	/**
	 * 
	 */
	private void getIntentVariables() {
		Intent intent = getIntent();
		time = intent.getIntExtra("time", 0);
		withBot = intent.getBooleanExtra("bot", false);
		setNames(intent.getStringExtra("name1"), intent.getStringExtra("name2"));
		newGame = intent.getBooleanExtra("newGame", false);
		Log.d("New Game",String.valueOf( newGame));
	}

	private void refreshStats() {
		
		tv2.setText(String.valueOf(player1.getScore()));
		tv4.setText(String.valueOf(player2.getScore()));
	}

	private void startGame() {
			Database mydatabase =new Database(this);
		String stWord= mydatabase.getRandomWord();
		mydatabase.close();
		if ((ta = load()) == null) {
			Log.d("ta","null");
			ta = new TextAdapter(this, gridview, stWord);
			usedWords.add(new String(stWord));
			refreshStats();

		} else {
			Log.d("ta","Not null");
			ta.setContext(this);
			ta.setGv(gridview);
		}
		tv1.setText(player1.getName());
		tv3.setText(player2.getName());
		gridview.setAdapter(ta);
		gridview.setVisibility(View.VISIBLE);
		keyboard = (GridView) findViewById(R.id.keyboard);
		ButtonAdapter ba = new ButtonAdapter(this, keyboard, ta);
		keyboard.setAdapter(ba);
		newTurn();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View arg0, MotionEvent me) {
		super.onTouchEvent(me);
		TextView tv = (TextView) gridview.getChildAt(gridview.pointToPosition(
				(int) me.getX(), (int) me.getY()));
		if (tv == null || tv.getText().length() < 1)
			return false;
		if (accept.getVisibility() == View.VISIBLE)
			decline.performClick();
		switch (me.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			Log.d("Action", "Down");
			addLetter(gridview
					.pointToPosition((int) me.getX(), (int) me.getY()));

			break;
		}
		case MotionEvent.ACTION_MOVE: {
			Log.d("Touch ", "Move");
			handler.removeCallbacks(runnable);
			handler.postDelayed(runnable, 1000);
			addLetter(gridview
					.pointToPosition((int) me.getX(), (int) me.getY()));
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			handler.removeCallbacks(runnable);
			if (word.length() == 1) {
				Log.d("Action Cancel len=1", " " + gridview.getHeight() + ","
						+ gridview.getWidth());

				MyTextView et = (MyTextView) gridview.getChildAt(gridview
						.pointToPosition((int) me.getX(), (int) me.getY()));
				gridview.performItemClick(
						et,
						gridview.pointToPosition((int) me.getX(),
								(int) me.getY()), 2);
				clearWord();
				break;
			}
			Log.d("Action Up",
					" " + gridview.getHeight() + "," + gridview.getWidth());

			createWord();
			break;
		}
		default:
			break;
		}
		return true;

	}

	/**
     *
     */
	public void createWord() {
		Log.d("Word", "Create word" + word);
		// gridview.setAdapter(ta);
		if (!exists(word)) {
			statusBar.setText("Слово " + word + " не знайдено");
			clearWord();
		} else if (usedWords.contains(word)) {
			statusBar.setText("Слово " + word + " вже використане");
			clearWord();
		} else if (wordPositions.contains(ta.getLast())) {
			ImageButton accept = (ImageButton) findViewById(R.id.accept);
			ImageButton decline = (ImageButton) findViewById(R.id.decline);
			accept.setVisibility(ImageButton.VISIBLE);
			decline.setVisibility(ImageButton.VISIBLE);
		} else
			clearWord();
	}

	/**
     *
     */
	public void clearWord() {
		word = "";
		for (Integer i : wordPositions) {
			MyTextView tv = ((MyTextView) gridview.getChildAt(i));
			if (tv != null)
				tv.refreshBack();
		}
		wordPositions.clear();
		// statusBar.setText("");
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	public void addLetter(int position) {

		TextView et = (TextView) gridview.getChildAt(position);
		if (et == null || et.getText().length() < 1
				|| et.getText().toString().equals(" ")) {
			Log.d("addLetter ", "Empty Cell");
			createWord();
			return;
		}
		Log.d("AddLetter", "" + position + " " + et.getText());
		if (wordPositions.size() > 0) {
			int last = wordPositions.get(wordPositions.size() - 1);
			if (position != last + 1 && position != last - 1
					&& position != last + 5 && position != last - 5)
				return;
		}
		et.setBackgroundColor(Color.BLACK);
		et.setTextColor(Color.WHITE);
		// Access text in the cell, or the object itself
		if (!wordPositions.contains(position)) {
			wordPositions.add(position);
			word += et.getText();
		}

		statusBar.setText(word);

	}

	public void setNames(String pl1, String pl2) {
		player1 = new Player(pl1, false);
		if (withBot) {
			player2 = new Player(pl2, true);
		} else
			player2 = new Player(pl2, false);

	}

	public boolean save() {
		boolean result = true;
		System.out.println("Path " + path.getAbsolutePath());
		path.mkdir();
		ObjectOutputStream oos = null;
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ta);
			oos.writeObject(player1);
			oos.writeObject(player2);
			oos.writeObject(usedWords);
			oos.writeInt(time);
			oos.writeInt(turn);
		} catch (FileNotFoundException e) {
			result = false;
			e.printStackTrace();

		} catch (IOException e) {
			result = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					result = false;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		return result;

	}

	public TextAdapter load() {
		if (newGame){
		    getIntent().putExtra("newGame", false);
		    return null;
	            
		}
		FileInputStream fis;
		TextAdapter textAdapter = null;
		ObjectInputStream in = null;
		try {

			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			try {
				textAdapter = (TextAdapter) in.readObject();
				if (textAdapter != null) {
					player1 = (Player) in.readObject();
					player2 = (Player) in.readObject();
					usedWords = (ArrayList<String>) in.readObject();
					time = in.readInt();
					turn = in.readInt() == 0 ? 1 : 0;
				}
			} catch (OptionalDataException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return textAdapter;
	}

}
