package com.UCIAutismAppJam.Team10;

import com.UCIAutismAppJam.Team10.Utilities.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnKeyListener;

public class AddModeActivity extends Activity implements OnClickListener {

	EditText eAddNew;
	Button add;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_mode);
		
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "chalkboard.ttf"); 
		
		add = (Button) findViewById(R.id.b_add);
        add.setOnClickListener(this);
        add.setTypeface(font);
        add.setTextColor(Color.WHITE);
        
        eAddNew = (EditText) findViewById(R.id.e_addNew);
        eAddNew.setTextColor(Color.WHITE);
        eAddNew.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  return true;
                }
                return false;
            }
        });
        eAddNew.setTypeface(font);
		
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.write_test, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.b_add:
			eAddNew = (EditText) findViewById(R.id.e_addNew);
			
			System.out.println(eAddNew.getText().toString());

			// Uncomment this
			// Util.checkSpell("Helllo");
			
			
			// Checking for empty input...
			if(eAddNew.getText().toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "Please enter something", Toast.LENGTH_LONG).show();
			} else {
				// Checking if the input string contains only characters
				if(eAddNew.getText().toString().trim().matches("[a-zA-Z ]+")) {
					// Checks if the entered input is a word or sentence.
					if(eAddNew.getText().toString().split(" ").length > 1) {
						Util.writeWordToFilesFolder(getApplicationContext(), "added_sentences.txt", eAddNew.getText().toString());
						Toast.makeText(getApplicationContext(), "The sentence \"" + eAddNew.getText().toString() + "\" has been added", Toast.LENGTH_LONG).show();
					} else {
						Util.writeWordToFilesFolder(getApplicationContext(), "added_words.txt", eAddNew.getText().toString());
						Toast.makeText(getApplicationContext(), "The word \"" + eAddNew.getText().toString() + "\" has been added", Toast.LENGTH_LONG).show();
					}
					eAddNew.setText("");
				} else {
					Toast.makeText(getApplicationContext(), "Please enter only characters", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
}