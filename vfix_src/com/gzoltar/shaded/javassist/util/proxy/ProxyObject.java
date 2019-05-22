package com.gzoltar.shaded.javassist.util.proxy;

public interface ProxyObject extends Proxy {
   void setHandler(MethodHandler mi);

   MethodHandler getHandler();
}
