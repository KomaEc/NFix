package groovy.inspect.swingui;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.EventObject;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ObjectBrowser implements GroovyObject {
   private Object inspector;
   private Object swing;
   private Object frame;
   private Object fieldTable;
   private Object methodTable;
   private Object itemTable;
   private Object mapTable;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)200;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)800;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)600;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202711L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202711 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$Inspector;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$ObjectBrowser;
   // $FF: synthetic field
   private static Class $class$groovy$swing$SwingBuilder;
   // $FF: synthetic field
   private static Class $class$javax$swing$WindowConstants;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$ui$Console;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$TableSorter;

   public ObjectBrowser() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].callStatic($get$$class$groovy$inspect$swingui$ObjectBrowser(), (Object)"some String");
   }

   public static void inspect(Object objectUnderInspection) {
      CallSite[] var1 = $getCallSiteArray();
      Object browser = var1[1].callConstructor($get$$class$groovy$inspect$swingui$ObjectBrowser());
      ScriptBytecodeAdapter.setProperty(var1[2].callConstructor($get$$class$groovy$inspect$Inspector(), (Object)objectUnderInspection), $get$$class$groovy$inspect$swingui$ObjectBrowser(), browser, "inspector");
      var1[3].call(browser);
   }

   public void run() {
      CallSite[] var1 = $getCallSiteArray();
      this.swing = var1[4].callConstructor($get$$class$groovy$swing$SwingBuilder());
      this.frame = var1[5].call(this.swing, ScriptBytecodeAdapter.createMap(new Object[]{"title", "Groovy Object Browser", "location", ScriptBytecodeAdapter.createList(new Object[]{$const$0, $const$0}), "size", ScriptBytecodeAdapter.createList(new Object[]{$const$1, $const$2}), "pack", Boolean.TRUE, "show", Boolean.TRUE, "iconImage", var1[6].callGetProperty(var1[7].call(this.swing, var1[8].callGetProperty($get$$class$groovy$ui$Console()))), "defaultCloseOperation", var1[9].callGetProperty($get$$class$javax$swing$WindowConstants())}), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
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
               private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  return var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"text", "Help"}), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;

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
                           private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4_closure5;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              return var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "About", "closure", ScriptBytecodeAdapter.getMethodPointer(this.getThisObject(), "showAbout")}));
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4_closure5()) {
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
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4_closure5(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4_closure5() {
                              Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4_closure5;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4_closure5 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure2_closure4_closure5");
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
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4()) {
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
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4() {
                        Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2_closure4 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure2_closure4");
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
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2()) {
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
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2() {
                  Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure2 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure2");
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
            return var2[1].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static final Integer $const$0 = (Integer)5;
               // $FF: synthetic field
               private static final Integer $const$1 = (Integer)10;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this);
                  var2[1].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"name", "Class Info", "border", var2[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createList(new Object[]{$const$0, $const$1, $const$0, $const$1})), "constraints", var2[3].callGroovyObjectGetProperty(this)}), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$awt$FlowLayout;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure6;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"alignment", var2[1].callGetProperty($get$$class$java$awt$FlowLayout())}));
                        Object props = var2[2].callGetProperty(var2[3].callGroovyObjectGetProperty(this));
                        Object classLabel = var2[4].call("<html>", (Object)var2[5].call(props, (Object)"<br>"));
                        return var2[6].callCurrent(this, (Object)classLabel);
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure6()) {
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
                        var0[0] = "flowLayout";
                        var0[1] = "LEFT";
                        var0[2] = "classProps";
                        var0[3] = "inspector";
                        var0[4] = "plus";
                        var0[5] = "join";
                        var0[6] = "label";
                        var0[7] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[8];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure6(), var0);
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
                     private static Class $get$$class$java$awt$FlowLayout() {
                        Class var10000 = $class$java$awt$FlowLayout;
                        if (var10000 == null) {
                           var10000 = $class$java$awt$FlowLayout = class$("java.awt.FlowLayout");
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
                     private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure6() {
                        Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure6;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure6 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure6");
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
                  return var2[4].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"constraints", var2[5].callGroovyObjectGetProperty(this)}), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        if (var2[0].callGetProperty(var2[1].callGroovyObjectGetProperty(this)) instanceof Collection) {
                           var2[2].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"name", " Collection data "}), new GeneratedClosure(this, this.getThisObject()) {
                              // $FF: synthetic field
                              private static ClassInfo $staticClassInfo;
                              // $FF: synthetic field
                              private static SoftReference $callSiteArray;
                              // $FF: synthetic field
                              private static Class $class$java$lang$Object;
                              // $FF: synthetic field
                              private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8;

                              public {
                                 CallSite[] var3 = $getCallSiteArray();
                              }

                              public Object doCall(Object it) {
                                 CallSite[] var2 = $getCallSiteArray();
                                 Object var10000 = var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                                    // $FF: synthetic field
                                    private static final Integer $const$0 = (Integer)0;
                                    // $FF: synthetic field
                                    private static ClassInfo $staticClassInfo;
                                    // $FF: synthetic field
                                    private static SoftReference $callSiteArray;
                                    // $FF: synthetic field
                                    private static Class $class$java$lang$Object;
                                    // $FF: synthetic field
                                    private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12;

                                    public {
                                       CallSite[] var3 = $getCallSiteArray();
                                    }

                                    public Object doCall(Object it) {
                                       CallSite[] var2 = $getCallSiteArray();
                                       Integer i = new Reference($const$0);
                                       Object data = var2[0].call(var2[1].callGetProperty(var2[2].callGroovyObjectGetProperty(this)), (Object)(new GeneratedClosure(this, this.getThisObject(), i) {
                                          private Reference<T> i;
                                          // $FF: synthetic field
                                          private static ClassInfo $staticClassInfo;
                                          // $FF: synthetic field
                                          private static SoftReference $callSiteArray;
                                          // $FF: synthetic field
                                          private static Class $class$java$lang$Integer;
                                          // $FF: synthetic field
                                          private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure13;

                                          public {
                                             CallSite[] var4 = $getCallSiteArray();
                                             this.i = (Reference)i;
                                          }

                                          public Object doCall(Object val) {
                                             Object valx = new Reference(val);
                                             CallSite[] var3 = $getCallSiteArray();
                                             Object[] var10000 = new Object[2];
                                             Object var4 = this.i.get();
                                             this.i.set(var3[0].call(this.i.get()));
                                             var10000[0] = var4;
                                             var10000[1] = valx.get();
                                             return ScriptBytecodeAdapter.createList(var10000);
                                          }

                                          public Integer getI() {
                                             CallSite[] var1 = $getCallSiteArray();
                                             return (Integer)ScriptBytecodeAdapter.castToType(this.i.get(), $get$$class$java$lang$Integer());
                                          }

                                          // $FF: synthetic method
                                          protected MetaClass $getStaticMetaClass() {
                                             if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure13()) {
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
                                             var0[0] = "next";
                                          }

                                          // $FF: synthetic method
                                          private static CallSiteArray $createCallSiteArray() {
                                             String[] var0 = new String[1];
                                             $createCallSiteArray_1(var0);
                                             return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure13(), var0);
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
                                          private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure13() {
                                             Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure13;
                                             if (var10000 == null) {
                                                var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure13 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure13");
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
                                       return var2[3].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"list", data}), new GeneratedClosure(this, this.getThisObject()) {
                                          // $FF: synthetic field
                                          private static ClassInfo $staticClassInfo;
                                          // $FF: synthetic field
                                          private static SoftReference $callSiteArray;
                                          // $FF: synthetic field
                                          private static Class $class$java$lang$Object;
                                          // $FF: synthetic field
                                          private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14;

                                          public {
                                             CallSite[] var3 = $getCallSiteArray();
                                          }

                                          public Object doCall(Object it) {
                                             CallSite[] var2 = $getCallSiteArray();
                                             var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Index", "read", new GeneratedClosure(this, this.getThisObject()) {
                                                // $FF: synthetic field
                                                private static final Integer $const$0 = (Integer)0;
                                                // $FF: synthetic field
                                                private static ClassInfo $staticClassInfo;
                                                // $FF: synthetic field
                                                private static SoftReference $callSiteArray;
                                                // $FF: synthetic field
                                                private static Class $class$java$lang$Object;
                                                // $FF: synthetic field
                                                private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure15;

                                                public {
                                                   CallSite[] var3 = $getCallSiteArray();
                                                }

                                                public Object doCall(Object it) {
                                                   Object itx = new Reference(it);
                                                   CallSite[] var3 = $getCallSiteArray();
                                                   return var3[0].call(itx.get(), (Object)$const$0);
                                                }

                                                public Object doCall() {
                                                   CallSite[] var1 = $getCallSiteArray();
                                                   return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                                }

                                                // $FF: synthetic method
                                                protected MetaClass $getStaticMetaClass() {
                                                   if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure15()) {
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
                                                   return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure15(), var0);
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
                                                private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure15() {
                                                   Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure15;
                                                   if (var10000 == null) {
                                                      var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure15 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure15");
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
                                             }}));
                                             return var2[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Value", "read", new GeneratedClosure(this, this.getThisObject()) {
                                                // $FF: synthetic field
                                                private static final Integer $const$0 = (Integer)1;
                                                // $FF: synthetic field
                                                private static ClassInfo $staticClassInfo;
                                                // $FF: synthetic field
                                                private static SoftReference $callSiteArray;
                                                // $FF: synthetic field
                                                private static Class $class$java$lang$Object;
                                                // $FF: synthetic field
                                                private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure16;

                                                public {
                                                   CallSite[] var3 = $getCallSiteArray();
                                                }

                                                public Object doCall(Object it) {
                                                   Object itx = new Reference(it);
                                                   CallSite[] var3 = $getCallSiteArray();
                                                   return var3[0].call(itx.get(), (Object)$const$0);
                                                }

                                                public Object doCall() {
                                                   CallSite[] var1 = $getCallSiteArray();
                                                   return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                                }

                                                // $FF: synthetic method
                                                protected MetaClass $getStaticMetaClass() {
                                                   if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure16()) {
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
                                                   return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure16(), var0);
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
                                                private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure16() {
                                                   Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure16;
                                                   if (var10000 == null) {
                                                      var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure16 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14_closure16");
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
                                             }}));
                                          }

                                          public Object doCall() {
                                             CallSite[] var1 = $getCallSiteArray();
                                             return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                          }

                                          // $FF: synthetic method
                                          protected MetaClass $getStaticMetaClass() {
                                             if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14()) {
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
                                             var0[0] = "closureColumn";
                                             var0[1] = "closureColumn";
                                             var0[2] = "doCall";
                                          }

                                          // $FF: synthetic method
                                          private static CallSiteArray $createCallSiteArray() {
                                             String[] var0 = new String[3];
                                             $createCallSiteArray_1(var0);
                                             return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14(), var0);
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
                                          private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14() {
                                             Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14;
                                             if (var10000 == null) {
                                                var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12_closure14");
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
                                       if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12()) {
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
                                       var0[0] = "collect";
                                       var0[1] = "object";
                                       var0[2] = "inspector";
                                       var0[3] = "tableModel";
                                       var0[4] = "doCall";
                                    }

                                    // $FF: synthetic method
                                    private static CallSiteArray $createCallSiteArray() {
                                       String[] var0 = new String[5];
                                       $createCallSiteArray_1(var0);
                                       return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12(), var0);
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
                                    private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12() {
                                       Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12;
                                       if (var10000 == null) {
                                          var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure8_closure12");
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
                                 ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8(), this, "itemTable");
                                 return var10000;
                              }

                              public Object doCall() {
                                 CallSite[] var1 = $getCallSiteArray();
                                 return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                              }

                              // $FF: synthetic method
                              protected MetaClass $getStaticMetaClass() {
                                 if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8()) {
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
                                 return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8(), var0);
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
                              private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8() {
                                 Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8;
                                 if (var10000 == null) {
                                    var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure8 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure8");
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

                        if (var2[3].callGetProperty(var2[4].callGroovyObjectGetProperty(this)) instanceof Map) {
                           var2[5].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"name", " Map data "}), new GeneratedClosure(this, this.getThisObject()) {
                              // $FF: synthetic field
                              private static ClassInfo $staticClassInfo;
                              // $FF: synthetic field
                              private static SoftReference $callSiteArray;
                              // $FF: synthetic field
                              private static Class $class$java$lang$Object;
                              // $FF: synthetic field
                              private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9;

                              public {
                                 CallSite[] var3 = $getCallSiteArray();
                              }

                              public Object doCall(Object it) {
                                 CallSite[] var2 = $getCallSiteArray();
                                 Object var10000 = var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                                    // $FF: synthetic field
                                    private static final Integer $const$0 = (Integer)0;
                                    // $FF: synthetic field
                                    private static ClassInfo $staticClassInfo;
                                    // $FF: synthetic field
                                    private static SoftReference $callSiteArray;
                                    // $FF: synthetic field
                                    private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17;
                                    // $FF: synthetic field
                                    private static Class $class$java$lang$Object;

                                    public {
                                       CallSite[] var3 = $getCallSiteArray();
                                    }

                                    public Object doCall(Object it) {
                                       CallSite[] var2 = $getCallSiteArray();
                                       Integer i = new Reference($const$0);
                                       Object data = var2[0].call(var2[1].callGetProperty(var2[2].callGroovyObjectGetProperty(this)), (Object)(new GeneratedClosure(this, this.getThisObject(), i) {
                                          private Reference<T> i;
                                          // $FF: synthetic field
                                          private static ClassInfo $staticClassInfo;
                                          // $FF: synthetic field
                                          private static SoftReference $callSiteArray;
                                          // $FF: synthetic field
                                          private static Class $class$java$lang$Integer;
                                          // $FF: synthetic field
                                          private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure18;

                                          public {
                                             CallSite[] var4 = $getCallSiteArray();
                                             this.i = (Reference)i;
                                          }

                                          public Object doCall(Object key, Object val) {
                                             Object keyx = new Reference(key);
                                             Object valx = new Reference(val);
                                             CallSite[] var5 = $getCallSiteArray();
                                             Object[] var10000 = new Object[3];
                                             Object var6 = this.i.get();
                                             this.i.set(var5[0].call(this.i.get()));
                                             var10000[0] = var6;
                                             var10000[1] = keyx.get();
                                             var10000[2] = valx.get();
                                             return ScriptBytecodeAdapter.createList(var10000);
                                          }

                                          public Object call(Object key, Object val) {
                                             Object keyx = new Reference(key);
                                             Object valx = new Reference(val);
                                             CallSite[] var5 = $getCallSiteArray();
                                             return var5[1].callCurrent(this, keyx.get(), valx.get());
                                          }

                                          public Integer getI() {
                                             CallSite[] var1 = $getCallSiteArray();
                                             return (Integer)ScriptBytecodeAdapter.castToType(this.i.get(), $get$$class$java$lang$Integer());
                                          }

                                          // $FF: synthetic method
                                          protected MetaClass $getStaticMetaClass() {
                                             if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure18()) {
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
                                             var0[0] = "next";
                                             var0[1] = "doCall";
                                          }

                                          // $FF: synthetic method
                                          private static CallSiteArray $createCallSiteArray() {
                                             String[] var0 = new String[2];
                                             $createCallSiteArray_1(var0);
                                             return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure18(), var0);
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
                                          private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure18() {
                                             Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure18;
                                             if (var10000 == null) {
                                                var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure18 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure18");
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
                                       return var2[3].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"list", data}), new GeneratedClosure(this, this.getThisObject()) {
                                          // $FF: synthetic field
                                          private static ClassInfo $staticClassInfo;
                                          // $FF: synthetic field
                                          private static SoftReference $callSiteArray;
                                          // $FF: synthetic field
                                          private static Class $class$java$lang$Object;
                                          // $FF: synthetic field
                                          private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19;

                                          public {
                                             CallSite[] var3 = $getCallSiteArray();
                                          }

                                          public Object doCall(Object it) {
                                             CallSite[] var2 = $getCallSiteArray();
                                             var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Index", "read", new GeneratedClosure(this, this.getThisObject()) {
                                                // $FF: synthetic field
                                                private static final Integer $const$0 = (Integer)0;
                                                // $FF: synthetic field
                                                private static ClassInfo $staticClassInfo;
                                                // $FF: synthetic field
                                                private static SoftReference $callSiteArray;
                                                // $FF: synthetic field
                                                private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure20;
                                                // $FF: synthetic field
                                                private static Class $class$java$lang$Object;

                                                public {
                                                   CallSite[] var3 = $getCallSiteArray();
                                                }

                                                public Object doCall(Object it) {
                                                   Object itx = new Reference(it);
                                                   CallSite[] var3 = $getCallSiteArray();
                                                   return var3[0].call(itx.get(), (Object)$const$0);
                                                }

                                                public Object doCall() {
                                                   CallSite[] var1 = $getCallSiteArray();
                                                   return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                                }

                                                // $FF: synthetic method
                                                protected MetaClass $getStaticMetaClass() {
                                                   if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure20()) {
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
                                                   return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure20(), var0);
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
                                                private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure20() {
                                                   Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure20;
                                                   if (var10000 == null) {
                                                      var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure20 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure20");
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
                                             }}));
                                             var2[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Key", "read", new GeneratedClosure(this, this.getThisObject()) {
                                                // $FF: synthetic field
                                                private static final Integer $const$0 = (Integer)1;
                                                // $FF: synthetic field
                                                private static ClassInfo $staticClassInfo;
                                                // $FF: synthetic field
                                                private static SoftReference $callSiteArray;
                                                // $FF: synthetic field
                                                private static Class $class$java$lang$Object;
                                                // $FF: synthetic field
                                                private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure21;

                                                public {
                                                   CallSite[] var3 = $getCallSiteArray();
                                                }

                                                public Object doCall(Object it) {
                                                   Object itx = new Reference(it);
                                                   CallSite[] var3 = $getCallSiteArray();
                                                   return var3[0].call(itx.get(), (Object)$const$0);
                                                }

                                                public Object doCall() {
                                                   CallSite[] var1 = $getCallSiteArray();
                                                   return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                                }

                                                // $FF: synthetic method
                                                protected MetaClass $getStaticMetaClass() {
                                                   if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure21()) {
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
                                                   return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure21(), var0);
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
                                                private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure21() {
                                                   Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure21;
                                                   if (var10000 == null) {
                                                      var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure21 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure21");
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
                                             }}));
                                             return var2[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Value", "read", new GeneratedClosure(this, this.getThisObject()) {
                                                // $FF: synthetic field
                                                private static final Integer $const$0 = (Integer)2;
                                                // $FF: synthetic field
                                                private static ClassInfo $staticClassInfo;
                                                // $FF: synthetic field
                                                private static SoftReference $callSiteArray;
                                                // $FF: synthetic field
                                                private static Class $class$java$lang$Object;
                                                // $FF: synthetic field
                                                private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure22;

                                                public {
                                                   CallSite[] var3 = $getCallSiteArray();
                                                }

                                                public Object doCall(Object it) {
                                                   Object itx = new Reference(it);
                                                   CallSite[] var3 = $getCallSiteArray();
                                                   return var3[0].call(itx.get(), (Object)$const$0);
                                                }

                                                public Object doCall() {
                                                   CallSite[] var1 = $getCallSiteArray();
                                                   return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                                }

                                                // $FF: synthetic method
                                                protected MetaClass $getStaticMetaClass() {
                                                   if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure22()) {
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
                                                   return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure22(), var0);
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
                                                private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure22() {
                                                   Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure22;
                                                   if (var10000 == null) {
                                                      var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure22 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19_closure22");
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
                                             }}));
                                          }

                                          public Object doCall() {
                                             CallSite[] var1 = $getCallSiteArray();
                                             return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                          }

                                          // $FF: synthetic method
                                          protected MetaClass $getStaticMetaClass() {
                                             if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19()) {
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
                                             var0[0] = "closureColumn";
                                             var0[1] = "closureColumn";
                                             var0[2] = "closureColumn";
                                             var0[3] = "doCall";
                                          }

                                          // $FF: synthetic method
                                          private static CallSiteArray $createCallSiteArray() {
                                             String[] var0 = new String[4];
                                             $createCallSiteArray_1(var0);
                                             return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19(), var0);
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
                                          private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19() {
                                             Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19;
                                             if (var10000 == null) {
                                                var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17_closure19");
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
                                       if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17()) {
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
                                       var0[0] = "collect";
                                       var0[1] = "object";
                                       var0[2] = "inspector";
                                       var0[3] = "tableModel";
                                       var0[4] = "doCall";
                                    }

                                    // $FF: synthetic method
                                    private static CallSiteArray $createCallSiteArray() {
                                       String[] var0 = new String[5];
                                       $createCallSiteArray_1(var0);
                                       return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17(), var0);
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
                                    private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17() {
                                       Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17;
                                       if (var10000 == null) {
                                          var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure9_closure17");
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
                                 ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9(), this, "itemTable");
                                 return var10000;
                              }

                              public Object doCall() {
                                 CallSite[] var1 = $getCallSiteArray();
                                 return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                              }

                              // $FF: synthetic method
                              protected MetaClass $getStaticMetaClass() {
                                 if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9()) {
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
                                 return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9(), var0);
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
                              private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9() {
                                 Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9;
                                 if (var10000 == null) {
                                    var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure9 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure9");
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

                        var2[6].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"name", " Public Fields and Properties "}), new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10;

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
                                 private static Class $class$groovy$inspect$Inspector;
                                 // $FF: synthetic field
                                 private static Class $class$java$lang$Object;
                                 // $FF: synthetic field
                                 private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23;

                                 public {
                                    CallSite[] var3 = $getCallSiteArray();
                                 }

                                 public Object doCall(Object it) {
                                    CallSite[] var2 = $getCallSiteArray();
                                    Object data = var2[0].call($get$$class$groovy$inspect$Inspector(), (Object)var2[1].call(var2[2].callGetProperty(var2[3].callGroovyObjectGetProperty(this))));
                                    var2[4].call(data, var2[5].call($get$$class$groovy$inspect$Inspector(), (Object)var2[6].call(var2[7].callGetProperty(var2[8].callGroovyObjectGetProperty(this)))));
                                    return var2[9].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"list", data}), new GeneratedClosure(this, this.getThisObject()) {
                                       // $FF: synthetic field
                                       private static ClassInfo $staticClassInfo;
                                       // $FF: synthetic field
                                       private static SoftReference $callSiteArray;
                                       // $FF: synthetic field
                                       private static Class $class$java$lang$Object;
                                       // $FF: synthetic field
                                       private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24;

                                       public {
                                          CallSite[] var3 = $getCallSiteArray();
                                       }

                                       public Object doCall(Object it) {
                                          CallSite[] var2 = $getCallSiteArray();
                                          var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Name", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure25;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure25()) {
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
                                                var0[1] = "MEMBER_NAME_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure25(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure25() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure25;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure25 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure25");
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
                                          }}));
                                          var2[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Value", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure26;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure26()) {
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
                                                var0[1] = "MEMBER_VALUE_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure26(), var0);
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure26() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure26;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure26 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure26");
                                                }

                                                return var10000;
                                             }

                                             // $FF: synthetic method
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                          }}));
                                          var2[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Type", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure27;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure27()) {
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
                                                var0[1] = "MEMBER_TYPE_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure27(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
                                                }

                                                return var10000;
                                             }

                                             // $FF: synthetic method
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure27() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure27;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure27 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure27");
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
                                          }}));
                                          var2[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Origin", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure28;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure28()) {
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
                                                var0[1] = "MEMBER_ORIGIN_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure28(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
                                                }

                                                return var10000;
                                             }

                                             // $FF: synthetic method
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure28() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure28;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure28 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure28");
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
                                          }}));
                                          var2[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Modifier", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure29;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure29()) {
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
                                                var0[1] = "MEMBER_MODIFIER_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure29(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
                                                }

                                                return var10000;
                                             }

                                             // $FF: synthetic method
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure29() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure29;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure29 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure29");
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
                                          }}));
                                          return var2[5].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Declarer", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure30;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure30()) {
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
                                                var0[1] = "MEMBER_DECLARER_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure30(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure30() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure30;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure30 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24_closure30");
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
                                          }}));
                                       }

                                       public Object doCall() {
                                          CallSite[] var1 = $getCallSiteArray();
                                          return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                       }

                                       // $FF: synthetic method
                                       protected MetaClass $getStaticMetaClass() {
                                          if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24()) {
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
                                          var0[0] = "closureColumn";
                                          var0[1] = "closureColumn";
                                          var0[2] = "closureColumn";
                                          var0[3] = "closureColumn";
                                          var0[4] = "closureColumn";
                                          var0[5] = "closureColumn";
                                          var0[6] = "doCall";
                                       }

                                       // $FF: synthetic method
                                       private static CallSiteArray $createCallSiteArray() {
                                          String[] var0 = new String[7];
                                          $createCallSiteArray_1(var0);
                                          return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24(), var0);
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
                                       private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24() {
                                          Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24;
                                          if (var10000 == null) {
                                             var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23_closure24");
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
                                    return var1[10].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                 }

                                 // $FF: synthetic method
                                 protected MetaClass $getStaticMetaClass() {
                                    if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23()) {
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
                                    var0[0] = "sort";
                                    var0[1] = "toList";
                                    var0[2] = "publicFields";
                                    var0[3] = "inspector";
                                    var0[4] = "addAll";
                                    var0[5] = "sort";
                                    var0[6] = "toList";
                                    var0[7] = "propertyInfo";
                                    var0[8] = "inspector";
                                    var0[9] = "tableModel";
                                    var0[10] = "doCall";
                                 }

                                 // $FF: synthetic method
                                 private static CallSiteArray $createCallSiteArray() {
                                    String[] var0 = new String[11];
                                    $createCallSiteArray_1(var0);
                                    return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23(), var0);
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
                                 private static Class $get$$class$groovy$inspect$Inspector() {
                                    Class var10000 = $class$groovy$inspect$Inspector;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                 private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23() {
                                    Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10_closure23");
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
                              ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10(), this, "fieldTable");
                              return var10000;
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10()) {
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
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10() {
                              Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure10 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure10");
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
                        return var2[7].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"name", " (Meta) Methods "}), new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11;

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
                                 private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31;
                                 // $FF: synthetic field
                                 private static Class $class$groovy$inspect$Inspector;
                                 // $FF: synthetic field
                                 private static Class $class$java$lang$Object;

                                 public {
                                    CallSite[] var3 = $getCallSiteArray();
                                 }

                                 public Object doCall(Object it) {
                                    CallSite[] var2 = $getCallSiteArray();
                                    Object data = var2[0].call($get$$class$groovy$inspect$Inspector(), (Object)var2[1].call(var2[2].callGetProperty(var2[3].callGroovyObjectGetProperty(this))));
                                    var2[4].call(data, var2[5].call($get$$class$groovy$inspect$Inspector(), (Object)var2[6].call(var2[7].callGetProperty(var2[8].callGroovyObjectGetProperty(this)))));
                                    return var2[9].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"list", data}), new GeneratedClosure(this, this.getThisObject()) {
                                       // $FF: synthetic field
                                       private static ClassInfo $staticClassInfo;
                                       // $FF: synthetic field
                                       private static SoftReference $callSiteArray;
                                       // $FF: synthetic field
                                       private static Class $class$java$lang$Object;
                                       // $FF: synthetic field
                                       private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32;

                                       public {
                                          CallSite[] var3 = $getCallSiteArray();
                                       }

                                       public Object doCall(Object it) {
                                          CallSite[] var2 = $getCallSiteArray();
                                          var2[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Name", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure33;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure33()) {
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
                                                var0[1] = "MEMBER_NAME_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure33(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
                                                }

                                                return var10000;
                                             }

                                             // $FF: synthetic method
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure33() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure33;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure33 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure33");
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
                                          }}));
                                          var2[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Params", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure34;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure34()) {
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
                                                var0[1] = "MEMBER_PARAMS_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure34(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure34() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure34;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure34 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure34");
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
                                          }}));
                                          var2[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Type", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure35;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure35()) {
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
                                                var0[1] = "MEMBER_TYPE_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure35(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure35() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure35;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure35 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure35");
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
                                          }}));
                                          var2[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Origin", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure36;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure36()) {
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
                                                var0[1] = "MEMBER_ORIGIN_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure36(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure36() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure36;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure36 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure36");
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
                                          }}));
                                          var2[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Modifier", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure37;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure37()) {
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
                                                var0[1] = "MEMBER_MODIFIER_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure37(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure37() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure37;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure37 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure37");
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
                                          }}));
                                          var2[5].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Declarer", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure38;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure38()) {
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
                                                var0[1] = "MEMBER_DECLARER_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure38(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure38() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure38;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure38 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure38");
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
                                          }}));
                                          return var2[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"header", "Exceptions", "read", new GeneratedClosure(this, this.getThisObject()) {
                                             // $FF: synthetic field
                                             private static ClassInfo $staticClassInfo;
                                             // $FF: synthetic field
                                             private static SoftReference $callSiteArray;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$Inspector;
                                             // $FF: synthetic field
                                             private static Class $class$java$lang$Object;
                                             // $FF: synthetic field
                                             private static Class $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure39;

                                             public {
                                                CallSite[] var3 = $getCallSiteArray();
                                             }

                                             public Object doCall(Object it) {
                                                Object itx = new Reference(it);
                                                CallSite[] var3 = $getCallSiteArray();
                                                return var3[0].call(itx.get(), var3[1].callGetProperty($get$$class$groovy$inspect$Inspector()));
                                             }

                                             public Object doCall() {
                                                CallSite[] var1 = $getCallSiteArray();
                                                return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                             }

                                             // $FF: synthetic method
                                             protected MetaClass $getStaticMetaClass() {
                                                if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure39()) {
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
                                                var0[1] = "MEMBER_EXCEPTIONS_IDX";
                                                var0[2] = "doCall";
                                             }

                                             // $FF: synthetic method
                                             private static CallSiteArray $createCallSiteArray() {
                                                String[] var0 = new String[3];
                                                $createCallSiteArray_1(var0);
                                                return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure39(), var0);
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
                                             private static Class $get$$class$groovy$inspect$Inspector() {
                                                Class var10000 = $class$groovy$inspect$Inspector;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                                             private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure39() {
                                                Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure39;
                                                if (var10000 == null) {
                                                   var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure39 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32_closure39");
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
                                          }}));
                                       }

                                       public Object doCall() {
                                          CallSite[] var1 = $getCallSiteArray();
                                          return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                       }

                                       // $FF: synthetic method
                                       protected MetaClass $getStaticMetaClass() {
                                          if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32()) {
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
                                          var0[0] = "closureColumn";
                                          var0[1] = "closureColumn";
                                          var0[2] = "closureColumn";
                                          var0[3] = "closureColumn";
                                          var0[4] = "closureColumn";
                                          var0[5] = "closureColumn";
                                          var0[6] = "closureColumn";
                                          var0[7] = "doCall";
                                       }

                                       // $FF: synthetic method
                                       private static CallSiteArray $createCallSiteArray() {
                                          String[] var0 = new String[8];
                                          $createCallSiteArray_1(var0);
                                          return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32(), var0);
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
                                       private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32() {
                                          Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32;
                                          if (var10000 == null) {
                                             var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31_closure32");
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
                                    return var1[10].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                 }

                                 // $FF: synthetic method
                                 protected MetaClass $getStaticMetaClass() {
                                    if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31()) {
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
                                    var0[0] = "sort";
                                    var0[1] = "toList";
                                    var0[2] = "methods";
                                    var0[3] = "inspector";
                                    var0[4] = "addAll";
                                    var0[5] = "sort";
                                    var0[6] = "toList";
                                    var0[7] = "metaMethods";
                                    var0[8] = "inspector";
                                    var0[9] = "tableModel";
                                    var0[10] = "doCall";
                                 }

                                 // $FF: synthetic method
                                 private static CallSiteArray $createCallSiteArray() {
                                    String[] var0 = new String[11];
                                    $createCallSiteArray_1(var0);
                                    return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31(), var0);
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
                                 private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31() {
                                    Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11_closure31");
                                    }

                                    return var10000;
                                 }

                                 // $FF: synthetic method
                                 private static Class $get$$class$groovy$inspect$Inspector() {
                                    Class var10000 = $class$groovy$inspect$Inspector;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
                              ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11(), this, "methodTable");
                              return var10000;
                           }

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11()) {
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
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11() {
                              Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7_closure11 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7_closure11");
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
                        return var1[8].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7()) {
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
                        var0[0] = "object";
                        var0[1] = "inspector";
                        var0[2] = "scrollPane";
                        var0[3] = "object";
                        var0[4] = "inspector";
                        var0[5] = "scrollPane";
                        var0[6] = "scrollPane";
                        var0[7] = "scrollPane";
                        var0[8] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[9];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7() {
                        Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3_closure7 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3_closure7");
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
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3()) {
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
                  var0[0] = "borderLayout";
                  var0[1] = "panel";
                  var0[2] = "emptyBorder";
                  var0[3] = "NORTH";
                  var0[4] = "tabbedPane";
                  var0[5] = "CENTER";
                  var0[6] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[7];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3() {
                  Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1_closure3 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1_closure3");
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
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser$_run_closure1() {
            Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$ObjectBrowser$_run_closure1 = class$("groovy.inspect.swingui.ObjectBrowser$_run_closure1");
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
      var1[10].callCurrent(this, (Object)this.itemTable);
      var1[11].callCurrent(this, (Object)this.mapTable);
      var1[12].callCurrent(this, (Object)this.fieldTable);
      var1[13].callCurrent(this, (Object)this.methodTable);
      var1[14].call(this.frame);
   }

   public void addSorter(Object table) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareNotEqual(table, (Object)null)) {
         Object sorter = var2[15].callConstructor($get$$class$groovy$inspect$swingui$TableSorter(), (Object)var2[16].callGetProperty(table));
         ScriptBytecodeAdapter.setProperty(sorter, $get$$class$groovy$inspect$swingui$ObjectBrowser(), table, "model");
         var2[17].call(sorter, table);
      }

   }

   public void showAbout(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object pane = var2[18].call(this.swing);
      var2[19].call(pane, (Object)"An interactive GUI to explore object capabilities.");
      Object dialog = var2[20].call(pane, this.frame, "About Groovy Object Browser");
      var2[21].call(dialog);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$ObjectBrowser()) {
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
      Class var10000 = $get$$class$groovy$inspect$swingui$ObjectBrowser();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$ObjectBrowser(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$ObjectBrowser(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public Object getInspector() {
      return this.inspector;
   }

   public void setInspector(Object var1) {
      this.inspector = var1;
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

   public Object getFieldTable() {
      return this.fieldTable;
   }

   public void setFieldTable(Object var1) {
      this.fieldTable = var1;
   }

   public Object getMethodTable() {
      return this.methodTable;
   }

   public void setMethodTable(Object var1) {
      this.methodTable = var1;
   }

   public Object getItemTable() {
      return this.itemTable;
   }

   public void setItemTable(Object var1) {
      this.itemTable = var1;
   }

   public Object getMapTable() {
      return this.mapTable;
   }

   public void setMapTable(Object var1) {
      this.mapTable = var1;
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
      var0[0] = "inspect";
      var0[1] = "<$constructor$>";
      var0[2] = "<$constructor$>";
      var0[3] = "run";
      var0[4] = "<$constructor$>";
      var0[5] = "frame";
      var0[6] = "image";
      var0[7] = "imageIcon";
      var0[8] = "ICON_PATH";
      var0[9] = "DISPOSE_ON_CLOSE";
      var0[10] = "addSorter";
      var0[11] = "addSorter";
      var0[12] = "addSorter";
      var0[13] = "addSorter";
      var0[14] = "toFront";
      var0[15] = "<$constructor$>";
      var0[16] = "model";
      var0[17] = "addMouseListenerToHeaderInTable";
      var0[18] = "optionPane";
      var0[19] = "setMessage";
      var0[20] = "createDialog";
      var0[21] = "show";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[22];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$ObjectBrowser(), var0);
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
   private static Class $get$$class$groovy$inspect$Inspector() {
      Class var10000 = $class$groovy$inspect$Inspector;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$Inspector = class$("groovy.inspect.Inspector");
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
   private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser() {
      Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$ObjectBrowser = class$("groovy.inspect.swingui.ObjectBrowser");
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
   private static Class $get$$class$groovy$inspect$swingui$TableSorter() {
      Class var10000 = $class$groovy$inspect$swingui$TableSorter;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$TableSorter = class$("groovy.inspect.swingui.TableSorter");
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
