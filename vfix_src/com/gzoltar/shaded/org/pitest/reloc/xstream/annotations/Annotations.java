package com.gzoltar.shaded.org.pitest.reloc.xstream.annotations;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStream;

/** @deprecated */
@Deprecated
public class Annotations {
   private Annotations() {
   }

   /** @deprecated */
   @Deprecated
   public static synchronized void configureAliases(XStream xstream, Class<?>... topLevelClasses) {
      xstream.processAnnotations(topLevelClasses);
   }
}
