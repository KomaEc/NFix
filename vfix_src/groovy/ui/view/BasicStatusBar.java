package groovy.ui.view;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class BasicStatusBar extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205371L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205371 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$awt$BorderLayout;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$BasicStatusBar;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public BasicStatusBar() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public BasicStatusBar(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$view$BasicStatusBar(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      Object var10000 = var1[1].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"constraints", var1[2].callGetProperty($get$$class$java$awt$BorderLayout())}), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final BigDecimal $const$0 = (BigDecimal)(new BigDecimal("1.0"));
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)3;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$awt$GridBagConstraints;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$javax$swing$SwingConstants;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicStatusBar$_run_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].callCurrent(this);
            var2[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"gridwidth", var2[2].callGetProperty($get$$class$java$awt$GridBagConstraints()), "fill", var2[3].callGetProperty($get$$class$java$awt$GridBagConstraints())}));
            ScriptBytecodeAdapter.setGroovyObjectProperty(var2[4].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"weightx", $const$0, "anchor", var2[5].callGetProperty($get$$class$java$awt$GridBagConstraints()), "fill", var2[6].callGetProperty($get$$class$java$awt$GridBagConstraints()), "insets", ScriptBytecodeAdapter.createList(new Object[]{$const$1, $const$2, $const$1, $const$2})}), "Welcome to Groovy."), $get$$class$groovy$ui$view$BasicStatusBar$_run_closure1(), this, "status");
            var2[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"orientation", var2[8].callGetProperty($get$$class$javax$swing$SwingConstants()), "fill", var2[9].callGetProperty($get$$class$java$awt$GridBagConstraints())}));
            Object var10000 = var2[10].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"insets", ScriptBytecodeAdapter.createList(new Object[]{$const$1, $const$2, $const$1, $const$2})}), "1:1");
            ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$view$BasicStatusBar$_run_closure1(), this, "rowNumAndColNum");
            return var10000;
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[11].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicStatusBar$_run_closure1()) {
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
         private static void $createCallSiteArray_1(String[] var0) {
            var0[0] = "gridBagLayout";
            var0[1] = "separator";
            var0[2] = "REMAINDER";
            var0[3] = "HORIZONTAL";
            var0[4] = "label";
            var0[5] = "WEST";
            var0[6] = "HORIZONTAL";
            var0[7] = "separator";
            var0[8] = "VERTICAL";
            var0[9] = "VERTICAL";
            var0[10] = "label";
            var0[11] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[12];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicStatusBar$_run_closure1(), var0);
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
         private static Class $get$$class$java$awt$GridBagConstraints() {
            Class var10000 = $class$java$awt$GridBagConstraints;
            if (var10000 == null) {
               var10000 = $class$java$awt$GridBagConstraints = class$("java.awt.GridBagConstraints");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$javax$swing$SwingConstants() {
            Class var10000 = $class$javax$swing$SwingConstants;
            if (var10000 == null) {
               var10000 = $class$javax$swing$SwingConstants = class$("javax.swing.SwingConstants");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$view$BasicStatusBar$_run_closure1() {
            Class var10000 = $class$groovy$ui$view$BasicStatusBar$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicStatusBar$_run_closure1 = class$("groovy.ui.view.BasicStatusBar$_run_closure1");
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
      });
      ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$view$BasicStatusBar(), this, "statusPanel");
      return var10000;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$view$BasicStatusBar()) {
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
      Class var10000 = $get$$class$groovy$ui$view$BasicStatusBar();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$view$BasicStatusBar(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$view$BasicStatusBar(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$3$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$3$println() {
      super.println();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$3$print(Object var1) {
      super.print(var1);
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object[] var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public Object super$3$evaluate(String var1) {
      return super.evaluate(var1);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public MetaClass super$2$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Binding super$3$getBinding() {
      return super.getBinding();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$setBinding(Binding var1) {
      super.setBinding(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$3$run(File var1, String[] var2) {
      super.run(var1, var2);
   }

   // $FF: synthetic method
   public Object super$3$evaluate(File var1) {
      return super.evaluate(var1);
   }

   // $FF: synthetic method
   public void super$3$println(Object var1) {
      super.println(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "runScript";
      var0[1] = "panel";
      var0[2] = "SOUTH";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[3];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$view$BasicStatusBar(), var0);
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
   private static Class $get$$class$java$awt$BorderLayout() {
      Class var10000 = $class$java$awt$BorderLayout;
      if (var10000 == null) {
         var10000 = $class$java$awt$BorderLayout = class$("java.awt.BorderLayout");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$Script() {
      Class var10000 = $class$groovy$lang$Script;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Script = class$("groovy.lang.Script");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$view$BasicStatusBar() {
      Class var10000 = $class$groovy$ui$view$BasicStatusBar;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$BasicStatusBar = class$("groovy.ui.view.BasicStatusBar");
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
