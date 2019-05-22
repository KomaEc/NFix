package org.jboss.util.propertyeditor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.jboss.util.NestedRuntimeException;
import org.jboss.util.StringPropertyReplacer;

public class InetAddressEditor extends TextPropertyEditorSupport {
   public Object getValue() {
      try {
         String text = this.getAsText();
         if (text == null) {
            return null;
         } else {
            if (text.startsWith("/")) {
               text = text.substring(1);
            }

            return InetAddress.getByName(StringPropertyReplacer.replaceProperties(text));
         }
      } catch (UnknownHostException var2) {
         throw new NestedRuntimeException(var2);
      }
   }
}
