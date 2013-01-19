package de.endless_aerospace.jeairc;

/**
 * Interface you need to implement to be
 * able to receive messages
 * @author richard
 * @version 0.1
 */
public interface MessageReceivable {

	/**
	 * received a ping
	 * @param server the server
	 * @param sender the sender
	 */
	public void recPing(JeaIRC server, User sender);
	
	/**
	 * received a private message
	 * @param server the server
	 * @param sender the sender
	 * @param receiver the receiver
	 * @param message the message
	 */
	public void recPrivMsg(JeaIRC server, User sender, Participant receiver, String message);
	
	/**
	 * received a CTCP message 
	 * @param sender the sender
	 * @param receiver the receiver
	 * @param ctcp the ctcp type
	 * @param message the message
	 */
	public void recCTCP(JeaIRC server, User sender, Participant receiver, String ctcp, String message);
	
	/**
	 * received a notice
	 * @param server the server
	 * @param sender the sender
	 * @param message the message
	 */
	public void recNotice(JeaIRC server, User sender, String message);
	
	/**
	 * received a join
	 * @param server the server
	 * @param user the user
	 * @param channel the channel
	 */
	public void recJoin(JeaIRC server, User user, Channel channel);
	
	/**
	 * received a part
	 * @param server the server
	 * @param user the user
	 * @param channel the channel
	 * @param message the part message
	 */
	public void recPart(JeaIRC server, User user, Channel channel, String message);
	
	/**
	 * received a channel mode change
	 * @param server the server
	 * @param user the user
	 * @param channel the channel
	 * @param mode the mode
	 */
	public void recChanMode(JeaIRC server, User user, Channel channel, String mode);
	
	/**
	 * received a channel user mode change
	 * @param server the server
	 * @param user the sender
	 * @param channel the channel
	 * @param mode the mode
	 * @param user2 the receiver
	 */
	public void recChanUserMode(JeaIRC server, User user, Channel channel, String mode, User user2);
	
	/**
	 * received a user mode change
	 * @param server the server
	 * @param user the user
	 * @param mode the mode
	 */
	public void recUserMode(JeaIRC server, User user, String mode);
	
	/**
	 * TODO: ADD SECOND USER
	 * received a kick
	 * @param server the server
	 * @param user the sender
	 * @param channel the channel
	 * @param reason the reason
	 */
	public void recKick(JeaIRC server, User user, Channel channel, String reason);
	
	/**
	 * received topic change
	 * @param server the server
	 * @param user the sender
	 * @param channel the channel
	 * @param topic the new topic
	 */
	public void recTopic(JeaIRC server, User user, Channel channel, String topic);
	
	/**
	 * received Quit message
	 * @param server the server
	 * @param user the sender
	 * @param message the message
	 */
	public void recQuit(JeaIRC server, User user, String message);
	
	/**
	 * received numeric reply to command. This might be
	 * an error. Please check the static numbers in ErrorReplies
	 * @param server the server
	 * @param numericReply the numeric reply.
	 */
	public void recReply(JeaIRC server, int numericReply);
	
	/**
	 * Received a raw message.
	 * @param server the server
	 * @param rawmessage the message
	 */
	public void recRaw(JeaIRC server, String rawmessage);
	
	/**
	 * received connected signal
	 * use this to initialize your stuff
	 * @param server the server
	 */
	public void connected(JeaIRC server);
}
