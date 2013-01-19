package de.endless_aerospace.jeairc;

/**
 * Constants for the Error Replies
 * @author richard
 * @version 0.1
 */
public class ErrorReplies {
	
	private ErrorReplies(){
		
	}
	
	public final static int ERR_NOSUCHNICK = 401;
	public final static int ERR_NOSUCHSERVER = 402;
	public final static int ERR_NOSUCHCHANNEL = 403;
	public final static int ERR_CANNOTSENDTOCHAN = 404;
	public final static int ERR_TOOMANYCHANNELS = 405;
	public final static int ERR_WASNOSUCHNICK = 406;
	public final static int ERR_TOOMANYTARGETS = 407;
	public final static int ERR_NOORIGIN = 409;
	public final static int ERR_NORECIPIENT = 411;
	public final static int ERR_NOTEXTTOSEND = 412;
	public final static int ERR_NOTOPLEVEL = 413;
	public final static int ERR_WILDTOPLEVEL = 414;
	public final static int ERR_UNKNOWNCOMMAND = 421;
	public final static int ERR_NOMOTD = 422;
	public final static int ERR_NOADMININFO = 423;
	public final static int ERR_FILEERROR = 424;
	public final static int ERR_NONICKNAMEGIVEN = 431;
	public final static int ERR_ERRONEUSNICKNAME = 432;
	public final static int ERR_NICKNAMEINUSE = 433;
	public final static int ERR_NICKCOLLISION = 436;
	public final static int ERR_USERNOTINCHANNEL = 441;
	public final static int ERR_NOTONCHANNEL = 442;
	public final static int ERR_USERONCHANNEL = 443;
	public final static int ERR_NOLOGIN = 444;
	public final static int ERR_SUMMONDISABLED = 445;
	public final static int ERR_USERDISABLED = 446;
	public final static int ERR_NOTREGISTERED = 451;
	public final static int ERR_MEEDMOREPARAMS = 461;
	public final static int ERR_ALREADYREGISTERED = 462;
	public final static int ERR_NOPERMFORHOST = 463;
	public final static int ERR_PASSWDMISMATCH = 464;
	public final static int ERR_YOUREBANNEDCREEP = 465;
	public final static int ERR_KEYSET = 467;
	public final static int ERR_UNKNOWNMODE = 472;
	public final static int ERR_INVITEONLYCHAN = 473;
	public final static int ERR_BANNEDFROMCHAN = 474;
	public final static int ERR_BADCHANNELKEY = 475;
	public final static int ERR_NOPRIVILEGES = 481;
	public final static int ERR_CHANOPRIVSNEEDED = 482;
	public final static int ERR_CANTKILLSERVER = 483;
	public final static int ERR_NOOPERHOST = 491;
	public final static int ERR_UMODEUNKNOWNFLAG = 501;
	public final static int ERR_USERSDONTMATCH = 502;
}
