package org.apache.tools.ant.taskdefs.cvslib;

import org.apache.tools.ant.util.LineOrientedOutputStream;

class RedirectingOutputStream extends LineOrientedOutputStream {
   private final ChangeLogParser parser;

   public RedirectingOutputStream(ChangeLogParser parser) {
      this.parser = parser;
   }

   protected void processLine(String line) {
      this.parser.stdout(line);
   }
}
