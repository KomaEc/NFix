package org.codehaus.groovy.runtime.callsite;

import groovy.lang.MetaClass;

public abstract class MetaClassSite extends AbstractCallSite {
   protected final MetaClass metaClass;

   public MetaClassSite(CallSite site, MetaClass metaClass) {
      super(site);
      this.metaClass = metaClass;
   }
}
