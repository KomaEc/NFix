package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ErrorWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastStack;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.AttributeNameIterator;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.util.Iterator;

public abstract class AbstractDocumentReader extends AbstractXmlReader implements DocumentReader {
   private FastStack pointers;
   private Object current;

   protected AbstractDocumentReader(Object rootElement) {
      this(rootElement, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   protected AbstractDocumentReader(Object rootElement, NameCoder nameCoder) {
      super(nameCoder);
      this.pointers = new FastStack(16);
      this.current = rootElement;
      this.pointers.push(new AbstractDocumentReader.Pointer());
      this.reassignCurrentElement(this.current);
   }

   /** @deprecated */
   protected AbstractDocumentReader(Object rootElement, XmlFriendlyReplacer replacer) {
      this(rootElement, (NameCoder)replacer);
   }

   protected abstract void reassignCurrentElement(Object var1);

   protected abstract Object getParent();

   protected abstract Object getChild(int var1);

   protected abstract int getChildCount();

   public boolean hasMoreChildren() {
      AbstractDocumentReader.Pointer pointer = (AbstractDocumentReader.Pointer)this.pointers.peek();
      return pointer.v < this.getChildCount();
   }

   public void moveUp() {
      this.current = this.getParent();
      this.pointers.popSilently();
      this.reassignCurrentElement(this.current);
   }

   public void moveDown() {
      AbstractDocumentReader.Pointer pointer = (AbstractDocumentReader.Pointer)this.pointers.peek();
      this.pointers.push(new AbstractDocumentReader.Pointer());
      this.current = this.getChild(pointer.v);
      ++pointer.v;
      this.reassignCurrentElement(this.current);
   }

   public Iterator getAttributeNames() {
      return new AttributeNameIterator(this);
   }

   public void appendErrors(ErrorWriter errorWriter) {
   }

   public Object getCurrent() {
      return this.current;
   }

   public void close() {
   }

   private static class Pointer {
      public int v;

      private Pointer() {
      }

      // $FF: synthetic method
      Pointer(Object x0) {
         this();
      }
   }
}
