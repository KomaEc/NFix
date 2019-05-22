package org.apache.velocity.app.event.implement;

import org.apache.commons.lang.StringEscapeUtils;

public class EscapeJavaScriptReference extends EscapeReference {
   protected String escape(Object text) {
      return StringEscapeUtils.escapeJavaScript(text.toString());
   }

   protected String getMatchAttribute() {
      return "eventhandler.escape.javascript.match";
   }
}
