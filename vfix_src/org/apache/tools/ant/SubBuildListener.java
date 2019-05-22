package org.apache.tools.ant;

public interface SubBuildListener extends BuildListener {
   void subBuildStarted(BuildEvent var1);

   void subBuildFinished(BuildEvent var1);
}
