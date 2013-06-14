package de.endless_aerospace.jeairc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Main Class of the IRC library
 * @author richard
 * @version 0.1
 */
public class JeaIRC implements Runnable{
	
	private String ip;
	private int port;
	private String nick;
	private String user;
	private String pass;
	private IRCParser parser;
	private LinkedBlockingDeque<String> messageBuffer;
	private PrintWriter output;
	private BufferedReader input;
	private Socket connection;
	private boolean authSent = false;
	private long bufferTimer = System.currentTimeMillis();
	private MessageReceivable receiver;
	private volatile boolean quit = false;
	private volatile boolean printOutput = false;
	private Server server;
	private long bufferTime = 1000;
	
	/**
	 * Creates a new IRC instance
	 * @param ip The Servers ip
	 * @param port the servers port
	 * @param nick your nick
	 * @param user your username. [username] [hostname] [servername] :[realname]"
	 */
	public JeaIRC(String ip, int port, String nick, String user, MessageReceivable receiver){
		
		this.ip = ip;
		this.port = port;
		this.nick = nick;
		this.user = user;
		parser = new IRCParser(this);
		messageBuffer = new LinkedBlockingDeque<String>();
		if (receiver == null){
			throw new NoReceiverSpecifiedException("No MessageReceiver specified when creating JeaIRC");
		}
		this.receiver = receiver;
		this.server = new Server(ip);
		
		
	}
	
	/**
	 * creates a new IRC instance
	 * @param ip The Servers ip
	 * @param port the servers port
	 * @param nick your nick
	 * @param user your username. [username] [hostname] [servername] :[realname]"
	 * @param pass the server password
	 */
	public JeaIRC(String ip, int port, String nick, String user, MessageReceivable receiver, String pass){
		
		this(ip, port, nick, user, receiver);
		this.pass = pass;
		
	}
	
	/**
	 * Creates a new IRC instance
	 * @param ip The Servers ip
	 * @param port the servers port
	 * @param nick your nick
	 * @param user a User object containing the information
	 */
	public JeaIRC(String ip, int port, String nick, User user, MessageReceivable receiver){
		
		this.ip = ip;
		this.port = port;
		this.nick = nick;
		this.user = user.getAuthString();
		parser = new IRCParser(this);
		messageBuffer = new LinkedBlockingDeque<String>();
		if (receiver == null){
			throw new NoReceiverSpecifiedException("No MessageReceiver specified when creating JeaIRC");
		}
		this.receiver = receiver;
		this.server = new Server(ip);	
		
	}
	
	/**
	 * creates a new IRC instance
	 * @param ip The Servers ip
	 * @param port the servers port
	 * @param nick your nick
	 * @param user a User object containing the information
	 * @param pass the server password
	 */
	public JeaIRC(String ip, int port, String nick, User user, MessageReceivable receiver, String pass){
		
		this(ip, port, nick, user, receiver);
		this.pass = pass;
		
	}
	
