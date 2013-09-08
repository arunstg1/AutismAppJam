package com.UCIAutismAppJam.Team10;



import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.UCIAutismAppJam.Team10.Utilities.Util;

public class MainActivity extends Activity implements OnClickListener 
{
	SpeechRecognition speech;
	
	
	Button bWordMode, bSentenceMode, bAddWordSentenceMode, bManagePrioritiesMode, bClearCustomWordsSentences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	try {
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.activity_main);
    		
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    		
    		speech = new SpeechRecognition(this);

    		Typeface font = Typeface.createFromAsset(getAssets(), "chalkboard.ttf");
    		
    		bWordMode = (Button) findViewById(R.id.b_wordmode);
    		bWordMode.setOnClickListener(this);
    		bWordMode.setTypeface(font);
    		bWordMode.setTextColor(Color.WHITE); 
    		
    		bSentenceMode = (Button) findViewById(R.id.b_sentencemode);
    		bSentenceMode.setOnClickListener(this);
    		bSentenceMode.setTypeface(font); 
    		bSentenceMode.setTextColor(Color.WHITE);
    		
    		bAddWordSentenceMode = (Button) findViewById(R.id.b_addwordssentences);
    		bAddWordSentenceMode.setOnClickListener(this);
    		bAddWordSentenceMode.setTypeface(font);
    		bAddWordSentenceMode.setTextColor(Color.WHITE);
    		
    		bClearCustomWordsSentences = (Button) findViewById(R.id.b_clearcustomwordssentences);
    		bClearCustomWordsSentences.setOnClickListener(this);
    		bClearCustomWordsSentences.setTypeface(font);
    		bClearCustomWordsSentences.setTextColor(Color.WHITE);
    		
    		// Checking if the words.txt file from the assets folder needs to be copied into the Files folder...
    		Util.copyFromAssetsToFilesFolder(getApplicationContext(), "words.txt", "store_words.txt");
    		
    		// Checking if the sentence.txt file from the assets folder needs to be copied into the Files folder...
    		Util.copyFromAssetsToFilesFolder(getApplicationContext(), "sentence.txt", "store_sentence.txt");
    		
//    	    TextView txt = (TextView) findViewById(R.id.custom_font);  
//    	    Typeface font = Typeface.createFromAsset(getAssets(), "chalkboard.ttf");  
//    	    txt.setTypeface(font);  
    	    
    	} catch (IOException e) {
    		e.printStackTrace();
    	}

    }
    
    public void onClick(View v) 
    {
    	switch(v.getId()) {
			case R.id.b_wordmode:
				Intent intWordModeActivity  = new Intent (this, WordSentenceModeActivity.class);
				intWordModeActivity.putExtra("Mode", "Word");
				this.startActivity(intWordModeActivity);
				// bWordMode.setBackgroundResource(R.drawable.buttonpressed);
				break;
			
			case R.id.b_sentencemode:
				Intent intSentenceModeActivity  = new Intent (this, WordSentenceModeActivity.class);
				intSentenceModeActivity.putExtra("Mode", "Sentence");
				this.startActivity(intSentenceModeActivity);
				// bSentenceMode.setBackgroundResource(R.drawable.buttonpressed);
				break;
				
			case R.id.b_addwordssentences:
				Intent intAddModeActivity  = new Intent (this, AddModeActivity.class);
				this.startActivity(intAddModeActivity);
				// bAddWordSentenceMode.setBackgroundResource(R.drawable.buttonpressed);
				break;
				
			case R.id.b_clearcustomwordssentences:
				
				final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
				// txtEditTeam.setImeActionLabel("DONE",EditorInfo.IME_ACTION_DONE);
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				dlg.setTitle("Clear custom contents");
				dlg.setMessage("Do you want to delete the words/sentences you added?");
				dlg.setPositiveButton("Cancel", null);
				dlg.setNegativeButton("OK", new DialogInterface.OnClickListener () {
					public void onClick(DialogInterface dialog, int button) {
						ArrayList<String> tempAL = new ArrayList<String>();
						tempAL.add("");
						Util.writeToFilesFolder(getApplicationContext(), "added_sentences.txt", tempAL);
						Util.writeToFilesFolder(getApplicationContext(), "added_words.txt", tempAL);
						Toast.makeText(getApplicationContext(), "Custom Words/Sentences Cleared", Toast.LENGTH_LONG).show();
					}
 			    });
				dlg.show();
				
    	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
