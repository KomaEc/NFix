package org.netbeans.lib.cvsclient.connection;

import java.io.IOException;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public interface Connection {
   LoggedDataInputStream getInputStream();

   LoggedDataOutputStream getOutputStream();

   void open() throws AuthenticationException, CommandAbortedException;

   void verify() throws AuthenticationException;

   void close() throws IOException;

   boolean isOpen();

   String getRepository();

   int getPort();

   void modifyInputStream(ConnectionModifier var1) throws IOException;

   void modifyOutputStream(ConnectionModifier var1) throws IOException;
}
