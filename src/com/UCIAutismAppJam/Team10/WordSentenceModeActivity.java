package com.UCIAutismAppJam.Team10;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.speech.tts.TextToSpeech;

import com.UCIAutismAppJam.Team10.Utilities.Util;

public class WordSentenceModeActivity extends Activity implements OnClickListener, TextToSpeech.OnInitListener {
	
	SpeechRecognition speech;
	
	Button bSpeak;
	TextView tvQuestion;
	
	AlphaAnimation fadeIn, fadeOut;
	
	private TextToSpeech tts;
	
	ArrayList<String> wordsSentenceList = new ArrayList<String>();
	ArrayList<String> copyOfWordsSentenceList = new ArrayList<String>();
	
	Random randomGenerator = new Random();
	
	String currentDisplayString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_sentence_mode);
		
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		tts = new TextToSpeech(this, this);
		
		fadeIn = new AlphaAnimation(0.0f , 1.0f );
		fadeOut = new AlphaAnimation( 1.0f , 0.0f );

		fadeIn.setDuration(1200);
		fadeIn.setFillAfter(true);
		
		fadeOut.setDuration(1200);
	    fadeOut.setFillAfter(true);
		
		speech = new SpeechRecognition(this);
		
		Intent intent = getIntent();
		
		String mode = intent.getStringExtra("Mode");
		String baseFileName = "";
		String customFileName = "";

		Typeface font = Typeface.createFromAsset(getAssets(), "chalkboard.ttf");
		tvQuestion = (TextView) findViewById(R.id.tv_question);
		
		tvQuestion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				speakOut();
			}});

		bSpeak = (Button) findViewById(R.id.b_speak);
		bSpeak.setOnClickListener(this);
		
		
		
		// Checking the files to be read based on the mode in which the user has entered this screen.
		if(mode.equals("Word")) {
			baseFileName = "store_words.txt";
			customFileName = "added_words.txt";
		} else if(mode.equals("Sentence")) {
			baseFileName = "store_sentence.txt";
			customFileName = "added_sentences.txt";
		}
		
		wordsSentenceList = Util.readWordsFromFileFolder(getApplicationContext(), baseFileName, customFileName);
		copyOfWordsSentenceList.addAll(wordsSentenceList);
		
		int randomIndex = ((randomGenerator.nextInt(100000))%(copyOfWordsSentenceList.size()));
		tvQuestion.setText(copyOfWordsSentenceList.get(randomIndex));
		tvQuestion.setTextSize(25);
		tvQuestion.setTypeface(font);
		tvQuestion.setTextColor(Color.WHITE);
		
		currentDisplayString = copyOfWordsSentenceList.get(randomIndex);
		
		// Removing the word in the index of the displayed word
		if(wordsSentenceList.size() != 1) {
			copyOfWordsSentenceList.remove(randomIndex);
		}
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.readtest, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.b_speak:
			speech.matches = new ArrayList<String>();
			speech.start();
			break;
		}
	}
	
	public void checkWords() throws InterruptedException {
		ArrayList<String> possibleWords = speech.matches;
		boolean wasSaid = false;
		
		for(String s : possibleWords) {
			this.setTitle(this.getTitle() + " | " + s);
			if(tvQuestion.getText().toString().equalsIgnoreCase(s)) {
				wasSaid = true;
			}
		}
		
		if(wasSaid == true) {
			this.setTitle("GOOD!");

			final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_disp);
			linearLayout.setBackgroundResource(R.drawable.bg2good);
			
			Handler handler = new Handler(); 
			handler.postDelayed(new Runnable() { 
				public void run() { 
					linearLayout.setBackgroundResource(R.drawable.bg2normal);
				} 
			}, 1300);

			//Play Good Sound
			final MediaPlayer rightSound=MediaPlayer.create(getBaseContext(), R.raw.right);
			rightSound.start();
			
			// Moving to the next word...
			int randomIndex = ((randomGenerator.nextInt(100000))%(copyOfWordsSentenceList.size()));

			tvQuestion.startAnimation(fadeOut);
			tvQuestion.setVisibility(View.GONE);

			tvQuestion.startAnimation(fadeIn);
			tvQuestion.setVisibility(View.VISIBLE);

			tvQuestion.setText(copyOfWordsSentenceList.get(randomIndex));
			
			currentDisplayString = copyOfWordsSentenceList.get(randomIndex);

			// Removing the element from the list once used and if the arrayList is empty repopulating it from te original list...
			if(wordsSentenceList.size() != 1) {
				copyOfWordsSentenceList.remove(randomIndex);
	
				if(copyOfWordsSentenceList.isEmpty()) {
					System.out.println("Sentence Copied");
					copyOfWordsSentenceList.addAll(wordsSentenceList);
				}
			}
			// linearLayout.setBackgroundResource(R.drawable.bg2normal);
		} else  {
			this.setTitle(tvQuestion.getText() +" : BAD! :: " + this.getTitle());
			
			final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_disp);
			linearLayout.setBackgroundResource(R.drawable.bg2mbad);
			
			Handler handler = new Handler(); 
			handler.postDelayed(new Runnable() { 
				public void run() { 
					linearLayout.setBackgroundResource(R.drawable.bg2normal);
				} 
			}, 1300);
			
			//Play Bad Sound
			final MediaPlayer wrongSound=MediaPlayer.create(getBaseContext(), R.raw.wrong);  
			wrongSound.start();
		}
		speech.matches = new ArrayList<String>();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate
		} else {
			Log.e("TTS", "Initilization Failed");
		}
	}
	
	private void speakOut() {

		tts.speak(currentDisplayString, TextToSpeech.QUEUE_FLUSH, null);
	}
}