package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.path.Path;

public interface ReferencingMarshallingContext extends MarshallingContext {
   /** @deprecated */
   Path currentPath();

   Object lookupReference(Object var1);

   void replace(Object var1, Object var2);

   void registerImplicit(Object var1);
}
