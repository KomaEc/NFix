package org.jboss.net.sockets;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.Random;

public class RMIMultiSocketClient implements InvocationHandler, Serializable {
   private static final long serialVersionUID = -945837789475428529L;
   protected Remote[] stubs;
   protected Random random;

   public RMIMultiSocketClient(Remote[] stubs) {
      this.stubs = stubs;
      this.random = new Random();
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (method.getName().equals("hashCode")) {
         return new Integer(this.stubs[0].hashCode());
      } else if (method.getName().equals("equals")) {
         return new Boolean(this.stubs[0].equals(args[0]));
      } else {
         int i = this.random.nextInt(this.stubs.length);
         long hash = MethodHash.calculateHash(method);
         RMIMultiSocket target = (RMIMultiSocket)this.stubs[i];
         return target.invoke(hash, args);
      }
   }
}
