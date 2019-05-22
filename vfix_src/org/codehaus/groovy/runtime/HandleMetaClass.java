package org.codehaus.groovy.runtime;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyObject;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;
import java.lang.reflect.Method;
import java.util.Iterator;

public class HandleMetaClass extends DelegatingMetaClass {
   private Object object;
   private static MetaClass myMetaClass;
   private static final Object NONE = new Object();

   public HandleMetaClass(MetaClass mc) {
      this(mc, (Object)null);
   }

   public HandleMetaClass(MetaClass mc, Object obj) {
      super(mc);
      if (obj != null) {
         if (InvokerHelper.getMetaClass(obj.getClass()) != mc && mc instanceof ExpandoMetaClass) {
            this.object = NONE;
         } else {
            this.object = obj;
         }
      }

      if (myMetaClass == null) {
         myMetaClass = InvokerHelper.getMetaClass(this.getClass());
      }

   }

   public void initialize() {
      this.replaceDelegate();
      this.delegate.initialize();
   }

   public GroovyObject replaceDelegate() {
      if (this.object == null) {
         if (!(this.delegate instanceof ExpandoMetaClass)) {
            this.delegate = new ExpandoMetaClass(this.delegate.getTheClass(), true, true);
            this.delegate.initialize();
         }

         DefaultGroovyMethods.setMetaClass(this.delegate.getTheClass(), this.delegate);
      } else if (this.object != NONE) {
         MetaClass metaClass = this.delegate;
         this.delegate = new ExpandoMetaClass(this.delegate.getTheClass(), false, true);
         if (metaClass instanceof ExpandoMetaClass) {
            ExpandoMetaClass emc = (ExpandoMetaClass)metaClass;
            Iterator i$ = emc.getExpandoMethods().iterator();

            while(i$.hasNext()) {
               MetaMethod method = (MetaMethod)i$.next();
               ((ExpandoMetaClass)this.delegate).registerInstanceMethod(method);
            }
         }

         this.delegate.initialize();
         DefaultGroovyMethods.setMetaClass(this.object, this.delegate);
         this.object = NONE;
      }

      return (GroovyObject)this.delegate;
   }

   public Object invokeMethod(String name, Object args) {
      return this.replaceDelegate().invokeMethod(name, args);
   }

   public Object getProperty(String property) {
      return !ExpandoMetaClass.isValidExpandoProperty(property) || !property.equals("static") && !property.equals("constructor") && myMetaClass.hasProperty(this, property) != null ? myMetaClass.getProperty(this, property) : this.replaceDelegate().getProperty(property);
   }

   public void setProperty(String property, Object newValue) {
      this.replaceDelegate().setProperty(property, newValue);
   }

   public void addNewInstanceMethod(Method method) {
      throw new UnsupportedOperationException();
   }

   public void addNewStaticMethod(Method method) {
      throw new UnsupportedOperationException();
   }

   public void addMetaMethod(MetaMethod metaMethod) {
      throw new UnsupportedOperationException();
   }

   public void addMetaBeanProperty(MetaBeanProperty metaBeanProperty) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object obj) {
      return super.equals(obj) || this.getAdaptee().equals(obj) || obj instanceof HandleMetaClass && this.equals(((HandleMetaClass)obj).getAdaptee());
   }
}
