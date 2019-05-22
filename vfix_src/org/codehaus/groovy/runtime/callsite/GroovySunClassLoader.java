package org.codehaus.groovy.runtime.callsite;

import groovyjarjarasm.asm.ClassReader;
import groovyjarjarasm.asm.ClassWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.codehaus.groovy.reflection.SunClassLoader;

public class GroovySunClassLoader extends SunClassLoader {
   public static final SunClassLoader sunVM = (SunClassLoader)AccessController.doPrivileged(new PrivilegedAction<SunClassLoader>() {
      public SunClassLoader run() {
         try {
            if (GroovySunClassLoader.sunVM != null) {
               return new GroovySunClassLoader();
            }
         } catch (Throwable var2) {
         }

         return null;
      }
   });

   protected GroovySunClassLoader() throws Throwable {
      this.loadAbstract();
      this.loadFromRes("org.codehaus.groovy.runtime.callsite.MetaClassSite");
      this.loadFromRes("org.codehaus.groovy.runtime.callsite.MetaMethodSite");
      this.loadFromRes("org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite");
      this.loadFromRes("org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite");
      this.loadFromRes("org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite");
   }

   private void loadAbstract() throws IOException {
      InputStream asStream = GroovySunClassLoader.class.getClass().getClassLoader().getResourceAsStream(resName("org.codehaus.groovy.runtime.callsite.AbstractCallSite"));
      ClassReader reader = new ClassReader(asStream);
      ClassWriter cw = new ClassWriter(1) {
         public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, "sun/reflect/GroovyMagic", interfaces);
         }
      };
      reader.accept(cw, 1);
      asStream.close();
      this.define(cw.toByteArray(), "org.codehaus.groovy.runtime.callsite.AbstractCallSite");
   }
}
