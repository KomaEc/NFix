package com.gzoltar.shaded.org.pitest.reloc.xstream.persistence;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.DomDriver;
import java.io.File;

public class FilePersistenceStrategy extends AbstractFilePersistenceStrategy {
   private final String illegalChars;

   public FilePersistenceStrategy(File baseDirectory) {
      this(baseDirectory, new XStream(new DomDriver()));
   }

   public FilePersistenceStrategy(File baseDirectory, XStream xstream) {
      this(baseDirectory, xstream, "utf-8", "<>?:/\\\"|*%");
   }

   public FilePersistenceStrategy(File baseDirectory, XStream xstream, String encoding, String illegalChars) {
      super(baseDirectory, xstream, encoding);
      this.illegalChars = illegalChars;
   }

   protected boolean isValid(File dir, String name) {
      return super.isValid(dir, name) && name.indexOf(64) > 0;
   }

   protected Object extractKey(String name) {
      String key = this.unescape(name.substring(0, name.length() - 4));
      if ("null@null".equals(key)) {
         return null;
      } else {
         int idx = key.indexOf(64);
         if (idx < 0) {
            throw new StreamException("Not a valid key: " + key);
         } else {
            Class type = this.getMapper().realClass(key.substring(0, idx));
            Converter converter = this.getConverterLookup().lookupConverterForType(type);
            if (converter instanceof SingleValueConverter) {
               SingleValueConverter svConverter = (SingleValueConverter)converter;
               return svConverter.fromString(key.substring(idx + 1));
            } else {
               throw new StreamException("No SingleValueConverter for type " + type.getName() + " available");
            }
         }
      }
   }

   protected String unescape(String name) {
      StringBuffer buffer = new StringBuffer();

      for(int idx = name.indexOf(37); idx >= 0; idx = name.indexOf(37)) {
         buffer.append(name.substring(0, idx));
         int c = Integer.parseInt(name.substring(idx + 1, idx + 3), 16);
         buffer.append((char)c);
         name = name.substring(idx + 3);
      }

      buffer.append(name);
      return buffer.toString();
   }

   protected String getName(Object key) {
      if (key == null) {
         return "null@null.xml";
      } else {
         Class type = key.getClass();
         Converter converter = this.getConverterLookup().lookupConverterForType(type);
         if (converter instanceof SingleValueConverter) {
            SingleValueConverter svConverter = (SingleValueConverter)converter;
            return this.getMapper().serializedClass(type) + '@' + this.escape(svConverter.toString(key)) + ".xml";
         } else {
            throw new StreamException("No SingleValueConverter for type " + type.getName() + " available");
         }
      }
   }

   protected String escape(String key) {
      StringBuffer buffer = new StringBuffer();
      char[] array = key.toCharArray();

      for(int i = 0; i < array.length; ++i) {
         char c = array[i];
         if (c >= ' ' && this.illegalChars.indexOf(c) < 0) {
            buffer.append(c);
         } else {
            buffer.append("%" + Integer.toHexString(c).toUpperCase());
         }
      }

      return buffer.toString();
   }
}
