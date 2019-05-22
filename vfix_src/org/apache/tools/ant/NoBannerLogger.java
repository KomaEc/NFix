package org.apache.tools.ant;

import org.apache.tools.ant.util.StringUtils;

public class NoBannerLogger extends DefaultLogger {
   protected String targetName;

   public void targetStarted(BuildEvent event) {
      this.targetName = event.getTarget().getName();
   }

   public void targetFinished(BuildEvent event) {
      this.targetName = null;
   }

   public void messageLogged(BuildEvent event) {
      if (event.getPriority() <= this.msgOutputLevel && null != event.getMessage() && !"".equals(event.getMessage().trim())) {
         if (null != this.targetName) {
            this.out.println(StringUtils.LINE_SEP + this.targetName + ":");
            this.targetName = null;
         }

         super.messageLogged(event);
      }
   }
}
