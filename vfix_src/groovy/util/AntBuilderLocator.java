package groovy.util;

import org.xml.sax.Locator;

class AntBuilderLocator implements Locator {
   public int getColumnNumber() {
      return 0;
   }

   public int getLineNumber() {
      return 0;
   }

   public String getPublicId() {
      return "";
   }

   public String getSystemId() {
      return "";
   }
}
