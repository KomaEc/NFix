package org.apache.tools.ant;

public interface Executor {
   void executeTargets(Project var1, String[] var2) throws BuildException;

   Executor getSubProjectExecutor();
}
