package groovy.util;

import com.gzoltar.shaded.org.apache.commons.cli.CommandLine;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class OptionAccessor implements GroovyObject {
   private CommandLine inner;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)-2;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203579L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203579 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$CommandLine;
   // $FF: synthetic field
   private static Class $class$groovy$util$OptionAccessor;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Character;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public OptionAccessor(CommandLine inner) {
      CallSite[] var2 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.inner = (CommandLine)ScriptBytecodeAdapter.castToType(inner, $get$$class$org$apache$commons$cli$CommandLine());
   }

   public Object invokeMethod(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      return var3[0].call(var3[1].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), (Object)this.inner), this.inner, name, args);
   }

   public Object getProperty(String name) {
      CallSite[] var2 = $getCallSiteArray();
      Object methodname = "getOptionValue";
      Object result;
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareGreaterThan(var2[2].call(name), $const$0) && DefaultTypeTransformation.booleanUnbox(var2[3].call(name, (Object)"s")) ? Boolean.TRUE : Boolean.FALSE)) {
         result = var2[4].call(name, (Object)ScriptBytecodeAdapter.createRange($const$1, $const$2, true));
         if (DefaultTypeTransformation.booleanUnbox(var2[5].callCurrent(this, (Object)result))) {
            name = (String)ScriptBytecodeAdapter.castToType(result, $get$$class$java$lang$String());
            methodname = var2[6].call(methodname, (Object)"s");
         }
      }

      if (ScriptBytecodeAdapter.compareEqual(var2[7].call(name), $const$0)) {
         name = (String)ScriptBytecodeAdapter.castToType((Character)ScriptBytecodeAdapter.asType(name, $get$$class$java$lang$Character()), $get$$class$java$lang$String());
      }

      result = var2[8].call(var2[9].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), (Object)this.inner), this.inner, methodname, name);
      if (ScriptBytecodeAdapter.compareEqual((Object)null, result)) {
         result = var2[10].call(this.inner, (Object)name);
      }

      if (result instanceof String[]) {
         result = var2[11].call(result);
      }

      return result;
   }

   public List<String> arguments() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var1[12].call(var1[13].callGetProperty(this.inner)), $get$$class$java$util$List());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$util$OptionAccessor()) {
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
   public Object this$dist$invoke$2(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$util$OptionAccessor();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$util$OptionAccessor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$util$OptionAccessor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public CommandLine getInner() {
      return this.inner;
   }

   public void setInner(CommandLine var1) {
      this.inner = var1;
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
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
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "invokeMethod";
      var0[1] = "getMetaClass";
      var0[2] = "size";
      var0[3] = "endsWith";
      var0[4] = "getAt";
      var0[5] = "hasOption";
      var0[6] = "plus";
      var0[7] = "size";
      var0[8] = "invokeMethod";
      var0[9] = "getMetaClass";
      var0[10] = "hasOption";
      var0[11] = "toList";
      var0[12] = "toList";
      var0[13] = "args";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[14];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$util$OptionAccessor(), var0);
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
   private static Class $get$$class$org$apache$commons$cli$CommandLine() {
      Class var10000 = $class$org$apache$commons$cli$CommandLine;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$CommandLine = class$("com.gzoltar.shaded.org.apache.commons.cli.CommandLine");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$OptionAccessor() {
      Class var10000 = $class$groovy$util$OptionAccessor;
      if (var10000 == null) {
         var10000 = $class$groovy$util$OptionAccessor = class$("groovy.util.OptionAccessor");
      }

      return var10000;
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
   private static Class $get$$class$java$lang$Character() {
      Class var10000 = $class$java$lang$Character;
      if (var10000 == null) {
         var10000 = $class$java$lang$Character = class$("java.lang.Character");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
      Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$List() {
      Class var10000 = $class$java$util$List;
      if (var10000 == null) {
         var10000 = $class$java$util$List = class$("java.util.List");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
