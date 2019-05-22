package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class PogoMetaMethodSite extends MetaMethodSite {
   public PogoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
      super(site, metaClass, metaMethod, params);
   }

   public Object invoke(Object receiver, Object[] args) throws Throwable {
      MetaClassHelper.unwrap(args);

      try {
         return this.metaMethod.doMethodInvoke(receiver, args);
      } catch (GroovyRuntimeException var4) {
         throw ScriptBytecodeAdapter.unwrap(var4);
      }
   }

   public Object callCurrent(GroovyObject receiver, Object[] args) throws Throwable {
      if (this.checkCall(receiver, (Object[])args)) {
         try {
            return this.invoke(receiver, args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCallCurrent(this, receiver, args);
      }
   }

   public Object call(Object receiver, Object[] args) throws Throwable {
      if (this.checkCall(receiver, args)) {
         try {
            return this.invoke(receiver, args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCall(this, receiver, args);
      }
   }

   protected boolean checkCall(Object receiver, Object[] args) {
      try {
         return this.usage.get() == 0 && ((GroovyObject)receiver).getMetaClass() == this.metaClass && MetaClassHelper.sameClasses(this.params, args);
      } catch (NullPointerException var4) {
         if (receiver == null) {
            return false;
         } else {
            throw var4;
         }
      } catch (ClassCastException var5) {
         if (!(receiver instanceof GroovyObject)) {
            return false;
         } else {
            throw var5;
         }
      }
   }

   protected boolean checkCall(Object receiver) {
      try {
         return this.usage.get() == 0 && ((GroovyObject)receiver).getMetaClass() == this.metaClass && MetaClassHelper.sameClasses(this.params);
      } catch (NullPointerException var3) {
         if (receiver == null) {
            return false;
         } else {
            throw var3;
         }
      } catch (ClassCastException var4) {
         if (!(receiver instanceof GroovyObject)) {
            return false;
         } else {
            throw var4;
         }
      }
   }

   protected boolean checkCall(Object receiver, Object arg1) {
      try {
         return this.usage.get() == 0 && ((GroovyObject)receiver).getMetaClass() == this.metaClass && MetaClassHelper.sameClasses(this.params, arg1);
      } catch (NullPointerException var4) {
         if (receiver == null) {
            return false;
         } else {
            throw var4;
         }
      } catch (ClassCastException var5) {
         if (!(receiver instanceof GroovyObject)) {
            return false;
         } else {
            throw var5;
         }
      }
   }

   protected boolean checkCall(Object receiver, Object arg1, Object arg2) {
      try {
         return this.usage.get() == 0 && ((GroovyObject)receiver).getMetaClass() == this.metaClass && MetaClassHelper.sameClasses(this.params, arg1, arg2);
      } catch (NullPointerException var5) {
         if (receiver == null) {
            return false;
         } else {
            throw var5;
         }
      } catch (ClassCastException var6) {
         if (!(receiver instanceof GroovyObject)) {
            return false;
         } else {
            throw var6;
         }
      }
   }

   protected boolean checkCall(Object receiver, Object arg1, Object arg2, Object arg3) {
      try {
         return this.usage.get() == 0 && ((GroovyObject)receiver).getMetaClass() == this.metaClass && MetaClassHelper.sameClasses(this.params, arg1, arg2, arg3);
      } catch (NullPointerException var6) {
         if (receiver == null) {
            return false;
         } else {
            throw var6;
         }
      } catch (ClassCastException var7) {
         if (!(receiver instanceof GroovyObject)) {
            return false;
         } else {
            throw var7;
         }
      }
   }

   protected boolean checkCall(Object receiver, Object arg1, Object arg2, Object arg3, Object arg4) {
      try {
         return this.usage.get() == 0 && ((GroovyObject)receiver).getMetaClass() == this.metaClass && MetaClassHelper.sameClasses(this.params, arg1, arg2, arg3, arg4);
      } catch (NullPointerException var7) {
         if (receiver == null) {
            return false;
         } else {
            throw var7;
         }
      } catch (ClassCastException var8) {
         if (!(receiver instanceof GroovyObject)) {
            return false;
         } else {
            throw var8;
         }
      }
   }

   public static CallSite createPogoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object[] args) {
      return metaMethod.getClass() == CachedMethod.class ? createCachedMethodSite(site, metaClass, (CachedMethod)metaMethod, params, args) : createNonAwareCallSite(site, metaClass, metaMethod, params, args);
   }

   private static CallSite createNonAwareCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object[] args) {
      if (metaMethod.correctArguments(args) == args && noWrappers(args)) {
         return (CallSite)(noCoerce(metaMethod, args) ? new PogoMetaMethodSite.PogoMetaMethodSiteNoUnwrap(site, metaClass, metaMethod, params) : new PogoMetaMethodSite.PogoMetaMethodSiteNoUnwrapNoCoerce(site, metaClass, metaMethod, params));
      } else {
         return new PogoMetaMethodSite(site, metaClass, metaMethod, params);
      }
   }

   public static CallSite createCachedMethodSite(CallSite site, MetaClassImpl metaClass, CachedMethod metaMethod, Class[] params, Object[] args) {
      if (metaMethod.correctArguments(args) == args && noWrappers(args)) {
         return (CallSite)(noCoerce(metaMethod, args) ? new PogoMetaMethodSite.PogoCachedMethodSiteNoUnwrap(site, metaClass, metaMethod, params) : metaMethod.createPogoMetaMethodSite(site, metaClass, params));
      } else {
         return new PogoMetaMethodSite.PogoCachedMethodSite(site, metaClass, metaMethod, params);
      }
   }

   public static class PogoMetaMethodSiteNoUnwrapNoCoerce extends PogoMetaMethodSite {
      public PogoMetaMethodSiteNoUnwrapNoCoerce(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public final Object invoke(Object receiver, Object[] args) throws Throwable {
         try {
            return this.metaMethod.invoke(receiver, args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      }
   }

   public static class PogoMetaMethodSiteNoUnwrap extends PogoMetaMethodSite {
      public PogoMetaMethodSiteNoUnwrap(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public final Object invoke(Object receiver, Object[] args) throws Throwable {
         try {
            return this.metaMethod.doMethodInvoke(receiver, args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      }
   }

   public static class PogoCachedMethodSiteNoUnwrapNoCoerce extends PogoMetaMethodSite.PogoCachedMethodSite {
      public PogoCachedMethodSiteNoUnwrapNoCoerce(CallSite site, MetaClassImpl metaClass, CachedMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public final Object invoke(Object receiver, Object[] args) throws Throwable {
         try {
            return this.reflect.invoke(receiver, args);
         } catch (InvocationTargetException var5) {
            Throwable cause = var5.getCause();
            if (cause instanceof GroovyRuntimeException) {
               throw ScriptBytecodeAdapter.unwrap((GroovyRuntimeException)cause);
            } else {
               throw cause;
            }
         }
      }
   }

   public static class PogoCachedMethodSiteNoUnwrap extends PogoMetaMethodSite.PogoCachedMethodSite {
      public PogoCachedMethodSiteNoUnwrap(CallSite site, MetaClassImpl metaClass, CachedMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public final Object invoke(Object receiver, Object[] args) throws Throwable {
         args = this.metaMethod.coerceArgumentsToClasses(args);

         try {
            return this.reflect.invoke(receiver, args);
         } catch (InvocationTargetException var5) {
            Throwable cause = var5.getCause();
            if (cause instanceof GroovyRuntimeException) {
               throw ScriptBytecodeAdapter.unwrap((GroovyRuntimeException)cause);
            } else {
               throw cause;
            }
         }
      }
   }

   public static class PogoCachedMethodSite extends PogoMetaMethodSite {
      final Method reflect;

      public PogoCachedMethodSite(CallSite site, MetaClassImpl metaClass, CachedMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
         this.reflect = metaMethod.setAccessible();
      }

      public Object invoke(Object receiver, Object[] args) throws Throwable {
         MetaClassHelper.unwrap(args);
         args = this.metaMethod.coerceArgumentsToClasses(args);

         try {
            return this.reflect.invoke(receiver, args);
         } catch (InvocationTargetException var5) {
            Throwable cause = var5.getCause();
            if (cause instanceof GroovyRuntimeException) {
               throw ScriptBytecodeAdapter.unwrap((GroovyRuntimeException)cause);
            } else {
               throw cause;
            }
         }
      }
   }
}
