package org.apache.tools.ant.util.regexp;

import java.util.Vector;
import org.apache.tools.ant.BuildException;

public interface RegexpMatcher {
   int MATCH_DEFAULT = 0;
   int MATCH_CASE_INSENSITIVE = 256;
   int MATCH_MULTILINE = 4096;
   int MATCH_SINGLELINE = 65536;

   void setPattern(String var1) throws BuildException;

   String getPattern() throws BuildException;

   boolean matches(String var1) throws BuildException;

   Vector getGroups(String var1) throws BuildException;

   boolean matches(String var1, int var2) throws BuildException;

   Vector getGroups(String var1, int var2) throws BuildException;
}
