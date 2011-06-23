package edu.gatech.grits.pancakes.social.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import edu.gatech.grits.pancakes.core.Kernel;

public class Tweetbase {

	private HashMap<String, String> tweets = new HashMap<String, String>();
	private Random generator = new Random(System.currentTimeMillis());
	
	public Tweetbase() {
		Kernel.getInstance().getSyslog().info("Populating tweetBase");
		loadManualDatabase();
	}
	
	public void loadManualDatabase() {
		tweets.put("tweet.bootup.k3.0", "I'm awake now!");
		tweets.put("tweet.bootup.player.0", "Simulated hardware is better than no hardware.");
		tweets.put("tweet.shutdown.0", "I'm going to sleep.");
		tweets.put("tweet.shutdown.1", "Why am I being turned off?");
	}
	
	public String getActionUpdate(String action) {
		System.out.println("Action = " + action);
		ArrayList<String> matches = new ArrayList<String>();
		for(String key : tweets.keySet()) {
			System.out.println("Key = " + key);
			if(key.startsWith(action))
				matches.add(key);
		}
		if(matches.size() > 0) {
			int match = generator.nextInt(matches.size());
			return tweets.get(matches.get(match));
		}
		
		return null;
	}
}
