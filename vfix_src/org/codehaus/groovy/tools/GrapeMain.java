package org.codehaus.groovy.tools;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import groovyjarjarcommonscli.CommandLine;
import groovyjarjarcommonscli.Options;
import java.io.File;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GrapeMain extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205456L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205456 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$CommandLine;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$OptionBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$Options;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$PosixParser;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$OptionGroup;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$GrapeMain;
   // $FF: synthetic field
   private static Class array$$class$java$lang$String;

   public GrapeMain() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public GrapeMain(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$org$codehaus$groovy$tools$GrapeMain(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectProperty(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)5;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)3;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)4;
         // $FF: synthetic field
         private static final Integer $const$3 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$4 = (Integer)2;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$grape$Grape;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object param1, Object param2) {
            // $FF: Couldn't be decompiled
         }

         public Object call(Object argx, Object cmd) {
            Object arg = new Reference(argx);
            CallSite[] var4 = $getCallSiteArray();
            return var4[14].callCurrent(this, arg.get(), cmd);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure1()) {
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
            var0[0] = "size";
            var0[1] = "size";
            var0[2] = "println";
            var0[3] = "size";
            var0[4] = "getAt";
            var0[5] = "size";
            var0[6] = "getAt";
            var0[7] = "getInstance";
            var0[8] = "setupLogging";
            var0[9] = "grab";
            var0[10] = "getAt";
            var0[11] = "getAt";
            var0[12] = "println";
            var0[13] = "ex";
            var0[14] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[15];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure1(), var0);
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
         private static Class $get$$class$groovy$grape$Grape() {
            Class var10000 = $class$groovy$grape$Grape;
            if (var10000 == null) {
               var10000 = $class$groovy$grape$Grape = class$("groovy.grape.Grape");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure1() {
            Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure1 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure1");
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
      }, $get$$class$org$codehaus$groovy$tools$GrapeMain(), this, "install");
      ScriptBytecodeAdapter.setGroovyObjectProperty(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$grape$Grape;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object arg, Object cmd) {
            CallSite[] var3 = $getCallSiteArray();
            var3[0].callCurrent(this, (Object)"");
            Integer moduleCount = new Reference($const$0);
            Integer versionCount = new Reference($const$0);
            var3[1].call($get$$class$groovy$grape$Grape());
            var3[2].callCurrent(this);
            var3[3].call(var3[4].call($get$$class$groovy$grape$Grape()), (Object)(new GeneratedClosure(this, this.getThisObject(), versionCount, moduleCount) {
               private Reference<T> versionCount;
               private Reference<T> moduleCount;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Integer;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.versionCount = (Reference)versionCount;
                  this.moduleCount = (Reference)moduleCount;
               }

               public Object doCall(String groupName, Map group) {
                  String groupNamex = new Reference(groupName);
                  Map groupx = new Reference(group);
                  CallSite[] var5 = $getCallSiteArray();
                  CallSite var10000 = var5[0];
                  Object var10001 = groupx.get();
                  Object var10005 = this.getThisObject();
                  Reference versionCount = this.versionCount;
                  Reference moduleCount = this.moduleCount;
                  return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, groupNamex, versionCount, moduleCount) {
                     private Reference<T> groupName;
                     private Reference<T> versionCount;
                     private Reference<T> moduleCount;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Integer;
                     // $FF: synthetic field
                     private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7_closure8;
                     // $FF: synthetic field
                     private static Class $class$java$lang$String;

                     public {
                        Reference versionCount = new Reference(versionCountx);
                        Reference moduleCount = new Reference(moduleCountx);
                        CallSite[] var8 = $getCallSiteArray();
                        this.groupName = (Reference)groupName;
                        this.versionCount = (Reference)((Reference)versionCount.get());
                        this.moduleCount = (Reference)((Reference)moduleCount.get());
                     }

                     public Object doCall(String moduleName, List<String> versions) {
                        String moduleNamex = new Reference(moduleName);
                        List versionsx = new Reference(versions);
                        CallSite[] var5 = $getCallSiteArray();
                        var5[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{this.groupName.get(), moduleNamex.get(), versionsx.get()}, new String[]{"", " ", "  ", ""})));
                        Object var6 = this.moduleCount.get();
                        this.moduleCount.set(var5[1].call(this.moduleCount.get()));
                        Object var10000 = var5[2].call(this.versionCount.get(), var5[3].call(versionsx.get()));
                        this.versionCount.set((Integer)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$Integer()));
                        return var10000;
                     }

                     public Object call(String moduleName, List<String> versions) {
                        String moduleNamex = new Reference(moduleName);
                        List versionsx = new Reference(versions);
                        CallSite[] var5 = $getCallSiteArray();
                        return var5[4].callCurrent(this, moduleNamex.get(), versionsx.get());
                     }

                     public String getGroupName() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (String)ScriptBytecodeAdapter.castToType(this.groupName.get(), $get$$class$java$lang$String());
                     }

                     public Integer getVersionCount() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (Integer)ScriptBytecodeAdapter.castToType(this.versionCount.get(), $get$$class$java$lang$Integer());
                     }

                     public Integer getModuleCount() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (Integer)ScriptBytecodeAdapter.castToType(this.moduleCount.get(), $get$$class$java$lang$Integer());
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7_closure8()) {
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
                        var0[0] = "println";
                        var0[1] = "next";
                        var0[2] = "plus";
                        var0[3] = "size";
                        var0[4] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[5];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7_closure8(), var0);
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
                     private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7_closure8() {
                        Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7_closure8;
                        if (var10000 == null) {
                           var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7_closure8 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure2_closure7_closure8");
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

               public Object call(String groupName, Map group) {
                  String groupNamex = new Reference(groupName);
                  Map groupx = new Reference(group);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[1].callCurrent(this, groupNamex.get(), groupx.get());
               }

               public Integer getVersionCount() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Integer)ScriptBytecodeAdapter.castToType(this.versionCount.get(), $get$$class$java$lang$Integer());
               }

               public Integer getModuleCount() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Integer)ScriptBytecodeAdapter.castToType(this.moduleCount.get(), $get$$class$java$lang$Integer());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7()) {
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
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7() {
                  Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2_closure7 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure2_closure7");
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
            var3[5].callCurrent(this, (Object)"");
            var3[6].callCurrent(this, (Object)(new GStringImpl(new Object[]{moduleCount.get()}, new String[]{"", " Grape modules cached"})));
            return var3[7].callCurrent(this, (Object)(new GStringImpl(new Object[]{versionCount.get()}, new String[]{"", " Grape module versions cached"})));
         }

         public Object call(Object arg, Object cmd) {
            CallSite[] var3 = $getCallSiteArray();
            return var3[8].callCurrent(this, arg, cmd);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2()) {
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
            var0[0] = "println";
            var0[1] = "getInstance";
            var0[2] = "setupLogging";
            var0[3] = "each";
            var0[4] = "enumerateGrapes";
            var0[5] = "println";
            var0[6] = "println";
            var0[7] = "println";
            var0[8] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[9];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2(), var0);
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
         private static Class $get$$class$groovy$grape$Grape() {
            Class var10000 = $class$groovy$grape$Grape;
            if (var10000 == null) {
               var10000 = $class$groovy$grape$Grape = class$("groovy.grape.Grape");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure2() {
            Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure2 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure2");
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
      }, $get$$class$org$codehaus$groovy$tools$GrapeMain(), this, "list");
      ScriptBytecodeAdapter.setGroovyObjectProperty(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)-1;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)3;
         // $FF: synthetic field
         private static final Integer $const$3 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure3;
         // $FF: synthetic field
         private static Class $class$org$apache$commons$cli$CommandLine;
         // $FF: synthetic field
         private static Class $class$groovy$grape$Grape;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$util$Message;
         // $FF: synthetic field
         private static Class $class$org$apache$commons$cli$OptionBuilder;
         // $FF: synthetic field
         private static Class $class$org$apache$commons$cli$Options;
         // $FF: synthetic field
         private static Class $class$java$io$File;
         // $FF: synthetic field
         private static Class $class$org$apache$commons$cli$PosixParser;
         // $FF: synthetic field
         private static Class array$$class$java$lang$String;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object param1, Object param2) {
            // $FF: Couldn't be decompiled
         }

         public Object call(Object argx, Object cmd) {
            Object arg = new Reference(argx);
            CallSite[] var4 = $getCallSiteArray();
            return var4[61].callCurrent(this, arg.get(), cmd);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure3()) {
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
            var0[1] = "addOption";
            var0[2] = "create";
            var0[3] = "withLongOpt";
            var0[4] = "hasArg";
            var0[5] = "addOption";
            var0[6] = "create";
            var0[7] = "withLongOpt";
            var0[8] = "hasArg";
            var0[9] = "addOption";
            var0[10] = "create";
            var0[11] = "withLongOpt";
            var0[12] = "hasArg";
            var0[13] = "addOption";
            var0[14] = "create";
            var0[15] = "withLongOpt";
            var0[16] = "hasArg";
            var0[17] = "parse";
            var0[18] = "<$constructor$>";
            var0[19] = "getAt";
            var0[20] = "args";
            var0[21] = "getInstance";
            var0[22] = "setupLogging";
            var0[23] = "MSG_ERR";
            var0[24] = "mod";
            var0[25] = "size";
            var0[26] = "println";
            var0[27] = "size";
            var0[28] = "args";
            var0[29] = "println";
            var0[30] = "hasOption";
            var0[31] = "hasOption";
            var0[32] = "hasOption";
            var0[33] = "hasOption";
            var0[34] = "iterator";
            var0[35] = "leftShift";
            var0[36] = "hasNext";
            var0[37] = "iter";
            var0[38] = "add";
            var0[39] = "next";
            var0[40] = "iter";
            var0[41] = "next";
            var0[42] = "iter";
            var0[43] = "next";
            var0[44] = "iter";
            var0[45] = "resolve";
            var0[46] = "iterator";
            var0[47] = "scheme";
            var0[48] = "plus";
            var0[49] = "path";
            var0[50] = "<$constructor$>";
            var0[51] = "plus";
            var0[52] = "toASCIIString";
            var0[53] = "each";
            var0[54] = "println";
            var0[55] = "join";
            var0[56] = "println";
            var0[57] = "println";
            var0[58] = "message";
            var0[59] = "message";
            var0[60] = "println";
            var0[61] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[62];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure3(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure3() {
            Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure3;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure3 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure3");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$commons$cli$CommandLine() {
            Class var10000 = $class$org$apache$commons$cli$CommandLine;
            if (var10000 == null) {
               var10000 = $class$org$apache$commons$cli$CommandLine = class$("groovyjarjarcommonscli.CommandLine");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$grape$Grape() {
            Class var10000 = $class$groovy$grape$Grape;
            if (var10000 == null) {
               var10000 = $class$groovy$grape$Grape = class$("groovy.grape.Grape");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$ivy$util$Message() {
            Class var10000 = $class$org$apache$ivy$util$Message;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$util$Message = class$("org.apache.ivy.util.Message");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$commons$cli$OptionBuilder() {
            Class var10000 = $class$org$apache$commons$cli$OptionBuilder;
            if (var10000 == null) {
               var10000 = $class$org$apache$commons$cli$OptionBuilder = class$("groovyjarjarcommonscli.OptionBuilder");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$commons$cli$Options() {
            Class var10000 = $class$org$apache$commons$cli$Options;
            if (var10000 == null) {
               var10000 = $class$org$apache$commons$cli$Options = class$("groovyjarjarcommonscli.Options");
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
         private static Class $get$$class$org$apache$commons$cli$PosixParser() {
            Class var10000 = $class$org$apache$commons$cli$PosixParser;
            if (var10000 == null) {
               var10000 = $class$org$apache$commons$cli$PosixParser = class$("groovyjarjarcommonscli.PosixParser");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$array$$class$java$lang$String() {
            Class var10000 = array$$class$java$lang$String;
            if (var10000 == null) {
               var10000 = array$$class$java$lang$String = class$("[Ljava.lang.String;");
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
      }, $get$$class$org$codehaus$groovy$tools$GrapeMain(), this, "resolve");
      Object commands = new Reference(ScriptBytecodeAdapter.createMap(new Object[]{"install", ScriptBytecodeAdapter.createMap(new Object[]{"closure", var1[1].callGroovyObjectGetProperty(this), "shortHelp", "Installs a particular grape"}), "list", ScriptBytecodeAdapter.createMap(new Object[]{"closure", var1[2].callGroovyObjectGetProperty(this), "shortHelp", "Lists all installed grapes"}), "resolve", ScriptBytecodeAdapter.createMap(new Object[]{"closure", var1[3].callGroovyObjectGetProperty(this), "shortHelp", "Enumerates the jars used by a grape"})}));
      Options options = new Reference(var1[4].callConstructor($get$$class$org$apache$commons$cli$Options()));
      var1[5].call(options.get(), var1[6].call(var1[7].call(var1[8].call(var1[9].call(var1[10].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)"define"), (Object)"define a system property"), (Object)Boolean.TRUE), (Object)"name=value"), (Object)"D"));
      var1[11].call(options.get(), var1[12].call(var1[13].call(var1[14].call(var1[15].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)Boolean.FALSE), (Object)"usage information"), (Object)"help"), (Object)"h"));
      var1[16].call(options.get(), var1[17].call(var1[18].call(var1[19].call(var1[20].call(var1[21].call(var1[22].callConstructor($get$$class$org$apache$commons$cli$OptionGroup()), var1[23].call(var1[24].call(var1[25].call(var1[26].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)Boolean.FALSE), (Object)"Log level 0 - only errors"), (Object)"quiet"), (Object)"q")), var1[27].call(var1[28].call(var1[29].call(var1[30].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)Boolean.FALSE), (Object)"Log level 1 - errors and warnings"), (Object)"warn"), (Object)"w")), var1[31].call(var1[32].call(var1[33].call(var1[34].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)Boolean.FALSE), (Object)"Log level 2 - info"), (Object)"info"), (Object)"i")), var1[35].call(var1[36].call(var1[37].call(var1[38].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)Boolean.FALSE), (Object)"Log level 3 - verbose"), (Object)"verbose"), (Object)"V")), var1[39].call(var1[40].call(var1[41].call(var1[42].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)Boolean.FALSE), (Object)"Log level 4 - debug"), (Object)"debug"), (Object)"d")));
      var1[43].call(options.get(), var1[44].call(var1[45].call(var1[46].call(var1[47].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)Boolean.FALSE), (Object)"display the Groovy and JVM versions"), (Object)"version"), (Object)"v"));
      CommandLine cmd = new Reference((CommandLine)ScriptBytecodeAdapter.castToType(var1[48].call(var1[49].callConstructor($get$$class$org$apache$commons$cli$PosixParser()), options.get(), var1[50].callGroovyObjectGetProperty(this), Boolean.TRUE), $get$$class$org$apache$commons$cli$CommandLine()));
      ScriptBytecodeAdapter.setGroovyObjectProperty(new GeneratedClosure(this, this, commands, options) {
         private Reference<T> commands;
         private Reference<T> options;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)3;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)80;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)2;
         // $FF: synthetic field
         private static final Integer $const$3 = (Integer)4;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$lang$System;
         // $FF: synthetic field
         private static Class $class$org$apache$commons$cli$Options;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$org$apache$commons$cli$HelpFormatter;
         // $FF: synthetic field
         private static Class $class$java$io$PrintWriter;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.commands = (Reference)commands;
            this.options = (Reference)options;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Integer spacesLen = new Reference((Integer)ScriptBytecodeAdapter.castToType(var2[0].call(var2[1].call(var2[2].call(var2[3].call(this.commands.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure10;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(itx.get());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure10()) {
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
                  var0[0] = "length";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure10(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure10() {
                  Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure10;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure10 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure4_closure10");
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
            }))), (Object)$const$0), $get$$class$java$lang$Integer()));
            String spaces = new Reference((String)ScriptBytecodeAdapter.castToType(var2[4].call(" ", (Object)spacesLen.get()), $get$$class$java$lang$String()));
            CallSite var10000 = var2[5];
            Class var10001 = $get$$class$java$io$PrintWriter();
            Object var10002 = var2[6].callGetProperty(var2[7].callGetProperty(var2[8].callGroovyObjectGetProperty(this)));
            if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
               var10002 = var2[9].callGetProperty($get$$class$java$lang$System());
            }

            PrintWriter pw = var10000.callConstructor(var10001, (Object)var10002);
            var2[10].call(var2[11].callConstructor($get$$class$org$apache$commons$cli$HelpFormatter()), ArrayUtil.createArray(pw, $const$1, "grape [options] <command> [args]\n", "options:", this.options.get(), $const$2, $const$3, (Object)null, Boolean.TRUE));
            var2[12].call(pw);
            var2[13].callCurrent(this, (Object)"");
            var2[14].callCurrent(this, (Object)"commands:");
            var2[15].call(this.commands.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), spacesLen, spaces) {
               private Reference<T> spacesLen;
               private Reference<T> spaces;
               // $FF: synthetic field
               private static final Integer $const$0 = (Integer)0;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Integer;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure11;
               // $FF: synthetic field
               private static Class $class$java$lang$String;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.spacesLen = (Reference)spacesLen;
                  this.spaces = (Reference)spaces;
               }

               public Object doCall(String k, Object v) {
                  String kx = new Reference(k);
                  Object vx = new Reference(v);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{var5[1].call(var5[2].call(kx.get(), this.spaces.get()), $const$0, this.spacesLen.get()), var5[3].callGetProperty(vx.get())}, new String[]{"  ", " ", ""})));
               }

               public Object call(String k, Object v) {
                  String kx = new Reference(k);
                  Object vx = new Reference(v);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[4].callCurrent(this, kx.get(), vx.get());
               }

               public Integer getSpacesLen() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Integer)ScriptBytecodeAdapter.castToType(this.spacesLen.get(), $get$$class$java$lang$Integer());
               }

               public String getSpaces() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (String)ScriptBytecodeAdapter.castToType(this.spaces.get(), $get$$class$java$lang$String());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure11()) {
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
                  var0[0] = "println";
                  var0[1] = "substring";
                  var0[2] = "plus";
                  var0[3] = "shortHelp";
                  var0[4] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[5];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure11(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure11() {
                  Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure11;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4_closure11 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure4_closure11");
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
            return var2[16].callCurrent(this, (Object)"");
         }

         public Object getCommands() {
            CallSite[] var1 = $getCallSiteArray();
            return this.commands.get();
         }

         public Options getOptions() {
            CallSite[] var1 = $getCallSiteArray();
            return (Options)ScriptBytecodeAdapter.castToType(this.options.get(), $get$$class$org$apache$commons$cli$Options());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[17].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4()) {
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
            var0[0] = "plus";
            var0[1] = "length";
            var0[2] = "max";
            var0[3] = "keySet";
            var0[4] = "multiply";
            var0[5] = "<$constructor$>";
            var0[6] = "out";
            var0[7] = "variables";
            var0[8] = "binding";
            var0[9] = "out";
            var0[10] = "printHelp";
            var0[11] = "<$constructor$>";
            var0[12] = "flush";
            var0[13] = "println";
            var0[14] = "println";
            var0[15] = "each";
            var0[16] = "println";
            var0[17] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[18];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure4() {
            Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure4 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure4");
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
         private static Class $get$$class$java$lang$System() {
            Class var10000 = $class$java$lang$System;
            if (var10000 == null) {
               var10000 = $class$java$lang$System = class$("java.lang.System");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$commons$cli$Options() {
            Class var10000 = $class$org$apache$commons$cli$Options;
            if (var10000 == null) {
               var10000 = $class$org$apache$commons$cli$Options = class$("groovyjarjarcommonscli.Options");
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
         private static Class $get$$class$org$apache$commons$cli$HelpFormatter() {
            Class var10000 = $class$org$apache$commons$cli$HelpFormatter;
            if (var10000 == null) {
               var10000 = $class$org$apache$commons$cli$HelpFormatter = class$("groovyjarjarcommonscli.HelpFormatter");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$io$PrintWriter() {
            Class var10000 = $class$java$io$PrintWriter;
            if (var10000 == null) {
               var10000 = $class$java$io$PrintWriter = class$("java.io.PrintWriter");
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
      }, $get$$class$org$codehaus$groovy$tools$GrapeMain(), this, "grapeHelp");
      ScriptBytecodeAdapter.setGroovyObjectProperty(new GeneratedClosure(this, this, cmd) {
         private Reference<T> cmd;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)2;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$apache$commons$cli$CommandLine;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure5;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$util$DefaultMessageLogger;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$util$Message;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.cmd = (Reference)cmd;
         }

         public Object doCall(int defaultLevel) {
            Integer defaultLevelx = new Reference(DefaultTypeTransformation.box(defaultLevel));
            CallSite[] var3 = $getCallSiteArray();
            if (DefaultTypeTransformation.booleanUnbox(var3[0].call(this.cmd.get(), (Object)"q"))) {
               return var3[1].call($get$$class$org$apache$ivy$util$Message(), (Object)var3[2].callConstructor($get$$class$org$apache$ivy$util$DefaultMessageLogger(), (Object)var3[3].callGetProperty($get$$class$org$apache$ivy$util$Message())));
            } else if (DefaultTypeTransformation.booleanUnbox(var3[4].call(this.cmd.get(), (Object)"w"))) {
               return var3[5].call($get$$class$org$apache$ivy$util$Message(), (Object)var3[6].callConstructor($get$$class$org$apache$ivy$util$DefaultMessageLogger(), (Object)var3[7].callGetProperty($get$$class$org$apache$ivy$util$Message())));
            } else if (DefaultTypeTransformation.booleanUnbox(var3[8].call(this.cmd.get(), (Object)"i"))) {
               return var3[9].call($get$$class$org$apache$ivy$util$Message(), (Object)var3[10].callConstructor($get$$class$org$apache$ivy$util$DefaultMessageLogger(), (Object)var3[11].callGetProperty($get$$class$org$apache$ivy$util$Message())));
            } else if (DefaultTypeTransformation.booleanUnbox(var3[12].call(this.cmd.get(), (Object)"V"))) {
               return var3[13].call($get$$class$org$apache$ivy$util$Message(), (Object)var3[14].callConstructor($get$$class$org$apache$ivy$util$DefaultMessageLogger(), (Object)var3[15].callGetProperty($get$$class$org$apache$ivy$util$Message())));
            } else {
               return DefaultTypeTransformation.booleanUnbox(var3[16].call(this.cmd.get(), (Object)"d")) ? var3[17].call($get$$class$org$apache$ivy$util$Message(), (Object)var3[18].callConstructor($get$$class$org$apache$ivy$util$DefaultMessageLogger(), (Object)var3[19].callGetProperty($get$$class$org$apache$ivy$util$Message()))) : var3[20].call($get$$class$org$apache$ivy$util$Message(), (Object)var3[21].callConstructor($get$$class$org$apache$ivy$util$DefaultMessageLogger(), (Object)defaultLevelx.get()));
            }
         }

         public Object call(int defaultLevel) {
            Integer defaultLevelx = new Reference(DefaultTypeTransformation.box(defaultLevel));
            CallSite[] var3 = $getCallSiteArray();
            return var3[22].callCurrent(this, (Object)defaultLevelx.get());
         }

         public CommandLine getCmd() {
            CallSite[] var1 = $getCallSiteArray();
            return (CommandLine)ScriptBytecodeAdapter.castToType(this.cmd.get(), $get$$class$org$apache$commons$cli$CommandLine());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[23].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper($const$0, Integer.TYPE));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure5()) {
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
            var0[0] = "hasOption";
            var0[1] = "setDefaultLogger";
            var0[2] = "<$constructor$>";
            var0[3] = "MSG_ERR";
            var0[4] = "hasOption";
            var0[5] = "setDefaultLogger";
            var0[6] = "<$constructor$>";
            var0[7] = "MSG_WARN";
            var0[8] = "hasOption";
            var0[9] = "setDefaultLogger";
            var0[10] = "<$constructor$>";
            var0[11] = "MSG_INFO";
            var0[12] = "hasOption";
            var0[13] = "setDefaultLogger";
            var0[14] = "<$constructor$>";
            var0[15] = "MSG_VERBOSE";
            var0[16] = "hasOption";
            var0[17] = "setDefaultLogger";
            var0[18] = "<$constructor$>";
            var0[19] = "MSG_DEBUG";
            var0[20] = "setDefaultLogger";
            var0[21] = "<$constructor$>";
            var0[22] = "doCall";
            var0[23] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[24];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure5(), var0);
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
               var10000 = $class$org$apache$commons$cli$CommandLine = class$("groovyjarjarcommonscli.CommandLine");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure5() {
            Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure5;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure5 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure5");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$ivy$util$DefaultMessageLogger() {
            Class var10000 = $class$org$apache$ivy$util$DefaultMessageLogger;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$util$DefaultMessageLogger = class$("org.apache.ivy.util.DefaultMessageLogger");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$ivy$util$Message() {
            Class var10000 = $class$org$apache$ivy$util$Message;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$util$Message = class$("org.apache.ivy.util.Message");
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
      }, $get$$class$org$codehaus$groovy$tools$GrapeMain(), this, "setupLogging");
      if (DefaultTypeTransformation.booleanUnbox(var1[51].call(cmd.get(), (Object)"h"))) {
         var1[52].callCurrent(this);
         return null;
      } else if (DefaultTypeTransformation.booleanUnbox(var1[53].call(cmd.get(), (Object)"v"))) {
         String version = (String)ScriptBytecodeAdapter.castToType(var1[54].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper()), $get$$class$java$lang$String());
         var1[55].callCurrent(this, (Object)(new GStringImpl(new Object[]{version, var1[56].call($get$$class$java$lang$System(), (Object)"java.version")}, new String[]{"Groovy Version: ", " JVM: ", ""})));
         return null;
      } else {
         var1[57].callSafe(var1[58].call(cmd.get(), (Object)"D"), (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)2;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$tools$GrapeMain$_run_closure6;
            // $FF: synthetic field
            private static Class $class$java$lang$System;
            // $FF: synthetic field
            private static Class $class$java$util$List;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(String prop) {
               String propx = new Reference(prop);
               CallSite[] var3 = $getCallSiteArray();
               List var10000 = (List)ScriptBytecodeAdapter.asType(var3[0].call(propx.get(), "=", $const$0), $get$$class$java$util$List());
               Object k = var3[1].call(var10000, (int)0);
               Object v = var3[2].call(var10000, (int)1);
               CallSite var6 = var3[3];
               Class var10001 = $get$$class$java$lang$System();
               Object var10003 = v;
               if (!DefaultTypeTransformation.booleanUnbox(v)) {
                  var10003 = "";
               }

               return var6.call(var10001, k, var10003);
            }

            public Object call(String prop) {
               String propx = new Reference(prop);
               CallSite[] var3 = $getCallSiteArray();
               return var3[4].callCurrent(this, (Object)propx.get());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure6()) {
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
               var0[0] = "split";
               var0[1] = "getAt";
               var0[2] = "getAt";
               var0[3] = "setProperty";
               var0[4] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure6(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain$_run_closure6() {
               Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure6;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$tools$GrapeMain$_run_closure6 = class$("org.codehaus.groovy.tools.GrapeMain$_run_closure6");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$lang$System() {
               Class var10000 = $class$java$lang$System;
               if (var10000 == null) {
                  var10000 = $class$java$lang$System = class$("java.lang.System");
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
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         }));
         String[] arg = (String[])ScriptBytecodeAdapter.castToType(var1[59].callGetProperty(cmd.get()), $get$array$$class$java$lang$String());
         if (ScriptBytecodeAdapter.compareEqual(var1[60].callGetPropertySafe(arg), $const$0)) {
            return var1[61].callCurrent(this);
         } else {
            return DefaultTypeTransformation.booleanUnbox(var1[62].call(commands.get(), var1[63].call(arg, (Object)$const$0))) ? var1[64].call(var1[65].call(commands.get(), var1[66].call(arg, (Object)$const$0)), arg, cmd.get()) : var1[67].callCurrent(this, (Object)(new GStringImpl(new Object[]{var1[68].call(arg, (Object)$const$0)}, new String[]{"grape: '", "' is not a grape command. See 'grape --help'"})));
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$GrapeMain()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$GrapeMain();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$GrapeMain(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$GrapeMain(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[1] = "install";
      var0[2] = "list";
      var0[3] = "resolve";
      var0[4] = "<$constructor$>";
      var0[5] = "addOption";
      var0[6] = "create";
      var0[7] = "withArgName";
      var0[8] = "hasArg";
      var0[9] = "withDescription";
      var0[10] = "withLongOpt";
      var0[11] = "addOption";
      var0[12] = "create";
      var0[13] = "withLongOpt";
      var0[14] = "withDescription";
      var0[15] = "hasArg";
      var0[16] = "addOptionGroup";
      var0[17] = "addOption";
      var0[18] = "addOption";
      var0[19] = "addOption";
      var0[20] = "addOption";
      var0[21] = "addOption";
      var0[22] = "<$constructor$>";
      var0[23] = "create";
      var0[24] = "withLongOpt";
      var0[25] = "withDescription";
      var0[26] = "hasArg";
      var0[27] = "create";
      var0[28] = "withLongOpt";
      var0[29] = "withDescription";
      var0[30] = "hasArg";
      var0[31] = "create";
      var0[32] = "withLongOpt";
      var0[33] = "withDescription";
      var0[34] = "hasArg";
      var0[35] = "create";
      var0[36] = "withLongOpt";
      var0[37] = "withDescription";
      var0[38] = "hasArg";
      var0[39] = "create";
      var0[40] = "withLongOpt";
      var0[41] = "withDescription";
      var0[42] = "hasArg";
      var0[43] = "addOption";
      var0[44] = "create";
      var0[45] = "withLongOpt";
      var0[46] = "withDescription";
      var0[47] = "hasArg";
      var0[48] = "parse";
      var0[49] = "<$constructor$>";
      var0[50] = "args";
      var0[51] = "hasOption";
      var0[52] = "grapeHelp";
      var0[53] = "hasOption";
      var0[54] = "getVersion";
      var0[55] = "println";
      var0[56] = "getProperty";
      var0[57] = "each";
      var0[58] = "getOptionValues";
      var0[59] = "args";
      var0[60] = "length";
      var0[61] = "grapeHelp";
      var0[62] = "containsKey";
      var0[63] = "getAt";
      var0[64] = "closure";
      var0[65] = "getAt";
      var0[66] = "getAt";
      var0[67] = "println";
      var0[68] = "getAt";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[69];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$GrapeMain(), var0);
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
         var10000 = $class$org$apache$commons$cli$CommandLine = class$("groovyjarjarcommonscli.CommandLine");
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
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
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
   private static Class $get$$class$org$apache$commons$cli$OptionBuilder() {
      Class var10000 = $class$org$apache$commons$cli$OptionBuilder;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$OptionBuilder = class$("groovyjarjarcommonscli.OptionBuilder");
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
   private static Class $get$$class$org$apache$commons$cli$Options() {
      Class var10000 = $class$org$apache$commons$cli$Options;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$Options = class$("groovyjarjarcommonscli.Options");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$commons$cli$PosixParser() {
      Class var10000 = $class$org$apache$commons$cli$PosixParser;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$PosixParser = class$("groovyjarjarcommonscli.PosixParser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$commons$cli$OptionGroup() {
      Class var10000 = $class$org$apache$commons$cli$OptionGroup;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$OptionGroup = class$("groovyjarjarcommonscli.OptionGroup");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$GrapeMain() {
      Class var10000 = $class$org$codehaus$groovy$tools$GrapeMain;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$GrapeMain = class$("org.codehaus.groovy.tools.GrapeMain");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$java$lang$String() {
      Class var10000 = array$$class$java$lang$String;
      if (var10000 == null) {
         var10000 = array$$class$java$lang$String = class$("[Ljava.lang.String;");
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
