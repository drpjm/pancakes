package edu.gatech.grits.pancakes.net;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.MigrationPacket;
import edu.gatech.grits.pancakes.lang.NetworkNeighbor;
import edu.gatech.grits.pancakes.lang.NetworkNeighborPacket;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.ControlOption;
import edu.gatech.grits.pancakes.service.NetworkService;

public class TaskMigrator extends Task {

	private FastMap<String, NetworkNeighbor> neighbors;
	private FastMap<String, TaskProperties> tasksToMigrate;

	private final static Long MAX_TIME = 10000l;

	private final static String NO = "no";
	private final static String YES = "yes";
	private final static String SCHEDULE = "schedule";

	public TaskMigrator() {
		setDelay(500l);
		neighbors = new FastMap<String, NetworkNeighbor>().shared();
		//		taskCandidateAgents = new FastMap<String, FastList<CandidateAgent>>().shared();
		tasksToMigrate = new FastMap<String, TaskProperties>().shared();

		// handle migration messages
		Callback<Packet> migrationCbk = new Callback<Packet>(){

			@Override
			public void onMessage(Packet message) {

				if(message.getPacketType().equals(PacketType.MIGRATE)){

					MigrationPacket mp = (MigrationPacket) message;
					if( !tasksToMigrate.containsKey( mp.getTaskName() ) ){
						// initialize task entry
						tasksToMigrate.put(mp.getTaskName(), new TaskProperties(false));

						// transmit migration packet to neighbors
						for(NetworkNeighbor nn : neighbors.values()){
							NetworkPacket outPkt = new NetworkPacket(Kernel.getInstance().getId(), nn.getID());
							outPkt.addPayloadPacket(message);
							publish(CoreChannel.NETWORK, outPkt);
						}
					}					
				}

			}

		};
		subscribe(NetworkService.MIGRATE, migrationCbk);

		// maintain the neighborhood
		Callback<Packet> neighborCbk = new Callback<Packet>(){

			@Override
			public void onMessage(Packet message) {
				if(message.getPacketType().equals(PacketType.NEIGHBOR)){
					NetworkNeighborPacket np = (NetworkNeighborPacket) message;
					if(!np.isExpired()){
						String id = np.getNeighbor().getID();
						neighbors.put(id, np.getNeighbor());
					}
					else{
						neighbors.remove(np.getNeighbor().getID());
					}
				}
			}

		};
		subscribe(NetworkService.NEIGHBORHOOD, neighborCbk);

		// handle candidate agent negotiation here
		Callback<Packet> netCbk = new Callback<Packet>(){

			@Override
			public void onMessage(Packet message) {
				if(message instanceof NetworkPacket){

					Packet firstPayloadPkt = ((NetworkPacket)message).getPayloadPackets().getFirst();
					if( firstPayloadPkt.getPacketType().equals(PacketType.MIGRATE) ){
						// inspect network packet for migration stuff from neighbor
						MigrationPacket mp = (MigrationPacket) firstPayloadPkt;
						FastList<String> reqDevices = mp.getRequiredDevices();
						NetworkPacket responsePkt = new NetworkPacket(Kernel.getInstance().getId(), ((NetworkPacket)message).getSource());
						Packet answer;
						if(!hasReqDevices(reqDevices)){
							// send rejection to requesting agent
							answer = new Packet(NO);
						}
						else {
							// send a yes response with "cost" i.e. # current devices
							answer = new Packet(YES);
						}
						answer.add("taskName", mp.getTaskName());
						// TODO: need to have a better way to calculate cost -- this code is a HACK!
						answer.add("cost", Integer.toString(Kernel.getInstance().getDevices().size()));
						responsePkt.addPayloadPacket(answer);
						publish( CoreChannel.NETWORK, responsePkt );
					}
					// handle neighbor responses
					String packetType = firstPayloadPkt.getPacketType();
					if( packetType.equals(YES) || packetType.equals(NO) ){
						String agent = ((NetworkPacket)message).getSource();
						String taskName = firstPayloadPkt.get("taskName");
						Integer cost;
						// this code is mildly hacky
						if(packetType.equals(NO)){
							cost = null;
						}
						else {
							cost = Integer.valueOf(firstPayloadPkt.get("cost"));	
						}
						
						
						tasksToMigrate.get(taskName).addCandidate(new CandidateAgent(agent, cost, firstPayloadPkt.getPacketType()));
						
						// task is ready to migrate once all neighbors have checked in!
						if(tasksToMigrate.get(taskName).getCandidates().size() == neighbors.size() ){
							tasksToMigrate.get(taskName).setReady(true);
						}
					}
					
					if( firstPayloadPkt.getPacketType().equals(SCHEDULE) ){
						String taskName = firstPayloadPkt.get("taskName");
						Kernel.getInstance().getSyslog().debug(Kernel.getInstance().getId() + " scheduling " + taskName + "!");
						ControlPacket ctrl = new ControlPacket(ControlOption.START, taskName, TaskMigrator.class.getSimpleName());
						publish(CoreChannel.SYSCTRL, ctrl);
					}

				}
			}

		};
		subscribe(CoreChannel.SYSTEM, netCbk);

	}

