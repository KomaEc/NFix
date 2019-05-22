package org.apache.maven.scm.provider.perforce.command.diff;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceDiffConsumer implements StreamConsumer {
   private StringWriter out = new StringWriter();
   private PrintWriter output;

   public PerforceDiffConsumer() {
      this.output = new PrintWriter(this.out);
   }

   public void consumeLine(String line) {
      this.output.println(line);
   }

   public String getOutput() {
      this.output.flush();
      this.out.flush();
      return this.out.toString();
   }
}
