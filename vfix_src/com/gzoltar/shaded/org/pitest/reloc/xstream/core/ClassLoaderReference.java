package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CompositeClassLoader;

public final class ClassLoaderReference {
   private transient ClassLoader reference;

   public ClassLoaderReference(ClassLoader reference) {
      this.setReference(reference);
   }

   public ClassLoader getReference() {
      return this.reference;
   }

   public void setReference(ClassLoader reference) {
      this.reference = reference instanceof com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.ClassLoaderReference ? ((com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.ClassLoaderReference)reference).getReference() : reference;
   }

   private Object readResolve() {
      this.reference = new CompositeClassLoader();
      return this;
   }
}
