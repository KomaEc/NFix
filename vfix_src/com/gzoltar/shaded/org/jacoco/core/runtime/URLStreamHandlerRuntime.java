package com.gzoltar.shaded.org.jacoco.core.runtime;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;

public class URLStreamHandlerRuntime extends AbstractRuntime {
   private static final String PROTOCOLPREFIX = "jacoco-";
   private final String protocol = "jacoco-" + Integer.toHexString(this.hashCode());
   private Map<String, URLStreamHandler> handlers;
   private final URLStreamHandler handler = new URLStreamHandler() {
      protected URLConnection openConnection(URL u) throws IOException {
         return URLStreamHandlerRuntime.this.connection;
      }
   };
   private final URLConnection connection = new URLConnection((URL)null) {
      public void connect() throws IOException {
         throw new AssertionError();
      }

      public boolean equals(Object obj) {
         return URLStreamHandlerRuntime.this.data.equals(obj);
      }
   };

   public void startup(RuntimeData data) throws Exception {
      super.startup(data);
      this.handlers = this.getHandlersReference();
      this.handlers.put(this.protocol, this.handler);
   }

   private Map<String, URLStreamHandler> getHandlersReference() throws Exception {
      Field field = URL.class.getDeclaredField("handlers");
      field.setAccessible(true);
      Map<String, URLStreamHandler> map = (Map)field.get((Object)null);
      return map;
   }

   public void shutdown() {
      this.handlers.remove(this.protocol);
   }

   public int generateDataAccessor(long classid, String classname, int probecount, MethodVisitor mv) {
      RuntimeData.generateArgumentArray(classid, classname, probecount, mv);
      mv.visitInsn(89);
      mv.visitTypeInsn(187, "java/net/URL");
      mv.visitInsn(89);
      mv.visitLdcInsn(this.protocol);
      mv.visitInsn(1);
      mv.visitLdcInsn("");
      mv.visitMethodInsn(183, "java/net/URL", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
      mv.visitMethodInsn(182, "java/net/URL", "openConnection", "()Ljava/net/URLConnection;", false);
      mv.visitInsn(95);
      mv.visitMethodInsn(182, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false);
      mv.visitInsn(87);
      mv.visitInsn(3);
      mv.visitInsn(50);
      mv.visitTypeInsn(192, "[Z");
      return 7;
   }
}
