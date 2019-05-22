package com.gzoltar.shaded.org.jacoco.core.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class CommandLineSupport {
   private static final char BLANK = ' ';
   private static final char QUOTE = '"';
   private static final char SLASH = '\\';
   private static final int M_STRIPWHITESPACE = 0;
   private static final int M_PARSEARGUMENT = 1;
   private static final int M_ESCAPED = 2;

   static String quote(String arg) {
      StringBuilder escaped = new StringBuilder();
      char[] arr$ = arg.toCharArray();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         char c = arr$[i$];
         if (c == '"' || c == '\\') {
            escaped.append('\\');
         }

         escaped.append(c);
      }

      if (arg.indexOf(32) != -1 || arg.indexOf(34) != -1) {
         escaped.insert(0, '"').append('"');
      }

      return escaped.toString();
   }

   static String quote(List<String> args) {
      StringBuilder result = new StringBuilder();
      boolean seperate = false;

      for(Iterator i$ = args.iterator(); i$.hasNext(); seperate = true) {
         String arg = (String)i$.next();
         if (seperate) {
            result.append(' ');
         }

         result.append(quote(arg));
      }

      return result.toString();
   }

   static List<String> split(String commandline) {
      if (commandline != null && commandline.length() != 0) {
         List<String> args = new ArrayList();
         StringBuilder current = new StringBuilder();
         int mode = 0;
         int endChar = 32;
         char[] arr$ = commandline.toCharArray();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            char c = arr$[i$];
            switch(mode) {
            case 0:
               if (!Character.isWhitespace(c)) {
                  if (c == '"') {
                     endChar = 34;
                  } else {
                     current.append(c);
                     endChar = 32;
                  }

                  mode = 1;
               }
               break;
            case 1:
               if (c == endChar) {
                  addArgument(args, current);
                  mode = 0;
               } else if (c == '\\') {
                  current.append('\\');
                  mode = 2;
               } else {
                  current.append(c);
               }
               break;
            case 2:
               if (c != '"' && c != '\\') {
                  if (c == endChar) {
                     addArgument(args, current);
                     boolean var9 = false;
                  } else {
                     current.append(c);
                  }
               } else {
                  current.setCharAt(current.length() - 1, c);
               }

               mode = 1;
            }
         }

         addArgument(args, current);
         return args;
      } else {
         return new ArrayList();
      }
   }

   private static void addArgument(List<String> args, StringBuilder current) {
      if (current.length() > 0) {
         args.add(current.toString());
         current.setLength(0);
      }

   }

   private CommandLineSupport() {
   }
}
