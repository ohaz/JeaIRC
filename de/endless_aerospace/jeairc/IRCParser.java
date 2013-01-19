package de.endless_aerospace.jeairc;

/**
 * The parser. Parses incoming and outgoing messages
 * @author richard
 * @version 0.1
 */
public class IRCParser {
	
	JeaIRC irc;
	/**
	 * Create a new parser
	 * @param jeaIRC the IRC connection
	 */
	public IRCParser(JeaIRC jeaIRC) {
		this.irc = jeaIRC;
	}

	/**
	 * creates a new Private Message
	 * @param receiver The receiver
	 * @param message the message
	 * @return the complete private message
	 */
	public static String createPrivMsg(Participant receiver, String message){
		return new String("PRIVMSG "+receiver.toString()+" :"+message);
	}
	
	/**
	 * creates a new Private Message
	 * @param receiver The nick/channel as a String
	 * @param message the message
	 * @return the complete private message
	 */
	public static String createPrivMsg(String receiver, String message){
		return new String("PRIVMSG "+receiver+" :"+message);
	}
	
	/**
	 * Creates a new Quit message
	 * @param reason the reason
	 * @return the complete quit message
	 */
	public static String createQuit(String reason){
		return new String("QUIT :"+reason);
	}
	
	/**
	 * Creates a new Part message
	 * @param chan The channel to part
	 * @param reason the reason
	 * @return the complete part message
	 */
	public static String createPart(Channel chan, String reason){
		return new String("PART "+chan.name+" :reason");
	}
	
	
	/**
	 * creates a new Pong signal
	 * @param receiver the receiver
	 * @return the complete Pong signal
	 */
	public static String createPong(Participant receiver){
		return new String("PONG "+receiver.toString());
	}
	
	/**
	 * Create a new join message
	 * @param chan the channel
	 * @return the complete join message
	 */
	public static String createJoin(Channel chan){
		if (chan.pass != null){
			return new String("JOIN "+chan.name+" "+chan.pass);
		}
		return new String("JOIN "+chan.name);
	}
	
	
	/**
	 * Creates a channel user mode message
	 * @param chan The channel
	 * @param u the user
	 * @param mode the mode
	 * @return the complete channelusermode message
	 */
	public static String createChanUMode(Channel chan, User u, String mode){
		return new String("MODE "+chan+ " "+ mode +" "+u.getNick());
	}
	/**
	 * Creates a new kick message
	 * @param chan The Channel to kick from
	 * @param u the user to kick
	 * @param reason the reason (can be null)
	 * @return the complete kick message
	 */
	public static String createKick(Channel chan, User u, String reason){
		if (reason != null){
			return new String("KICK "+chan+" "+u+ ":"+reason);
		} else {
			return new String("KICK "+chan+" "+u); 
		}
	}
	/**
	 * Creates a new ACTION (/me) message
	 * @param chan The Channel to send to
	 * @param message the /me message
	 * @return the complete ACTION message
	 */
	public static String createAction(Channel chan, String message){
		return new String("PRIVMSG "+chan.name+" :\001ACTION "+message+"\001");
	}
	
	public static String createCTCP(Participant rec, String ctcp, String message){
		return new String("PRIVMSG "+rec.toString()+" :\001"+ctcp+message+"\001");
	}
	
	public static String stripIllegal(String message){
		String newmessage = new String(message);
		newmessage.replace("\r", "");
		newmessage.replace("\n", "");
		newmessage.replace("/", "");
		return newmessage;
	}
	
