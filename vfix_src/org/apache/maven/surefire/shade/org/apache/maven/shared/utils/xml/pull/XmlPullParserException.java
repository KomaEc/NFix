package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.pull;

import java.io.IOException;
import org.xml.sax.SAXException;

public class XmlPullParserException extends RuntimeException {
   public XmlPullParserException(IOException e) {
      super(e);
   }

   public XmlPullParserException(SAXException e) {
      super(e);
   }

   public XmlPullParserException(String message) {
      super(message);
   }
}
