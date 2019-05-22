package org.jboss.net.sockets;

import java.rmi.Remote;

public interface RMIMultiSocket extends Remote {
   Object invoke(long var1, Object[] var3) throws Exception;
}
