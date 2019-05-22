package com.gzoltar.shaded.org.jacoco.core.runtime;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.ExecutionDataStore;
import com.gzoltar.shaded.org.jacoco.core.data.IExecutionDataVisitor;
import com.gzoltar.shaded.org.jacoco.core.data.ISessionInfoVisitor;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfo;
import com.gzoltar.shaded.org.jacoco.core.internal.instr.InstrSupport;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;

public class RuntimeData {
   protected final ExecutionDataStore store = new ExecutionDataStore();
   private long startTimeStamp = System.currentTimeMillis();
   private String sessionId = "<none>";

   public void setSessionId(String id) {
      this.sessionId = id;
   }

   public String getSessionId() {
      return this.sessionId;
   }

   public final void collect(IExecutionDataVisitor executionDataVisitor, ISessionInfoVisitor sessionInfoVisitor, boolean reset) {
      synchronized(this.store) {
         SessionInfo info = new SessionInfo(this.sessionId, this.startTimeStamp, System.currentTimeMillis());
         sessionInfoVisitor.visitSessionInfo(info);
         this.store.accept(executionDataVisitor);
         if (reset) {
            this.reset();
         }

      }
   }

   public final void reset() {
      synchronized(this.store) {
         this.store.reset();
         this.startTimeStamp = System.currentTimeMillis();
      }
   }

   public ExecutionData getExecutionData(Long id, String name, int probecount) {
      synchronized(this.store) {
         return this.store.get(id, name, probecount);
      }
   }

   public void getProbes(Object[] args) {
      Long classid = (Long)args[0];
      String name = (String)args[1];
      int probecount = (Integer)args[2];
      args[0] = this.getExecutionData(classid, name, probecount).getProbes();
   }

   public boolean equals(Object args) {
      if (args instanceof Object[]) {
         this.getProbes((Object[])((Object[])args));
      }

      return super.equals(args);
   }

   public static void generateArgumentArray(long classid, String classname, int probecount, MethodVisitor mv) {
      mv.visitInsn(6);
      mv.visitTypeInsn(189, "java/lang/Object");
      mv.visitInsn(89);
      mv.visitInsn(3);
      mv.visitLdcInsn(classid);
      mv.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
      mv.visitInsn(83);
      mv.visitInsn(89);
      mv.visitInsn(4);
      mv.visitLdcInsn(classname);
      mv.visitInsn(83);
      mv.visitInsn(89);
      mv.visitInsn(5);
      InstrSupport.push(mv, probecount);
      mv.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
      mv.visitInsn(83);
   }

   public static void generateAccessCall(long classid, String classname, int probecount, MethodVisitor mv) {
      generateArgumentArray(classid, classname, probecount, mv);
      mv.visitInsn(90);
      mv.visitMethodInsn(182, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false);
      mv.visitInsn(87);
      mv.visitInsn(3);
      mv.visitInsn(50);
      mv.visitTypeInsn(192, "[Z");
   }
}