	@Override
	public void run() {

		// run only if there are tasks to migrate
		if(!tasksToMigrate.isEmpty()){

//			System.out.println(tasksToMigrate.size() + " tasks to migrate: \n" + tasksToMigrate);

			for(FastMap.Entry<String, TaskProperties> taskEntry = tasksToMigrate.head(), end = tasksToMigrate.tail(); (taskEntry = taskEntry.getNext()) != end; ){

				//TODO: add in a timing limit to migrate
				if(System.currentTimeMillis() - taskEntry.getValue().getTimeStamp() > this.MAX_TIME){
					Kernel.getInstance().getSyslog().debug("Migration window expired! Remove task.");
					ControlPacket ctrl = new ControlPacket(ControlOption.STOP, taskEntry.getKey(), TaskMigrator.class.getSimpleName());
					publish(CoreChannel.SYSCTRL, ctrl);
					tasksToMigrate.remove(taskEntry.getKey());
					
				}
				// Tasks will migrate if a candidate is found. If not, task is canceled and removed.
				if(taskEntry.getValue().getReadyToMigrate()){

					// migrate and clear tasks that are assigned 
					final FastList<CandidateAgent> candidates = taskEntry.getValue().getCandidates();
					String lowestCostAgent = null;
					Integer lowestCost = Integer.MAX_VALUE;
					for(CandidateAgent currAgent : candidates){
						
						if(currAgent.getCost() != null){
							if(currAgent.getCost() < lowestCost){
								lowestCost = currAgent.getCost();
								lowestCostAgent = currAgent.getAgentName();
							}
						}
						
					}
					if(lowestCostAgent != null){
						NetworkPacket outPkt = new NetworkPacket(Kernel.getInstance().getId(), lowestCostAgent);
						Packet schedulePkt = new Packet(SCHEDULE);
						schedulePkt.add("taskName", taskEntry.getKey());
						outPkt.addPayloadPacket(schedulePkt);
						Kernel.getInstance().getSyslog().debug(this.getClass().getSimpleName() + " migrating " + taskEntry.getKey());
						publish(CoreChannel.NETWORK, outPkt);
					}
					else{
						Kernel.getInstance().getSyslog().debug(TaskMigrator.class.getSimpleName() + " cannot migrate " + taskEntry.getKey());
					}
								
					// stop local task
					ControlPacket ctrl = new ControlPacket(ControlOption.STOP, taskEntry.getKey(), TaskMigrator.class.getSimpleName());
					publish(CoreChannel.SYSCTRL, ctrl);
					tasksToMigrate.remove(taskEntry.getKey());
				}
			}

		}

	}

	private class TaskProperties {

		private Boolean readyToMigrate;
		private Long timeStamp;

		private FastList<CandidateAgent> candidates;

		public TaskProperties(){
			readyToMigrate = false;
			candidates = new FastList<CandidateAgent>();
			timeStamp = System.currentTimeMillis();
		}

		public TaskProperties(Boolean b){
			this();
			readyToMigrate = b;
		}

		public Long getTimeStamp() {
			return timeStamp;
		}
		
		public Boolean getReadyToMigrate() {
			return readyToMigrate;
		}

		public FastList<CandidateAgent> getCandidates() {
			return candidates;
		}

		public void setReady(Boolean val){
			readyToMigrate = val;
		}

		public void addCandidate(CandidateAgent ca){
			candidates.add(ca);
		}

		@Override
		public String toString() {
			String out = "";
			if(readyToMigrate){
				out += "READY";
			}
			else{
				out+= "NOT READY";
			}
			out += ", " + timeStamp;
			out += ", " + candidates;

			return out;
		}

	}

	private class CandidateAgent {
		private final String agentName;
		private final Integer cost;
		private final String reply;

		public CandidateAgent(String a, Integer c, String r){
			agentName = a;
			cost = c;
			reply = r;
		}

		public String getAgentName() {
			return agentName;
		}

		public Integer getCost() {
			return cost;
		}

		public String getReply() {
			return reply;
		}

		@Override
		public String toString() {
			String out = "";
			out += "(" + agentName + ", cost=" + cost + ", reply=" + reply +")";

			return out;
		}

	}

	private final boolean hasReqDevices(FastList<String> reqDev){
		// TODO: function needs to work for empty required devices
		if(Kernel.getInstance().getDevices().containsAll(reqDev) || reqDev.size() == 0){
			Kernel.getInstance().getSyslog().debug("I have required devices.");
			return true;
		}
		else{
			Kernel.getInstance().getSyslog().debug("I DO NOT have required devices.");
			return false;
		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}


}
