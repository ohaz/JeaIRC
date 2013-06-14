package de.endless_aerospace.jeairc;

/**
 * A user
 * @author richard
 * @version 0.1
 */
public class User implements Participant{
	
	String nick;
	String ident;
	String host;
	String realname;
	String hostname = new String("null");
	String servername = new String("null");
	
	public User(String nick){
		this.nick = nick;
	}
	
	/**
	 * Creates a new User
	 * @param nick
	 * @param realname
	 * @param username the username / ident
	 */
	public User(String nick, String realname, String username){
		this.nick = nick;
		this.realname = realname;
		this.ident = username;
	}
	
	/**
	 * Creates a new User
	 * @param nick the nick of the User
	 * @param username the username/ident
	 */
	public User(String nick, String username){
		this.nick = nick;
		this.ident = username;
	}
	
	
	/**
	 * get the auth User String
	 * @return the Auth User String
	 */
	String getAuthString(){
		StringBuilder sb = new StringBuilder();
		if (ident != null){
			sb.append(ident);
		} else {
			sb.append(nick);
		}
		sb.append(" "+hostname+" "+servername+" ");
		if (realname != null){
			sb.append(":"+realname);
		} else {
			sb.append(":"+nick);
		}
		return sb.toString();
	}
	
	/**
	 * Creates a new User
	 * @param name The nick / complete name
	 * @param complete wether name is only the nick or the complete name
	 * Complete Name: nick!ident@host
	 */
	public User(String name, boolean complete){
		if (complete){
			String[] split = name.split("[!@]");
			try {
				if (name.contains("!") && name.contains("@")){
					nick = split[0];
					ident = split[1];
					host = split[2];
				} else {
					nick = split[0];
				}
			}
			catch (ArrayIndexOutOfBoundsException e){
				throw new MalformedUserException("Wrong String. Should be of type: nick!ident@host");
			}
		} else {
			this.nick = name;
		}
	}
	
	/**
	 * Sets the hostname used in the auth function
	 * Most users won't need this
	 * @param hostname the hostname
	 */
	public void setHostname(String hostname){
		this.hostname = hostname;
	}
	
	/**
	 * Sets the servername used in the auth function
	 * Most users won't need this
	 * @param servername the servername
	 */
	public void setServername(String servername){
		this.servername = servername;
	}
	
	@Override
	public String toString() {
		return nick;
	}
	
	/**
	 * gets the Realname if known.
	 * Otherwise returns null
	 * @return realname
	 */
	public String getRealName(){
		return realname;
	}
	
	/**
	 * get the nick of the user
	 * NICK!*@* only the NICK is returned
	 * @return the nick
	 */
	public String getNick(){
		return nick;
	}
	
	/**
	 * get the ident of the user
	 * *!IDENT@* only the IDENT is returned
	 * @return the ident
	 */
	public String getIdent(){
		return ident;
	}
	
	/**
	 * get the host of the user
	 * *!*@HOST only the host is returned
	 * @return the host
	 */
	public String getHost(){
		return host;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null){
			return false;
		}
		if (o instanceof User){
			if (((User) o).getAuthString().equals(this.getAuthString())){
				return true;
			}
		}
		return false;
	}
}
