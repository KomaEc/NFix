package org.apache.maven.doxia;

import java.io.Reader;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.parser.Parser;
import org.apache.maven.doxia.parser.manager.ParserNotFoundException;
import org.apache.maven.doxia.sink.Sink;

public interface Doxia {
   String ROLE = Doxia.class.getName();

   void parse(Reader var1, String var2, Sink var3) throws ParserNotFoundException, ParseException;

   Parser getParser(String var1) throws ParserNotFoundException;
}
