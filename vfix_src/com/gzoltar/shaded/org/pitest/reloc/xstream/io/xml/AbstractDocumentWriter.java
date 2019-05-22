package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastStack;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDocumentWriter extends AbstractXmlWriter implements DocumentWriter {
   private final List result;
   private final FastStack nodeStack;

   public AbstractDocumentWriter(Object container, NameCoder nameCoder) {
      super(nameCoder);
      this.result = new ArrayList();
      this.nodeStack = new FastStack(16);
      if (container != null) {
         this.nodeStack.push(container);
         this.result.add(container);
      }

   }

   /** @deprecated */
   public AbstractDocumentWriter(Object container, XmlFriendlyReplacer replacer) {
      this(container, (NameCoder)replacer);
   }

   public final void startNode(String name) {
      Object node = this.createNode(name);
      this.nodeStack.push(node);
   }

   protected abstract Object createNode(String var1);

   public final void endNode() {
      this.endNodeInternally();
      Object node = this.nodeStack.pop();
      if (this.nodeStack.size() == 0) {
         this.result.add(node);
      }

   }

   public void endNodeInternally() {
   }

   protected final Object getCurrent() {
      return this.nodeStack.peek();
   }

   public List getTopLevelNodes() {
      return this.result;
   }

   public void flush() {
   }

   public void close() {
   }
}
