package com.gzoltar.shaded.org.jacoco.core.internal.flow;

import com.gzoltar.shaded.org.objectweb.asm.Label;

public final class LabelInfo {
   public static final int NO_PROBE = -1;
   private boolean target = false;
   private boolean multiTarget = false;
   private boolean successor = false;
   private boolean methodInvocationLine = false;
   private boolean done = false;
   private int probeid = -1;
   private Label intermediate = null;
   private Instruction instruction = null;

   private LabelInfo() {
   }

   public static void setTarget(Label label) {
      LabelInfo info = create(label);
      if (!info.target && !info.successor) {
         info.target = true;
      } else {
         info.multiTarget = true;
      }

   }

   public static void setSuccessor(Label label) {
      LabelInfo info = create(label);
      info.successor = true;
      if (info.target) {
         info.multiTarget = true;
      }

   }

   public static boolean isMultiTarget(Label label) {
      LabelInfo info = get(label);
      return info == null ? false : info.multiTarget;
   }

   public static boolean isSuccessor(Label label) {
      LabelInfo info = get(label);
      return info == null ? false : info.successor;
   }

   public static void setMethodInvocationLine(Label label) {
      create(label).methodInvocationLine = true;
   }

   public static boolean isMethodInvocationLine(Label label) {
      LabelInfo info = get(label);
      return info == null ? false : info.methodInvocationLine;
   }

   public static boolean needsProbe(Label label) {
      LabelInfo info = get(label);
      return info != null && info.successor && (info.multiTarget || info.methodInvocationLine);
   }

   public static void setDone(Label label) {
      create(label).done = true;
   }

   public static void resetDone(Label label) {
      LabelInfo info = get(label);
      if (info != null) {
         info.done = false;
      }

   }

   public static void resetDone(Label[] labels) {
      Label[] arr$ = labels;
      int len$ = labels.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Label label = arr$[i$];
         resetDone(label);
      }

   }

   public static boolean isDone(Label label) {
      LabelInfo info = get(label);
      return info == null ? false : info.done;
   }

   public static void setProbeId(Label label, int id) {
      create(label).probeid = id;
   }

   public static int getProbeId(Label label) {
      LabelInfo info = get(label);
      return info == null ? -1 : info.probeid;
   }

   public static void setIntermediateLabel(Label label, Label intermediate) {
      create(label).intermediate = intermediate;
   }

   public static Label getIntermediateLabel(Label label) {
      LabelInfo info = get(label);
      return info == null ? null : info.intermediate;
   }

   public static void setInstruction(Label label, Instruction instruction) {
      create(label).instruction = instruction;
   }

   public static Instruction getInstruction(Label label) {
      LabelInfo info = get(label);
      return info == null ? null : info.instruction;
   }

   private static LabelInfo get(Label label) {
      Object info = label.info;
      return info instanceof LabelInfo ? (LabelInfo)info : null;
   }

   private static LabelInfo create(Label label) {
      LabelInfo info = get(label);
      if (info == null) {
         info = new LabelInfo();
         label.info = info;
      }

      return info;
   }
}
