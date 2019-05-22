package org.apache.maven.doxia.module.apt;

public interface AptSource {
   String getNextLine() throws AptParseException;

   String getName();

   int getLineNumber();
}
