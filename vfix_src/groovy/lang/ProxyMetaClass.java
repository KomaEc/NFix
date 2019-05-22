package groovy.lang;

import java.beans.IntrospectionException;

public class ProxyMetaClass extends MetaClassImpl implements AdaptingMetaClass {
   protected MetaClass adaptee = null;
   protected Interceptor interceptor = null;

   public static ProxyMetaClass getInstance(Class theClass) throws IntrospectionException {
      MetaClassRegistry metaRegistry = GroovySystem.getMetaClassRegistry();
      MetaClass meta = metaRegistry.getMetaClass(theClass);
      return new ProxyMetaClass(metaRegistry, theClass, meta);
   }

   public ProxyMetaClass(MetaClassRegistry registry, Class theClass, MetaClass adaptee) throws IntrospectionException {
      super(registry, theClass);
      this.adaptee = adaptee;
      if (null == adaptee) {
         throw new IllegalArgumentException("adaptee must not be null");
      } else {
         super.initialize();
      }
   }

   public synchronized void initialize() {
      this.adaptee.initialize();
   }

   public Object use(Closure closure) {
      MetaClass origMetaClass = this.registry.getMetaClass(this.theClass);
      this.registry.setMetaClass(this.theClass, this);

      Object var3;
      try {
         var3 = closure.call();
      } finally {
         this.registry.setMetaClass(this.theClass, origMetaClass);
      }

      return var3;
   }

   public Object use(GroovyObject object, Closure closure) {
      MetaClass origMetaClass = object.getMetaClass();
      object.setMetaClass(this);

      Object var4;
      try {
         var4 = closure.call();
      } finally {
         object.setMetaClass(origMetaClass);
      }

      return var4;
   }

   public Interceptor getInterceptor() {
      return this.interceptor;
   }

   public void setInterceptor(Interceptor interceptor) {
      this.interceptor = interceptor;
   }

   public Object invokeMethod(final Object object, final String methodName, final Object[] arguments) {
      return this.doCall(object, methodName, arguments, this.interceptor, new ProxyMetaClass.Callable() {
         public Object call() {
            return ProxyMetaClass.this.adaptee.invokeMethod(object, methodName, arguments);
         }
      });
   }

   public Object invokeStaticMethod(final Object object, final String methodName, final Object[] arguments) {
      return this.doCall(object, methodName, arguments, this.interceptor, new ProxyMetaClass.Callable() {
         public Object call() {
            return ProxyMetaClass.this.adaptee.invokeStaticMethod(object, methodName, arguments);
         }
      });
   }

   public Object invokeConstructor(final Object[] arguments) {
      return this.doCall(this.theClass, "ctor", arguments, this.interceptor, new ProxyMetaClass.Callable() {
         public Object call() {
            return ProxyMetaClass.this.adaptee.invokeConstructor(arguments);
         }
      });
   }

   public Object getProperty(Class aClass, Object object, String property, boolean b, boolean b1) {
      if (null == this.interceptor) {
         return super.getProperty(aClass, object, property, b, b1);
      } else if (this.interceptor instanceof PropertyAccessInterceptor) {
         PropertyAccessInterceptor pae = (PropertyAccessInterceptor)this.interceptor;
         Object result = pae.beforeGet(object, property);
         if (this.interceptor.doInvoke()) {
            result = super.getProperty(aClass, object, property, b, b1);
         }

         return result;
      } else {
         return super.getProperty(aClass, object, property, b, b1);
      }
   }

   public void setProperty(Class aClass, Object object, String property, Object newValue, boolean b, boolean b1) {
      if (null == this.interceptor) {
         super.setProperty(aClass, object, property, newValue, b, b1);
      }

      if (this.interceptor instanceof PropertyAccessInterceptor) {
         PropertyAccessInterceptor pae = (PropertyAccessInterceptor)this.interceptor;
         pae.beforeSet(object, property, newValue);
         if (this.interceptor.doInvoke()) {
            super.setProperty(aClass, object, property, newValue, b, b1);
         }
      } else {
         super.setProperty(aClass, object, property, newValue, b, b1);
      }

   }

   public MetaClass getAdaptee() {
      return this.adaptee;
   }

   public void setAdaptee(MetaClass metaClass) {
      this.adaptee = metaClass;
   }

   private Object doCall(Object object, String methodName, Object[] arguments, Interceptor interceptor, ProxyMetaClass.Callable howToInvoke) {
      if (null == interceptor) {
         return howToInvoke.call();
      } else {
         Object result = interceptor.beforeInvoke(object, methodName, arguments);
         if (interceptor.doInvoke()) {
            result = howToInvoke.call();
         }

         result = interceptor.afterInvoke(object, methodName, arguments, result);
         return result;
      }
   }

   private interface Callable {
      Object call();
   }
}
