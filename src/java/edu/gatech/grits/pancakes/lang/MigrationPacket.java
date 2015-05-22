package edu.gatech.grits.pancakes.lang;

import edu.gatech.grits.pancakes.client.*;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.devices.*;
import javolution.util.FastList;

public class MigrationPacket extends Packet {

	private static final long serialVersionUID = 1847201394792917195L;

	
	public MigrationPacket() {
		super(PacketType.MIGRATE);
		this.setTaskName("");
		this.setRequiredDevices(new FastList<String>());
	}

	public final void setTaskName(String name){
		this.add("taskname", name);
	}
	
	public final String getTaskName(){
		return this.get("taskname");
	}
	
	public final void setRequiredDevices(FastList<String> depends){
		String list = "";
		for(int i = 0; i < depends.size(); i++){
			if(i != depends.size()-1){
				list += depends.get(i) + ",";
			}
			else{
				list += depends.get(i);
			}
		}
		this.add("required", list);
	}
	
	public final FastList<String> getRequiredDevices(){
	
		String[] depends = this.get("required").split(",");

		FastList<String> output = new FastList<String>();
		for(String s : depends){
			if(!s.isEmpty()){
				output.add(s);
			}
		}
		
		return output;
	}
	
	public static void main(String[] args){
		MigrationPacket mp = new MigrationPacket();
		
		mp.setTaskName(ControlTester.class.getSimpleName());
		FastList<String> d = new FastList<String>();
		d.add(LocalPoseDevice.class.getSimpleName());
		d.add(SonarDevice.class.getSimpleName());
		d.add(MotorDevice.class.getSimpleName());
		
		mp.setRequiredDevices(d);
		
		mp.debug();
		
		System.out.println("Get required devices: \n" + mp.getRequiredDevices());
		
	}

}
