package org.codehaus.groovy.transform.powerassert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class AssertionRenderer {
   private final String text;
   private final ValueRecorder recorder;
   private final List<StringBuilder> lines = new ArrayList();
   private final List<Integer> startColumns = new ArrayList();

   private AssertionRenderer(String text, ValueRecorder recorder) {
      if (text.contains("\n")) {
         throw new IllegalArgumentException("source text may not contain line breaks");
      } else {
         this.text = text;
         this.recorder = recorder;
      }
   }

   public static String render(String text, ValueRecorder recorder) {
      return (new AssertionRenderer(text, recorder)).render();
   }

   private String render() {
      this.renderText();
      this.sortValues();
      this.renderValues();
      return this.linesToString();
   }

   private void renderText() {
      this.lines.add(new StringBuilder(this.text));
      this.startColumns.add(0);
      this.lines.add(new StringBuilder());
      this.startColumns.add(0);
   }

   private void sortValues() {
      Collections.sort(this.recorder.getValues(), new Comparator<Value>() {
         public int compare(Value v1, Value v2) {
            return v2.getColumn() - v1.getColumn();
         }
      });
   }

   private void renderValues() {
      List<Value> values = this.recorder.getValues();

      label58:
      for(int i = 0; i < values.size(); ++i) {
         Value value = (Value)values.get(i);
         int startColumn = value.getColumn();
         if (startColumn >= 1) {
            Value next = i + 1 < values.size() ? (Value)values.get(i + 1) : null;
            if (next == null || next.getColumn() != value.getColumn()) {
               String str = valueToString(value.getValue());
               if (str != null) {
                  String[] strs = str.split("\r\n|\r|\n");
                  int endColumn = strs.length == 1 ? value.getColumn() + str.length() : Integer.MAX_VALUE;

                  for(int j = 1; j < this.lines.size(); ++j) {
                     if (endColumn < (Integer)this.startColumns.get(j)) {
                        placeString((StringBuilder)this.lines.get(j), str, startColumn);
                        this.startColumns.set(j, startColumn);
                        continue label58;
                     }

                     placeString((StringBuilder)this.lines.get(j), "|", startColumn);
                     if (j > 1) {
                        this.startColumns.set(j, startColumn + 1);
                     }
                  }

                  String[] arr$ = strs;
                  int len$ = strs.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     String s = arr$[i$];
                     StringBuilder newLine = new StringBuilder();
                     this.lines.add(newLine);
                     placeString(newLine, s, startColumn);
                     this.startColumns.add(startColumn);
                  }
               }
            }
         }
      }

   }

   private String linesToString() {
      StringBuilder firstLine = (StringBuilder)this.lines.get(0);

      for(int i = 1; i < this.lines.size(); ++i) {
         firstLine.append('\n').append(((StringBuilder)this.lines.get(i)).toString());
      }

      return firstLine.toString();
   }

   private static void placeString(StringBuilder line, String str, int column) {
      while(line.length() < column) {
         line.append(' ');
      }

      line.replace(column - 1, column - 1 + str.length(), str);
   }

   private static String valueToString(Object value) {
      String toString;
      try {
         toString = DefaultGroovyMethods.toString(value);
      } catch (Exception var3) {
         return String.format("%s (toString() threw %s)", javaLangObjectToString(value), var3.getClass().getName());
      }

      if (toString == null) {
         return String.format("%s (toString() == null)", javaLangObjectToString(value));
      } else if (toString.equals("")) {
         return hasStringLikeType(value) ? "\"\"" : String.format("%s (toString() == \"\")", javaLangObjectToString(value));
      } else {
         return toString;
      }
   }

   private static boolean hasStringLikeType(Object value) {
      Class<?> clazz = value.getClass();
      return clazz == String.class || clazz == StringBuffer.class || clazz == StringBuilder.class;
   }

   private static String javaLangObjectToString(Object value) {
      String hash = Integer.toHexString(System.identityHashCode(value));
      return value.getClass().getName() + "@" + hash;
   }
}
