package groovy.xml.dom;

import groovy.xml.XmlUtil;
import java.io.OutputStream;
import org.w3c.dom.Element;

public class DOMUtil {
   /** @deprecated */
   public static String serialize(Element element) {
      return XmlUtil.serialize(element);
   }

   /** @deprecated */
   public static void serialize(Element element, OutputStream os) {
      XmlUtil.serialize(element, os);
   }
}
