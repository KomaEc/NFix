package org.codehaus.groovy.runtime.wrappers;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;

public abstract class Wrapper implements GroovyObject {
   /** @deprecated */
   @Deprecated
   protected MetaClass delegatingMetaClass;
   protected final Class constrainedType;

   public Wrapper(Class constrainedType) {
      this.constrainedType = constrainedType;
   }

   public MetaClass getMetaClass() {
      return this.getDelegatedMetaClass();
   }

   public Class getType() {
      return this.constrainedType;
   }

   public abstract Object unwrap();

   protected abstract Object getWrapped();

   protected abstract MetaClass getDelegatedMetaClass();
}
