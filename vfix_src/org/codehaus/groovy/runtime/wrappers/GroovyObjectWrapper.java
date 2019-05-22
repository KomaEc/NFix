package org.codehaus.groovy.runtime.wrappers;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;

public class GroovyObjectWrapper extends Wrapper {
   protected final GroovyObject wrapped;

   public GroovyObjectWrapper(GroovyObject wrapped, Class constrainedType) {
      super(constrainedType);
      this.wrapped = wrapped;
   }

   public Object unwrap() {
      return this.wrapped;
   }

   public Object getProperty(String property) {
      return this.wrapped.getProperty(property);
   }

   public Object invokeMethod(String name, Object args) {
      return this.wrapped.invokeMethod(name, args);
   }

   public void setMetaClass(MetaClass metaClass) {
      this.wrapped.setMetaClass(metaClass);
   }

   public void setProperty(String property, Object newValue) {
      this.wrapped.setProperty(property, newValue);
   }

   protected Object getWrapped() {
      return this.wrapped;
   }

   protected MetaClass getDelegatedMetaClass() {
      return this.wrapped.getMetaClass();
   }
}
