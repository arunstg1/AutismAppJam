package com.UCIAutismAppJam.Team10;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.WindowManager;
import android.widget.Button;

public class SpeechRecognition implements RecognitionListener
{
	private SpeechRecognizer speech;
	private Activity parent;
	private Intent intent;
	private Boolean isValid;


	public  ArrayList<String> matches;

	public boolean isWorking = false;

	public SpeechRecognition(Activity parent)
	{
		this.parent = parent;
		speech = SpeechRecognizer.createSpeechRecognizer(parent);
		speech.setRecognitionListener(this); //May need to be Main Activity (Parent)

		intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, parent.getPackageName());

		matches = new ArrayList<String>();
		matches.add("");

		this.isValid = true;
	}

	public ArrayList<String> start()
	{

		this.parent.setTitle("START()");

		Button bSpeak = (Button)this.parent.findViewById(R.id.b_speak);
		bSpeak.setBackgroundResource(R.drawable.recordbuttonon);

		if(!speech.isRecognitionAvailable(parent.getBaseContext()))
		{
			this.isValid = false;
			this.parent.setTitle("ERROR: VOICE RECOGNITION NOT AVAILABLE ON THIS DEVICE!");
			final AlertDialog.Builder dlg = new AlertDialog.Builder(this.parent);
			// txtEditTeam.setImeActionLabel("DONE",EditorInfo.IME_ACTION_DONE);
			this.parent.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			dlg.setMessage("Your device doesn't support voice input");
			dlg.setPositiveButton("Cancel", null);
			dlg.show();
		}


		isWorking = true;
		speech.startListening(intent);

		return matches;
	}


	@Override
	public void onBeginningOfSpeech()
	{
		if(this.isValid) {
			this.parent.setTitle("SPEECH START");
			Button bSpeak = (Button)this.parent.findViewById(R.id.b_speak);
			bSpeak.setBackgroundResource(R.drawable.recordbuttonon);
		}
	}

	@Override
	public void onBufferReceived(byte[] arg0)
	{
	}

	@Override
	public void onEndOfSpeech()
	{
		if(this.isValid) {
			isWorking = false;
			this.parent.setTitle("SPEECH END");
			Button bSpeak = (Button)this.parent.findViewById(R.id.b_speak);
			bSpeak.setBackgroundResource(R.drawable.recordbutton);
		}
	}

	@Override
	public void onError(int e)
	{
	}

	@Override
	public void onEvent(int arg0, Bundle arg1)
	{
	}

	@Override
	public void onPartialResults(Bundle arg0)
	{
	}

	@Override
	public void onReadyForSpeech(Bundle arg0)
	{
		this.parent.setTitle("READY FOR SPEECH");
	}

	@Override
	public void onResults(Bundle data)
	{
		if(this.isValid) {
			matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			try {
				((WordSentenceModeActivity) parent).checkWords();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onRmsChanged(float arg0)
	{
	}
}
