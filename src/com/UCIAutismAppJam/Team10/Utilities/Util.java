package com.UCIAutismAppJam.Team10.Utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.xeustechnologies.googleapi.spelling.Configuration;
import org.xeustechnologies.googleapi.spelling.Language;
import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellRequest;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

import android.content.Context;
import android.util.Log;

public class Util {
	
	public static ArrayList<String> readFileFromAssetFolder(Context context, String fileName) throws IOException {
		ArrayList<String> linesReadFromFile = new ArrayList<String>();
		InputStream inputStream = context.getAssets().open(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		while((line = br.readLine()) != null) {
			linesReadFromFile.add(line);
		}
		br.close();
		return linesReadFromFile;
	}
	
	
	/***
	 * The method will create the fileName to be created in the files in following path in the DDMS view.
	 * Data -> data -> (package name) -> view
	 * And we will be able to write data only to this location and not to the assets folder files.
	 * 
	 *  Note: This file writes a list of words to the output file.
	 * @return
	 */
	public static void writeToFilesFolder(Context context, String fileName, ArrayList<String> words) {
		try {
            DataOutputStream out = 
                    new DataOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            for (int i=0; i<words.size(); i++) {
                out.writeUTF(words.get(i));
            }
            out.close();
        } catch (IOException e) {
            Log.i("Data Input Sample", "I/O Error");
        }
	}
	
	
	/***
	 * This method writes a single word to the output file
	 * @param context
	 * @param fileName
	 * @param words
	 */
	public static void writeWordToFilesFolder(Context context, String fileName, String word) {
		try {
            DataOutputStream out = 
                    new DataOutputStream(context.openFileOutput(fileName, Context.MODE_APPEND));
            out.writeUTF(word);
            out.close();
        } catch (IOException e) {
            Log.i("Data Input Sample", "I/O Error");
        }
	}
	
	
	/**
	 * 
	 * @param context
	 * @param baseFileName -- The word/sentences list that is present in the assets folder - the one that we provide.
	 * @param customFileName -- The words/sentences list that has been added by the user.
	 * @return
	 */
	public static ArrayList<String> readWordsFromFileFolder(Context context, String baseFileName, String customFileName) {
		ArrayList<String> wordsList = new ArrayList<String>();
		wordsList = readFromFilesFolder(context, customFileName, wordsList);
		wordsList = readFromFilesFolder(context, baseFileName, wordsList);
		return wordsList;
	}
	
	public static ArrayList<String> readFromFilesFolder(Context context, String fileName, ArrayList<String> wordsList) {
		Integer lineNumber = 0;
		try {
			DataInputStream in = new DataInputStream(context.openFileInput(fileName));
            for (;;) {
            	String lineContents = in.readUTF();
            	// Ignoring the first number if it contains version number.
            	if(lineNumber == 0 && isNumber(lineContents)) {
            		lineNumber++;
            	} else {
            		wordsList.add(lineContents);
            	}
            }
        } catch (IOException e) {
            Log.i("Data Input Sample", "I/O Error");
        }
		return wordsList;
	}
	
	public static Boolean isNumber(String number) {
		Boolean isNum = true;
		for (char c : number.toCharArray()) {
			if (!Character.isDigit(c)) {
				isNum = false;
				break;
			}
		}
		return isNum;
	}
	
	
	public static int getFileVersionNumber(Context context, String fileName) {
		int versionNumber = 0;
		try {
			DataInputStream in = new DataInputStream(context.openFileInput(fileName));
			versionNumber = Integer.parseInt(in.readUTF());
		} catch (IOException e) {
			versionNumber = -1;
		}
		return versionNumber;
	}
	
	public static int getFileVerNumFromAssetFolder(Context context, String fileName) throws IOException {
		int versionNumber = 0;
		InputStream inputStream = context.getAssets().open(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = br.readLine();
		versionNumber = Integer.parseInt(line);
		br.close();
		return versionNumber;
	}
	
	
	public static void copyFromAssetsToFilesFolder(Context context, String baseFile, String customFileName) throws IOException {
		int assetFolderWordFileVersionNumber = getFileVerNumFromAssetFolder(context, baseFile);
		int filesFolderWordFileVersionNumber = getFileVersionNumber(context, customFileName);
		
		System.out.println(assetFolderWordFileVersionNumber + " --- " + filesFolderWordFileVersionNumber);

		if(filesFolderWordFileVersionNumber == -1 || filesFolderWordFileVersionNumber < assetFolderWordFileVersionNumber) {
			ArrayList<String> wordsList = readFileFromAssetFolder(context, baseFile);
			writeToFilesFolder(context, customFileName, wordsList);
		} else {
			System.out.println("No need to write this!!!");
			Log.i("Write Operation", "Need Not Write");
		}
	}
	
	
	public static void checkSpell(String text) {
		Configuration config = new Configuration();
		SpellChecker checker = new SpellChecker(config);
		checker.setOverHttps( true );
		checker.setLanguage( Language.ENGLISH );

		SpellRequest request = new SpellRequest();
		request.setText( "helloo helloo worlrd" );
		request.setIgnoreDuplicates( true ); // Ignore duplicates

		SpellResponse spellResponse = checker.check( request );

		if(spellResponse.getCorrections() == null)
			System.out.println("No spelling mistakes");
		else
			System.out.println(spellResponse.getCorrections().length);
	}

}
