package org.apache.maven.doxia.parser.manager;

import java.util.Map;
import org.apache.maven.doxia.parser.Parser;

public class DefaultParserManager implements ParserManager {
   private Map parsers;

   public Parser getParser(String id) throws ParserNotFoundException {
      Parser parser = (Parser)this.parsers.get(id);
      if (parser == null) {
         throw new ParserNotFoundException("Cannot find parser with id = " + id);
      } else {
         return parser;
      }
   }
}
