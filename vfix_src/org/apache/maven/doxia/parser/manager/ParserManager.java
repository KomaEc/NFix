package org.apache.maven.doxia.parser.manager;

import org.apache.maven.doxia.parser.Parser;

public interface ParserManager {
   String ROLE = ParserManager.class.getName();

   Parser getParser(String var1) throws ParserNotFoundException;
}
