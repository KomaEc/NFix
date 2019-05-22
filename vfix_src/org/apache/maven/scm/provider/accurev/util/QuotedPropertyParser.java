package org.apache.maven.scm.provider.accurev.util;

import java.util.HashMap;
import java.util.Map;

public final class QuotedPropertyParser {
   private QuotedPropertyParser() {
   }

   public static Map<String, String> parse(CharSequence seq) {
      Map<String, String> hashMap = new HashMap();
      parse(seq, hashMap);
      return hashMap;
   }

   public static void parse(CharSequence string, Map<? super String, ? super String> propertyMap) {
      QuotedPropertyParser.QuotedParseState state = QuotedPropertyParser.QuotedParseState.KEY;
      char quote = 0;
      StringBuilder buffer = new StringBuilder();
      String propertyKey = "";
      int i = 0;

      int pos;
      for(pos = 0; i < string.length(); ++i) {
         char current = string.charAt(i);
         switch(state) {
         case KEY:
            switch(current) {
            case '"':
            case '\'':
               quote = current;
               state = QuotedPropertyParser.QuotedParseState.IN_QUOTED_KEY;
               if (i >= pos) {
                  buffer.append(string.subSequence(pos, i));
               }

               pos = i + 1;
               continue;
            case '=':
               if (i >= pos) {
                  buffer.append(string.subSequence(pos, i));
               }

               propertyKey = buffer.toString();
               buffer = new StringBuilder();
               state = QuotedPropertyParser.QuotedParseState.VALUE;
               pos = i + 1;
            default:
               continue;
            }
         case VALUE:
            switch(current) {
            case '"':
            case '\'':
               quote = current;
               state = QuotedPropertyParser.QuotedParseState.IN_QUOTED_VALUE;
               if (i >= pos) {
                  buffer.append(string.subSequence(pos, i));
               }

               pos = i + 1;
               continue;
            case '&':
               if (i >= pos) {
                  buffer.append(string.subSequence(pos, i));
               }

               propertyMap.put(propertyKey, buffer.toString());
               pos = i + 1;
               buffer = new StringBuilder();
               state = QuotedPropertyParser.QuotedParseState.KEY;
            default:
               continue;
            }
         case IN_QUOTED_KEY:
         case IN_QUOTED_VALUE:
            if (current == quote) {
               state = state == QuotedPropertyParser.QuotedParseState.IN_QUOTED_KEY ? QuotedPropertyParser.QuotedParseState.KEY : QuotedPropertyParser.QuotedParseState.VALUE;
               if (i >= pos) {
                  buffer.append(string.subSequence(pos, i));
               }

               pos = i + 1;
            }
         }
      }

      if (state == QuotedPropertyParser.QuotedParseState.VALUE) {
         if (i >= pos) {
            buffer.append(string.subSequence(pos, i));
         }

         propertyMap.put(propertyKey, buffer.toString());
      }

   }

   public static enum QuotedParseState {
      KEY,
      IN_QUOTED_KEY,
      IN_QUOTED_VALUE,
      VALUE;
   }
}
