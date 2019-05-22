package com.gzoltar.shaded.javassist.tools;

import com.gzoltar.shaded.javassist.CannotCompileException;
import com.gzoltar.shaded.javassist.CtBehavior;
import java.util.HashMap;
import java.util.UUID;

public abstract class Callback {
   public static HashMap<String, Callback> callbacks = new HashMap();
   private final String sourceCode;

   public Callback(String src) {
      String uuid = UUID.randomUUID().toString();
      callbacks.put(uuid, this);
      this.sourceCode = "((javassist.tools.Callback) javassist.tools.Callback.callbacks.get(\"" + uuid + "\")).result(new Object[]{" + src + "});";
   }

   public abstract void result(Object... objects);

   public String toString() {
      return this.sourceCode();
   }

   public String sourceCode() {
      return this.sourceCode;
   }

   public static void insertBefore(CtBehavior behavior, Callback callback) throws CannotCompileException {
      behavior.insertBefore(callback.toString());
   }

   public static void insertAfter(CtBehavior behavior, Callback callback) throws CannotCompileException {
      behavior.insertAfter(callback.toString(), false);
   }

   public static void insertAfter(CtBehavior behavior, Callback callback, boolean asFinally) throws CannotCompileException {
      behavior.insertAfter(callback.toString(), asFinally);
   }

   public static int insertAt(CtBehavior behavior, Callback callback, int lineNum) throws CannotCompileException {
      return behavior.insertAt(lineNum, callback.toString());
   }
}
