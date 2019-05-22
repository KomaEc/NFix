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

public class BasicMenuBar extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205134L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205134 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$BasicMenuBar;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public BasicMenuBar() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public BasicMenuBar(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$view$BasicMenuBar(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      return var1[1].callCurrent(this, (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicMenuBar$_run_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "File", "mnemonic", "F"}), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure2;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, (Object)var2[1].callGroovyObjectGetProperty(this));
                  var2[2].callCurrent(this, (Object)var2[3].callGroovyObjectGetProperty(this));
                  var2[4].callCurrent(this, (Object)var2[5].callGroovyObjectGetProperty(this));
                  var2[6].callCurrent(this);
                  var2[7].callCurrent(this, (Object)var2[8].callGroovyObjectGetProperty(this));
                  var2[9].callCurrent(this, (Object)var2[10].callGroovyObjectGetProperty(this));
                  var2[11].callCurrent(this);
                  var2[12].callCurrent(this, (Object)var2[13].callGroovyObjectGetProperty(this));
                  var2[14].callCurrent(this);
                  return var2[15].callCurrent(this, (Object)var2[16].callGroovyObjectGetProperty(this));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[17].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure2()) {
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
                  var0[0] = "menuItem";
                  var0[1] = "newFileAction";
                  var0[2] = "menuItem";
                  var0[3] = "newWindowAction";
                  var0[4] = "menuItem";
                  var0[5] = "openAction";
                  var0[6] = "separator";
                  var0[7] = "menuItem";
                  var0[8] = "saveAction";
                  var0[9] = "menuItem";
                  var0[10] = "saveAsAction";
                  var0[11] = "separator";
                  var0[12] = "menuItem";
                  var0[13] = "printAction";
                  var0[14] = "separator";
                  var0[15] = "menuItem";
                  var0[16] = "exitAction";
                  var0[17] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[18];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure2(), var0);
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
               private static Class $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure2() {
                  Class var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure2;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure2 = class$("groovy.ui.view.BasicMenuBar$_run_closure1_closure2");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            });
            var2[1].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "Edit", "mnemonic", "E"}), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure3;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, (Object)var2[1].callGroovyObjectGetProperty(this));
                  var2[2].callCurrent(this, (Object)var2[3].callGroovyObjectGetProperty(this));
                  var2[4].callCurrent(this);
                  var2[5].callCurrent(this, (Object)var2[6].callGroovyObjectGetProperty(this));
                  var2[7].callCurrent(this, (Object)var2[8].callGroovyObjectGetProperty(this));
                  var2[9].callCurrent(this, (Object)var2[10].callGroovyObjectGetProperty(this));
                  var2[11].callCurrent(this);
                  var2[12].callCurrent(this, (Object)var2[13].callGroovyObjectGetProperty(this));
                  var2[14].callCurrent(this, (Object)var2[15].callGroovyObjectGetProperty(this));
                  var2[16].callCurrent(this, (Object)var2[17].callGroovyObjectGetProperty(this));
                  var2[18].callCurrent(this, (Object)var2[19].callGroovyObjectGetProperty(this));
                  var2[20].callCurrent(this);
                  return var2[21].callCurrent(this, (Object)var2[22].callGroovyObjectGetProperty(this));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[23].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure3()) {
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
                  var0[0] = "menuItem";
                  var0[1] = "undoAction";
                  var0[2] = "menuItem";
                  var0[3] = "redoAction";
                  var0[4] = "separator";
                  var0[5] = "menuItem";
                  var0[6] = "cutAction";
                  var0[7] = "menuItem";
                  var0[8] = "copyAction";
                  var0[9] = "menuItem";
                  var0[10] = "pasteAction";
                  var0[11] = "separator";
                  var0[12] = "menuItem";
                  var0[13] = "findAction";
                  var0[14] = "menuItem";
                  var0[15] = "findNextAction";
                  var0[16] = "menuItem";
                  var0[17] = "findPreviousAction";
                  var0[18] = "menuItem";
                  var0[19] = "replaceAction";
                  var0[20] = "separator";
                  var0[21] = "menuItem";
                  var0[22] = "selectAllAction";
                  var0[23] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[24];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure3(), var0);
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
               private static Class $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure3() {
                  Class var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure3;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure3 = class$("groovy.ui.view.BasicMenuBar$_run_closure1_closure3");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            });
            var2[2].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "View", "mnemonic", "V"}), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure4;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, (Object)var2[1].callGroovyObjectGetProperty(this));
                  var2[2].callCurrent(this);
                  var2[3].callCurrent(this, (Object)var2[4].callGroovyObjectGetProperty(this));
                  var2[5].callCurrent(this, (Object)var2[6].callGroovyObjectGetProperty(this));
                  var2[7].callCurrent(this);
                  var2[8].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[9].callGetProperty(var2[10].callGroovyObjectGetProperty(this))}), var2[11].callGroovyObjectGetProperty(this));
                  var2[12].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[13].callGetProperty(var2[14].callGroovyObjectGetProperty(this))}), var2[15].callGroovyObjectGetProperty(this));
                  var2[16].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[17].callGetProperty(var2[18].callGroovyObjectGetProperty(this))}), var2[19].callGroovyObjectGetProperty(this));
                  var2[20].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[21].callGetProperty(var2[22].callGroovyObjectGetProperty(this))}), var2[23].callGroovyObjectGetProperty(this));
                  var2[24].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[25].callGetProperty(var2[26].callGroovyObjectGetProperty(this))}), var2[27].callGroovyObjectGetProperty(this));
                  var2[28].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[29].callGetProperty(var2[30].callGroovyObjectGetProperty(this))}), var2[31].callGroovyObjectGetProperty(this));
                  var2[32].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[33].callGetProperty(var2[34].callGroovyObjectGetProperty(this))}), var2[35].callGroovyObjectGetProperty(this));
                  return var2[36].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[37].callGetProperty(var2[38].callGroovyObjectGetProperty(this))}), var2[39].callGroovyObjectGetProperty(this));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[40].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure4()) {
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
                  var0[0] = "menuItem";
                  var0[1] = "clearOutputAction";
                  var0[2] = "separator";
                  var0[3] = "menuItem";
                  var0[4] = "largerFontAction";
                  var0[5] = "menuItem";
                  var0[6] = "smallerFontAction";
                  var0[7] = "separator";
                  var0[8] = "checkBoxMenuItem";
                  var0[9] = "captureStdOut";
                  var0[10] = "controller";
                  var0[11] = "captureStdOutAction";
                  var0[12] = "checkBoxMenuItem";
                  var0[13] = "captureStdErr";
                  var0[14] = "controller";
                  var0[15] = "captureStdErrAction";
                  var0[16] = "checkBoxMenuItem";
                  var0[17] = "fullStackTraces";
                  var0[18] = "controller";
                  var0[19] = "fullStackTracesAction";
                  var0[20] = "checkBoxMenuItem";
                  var0[21] = "showScriptInOutput";
                  var0[22] = "controller";
                  var0[23] = "showScriptInOutputAction";
                  var0[24] = "checkBoxMenuItem";
                  var0[25] = "visualizeScriptResults";
                  var0[26] = "controller";
                  var0[27] = "visualizeScriptResultsAction";
                  var0[28] = "checkBoxMenuItem";
                  var0[29] = "showToolbar";
                  var0[30] = "controller";
                  var0[31] = "showToolbarAction";
                  var0[32] = "checkBoxMenuItem";
                  var0[33] = "detachedOutput";
                  var0[34] = "controller";
                  var0[35] = "detachedOutputAction";
                  var0[36] = "checkBoxMenuItem";
                  var0[37] = "autoClearOutput";
                  var0[38] = "controller";
                  var0[39] = "autoClearOutputAction";
                  var0[40] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[41];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure4(), var0);
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
               private static Class $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure4() {
                  Class var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure4;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure4 = class$("groovy.ui.view.BasicMenuBar$_run_closure1_closure4");
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
            var2[3].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "History", "mnemonic", "I"}), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure5;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, (Object)var2[1].callGroovyObjectGetProperty(this));
                  return var2[2].callCurrent(this, (Object)var2[3].callGroovyObjectGetProperty(this));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure5()) {
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
                  var0[0] = "menuItem";
                  var0[1] = "historyPrevAction";
                  var0[2] = "menuItem";
                  var0[3] = "historyNextAction";
                  var0[4] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[5];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure5(), var0);
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
               private static Class $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure5() {
                  Class var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure5;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure5 = class$("groovy.ui.view.BasicMenuBar$_run_closure1_closure5");
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
            var2[4].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "Script", "mnemonic", "S"}), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure6;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, (Object)var2[1].callGroovyObjectGetProperty(this));
                  var2[2].callCurrent(this, (Object)var2[3].callGroovyObjectGetProperty(this));
                  var2[4].callCurrent(this, (Object)var2[5].callGroovyObjectGetProperty(this));
                  var2[6].callCurrent(this, (Object)var2[7].callGroovyObjectGetProperty(this));
                  var2[8].callCurrent(this);
                  var2[9].callCurrent(this, (Object)var2[10].callGroovyObjectGetProperty(this));
                  var2[11].callCurrent(this, (Object)var2[12].callGroovyObjectGetProperty(this));
                  var2[13].callCurrent(this, (Object)var2[14].callGroovyObjectGetProperty(this));
                  var2[15].callCurrent(this);
                  var2[16].callCurrent(this, (Object)var2[17].callGroovyObjectGetProperty(this));
                  var2[18].callCurrent(this, (Object)var2[19].callGroovyObjectGetProperty(this));
                  return var2[20].callCurrent(this, (Object)var2[21].callGroovyObjectGetProperty(this));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[22].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure6()) {
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
                  var0[0] = "menuItem";
                  var0[1] = "runAction";
                  var0[2] = "menuItem";
                  var0[3] = "runSelectionAction";
                  var0[4] = "menuItem";
                  var0[5] = "interruptAction";
                  var0[6] = "menuItem";
                  var0[7] = "compileAction";
                  var0[8] = "separator";
                  var0[9] = "menuItem";
                  var0[10] = "addClasspathJar";
                  var0[11] = "menuItem";
                  var0[12] = "addClasspathDir";
                  var0[13] = "menuItem";
                  var0[14] = "clearClassloader";
                  var0[15] = "separator";
                  var0[16] = "menuItem";
                  var0[17] = "inspectLastAction";
                  var0[18] = "menuItem";
                  var0[19] = "inspectVariablesAction";
                  var0[20] = "menuItem";
                  var0[21] = "inspectAstAction";
                  var0[22] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[23];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure6(), var0);
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
               private static Class $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure6() {
                  Class var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure6;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure6 = class$("groovy.ui.view.BasicMenuBar$_run_closure1_closure6");
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
            return var2[5].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "Help", "mnemonic", "H"}), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure7;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  return var2[0].callCurrent(this, (Object)var2[1].callGroovyObjectGetProperty(this));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure7()) {
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
                  var0[0] = "menuItem";
                  var0[1] = "aboutAction";
                  var0[2] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure7(), var0);
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
               private static Class $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1_closure7() {
                  Class var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure7;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1_closure7 = class$("groovy.ui.view.BasicMenuBar$_run_closure1_closure7");
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
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1()) {
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
            var0[0] = "menu";
            var0[1] = "menu";
            var0[2] = "menu";
            var0[3] = "menu";
            var0[4] = "menu";
            var0[5] = "menu";
            var0[6] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[7];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicMenuBar$_run_closure1(), var0);
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
         private static Class $get$$class$groovy$ui$view$BasicMenuBar$_run_closure1() {
            Class var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicMenuBar$_run_closure1 = class$("groovy.ui.view.BasicMenuBar$_run_closure1");
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
      }));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$view$BasicMenuBar()) {
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
      Class var10000 = $get$$class$groovy$ui$view$BasicMenuBar();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$view$BasicMenuBar(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$view$BasicMenuBar(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[1] = "menuBar";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[2];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$view$BasicMenuBar(), var0);
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
   private static Class $get$$class$groovy$lang$Script() {
      Class var10000 = $class$groovy$lang$Script;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Script = class$("groovy.lang.Script");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$view$BasicMenuBar() {
      Class var10000 = $class$groovy$ui$view$BasicMenuBar;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$BasicMenuBar = class$("groovy.ui.view.BasicMenuBar");
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
