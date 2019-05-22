package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import java.util.ArrayList;
import java.util.List;

public class IteratorClosureAdapter<T> extends Closure {
   private final List<T> list = new ArrayList();
   private MetaClass metaClass = InvokerHelper.getMetaClass(this.getClass());

   public IteratorClosureAdapter(Object delegate) {
      super(delegate);
   }

   public MetaClass getMetaClass() {
      return this.metaClass;
   }

   public void setMetaClass(MetaClass metaClass) {
      this.metaClass = metaClass;
   }

   public List<T> asList() {
      return this.list;
   }

   protected Object doCall(T argument) {
      this.list.add(argument);
      return null;
   }
}
