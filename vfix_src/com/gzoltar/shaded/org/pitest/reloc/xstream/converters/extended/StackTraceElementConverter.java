package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackTraceElementConverter extends AbstractSingleValueConverter {
   private static final Pattern PATTERN = Pattern.compile("^(.+)\\.([^\\(]+)\\(([^:]*)(:(\\d+))?\\)$");
   private static final StackTraceElementFactory FACTORY;

   public boolean canConvert(Class type) {
      return StackTraceElement.class.equals(type) && FACTORY != null;
   }

   public String toString(Object obj) {
      String s = super.toString(obj);
      return s.replaceFirst(":\\?\\?\\?", "");
   }

   public Object fromString(String str) {
      Matcher matcher = PATTERN.matcher(str);
      if (matcher.matches()) {
         String declaringClass = matcher.group(1);
         String methodName = matcher.group(2);
         String fileName = matcher.group(3);
         if (fileName.equals("Unknown Source")) {
            return FACTORY.unknownSourceElement(declaringClass, methodName);
         } else if (fileName.equals("Native Method")) {
            return FACTORY.nativeMethodElement(declaringClass, methodName);
         } else if (matcher.group(4) != null) {
            int lineNumber = Integer.parseInt(matcher.group(5));
            return FACTORY.element(declaringClass, methodName, fileName, lineNumber);
         } else {
            return FACTORY.element(declaringClass, methodName, fileName);
         }
      } else {
         throw new ConversionException("Could not parse StackTraceElement : " + str);
      }
   }

   static {
      StackTraceElementFactory factory = null;
      if (JVM.is15()) {
         Class factoryType = JVM.loadClassForName("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.StackTraceElementFactory15", false);

         try {
            factory = (StackTraceElementFactory)factoryType.newInstance();
         } catch (Exception var5) {
         } catch (LinkageError var6) {
         }
      }

      if (factory == null) {
         factory = new StackTraceElementFactory();
      }

      try {
         factory.unknownSourceElement("a", "b");
      } catch (Exception var3) {
         factory = null;
      } catch (NoClassDefFoundError var4) {
         factory = null;
      }

      FACTORY = factory;
   }
}
