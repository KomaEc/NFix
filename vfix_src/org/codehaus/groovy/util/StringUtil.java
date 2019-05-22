package org.codehaus.groovy.util;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class StringUtil implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204104L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204104 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$util$StringUtil;

   public StringUtil() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static String tr(String text, String source, String replacement) {
      String source = new groovy.lang.Reference(source);
      String replacement = new groovy.lang.Reference(replacement);
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(text) && DefaultTypeTransformation.booleanUnbox(source.get()) ? Boolean.FALSE : Boolean.TRUE)) {
         return (String)ScriptBytecodeAdapter.castToType(text, $get$$class$java$lang$String());
      } else {
         source.set((String)ScriptBytecodeAdapter.castToType(var5[0].callStatic($get$$class$org$codehaus$groovy$util$StringUtil(), source.get()), $get$$class$java$lang$String()));
         replacement.set((String)ScriptBytecodeAdapter.castToType(var5[1].callStatic($get$$class$org$codehaus$groovy$util$StringUtil(), replacement.get()), $get$$class$java$lang$String()));
         replacement.set((String)ScriptBytecodeAdapter.castToType(var5[2].call(replacement.get(), var5[3].call(source.get()), var5[4].call(replacement.get(), var5[5].call(var5[6].call(replacement.get()), (Object)$const$0))), $get$$class$java$lang$String()));
         return (String)ScriptBytecodeAdapter.castToType(var5[7].call(var5[8].call(text, (Object)(new GeneratedClosure($get$$class$org$codehaus$groovy$util$StringUtil(), $get$$class$org$codehaus$groovy$util$StringUtil(), replacement, source) {
            private groovy.lang.Reference<T> replacement;
            private groovy.lang.Reference<T> source;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$util$StringUtil$_tr_closure1;
            // $FF: synthetic field
            private static Class $class$java$lang$String;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.replacement = (groovy.lang.Reference)replacement;
               this.source = (groovy.lang.Reference)source;
            }

            public Object doCall(Object original) {
               Object originalx = new groovy.lang.Reference(original);
               CallSite[] var3 = $getCallSiteArray();
               return DefaultTypeTransformation.booleanUnbox(var3[0].call(this.source.get(), originalx.get())) ? var3[1].call(this.replacement.get(), var3[2].call(this.source.get(), originalx.get())) : originalx.get();
            }

            public String getReplacement() {
               CallSite[] var1 = $getCallSiteArray();
               return (String)ScriptBytecodeAdapter.castToType(this.replacement.get(), $get$$class$java$lang$String());
            }

            public String getSource() {
               CallSite[] var1 = $getCallSiteArray();
               return (String)ScriptBytecodeAdapter.castToType(this.source.get(), $get$$class$java$lang$String());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$util$StringUtil$_tr_closure1()) {
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
               var0[0] = "contains";
               var0[1] = "getAt";
               var0[2] = "lastIndexOf";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[3];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$util$StringUtil$_tr_closure1(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$util$StringUtil$_tr_closure1() {
               Class var10000 = $class$org$codehaus$groovy$util$StringUtil$_tr_closure1;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$util$StringUtil$_tr_closure1 = class$("org.codehaus.groovy.util.StringUtil$_tr_closure1");
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
         }))), $get$$class$java$lang$String());
      }
   }

   private static String expandHyphen(String text) {
      CallSite[] var1 = $getCallSiteArray();
      return !DefaultTypeTransformation.booleanUnbox(var1[9].call(text, (Object)"-")) ? (String)ScriptBytecodeAdapter.castToType(text, $get$$class$java$lang$String()) : (String)ScriptBytecodeAdapter.castToType(var1[10].call(text, "(.)-(.)", new GeneratedClosure($get$$class$org$codehaus$groovy$util$StringUtil(), $get$$class$org$codehaus$groovy$util$StringUtil()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$util$StringUtil$_expandHyphen_closure2;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object all, Object begin, Object end) {
            Object beginx = new groovy.lang.Reference(begin);
            Object endx = new groovy.lang.Reference(end);
            CallSite[] var6 = $getCallSiteArray();
            return var6[0].call(ScriptBytecodeAdapter.createRange(beginx.get(), endx.get(), true));
         }

         public Object call(Object all, Object begin, Object end) {
            Object beginx = new groovy.lang.Reference(begin);
            Object endx = new groovy.lang.Reference(end);
            CallSite[] var6 = $getCallSiteArray();
            return var6[1].callCurrent(this, all, beginx.get(), endx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$util$StringUtil$_expandHyphen_closure2()) {
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
            var0[0] = "join";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$util$StringUtil$_expandHyphen_closure2(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$util$StringUtil$_expandHyphen_closure2() {
            Class var10000 = $class$org$codehaus$groovy$util$StringUtil$_expandHyphen_closure2;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$util$StringUtil$_expandHyphen_closure2 = class$("org.codehaus.groovy.util.StringUtil$_expandHyphen_closure2");
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
      }), $get$$class$java$lang$String());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$util$StringUtil()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$util$StringUtil();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$util$StringUtil(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$util$StringUtil(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "expandHyphen";
      var0[1] = "expandHyphen";
      var0[2] = "padRight";
      var0[3] = "size";
      var0[4] = "getAt";
      var0[5] = "minus";
      var0[6] = "size";
      var0[7] = "join";
      var0[8] = "collect";
      var0[9] = "contains";
      var0[10] = "replaceAll";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[11];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$util$StringUtil(), var0);
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
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$util$StringUtil() {
      Class var10000 = $class$org$codehaus$groovy$util$StringUtil;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$util$StringUtil = class$("org.codehaus.groovy.util.StringUtil");
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
