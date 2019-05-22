package org.netbeans.lib.cvsclient.connection;

import java.io.IOException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public interface ConnectionModifier {
   void modifyInputStream(LoggedDataInputStream var1) throws IOException;

   void modifyOutputStream(LoggedDataOutputStream var1) throws IOException;
}
