package org.apache.velocity.app.event.implement;

import org.apache.commons.lang.StringEscapeUtils;

public class EscapeSqlReference extends EscapeReference {
   protected String escape(Object text) {
      return StringEscapeUtils.escapeSql(text.toString());
   }

   protected String getMatchAttribute() {
      return "eventhandler.escape.sql.match";
   }
}
