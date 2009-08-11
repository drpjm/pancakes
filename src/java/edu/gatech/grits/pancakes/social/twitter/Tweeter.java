package edu.gatech.grits.pancakes.social.twitter;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.util.Properties;
import winterwell.jtwitter.Twitter;

public class Tweeter {
	
	private Twitter client;
	private boolean isTweeting = false;
	private Tweetbase db = new Tweetbase();
	
	public Tweeter(Properties properties) {
		client = new Twitter(properties.getTwitterID(), properties.getTwitterPasswd());
		isTweeting = true;
	}
	
	public void actionTweet(String action) {
		System.out.println("Tweet: " + db.getActionUpdate(action));
		client.updateStatus(db.getActionUpdate(action));
	}
	
	public void tweet(String message) {
		Kernel.syslog.info("Tweet: " + message);
		Kernel.syslog.info("Message is " + message.length() + " character(s) long.");
		client.updateStatus(message);
	}
	
	public boolean isTweeting() {
		return isTweeting;
	}
	

}
