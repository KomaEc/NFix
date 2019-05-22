package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public class StandardStaxDriver extends StaxDriver {
   public StandardStaxDriver() {
   }

   /** @deprecated */
   public StandardStaxDriver(QNameMap qnameMap, XmlFriendlyNameCoder nameCoder) {
      super(qnameMap, (NameCoder)nameCoder);
   }

   public StandardStaxDriver(QNameMap qnameMap, NameCoder nameCoder) {
      super(qnameMap, nameCoder);
   }

   public StandardStaxDriver(QNameMap qnameMap) {
      super(qnameMap);
   }

   /** @deprecated */
   public StandardStaxDriver(XmlFriendlyNameCoder nameCoder) {
      super((NameCoder)nameCoder);
   }

   public StandardStaxDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   protected XMLInputFactory createInputFactory() {
      Object exception = null;

      try {
         Class staxInputFactory = JVM.getStaxInputFactory();
         if (staxInputFactory != null) {
            return (XMLInputFactory)staxInputFactory.newInstance();
         }

         throw new StreamException("Java runtime has no standard XMLInputFactory implementation.", (Throwable)exception);
      } catch (InstantiationException var3) {
         exception = var3;
      } catch (IllegalAccessException var4) {
         exception = var4;
      } catch (ClassNotFoundException var5) {
         exception = var5;
      }

      throw new StreamException("Cannot create standard XMLInputFactory instance of Java runtime.", (Throwable)exception);
   }

   protected XMLOutputFactory createOutputFactory() {
      Object exception = null;

      try {
         Class staxOutputFactory = JVM.getStaxOutputFactory();
         if (staxOutputFactory != null) {
            return (XMLOutputFactory)staxOutputFactory.newInstance();
         }

         throw new StreamException("Java runtime has no standard XMLOutputFactory implementation.", (Throwable)exception);
      } catch (InstantiationException var3) {
         exception = var3;
      } catch (IllegalAccessException var4) {
         exception = var4;
      } catch (ClassNotFoundException var5) {
         exception = var5;
      }

      throw new StreamException("Cannot create standard XMLOutputFactory instance of Java runtime.", (Throwable)exception);
   }
}
