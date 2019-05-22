package org.apache.maven.doxia.parser;

import java.io.Reader;
import org.apache.maven.doxia.sink.Sink;

public interface Parser {
   String ROLE = Parser.class.getName();
   int UNKNOWN_TYPE = 0;
   int TXT_TYPE = 1;
   int XML_TYPE = 2;
   int JUSTIFY_CENTER = 0;
   int JUSTIFY_LEFT = 1;
   int JUSTIFY_RIGHT = 2;

   void parse(Reader var1, Sink var2) throws ParseException;

   int getType();
}
