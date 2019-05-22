package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/** @deprecated */
public class SjsxpDriver extends StaxDriver {
   /** @deprecated */
   public SjsxpDriver() {
   }

   /** @deprecated */
   public SjsxpDriver(QNameMap qnameMap, XmlFriendlyNameCoder nameCoder) {
      super(qnameMap, (NameCoder)nameCoder);
   }

   /** @deprecated */
   public SjsxpDriver(QNameMap qnameMap) {
      super(qnameMap);
   }

   /** @deprecated */
   public SjsxpDriver(XmlFriendlyNameCoder nameCoder) {
      super((NameCoder)nameCoder);
   }

   /** @deprecated */
   protected XMLInputFactory createInputFactory() {
      Object exception = null;

      try {
         return (XMLInputFactory)Class.forName("com.sun.xml.internal.stream.XMLInputFactoryImpl").newInstance();
      } catch (InstantiationException var3) {
         exception = var3;
      } catch (IllegalAccessException var4) {
         exception = var4;
      } catch (ClassNotFoundException var5) {
         exception = var5;
      }

      throw new StreamException("Cannot create SJSXP (Sun JDK 6 StAX) XMLInputFaqctory instance.", (Throwable)exception);
   }

   /** @deprecated */
   protected XMLOutputFactory createOutputFactory() {
      Object exception = null;

      try {
         return (XMLOutputFactory)Class.forName("com.sun.xml.internal.stream.XMLOutputFactoryImpl").newInstance();
      } catch (InstantiationException var3) {
         exception = var3;
      } catch (IllegalAccessException var4) {
         exception = var4;
      } catch (ClassNotFoundException var5) {
         exception = var5;
      }

      throw new StreamException("Cannot create SJSXP (Sun JDK 6 StAX) XMLOutputFaqctory instance.", (Throwable)exception);
   }
}
