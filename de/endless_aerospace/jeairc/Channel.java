package de.endless_aerospace.jeairc;

import java.util.ArrayList;

/**
 * A Channel
 * @author richard
 * @version 0.1
 */
public class Channel implements Participant{
	String name;
	String pass;
	String mode;
	ArrayList<User> users;
	
	/**
	 * Create a new Channel
	 * @param name The name of the Channel
	 */
	public Channel(String name){
		this.name = name;
		users = new ArrayList<User>();
	}
	/**
	 * Create a new Channel
	 * @param name the name of the channel
	 * @param pass the pass for the channel
	 */
	public Channel(String name, String pass){
		this(name);
		this.pass = pass;
	}
	
	/**
	 * Set the channel modes.
	 * This does not send anything to the irc server
	 * @param mode the mode
	 */
	public void setMode(String mode){
		this.mode = mode;
	}
	
	/**
	 * Get the channel modes.
	 * This does not query anything from the irc server.
	 * @return the mode
	 */
	public String getMode(){
		return mode;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Adds a user to the userlist
	 * @param user the user
	 */
	public void addUser(User user){
		users.add(user);
	}
	
	/**
	 * Gets the userlist
	 * @return the Userlist
	 */
	public ArrayList<User> getUserList(){
		return users;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null){
			return false;
		}
		if (o instanceof Channel){
			if (((Channel) o).toString().equals(this.toString())){
				return true;
			}
		}
		return false;
	}
}
