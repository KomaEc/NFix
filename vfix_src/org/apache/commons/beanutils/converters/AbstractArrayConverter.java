package org.apache.commons.beanutils.converters;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public abstract class AbstractArrayConverter implements Converter {
   protected Object defaultValue = null;
   protected static String[] strings = new String[0];
   protected boolean useDefault = true;

   public abstract Object convert(Class var1, Object var2);

   protected List parseElements(String svalue) {
      if (svalue == null) {
         throw new NullPointerException();
      } else {
         svalue = svalue.trim();
         if (svalue.startsWith("{") && svalue.endsWith("}")) {
            svalue = svalue.substring(1, svalue.length() - 1);
         }

         try {
            StreamTokenizer st = new StreamTokenizer(new StringReader(svalue));
            st.whitespaceChars(44, 44);
            st.ordinaryChars(48, 57);
            st.ordinaryChars(46, 46);
            st.ordinaryChars(45, 45);
            st.wordChars(48, 57);
            st.wordChars(46, 46);
            st.wordChars(45, 45);
            ArrayList list = new ArrayList();

            while(true) {
               int ttype = st.nextToken();
               if (ttype != -3 && ttype <= 0) {
                  if (ttype == -1) {
                     return list;
                  }

                  throw new ConversionException("Encountered token of type " + ttype);
               }

               list.add(st.sval);
            }
         } catch (IOException var5) {
            throw new ConversionException(var5);
         }
      }
   }
}
