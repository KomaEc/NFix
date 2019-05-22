package groovy.xml;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class FactorySupport {
   static Object createFactory(PrivilegedExceptionAction action) throws ParserConfigurationException {
      try {
         Object factory = AccessController.doPrivileged(action);
         return factory;
      } catch (PrivilegedActionException var4) {
         Exception e = var4.getException();
         if (e instanceof ParserConfigurationException) {
            throw (ParserConfigurationException)e;
         } else {
            throw new RuntimeException(e);
         }
      }
   }

   public static DocumentBuilderFactory createDocumentBuilderFactory() throws ParserConfigurationException {
      return (DocumentBuilderFactory)createFactory(new PrivilegedExceptionAction() {
         public Object run() throws ParserConfigurationException {
            return DocumentBuilderFactory.newInstance();
         }
      });
   }

   public static SAXParserFactory createSaxParserFactory() throws ParserConfigurationException {
      return (SAXParserFactory)createFactory(new PrivilegedExceptionAction() {
         public Object run() throws ParserConfigurationException {
            return SAXParserFactory.newInstance();
         }
      });
   }
}
