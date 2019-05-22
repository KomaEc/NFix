package org.jboss.net.protocol;

import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.jboss.logging.Logger;

public class URLStreamHandlerFactory implements java.net.URLStreamHandlerFactory {
   private static final Logger log = Logger.getLogger(URLStreamHandlerFactory.class);
   public static final String PACKAGE_PREFIX = "org.jboss.net.protocol";
   private static Map handlerMap = Collections.synchronizedMap(new HashMap());
   private static ThreadLocal createURLStreamHandlerProtocol = new ThreadLocal();
   private String[] handlerPkgs = new String[]{"org.jboss.net.protocol"};
   private String lastHandlerPkgs = "org.jboss.net.protocol";
   public static final String[] PROTOCOLS = new String[]{"file", "resource"};

   public static void preload() {
      for(int i = 0; i < PROTOCOLS.length; ++i) {
         try {
            new URL(PROTOCOLS[i], "", -1, "");
            log.trace("Loaded protocol: " + PROTOCOLS[i]);
         } catch (Exception var2) {
            log.warn("Failed to load protocol: " + PROTOCOLS[i], var2);
         }
      }

   }

   public static void clear() {
      handlerMap.clear();
   }

   public URLStreamHandler createURLStreamHandler(String protocol) {
      URLStreamHandler handler = (URLStreamHandler)handlerMap.get(protocol);
      if (handler != null) {
         return handler;
      } else {
         String prevProtocol = (String)createURLStreamHandlerProtocol.get();
         if (prevProtocol != null && prevProtocol.equals(protocol)) {
            return null;
         } else {
            createURLStreamHandlerProtocol.set(protocol);
            this.checkHandlerPkgs();
            ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();

            for(int p = 0; p < this.handlerPkgs.length; ++p) {
               try {
                  String classname = this.handlerPkgs[p] + "." + protocol + ".Handler";
                  Class type = null;

                  try {
                     type = ctxLoader.loadClass(classname);
                  } catch (ClassNotFoundException var9) {
                     type = Class.forName(classname);
                  }

                  if (type != null) {
                     handler = (URLStreamHandler)type.newInstance();
                     handlerMap.put(protocol, handler);
                     log.trace("Found protocol:" + protocol + " handler:" + handler);
                  }
               } catch (Throwable var10) {
               }
            }

            createURLStreamHandlerProtocol.set((Object)null);
            return handler;
         }
      }
   }

   private synchronized void checkHandlerPkgs() {
      String handlerPkgsProp = System.getProperty("java.protocol.handler.pkgs");
      if (handlerPkgsProp != null && !handlerPkgsProp.equals(this.lastHandlerPkgs)) {
         StringTokenizer tokeninzer = new StringTokenizer(handlerPkgsProp, "|");
         ArrayList tmp = new ArrayList();

         while(tokeninzer.hasMoreTokens()) {
            String pkg = tokeninzer.nextToken().intern();
            if (!tmp.contains(pkg)) {
               tmp.add(pkg);
            }
         }

         if (!tmp.contains("org.jboss.net.protocol")) {
            tmp.add("org.jboss.net.protocol");
         }

         this.handlerPkgs = new String[tmp.size()];
         tmp.toArray(this.handlerPkgs);
         this.lastHandlerPkgs = handlerPkgsProp;
      }

   }
}
