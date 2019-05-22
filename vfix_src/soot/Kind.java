package soot;

import soot.util.Numberable;

public final class Kind implements Numberable {
   public static final Kind INVALID = new Kind("INVALID");
   public static final Kind STATIC = new Kind("STATIC");
   public static final Kind VIRTUAL = new Kind("VIRTUAL");
   public static final Kind INTERFACE = new Kind("INTERFACE");
   public static final Kind SPECIAL = new Kind("SPECIAL");
   public static final Kind CLINIT = new Kind("CLINIT");
   public static final Kind THREAD = new Kind("THREAD");
   public static final Kind EXECUTOR = new Kind("EXECUTOR");
   public static final Kind ASYNCTASK = new Kind("ASYNCTASK");
   public static final Kind FINALIZE = new Kind("FINALIZE");
   public static final Kind HANDLER = new Kind("HANDLER");
   public static final Kind INVOKE_FINALIZE = new Kind("INVOKE_FINALIZE");
   public static final Kind PRIVILEGED = new Kind("PRIVILEGED");
   public static final Kind NEWINSTANCE = new Kind("NEWINSTANCE");
   public static final Kind REFL_INVOKE = new Kind("REFL_METHOD_INVOKE");
   public static final Kind REFL_CONSTR_NEWINSTANCE = new Kind("REFL_CONSTRUCTOR_NEWINSTANCE");
   public static final Kind REFL_CLASS_NEWINSTANCE = new Kind("REFL_CLASS_NEWINSTANCE");
   private final String name;
   private int num;

   private Kind(String name) {
      this.name = name;
   }

   public String name() {
      return this.name;
   }

   public int getNumber() {
      return this.num;
   }

   public void setNumber(int num) {
      this.num = num;
   }

   public String toString() {
      return this.name();
   }

   public boolean passesParameters() {
      return this.isExplicit() || this == THREAD || this == EXECUTOR || this == ASYNCTASK || this == FINALIZE || this == PRIVILEGED || this == NEWINSTANCE || this == INVOKE_FINALIZE || this == REFL_INVOKE || this == REFL_CONSTR_NEWINSTANCE || this == REFL_CLASS_NEWINSTANCE;
   }

   public boolean isFake() {
      return this == THREAD || this == EXECUTOR || this == ASYNCTASK || this == PRIVILEGED || this == HANDLER;
   }

   public boolean isExplicit() {
      return this.isInstance() || this.isStatic();
   }

   public boolean isInstance() {
      return this == VIRTUAL || this == INTERFACE || this == SPECIAL;
   }

   public boolean isVirtual() {
      return this == VIRTUAL;
   }

   public boolean isSpecial() {
      return this == SPECIAL;
   }

   public boolean isClinit() {
      return this == CLINIT;
   }

   public boolean isStatic() {
      return this == STATIC;
   }

   public boolean isThread() {
      return this == THREAD;
   }

   public boolean isExecutor() {
      return this == EXECUTOR;
   }

   public boolean isAsyncTask() {
      return this == ASYNCTASK;
   }

   public boolean isPrivileged() {
      return this == PRIVILEGED;
   }

   public boolean isReflection() {
      return this == REFL_CLASS_NEWINSTANCE || this == REFL_CONSTR_NEWINSTANCE || this == REFL_INVOKE;
   }

   public boolean isReflInvoke() {
      return this == REFL_INVOKE;
   }
}
