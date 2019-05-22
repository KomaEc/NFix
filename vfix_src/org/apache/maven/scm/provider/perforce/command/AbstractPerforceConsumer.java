package org.apache.maven.scm.provider.perforce.command;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.codehaus.plexus.util.cli.StreamConsumer;

public abstract class AbstractPerforceConsumer implements StreamConsumer {
   private StringWriter out = new StringWriter();
   protected PrintWriter output;

   public AbstractPerforceConsumer() {
      this.output = new PrintWriter(this.out);
   }

   public String getOutput() {
      this.output.flush();
      this.out.flush();
      return this.out.toString();
   }
}
