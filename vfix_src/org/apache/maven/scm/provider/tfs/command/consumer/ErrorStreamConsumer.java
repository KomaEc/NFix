package org.apache.maven.scm.provider.tfs.command.consumer;

import org.codehaus.plexus.util.cli.CommandLineUtils;

public class ErrorStreamConsumer extends CommandLineUtils.StringStreamConsumer {
   private boolean fed = false;

   public void consumeLine(String line) {
      this.fed = true;
      super.consumeLine(line);
   }

   public boolean hasBeenFed() {
      return this.fed;
   }
}