	/**
	 * receives and parses a message
	 * @param content the message
	 */
	void receive(String content){
		irc.recRaw(content);
		String[] split = content.split(" ");
		//Messages with sender:
		if (split.length > 1){
			int command;
			try{
				command = Integer.parseInt(split[1]);
				//Server Command
				if (command == 001){
					irc.connected();
				} else if (command <= 1000 && command >= 200){
					irc.reply(command);
				}
			}
			catch (NumberFormatException e){
				//Normal Message
				if (split[1].equals("PRIVMSG")){
					User sender = new User(split[0].substring(1), true);
					Participant receiver;
					if (split[2].startsWith("#")){
						receiver = new Channel(split[2]);
						irc.getServer().addChannel((Channel)receiver);
					} else {
						receiver = new User(split[2]);
					}
					StringBuilder message = new StringBuilder(split[3].substring(1));
					for (int i = 4; i < split.length; ++i){
						message.append(" "+split[i]);
					}
					if (message.toString().startsWith("\001")){
						String ctcp = message.toString().substring(message.indexOf("\001")+1, message.indexOf(" "));
						String mess = message.toString().substring(message.indexOf(" ")+1, message.lastIndexOf("\001"));
						irc.recCTCP(sender, receiver, ctcp, mess);
					} else {
						irc.recPrivMsg(sender, receiver, message.toString());
					}
				} else if (split[1].equals("NOTICE")){
					User sender = new User(split[0].substring(1), true);
					StringBuilder message = new StringBuilder(split[3].substring(1));
					for (int i = 4; i < split.length; ++i){
						message.append(" "+split[i]);
					}
					irc.recNotice(sender, message.toString());
				} else if (split[1].equals("JOIN")){
					User joiner = new User(split[0].substring(1), true);
					Channel joined = new Channel(split[2]);
					irc.getServer().addChannel((Channel)joined);
					irc.recJoin(joiner, joined);
				} else if (split[1].equals("PART")){
					User parter = new User(split[0].substring(1), true);
					Channel parted = new Channel(split[2]);
					irc.getServer().addChannel((Channel)parted);
					String partMessage = null;
					if (split.length > 3){
						partMessage = split[3];
					}
					irc.recPart(parter, parted, partMessage);
				} else if (split[1].equals("MODE")){
					User user = new User(split[0].substring(1), true);
					Channel chan = null;
					if (IRCParser.isChannel(split[2])){
						chan = new Channel(split[2]);
						irc.getServer().addChannel((Channel)chan);
						String mode = new String(split[3]);
						if (split.length > 4){
							User user2 = new User(split[4]);
							irc.recChanUMode(user, chan, mode, user2);
						} else {
							irc.recChanMode(user, chan, mode);
						}
					} else {
						String mode = new String(split[2]);
						irc.recUserMode(user, mode);
					}
				} else if (split[1].equals("TOPIC")){
					User user = new User(split[0].substring(1), true);
					Channel chan = new Channel(split[2]);
					irc.getServer().addChannel((Channel)chan);
					String topic = new String(split[3].substring(1));
					irc.recTopic(user, chan, topic);
				} else if (split[1].equals("KICK")){
					User user = new User(split[0].substring(1), true);
					Channel chan = new Channel(split[2]);
					irc.getServer().addChannel((Channel)chan);
					String reason = null;
					if (split.length > 3){
						reason = new String(split[3].substring(1));
					}
					irc.recKick(user, chan, reason);
				}
			}
		}
		//Messages without a sender:
		if (split[0].equals("PRIVMSG")){
			
		} else if (split[0].equals("NOTICE")){
			
		} else if (split[0].equals("PING")){
			String answer = split[1];
			irc.recPing(new User(answer.substring(1)));
			//messageBuffer.add(IRCParser.createPong(new User(split[1])));
			
		} else if (split[0].equals("PONG")){
			
		} else if (split[0].equals("ERROR")){
			
		} else if (split[0].equals("KICK")){
			
		} else if (split[0].equals("TOPIC")){
			
		} else if (split[0].equals("MODE")){
			
		} else if (split[0].equals("PART")){
			
		} else if (split[0].equals("JOIN")){
			
		} else if (split[0].equals("QUIT")){
			
		}
	}
	
	/**
	 * Checks if the string is a channel
	 * @param s the string to test
	 * @return is Channel
	 */
	public static boolean isChannel(String s){
		//perhaps change to s.contains
		if (s.startsWith("#") || s.startsWith("&")){
			return true;
		}
		return false;
	}
}
