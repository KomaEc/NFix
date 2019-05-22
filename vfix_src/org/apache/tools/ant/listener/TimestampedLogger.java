package org.apache.tools.ant.listener;

import java.text.DateFormat;
import java.util.Date;
import org.apache.tools.ant.DefaultLogger;

public class TimestampedLogger extends DefaultLogger {
   private static final String SPACER = " - at ";

   protected String getBuildFailedMessage() {
      return super.getBuildFailedMessage() + " - at " + this.getTimestamp();
   }

   protected String getBuildSuccessfulMessage() {
      return super.getBuildSuccessfulMessage() + " - at " + this.getTimestamp();
   }

   protected String getTimestamp() {
      Date date = new Date(System.currentTimeMillis());
      DateFormat formatter = DateFormat.getDateTimeInstance(3, 3);
      String finishTime = formatter.format(date);
      return finishTime;
   }
}
