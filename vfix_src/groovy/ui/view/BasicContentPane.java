package groovy.ui.view;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class BasicContentPane extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static final BigDecimal $const$0 = (BigDecimal)(new BigDecimal("0.5"));
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)100;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)119;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)81;
   // $FF: synthetic field
   private static final Integer $const$4 = (Integer)12;
   // $FF: synthetic field
   private static final Integer $const$5 = (Integer)-1;
   // $FF: synthetic field
   private static final Integer $const$6 = (Integer)0;
   // $FF: synthetic field
   private static final BigDecimal $const$7 = (BigDecimal)(new BigDecimal("1.0"));
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205096L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205096 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$awt$BorderLayout;
   // $FF: synthetic field
   private static Class $class$java$awt$Dimension;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$java$awt$Font;
   // $FF: synthetic field
   private static Class $class$java$util$prefs$Preferences;
   // $FF: synthetic field
   private static Class $class$java$awt$image$BufferedImage;
   // $FF: synthetic field
   private static Class $class$javax$swing$JSplitPane;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$ui$Console;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$Style;
   // $FF: synthetic field
   private static Class $class$java$awt$GraphicsEnvironment;
   // $FF: synthetic field
   private static Class $class$java$awt$Graphics;
   // $FF: synthetic field
   private static Class $class$java$awt$FontMetrics;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$javax$swing$WindowConstants;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$BasicContentPane;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$StyledDocument;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$StyleContext;

   public BasicContentPane() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public BasicContentPane(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$view$BasicContentPane(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      Object prefs = new Reference(var1[1].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$ui$Console()));
      Object detachedOutputFlag = var1[2].call(prefs.get(), "detachedOutput", Boolean.FALSE);
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[3].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"visible", Boolean.FALSE, "defaultCloseOperation", var1[4].callGetProperty($get$$class$javax$swing$WindowConstants())}), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$awt$Dimension;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicContentPane$_run_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            ScriptBytecodeAdapter.setGroovyObjectProperty(var2[0].callCurrent(this), $get$$class$groovy$ui$view$BasicContentPane$_run_closure1(), this, "blank");
            Dimension var10000 = (Dimension)ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createList(new Object[]{$const$0, $const$0}), $get$$class$java$awt$Dimension());
            ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$view$BasicContentPane$_run_closure1(), var2[1].callGroovyObjectGetProperty(this), "preferredSize");
            return var10000;
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_run_closure1()) {
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
            var0[0] = "glue";
            var0[1] = "blank";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_run_closure1(), var0);
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
         private static Class $get$$class$java$awt$Dimension() {
            Class var10000 = $class$java$awt$Dimension;
            if (var10000 == null) {
               var10000 = $class$java$awt$Dimension = class$("java.awt.Dimension");
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
         private static Class $get$$class$groovy$ui$view$BasicContentPane$_run_closure1() {
            Class var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure1 = class$("groovy.ui.view.BasicContentPane$_run_closure1");
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
      }), $get$$class$groovy$ui$view$BasicContentPane(), this, "outputWindow");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[5].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"resizeWeight", $const$0, "orientation", var1[6].callGetProperty($get$$class$javax$swing$JSplitPane())}), new GeneratedClosure(this, this, prefs) {
         private Reference<T> prefs;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleTextEditor;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicContentPane$_run_closure2;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.prefs = (Reference)prefs;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            ScriptBytecodeAdapter.setGroovyObjectProperty(var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"border", var2[1].callCurrent(this, (Object)$const$0)}), var2[2].callConstructor($get$$class$groovy$ui$ConsoleTextEditor())), $get$$class$groovy$ui$view$BasicContentPane$_run_closure2(), this, "inputEditor");
            return var2[3].callCurrent(this, (Object)this.prefs.get());
         }

         public Object getPrefs() {
            CallSite[] var1 = $getCallSiteArray();
            return this.prefs.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_run_closure2()) {
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
            var0[0] = "widget";
            var0[1] = "emptyBorder";
            var0[2] = "<$constructor$>";
            var0[3] = "buildOutputArea";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_run_closure2(), var0);
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
         private static Class $get$$class$groovy$ui$ConsoleTextEditor() {
            Class var10000 = $class$groovy$ui$ConsoleTextEditor;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleTextEditor = class$("groovy.ui.ConsoleTextEditor");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$view$BasicContentPane$_run_closure2() {
            Class var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure2 = class$("groovy.ui.view.BasicContentPane$_run_closure2");
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
      }), $get$$class$groovy$ui$view$BasicContentPane(), this, "splitPane");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[7].callGetProperty(var1[8].callGroovyObjectGetProperty(this)), $get$$class$groovy$ui$view$BasicContentPane(), this, "inputArea");
      var1[9].callCurrent(this, (Object)(new GeneratedClosure(this, this, prefs) {
         private Reference<T> prefs;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)12;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)4;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$awt$Font;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicContentPane$_run_closure3;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.prefs = (Reference)prefs;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"name", "inputArea", "font", var2[1].callConstructor($get$$class$java$awt$Font(), "Monospaced", var2[2].callGetProperty($get$$class$java$awt$Font()), var2[3].call(this.prefs.get(), "fontSize", $const$0)), "border", var2[4].callCurrent(this, (Object)$const$1)}), var2[5].callGroovyObjectGetProperty(this), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicContentPane$_run_closure3_closure7;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, (Object)var2[1].callGroovyObjectGetProperty(this));
                  var2[2].callCurrent(this, (Object)var2[3].callGroovyObjectGetProperty(this));
                  return var2[4].callCurrent(this, (Object)var2[5].callGroovyObjectGetProperty(this));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_run_closure3_closure7()) {
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
                  var0[0] = "action";
                  var0[1] = "runAction";
                  var0[2] = "action";
                  var0[3] = "runSelectionAction";
                  var0[4] = "action";
                  var0[5] = "showOutputWindowAction";
                  var0[6] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[7];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_run_closure3_closure7(), var0);
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
               private static Class $get$$class$groovy$ui$view$BasicContentPane$_run_closure3_closure7() {
                  Class var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure3_closure7;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure3_closure7 = class$("groovy.ui.view.BasicContentPane$_run_closure3_closure7");
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
            return var2[6].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"name", "outputArea"}), var2[7].callGroovyObjectGetProperty(this), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicContentPane$_run_closure3_closure8;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, (Object)var2[1].callGroovyObjectGetProperty(this));
                  var2[2].callCurrent(this, (Object)var2[3].callGroovyObjectGetProperty(this));
                  var2[4].callCurrent(this, (Object)var2[5].callGroovyObjectGetProperty(this));
                  return var2[6].callCurrent(this, (Object)var2[7].callGroovyObjectGetProperty(this));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[8].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_run_closure3_closure8()) {
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
                  var0[0] = "action";
                  var0[1] = "hideOutputWindowAction1";
                  var0[2] = "action";
                  var0[3] = "hideOutputWindowAction2";
                  var0[4] = "action";
                  var0[5] = "hideOutputWindowAction3";
                  var0[6] = "action";
                  var0[7] = "hideOutputWindowAction4";
                  var0[8] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[9];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_run_closure3_closure8(), var0);
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
               private static Class $get$$class$groovy$ui$view$BasicContentPane$_run_closure3_closure8() {
                  Class var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure3_closure8;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure3_closure8 = class$("groovy.ui.view.BasicContentPane$_run_closure3_closure8");
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

         public Object getPrefs() {
            CallSite[] var1 = $getCallSiteArray();
            return this.prefs.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[8].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_run_closure3()) {
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
            var0[0] = "container";
            var0[1] = "<$constructor$>";
            var0[2] = "PLAIN";
            var0[3] = "getInt";
            var0[4] = "emptyBorder";
            var0[5] = "inputArea";
            var0[6] = "container";
            var0[7] = "outputArea";
            var0[8] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[9];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_run_closure3(), var0);
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
         private static Class $get$$class$java$awt$Font() {
            Class var10000 = $class$java$awt$Font;
            if (var10000 == null) {
               var10000 = $class$java$awt$Font = class$("java.awt.Font");
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
         private static Class $get$$class$groovy$ui$view$BasicContentPane$_run_closure3() {
            Class var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure3 = class$("groovy.ui.view.BasicContentPane$_run_closure3");
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
      ScriptBytecodeAdapter.setProperty(var1[10].callConstructor($get$$class$java$awt$Font(), "Monospaced", var1[11].callGetProperty(var1[12].callGetProperty(var1[13].callGroovyObjectGetProperty(this))), var1[14].callGetProperty(var1[15].callGetProperty(var1[16].callGroovyObjectGetProperty(this)))), $get$$class$groovy$ui$view$BasicContentPane(), var1[17].callGroovyObjectGetProperty(this), "font");
      StyledDocument doc = (StyledDocument)ScriptBytecodeAdapter.castToType(var1[18].callGetProperty(var1[19].callGroovyObjectGetProperty(this)), $get$$class$javax$swing$text$StyledDocument());
      Style defStyle = (Style)ScriptBytecodeAdapter.castToType(var1[20].call(var1[21].callGetProperty($get$$class$javax$swing$text$StyleContext()), var1[22].callGetProperty($get$$class$javax$swing$text$StyleContext())), $get$$class$javax$swing$text$Style());
      Object applyStyle = new Reference(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicContentPane$_run_closure4;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Style style, Object values) {
            Style stylex = new Reference(style);
            Object valuesx = new Reference(values);
            CallSite[] var5 = $getCallSiteArray();
            return var5[0].call(valuesx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), stylex) {
               private Reference<T> style;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$javax$swing$text$Style;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$BasicContentPane$_run_closure4_closure9;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.style = (Reference)style;
               }

               public Object doCall(Object k, Object v) {
                  Object kx = new Reference(k);
                  Object vx = new Reference(v);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[0].call(this.style.get(), kx.get(), vx.get());
               }

               public Object call(Object k, Object v) {
                  Object kx = new Reference(k);
                  Object vx = new Reference(v);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[1].callCurrent(this, kx.get(), vx.get());
               }

               public Style getStyle() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Style)ScriptBytecodeAdapter.castToType(this.style.get(), $get$$class$javax$swing$text$Style());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_run_closure4_closure9()) {
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
                  var0[0] = "addAttribute";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_run_closure4_closure9(), var0);
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
               private static Class $get$$class$javax$swing$text$Style() {
                  Class var10000 = $class$javax$swing$text$Style;
                  if (var10000 == null) {
                     var10000 = $class$javax$swing$text$Style = class$("javax.swing.text.Style");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$ui$view$BasicContentPane$_run_closure4_closure9() {
                  Class var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure4_closure9;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure4_closure9 = class$("groovy.ui.view.BasicContentPane$_run_closure4_closure9");
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

         public Object call(Style style, Object values) {
            Style stylex = new Reference(style);
            Object valuesx = new Reference(values);
            CallSite[] var5 = $getCallSiteArray();
            return var5[1].callCurrent(this, stylex.get(), valuesx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_run_closure4()) {
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
            var0[0] = "each";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_run_closure4(), var0);
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
         private static Class $get$$class$groovy$ui$view$BasicContentPane$_run_closure4() {
            Class var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure4 = class$("groovy.ui.view.BasicContentPane$_run_closure4");
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
      Style regular = (Style)ScriptBytecodeAdapter.castToType(var1[23].call(doc, "regular", defStyle), $get$$class$javax$swing$text$Style());
      var1[24].call(applyStyle.get(), regular, var1[25].callGetProperty(var1[26].callGroovyObjectGetProperty(this)));
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[27].call(doc, "prompt", regular), $get$$class$groovy$ui$view$BasicContentPane(), this, "promptStyle");
      var1[28].call(applyStyle.get(), var1[29].callGroovyObjectGetProperty(this), var1[30].callGetProperty(var1[31].callGroovyObjectGetProperty(this)));
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[32].call(doc, "command", regular), $get$$class$groovy$ui$view$BasicContentPane(), this, "commandStyle");
      var1[33].call(applyStyle.get(), var1[34].callGroovyObjectGetProperty(this), var1[35].callGetProperty(var1[36].callGroovyObjectGetProperty(this)));
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[37].call(doc, "output", regular), $get$$class$groovy$ui$view$BasicContentPane(), this, "outputStyle");
      var1[38].call(applyStyle.get(), var1[39].callGroovyObjectGetProperty(this), var1[40].callGetProperty(var1[41].callGroovyObjectGetProperty(this)));
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[42].call(doc, "result", regular), $get$$class$groovy$ui$view$BasicContentPane(), this, "resultStyle");
      var1[43].call(applyStyle.get(), var1[44].callGroovyObjectGetProperty(this), var1[45].callGetProperty(var1[46].callGroovyObjectGetProperty(this)));
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[47].call(doc, "stacktrace", regular), $get$$class$groovy$ui$view$BasicContentPane(), this, "stacktraceStyle");
      var1[48].call(applyStyle.get(), var1[49].callGroovyObjectGetProperty(this), var1[50].callGetProperty(var1[51].callGroovyObjectGetProperty(this)));
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[52].call(doc, "hyperlink", regular), $get$$class$groovy$ui$view$BasicContentPane(), this, "hyperlinkStyle");
      var1[53].call(applyStyle.get(), var1[54].callGroovyObjectGetProperty(this), var1[55].callGetProperty(var1[56].callGroovyObjectGetProperty(this)));
      doc = (StyledDocument)ScriptBytecodeAdapter.castToType(var1[57].callGetProperty(var1[58].callGroovyObjectGetProperty(this)), $get$$class$javax$swing$text$StyledDocument());
      StyleContext styleContext = new Reference((StyleContext)ScriptBytecodeAdapter.castToType(var1[59].callGetProperty($get$$class$javax$swing$text$StyleContext()), $get$$class$javax$swing$text$StyleContext()));
      var1[60].call(var1[61].callGroovyObjectGetProperty(this), (Object)(new GeneratedClosure(this, this, styleContext, applyStyle) {
         private Reference<T> styleContext;
         private Reference<T> applyStyle;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$awt$Font;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicContentPane$_run_closure5;
         // $FF: synthetic field
         private static Class $class$javax$swing$text$StyleConstants;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$javax$swing$text$Style;
         // $FF: synthetic field
         private static Class $class$javax$swing$text$StyleContext;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.styleContext = (Reference)styleContext;
            this.applyStyle = (Reference)applyStyle;
         }

         public Object doCall(Object styleName, Object defs) {
            Object styleNamex = new Reference(styleName);
            Object defsx = new Reference(defs);
            CallSite[] var5 = $getCallSiteArray();
            Style style = new Reference((Style)ScriptBytecodeAdapter.castToType(var5[0].call(this.styleContext.get(), styleNamex.get()), $get$$class$javax$swing$text$Style()));
            if (!DefaultTypeTransformation.booleanUnbox(style.get())) {
               return null;
            } else {
               var5[1].call(this.applyStyle.get(), style.get(), defsx.get());
               String family = new Reference((String)ScriptBytecodeAdapter.castToType(var5[2].call(defsx.get(), var5[3].callGetProperty($get$$class$javax$swing$text$StyleConstants())), $get$$class$java$lang$String()));
               if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var5[4].callGetProperty(style.get()), "default") && DefaultTypeTransformation.booleanUnbox(family.get()) ? Boolean.TRUE : Boolean.FALSE)) {
                  ScriptBytecodeAdapter.setProperty(family.get(), $get$$class$groovy$ui$view$BasicContentPane$_run_closure5(), var5[5].callGroovyObjectGetProperty(this), "defaultFamily");
                  Object var10000 = var5[6].callConstructor($get$$class$java$awt$Font(), family.get(), var5[7].callGetProperty($get$$class$java$awt$Font()), var5[8].callGetProperty(var5[9].callGetProperty(var5[10].callGroovyObjectGetProperty(this))));
                  ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$view$BasicContentPane$_run_closure5(), var5[11].callGroovyObjectGetProperty(this), "font");
                  return var10000;
               } else {
                  return null;
               }
            }
         }

         public Object call(Object styleName, Object defs) {
            Object styleNamex = new Reference(styleName);
            Object defsx = new Reference(defs);
            CallSite[] var5 = $getCallSiteArray();
            return var5[12].callCurrent(this, styleNamex.get(), defsx.get());
         }

         public StyleContext getStyleContext() {
            CallSite[] var1 = $getCallSiteArray();
            return (StyleContext)ScriptBytecodeAdapter.castToType(this.styleContext.get(), $get$$class$javax$swing$text$StyleContext());
         }

         public Object getApplyStyle() {
            CallSite[] var1 = $getCallSiteArray();
            return this.applyStyle.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_run_closure5()) {
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
            var0[0] = "getStyle";
            var0[1] = "call";
            var0[2] = "getAt";
            var0[3] = "FontFamily";
            var0[4] = "name";
            var0[5] = "inputEditor";
            var0[6] = "<$constructor$>";
            var0[7] = "PLAIN";
            var0[8] = "size";
            var0[9] = "font";
            var0[10] = "inputArea";
            var0[11] = "inputArea";
            var0[12] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[13];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_run_closure5(), var0);
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
         private static Class $get$$class$java$awt$Font() {
            Class var10000 = $class$java$awt$Font;
            if (var10000 == null) {
               var10000 = $class$java$awt$Font = class$("java.awt.Font");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$view$BasicContentPane$_run_closure5() {
            Class var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicContentPane$_run_closure5 = class$("groovy.ui.view.BasicContentPane$_run_closure5");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$javax$swing$text$StyleConstants() {
            Class var10000 = $class$javax$swing$text$StyleConstants;
            if (var10000 == null) {
               var10000 = $class$javax$swing$text$StyleConstants = class$("javax.swing.text.StyleConstants");
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
         private static Class $get$$class$javax$swing$text$Style() {
            Class var10000 = $class$javax$swing$text$Style;
            if (var10000 == null) {
               var10000 = $class$javax$swing$text$Style = class$("javax.swing.text.Style");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$javax$swing$text$StyleContext() {
            Class var10000 = $class$javax$swing$text$StyleContext;
            if (var10000 == null) {
               var10000 = $class$javax$swing$text$StyleContext = class$("javax.swing.text.StyleContext");
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
      Graphics g = (Graphics)ScriptBytecodeAdapter.castToType(var1[62].call(var1[63].callGetProperty($get$$class$java$awt$GraphicsEnvironment()), var1[64].callConstructor($get$$class$java$awt$image$BufferedImage(), $const$1, $const$1, var1[65].callGetProperty($get$$class$java$awt$image$BufferedImage()))), $get$$class$java$awt$Graphics());
      FontMetrics fm = (FontMetrics)ScriptBytecodeAdapter.castToType(var1[66].call(g, (Object)var1[67].callGetProperty(var1[68].callGroovyObjectGetProperty(this))), $get$$class$java$awt$FontMetrics());
      ScriptBytecodeAdapter.setProperty((Dimension)ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createList(new Object[]{var1[69].call(prefs.get(), "outputAreaWidth", var1[70].call(var1[71].call(fm, (Object)$const$2), (Object)$const$3)), var1[72].call(prefs.get(), "outputAreaHeight", var1[73].call(var1[74].call(var1[75].call(fm), var1[76].callGetProperty(fm)), (Object)$const$4))}), $get$$class$java$awt$Dimension()), $get$$class$groovy$ui$view$BasicContentPane(), var1[77].callGroovyObjectGetProperty(this), "preferredSize");
      ScriptBytecodeAdapter.setProperty((Dimension)ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createList(new Object[]{var1[78].call(prefs.get(), "inputAreaWidth", var1[79].call(var1[80].call(fm, (Object)$const$2), (Object)$const$3)), var1[81].call(prefs.get(), "inputAreaHeight", var1[82].call(var1[83].call(var1[84].call(fm), var1[85].callGetProperty(fm)), (Object)$const$4))}), $get$$class$java$awt$Dimension()), $get$$class$groovy$ui$view$BasicContentPane(), var1[86].callGroovyObjectGetProperty(this), "preferredSize");
      ScriptBytecodeAdapter.setGroovyObjectProperty($const$5, $get$$class$groovy$ui$view$BasicContentPane(), this, "origDividerSize");
      if (DefaultTypeTransformation.booleanUnbox(detachedOutputFlag)) {
         var1[87].call(var1[88].callGroovyObjectGetProperty(this), var1[89].callGroovyObjectGetProperty(this), var1[90].callGetProperty($get$$class$javax$swing$JSplitPane()));
         ScriptBytecodeAdapter.setGroovyObjectProperty(var1[91].callGetProperty(var1[92].callGroovyObjectGetProperty(this)), $get$$class$groovy$ui$view$BasicContentPane(), this, "origDividerSize");
         ScriptBytecodeAdapter.setProperty($const$6, $get$$class$groovy$ui$view$BasicContentPane(), var1[93].callGroovyObjectGetProperty(this), "dividerSize");
         ScriptBytecodeAdapter.setProperty($const$7, $get$$class$groovy$ui$view$BasicContentPane(), var1[94].callGroovyObjectGetProperty(this), "resizeWeight");
         return var1[95].call(var1[96].callGroovyObjectGetProperty(this), var1[97].callGroovyObjectGetProperty(this), var1[98].callGetProperty($get$$class$java$awt$BorderLayout()));
      } else {
         return null;
      }
   }

   private Object buildOutputArea(Object prefs) {
      Object prefs = new Reference(prefs);
      CallSite[] var3 = $getCallSiteArray();
      Object var10000 = var3[99].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"border", var3[100].callCurrent(this, (Object)$const$6)}), new GeneratedClosure(this, this, prefs) {
         private Reference<T> prefs;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)255;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)218;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)12;
         // $FF: synthetic field
         private static final Integer $const$3 = (Integer)4;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$view$BasicContentPane$_buildOutputArea_closure6;
         // $FF: synthetic field
         private static Class $class$java$awt$Font;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$awt$Color;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.prefs = (Reference)prefs;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Object var10000 = var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"editable", Boolean.FALSE, "name", "outputArea", "contentType", "text/html", "background", var2[1].callConstructor($get$$class$java$awt$Color(), $const$0, $const$0, $const$1), "font", var2[2].callConstructor($get$$class$java$awt$Font(), "Monospaced", var2[3].callGetProperty($get$$class$java$awt$Font()), var2[4].call(this.prefs.get(), "fontSize", $const$2)), "border", var2[5].callCurrent(this, (Object)$const$3)}));
            ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$view$BasicContentPane$_buildOutputArea_closure6(), this, "outputArea");
            return var10000;
         }

         public Object getPrefs() {
            CallSite[] var1 = $getCallSiteArray();
            return this.prefs.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane$_buildOutputArea_closure6()) {
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
            var0[0] = "textPane";
            var0[1] = "<$constructor$>";
            var0[2] = "<$constructor$>";
            var0[3] = "PLAIN";
            var0[4] = "getInt";
            var0[5] = "emptyBorder";
            var0[6] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[7];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane$_buildOutputArea_closure6(), var0);
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
         private static Class $get$$class$groovy$ui$view$BasicContentPane$_buildOutputArea_closure6() {
            Class var10000 = $class$groovy$ui$view$BasicContentPane$_buildOutputArea_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$view$BasicContentPane$_buildOutputArea_closure6 = class$("groovy.ui.view.BasicContentPane$_buildOutputArea_closure6");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$Font() {
            Class var10000 = $class$java$awt$Font;
            if (var10000 == null) {
               var10000 = $class$java$awt$Font = class$("java.awt.Font");
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
         private static Class $get$$class$java$awt$Color() {
            Class var10000 = $class$java$awt$Color;
            if (var10000 == null) {
               var10000 = $class$java$awt$Color = class$("java.awt.Color");
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
      ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$view$BasicContentPane(), this, "scrollArea");
      return var10000;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$view$BasicContentPane()) {
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
      Class var10000 = $get$$class$groovy$ui$view$BasicContentPane();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$view$BasicContentPane(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$view$BasicContentPane(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$4$buildOutputArea(Object var1) {
      return this.buildOutputArea(var1);
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
      var0[1] = "userNodeForPackage";
      var0[2] = "getBoolean";
      var0[3] = "frame";
      var0[4] = "HIDE_ON_CLOSE";
      var0[5] = "splitPane";
      var0[6] = "VERTICAL_SPLIT";
      var0[7] = "textEditor";
      var0[8] = "inputEditor";
      var0[9] = "actions";
      var0[10] = "<$constructor$>";
      var0[11] = "style";
      var0[12] = "font";
      var0[13] = "outputArea";
      var0[14] = "size";
      var0[15] = "font";
      var0[16] = "outputArea";
      var0[17] = "outputArea";
      var0[18] = "styledDocument";
      var0[19] = "outputArea";
      var0[20] = "getStyle";
      var0[21] = "defaultStyleContext";
      var0[22] = "DEFAULT_STYLE";
      var0[23] = "addStyle";
      var0[24] = "call";
      var0[25] = "regular";
      var0[26] = "styles";
      var0[27] = "addStyle";
      var0[28] = "call";
      var0[29] = "promptStyle";
      var0[30] = "prompt";
      var0[31] = "styles";
      var0[32] = "addStyle";
      var0[33] = "call";
      var0[34] = "commandStyle";
      var0[35] = "command";
      var0[36] = "styles";
      var0[37] = "addStyle";
      var0[38] = "call";
      var0[39] = "outputStyle";
      var0[40] = "output";
      var0[41] = "styles";
      var0[42] = "addStyle";
      var0[43] = "call";
      var0[44] = "resultStyle";
      var0[45] = "result";
      var0[46] = "styles";
      var0[47] = "addStyle";
      var0[48] = "call";
      var0[49] = "stacktraceStyle";
      var0[50] = "stacktrace";
      var0[51] = "styles";
      var0[52] = "addStyle";
      var0[53] = "call";
      var0[54] = "hyperlinkStyle";
      var0[55] = "hyperlink";
      var0[56] = "styles";
      var0[57] = "styledDocument";
      var0[58] = "inputArea";
      var0[59] = "defaultStyleContext";
      var0[60] = "each";
      var0[61] = "styles";
      var0[62] = "createGraphics";
      var0[63] = "localGraphicsEnvironment";
      var0[64] = "<$constructor$>";
      var0[65] = "TYPE_INT_RGB";
      var0[66] = "getFontMetrics";
      var0[67] = "font";
      var0[68] = "outputArea";
      var0[69] = "getInt";
      var0[70] = "multiply";
      var0[71] = "charWidth";
      var0[72] = "getInt";
      var0[73] = "multiply";
      var0[74] = "plus";
      var0[75] = "getHeight";
      var0[76] = "leading";
      var0[77] = "outputArea";
      var0[78] = "getInt";
      var0[79] = "multiply";
      var0[80] = "charWidth";
      var0[81] = "getInt";
      var0[82] = "multiply";
      var0[83] = "plus";
      var0[84] = "getHeight";
      var0[85] = "leading";
      var0[86] = "inputEditor";
      var0[87] = "add";
      var0[88] = "splitPane";
      var0[89] = "blank";
      var0[90] = "BOTTOM";
      var0[91] = "dividerSize";
      var0[92] = "splitPane";
      var0[93] = "splitPane";
      var0[94] = "splitPane";
      var0[95] = "add";
      var0[96] = "outputWindow";
      var0[97] = "scrollArea";
      var0[98] = "CENTER";
      var0[99] = "scrollPane";
      var0[100] = "emptyBorder";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[101];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$view$BasicContentPane(), var0);
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
   private static Class $get$$class$java$awt$Dimension() {
      Class var10000 = $class$java$awt$Dimension;
      if (var10000 == null) {
         var10000 = $class$java$awt$Dimension = class$("java.awt.Dimension");
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
   private static Class $get$$class$java$awt$Font() {
      Class var10000 = $class$java$awt$Font;
      if (var10000 == null) {
         var10000 = $class$java$awt$Font = class$("java.awt.Font");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$prefs$Preferences() {
      Class var10000 = $class$java$util$prefs$Preferences;
      if (var10000 == null) {
         var10000 = $class$java$util$prefs$Preferences = class$("java.util.prefs.Preferences");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$image$BufferedImage() {
      Class var10000 = $class$java$awt$image$BufferedImage;
      if (var10000 == null) {
         var10000 = $class$java$awt$image$BufferedImage = class$("java.awt.image.BufferedImage");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JSplitPane() {
      Class var10000 = $class$javax$swing$JSplitPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JSplitPane = class$("javax.swing.JSplitPane");
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
   private static Class $get$$class$groovy$ui$Console() {
      Class var10000 = $class$groovy$ui$Console;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$Console = class$("groovy.ui.Console");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$text$Style() {
      Class var10000 = $class$javax$swing$text$Style;
      if (var10000 == null) {
         var10000 = $class$javax$swing$text$Style = class$("javax.swing.text.Style");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$GraphicsEnvironment() {
      Class var10000 = $class$java$awt$GraphicsEnvironment;
      if (var10000 == null) {
         var10000 = $class$java$awt$GraphicsEnvironment = class$("java.awt.GraphicsEnvironment");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$Graphics() {
      Class var10000 = $class$java$awt$Graphics;
      if (var10000 == null) {
         var10000 = $class$java$awt$Graphics = class$("java.awt.Graphics");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$FontMetrics() {
      Class var10000 = $class$java$awt$FontMetrics;
      if (var10000 == null) {
         var10000 = $class$java$awt$FontMetrics = class$("java.awt.FontMetrics");
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
   private static Class $get$$class$javax$swing$WindowConstants() {
      Class var10000 = $class$javax$swing$WindowConstants;
      if (var10000 == null) {
         var10000 = $class$javax$swing$WindowConstants = class$("javax.swing.WindowConstants");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$view$BasicContentPane() {
      Class var10000 = $class$groovy$ui$view$BasicContentPane;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$BasicContentPane = class$("groovy.ui.view.BasicContentPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$text$StyledDocument() {
      Class var10000 = $class$javax$swing$text$StyledDocument;
      if (var10000 == null) {
         var10000 = $class$javax$swing$text$StyledDocument = class$("javax.swing.text.StyledDocument");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$text$StyleContext() {
      Class var10000 = $class$javax$swing$text$StyleContext;
      if (var10000 == null) {
         var10000 = $class$javax$swing$text$StyleContext = class$("javax.swing.text.StyleContext");
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
