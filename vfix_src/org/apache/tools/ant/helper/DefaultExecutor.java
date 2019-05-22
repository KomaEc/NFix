package org.apache.tools.ant.helper;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Executor;
import org.apache.tools.ant.Project;

public class DefaultExecutor implements Executor {
   private static final SingleCheckExecutor SUB_EXECUTOR = new SingleCheckExecutor();

   public void executeTargets(Project project, String[] targetNames) throws BuildException {
      BuildException thrownException = null;

      for(int i = 0; i < targetNames.length; ++i) {
         try {
            project.executeTarget(targetNames[i]);
         } catch (BuildException var6) {
            if (!project.isKeepGoingMode()) {
               throw var6;
            }

            thrownException = var6;
         }
      }

      if (thrownException != null) {
         throw thrownException;
      }
   }

   public Executor getSubProjectExecutor() {
      return SUB_EXECUTOR;
   }
}
