package org.apache.tools.ant;

import java.util.EventListener;

public interface BuildListener extends EventListener {
   void buildStarted(BuildEvent var1);

   void buildFinished(BuildEvent var1);

   void targetStarted(BuildEvent var1);

   void targetFinished(BuildEvent var1);

   void taskStarted(BuildEvent var1);

   void taskFinished(BuildEvent var1);

   void messageLogged(BuildEvent var1);
}
