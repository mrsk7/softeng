package net.java.sip.communicator.sip.event;

import java.util.*;

/**
 * <p>Title: SIP COMMUNICATOR</p>
 * <p>Description:JAIN-SIP Audio/Video phone application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Organisation: LSIIT laboratory (http://lsiit.u-strasbg.fr) </p>
 * <p>Network Research Team (http://www-r2.u-strasbg.fr))</p>
 * <p>Louis Pasteur University - Strasbourg - France</p>
 * @author Emil Ivov (http://www.emcho.com)
 * @version 1.1
 *
 */
public interface CommunicationsListener
    extends EventListener
{
    public void callReceived(CallEvent evt);

    public void callRejectedLocally(CallRejectedEvent evt);

    public void callRejectedRemotely(CallRejectedEvent evt);

    public void messageReceived(MessageEvent evt);

    public void receivedUnknownMessage(UnknownMessageEvent evt);

    public void communicationsErrorOccurred(CommunicationsErrorEvent evt);

    public void registered(RegistrationEvent evt);

    public void registering(RegistrationEvent evt);

    public void unregistering(RegistrationEvent evt);

    public void unregistered(RegistrationEvent evt);
//    public void callAccepted(CommunicationsEvent evt);
//    public void callRinging(CommunicationsEvent evt);
//    public void callTrying(CommunicationsEvent evt);
//    public void callEnded(CommunicationsEvent evt);

	public void receivedForwardMessage(String newTo);

	public void policesReceived(String[] policyList);
}
