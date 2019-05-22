package org.apache.velocity.util.introspection;

import java.util.Iterator;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.RuntimeServicesAware;

public class SecureUberspector extends UberspectImpl implements RuntimeServicesAware {
   RuntimeServices runtimeServices;

   public void init() {
      String[] badPackages = this.runtimeServices.getConfiguration().getStringArray("introspector.restrict.packages");
      String[] badClasses = this.runtimeServices.getConfiguration().getStringArray("introspector.restrict.classes");
      this.introspector = new SecureIntrospectorImpl(badClasses, badPackages, this.log);
   }

   public Iterator getIterator(Object obj, Info i) throws Exception {
      if (obj != null && !((SecureIntrospectorControl)this.introspector).checkObjectExecutePermission(obj.getClass(), (String)null)) {
         this.log.warn("Cannot retrieve iterator from object of class " + obj.getClass().getName() + " due to security restrictions.");
         return null;
      } else {
         return super.getIterator(obj, i);
      }
   }

   public void setRuntimeServices(RuntimeServices rs) {
      this.runtimeServices = rs;
   }
}
