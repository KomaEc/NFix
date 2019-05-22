package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml;

final class XMLEncode {
   private static final int CDATA_BLOCK_THRESHOLD_LENGTH = 12;
   private static final char DEFAULT_QUOTE_CHAR = '"';

   public static boolean isWhiteSpace(String text) {
      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         if (!Character.isWhitespace(c)) {
            return false;
         }
      }

      return true;
   }

   public static String xmlEncodeTextForAttribute(String text, char quoteChar) {
      return text == null ? null : xmlEncodeTextAsPCDATA(text, true, quoteChar);
   }

   public static String xmlEncodeText(String text) {
      if (text == null) {
         return null;
      } else if (!needsEncoding(text)) {
         return text;
      } else {
         if (text.length() > 12) {
            String cdata = xmlEncodeTextAsCDATABlock(text);
            if (cdata != null) {
               return cdata;
            }
         }

         return xmlEncodeTextAsPCDATA(text);
      }
   }

   public static String xmlEncodeTextAsPCDATA(String text) {
      return text == null ? null : xmlEncodeTextAsPCDATA(text, false);
   }

   public static String xmlEncodeTextAsPCDATA(String text, boolean forAttribute) {
      return xmlEncodeTextAsPCDATA(text, forAttribute, '"');
   }

   public static String xmlEncodeTextAsPCDATA(String text, boolean forAttribute, char quoteChar) {
      if (text == null) {
         return null;
      } else {
         StringBuilder n = new StringBuilder(text.length() * 2);

         for(int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            switch(c) {
            case '\n':
               if (forAttribute) {
                  n.append("&#10;");
               }
               break;
            case '\r':
               if (forAttribute) {
                  if (i == text.length() - 1 || text.charAt(i + 1) != '\n') {
                     n.append("&#13;");
                  }
                  break;
               }

               n.append(c);
               break;
            case '"':
               if (forAttribute) {
                  n.append("&quot;");
               } else {
                  n.append(c);
               }
               break;
            case '&':
               n.append("&amp;");
               break;
            case '\'':
               if (forAttribute) {
                  n.append("&apos;");
               } else {
                  n.append(c);
               }
               break;
            case '<':
               n.append("&lt;");
               break;
            case '>':
               n.append("&gt;");
               break;
            default:
               n.append(c);
            }
         }

         if (forAttribute) {
            n.append(quoteChar);
            n.insert(0, quoteChar);
         }

         return n.toString();
      }
   }

   public static String xmlEncodeTextAsCDATABlock(String text) {
      if (text == null) {
         return null;
      } else {
         return isCompatibleWithCDATABlock(text) ? "<![CDATA[" + text + "]]>" : null;
      }
   }

   public static boolean needsEncoding(String text) {
      return needsEncoding(text, false);
   }

   public static boolean needsEncoding(String data, boolean checkForAttr) {
      if (data == null) {
         return false;
      } else {
         for(int i = 0; i < data.length(); ++i) {
            char c = data.charAt(i);
            if (c == '&' || c == '<' || checkForAttr && (c == '"' || c == '\'')) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isCompatibleWithCDATABlock(String text) {
      return text != null && !text.contains("]]>");
   }

   public static String xmlDecodeTextToCDATA(String pcdata) {
      if (pcdata == null) {
         return null;
      } else {
         StringBuilder n = new StringBuilder(pcdata.length());

         for(int i = 0; i < pcdata.length(); ++i) {
            char c = pcdata.charAt(i);
            if (c == '&') {
               char c1 = lookAhead(1, i, pcdata);
               char c2 = lookAhead(2, i, pcdata);
               char c3 = lookAhead(3, i, pcdata);
               char c4 = lookAhead(4, i, pcdata);
               char c5 = lookAhead(5, i, pcdata);
               if (c1 == 'a' && c2 == 'm' && c3 == 'p' && c4 == ';') {
                  n.append("&");
                  i += 4;
               } else if (c1 == 'l' && c2 == 't' && c3 == ';') {
                  n.append("<");
                  i += 3;
               } else if (c1 == 'g' && c2 == 't' && c3 == ';') {
                  n.append(">");
                  i += 3;
               } else if (c1 == 'q' && c2 == 'u' && c3 == 'o' && c4 == 't' && c5 == ';') {
                  n.append("\"");
                  i += 5;
               } else if (c1 == 'a' && c2 == 'p' && c3 == 'o' && c4 == 's' && c5 == ';') {
                  n.append("'");
                  i += 5;
               } else {
                  n.append("&");
               }
            } else {
               n.append(c);
            }
         }

         return n.toString();
      }
   }

   private static char lookAhead(int la, int offset, String data) {
      try {
         return data.charAt(offset + la);
      } catch (StringIndexOutOfBoundsException var4) {
         return '\u0000';
      }
   }

   private static boolean contains(String text, char[] chars) {
      if (text != null && chars != null && chars.length != 0) {
         for(int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            char[] arr$ = chars;
            int len$ = chars.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               char aChar = arr$[i$];
               if (aChar == c) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
