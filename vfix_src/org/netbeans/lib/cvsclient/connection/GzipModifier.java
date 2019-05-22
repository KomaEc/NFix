package org.netbeans.lib.cvsclient.connection;

import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public class GzipModifier implements ConnectionModifier {
   public void modifyInputStream(LoggedDataInputStream var1) throws IOException {
      GZIPInputStream var2 = new GZIPInputStream(var1.getUnderlyingStream());
      var1.setUnderlyingStream(var2);
   }

   public void modifyOutputStream(LoggedDataOutputStream var1) throws IOException {
      var1.setUnderlyingStream(new GZIPOutputStream(var1.getUnderlyingStream()));
   }
}
