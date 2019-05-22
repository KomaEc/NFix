package org.apache.maven.doxia.sink;

import java.util.Enumeration;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import org.apache.maven.doxia.markup.XmlMarkup;

public abstract class AbstractXmlSink extends SinkAdapter implements XmlMarkup {
   protected void writeStartTag(Tag t) {
      this.writeStartTag(t, (MutableAttributeSet)null);
   }

   protected void writeStartTag(Tag t, MutableAttributeSet att) {
      this.writeStartTag(t, att, false);
   }

   protected void writeStartTag(Tag t, MutableAttributeSet att, boolean isSimpleTag) {
      if (t == null) {
         throw new IllegalArgumentException("A tag is required");
      } else {
         StringBuffer sb = new StringBuffer();
         sb.append('<');
         sb.append(t.toString());
         if (att != null) {
            Enumeration names = att.getAttributeNames();

            while(names.hasMoreElements()) {
               Object key = names.nextElement();
               Object value = att.getAttribute(key);
               if (!(value instanceof AttributeSet)) {
                  sb.append(' ').append(key.toString()).append('=').append('"').append(value.toString()).append('"');
               }
            }
         }

         if (isSimpleTag) {
            sb.append(' ').append('/');
         }

         sb.append('>');
         if (isSimpleTag) {
            sb.append(EOL);
         }

         this.write(sb.toString());
      }
   }

   protected void writeEndTag(Tag t) {
      this.writeEndTagWithoutEOL(t);
      this.write(EOL);
   }

   protected void writeEndTagWithoutEOL(Tag t) {
      StringBuffer sb = new StringBuffer();
      sb.append('<');
      sb.append('/');
      sb.append(t.toString());
      sb.append('>');
      this.write(sb.toString());
   }

   protected void writeSimpleTag(Tag t) {
      this.writeSimpleTag(t, (MutableAttributeSet)null);
   }

   protected void writeSimpleTag(Tag t, MutableAttributeSet att) {
      this.writeStartTag(t, att, true);
   }

   protected abstract void write(String var1);
}