	@Override
	public void run() {
		try {
			connection = new Socket(ip, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (connection.isConnected()){
			try {
				output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
				input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			println("==Connected");
			while (!quit){
				try {
					auth();
					if (input.ready()){
						String message = input.readLine();
						auth();
						message.replace("\r", "");
						parser.receive(message);
						println(message);		
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendBuffer();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e){
					continue;
				}
			}
			try {
				connection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Could not connect to host");
		}
	}
	
	private void println(String message){
		if (printOutput){
			System.out.println(message);
		}
	}
	
	/**
	 * Quits the connection. This might take some time!
	 */
	public void quit(){
		quit = true;
	}
	
	/**
	 * send a auth signal
	 */
	private void auth(){
		if (!authSent){
			if (pass != null){
				println(">>Sending Pass");
				output.println("PASS "+pass+"\r");
			}
			println(">> Sending USER/NICK");
			println("NICK: "+nick+"\nUSER: "+user);
			output.println("USER "+user+"\r");
			output.println("NICK "+nick+"\r");
			output.flush();
			authSent = true;
		}
	}
	
	/**
	 * sends the message buffer.
	 * Throttles connection if it sends too many lines.
	 */
	private synchronized void sendBuffer(){
		long curTime = System.currentTimeMillis();
		if (curTime - bufferTimer > bufferTime){
			bufferTimer = curTime;
			if (messageBuffer.size() > 0){
				output.println(messageBuffer.poll());
				output.flush();
			}
		}
	}
	
	/**
	 * Changes the time the client waits to send messages if you spam them
	 * @param newTime the new time (in nanoseconds)
	 */
	public void setBufferTime(int newTime){
		bufferTime = newTime;
	}
	
	/**
	 * Enable println of everything the server sends
	 * For debug purposes mostly
	 * @param output print the output
	 */
	public void setOutput(boolean output){
		this.printOutput = output;
	}
	
	/**
	 * Is the output of server enabled
	 * @return Output enabled
	 */
	public boolean getOutput(){
		return printOutput;
	}
	
	/**
	 * Returns the Server
	 * @return the server
	 */
	public Server getServer(){
		return server;
	}
	
	/**
	 * send a single line
	 * @param line the line to send
	 */
	public synchronized void sendLine(String line){
		messageBuffer.add(line.concat("\r"));
	}
	
	/**
	 * received a ping
	 * @param sender the sender
	 */
	void recPing(User sender){
		messageBuffer.add("PONG "+sender.toString());
		receiver.recPing(this, sender);
	}
	
	/**
	 * received a private message
	 * @param sender the sender
	 * @param receiver the receiver
	 * @param message the message
	 */
	void recPrivMsg(User sender, Participant receiver, String message){
		this.receiver.recPrivMsg(this, sender, receiver, message);
	}
	
	/**
	 * received a CTCP message 
	 * @param sender the sender
	 * @param receiver the receiver
	 * @param ctcp the ctcp type
	 * @param message the message
	 */
	void recCTCP(User sender, Participant receiver, String ctcp, String message){
		this.receiver.recCTCP(this, sender, receiver, ctcp, message);
	}
	
	/**
	 * received a notice
	 * @param sender the sender
	 * @param message the message
	 */
	void recNotice(User sender, String message){
		receiver.recNotice(this, sender, message);
	}
	
	/**
	 * received a join
	 * @param user the user
	 * @param channel the channel
	 */
	void recJoin(User user, Channel channel){
		receiver.recJoin(this, user, channel);
	}
	
	/**
	 * received a part
	 * @param user the user
	 * @param channel the channel
	 */
	void recPart(User user, Channel channel, String message){
		receiver.recPart(this, user, channel, message);
	}
	
	/**
	 * received a channel mode change
	 * @param user the user who changes the mode 
	 * @param channel the channel
	 * @param mode the mode
	 */
	void recChanMode(User user, Channel channel, String mode){
		receiver.recChanMode(this, user, channel, mode);
	}
	
	/**
	 * received a channel user mode change
	 * @param user the sender 
	 * @param channel the channel
	 * @param mode the mode
	 * @param user2 the receiver
	 */
	void recChanUMode(User user, Channel channel, String mode, User user2){
		receiver.recChanUserMode(this, user, channel, mode, user2);
	}
	
	/**
	 * received a user mode change
	 * @param user the user
	 * @param mode the mode
	 */
	void recUserMode(User user, String mode){
		receiver.recUserMode(this, user, mode);
	}
	
	/**
	 * received a kick
	 * @param user the user
	 * @param channel the channel
	 * @param reason the reason
	 */
	void recKick(User user, Channel channel, String reason){
		receiver.recKick(this, user, channel, reason);
	}
	
	/**
	 * received a topic change
	 * @param user the user
	 * @param channel the channel
	 * @param topic the new topic
	 */
	void recTopic(User user, Channel channel, String topic){
		receiver.recTopic(this, user, channel, topic);
	}
	
	/**
	 * received a quit
	 * @param user the user
	 * @param message the message
	 */
	void recQuit(User user, String message){
		receiver.recQuit(this, user, message);
	}
	
	/**
	 * received a raw message
	 * @param rawMessage the message
	 */
	void recRaw(String rawMessage){
		receiver.recRaw(this, rawMessage);
	}
	
	/**
	 * received a numeric reply
	 * @param numericReply the reply
	 */
	void reply(int numericReply){
		receiver.recReply(this, numericReply);
	}
	
	/**
	 * client is connected
	 */
	void connected(){
		receiver.connected(this);
	}
}
