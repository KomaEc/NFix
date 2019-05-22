package groovy.lang;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.reflection.stdclasses.CachedClosureClass;
import org.codehaus.groovy.runtime.CurriedClosure;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public abstract class Closure extends GroovyObjectSupport implements Cloneable, Runnable, Serializable {
   public static final int OWNER_FIRST = 0;
   public static final int DELEGATE_FIRST = 1;
   public static final int OWNER_ONLY = 2;
   public static final int DELEGATE_ONLY = 3;
   public static final int TO_SELF = 4;
   public static final int DONE = 1;
   public static final int SKIP = 2;
   private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
   private Object delegate;
   private Object owner;
   private Object thisObject;
   private int resolveStrategy;
   private int directive;
   protected Class[] parameterTypes;
   protected int maximumNumberOfParameters;
   private static final long serialVersionUID = 4368710879820278874L;

   public Closure(Object owner, Object thisObject) {
      this.resolveStrategy = 0;
      this.owner = owner;
      this.delegate = owner;
      this.thisObject = thisObject;
      CachedClosureClass cachedClass = (CachedClosureClass)ReflectionCache.getCachedClass(this.getClass());
      this.parameterTypes = cachedClass.getParameterTypes();
      this.maximumNumberOfParameters = cachedClass.getMaximumNumberOfParameters();
   }

   public Closure(Object owner) {
      this(owner, (Object)null);
   }

   public void setResolveStrategy(int resolveStrategy) {
      this.resolveStrategy = resolveStrategy;
   }

   public int getResolveStrategy() {
      return this.resolveStrategy;
   }

   public Object getThisObject() {
      return this.thisObject;
   }

   public Object getProperty(String property) {
      if ("delegate".equals(property)) {
         return this.getDelegate();
      } else if ("owner".equals(property)) {
         return this.getOwner();
      } else if ("maximumNumberOfParameters".equals(property)) {
         return this.getMaximumNumberOfParameters();
      } else if ("parameterTypes".equals(property)) {
         return this.getParameterTypes();
      } else if ("metaClass".equals(property)) {
         return this.getMetaClass();
      } else if ("class".equals(property)) {
         return this.getClass();
      } else if ("directive".equals(property)) {
         return this.getDirective();
      } else {
         switch(this.resolveStrategy) {
         case 1:
            return this.getPropertyDelegateFirst(property);
         case 2:
            return InvokerHelper.getProperty(this.owner, property);
         case 3:
            return InvokerHelper.getProperty(this.delegate, property);
         case 4:
            return super.getProperty(property);
         default:
            return this.getPropertyOwnerFirst(property);
         }
      }
   }

   private Object getPropertyDelegateFirst(String property) {
      return this.delegate == null ? this.getPropertyOwnerFirst(property) : this.getPropertyTryThese(property, this.delegate, this.owner);
   }

   private Object getPropertyOwnerFirst(String property) {
      return this.getPropertyTryThese(property, this.owner, this.delegate);
   }

   private Object getPropertyTryThese(String property, Object firstTry, Object secondTry) {
      try {
         return InvokerHelper.getProperty(firstTry, property);
      } catch (MissingPropertyException var7) {
         if (secondTry != null && firstTry != this && firstTry != secondTry) {
            try {
               return InvokerHelper.getProperty(secondTry, property);
            } catch (GroovyRuntimeException var6) {
            }
         }

         throw var7;
      }
   }

   public void setProperty(String property, Object newValue) {
      if ("delegate".equals(property)) {
         this.setDelegate(newValue);
      } else if ("metaClass".equals(property)) {
         this.setMetaClass((MetaClass)newValue);
      } else if ("resolveStrategy".equals(property)) {
         this.setResolveStrategy(((Number)newValue).intValue());
      } else {
         switch(this.resolveStrategy) {
         case 1:
            this.setPropertyDelegateFirst(property, newValue);
            break;
         case 2:
            InvokerHelper.setProperty(this.owner, property, newValue);
            break;
         case 3:
            InvokerHelper.setProperty(this.delegate, property, newValue);
            break;
         case 4:
            super.setProperty(property, newValue);
            break;
         default:
            this.setPropertyOwnerFirst(property, newValue);
         }
      }

   }

   private void setPropertyDelegateFirst(String property, Object newValue) {
      if (this.delegate == null) {
         this.setPropertyOwnerFirst(property, newValue);
      } else {
         this.setPropertyTryThese(property, newValue, this.delegate, this.owner);
      }

   }

   private void setPropertyOwnerFirst(String property, Object newValue) {
      this.setPropertyTryThese(property, newValue, this.owner, this.delegate);
   }

   private void setPropertyTryThese(String property, Object newValue, Object firstTry, Object secondTry) {
      try {
         InvokerHelper.setProperty(firstTry, property, newValue);
      } catch (GroovyRuntimeException var8) {
         if (firstTry != null && firstTry != this && firstTry != secondTry) {
            try {
               InvokerHelper.setProperty(secondTry, property, newValue);
               return;
            } catch (GroovyRuntimeException var7) {
            }
         }

         throw var8;
      }
   }

   public boolean isCase(Object candidate) {
      return DefaultTypeTransformation.castToBoolean(this.call(candidate));
   }

   public Object call() {
      Object[] NOARGS = EMPTY_OBJECT_ARRAY;
      return this.call(NOARGS);
   }

   public Object call(Object[] args) {
      try {
         return this.getMetaClass().invokeMethod(this, "doCall", args);
      } catch (Exception var3) {
         return throwRuntimeException(var3);
      }
   }

   public Object call(Object arguments) {
      return this.call(new Object[]{arguments});
   }

   protected static Object throwRuntimeException(Throwable throwable) {
      if (throwable instanceof RuntimeException) {
         throw (RuntimeException)throwable;
      } else {
         throw new GroovyRuntimeException(throwable.getMessage(), throwable);
      }
   }

   public Object getOwner() {
      return this.owner;
   }

   public Object getDelegate() {
      return this.delegate;
   }

   public void setDelegate(Object delegate) {
      this.delegate = delegate;
   }

   public Class[] getParameterTypes() {
      return this.parameterTypes;
   }

   public int getMaximumNumberOfParameters() {
      return this.maximumNumberOfParameters;
   }

   public Closure asWritable() {
      return new Closure.WritableClosure();
   }

   public void run() {
      this.call();
   }

   public Closure curry(Object[] arguments) {
      return new CurriedClosure(this, arguments);
   }

   public Closure rcurry(Object[] arguments) {
      return new CurriedClosure(-arguments.length, this, arguments);
   }

   public Closure ncurry(int n, Object[] arguments) {
      return new CurriedClosure(n, this, arguments);
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public int getDirective() {
      return this.directive;
   }

   public void setDirective(int directive) {
      this.directive = directive;
   }

   private class WritableClosure extends Closure implements Writable {
      public WritableClosure() {
         super(Closure.this);
      }

      public Writer writeTo(Writer out) throws IOException {
         Closure.this.call(new Object[]{out});
         return out;
      }

      public Object invokeMethod(String method, Object arguments) {
         if ("clone".equals(method)) {
            return this.clone();
         } else if ("curry".equals(method)) {
            return this.curry((Object[])((Object[])arguments));
         } else {
            return "asWritable".equals(method) ? this.asWritable() : Closure.this.invokeMethod(method, arguments);
         }
      }

      public Object getProperty(String property) {
         return Closure.this.getProperty(property);
      }

      public void setProperty(String property, Object newValue) {
         Closure.this.setProperty(property, newValue);
      }

      public Object call() {
         return ((Closure)this.getOwner()).call();
      }

      public Object call(Object arguments) {
         return ((Closure)this.getOwner()).call(arguments);
      }

      public Object call(Object[] args) {
         return ((Closure)this.getOwner()).call(args);
      }

      public Object doCall(Object[] args) {
         return this.call(args);
      }

      public Object getDelegate() {
         return Closure.this.getDelegate();
      }

      public void setDelegate(Object delegate) {
         Closure.this.setDelegate(delegate);
      }

      public Class[] getParameterTypes() {
         return Closure.this.getParameterTypes();
      }

      public int getMaximumNumberOfParameters() {
         return Closure.this.getMaximumNumberOfParameters();
      }

      public Closure asWritable() {
         return this;
      }

      public void run() {
         Closure.this.run();
      }

      public Object clone() {
         return ((Closure)Closure.this.clone()).asWritable();
      }

      public int hashCode() {
         return Closure.this.hashCode();
      }

      public boolean equals(Object arg0) {
         return Closure.this.equals(arg0);
      }

      public String toString() {
         StringWriter writer = new StringWriter();

         try {
            this.writeTo(writer);
         } catch (IOException var3) {
            return null;
         }

         return writer.toString();
      }

      public Closure curry(Object[] arguments) {
         return (new CurriedClosure(this, arguments)).asWritable();
      }

      public void setResolveStrategy(int resolveStrategy) {
         Closure.this.setResolveStrategy(resolveStrategy);
      }

      public int getResolveStrategy() {
         return Closure.this.getResolveStrategy();
      }
   }
}
