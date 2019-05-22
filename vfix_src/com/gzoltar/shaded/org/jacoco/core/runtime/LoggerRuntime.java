package com.gzoltar.shaded.org.jacoco.core.runtime;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerRuntime extends AbstractRuntime {
   private static final String CHANNEL = "jacoco-runtime";
   private final String key = Integer.toHexString(this.hashCode());
   private final Logger logger = this.configureLogger();
   private final Handler handler = new LoggerRuntime.RuntimeHandler();

   private Logger configureLogger() {
      Logger l = Logger.getLogger("jacoco-runtime");
      l.setUseParentHandlers(false);
      l.setLevel(Level.ALL);
      return l;
   }

   public int generateDataAccessor(long classid, String classname, int probecount, MethodVisitor mv) {
      RuntimeData.generateArgumentArray(classid, classname, probecount, mv);
      mv.visitInsn(89);
      mv.visitLdcInsn("jacoco-runtime");
      mv.visitMethodInsn(184, "java/util/logging/Logger", "getLogger", "(Ljava/lang/String;)Ljava/util/logging/Logger;", false);
      mv.visitInsn(95);
      mv.visitFieldInsn(178, "java/util/logging/Level", "INFO", "Ljava/util/logging/Level;");
      mv.visitInsn(95);
      mv.visitLdcInsn(this.key);
      mv.visitInsn(95);
      mv.visitMethodInsn(182, "java/util/logging/Logger", "log", "(Ljava/util/logging/Level;Ljava/lang/String;[Ljava/lang/Object;)V", false);
      mv.visitInsn(3);
      mv.visitInsn(50);
      mv.visitTypeInsn(192, "[Z");
      return 5;
   }

   public void startup(RuntimeData data) throws Exception {
      super.startup(data);
      this.logger.addHandler(this.handler);
   }

   public void shutdown() {
      this.logger.removeHandler(this.handler);
   }

   private class RuntimeHandler extends Handler {
      private RuntimeHandler() {
      }

      public void publish(LogRecord record) {
         if (LoggerRuntime.this.key.equals(record.getMessage())) {
            LoggerRuntime.this.data.getProbes(record.getParameters());
         }

      }

      public void flush() {
      }

      public void close() throws SecurityException {
         LoggerRuntime.this.logger.addHandler(LoggerRuntime.this.handler);
      }

      // $FF: synthetic method
      RuntimeHandler(Object x1) {
         this();
      }
   }
}
