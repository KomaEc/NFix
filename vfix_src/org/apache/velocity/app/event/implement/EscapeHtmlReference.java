package org.apache.velocity.app.event.implement;

import org.apache.commons.lang.StringEscapeUtils;

public class EscapeHtmlReference extends EscapeReference {
   protected String escape(Object text) {
      return StringEscapeUtils.escapeHtml(text.toString());
   }

   protected String getMatchAttribute() {
      return "eventhandler.escape.html.match";
   }
}
