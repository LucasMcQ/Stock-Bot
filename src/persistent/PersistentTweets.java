package persistent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PersistentTweets {
	
	public static void writeTweets(ArrayList<Long> tweets, String file) {
		
		try {
			FileOutputStream bytesToDisk = new FileOutputStream(file, false);
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);	
			outFile.writeObject(tweets);
			outFile.close();
		} 
		catch (FileNotFoundException e) {}
		catch (IOException io) {}
		
	}

	
	@SuppressWarnings("unchecked")
	public static ArrayList<Long> readTweets(String file) {
		
		ArrayList<Long> list = null;
		
		try {
			FileInputStream rawBytes = new FileInputStream(file);
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			// Need to cast Objects to the class they are known to be
			list = (ArrayList<Long>) inFile.readObject();
			inFile.close();
		}
		catch (Exception e) {}
		
		return list;
		
	}

}