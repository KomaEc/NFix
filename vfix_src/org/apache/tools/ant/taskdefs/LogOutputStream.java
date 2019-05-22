package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.LineOrientedOutputStream;

public class LogOutputStream extends LineOrientedOutputStream {
   private ProjectComponent pc;
   private int level;

   public LogOutputStream(Task task, int level) {
      this((ProjectComponent)task, level);
   }

   public LogOutputStream(ProjectComponent pc, int level) {
      this.level = 2;
      this.pc = pc;
      this.level = level;
   }

   protected void processBuffer() {
      try {
         super.processBuffer();
      } catch (IOException var2) {
         throw new RuntimeException("Impossible IOException caught: " + var2);
      }
   }

   protected void processLine(String line) {
      this.processLine(line, this.level);
   }

   protected void processLine(String line, int level) {
      this.pc.log(line, level);
   }

   public int getMessageLevel() {
      return this.level;
   }
}
