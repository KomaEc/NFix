package org.codehaus.groovy.tools.shell;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class CommandException extends Exception implements GroovyObject {
   private final Command command;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205473L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205473 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandException;
   // $FF: synthetic field
   private static Class $class$java$lang$Exception;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Command;

   public CommandException(Command command, String msg) {
      CallSite[] var3 = $getCallSiteArray();
      Object[] var10000 = new Object[]{msg};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 4, $get$$class$java$lang$Exception());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super();
         break;
      case 1:
         super((String)var10001[0]);
         break;
      case 2:
         super((String)var10001[0], (Throwable)var10001[1]);
         break;
      case 3:
         super((Throwable)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ValueRecorder var4 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var4.record(command, 8))) {
            var4.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert command", var4), (Object)null);
         }
      } catch (Throwable var6) {
         var4.clear();
         throw var6;
      }

      this.command = (Command)ScriptBytecodeAdapter.castToType(command, $get$$class$org$codehaus$groovy$tools$shell$Command());
   }

   public CommandException(Command command, String msg, Throwable cause) {
      CallSite[] var4 = $getCallSiteArray();
      Object[] var10000 = new Object[]{msg, cause};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 4, $get$$class$java$lang$Exception());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super();
         break;
      case 1:
         super((String)var10001[0]);
         break;
      case 2:
         super((String)var10001[0], (Throwable)var10001[1]);
         break;
      case 3:
         super((Throwable)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ValueRecorder var5 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var5.record(command, 8))) {
            var5.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert command", var5), (Object)null);
         }
      } catch (Throwable var7) {
         var5.clear();
         throw var7;
      }

      this.command = (Command)ScriptBytecodeAdapter.castToType(command, $get$$class$org$codehaus$groovy$tools$shell$Command());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$CommandException()) {
         return ScriptBytecodeAdapter.initMetaClass(this);
      } else {
         ClassInfo var1 = $staticClassInfo;
         if (var1 == null) {
            $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
         }

         return var1.getMetaClass();
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$4(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$CommandException();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$CommandException(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$CommandException(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   // $FF: synthetic method
   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   // $FF: synthetic method
   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   // $FF: synthetic method
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public final Command getCommand() {
      return this.command;
   }

   // $FF: synthetic method
   public Throwable[] super$2$getSuppressedExceptions() {
      return super.getSuppressedExceptions();
   }

   // $FF: synthetic method
   public void super$2$printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
   }

   // $FF: synthetic method
   public Throwable super$2$fillInStackTrace() {
      return super.fillInStackTrace();
   }

   // $FF: synthetic method
   public String super$2$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public String super$2$getMessage() {
      return super.getMessage();
   }

   // $FF: synthetic method
   public String super$2$getLocalizedMessage() {
      return super.getLocalizedMessage();
   }

   // $FF: synthetic method
   public Throwable super$2$initCause(Throwable var1) {
      return super.initCause(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$2$addSuppressedException(Throwable var1) {
      super.addSuppressedException(var1);
   }

   // $FF: synthetic method
   public void super$2$printStackTrace(PrintStream var1) {
      super.printStackTrace(var1);
   }

   // $FF: synthetic method
   public void super$2$setStackTrace(StackTraceElement[] var1) {
      super.setStackTrace(var1);
   }

   // $FF: synthetic method
   public Throwable super$2$getCause() {
      return super.getCause();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$2$printStackTrace() {
      super.printStackTrace();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public StackTraceElement[] super$2$getStackTrace() {
      return super.getStackTrace();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[0];
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$CommandException(), var0);
   }

   // $FF: synthetic method
   private static CallSite[] $getCallSiteArray() {
      CallSiteArray var0;
      if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
         var0 = $createCallSiteArray();
         $callSiteArray = new SoftReference(var0);
      }

      return var0.array;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandException() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandException;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$CommandException = class$("org.codehaus.groovy.tools.shell.CommandException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Exception() {
      Class var10000 = $class$java$lang$Exception;
      if (var10000 == null) {
         var10000 = $class$java$lang$Exception = class$("java.lang.Exception");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Command() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Command;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Command = class$("org.codehaus.groovy.tools.shell.Command");
      }

      return var10000;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
