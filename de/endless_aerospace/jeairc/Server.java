package de.endless_aerospace.jeairc;

import java.util.ArrayList;


/**
 * A server. Currently not used.
 * @author richard
 * @version 0.1
 */
public class Server implements Participant {
	
	String name;
	ArrayList<Channel> chanList;
	
	/**
	 * create a server
	 * @param name the servername
	 */
	public Server(String name){
		this.name = name;
		chanList = new ArrayList<Channel>();
	}
	
	/**
	 * add a channel to the channel list.
	 * @param chan the channel
	 */
	synchronized void addChannel(Channel chan){
		if (!chanList.contains(chan)){
			chanList.add(chan);
		}
		//TODO: This part. Might be useless. Just for RAM-testing
		if (chanList.size() > 20){
			chanList.remove(chanList.size()-1);
		}
	}
	
	/**
	 * returns the channel list
	 * @return Channel list
	 */
	public ArrayList<Channel> getChanList(){
		return chanList;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
