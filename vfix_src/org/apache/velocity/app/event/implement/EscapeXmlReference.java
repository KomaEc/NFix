package org.apache.velocity.app.event.implement;

import org.apache.commons.lang.StringEscapeUtils;

public class EscapeXmlReference extends EscapeReference {
   protected String escape(Object text) {
      return StringEscapeUtils.escapeXml(text.toString());
   }

   protected String getMatchAttribute() {
      return "eventhandler.escape.xml.match";
   }
}
