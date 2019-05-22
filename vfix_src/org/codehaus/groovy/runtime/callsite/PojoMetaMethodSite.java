package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.NullObject;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class PojoMetaMethodSite extends MetaMethodSite {
   protected final int version;

   public PojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
      super(site, metaClass, metaMethod, params);
      this.version = metaClass.getVersion();
   }

   public Object invoke(Object receiver, Object[] args) throws Throwable {
      MetaClassHelper.unwrap(args);
      return this.metaMethod.doMethodInvoke(receiver, args);
   }

   public Object call(Object receiver, Object[] args) throws Throwable {
      return this.checkCall(receiver, args) ? this.invoke(receiver, args) : CallSiteArray.defaultCall(this, receiver, args);
   }

   protected final boolean checkPojoMetaClass() {
      return this.usage.get() == 0 && ((MetaClassImpl)this.metaClass).getVersion() == this.version;
   }

   protected final boolean checkCall(Object receiver, Object[] args) {
      try {
         return receiver.getClass() == this.metaClass.getTheClass() && this.checkPojoMetaClass() && MetaClassHelper.sameClasses(this.params, args);
      } catch (NullPointerException var4) {
         if (receiver == null) {
            return this.checkCall(NullObject.getNullObject(), (Object[])args);
         } else {
            throw var4;
         }
      }
   }

   protected final boolean checkCall(Object receiver) {
      try {
         return receiver.getClass() == this.metaClass.getTheClass() && this.checkPojoMetaClass() && MetaClassHelper.sameClasses(this.params);
      } catch (NullPointerException var3) {
         if (receiver == null) {
            return this.checkCall(NullObject.getNullObject());
         } else {
            throw var3;
         }
      }
   }

   protected final boolean checkCall(Object receiver, Object arg1) {
      try {
         return receiver.getClass() == this.metaClass.getTheClass() && this.checkPojoMetaClass() && MetaClassHelper.sameClasses(this.params, arg1);
      } catch (NullPointerException var4) {
         if (receiver == null) {
            return this.checkCall(NullObject.getNullObject(), (Object)arg1);
         } else {
            throw var4;
         }
      }
   }

   protected final boolean checkCall(Object receiver, Object arg1, Object arg2) {
      try {
         return receiver.getClass() == this.metaClass.getTheClass() && this.checkPojoMetaClass() && MetaClassHelper.sameClasses(this.params, arg1, arg2);
      } catch (NullPointerException var5) {
         if (receiver == null) {
            return this.checkCall(NullObject.getNullObject(), arg1, arg2);
         } else {
            throw var5;
         }
      }
   }

   protected final boolean checkCall(Object receiver, Object arg1, Object arg2, Object arg3) {
      try {
         return receiver.getClass() == this.metaClass.getTheClass() && this.checkPojoMetaClass() && MetaClassHelper.sameClasses(this.params, arg1, arg2, arg3);
      } catch (NullPointerException var6) {
         if (receiver == null) {
            return this.checkCall(NullObject.getNullObject(), arg1, arg2, arg3);
         } else {
            throw var6;
         }
      }
   }

   protected final boolean checkCall(Object receiver, Object arg1, Object arg2, Object arg3, Object arg4) {
      try {
         return receiver.getClass() == this.metaClass.getTheClass() && this.checkPojoMetaClass() && MetaClassHelper.sameClasses(this.params, arg1, arg2, arg3, arg4);
      } catch (NullPointerException var7) {
         if (receiver == null) {
            return this.checkCall(NullObject.getNullObject(), arg1, arg2, arg3, arg4);
         } else {
            throw var7;
         }
      }
   }

   public static CallSite createPojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      if (metaMethod instanceof CallSiteAwareMetaMethod) {
         return ((CallSiteAwareMetaMethod)metaMethod).createPojoCallSite(site, metaClass, metaMethod, params, receiver, args);
      } else {
         return metaMethod.getClass() == CachedMethod.class ? createCachedMethodSite(site, metaClass, (CachedMethod)metaMethod, params, args) : createNonAwareCallSite(site, metaClass, metaMethod, params, args);
      }
   }

   public static CallSite createCachedMethodSite(CallSite site, MetaClassImpl metaClass, CachedMethod metaMethod, Class[] params, Object[] args) {
      if (metaMethod.correctArguments(args) == args && noWrappers(args)) {
         return (CallSite)(noCoerce(metaMethod, args) ? new PojoMetaMethodSite.PojoCachedMethodSiteNoUnwrap(site, metaClass, metaMethod, params) : metaMethod.createPojoMetaMethodSite(site, metaClass, params));
      } else {
         return new PojoMetaMethodSite.PojoCachedMethodSite(site, metaClass, metaMethod, params);
      }
   }

   public static CallSite createNonAwareCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object[] args) {
      if (metaMethod.correctArguments(args) == args && noWrappers(args)) {
         return (CallSite)(noCoerce(metaMethod, args) ? new PojoMetaMethodSite.PojoMetaMethodSiteNoUnwrap(site, metaClass, metaMethod, params) : new PojoMetaMethodSite.PojoMetaMethodSiteNoUnwrapNoCoerce(site, metaClass, metaMethod, params));
      } else {
         return new PojoMetaMethodSite(site, metaClass, metaMethod, params);
      }
   }

   public static class PojoMetaMethodSiteNoUnwrapNoCoerce extends PojoMetaMethodSite {
      public PojoMetaMethodSiteNoUnwrapNoCoerce(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
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

   public static class PojoMetaMethodSiteNoUnwrap extends PojoMetaMethodSite {
      public PojoMetaMethodSiteNoUnwrap(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
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

   public static class PojoCachedMethodSiteNoUnwrapNoCoerce extends PojoMetaMethodSite.PojoCachedMethodSite {
      public PojoCachedMethodSiteNoUnwrapNoCoerce(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
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

   public static class PojoCachedMethodSiteNoUnwrap extends PojoMetaMethodSite.PojoCachedMethodSite {
      public PojoCachedMethodSiteNoUnwrap(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
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

   public static class PojoCachedMethodSite extends PojoMetaMethodSite {
      final Method reflect;

      public PojoCachedMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
         this.reflect = ((CachedMethod)metaMethod).setAccessible();
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
