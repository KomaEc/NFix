package org.codehaus.groovy.runtime.wrappers;

import groovy.lang.MetaClass;

public class PojoWrapper extends Wrapper {
   protected MetaClass delegate;
   protected final Object wrapped;

   public PojoWrapper(Object wrapped, Class constrainedType) {
      super(constrainedType);
      this.wrapped = wrapped;
   }

   public Object unwrap() {
      return this.wrapped;
   }

   public Object getProperty(String property) {
      return this.delegate.getProperty(this.wrapped, property);
   }

   public Object invokeMethod(String methodName, Object arguments) {
      return this.delegate.invokeMethod(this.wrapped, methodName, arguments);
   }

   public void setMetaClass(MetaClass metaClass) {
      this.delegate = metaClass;
   }

   public void setProperty(String property, Object newValue) {
      this.delegate.setProperty(this.wrapped, property, newValue);
   }

   protected Object getWrapped() {
      return this.wrapped;
   }

   protected MetaClass getDelegatedMetaClass() {
      return this.delegate;
   }
}
