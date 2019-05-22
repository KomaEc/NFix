package org.apache.maven.doxia.parser;

import org.apache.maven.doxia.markup.TextMarkup;

public abstract class AbstractTextParser extends AbstractParser implements TextMarkup {
   public final int getType() {
      return 1;
   }
}
