package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public interface SecureProtocolSocketFactory extends ProtocolSocketFactory {
   Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException, UnknownHostException;
}
