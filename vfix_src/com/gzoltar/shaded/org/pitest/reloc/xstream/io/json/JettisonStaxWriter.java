package com.gzoltar.shaded.org.pitest.reloc.xstream.io.json;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.QNameMap;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.StaxWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.XmlFriendlyReplacer;
import java.util.Collection;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.codehaus.jettison.AbstractXMLStreamWriter;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;

public class JettisonStaxWriter extends StaxWriter {
   private final MappedNamespaceConvention convention;

   public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, boolean writeEnclosingDocument, boolean namespaceRepairingMode, NameCoder nameCoder, MappedNamespaceConvention convention) throws XMLStreamException {
      super(qnameMap, out, writeEnclosingDocument, namespaceRepairingMode, nameCoder);
      this.convention = convention;
   }

   /** @deprecated */
   public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, boolean writeEnclosingDocument, boolean namespaceRepairingMode, XmlFriendlyReplacer replacer, MappedNamespaceConvention convention) throws XMLStreamException {
      this(qnameMap, out, writeEnclosingDocument, namespaceRepairingMode, (NameCoder)replacer, convention);
   }

   public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, boolean writeEnclosingDocument, boolean namespaceRepairingMode, MappedNamespaceConvention convention) throws XMLStreamException {
      super(qnameMap, out, writeEnclosingDocument, namespaceRepairingMode);
      this.convention = convention;
   }

   public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, MappedNamespaceConvention convention) throws XMLStreamException {
      super(qnameMap, out);
      this.convention = convention;
   }

   public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, NameCoder nameCoder, MappedNamespaceConvention convention) throws XMLStreamException {
      super(qnameMap, out, nameCoder);
      this.convention = convention;
   }

   public void startNode(String name, Class clazz) {
      XMLStreamWriter out = this.getXMLStreamWriter();
      if (clazz != null && out instanceof AbstractXMLStreamWriter && (Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz) || clazz.isArray())) {
         QName qname = this.getQNameMap().getQName(this.encodeNode(name));
         String prefix = qname.getPrefix();
         String uri = qname.getNamespaceURI();
         String key = this.convention.createKey(prefix, uri, qname.getLocalPart());
         if (!((AbstractXMLStreamWriter)out).getSerializedAsArrays().contains(key)) {
            ((AbstractXMLStreamWriter)out).seriliazeAsArray(key);
         }
      }

      this.startNode(name);
   }
}
