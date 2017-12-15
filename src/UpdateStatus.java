import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class UpdateStatus {

	public static void main(String[] args) {
		Twitter twitter = TwitterFactory.getSingleton();
		try {
			Status status = twitter.updateStatus("-test-");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

}
