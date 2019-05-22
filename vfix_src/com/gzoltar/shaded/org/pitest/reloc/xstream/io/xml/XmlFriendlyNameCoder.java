package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class XmlFriendlyNameCoder implements NameCoder, Cloneable {
   private static final XmlFriendlyNameCoder.IntPair[] XML_NAME_START_CHAR_BOUNDS;
   private static final XmlFriendlyNameCoder.IntPair[] XML_NAME_CHAR_EXTRA_BOUNDS;
   private final String dollarReplacement;
   private final String escapeCharReplacement;
   private transient Map escapeCache;
   private transient Map unescapeCache;
   private final String hexPrefix;

   public XmlFriendlyNameCoder() {
      this("_-", "__");
   }

   public XmlFriendlyNameCoder(String dollarReplacement, String escapeCharReplacement) {
      this(dollarReplacement, escapeCharReplacement, "_.");
   }

   public XmlFriendlyNameCoder(String dollarReplacement, String escapeCharReplacement, String hexPrefix) {
      this.dollarReplacement = dollarReplacement;
      this.escapeCharReplacement = escapeCharReplacement;
      this.hexPrefix = hexPrefix;
      this.readResolve();
   }

   public String decodeAttribute(String attributeName) {
      return this.decodeName(attributeName);
   }

   public String decodeNode(String elementName) {
      return this.decodeName(elementName);
   }

   public String encodeAttribute(String name) {
      return this.encodeName(name);
   }

   public String encodeNode(String name) {
      return this.encodeName(name);
   }

   private String encodeName(String name) {
      String s = (String)this.escapeCache.get(name);
      if (s == null) {
         int length = name.length();

         int i;
         for(i = 0; i < length; ++i) {
            char c = name.charAt(i);
            if (c == '$' || c == '_' || c <= 27 || c >= 127) {
               break;
            }
         }

         if (i == length) {
            return name;
         }

         StringBuffer result = new StringBuffer(length + 8);
         if (i > 0) {
            result.append(name.substring(0, i));
         }

         for(; i < length; ++i) {
            char c = name.charAt(i);
            if (c == '$') {
               result.append(this.dollarReplacement);
            } else if (c == '_') {
               result.append(this.escapeCharReplacement);
            } else if ((i != 0 || isXmlNameStartChar(c)) && (i <= 0 || isXmlNameChar(c))) {
               result.append(c);
            } else {
               result.append(this.hexPrefix);
               if (c < 16) {
                  result.append("000");
               } else if (c < 256) {
                  result.append("00");
               } else if (c < 4096) {
                  result.append("0");
               }

               result.append(Integer.toHexString(c));
            }
         }

         s = result.toString();
         this.escapeCache.put(name, s);
      }

      return s;
   }

   private String decodeName(String name) {
      String s = (String)this.unescapeCache.get(name);
      if (s == null) {
         char dollarReplacementFirstChar = this.dollarReplacement.charAt(0);
         char escapeReplacementFirstChar = this.escapeCharReplacement.charAt(0);
         char hexPrefixFirstChar = this.hexPrefix.charAt(0);
         int length = name.length();

         int i;
         for(i = 0; i < length; ++i) {
            char c = name.charAt(i);
            if (c == dollarReplacementFirstChar || c == escapeReplacementFirstChar || c == hexPrefixFirstChar) {
               break;
            }
         }

         if (i == length) {
            return name;
         }

         StringBuffer result = new StringBuffer(length + 8);
         if (i > 0) {
            result.append(name.substring(0, i));
         }

         for(; i < length; ++i) {
            char c = name.charAt(i);
            if (c == dollarReplacementFirstChar && name.startsWith(this.dollarReplacement, i)) {
               i += this.dollarReplacement.length() - 1;
               result.append('$');
            } else if (c == hexPrefixFirstChar && name.startsWith(this.hexPrefix, i)) {
               i += this.hexPrefix.length();
               c = (char)Integer.parseInt(name.substring(i, i + 4), 16);
               i += 3;
               result.append(c);
            } else if (c == escapeReplacementFirstChar && name.startsWith(this.escapeCharReplacement, i)) {
               i += this.escapeCharReplacement.length() - 1;
               result.append('_');
            } else {
               result.append(c);
            }
         }

         s = result.toString();
         this.unescapeCache.put(name, s);
      }

      return s;
   }

   public Object clone() {
      try {
         XmlFriendlyNameCoder coder = (XmlFriendlyNameCoder)super.clone();
         coder.readResolve();
         return coder;
      } catch (CloneNotSupportedException var2) {
         throw new ObjectAccessException("Cannot clone XmlFriendlyNameCoder", var2);
      }
   }

   private Object readResolve() {
      this.escapeCache = this.createCacheMap();
      this.unescapeCache = this.createCacheMap();
      return this;
   }

   protected Map createCacheMap() {
      return new HashMap();
   }

   private static boolean isXmlNameStartChar(int cp) {
      return isInNameCharBounds(cp, XML_NAME_START_CHAR_BOUNDS);
   }

   private static boolean isXmlNameChar(int cp) {
      return isXmlNameStartChar(cp) ? true : isInNameCharBounds(cp, XML_NAME_CHAR_EXTRA_BOUNDS);
   }

   private static boolean isInNameCharBounds(int cp, XmlFriendlyNameCoder.IntPair[] nameCharBounds) {
      for(int i = 0; i < nameCharBounds.length; ++i) {
         XmlFriendlyNameCoder.IntPair p = nameCharBounds[i];
         if (cp >= p.min && cp <= p.max) {
            return true;
         }
      }

      return false;
   }

   static {
      class IntPairList extends ArrayList {
         void add(int min, int max) {
            super.add(new XmlFriendlyNameCoder.IntPair(min, max));
         }

         void add(char cp) {
            super.add(new XmlFriendlyNameCoder.IntPair(cp, cp));
         }
      }

      IntPairList list = new IntPairList();
      list.add(':');
      list.add(65, 90);
      list.add(97, 122);
      list.add('_');
      list.add(192, 214);
      list.add(216, 246);
      list.add(248, 767);
      list.add(880, 893);
      list.add(895, 8191);
      list.add(8204, 8205);
      list.add(8304, 8591);
      list.add(11264, 12271);
      list.add(12289, 55295);
      list.add(63744, 64975);
      list.add(65008, 65533);
      list.add(65536, 983039);
      XML_NAME_START_CHAR_BOUNDS = (XmlFriendlyNameCoder.IntPair[])((XmlFriendlyNameCoder.IntPair[])list.toArray(new XmlFriendlyNameCoder.IntPair[list.size()]));
      list.clear();
      list.add('-');
      list.add('.');
      list.add(48, 57);
      list.add('·');
      list.add(768, 879);
      list.add(8255, 8256);
      XML_NAME_CHAR_EXTRA_BOUNDS = (XmlFriendlyNameCoder.IntPair[])((XmlFriendlyNameCoder.IntPair[])list.toArray(new XmlFriendlyNameCoder.IntPair[list.size()]));
   }

   private static class IntPair {
      int min;
      int max;

      public IntPair(int min, int max) {
         this.min = min;
         this.max = max;
      }
   }
}
