package org.apache.commons.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.Properties;

class LogFactory$5 implements PrivilegedAction {
   // $FF: synthetic field
   private final URL val$url;

   LogFactory$5(URL var1) {
      this.val$url = var1;
   }

   public Object run() {
      try {
         InputStream stream = this.val$url.openStream();
         if (stream != null) {
            Properties props = new Properties();
            props.load(stream);
            stream.close();
            return props;
         }
      } catch (IOException var3) {
         if (LogFactory.isDiagnosticsEnabled()) {
            LogFactory.access$000("Unable to read URL " + this.val$url);
         }
      }

      return null;
   }
}
