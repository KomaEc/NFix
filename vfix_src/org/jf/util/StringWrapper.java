package org.jf.util;

import java.io.PrintStream;
import java.text.BreakIterator;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StringWrapper {
   public static Iterable<String> wrapStringOnBreaks(@Nonnull final String string, final int maxWidth) {
      final BreakIterator breakIterator = BreakIterator.getLineInstance();
      breakIterator.setText(string);
      return new Iterable<String>() {
         public Iterator<String> iterator() {
            return new Iterator<String>() {
               private int currentLineStart = 0;
               private boolean nextLineSet = false;
               private String nextLine;

               public boolean hasNext() {
                  if (!this.nextLineSet) {
                     this.calculateNext();
                  }

                  return this.nextLine != null;
               }

               private void calculateNext() {
                  int lineEnd = this.currentLineStart;

                  while(true) {
                     lineEnd = breakIterator.following(lineEnd);
                     if (lineEnd == -1) {
                        lineEnd = breakIterator.last();
                        if (lineEnd <= this.currentLineStart) {
                           this.nextLine = null;
                           this.nextLineSet = true;
                           return;
                        }
                        break;
                     }

                     if (lineEnd - this.currentLineStart > maxWidth) {
                        lineEnd = breakIterator.preceding(lineEnd);
                        if (lineEnd <= this.currentLineStart) {
                           lineEnd = this.currentLineStart + maxWidth;
                        }
                        break;
                     }

                     if (string.charAt(lineEnd - 1) == '\n') {
                        this.nextLine = string.substring(this.currentLineStart, lineEnd - 1);
                        this.nextLineSet = true;
                        this.currentLineStart = lineEnd;
                        return;
                     }
                  }

                  this.nextLine = string.substring(this.currentLineStart, lineEnd);
                  this.nextLineSet = true;
                  this.currentLineStart = lineEnd;
               }

               public String next() {
                  String ret = this.nextLine;
                  this.nextLine = null;
                  this.nextLineSet = false;
                  return ret;
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   public static String[] wrapString(@Nonnull String str, int maxWidth, @Nullable String[] output) {
      if (output == null) {
         output = new String[(int)((double)(str.length() / maxWidth) * 1.5D + 1.0D)];
      }

      int lineStart = 0;
      int arrayIndex = 0;

      int i;
      for(i = 0; i < str.length(); ++i) {
         char c = str.charAt(i);
         if (c == '\n') {
            output = addString(output, str.substring(lineStart, i), arrayIndex++);
            lineStart = i + 1;
         } else if (i - lineStart == maxWidth) {
            output = addString(output, str.substring(lineStart, i), arrayIndex++);
            lineStart = i;
         }
      }

      if (lineStart != i || i == 0) {
         output = addString(output, str.substring(lineStart), arrayIndex++, output.length + 1);
      }

      if (arrayIndex < output.length) {
         output[arrayIndex] = null;
      }

      return output;
   }

   private static String[] addString(@Nonnull String[] arr, String str, int index) {
      if (index >= arr.length) {
         arr = enlargeArray(arr, (int)Math.ceil((double)(arr.length + 1) * 1.5D));
      }

      arr[index] = str;
      return arr;
   }

   private static String[] addString(@Nonnull String[] arr, String str, int index, int newLength) {
      if (index >= arr.length) {
         arr = enlargeArray(arr, newLength);
      }

      arr[index] = str;
      return arr;
   }

   private static String[] enlargeArray(String[] arr, int newLength) {
      String[] newArr = new String[newLength];
      System.arraycopy(arr, 0, newArr, 0, arr.length);
      return newArr;
   }

   public static void printWrappedString(@Nonnull PrintStream stream, @Nonnull String string) {
      printWrappedString(stream, string, ConsoleUtil.getConsoleWidth());
   }

   public static void printWrappedString(@Nonnull PrintStream stream, @Nonnull String string, int maxWidth) {
      Iterator var3 = wrapStringOnBreaks(string, maxWidth).iterator();

      while(var3.hasNext()) {
         String str = (String)var3.next();
         stream.println(str);
      }

   }
}
