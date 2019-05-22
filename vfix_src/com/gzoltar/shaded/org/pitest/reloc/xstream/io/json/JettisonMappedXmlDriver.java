package com.gzoltar.shaded.org.pitest.reloc.xstream.io.json;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.AbstractDriver;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.QNameMap;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.StaxReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.StaxWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import javax.xml.stream.XMLStreamException;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLInputFactory;
import org.codehaus.jettison.mapped.MappedXMLOutputFactory;

public class JettisonMappedXmlDriver extends AbstractDriver {
   protected final MappedXMLOutputFactory mof;
   protected final MappedXMLInputFactory mif;
   protected final MappedNamespaceConvention convention;
   protected final boolean useSerializeAsArray;

   public JettisonMappedXmlDriver() {
      this(new Configuration());
   }

   public JettisonMappedXmlDriver(Configuration config) {
      this(config, true);
   }

   public JettisonMappedXmlDriver(Configuration config, boolean useSerializeAsArray) {
      this.mof = new MappedXMLOutputFactory(config);
      this.mif = new MappedXMLInputFactory(config);
      this.convention = new MappedNamespaceConvention(config);
      this.useSerializeAsArray = useSerializeAsArray;
   }

   public HierarchicalStreamReader createReader(Reader reader) {
      try {
         return new StaxReader(new QNameMap(), this.mif.createXMLStreamReader(reader), this.getNameCoder());
      } catch (XMLStreamException var3) {
         throw new StreamException(var3);
      }
   }

   public HierarchicalStreamReader createReader(InputStream input) {
      try {
         return new StaxReader(new QNameMap(), this.mif.createXMLStreamReader(input), this.getNameCoder());
      } catch (XMLStreamException var3) {
         throw new StreamException(var3);
      }
   }

   public HierarchicalStreamReader createReader(URL in) {
      InputStream instream = null;

      StaxReader var3;
      try {
         instream = in.openStream();
         var3 = new StaxReader(new QNameMap(), this.mif.createXMLStreamReader(in.toExternalForm(), instream), this.getNameCoder());
      } catch (XMLStreamException var13) {
         throw new StreamException(var13);
      } catch (IOException var14) {
         throw new StreamException(var14);
      } finally {
         if (instream != null) {
            try {
               instream.close();
            } catch (IOException var12) {
            }
         }

      }

      return var3;
   }

   public HierarchicalStreamReader createReader(File in) {
      FileInputStream instream = null;

      StaxReader var3;
      try {
         instream = new FileInputStream(in);
         var3 = new StaxReader(new QNameMap(), this.mif.createXMLStreamReader(in.toURI().toASCIIString(), instream), this.getNameCoder());
      } catch (XMLStreamException var13) {
         throw new StreamException(var13);
      } catch (IOException var14) {
         throw new StreamException(var14);
      } finally {
         if (instream != null) {
            try {
               instream.close();
            } catch (IOException var12) {
            }
         }

      }

      return var3;
   }

   public HierarchicalStreamWriter createWriter(Writer writer) {
      try {
         return (HierarchicalStreamWriter)(this.useSerializeAsArray ? new JettisonStaxWriter(new QNameMap(), this.mof.createXMLStreamWriter(writer), this.getNameCoder(), this.convention) : new StaxWriter(new QNameMap(), this.mof.createXMLStreamWriter(writer), this.getNameCoder()));
      } catch (XMLStreamException var3) {
         throw new StreamException(var3);
      }
   }

   public HierarchicalStreamWriter createWriter(OutputStream output) {
      try {
         return (HierarchicalStreamWriter)(this.useSerializeAsArray ? new JettisonStaxWriter(new QNameMap(), this.mof.createXMLStreamWriter(output), this.getNameCoder(), this.convention) : new StaxWriter(new QNameMap(), this.mof.createXMLStreamWriter(output), this.getNameCoder()));
      } catch (XMLStreamException var3) {
         throw new StreamException(var3);
      }
   }
}
