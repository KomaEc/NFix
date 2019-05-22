package org.testng.log;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class TextFormatter extends SimpleFormatter {
   public synchronized String format(LogRecord record) {
      StringBuffer result = new StringBuffer();
      result.append(record.getMessage()).append("\n");
      return result.toString();
   }
}
