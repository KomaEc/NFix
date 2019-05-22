package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.taskdefs.email.EmailTask;

public class SendEmail extends EmailTask {
   /** @deprecated */
   public void setMailport(Integer value) {
      this.setMailport(value);
   }
}
