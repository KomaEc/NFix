package com.gzoltar.shaded.javassist.tools.reflect;

import com.gzoltar.shaded.javassist.CannotCompileException;
import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.javassist.NotFoundException;

public class Loader extends com.gzoltar.shaded.javassist.Loader {
   protected Reflection reflection;

   public static void main(String[] args) throws Throwable {
      Loader cl = new Loader();
      cl.run(args);
   }

   public Loader() throws CannotCompileException, NotFoundException {
      this.delegateLoadingOf("com.gzoltar.shaded.javassist.tools.reflect.Loader");
      this.reflection = new Reflection();
      ClassPool pool = ClassPool.getDefault();
      this.addTranslator(pool, this.reflection);
   }

   public boolean makeReflective(String clazz, String metaobject, String metaclass) throws CannotCompileException, NotFoundException {
      return this.reflection.makeReflective(clazz, metaobject, metaclass);
   }
}
