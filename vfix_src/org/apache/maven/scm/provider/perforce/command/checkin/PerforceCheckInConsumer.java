package org.apache.maven.scm.provider.perforce.command.checkin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceCheckInConsumer implements StreamConsumer {
   private static final Pattern CREATED_PATTERN = Pattern.compile("^Change \\d+ created .+$");
   private static final Pattern SUBMITTING_PATTERN = Pattern.compile("^Submitting change \\d+\\.$");
   private static final Pattern LOCKING_PATTERN = Pattern.compile("^Locking \\d+ files \\.\\.\\.$");
   private static final Pattern OPERATION_PATTERN = Pattern.compile("^[a-z]+ //[^#]+#\\d+$");
   private static final Pattern COMPLETE_PATTERN = Pattern.compile("^Change \\d+ .*submitted.$");
   public static final int STATE_CREATED = 1;
   public static final int STATE_SUBMITTING = 2;
   public static final int STATE_LOCKING = 3;
   public static final int STATE_OP = 4;
   public static final int STATE_COMPLETE = 5;
   public static final int STATE_ERROR = 6;
   private StringWriter errors = new StringWriter();
   private PrintWriter errorOutput;
   private int currentState;

   public PerforceCheckInConsumer() {
      this.errorOutput = new PrintWriter(this.errors);
      this.currentState = 1;
   }

   public void consumeLine(String line) {
      if (!line.startsWith("... ")) {
         switch(this.currentState) {
         case 1:
            boolean created = CREATED_PATTERN.matcher(line).matches();
            if (created) {
               ++this.currentState;
            } else {
               this.error(line);
            }
            break;
         case 2:
            boolean submitting = SUBMITTING_PATTERN.matcher(line).matches();
            if (submitting) {
               ++this.currentState;
            } else {
               this.error(line);
            }
            break;
         case 3:
            boolean locked = LOCKING_PATTERN.matcher(line).matches();
            if (locked) {
               ++this.currentState;
            } else {
               this.error(line);
            }
            break;
         case 4:
            boolean operation = OPERATION_PATTERN.matcher(line).matches();
            if (!operation) {
               if (COMPLETE_PATTERN.matcher(line).matches()) {
                  ++this.currentState;
               } else {
                  this.error(line);
               }
            }
         case 5:
         default:
            break;
         case 6:
            this.error(line);
         }

      }
   }

   private void error(String line) {
      this.currentState = 6;
      this.errorOutput.println(line);
   }

   public boolean isSuccess() {
      return this.currentState == 5;
   }

   public String getOutput() {
      this.errorOutput.flush();
      this.errors.flush();
      return this.errors.toString();
   }
}
