package org.apache.commons.httpclient;

import java.util.BitSet;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HeaderElement extends NameValuePair {
   private static final Log LOG;
   private static final BitSet SEPARATORS;
   private static final BitSet TOKEN_CHAR;
   private static final BitSet UNSAFE_CHAR;
   private NameValuePair[] parameters;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$HeaderElement;

   public HeaderElement() {
      this((String)null, (String)null, (NameValuePair[])null);
   }

   public HeaderElement(String name, String value) {
      this(name, value, (NameValuePair[])null);
   }

   public HeaderElement(String name, String value, NameValuePair[] parameters) {
      super(name, value);
      this.parameters = null;
      this.setParameters(parameters);
   }

   public NameValuePair[] getParameters() {
      return this.parameters;
   }

   protected void setParameters(NameValuePair[] pairs) {
      this.parameters = pairs;
   }

   public static final HeaderElement[] parse(String headerValue) throws HttpException {
      LOG.trace("enter HeaderElement.parse(String)");
      if (headerValue == null) {
         return null;
      } else {
         Vector elements = new Vector();

         HeaderElement element;
         for(StringTokenizer tokenizer = new StringTokenizer(headerValue.trim(), ","); tokenizer.countTokens() > 0; elements.addElement(element)) {
            String nextToken = tokenizer.nextToken();

            try {
               while(hasOddNumberOfQuotationMarks(nextToken)) {
                  nextToken = nextToken + "," + tokenizer.nextToken();
               }
            } catch (NoSuchElementException var12) {
               throw new HttpException("Bad header format: wrong number of quotation marks");
            }

            String tmp;
            try {
               if (tokenizer.hasMoreTokens()) {
                  tmp = nextToken.toLowerCase();
                  if (tmp.endsWith("mon") || tmp.endsWith("tue") || tmp.endsWith("wed") || tmp.endsWith("thu") || tmp.endsWith("fri") || tmp.endsWith("sat") || tmp.endsWith("sun") || tmp.endsWith("monday") || tmp.endsWith("tuesday") || tmp.endsWith("wednesday") || tmp.endsWith("thursday") || tmp.endsWith("friday") || tmp.endsWith("saturday") || tmp.endsWith("sunday")) {
                     nextToken = nextToken + "," + tokenizer.nextToken();
                  }
               }
            } catch (NoSuchElementException var13) {
               throw new HttpException("Bad header format: parsing with wrong header elements");
            }

            tmp = nextToken.trim();
            if (!tmp.endsWith(";")) {
               tmp = tmp + ";";
            }

            char[] header = tmp.toCharArray();
            boolean inAString = false;
            int startPos = 0;
            element = new HeaderElement();
            Vector paramlist = new Vector();

            for(int i = 0; i < header.length; ++i) {
               if (header[i] == ';' && !inAString) {
                  NameValuePair pair = parsePair(header, startPos, i);
                  if (pair == null) {
                     throw new HttpException("Bad header format: empty name/value pair in" + nextToken);
                  }

                  if (startPos == 0) {
                     element.setName(pair.getName());
                     element.setValue(pair.getValue());
                  } else {
                     paramlist.addElement(pair);
                  }

                  startPos = i + 1;
               } else if (header[i] == '"' && (!inAString || i <= 0 || header[i - 1] != '\\')) {
                  inAString = !inAString;
               }
            }

            if (paramlist.size() > 0) {
               NameValuePair[] tmp2 = new NameValuePair[paramlist.size()];
               paramlist.copyInto((NameValuePair[])tmp2);
               element.setParameters(tmp2);
               paramlist.removeAllElements();
            }
         }

         HeaderElement[] headerElements = new HeaderElement[elements.size()];
         elements.copyInto((HeaderElement[])headerElements);
         return headerElements;
      }
   }

   private static final boolean hasOddNumberOfQuotationMarks(String string) {
      boolean odd = false;

      for(int start = -1; (start = string.indexOf(34, start + 1)) != -1; odd = !odd) {
      }

      return odd;
   }

   private static final NameValuePair parsePair(char[] header, int start, int end) {
      LOG.trace("enter HeaderElement.parsePair(char[], int, int)");
      NameValuePair pair = null;
      String name = (new String(header, start, end - start)).trim();
      String value = null;
      int index = name.indexOf("=");
      if (index >= 0) {
         if (index + 1 < name.length()) {
            value = name.substring(index + 1).trim();
            if (value.startsWith("\"") && value.endsWith("\"")) {
               value = value.substring(1, value.length() - 1);
            }
         }

         name = name.substring(0, index).trim();
      }

      pair = new NameValuePair(name, value);
      return pair;
   }

   public NameValuePair getParameterByName(String name) {
      if (name == null) {
         throw new NullPointerException("Name is null");
      } else {
         NameValuePair found = null;
         NameValuePair[] parameters = this.getParameters();
         if (parameters != null) {
            for(int i = 0; i < parameters.length; ++i) {
               NameValuePair current = parameters[i];
               if (current.getName().equalsIgnoreCase(name)) {
                  found = current;
                  break;
               }
            }
         }

         return found;
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$HeaderElement == null ? (class$org$apache$commons$httpclient$HeaderElement = class$("org.apache.commons.httpclient.HeaderElement")) : class$org$apache$commons$httpclient$HeaderElement);
      SEPARATORS = new BitSet(128);
      TOKEN_CHAR = new BitSet(128);
      UNSAFE_CHAR = new BitSet(128);
      SEPARATORS.set(40);
      SEPARATORS.set(41);
      SEPARATORS.set(60);
      SEPARATORS.set(62);
      SEPARATORS.set(64);
      SEPARATORS.set(44);
      SEPARATORS.set(59);
      SEPARATORS.set(58);
      SEPARATORS.set(92);
      SEPARATORS.set(34);
      SEPARATORS.set(47);
      SEPARATORS.set(91);
      SEPARATORS.set(93);
      SEPARATORS.set(63);
      SEPARATORS.set(61);
      SEPARATORS.set(123);
      SEPARATORS.set(125);
      SEPARATORS.set(32);
      SEPARATORS.set(9);

      for(int ch = 32; ch < 127; ++ch) {
         TOKEN_CHAR.set(ch);
      }

      TOKEN_CHAR.xor(SEPARATORS);

      for(int ch = 0; ch < 32; ++ch) {
         UNSAFE_CHAR.set(ch);
      }

      UNSAFE_CHAR.set(32);
      UNSAFE_CHAR.set(60);
      UNSAFE_CHAR.set(62);
      UNSAFE_CHAR.set(34);
      UNSAFE_CHAR.set(123);
      UNSAFE_CHAR.set(125);
      UNSAFE_CHAR.set(124);
      UNSAFE_CHAR.set(92);
      UNSAFE_CHAR.set(94);
      UNSAFE_CHAR.set(126);
      UNSAFE_CHAR.set(91);
      UNSAFE_CHAR.set(93);
      UNSAFE_CHAR.set(96);
      UNSAFE_CHAR.set(127);
   }
}
