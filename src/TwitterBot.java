import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterBot {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query("source:twitter4j yusukey");
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
        }
	}
	
}
