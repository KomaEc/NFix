package org.apache.maven.scm.provider.jazz.command.consumer;

import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;

public class DebugLoggerConsumer extends AbstractRepositoryConsumer {
   private StringBuilder content = new StringBuilder();
   private String ls = System.getProperty("line.separator");

   public DebugLoggerConsumer(ScmLogger logger) {
      super((ScmProviderRepository)null, logger);
   }

   public void consumeLine(String line) {
      super.consumeLine(line);
      this.content.append(line).append(this.ls);
   }

   public String getOutput() {
      return this.content.toString();
   }
}
