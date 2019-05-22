package org.codehaus.groovy.runtime.callsite;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;

public abstract class CallSiteAwareMetaMethod extends MetaMethod {
   public abstract CallSite createPojoCallSite(CallSite var1, MetaClassImpl var2, MetaMethod var3, Class[] var4, Object var5, Object[] var6);
}
