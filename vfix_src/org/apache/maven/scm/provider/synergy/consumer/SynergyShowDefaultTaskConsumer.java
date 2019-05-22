package org.apache.maven.scm.provider.synergy.consumer;

import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class SynergyShowDefaultTaskConsumer extends AbstractConsumer {
   private int task;

   public int getTask() {
      return this.task;
   }

   public SynergyShowDefaultTaskConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Consume: " + line);
      }

      if (!line.contains("not set")) {
         this.task = Integer.parseInt(line.substring(0, line.indexOf(58)));
      }

   }
}
