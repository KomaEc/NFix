package org.testng.remote.strprotocol;

import java.io.IOException;
import java.net.SocketTimeoutException;

public interface IMessageSender {
   void connect() throws IOException;

   void initReceiver() throws SocketTimeoutException;

   void sendMessage(IMessage var1) throws Exception;

   IMessage receiveMessage() throws Exception;

   void shutDown();

   void sendAck();

   void sendStop();
}
