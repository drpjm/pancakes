package edu.gatech.grits.pancakes.devices.backend;

import org.swig.k3i.k3i;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.StringTokenizer;
//import edu.gatech.grits.pancakes.lang.BatteryPacket;
//import edu.gatech.grits.pancakes.lang.IRPacket;
//import edu.gatech.grits.pancakes.lang.Packet;
//import edu.gatech.grits.pancakes.lang.SonarPacket;
//import flanagan.interpolation.CubicSpline;

public class K3Backend extends Backend {

	static {
		System.loadLibrary("k3i");
	}
	
//	CubicSpline cinterp;
	
	public K3Backend() {
		super("k3");
		
		k3i.k3Initialize();
		
//		double[] distance = { 0.0, 0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 25.0 };
//		double[] measurement = { 4000.0, 3945.0, 1342.0, 163.0, 38.0, 22.0, 12.0, 0.0 };
//		
//		cinterp = new CubicSpline(measurement, distance);
	}

	@Override
	public final Object getHandle() {
		// since the K3 backend is really a swig library, do nothing
		return null;
	}
	
//	public ArrayList<Packet> update() {
//		return parse(execute("k3i -f"));
//	}
	
	public final void update() {
		k3i.k3Update();
	}
	
	public final void close() {
		k3i.motorsStop(); // make sure we are stopped
	}

//	public synchronized ArrayList<String> execute(String command) {
//
//		String s = null;
//		ArrayList<String> tokens = new ArrayList<String>();
//		
//		try {
//			Process p = Runtime.getRuntime().exec(command);
//			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            // read the output from the command
//            
//			
//            while ((s = stdInput.readLine()) != null) {
//            	tokens.add(s);
//            }
//        } catch (IOException e) {
//            System.out.println("exception happened - here's what I know: ");
//            e.printStackTrace();
//            System.exit(-1);
//        }
//        
//        
//        return tokens;
//	}
//	
//	public ArrayList<Packet> parse(ArrayList<String> tokens) {
//		ArrayList<Packet> packets = new ArrayList<Packet>(3);
//		ArrayList<String> sonars = new ArrayList<String>(5);
//		
//		for(String s : tokens) {
//			if(s.startsWith("battery")) {
//				packets.add(parseBattery(s));
//			} else if (s.startsWith("ir")) {
//				packets.add(parseIR(s));
//			} else if (s.startsWith("sonar")) {
//				sonars.add(s);
//			}
//		}
//		
//		packets.add(parseSonars(sonars));
//		
//		return packets;
//	}
//	
//	public SonarPacket parseSonars(ArrayList<String> sonars) {
//		SonarPacket pkt = new SonarPacket();
//		
//		for(String s : sonars) {
//			StringTokenizer st = new StringTokenizer(s.trim(), ",");
//			ArrayList<String> tokens = new ArrayList<String>(4);
//			while(st.hasMoreTokens()) {
//				tokens.add(st.nextToken().trim());
//			}
//			
//			pkt.add(tokens.get(0), new Float(tokens.get(4)));
//			
//		}
//		return pkt;
//	}
//	
//	public BatteryPacket parseBattery(String input) {		
//		BatteryPacket pkt = new BatteryPacket();
//		
//		StringTokenizer st = new StringTokenizer(input.trim(), ",");
//		ArrayList<String> tokens = new ArrayList<String>(8);
//		while(st.hasMoreTokens()) {
//			tokens.add(st.nextToken());
//		}
//		
//		pkt.setVoltage(new Float(tokens.get(2)));
//		pkt.setCurrent(new Float(tokens.get(3)));
//		
//		return pkt;
//	}
//	
//	public Packet parseIR(String input) {
//		StringTokenizer st = new StringTokenizer(input.trim(), ",");
//		ArrayList<String> tokens = new ArrayList<String>(13);
//		while(st.hasMoreTokens()) {
//			tokens.add(st.nextToken());
//		}
//		
//		IRPacket pkt = new IRPacket(); //9);
//		
//		float ranges[] = new float[9];
//		
//		for(int i=3; i<12; i++) {
//			float reading = new Float(tokens.get(i));
//			ranges[i-3] = (float) cinterp.interpolate(reading);
//			ranges[i-3] /= 100.0f;
//		}
//		
//		pkt.setIRReadings(ranges);
//		
//		return pkt;
//	}
	
	
}
