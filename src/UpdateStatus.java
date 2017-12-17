import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Example application that uses OAuth method to acquire access to your account.<br>
 * This application illustrates how to use OAuth method with Twitter4J.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class UpdateStatus {
    /**
     * Usage: java twitter4j.examples.tweets.UpdateStatus [text]
     *
     * @param args message
     */
    public static void main(String[] args) {
    	
        Twitter twitter = TwitterFactory.getSingleton();
        Status status = null;
		try {
			status = twitter.updateStatus("Lucas Test");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
    }
}