package groovy.ui.view;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class BasicToolBar extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205373L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205373 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$BasicToolBar;
   // $FF: synthetic field
   private static Class $class$java$awt$BorderLayout;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public BasicToolBar() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public BasicToolBar(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$view$BasicToolBar(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      Object var10000 = var1[1].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"rollover", Boolean.TRUE, "visible", var1[2].callGetProperty(var1[3].callGroovyObjectGetProperty(this)), "constraints", var1[4].callGetProperty($get$$class$java$awt$BorderLayout())}), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$javax$swing$SwingConstants;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicToolBar$_run_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[1].callGroovyObjectGetProperty(this));
            var2[2].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[3].callGroovyObjectGetProperty(this));
            var2[4].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[5].callGroovyObjectGetProperty(this));
            var2[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"orientation", var2[7].callGetProperty($get$$class$javax$swing$SwingConstants())}));
            var2[8].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[9].callGroovyObjectGetProperty(this));
            var2[10].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[11].callGroovyObjectGetProperty(this));
            var2[12].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"orientation", var2[13].callGetProperty($get$$class$javax$swing$SwingConstants())}));
            var2[14].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[15].callGroovyObjectGetProperty(this));
            var2[16].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[17].callGroovyObjectGetProperty(this));
            var2[18].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[19].callGroovyObjectGetProperty(this));
            var2[20].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"orientation", var2[21].callGetProperty($get$$class$javax$swing$SwingConstants())}));
            var2[22].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[23].callGroovyObjectGetProperty(this));
            var2[24].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[25].callGroovyObjectGetProperty(this));
            var2[26].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"orientation", var2[27].callGetProperty($get$$class$javax$swing$SwingConstants())}));
            var2[28].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[29].callGroovyObjectGetProperty(this));
            var2[30].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[31].callGroovyObjectGetProperty(this));
            var2[32].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"orientation", var2[33].callGetProperty($get$$class$javax$swing$SwingConstants())}));
            var2[34].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[35].callGroovyObjectGetProperty(this));
            return var2[36].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", null}), var2[37].callGroovyObjectGetProperty(this));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[38].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicToolBar$_run_closure1()) {
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
            var0[0] = "button";
            var0[1] = "newFileAction";
            var0[2] = "button";
            var0[3] = "openAction";
            var0[4] = "button";
            var0[5] = "saveAction";
            var0[6] = "separator";
            var0[7] = "VERTICAL";
            var0[8] = "button";
            var0[9] = "undoAction";
            var0[10] = "button";
            var0[11] = "redoAction";
            var0[12] = "separator";
            var0[13] = "VERTICAL";
            var0[14] = "button";
            var0[15] = "cutAction";
            var0[16] = "button";
            var0[17] = "copyAction";
            var0[18] = "button";
            var0[19] = "pasteAction";
            var0[20] = "separator";
            var0[21] = "VERTICAL";
            var0[22] = "button";
            var0[23] = "findAction";
            var0[24] = "button";
            var0[25] = "replaceAction";
            var0[26] = "separator";
            var0[27] = "VERTICAL";
            var0[28] = "button";
            var0[29] = "historyPrevAction";
            var0[30] = "button";
            var0[31] = "historyNextAction";
            var0[32] = "separator";
            var0[33] = "VERTICAL";
            var0[34] = "button";
            var0[35] = "runAction";
            var0[36] = "button";
            var0[37] = "interruptAction";
            var0[38] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[39];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicToolBar$_run_closure1(), var0);
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
         private static Class $get$$class$groovy$ui$view$BasicToolBar$_run_closure1() {
            Class var10000 = $class$groovy$ui$view$BasicToolBar$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicToolBar$_run_closure1 = class$("groovy.ui.view.BasicToolBar$_run_closure1");
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
      ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$view$BasicToolBar(), this, "toolbar");
      return var10000;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$view$BasicToolBar()) {
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
      Class var10000 = $get$$class$groovy$ui$view$BasicToolBar();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$view$BasicToolBar(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$view$BasicToolBar(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[1] = "toolBar";
      var0[2] = "showToolbar";
      var0[3] = "controller";
      var0[4] = "NORTH";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[5];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$view$BasicToolBar(), var0);
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
   private static Class $get$$class$groovy$ui$view$BasicToolBar() {
      Class var10000 = $class$groovy$ui$view$BasicToolBar;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$BasicToolBar = class$("groovy.ui.view.BasicToolBar");
      }

      return var10000;
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
