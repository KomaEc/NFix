package org.apache.tools.ant.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

public final class StringUtils {
   public static final String LINE_SEP = System.getProperty("line.separator");

   private StringUtils() {
   }

   public static Vector lineSplit(String data) {
      return split(data, 10);
   }

   public static Vector split(String data, int ch) {
      Vector elems = new Vector();
      int pos = true;

      int i;
      int pos;
      for(i = 0; (pos = data.indexOf(ch, i)) != -1; i = pos + 1) {
         String elem = data.substring(i, pos);
         elems.addElement(elem);
      }

      elems.addElement(data.substring(i));
      return elems;
   }

   public static String replace(String data, String from, String to) {
      StringBuffer buf = new StringBuffer(data.length());
      int pos = true;

      int i;
      int pos;
      for(i = 0; (pos = data.indexOf(from, i)) != -1; i = pos + from.length()) {
         buf.append(data.substring(i, pos)).append(to);
      }

      buf.append(data.substring(i));
      return buf.toString();
   }

   public static String getStackTrace(Throwable t) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw, true);
      t.printStackTrace(pw);
      pw.flush();
      pw.close();
      return sw.toString();
   }

   public static boolean endsWith(StringBuffer buffer, String suffix) {
      if (suffix.length() > buffer.length()) {
         return false;
      } else {
         int endIndex = suffix.length() - 1;

         for(int bufferIndex = buffer.length() - 1; endIndex >= 0; --endIndex) {
            if (buffer.charAt(bufferIndex) != suffix.charAt(endIndex)) {
               return false;
            }

            --bufferIndex;
         }

         return true;
      }
   }

   public static String resolveBackSlash(String input) {
      StringBuffer b = new StringBuffer();
      boolean backSlashSeen = false;

      for(int i = 0; i < input.length(); ++i) {
         char c = input.charAt(i);
         if (!backSlashSeen) {
            if (c == '\\') {
               backSlashSeen = true;
            } else {
               b.append(c);
            }
         } else {
            switch(c) {
            case '\\':
               b.append('\\');
               break;
            case 'f':
               b.append('\f');
               break;
            case 'n':
               b.append('\n');
               break;
            case 'r':
               b.append('\r');
               break;
            case 's':
               b.append(" \t\n\r\f");
               break;
            case 't':
               b.append('\t');
               break;
            default:
               b.append(c);
            }

            backSlashSeen = false;
         }
      }

      return b.toString();
   }

   public static long parseHumanSizes(String humanSize) throws Exception {
      long KILOBYTE = 1024L;
      long MEGABYTE = 1048576L;
      long GIGABYTE = 1073741824L;
      long TERABYTE = 1099511627776L;
      long PETABYTE = 1125899906842624L;
      if (!Character.isDigit(humanSize.charAt(humanSize.length() - 1))) {
         char c = humanSize.charAt(humanSize.length() - 1);
         long value = Long.valueOf(humanSize.substring(0, humanSize.length() - 1));
         switch(c) {
         case 'G':
            return value * 1073741824L;
         case 'H':
         case 'I':
         case 'J':
         case 'L':
         case 'N':
         case 'O':
         case 'Q':
         case 'R':
         case 'S':
         default:
            return value;
         case 'K':
            return value * 1024L;
         case 'M':
            return value * 1048576L;
         case 'P':
            return value * 1125899906842624L;
         case 'T':
            return value * 1099511627776L;
         }
      } else {
         return Long.parseLong(humanSize);
      }
   }
}
