package org.codehaus.groovy.runtime.callsite;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.codehaus.groovy.reflection.ClassLoaderForClassArtifacts;

public class CallSiteClassLoader extends ClassLoaderForClassArtifacts {
   private static final Set<String> KNOWN_CLASSES = new HashSet();

   public CallSiteClassLoader(Class klazz) {
      super(klazz);
   }

   protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
      if (KNOWN_CLASSES.contains(name)) {
         return this.getClass().getClassLoader().loadClass(name);
      } else {
         try {
            return super.loadClass(name, resolve);
         } catch (ClassNotFoundException var4) {
            return this.getClass().getClassLoader().loadClass(name);
         }
      }
   }

   static {
      Collections.addAll(KNOWN_CLASSES, new String[]{"org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite", "org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite", "org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite", "org.codehaus.groovy.runtime.callsite.CallSite", "org.codehaus.groovy.runtime.callsite.CallSiteArray", "groovy.lang.MetaMethod", "groovy.lang.MetaClassImpl"});
   }
}
