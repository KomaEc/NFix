package org.apache.maven.doxia;

import java.io.Reader;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.parser.Parser;
import org.apache.maven.doxia.parser.manager.ParserManager;
import org.apache.maven.doxia.parser.manager.ParserNotFoundException;
import org.apache.maven.doxia.sink.Sink;

public class DefaultDoxia implements Doxia {
   private ParserManager parserManager;

   public void parse(Reader source, String parserId, Sink sink) throws ParserNotFoundException, ParseException {
      Parser parser = this.parserManager.getParser(parserId);
      parser.parse(source, sink);
   }

   public Parser getParser(String parserId) throws ParserNotFoundException {
      return this.parserManager.getParser(parserId);
   }
}
