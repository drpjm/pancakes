package edu.gatech.grits.pancakes.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Properties {
	
	private final String CFG_LOC = "/cfg/";

	private HashMap<String, String> propertyTable = null;
	
	public Properties(String fileName) {
		if(fileName.endsWith(".props"))
			this.loadFromPlainText(fileName);
		else
			System.err.println("error: invalid configuration file!");
	}
	
	public void loadFromPlainText(String fileName) {
		String fullFileName = this.getPathToFile(fileName);
		propertyTable = new HashMap<String, String>();	
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(fullFileName));
			
			while(br.ready()) {
				String line = br.readLine();
				StringTokenizer st = new StringTokenizer(line, "=");
				ArrayList<String> tokens = new ArrayList<String>(2);
				while(st.hasMoreTokens()) {
					tokens.add(st.nextToken());
				}
				propertyTable.put(tokens.get(0).trim(), tokens.get(1).trim());
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addProperty(String key, String value) {
		propertyTable.put(key, value);
	}

	public void debug() {
		for(String key : propertyTable.keySet())
			System.out.println(key + " = " + propertyTable.get(key));
	}
	
	
	public ArrayList<String> getDevices() {
		String result = "";
		ArrayList<String> list = new ArrayList<String>();
		for(String key : propertyTable.keySet()) {
			if(key.startsWith("service.devices.device.list"))
				result = propertyTable.get(key);
		}
		
		if(result != null) {
			StringTokenizer st = new StringTokenizer(result, ",");
			ArrayList<String> tokens = new ArrayList<String>(2);
			while(st.hasMoreTokens()) {
				list.add(st.nextToken());
			}
		}
		return list;
	}
	
	private final String getPathToFile(String fileName){
		String rootName = System.getProperty("user.dir");
		return rootName + this.CFG_LOC + fileName;
	}
	
	// accessors
	
	public String getID() {
		return propertyTable.get("kernel.id");
	}
	
	public String getBackend() {
		return propertyTable.get("service.devices.backend.type");
	}
	
	public long getDelay(String device) {
		String result = propertyTable.get("service.devices.device." + device + ".delay");
		if(result != null) {
			return new Long(result);
		} else {
			return 0l;
		}
	}
	
	public boolean isDevicesEnabled() {
		if(propertyTable.get("service.devices.enabled").equals("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getBackendPort() {
		//System.out.println(propertyTable.get("service.backend.port"));
		return new Integer(propertyTable.get("service.devices.backend.port"));
	}
	
	public boolean isNetworkEnabled() {
		if(propertyTable.get("service.network.enabled").equals("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getNetworkAddress() {
		return propertyTable.get("service.network.address");
	}
	
	public int getNetworkPort() {
		return new Integer(propertyTable.get("service.network.port"));
	}
	
	// log4j
	
	public boolean isLog4jEnabled() {
		if(propertyTable.get("service.log4j.enabled").equals("true"))
			return true;
		else
			return false;
	}
	
	// Twitter
	
	public boolean isTwitterEnabled() {
		if(propertyTable.get("service.twitter.enabled").equals("true"))
			return true;
		else
			return false;
	}
	
	public String getTwitterID() {
		return propertyTable.get("service.twitter.username");
	}
	
	public String getTwitterPasswd() {
		return propertyTable.get("service.twitter.password");
	}
}
