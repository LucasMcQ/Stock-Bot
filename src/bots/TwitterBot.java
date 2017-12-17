package bots;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.StringTokenizer;

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

	public static void main(String[] args) {

		searchTweets();

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

			System.out.println(status.getText());

			// if the tweet is a pricebot request
			if (tweet.contains("#pricebot")) {
				stockName = parseStockName(tweet);
				
				if (stockName != null) {

					examineStock(stockName);

				}
			}

		}
		
	}
	
	
	
	/* Method: examineStock()
	 * @param stockName -- the name of the stock
	 * 
	 * Description: This will do operations with the name of the stock passed in.
	 * This method will print out the current price of the stock.
	 * 
	 */

	private static void examineStock(String stockName) {
		
		Stock stock = null; // the stock information
		try {
			stock = YahooFinance.get(stockName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BigDecimal price = stock.getQuote().getPrice(); // the price of the stock

		System.out.println("STOCK NAME: " + stockName + " price = " + price);
		
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