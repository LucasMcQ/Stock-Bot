package bots;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import persistent.PersistentTweets;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;


/* Class: TwitterBot
 * 
 * This twitter bot will search all of twitter looking for tweets with the hashtag '#pricebot'
 * when a user types in that hashtag and a stock name next to it, this program will reply to
 * the user that posted the tweet with the current price of the stock.
 * 
 * EX:
 * The user tweets:
 * 	GOOGL #pricebot
 * 
 * The program will then reply to the user with the current price of the stock google.
 * 
 * 
 * 
 */

public class TwitterBot {

	private static final String HASH_TAG_KEY = "#pricebot";
	private static final String PERSISTENT_TWEETS_FILE = "persistentTweets.txt";
	private static ArrayList<Long> allResponses = new ArrayList<Long>();

	public static void main(String[] args) {
		
		// We will check if there is a file with previous tweets to read from.
		// If there is a file, we will read that file into the allResponses list.
		File file = new File(PERSISTENT_TWEETS_FILE);
		if(file.exists()) {
			allResponses = PersistentTweets.readTweets(PERSISTENT_TWEETS_FILE);
		}

		while(true) {
			
			searchTweets();
			
			// Sleep for 15 seconds so Twitter does not get mad at us for API request
			try {
				TimeUnit.SECONDS.sleep(15);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	

	
	/* Method: searchTweets()
	 * 
	 * Description: This will search all of the tweets on twitter looking for a hashtag key.
	 * When the bot finds the hashtag "#pricebot" it will send the tweet to the examineStock()
	 * method where it will extract the stock information by the stock that was found in the
	 * tweet.
	 * 
	 */

	private static void searchTweets() {

		Twitter twitter = TwitterFactory.getSingleton(); // All tweets
		Query query = new Query(HASH_TAG_KEY); // tweets containing a substring
		QueryResult result = null; // all of the tweets containing the substring we are looking for
		
		
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String stockName = null; // the name of the stock
		
		for (Status status : result.getTweets()) {

			String tweet = status.getText(); // the current tweet as a string

			System.out.println(tweet);

			// if the tweet is a pricebot request
			if (tweet.contains(HASH_TAG_KEY)) {
				stockName = parseStockName(tweet);
				
				if (stockName != null) {
					
					String username = status.getUser().getScreenName(); // the username of the person tweeting
					long id = status.getId(); // the id of the tweet
					
					// If the current tweet has not been responded to, we will tweet to them
					// the price of their desired stock.
					if(!allResponses.contains(id)) {
						
						// add the tweet id to the list
						allResponses.add(id);
						
						// We will write the list to a file named persistentTweets.txt
						PersistentTweets.writeTweets(allResponses, PERSISTENT_TWEETS_FILE);
						
						String finalTweet = examineStock(stockName, username);
						// Reply to the user with the stock price.
						replyTweet(twitter, finalTweet);
						
					}

				}
			}
		}	
	}
	
	/**
	 * Replies to a tweet with stock name and price.
	 *
	 *@param twitter the twitterfactory instance
	 *@param tweet the string to reply with
	 */
	private static void replyTweet(Twitter twitter, String tweet) {
		try {
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* Method: examineStock()
	 * @param stockName -- the name of the stock
	 * 
	 * Description: This will do operations with the name of the stock passed in.
	 * This method will print out the current price of the stock.
	 */

	private static String examineStock(String stockName, String username) {
		
		Stock stock = null; // the stock information
		try {
			stock = YahooFinance.get(stockName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BigDecimal price = stock.getQuote().getPrice(); // the price of the stock
		
		String response =  stockName + " is $" + price + "   @" + username; 
		
		return response;
		
	}
	
	
	
	/* Method: parseStockName()
	 * @param tweet -- the contents of the tweet
	 * 
	 * 
	 * Description: This will parse the name of the stock out of the tweet, and return
	 * the name of the stock that we will do operations on.
	 * 
	 */
	
	private static String parseStockName(String tweet) {

		StringTokenizer st = new StringTokenizer(tweet); // tokenizes the tweet so we can separate key hashtag from stock name
		
		while (st.hasMoreTokens()) {

			String token = st.nextToken(); // the tokenized string

			// if the token is not the hash key, it will be the stock name
			if (!token.equals(HASH_TAG_KEY)) {
				return token;
			}

		}
		
		return null;

	}
	
}