package org.apache.maven.doxia.util;

import org.apache.maven.doxia.parser.ParseException;

public interface ByLineSource {
   String getNextLine() throws ParseException;

   String getName();

   int getLineNumber();

   void ungetLine() throws IllegalStateException;

   void unget(String var1) throws IllegalStateException;

   void close();
}
