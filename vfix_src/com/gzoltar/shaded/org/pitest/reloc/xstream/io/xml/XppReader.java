package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ErrorWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.io.IOException;
import java.io.Reader;

public class XppReader extends AbstractPullReader {
   private final XmlPullParser parser;
   private final Reader reader;

   public XppReader(Reader reader, XmlPullParser parser) {
      this(reader, parser, new XmlFriendlyNameCoder());
   }

   public XppReader(Reader reader, XmlPullParser parser, NameCoder nameCoder) {
      super(nameCoder);
      this.parser = parser;
      this.reader = reader;

      try {
         parser.setInput(this.reader);
      } catch (XmlPullParserException var5) {
         throw new StreamException(var5);
      }

      this.moveDown();
   }

   /** @deprecated */
   public XppReader(Reader reader) {
      this(reader, new XmlFriendlyReplacer());
   }

   /** @deprecated */
   public XppReader(Reader reader, XmlFriendlyReplacer replacer) {
      super(replacer);

      try {
         this.parser = this.createParser();
         this.reader = reader;
         this.parser.setInput(this.reader);
         this.moveDown();
      } catch (XmlPullParserException var4) {
         throw new StreamException(var4);
      }
   }

   /** @deprecated */
   protected XmlPullParser createParser() {
      Object exception = null;

      try {
         return (XmlPullParser)Class.forName("com.gzoltar.shaded.org.pitest.reloc.xmlpull.mxp1.MXParser", true, XmlPullParser.class.getClassLoader()).newInstance();
      } catch (InstantiationException var3) {
         exception = var3;
      } catch (IllegalAccessException var4) {
         exception = var4;
      } catch (ClassNotFoundException var5) {
         exception = var5;
      }

      throw new StreamException("Cannot create Xpp3 parser instance.", (Throwable)exception);
   }

   protected int pullNextEvent() {
      try {
         switch(this.parser.next()) {
         case 0:
         case 2:
            return 1;
         case 1:
         case 3:
            return 2;
         case 4:
            return 3;
         case 5:
         case 6:
         case 7:
         case 8:
         default:
            return 0;
         case 9:
            return 4;
         }
      } catch (XmlPullParserException var2) {
         throw new StreamException(var2);
      } catch (IOException var3) {
         throw new StreamException(var3);
      }
   }

   protected String pullElementName() {
      return this.parser.getName();
   }

   protected String pullText() {
      return this.parser.getText();
   }

   public String getAttribute(String name) {
      return this.parser.getAttributeValue((String)null, this.encodeAttribute(name));
   }

   public String getAttribute(int index) {
      return this.parser.getAttributeValue(index);
   }

   public int getAttributeCount() {
      return this.parser.getAttributeCount();
   }

   public String getAttributeName(int index) {
      return this.decodeAttribute(this.parser.getAttributeName(index));
   }

   public void appendErrors(ErrorWriter errorWriter) {
      errorWriter.add("line number", String.valueOf(this.parser.getLineNumber()));
   }

   public void close() {
      try {
         this.reader.close();
      } catch (IOException var2) {
         throw new StreamException(var2);
      }
   }
}
