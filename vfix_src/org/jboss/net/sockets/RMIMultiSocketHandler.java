package org.jboss.net.sockets;

import java.lang.reflect.Method;
import java.util.Map;

public class RMIMultiSocketHandler implements RMIMultiSocket {
   Object target;
   Map invokerMap;

   public RMIMultiSocketHandler(Object target, Map invokerMap) {
      this.target = target;
      this.invokerMap = invokerMap;
   }

   public Object invoke(long methodHash, Object[] args) throws Exception {
      Method method = (Method)this.invokerMap.get(new Long(methodHash));
      return method.invoke(this.target, args);
   }
}
