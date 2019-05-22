package org.apache.velocity.anakia;

import java.io.IOException;
import java.io.StringWriter;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OutputWrapper extends XMLOutputter {
   public OutputWrapper() {
   }

   public OutputWrapper(Format f) {
      super(f);
   }

   public String outputString(Element element, boolean strip) {
      StringWriter buff = new StringWriter();

      try {
         this.outputElementContent(element, buff);
      } catch (IOException var5) {
      }

      return buff.toString();
   }
}
