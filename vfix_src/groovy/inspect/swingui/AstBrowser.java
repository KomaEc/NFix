package groovy.inspect.swingui;

import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Map;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AstBrowser implements GroovyObject {
   private Object inputArea;
   private Object rootElement;
   private Object decompiledSource;
   private Object jTree;
   private Object propertyTable;
   private boolean showScriptFreeForm;
   private boolean showScriptClass;
   private GroovyClassLoader classLoader;
   private Object prefs;
   private Object swing;
   private Object frame;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)40;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)4;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202609L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202609 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$util$EventObject;
   // $FF: synthetic field
   private static Class $class$java$awt$Font;
   // $FF: synthetic field
   private static Class $class$java$awt$Cursor;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$javax$swing$tree$TreeSelectionModel;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$ui$Console;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$AstBrowserUiPreferences;
   // $FF: synthetic field
   private static Class $class$javax$swing$UIManager;
   // $FF: synthetic field
   private static Class $class$javax$swing$event$TreeSelectionListener;
   // $FF: synthetic field
   private static Class $class$groovy$swing$SwingBuilder;
   // $FF: synthetic field
   private static Class $class$javax$swing$WindowConstants;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$AstBrowser;

   public AstBrowser(Object inputArea, Object rootElement, Object classLoader) {
      CallSite[] var4 = $getCallSiteArray();
      this.prefs = var4[0].callConstructor($get$$class$groovy$inspect$swingui$AstBrowserUiPreferences());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.inputArea = inputArea;
      this.rootElement = rootElement;
      this.classLoader = (GroovyClassLoader)ScriptBytecodeAdapter.castToType(classLoader, $get$$class$groovy$lang$GroovyClassLoader());
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(args)) {
         var1[1].callStatic($get$$class$groovy$inspect$swingui$AstBrowser(), (Object)"Usage: java groovy.inspect.swingui.AstBrowser [filename]\nwhere [filename] is a Groovy script");
      } else {
         Object file = new Reference(var1[2].callConstructor($get$$class$java$io$File(), (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[3].call(args, (Object)$const$0), $get$$class$java$lang$String()), $get$$class$java$lang$String())));
         if (!DefaultTypeTransformation.booleanUnbox(var1[4].call(file.get()))) {
            var1[5].callStatic($get$$class$groovy$inspect$swingui$AstBrowser(), (Object)(new GStringImpl(new Object[]{args}, new String[]{"File ", "[0] cannot be found."})));
         } else {
            var1[6].call($get$$class$javax$swing$UIManager(), (Object)var1[7].call($get$$class$javax$swing$UIManager()));
            var1[8].call(var1[9].callConstructor($get$$class$groovy$inspect$swingui$AstBrowser(), (Object)null, (Object)null, var1[10].callConstructor($get$$class$groovy$lang$GroovyClassLoader())), new GeneratedClosure($get$$class$groovy$inspect$swingui$AstBrowser(), $get$$class$groovy$inspect$swingui$AstBrowser(), file) {
               private Reference<T> file;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstBrowser$_main_closure1;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.file = (Reference)file;
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  return var2[0].callGetProperty(this.file.get());
               }

               public Object getFile() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.file.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_main_closure1()) {
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
                  var0[0] = "text";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_main_closure1(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_main_closure1() {
                  Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_main_closure1;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstBrowser$_main_closure1 = class$("groovy.inspect.swingui.AstBrowser$_main_closure1");
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
            }, var1[11].callGetProperty(file.get()));
         }
      }

   }

   public void run(Closure script) {
      CallSite[] var2 = $getCallSiteArray();
      var2[12].callCurrent(this, script, (Object)null);
   }

   public void run(Closure script, String name) {
      Closure script = new Reference(script);
      CallSite[] var4 = $getCallSiteArray();
      this.swing = var4[13].callConstructor($get$$class$groovy$swing$SwingBuilder());
      Object phasePicker = new Reference((Object)null);
      Object splitterPane = new Reference((Object)null);
      Object mainSplitter = new Reference((Object)null);
      this.showScriptFreeForm = DefaultTypeTransformation.booleanUnbox(var4[14].callGetProperty(this.prefs));
      this.showScriptClass = DefaultTypeTransformation.booleanUnbox(var4[15].callGetProperty(this.prefs));
      this.frame = var4[16].call(this.swing, ScriptBytecodeAdapter.createMap(new Object[]{"title", var4[17].call("Groovy AST Browser", (Object)(DefaultTypeTransformation.booleanUnbox(name) ? new GStringImpl(new Object[]{name}, new String[]{" - ", ""}) : "")), "location", var4[18].callGetProperty(this.prefs), "size", var4[19].callGetProperty(this.prefs), "iconImage", var4[20].callGetProperty(var4[21].call(this.swing, var4[22].callGetProperty($get$$class$groovy$ui$Console()))), "defaultCloseOperation", var4[23].callGetProperty($get$$class$javax$swing$WindowConstants()), "windowClosing", new GeneratedClosure(this, this, phasePicker, splitterPane, mainSplitter) {
         private Reference<T> phasePicker;
         private Reference<T> splitterPane;
         private Reference<T> mainSplitter;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure2;

         public {
            CallSite[] var6 = $getCallSiteArray();
            this.phasePicker = (Reference)phasePicker;
            this.splitterPane = (Reference)splitterPane;
            this.mainSplitter = (Reference)mainSplitter;
         }

         public Object doCall(Object event) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].call(var2[1].callGroovyObjectGetProperty(this), ArrayUtil.createArray(var2[2].callGroovyObjectGetProperty(this), this.splitterPane.get(), this.mainSplitter.get(), var2[3].callGroovyObjectGetProperty(this), var2[4].callGroovyObjectGetProperty(this), var2[5].callGetProperty(this.phasePicker.get())));
         }

         public Object getPhasePicker() {
            CallSite[] var1 = $getCallSiteArray();
            return this.phasePicker.get();
         }

         public Object getSplitterPane() {
            CallSite[] var1 = $getCallSiteArray();
            return this.splitterPane.get();
         }

         public Object getMainSplitter() {
            CallSite[] var1 = $getCallSiteArray();
            return this.mainSplitter.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure2()) {
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
            var0[0] = "save";
            var0[1] = "prefs";
            var0[2] = "frame";
            var0[3] = "showScriptFreeForm";
            var0[4] = "showScriptClass";
            var0[5] = "selectedItem";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure2(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure2() {
            Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure2 = class$("groovy.inspect.swingui.AstBrowser$_run_closure2");
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
      }}), new GeneratedClosure(this, this, phasePicker, splitterPane, mainSplitter, script) {
         private Reference<T> phasePicker;
         private Reference<T> splitterPane;
         private Reference<T> mainSplitter;
         private Reference<T> script;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3;
         // $FF: synthetic field
         private static Class $class$groovy$lang$Closure;

         public {
            CallSite[] var7 = $getCallSiteArray();
            this.phasePicker = (Reference)phasePicker;
            this.splitterPane = (Reference)splitterPane;
            this.mainSplitter = (Reference)mainSplitter;
            this.script = (Reference)script;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            CallSite var10000 = var2[0];
            Object var10005 = this.getThisObject();
            Reference phasePicker = this.phasePicker;
            Reference script = this.script;
            var10000.callCurrent(this, (Object)(new GeneratedClosure(this, var10005, phasePicker, script) {
               private Reference<T> phasePicker;
               private Reference<T> script;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8;
               // $FF: synthetic field
               private static Class $class$groovy$lang$Closure;

               public {
                  Reference phasePickerx = new Reference(phasePicker);
                  Reference script = new Reference(scriptx);
                  CallSite[] var7 = $getCallSiteArray();
                  this.phasePicker = (Reference)((Reference)phasePickerx.get());
                  this.script = (Reference)((Reference)script.get());
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "Show Script", "mnemonic", "S"}), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[1].callGroovyObjectGetProperty(this)}), new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure13;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              return var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Free Form", "closure", ScriptBytecodeAdapter.getMethodPointer(this.getThisObject(), "showScriptFreeForm"), "mnemonic", "F"}));
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure13()) {
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
                              var0[1] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[2];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure13(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure13() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure13;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure13 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure10_closure13");
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
                        return var2[2].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"selected", var2[3].callGroovyObjectGetProperty(this)}), new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure14;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              return var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Class Form", "closure", ScriptBytecodeAdapter.getMethodPointer(this.getThisObject(), "showScriptClass"), "mnemonic", "C"}));
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure14()) {
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
                              var0[1] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[2];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure14(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure14() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure14;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10_closure14 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure10_closure14");
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
                        return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10()) {
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
                        var0[0] = "checkBoxMenuItem";
                        var0[1] = "showScriptFreeForm";
                        var0[2] = "checkBoxMenuItem";
                        var0[3] = "showScriptClass";
                        var0[4] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[5];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure10 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure10");
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
                  CallSite var10000 = var2[1];
                  Map var10002 = ScriptBytecodeAdapter.createMap(new Object[]{"text", "View", "mnemonic", "V"});
                  Object var10006 = this.getThisObject();
                  Reference phasePicker = this.phasePicker;
                  Reference script = this.script;
                  var10000.callCurrent(this, var10002, new GeneratedClosure(this, var10006, phasePicker, script) {
                     private Reference<T> phasePicker;
                     private Reference<T> script;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$lang$Closure;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11;

                     public {
                        Reference phasePickerx = new Reference(phasePicker);
                        Reference script = new Reference(scriptx);
                        CallSite[] var7 = $getCallSiteArray();
                        this.phasePicker = (Reference)((Reference)phasePickerx.get());
                        this.script = (Reference)((Reference)script.get());
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure15;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              return var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Larger Font", "closure", ScriptBytecodeAdapter.getMethodPointer(this.getThisObject(), "largerFont"), "mnemonic", "L", "accelerator", var2[1].callCurrent(this, (Object)"shift L")}));
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure15()) {
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
                              var0[1] = "shortcut";
                              var0[2] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[3];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure15(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure15() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure15;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure15 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure11_closure15");
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
                        var2[1].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure16;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              return var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Smaller Font", "closure", ScriptBytecodeAdapter.getMethodPointer(this.getThisObject(), "smallerFont"), "mnemonic", "S", "accelerator", var2[1].callCurrent(this, (Object)"shift S")}));
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure16()) {
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
                              var0[1] = "shortcut";
                              var0[2] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[3];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure16(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure16() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure16;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure16 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure11_closure16");
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
                        }));
                        CallSite var10000 = var2[2];
                        Object var10005 = this.getThisObject();
                        Reference phasePicker = this.phasePicker;
                        Reference script = this.script;
                        return var10000.callCurrent(this, (Object)(new GeneratedClosure(this, var10005, phasePicker, script) {
                           private Reference<T> phasePicker;
                           private Reference<T> script;
                           // $FF: synthetic field
                           private static final Integer $const$0 = (Integer)0;
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$javax$swing$KeyStroke;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$java$awt$event$KeyEvent;
                           // $FF: synthetic field
                           private static Class $class$groovy$lang$Closure;

                           public {
                              Reference phasePickerx = new Reference(phasePicker);
                              Reference script = new Reference(scriptx);
                              CallSite[] var7 = $getCallSiteArray();
                              this.phasePicker = (Reference)((Reference)phasePickerx.get());
                              this.script = (Reference)((Reference)script.get());
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              CallSite var10000 = var2[0];
                              Object[] var10002 = new Object[]{"name", "Refresh", "closure", null, null, null, null, null};
                              Object var10008 = this.getThisObject();
                              Reference phasePicker = this.phasePicker;
                              Reference script = this.script;
                              var10002[3] = new GeneratedClosure(this, var10008, phasePicker, script) {
                                 private Reference<T> phasePicker;
                                 private Reference<T> script;
                                 // $FF: synthetic field
                                 private static ClassInfo $staticClassInfo;
                                 // $FF: synthetic field
                                 private static SoftReference $callSiteArray;
                                 // $FF: synthetic field
                                 private static Class $class$java$lang$Object;
                                 // $FF: synthetic field
                                 private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17_closure18;
                                 // $FF: synthetic field
                                 private static Class $class$groovy$lang$Closure;

                                 public {
                                    Reference phasePickerx = new Reference(phasePicker);
                                    Reference script = new Reference(scriptx);
                                    CallSite[] var7 = $getCallSiteArray();
                                    this.phasePicker = (Reference)((Reference)phasePickerx.get());
                                    this.script = (Reference)((Reference)script.get());
                                 }

                                 public Object doCall(Object it) {
                                    CallSite[] var2 = $getCallSiteArray();
                                    var2[0].callCurrent(this, var2[1].callGetProperty(var2[2].callGetProperty(this.phasePicker.get())), var2[3].call(this.script.get()));
                                    return var2[4].callCurrent(this, var2[5].callGroovyObjectGetProperty(this), var2[6].call(this.script.get()), var2[7].callGetProperty(var2[8].callGetProperty(this.phasePicker.get())));
                                 }

                                 public Object getPhasePicker() {
                                    CallSite[] var1 = $getCallSiteArray();
                                    return this.phasePicker.get();
                                 }

                                 public Closure getScript() {
                                    CallSite[] var1 = $getCallSiteArray();
                                    return (Closure)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$groovy$lang$Closure());
                                 }

                                 public Object doCall() {
                                    CallSite[] var1 = $getCallSiteArray();
                                    return var1[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                 }

                                 // $FF: synthetic method
                                 protected MetaClass $getStaticMetaClass() {
                                    if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17_closure18()) {
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
                                    var0[0] = "decompile";
                                    var0[1] = "phaseId";
                                    var0[2] = "selectedItem";
                                    var0[3] = "call";
                                    var0[4] = "compile";
                                    var0[5] = "jTree";
                                    var0[6] = "call";
                                    var0[7] = "phaseId";
                                    var0[8] = "selectedItem";
                                    var0[9] = "doCall";
                                 }

                                 // $FF: synthetic method
                                 private static CallSiteArray $createCallSiteArray() {
                                    String[] var0 = new String[10];
                                    $createCallSiteArray_1(var0);
                                    return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17_closure18(), var0);
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
                                 private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17_closure18() {
                                    Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17_closure18;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17_closure18 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure11_closure17_closure18");
                                    }

                                    return var10000;
                                 }

                                 // $FF: synthetic method
                                 private static Class $get$$class$groovy$lang$Closure() {
                                    Class var10000 = $class$groovy$lang$Closure;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
                              };
                              var10002[4] = "mnemonic";
                              var10002[5] = "R";
                              var10002[6] = "accelerator";
                              var10002[7] = var2[1].call($get$$class$javax$swing$KeyStroke(), var2[2].callGetProperty($get$$class$java$awt$event$KeyEvent()), $const$0);
                              return var10000.callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(var10002));
                           }

                           public Object getPhasePicker() {
                              CallSite[] var1 = $getCallSiteArray();
                              return this.phasePicker.get();
                           }

                           public Closure getScript() {
                              CallSite[] var1 = $getCallSiteArray();
                              return (Closure)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$groovy$lang$Closure());
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17()) {
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
                              var0[1] = "getKeyStroke";
                              var0[2] = "VK_F5";
                              var0[3] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[4];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17(), var0);
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
                           private static Class $get$$class$javax$swing$KeyStroke() {
                              Class var10000 = $class$javax$swing$KeyStroke;
                              if (var10000 == null) {
                                 var10000 = $class$javax$swing$KeyStroke = class$("javax.swing.KeyStroke");
                              }

                              return var10000;
                           }

                           // $FF: synthetic method
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11_closure17 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure11_closure17");
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
                           private static Class $get$$class$java$awt$event$KeyEvent() {
                              Class var10000 = $class$java$awt$event$KeyEvent;
                              if (var10000 == null) {
                                 var10000 = $class$java$awt$event$KeyEvent = class$("java.awt.event.KeyEvent");
                              }

                              return var10000;
                           }

                           // $FF: synthetic method
                           private static Class $get$$class$groovy$lang$Closure() {
                              Class var10000 = $class$groovy$lang$Closure;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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

                     public Object getPhasePicker() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.phasePicker.get();
                     }

                     public Closure getScript() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (Closure)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$groovy$lang$Closure());
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11()) {
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
                        var0[1] = "menuItem";
                        var0[2] = "menuItem";
                        var0[3] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[4];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11(), var0);
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
                     private static Class $get$$class$groovy$lang$Closure() {
                        Class var10000 = $class$groovy$lang$Closure;
                        if (var10000 == null) {
                           var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure11 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure11");
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
                  return var2[2].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "Help", "mnemonic", "H"}), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        return var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12_closure19;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              return var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "About", "closure", ScriptBytecodeAdapter.getMethodPointer(this.getThisObject(), "showAbout"), "mnemonic", "A"}));
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12_closure19()) {
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
                              var0[1] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[2];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12_closure19(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12_closure19() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12_closure19;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12_closure19 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure12_closure19");
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

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12()) {
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
                        var0[1] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[2];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8_closure12 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8_closure12");
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

               public Object getPhasePicker() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.phasePicker.get();
               }

               public Closure getScript() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Closure)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$groovy$lang$Closure());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8()) {
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
                  var0[3] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8() {
                  Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure8 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure8");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$lang$Closure() {
                  Class var10000 = $class$groovy$lang$Closure;
                  if (var10000 == null) {
                     var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
            var10000 = var2[1];
            var10005 = this.getThisObject();
            Reference splitterPane = this.splitterPane;
            Reference mainSplitter = this.mainSplitter;
            return var10000.callCurrent(this, (Object)(new GeneratedClosure(this, var10005, phasePicker, splitterPane, mainSplitter, script) {
               private Reference<T> phasePicker;
               private Reference<T> splitterPane;
               private Reference<T> mainSplitter;
               private Reference<T> script;
               // $FF: synthetic field
               private static final Integer $const$0 = (Integer)0;
               // $FF: synthetic field
               private static final Integer $const$1 = (Integer)1;
               // $FF: synthetic field
               private static final Integer $const$2 = (Integer)2;
               // $FF: synthetic field
               private static final BigDecimal $const$3 = (BigDecimal)(new BigDecimal("1.0"));
               // $FF: synthetic field
               private static final Integer $const$4 = (Integer)3;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$CompilePhaseAdapter;
               // $FF: synthetic field
               private static Class $class$java$awt$GridBagConstraints;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$javax$swing$JSplitPane;
               // $FF: synthetic field
               private static Class $class$groovy$lang$Closure;

               public {
                  Reference splitterPane = new Reference(splitterPanex);
                  Reference mainSplitter = new Reference(mainSplitterx);
                  CallSite[] var9 = $getCallSiteArray();
                  this.phasePicker = (Reference)phasePicker;
                  this.splitterPane = (Reference)((Reference)splitterPane.get());
                  this.mainSplitter = (Reference)((Reference)mainSplitter.get());
                  this.script = (Reference)script;
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this);
                  var2[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"text", "At end of Phase: ", "constraints", var2[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"gridx", $const$0, "gridy", $const$0, "gridwidth", $const$1, "gridheight", $const$1, "weightx", $const$0, "weighty", $const$0, "anchor", var2[3].callGetProperty($get$$class$java$awt$GridBagConstraints()), "fill", var2[4].callGetProperty($get$$class$java$awt$GridBagConstraints()), "insets", ScriptBytecodeAdapter.createList(new Object[]{$const$2, $const$2, $const$2, $const$2})}))}));
                  CallSite var10000 = var2[5];
                  Object[] var10002 = new Object[]{"items", var2[6].call($get$$class$groovy$inspect$swingui$CompilePhaseAdapter()), "selectedItem", var2[7].callGetProperty(var2[8].callGroovyObjectGetProperty(this)), "actionPerformed", null, null, null};
                  Object var10008 = this.getThisObject();
                  Reference phasePicker = this.phasePicker;
                  Reference script = this.script;
                  var10002[5] = new GeneratedClosure(this, var10008, phasePicker, script) {
                     private Reference<T> phasePicker;
                     private Reference<T> script;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure20;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$lang$Closure;

                     public {
                        Reference phasePickerx = new Reference(phasePicker);
                        Reference script = new Reference(scriptx);
                        CallSite[] var7 = $getCallSiteArray();
                        this.phasePicker = (Reference)((Reference)phasePickerx.get());
                        this.script = (Reference)((Reference)script.get());
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        var2[0].callCurrent(this, var2[1].callGetProperty(var2[2].callGetProperty(this.phasePicker.get())), var2[3].call(this.script.get()));
                        return var2[4].callCurrent(this, var2[5].callGroovyObjectGetProperty(this), var2[6].call(this.script.get()), var2[7].callGetProperty(var2[8].callGetProperty(this.phasePicker.get())));
                     }

                     public Object getPhasePicker() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.phasePicker.get();
                     }

                     public Closure getScript() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (Closure)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$groovy$lang$Closure());
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure20()) {
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
                        var0[0] = "decompile";
                        var0[1] = "phaseId";
                        var0[2] = "selectedItem";
                        var0[3] = "call";
                        var0[4] = "compile";
                        var0[5] = "jTree";
                        var0[6] = "call";
                        var0[7] = "phaseId";
                        var0[8] = "selectedItem";
                        var0[9] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[10];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure20(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure20() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure20;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure20 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure20");
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
                     private static Class $get$$class$groovy$lang$Closure() {
                        Class var10000 = $class$groovy$lang$Closure;
                        if (var10000 == null) {
                           var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
                  };
                  var10002[6] = "constraints";
                  var10002[7] = var2[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"gridx", $const$1, "gridy", $const$0, "gridwidth", $const$1, "gridheight", $const$1, "weightx", $const$3, "weighty", $const$0, "anchor", var2[10].callGetProperty($get$$class$java$awt$GridBagConstraints()), "fill", var2[11].callGetProperty($get$$class$java$awt$GridBagConstraints()), "insets", ScriptBytecodeAdapter.createList(new Object[]{$const$2, $const$2, $const$2, $const$2})}));
                  phasePicker.set(var10000.callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(var10002)));
                  var2[12].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"text", "Refresh", "actionPerformed", new GeneratedClosure(this, this.getThisObject(), phasePicker, script) {
                     private Reference<T> phasePicker;
                     private Reference<T> script;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure21;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$lang$Closure;

                     public {
                        CallSite[] var5 = $getCallSiteArray();
                        this.phasePicker = (Reference)phasePicker;
                        this.script = (Reference)script;
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        var2[0].callCurrent(this, var2[1].callGetProperty(var2[2].callGetProperty(this.phasePicker.get())), var2[3].call(this.script.get()));
                        return var2[4].callCurrent(this, var2[5].callGroovyObjectGetProperty(this), var2[6].call(this.script.get()), var2[7].callGetProperty(var2[8].callGetProperty(this.phasePicker.get())));
                     }

                     public Object getPhasePicker() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.phasePicker.get();
                     }

                     public Closure getScript() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (Closure)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$groovy$lang$Closure());
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure21()) {
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
                        var0[0] = "decompile";
                        var0[1] = "phaseId";
                        var0[2] = "selectedItem";
                        var0[3] = "call";
                        var0[4] = "compile";
                        var0[5] = "jTree";
                        var0[6] = "call";
                        var0[7] = "phaseId";
                        var0[8] = "selectedItem";
                        var0[9] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[10];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure21(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure21() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure21;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure21 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure21");
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
                     private static Class $get$$class$groovy$lang$Closure() {
                        Class var10000 = $class$groovy$lang$Closure;
                        if (var10000 == null) {
                           var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
                  }, "constraints", var2[13].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"gridx", $const$2, "gridy", $const$0, "gridwidth", $const$1, "gridheight", $const$1, "weightx", $const$0, "weighty", $const$0, "anchor", var2[14].callGetProperty($get$$class$java$awt$GridBagConstraints()), "fill", var2[15].callGetProperty($get$$class$java$awt$GridBagConstraints()), "insets", ScriptBytecodeAdapter.createList(new Object[]{$const$2, $const$2, $const$2, $const$4})}))}));
                  this.splitterPane.set(var2[16].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"leftComponent", var2[17].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22;
                     // $FF: synthetic field
                     private static Class $class$javax$swing$tree$DefaultTreeModel;
                     // $FF: synthetic field
                     private static Class $class$javax$swing$tree$DefaultMutableTreeNode;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        Object var10000 = var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"name", "AstTreeView", "model", var2[1].callConstructor($get$$class$javax$swing$tree$DefaultTreeModel(), (Object)var2[2].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)"Loading..."))}), new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22_closure27;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              return null;
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22_closure27()) {
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
                              var0[0] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[1];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22_closure27(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22_closure27() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22_closure27;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22_closure27 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure22_closure27");
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
                        ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22(), this, "jTree");
                        return var10000;
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22()) {
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
                        var0[0] = "tree";
                        var0[1] = "<$constructor$>";
                        var0[2] = "<$constructor$>";
                        var0[3] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[4];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure22 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure22");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$javax$swing$tree$DefaultTreeModel() {
                        Class var10000 = $class$javax$swing$tree$DefaultTreeModel;
                        if (var10000 == null) {
                           var10000 = $class$javax$swing$tree$DefaultTreeModel = class$("javax.swing.tree.DefaultTreeModel");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$javax$swing$tree$DefaultMutableTreeNode() {
                        Class var10000 = $class$javax$swing$tree$DefaultMutableTreeNode;
                        if (var10000 == null) {
                           var10000 = $class$javax$swing$tree$DefaultMutableTreeNode = class$("javax.swing.tree.DefaultMutableTreeNode");
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
                  })), "rightComponent", var2[18].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        Object var10000 = var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              return var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"list", ScriptBytecodeAdapter.createList(new Object[]{ScriptBytecodeAdapter.createMap(new Object[0])})}), new GeneratedClosure(this, this.getThisObject()) {
                                 // $FF: synthetic field
                                 private static ClassInfo $staticClassInfo;
                                 // $FF: synthetic field
                                 private static SoftReference $callSiteArray;
                                 // $FF: synthetic field
                                 private static Class $class$java$lang$Object;
                                 // $FF: synthetic field
                                 private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28_closure29;

                                 public {
                                    CallSite[] var3 = $getCallSiteArray();
                                 }

                                 public Object doCall(Object it) {
                                    CallSite[] var2 = $getCallSiteArray();
                                    var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Name", "propertyName", "name"}));
                                    var2[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Value", "propertyName", "value"}));
                                    return var2[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Type", "propertyName", "type"}));
                                 }

                                 public Object doCall() {
                                    CallSite[] var1 = $getCallSiteArray();
                                    return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                 }

                                 // $FF: synthetic method
                                 protected MetaClass $getStaticMetaClass() {
                                    if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28_closure29()) {
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
                                    var0[0] = "propertyColumn";
                                    var0[1] = "propertyColumn";
                                    var0[2] = "propertyColumn";
                                    var0[3] = "doCall";
                                 }

                                 // $FF: synthetic method
                                 private static CallSiteArray $createCallSiteArray() {
                                    String[] var0 = new String[4];
                                    $createCallSiteArray_1(var0);
                                    return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28_closure29(), var0);
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
                                 private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28_closure29() {
                                    Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28_closure29;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28_closure29 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure23_closure28_closure29");
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
                              return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28()) {
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
                              var0[0] = "tableModel";
                              var0[1] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[2];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23_closure28 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure23_closure28");
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
                        ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23(), this, "propertyTable");
                        return var10000;
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23()) {
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
                        var0[0] = "table";
                        var0[1] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[2];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure23 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure23");
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
                  }))}), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure24;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        return null;
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure24()) {
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
                        var0[0] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[1];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure24(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure24() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure24;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure24 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure24");
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
                  Object var5 = var2[19].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"orientation", var2[20].callGetProperty($get$$class$javax$swing$JSplitPane()), "topComponent", this.splitterPane.get(), "bottomComponent", var2[21].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure25;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        Object var10000 = var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"editable", Boolean.FALSE}));
                        ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure25(), this, "decompiledSource");
                        return var10000;
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure25()) {
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
                        var0[0] = "textArea";
                        var0[1] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[2];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure25(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure25() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure25;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure25 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure25");
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
                  })), "constraints", var2[22].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"gridx", $const$0, "gridy", $const$2, "gridwidth", $const$4, "gridheight", $const$1, "weightx", $const$3, "weighty", $const$3, "anchor", var2[23].callGetProperty($get$$class$java$awt$GridBagConstraints()), "fill", var2[24].callGetProperty($get$$class$java$awt$GridBagConstraints()), "insets", ScriptBytecodeAdapter.createList(new Object[]{$const$2, $const$2, $const$2, $const$2})}))}), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure26;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        return null;
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure26()) {
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
                        var0[0] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[1];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure26(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure26() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure26;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9_closure26 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9_closure26");
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
                  this.mainSplitter.set(var5);
                  return var5;
               }

               public Object getPhasePicker() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.phasePicker.get();
               }

               public Object getSplitterPane() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.splitterPane.get();
               }

               public Object getMainSplitter() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.mainSplitter.get();
               }

               public Closure getScript() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Closure)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$groovy$lang$Closure());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[25].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9()) {
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
                  var0[1] = "label";
                  var0[2] = "gbc";
                  var0[3] = "WEST";
                  var0[4] = "HORIZONTAL";
                  var0[5] = "comboBox";
                  var0[6] = "values";
                  var0[7] = "selectedPhase";
                  var0[8] = "prefs";
                  var0[9] = "gbc";
                  var0[10] = "NORTHWEST";
                  var0[11] = "NONE";
                  var0[12] = "button";
                  var0[13] = "gbc";
                  var0[14] = "NORTHEAST";
                  var0[15] = "NONE";
                  var0[16] = "splitPane";
                  var0[17] = "scrollPane";
                  var0[18] = "scrollPane";
                  var0[19] = "splitPane";
                  var0[20] = "VERTICAL_SPLIT";
                  var0[21] = "scrollPane";
                  var0[22] = "gbc";
                  var0[23] = "NORTHWEST";
                  var0[24] = "BOTH";
                  var0[25] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[26];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$CompilePhaseAdapter() {
                  Class var10000 = $class$groovy$inspect$swingui$CompilePhaseAdapter;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$CompilePhaseAdapter = class$("groovy.inspect.swingui.CompilePhaseAdapter");
                  }

                  return var10000;
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
               private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9() {
                  Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3_closure9 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3_closure9");
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
               private static Class $get$$class$javax$swing$JSplitPane() {
                  Class var10000 = $class$javax$swing$JSplitPane;
                  if (var10000 == null) {
                     var10000 = $class$javax$swing$JSplitPane = class$("javax.swing.JSplitPane");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$lang$Closure() {
                  Class var10000 = $class$groovy$lang$Closure;
                  if (var10000 == null) {
                     var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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

         public Object getPhasePicker() {
            CallSite[] var1 = $getCallSiteArray();
            return this.phasePicker.get();
         }

         public Object getSplitterPane() {
            CallSite[] var1 = $getCallSiteArray();
            return this.splitterPane.get();
         }

         public Object getMainSplitter() {
            CallSite[] var1 = $getCallSiteArray();
            return this.mainSplitter.get();
         }

         public Closure getScript() {
            CallSite[] var1 = $getCallSiteArray();
            return (Closure)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$groovy$lang$Closure());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3()) {
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
            var0[0] = "menuBar";
            var0[1] = "panel";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure3() {
            Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure3 = class$("groovy.inspect.swingui.AstBrowser$_run_closure3");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$lang$Closure() {
            Class var10000 = $class$groovy$lang$Closure;
            if (var10000 == null) {
               var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
      var4[24].call(var4[25].callGetProperty(var4[26].callGetProperty(this.propertyTable)));
      var4[27].call(var4[28].callGetProperty(this.jTree), var4[29].call(this.swing, var4[30].callGetProperty($get$$class$groovy$ui$Console())));
      ScriptBytecodeAdapter.setProperty(var4[31].callGetProperty($get$$class$javax$swing$tree$TreeSelectionModel()), $get$$class$groovy$inspect$swingui$AstBrowser(), var4[32].callGetProperty(this.jTree), "selectionMode");
      var4[33].call(this.jTree, (Object)ScriptBytecodeAdapter.createPojoWrapper((TreeSelectionListener)ScriptBytecodeAdapter.asType(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)1;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$javax$swing$tree$TreeNode;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure4;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(TreeSelectionEvent e) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].call(var2[1].callGetProperty(var2[2].callGetProperty(var2[3].callGroovyObjectGetProperty(this))));
            TreeNode node = new Reference((TreeNode)ScriptBytecodeAdapter.castToType(var2[4].callGetProperty(var2[5].callGroovyObjectGetProperty(this)), $get$$class$javax$swing$tree$TreeNode()));
            if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareNotEqual(node.get(), (Object)null) && node.get() instanceof TreeNodeWithProperties ? Boolean.TRUE : Boolean.FALSE)) {
               var2[6].call(var2[7].callGetProperty(node.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
                  // $FF: synthetic field
                  private static final Integer $const$0 = (Integer)0;
                  // $FF: synthetic field
                  private static final Integer $const$1 = (Integer)1;
                  // $FF: synthetic field
                  private static final Integer $const$2 = (Integer)2;
                  // $FF: synthetic field
                  private static ClassInfo $staticClassInfo;
                  // $FF: synthetic field
                  private static SoftReference $callSiteArray;
                  // $FF: synthetic field
                  private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure30;
                  // $FF: synthetic field
                  private static Class $class$java$lang$Object;

                  public {
                     CallSite[] var3 = $getCallSiteArray();
                  }

                  public Object doCall(Object it) {
                     Object itx = new Reference(it);
                     CallSite[] var3 = $getCallSiteArray();
                     return var3[0].call(var3[1].callGetProperty(var3[2].callGetProperty(var3[3].callGroovyObjectGetProperty(this))), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", var3[4].call(itx.get(), (Object)$const$0), "value", var3[5].call(itx.get(), (Object)$const$1), "type", var3[6].call(itx.get(), (Object)$const$2)}));
                  }

                  public Object doCall() {
                     CallSite[] var1 = $getCallSiteArray();
                     return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                  }

                  // $FF: synthetic method
                  protected MetaClass $getStaticMetaClass() {
                     if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure30()) {
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
                     var0[0] = "leftShift";
                     var0[1] = "rows";
                     var0[2] = "model";
                     var0[3] = "propertyTable";
                     var0[4] = "getAt";
                     var0[5] = "getAt";
                     var0[6] = "getAt";
                     var0[7] = "doCall";
                  }

                  // $FF: synthetic method
                  private static CallSiteArray $createCallSiteArray() {
                     String[] var0 = new String[8];
                     $createCallSiteArray_1(var0);
                     return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure30(), var0);
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
                  private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure30() {
                     Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure30;
                     if (var10000 == null) {
                        var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure30 = class$("groovy.inspect.swingui.AstBrowser$_run_closure4_closure30");
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
               }));
               if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var2[8].callGroovyObjectGetProperty(this)) && DefaultTypeTransformation.booleanUnbox(var2[9].callGroovyObjectGetProperty(this)) ? Boolean.TRUE : Boolean.FALSE)) {
                  Object lineInfo = var2[10].call(var2[11].callGetProperty(node.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static final Integer $const$0 = (Integer)0;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure31;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        Object itx = new Reference(it);
                        CallSite[] var3 = $getCallSiteArray();
                        return ScriptBytecodeAdapter.isCase(var3[0].call(itx.get(), (Object)$const$0), ScriptBytecodeAdapter.createList(new Object[]{"lineNumber", "columnNumber", "lastLineNumber", "lastColumnNumber"})) ? Boolean.TRUE : Boolean.FALSE;
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure31()) {
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
                        var0[0] = "getAt";
                        var0[1] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[2];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure31(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure31() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure31;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure31 = class$("groovy.inspect.swingui.AstBrowser$_run_closure4_closure31");
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
                  Object lineInfoMap = new Reference(var2[12].call(lineInfo, ScriptBytecodeAdapter.createMap(new Object[0]), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static final Integer $const$0 = (Integer)0;
                     // $FF: synthetic field
                     private static final Integer $const$1 = (Integer)1;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Integer;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure32;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object map, Object info) {
                        Object mapx = new Reference(map);
                        Object infox = new Reference(info);
                        CallSite[] var5 = $getCallSiteArray();
                        CallSite var10000 = var5[0];
                        Object var10001 = mapx.get();
                        Object var10002 = var5[1].call(infox.get(), (Object)$const$0);
                        Object var6 = var5[2].call($get$$class$java$lang$Integer(), (Object)var5[3].call(infox.get(), (Object)$const$1));
                        var10000.call(var10001, var10002, var6);
                        return mapx.get();
                     }

                     public Object call(Object map, Object info) {
                        Object mapx = new Reference(map);
                        Object infox = new Reference(info);
                        CallSite[] var5 = $getCallSiteArray();
                        return var5[4].callCurrent(this, mapx.get(), infox.get());
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure32()) {
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
                        var0[0] = "putAt";
                        var0[1] = "getAt";
                        var0[2] = "valueOf";
                        var0[3] = "getAt";
                        var0[4] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[5];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure32(), var0);
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
                     private static Class $get$$class$java$lang$Integer() {
                        Class var10000 = $class$java$lang$Integer;
                        if (var10000 == null) {
                           var10000 = $class$java$lang$Integer = class$("java.lang.Integer");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure32() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure32;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure32 = class$("groovy.inspect.swingui.AstBrowser$_run_closure4_closure32");
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
                  if (!DefaultTypeTransformation.booleanUnbox(var2[13].call(lineInfoMap.get(), (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static final Integer $const$0 = (Integer)-1;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure33;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object k, Object vx) {
                        Object v = new Reference(vx);
                        CallSite[] var4 = $getCallSiteArray();
                        return ScriptBytecodeAdapter.compareEqual(v.get(), $const$0) ? Boolean.TRUE : Boolean.FALSE;
                     }

                     public Object call(Object k, Object vx) {
                        Object v = new Reference(vx);
                        CallSite[] var4 = $getCallSiteArray();
                        return var4[0].callCurrent(this, k, v.get());
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure33()) {
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
                        var0[0] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[1];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure33(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure33() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure33;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4_closure33 = class$("groovy.inspect.swingui.AstBrowser$_run_closure4_closure33");
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
                  })))) {
                     Object startOffset = var2[14].callGetProperty(var2[15].call(var2[16].callGroovyObjectGetProperty(this), var2[17].call(var2[18].callGetProperty(lineInfoMap.get()), (Object)$const$0)));
                     var2[19].call(var2[20].callGroovyObjectGetProperty(this), var2[21].call(var2[22].call(startOffset, var2[23].callGetProperty(lineInfoMap.get())), (Object)$const$0));
                     Object endOffset = var2[24].callGetProperty(var2[25].call(var2[26].callGroovyObjectGetProperty(this), var2[27].call(var2[28].callGetProperty(lineInfoMap.get()), (Object)$const$0)));
                     var2[29].call(var2[30].callGroovyObjectGetProperty(this), var2[31].call(var2[32].call(endOffset, var2[33].callGetProperty(lineInfoMap.get())), (Object)$const$0));
                  } else {
                     var2[34].call(var2[35].callGroovyObjectGetProperty(this), var2[36].call(var2[37].callGroovyObjectGetProperty(this)));
                  }
               }
            }

            return var2[38].call(var2[39].callGetProperty(var2[40].callGroovyObjectGetProperty(this)));
         }

         public Object call(TreeSelectionEvent e) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[41].callCurrent(this, (Object)e);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4()) {
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
            var0[0] = "clear";
            var0[1] = "rows";
            var0[2] = "model";
            var0[3] = "propertyTable";
            var0[4] = "lastSelectedPathComponent";
            var0[5] = "jTree";
            var0[6] = "each";
            var0[7] = "properties";
            var0[8] = "inputArea";
            var0[9] = "rootElement";
            var0[10] = "findAll";
            var0[11] = "properties";
            var0[12] = "inject";
            var0[13] = "every";
            var0[14] = "startOffset";
            var0[15] = "getElement";
            var0[16] = "rootElement";
            var0[17] = "minus";
            var0[18] = "lineNumber";
            var0[19] = "setCaretPosition";
            var0[20] = "inputArea";
            var0[21] = "minus";
            var0[22] = "plus";
            var0[23] = "columnNumber";
            var0[24] = "startOffset";
            var0[25] = "getElement";
            var0[26] = "rootElement";
            var0[27] = "minus";
            var0[28] = "lastLineNumber";
            var0[29] = "moveCaretPosition";
            var0[30] = "inputArea";
            var0[31] = "minus";
            var0[32] = "plus";
            var0[33] = "lastColumnNumber";
            var0[34] = "moveCaretPosition";
            var0[35] = "inputArea";
            var0[36] = "getCaretPosition";
            var0[37] = "inputArea";
            var0[38] = "fireTableDataChanged";
            var0[39] = "model";
            var0[40] = "propertyTable";
            var0[41] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[42];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4(), var0);
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
         private static Class $get$$class$javax$swing$tree$TreeNode() {
            Class var10000 = $class$javax$swing$tree$TreeNode;
            if (var10000 == null) {
               var10000 = $class$javax$swing$tree$TreeNode = class$("javax.swing.tree.TreeNode");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_run_closure4() {
            Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstBrowser$_run_closure4 = class$("groovy.inspect.swingui.AstBrowser$_run_closure4");
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
      }, $get$$class$javax$swing$event$TreeSelectionListener()), $get$$class$javax$swing$event$TreeSelectionListener()));
      var4[34].callCurrent(this, (Object)var4[35].callGetProperty(this.prefs));
      var4[36].call(this.frame);
      ScriptBytecodeAdapter.setProperty(var4[37].callGetProperty(this.prefs), $get$$class$groovy$inspect$swingui$AstBrowser(), this.frame, "location");
      ScriptBytecodeAdapter.setProperty(var4[38].callGetProperty(this.prefs), $get$$class$groovy$inspect$swingui$AstBrowser(), this.frame, "size");
      ScriptBytecodeAdapter.setProperty(var4[39].callGetProperty(this.prefs), $get$$class$groovy$inspect$swingui$AstBrowser(), splitterPane.get(), "dividerLocation");
      ScriptBytecodeAdapter.setProperty(var4[40].callGetProperty(this.prefs), $get$$class$groovy$inspect$swingui$AstBrowser(), mainSplitter.get(), "dividerLocation");
      var4[41].call(this.frame);
      String source = (String)ScriptBytecodeAdapter.castToType(var4[42].call(script.get()), $get$$class$java$lang$String());
      var4[43].callCurrent(this, var4[44].callGetProperty(var4[45].callGetProperty(phasePicker.get())), source);
      var4[46].callCurrent(this, this.jTree, source, var4[47].callGetProperty(var4[48].callGetProperty(phasePicker.get())));
      ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$inspect$swingui$AstBrowser(), this.jTree, "rootVisible");
      ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$inspect$swingui$AstBrowser(), this.jTree, "showsRootHandles");
   }

   public void largerFont(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[49].callCurrent(this, (Object)var2[50].call(var2[51].callGetProperty(var2[52].callGetProperty(this.decompiledSource)), (Object)$const$1));
   }

   public void smallerFont(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[53].callCurrent(this, (Object)var2[54].call(var2[55].callGetProperty(var2[56].callGetProperty(this.decompiledSource)), (Object)$const$1));
   }

   private Object updateFontSize(Object newFontSize) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareGreaterThan(newFontSize, $const$2)) {
         newFontSize = $const$2;
      } else if (ScriptBytecodeAdapter.compareLessThan(newFontSize, $const$3)) {
         newFontSize = $const$3;
      }

      ScriptBytecodeAdapter.setProperty(newFontSize, $get$$class$groovy$inspect$swingui$AstBrowser(), this.prefs, "decompiledSourceFontSize");
      ScriptBytecodeAdapter.setProperty(var2[57].callConstructor($get$$class$java$awt$Font(), var2[58].callGetProperty(var2[59].callGetProperty(this.decompiledSource)), var2[60].callGetProperty(var2[61].callGetProperty(this.decompiledSource)), newFontSize), $get$$class$groovy$inspect$swingui$AstBrowser(), this.decompiledSource, "font");
      ScriptBytecodeAdapter.setProperty(var2[62].callConstructor($get$$class$java$awt$Font(), var2[63].callGetProperty(var2[64].callGetProperty(this.decompiledSource)), var2[65].callGetProperty(var2[66].callGetProperty(this.decompiledSource)), newFontSize), $get$$class$groovy$inspect$swingui$AstBrowser(), var2[67].callGetProperty(this.jTree), "font");
      var2[68].call(var2[69].callGetProperty(this.jTree), var2[70].callGetProperty(var2[71].callGetProperty(this.jTree)));
      ScriptBytecodeAdapter.setProperty(var2[72].callConstructor($get$$class$java$awt$Font(), var2[73].callGetProperty(var2[74].callGetProperty(this.decompiledSource)), var2[75].callGetProperty(var2[76].callGetProperty(this.decompiledSource)), newFontSize), $get$$class$groovy$inspect$swingui$AstBrowser(), var2[77].callGetProperty(this.propertyTable), "font");
      ScriptBytecodeAdapter.setProperty(var2[78].callConstructor($get$$class$java$awt$Font(), var2[79].callGetProperty(var2[80].callGetProperty(this.decompiledSource)), var2[81].callGetProperty(var2[82].callGetProperty(this.decompiledSource)), newFontSize), $get$$class$groovy$inspect$swingui$AstBrowser(), this.propertyTable, "font");
      Object var10000 = var2[83].call(newFontSize, (Object)$const$1);
      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$inspect$swingui$AstBrowser(), this.propertyTable, "rowHeight");
      return var10000;
   }

   public void showAbout(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object pane = var2[84].call(this.swing);
      var2[85].call(pane, (Object)"An interactive GUI to explore AST capabilities.");
      Object dialog = var2[86].call(pane, this.frame, "About Groovy AST Browser");
      var2[87].call(dialog);
   }

   public void showScriptFreeForm(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      this.showScriptFreeForm = DefaultTypeTransformation.booleanUnbox(var2[88].callGetProperty(var2[89].callGetProperty(evt)));
   }

   public void showScriptClass(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      this.showScriptClass = DefaultTypeTransformation.booleanUnbox(var2[90].callGetProperty(var2[91].callGetProperty(evt)));
   }

   public void decompile(Object phaseId, Object source) {
      Object phaseId = new Reference(phaseId);
      Object source = new Reference(source);
      CallSite[] var5 = $getCallSiteArray();
      var5[92].call(this.decompiledSource, var5[93].call($get$$class$java$awt$Cursor(), (Object)var5[94].callGetProperty($get$$class$java$awt$Cursor())));
      ScriptBytecodeAdapter.setProperty("Loading...", $get$$class$groovy$inspect$swingui$AstBrowser(), this.decompiledSource, "text");
      var5[95].call(this.swing, (Object)(new GeneratedClosure(this, this, source, phaseId) {
         private Reference<T> source;
         private Reference<T> phaseId;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstBrowser$_decompile_closure5;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptAdapter;
         // $FF: synthetic field
         private static Class $class$java$lang$String;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.source = (Reference)source;
            this.phaseId = (Reference)phaseId;
         }

         public Object doCall(Object param1) {
            // $FF: Couldn't be decompiled
         }

         public Object getSource() {
            CallSite[] var1 = $getCallSiteArray();
            return this.source.get();
         }

         public Object getPhaseId() {
            CallSite[] var1 = $getCallSiteArray();
            return this.phaseId.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_decompile_closure5()) {
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
            var0[0] = "compileToScript";
            var0[1] = "<$constructor$>";
            var0[2] = "classLoader";
            var0[3] = "showScriptFreeForm";
            var0[4] = "showScriptClass";
            var0[5] = "doLater";
            var0[6] = "swing";
            var0[7] = "doLater";
            var0[8] = "swing";
            var0[9] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[10];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_decompile_closure5(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_decompile_closure5() {
            Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_decompile_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstBrowser$_decompile_closure5 = class$("groovy.inspect.swingui.AstBrowser$_decompile_closure5");
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptAdapter;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptAdapter = class$("groovy.inspect.swingui.AstNodeToScriptAdapter");
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
      }));
   }

   public void compile(Object jTree, String script, int compilePhase) {
      Object jTree = new Reference(jTree);
      String script = new Reference(script);
      Integer compilePhase = new Reference(DefaultTypeTransformation.box(compilePhase));
      CallSite[] var7 = $getCallSiteArray();
      var7[96].call(jTree.get(), var7[97].call($get$$class$java$awt$Cursor(), (Object)var7[98].callGetProperty($get$$class$java$awt$Cursor())));
      Object model = new Reference(var7[99].callGetProperty(jTree.get()));
      var7[100].call(this.swing, (Object)(new GeneratedClosure(this, this, model) {
         private Reference<T> model;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$javax$swing$tree$DefaultMutableTreeNode;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstBrowser$_compile_closure6;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.model = (Reference)model;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Object root = var2[0].call(this.model.get());
            var2[1].call(root);
            var2[2].call(root, var2[3].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)var2[4].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)"Loading...")));
            return var2[5].call(this.model.get(), root);
         }

         public Object getModel() {
            CallSite[] var1 = $getCallSiteArray();
            return this.model.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_compile_closure6()) {
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
            var0[0] = "getRoot";
            var0[1] = "removeAllChildren";
            var0[2] = "add";
            var0[3] = "<$constructor$>";
            var0[4] = "<$constructor$>";
            var0[5] = "reload";
            var0[6] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[7];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_compile_closure6(), var0);
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
         private static Class $get$$class$javax$swing$tree$DefaultMutableTreeNode() {
            Class var10000 = $class$javax$swing$tree$DefaultMutableTreeNode;
            if (var10000 == null) {
               var10000 = $class$javax$swing$tree$DefaultMutableTreeNode = class$("javax.swing.tree.DefaultMutableTreeNode");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_compile_closure6() {
            Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_compile_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstBrowser$_compile_closure6 = class$("groovy.inspect.swingui.AstBrowser$_compile_closure6");
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
      var7[101].call(this.swing, (Object)(new GeneratedClosure(this, this, model, compilePhase, script, jTree) {
         private Reference<T> model;
         private Reference<T> compilePhase;
         private Reference<T> script;
         private Reference<T> jTree;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$SwingTreeNodeMaker;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstBrowser$_compile_closure7;

         public {
            CallSite[] var7 = $getCallSiteArray();
            this.model = (Reference)model;
            this.compilePhase = (Reference)compilePhase;
            this.script = (Reference)script;
            this.jTree = (Reference)jTree;
         }

         public Object doCall(Object param1) {
            // $FF: Couldn't be decompiled
         }

         public Object getModel() {
            CallSite[] var1 = $getCallSiteArray();
            return this.model.get();
         }

         public int getCompilePhase() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.compilePhase.get(), $get$$class$java$lang$Integer()));
         }

         public String getScript() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.script.get(), $get$$class$java$lang$String());
         }

         public Object getjTree() {
            CallSite[] var1 = $getCallSiteArray();
            return this.jTree.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[10].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser$_compile_closure7()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "<$constructor$>";
            var0[2] = "classLoader";
            var0[3] = "showScriptFreeForm";
            var0[4] = "showScriptClass";
            var0[5] = "compile";
            var0[6] = "doLater";
            var0[7] = "swing";
            var0[8] = "doLater";
            var0[9] = "swing";
            var0[10] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[11];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser$_compile_closure7(), var0);
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
         private static Class $get$$class$java$lang$Integer() {
            Class var10000 = $class$java$lang$Integer;
            if (var10000 == null) {
               var10000 = $class$java$lang$Integer = class$("java.lang.Integer");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter() {
            Class var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter = class$("groovy.inspect.swingui.ScriptToTreeNodeAdapter");
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
         private static Class $get$$class$java$lang$String() {
            Class var10000 = $class$java$lang$String;
            if (var10000 == null) {
               var10000 = $class$java$lang$String = class$("java.lang.String");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$SwingTreeNodeMaker() {
            Class var10000 = $class$groovy$inspect$swingui$SwingTreeNodeMaker;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$SwingTreeNodeMaker = class$("groovy.inspect.swingui.SwingTreeNodeMaker");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstBrowser$_compile_closure7() {
            Class var10000 = $class$groovy$inspect$swingui$AstBrowser$_compile_closure7;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstBrowser$_compile_closure7 = class$("groovy.inspect.swingui.AstBrowser$_compile_closure7");
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
      if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowser()) {
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
      Class var10000 = $get$$class$groovy$inspect$swingui$AstBrowser();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$AstBrowser(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$AstBrowser(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public void largerFont() {
      CallSite[] var1 = $getCallSiteArray();
      var1[102].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void smallerFont() {
      CallSite[] var1 = $getCallSiteArray();
      var1[103].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
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

   public boolean getShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public boolean isShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public void setShowScriptFreeForm(boolean var1) {
      this.showScriptFreeForm = var1;
   }

   public boolean getShowScriptClass() {
      return this.showScriptClass;
   }

   public boolean isShowScriptClass() {
      return this.showScriptClass;
   }

   public void setShowScriptClass(boolean var1) {
      this.showScriptClass = var1;
   }

   public GroovyClassLoader getClassLoader() {
      return this.classLoader;
   }

   public void setClassLoader(GroovyClassLoader var1) {
      this.classLoader = var1;
   }

   public Object getPrefs() {
      return this.prefs;
   }

   public void setPrefs(Object var1) {
      this.prefs = var1;
   }

   public Object getSwing() {
      return this.swing;
   }

   public void setSwing(Object var1) {
      this.swing = var1;
   }

   public Object getFrame() {
      return this.frame;
   }

   public void setFrame(Object var1) {
      this.frame = var1;
   }

   // $FF: synthetic method
   public Object this$2$updateFontSize(Object var1) {
      return this.updateFontSize(var1);
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
      var0[0] = "<$constructor$>";
      var0[1] = "println";
      var0[2] = "<$constructor$>";
      var0[3] = "getAt";
      var0[4] = "exists";
      var0[5] = "println";
      var0[6] = "setLookAndFeel";
      var0[7] = "getSystemLookAndFeelClassName";
      var0[8] = "run";
      var0[9] = "<$constructor$>";
      var0[10] = "<$constructor$>";
      var0[11] = "path";
      var0[12] = "run";
      var0[13] = "<$constructor$>";
      var0[14] = "showScriptFreeForm";
      var0[15] = "showScriptClass";
      var0[16] = "frame";
      var0[17] = "plus";
      var0[18] = "frameLocation";
      var0[19] = "frameSize";
      var0[20] = "image";
      var0[21] = "imageIcon";
      var0[22] = "ICON_PATH";
      var0[23] = "DISPOSE_ON_CLOSE";
      var0[24] = "clear";
      var0[25] = "rows";
      var0[26] = "model";
      var0[27] = "setLeafIcon";
      var0[28] = "cellRenderer";
      var0[29] = "imageIcon";
      var0[30] = "NODE_ICON_PATH";
      var0[31] = "SINGLE_TREE_SELECTION";
      var0[32] = "selectionModel";
      var0[33] = "addTreeSelectionListener";
      var0[34] = "updateFontSize";
      var0[35] = "decompiledSourceFontSize";
      var0[36] = "pack";
      var0[37] = "frameLocation";
      var0[38] = "frameSize";
      var0[39] = "verticalDividerLocation";
      var0[40] = "horizontalDividerLocation";
      var0[41] = "show";
      var0[42] = "call";
      var0[43] = "decompile";
      var0[44] = "phaseId";
      var0[45] = "selectedItem";
      var0[46] = "compile";
      var0[47] = "phaseId";
      var0[48] = "selectedItem";
      var0[49] = "updateFontSize";
      var0[50] = "plus";
      var0[51] = "size";
      var0[52] = "font";
      var0[53] = "updateFontSize";
      var0[54] = "minus";
      var0[55] = "size";
      var0[56] = "font";
      var0[57] = "<$constructor$>";
      var0[58] = "name";
      var0[59] = "font";
      var0[60] = "style";
      var0[61] = "font";
      var0[62] = "<$constructor$>";
      var0[63] = "name";
      var0[64] = "font";
      var0[65] = "style";
      var0[66] = "font";
      var0[67] = "cellRenderer";
      var0[68] = "reload";
      var0[69] = "model";
      var0[70] = "root";
      var0[71] = "model";
      var0[72] = "<$constructor$>";
      var0[73] = "name";
      var0[74] = "font";
      var0[75] = "style";
      var0[76] = "font";
      var0[77] = "tableHeader";
      var0[78] = "<$constructor$>";
      var0[79] = "name";
      var0[80] = "font";
      var0[81] = "style";
      var0[82] = "font";
      var0[83] = "plus";
      var0[84] = "optionPane";
      var0[85] = "setMessage";
      var0[86] = "createDialog";
      var0[87] = "show";
      var0[88] = "selected";
      var0[89] = "source";
      var0[90] = "selected";
      var0[91] = "source";
      var0[92] = "setCursor";
      var0[93] = "getPredefinedCursor";
      var0[94] = "WAIT_CURSOR";
      var0[95] = "doOutside";
      var0[96] = "setCursor";
      var0[97] = "getPredefinedCursor";
      var0[98] = "WAIT_CURSOR";
      var0[99] = "model";
      var0[100] = "edt";
      var0[101] = "doOutside";
      var0[102] = "largerFont";
      var0[103] = "smallerFont";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[104];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowser(), var0);
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
   private static Class $get$$class$java$util$EventObject() {
      Class var10000 = $class$java$util$EventObject;
      if (var10000 == null) {
         var10000 = $class$java$util$EventObject = class$("java.util.EventObject");
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
   private static Class $get$$class$java$awt$Cursor() {
      Class var10000 = $class$java$awt$Cursor;
      if (var10000 == null) {
         var10000 = $class$java$awt$Cursor = class$("java.awt.Cursor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyClassLoader() {
      Class var10000 = $class$groovy$lang$GroovyClassLoader;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyClassLoader = class$("groovy.lang.GroovyClassLoader");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$tree$TreeSelectionModel() {
      Class var10000 = $class$javax$swing$tree$TreeSelectionModel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$tree$TreeSelectionModel = class$("javax.swing.tree.TreeSelectionModel");
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
   private static Class $get$$class$java$io$File() {
      Class var10000 = $class$java$io$File;
      if (var10000 == null) {
         var10000 = $class$java$io$File = class$("java.io.File");
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
   private static Class $get$$class$groovy$inspect$swingui$AstBrowserUiPreferences() {
      Class var10000 = $class$groovy$inspect$swingui$AstBrowserUiPreferences;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$AstBrowserUiPreferences = class$("groovy.inspect.swingui.AstBrowserUiPreferences");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$UIManager() {
      Class var10000 = $class$javax$swing$UIManager;
      if (var10000 == null) {
         var10000 = $class$javax$swing$UIManager = class$("javax.swing.UIManager");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$event$TreeSelectionListener() {
      Class var10000 = $class$javax$swing$event$TreeSelectionListener;
      if (var10000 == null) {
         var10000 = $class$javax$swing$event$TreeSelectionListener = class$("javax.swing.event.TreeSelectionListener");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$SwingBuilder() {
      Class var10000 = $class$groovy$swing$SwingBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$SwingBuilder = class$("groovy.swing.SwingBuilder");
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
   private static Class $get$$class$groovy$inspect$swingui$AstBrowser() {
      Class var10000 = $class$groovy$inspect$swingui$AstBrowser;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$AstBrowser = class$("groovy.inspect.swingui.AstBrowser");
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
