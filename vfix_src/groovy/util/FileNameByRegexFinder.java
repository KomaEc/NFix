package groovy.util;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class FileNameByRegexFinder implements IFileNameFinder, GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203643L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203643 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$groovy$util$FileNameByRegexFinder;

   public FileNameByRegexFinder() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public List getFileNames(String basedir, String pattern) {
      CallSite[] var3 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var3[0].callCurrent(this, basedir, pattern, ""), $get$$class$java$util$List());
   }

   public List getFileNames(String basedir, String pattern, String excludesPattern) {
      String pattern = new Reference(pattern);
      String excludesPattern = new Reference(excludesPattern);
      CallSite[] var6 = $getCallSiteArray();
      Object result = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
      var6[1].call(var6[2].callConstructor($get$$class$java$io$File(), (Object)basedir), (Object)(new GeneratedClosure(this, this, result, pattern, excludesPattern) {
         private Reference<T> result;
         private Reference<T> pattern;
         private Reference<T> excludesPattern;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$util$FileNameByRegexFinder$_getFileNames_closure1;
         // $FF: synthetic field
         private static Class $class$java$lang$String;

         public {
            CallSite[] var6 = $getCallSiteArray();
            this.result = (Reference)result;
            this.pattern = (Reference)pattern;
            this.excludesPattern = (Reference)excludesPattern;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.findRegex(var3[0].callGetProperty(itx.get()), this.pattern.get())) && DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(this.excludesPattern.get()) && DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.findRegex(var3[1].callGetProperty(itx.get()), this.excludesPattern.get())) ? Boolean.FALSE : Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE) ? var3[2].call(this.result.get(), var3[3].callGetProperty(itx.get())) : null;
         }

         public Object getResult() {
            CallSite[] var1 = $getCallSiteArray();
            return this.result.get();
         }

         public String getPattern() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.pattern.get(), $get$$class$java$lang$String());
         }

         public String getExcludesPattern() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.excludesPattern.get(), $get$$class$java$lang$String());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$FileNameByRegexFinder$_getFileNames_closure1()) {
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
            var0[0] = "path";
            var0[1] = "path";
            var0[2] = "leftShift";
            var0[3] = "absolutePath";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$FileNameByRegexFinder$_getFileNames_closure1(), var0);
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
         private static Class $get$$class$groovy$util$FileNameByRegexFinder$_getFileNames_closure1() {
            Class var10000 = $class$groovy$util$FileNameByRegexFinder$_getFileNames_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$util$FileNameByRegexFinder$_getFileNames_closure1 = class$("groovy.util.FileNameByRegexFinder$_getFileNames_closure1");
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
      return (List)ScriptBytecodeAdapter.castToType(result.get(), $get$$class$java$util$List());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$util$FileNameByRegexFinder()) {
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
      Class var10000 = $get$$class$groovy$util$FileNameByRegexFinder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$util$FileNameByRegexFinder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$util$FileNameByRegexFinder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "getFileNames";
      var0[1] = "eachFileRecurse";
      var0[2] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[3];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$util$FileNameByRegexFinder(), var0);
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
   private static Class $get$$class$java$io$File() {
      Class var10000 = $class$java$io$File;
      if (var10000 == null) {
         var10000 = $class$java$io$File = class$("java.io.File");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$FileNameByRegexFinder() {
      Class var10000 = $class$groovy$util$FileNameByRegexFinder;
      if (var10000 == null) {
         var10000 = $class$groovy$util$FileNameByRegexFinder = class$("groovy.util.FileNameByRegexFinder");
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
