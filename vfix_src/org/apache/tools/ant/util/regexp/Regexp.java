package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;

public interface Regexp extends RegexpMatcher {
   int REPLACE_FIRST = 1;
   int REPLACE_ALL = 16;

   String substitute(String var1, String var2, int var3) throws BuildException;
}
