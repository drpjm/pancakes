package edu.gatech.grits.pancakes.client;

import javolution.util.FastList;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.*;
import edu.gatech.grits.pancakes.service.NetworkService;

/**
 * This scheduled task shares information with any neighbors on the network.
 * @author pmartin
 *
 */
public class LocalPoseMonitor extends Task {

	private FastList<String> neighborIds;
	private LocalPosePacket currLocalPose;
	
	public LocalPoseMonitor(){
		setDelay(1000l);
		neighborIds = new FastList<String>();
		currLocalPose = new LocalPosePacket();
		
		Callback<Packet> neighborCbk = new Callback<Packet>(){

			public void onMessage(Packet message) {
				
				if(message instanceof NetworkNeighborPacket){
					NetworkNeighborPacket pkt = (NetworkNeighborPacket) message;
					NetworkNeighbor n = pkt.getNeighbor();
					if(!pkt.isExpired()){
						// add
						neighborIds.add(n.getID());
					}
					else{
						// remove
						neighborIds.remove(n.getID());
					}
				}
				
			}
			
		};
		subscribe(NetworkService.NEIGHBORHOOD, neighborCbk);
		
		Callback<Packet> localCbk = new Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message instanceof LocalPosePacket){
					currLocalPose = (LocalPosePacket)message;
				}
			}
			
		};
		subscribe(CoreChannel.SYSTEM, localCbk);
		
	}
	
	public void close() {
		// TODO Auto-generated method stub

	}

	public final void run() {
		
//		Kernel.syslog.debug("Sending current pose to " + neighborIds.size() + " neighbor(s)");
		for(String id : neighborIds){
			NetworkPacket out = new NetworkPacket(Kernel.id, id);
			out.addPacket(currLocalPose);
			publish(CoreChannel.NETWORK, out);
		}

	}

}
