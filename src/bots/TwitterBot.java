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

public class TwitterBot {

	private static final String HASH_TAG_KEY = "#pricebot";

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		// The factory instance is re-useable and thread safe.
		Twitter twitter = TwitterFactory.getSingleton();
		Query query = new Query(HASH_TAG_KEY);
		QueryResult result = null;
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
			}

		}
	}

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