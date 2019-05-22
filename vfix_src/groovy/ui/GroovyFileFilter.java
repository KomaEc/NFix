package groovy.ui;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.io.File;
import java.lang.ref.SoftReference;
import javax.swing.filechooser.FileFilter;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GroovyFileFilter extends FileFilter implements GroovyObject {
   private static final Object GROOVY_SOURCE_EXTENSIONS = ScriptBytecodeAdapter.createList(new Object[]{"*.groovy", "*.gvy", "*.gy", "*.gsh"});
   private static final Object GROOVY_SOURCE_EXT_DESC;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204582L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204582 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$ui$GroovyFileFilter;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public GroovyFileFilter() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public boolean accept(File f) {
      File f = new Reference(f);
      CallSite[] var3 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox(var3[0].call(f.get())) ? DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean())) : DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(var3[1].call(GROOVY_SOURCE_EXTENSIONS, (Object)(new GeneratedClosure(this, this, f) {
         private Reference<T> f;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$GroovyFileFilter;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$ui$GroovyFileFilter$_accept_closure1;
         // $FF: synthetic field
         private static Class $class$java$io$File;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.f = (Reference)f;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.compareEqual(itx.get(), var3[0].callStatic($get$$class$groovy$ui$GroovyFileFilter(), this.f.get())) ? Boolean.TRUE : Boolean.FALSE;
         }

         public File getF() {
            CallSite[] var1 = $getCallSiteArray();
            return (File)ScriptBytecodeAdapter.castToType(this.f.get(), $get$$class$java$io$File());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$GroovyFileFilter$_accept_closure1()) {
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
            var0[0] = "getExtension";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$GroovyFileFilter$_accept_closure1(), var0);
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
         private static Class $get$$class$groovy$ui$GroovyFileFilter() {
            Class var10000 = $class$groovy$ui$GroovyFileFilter;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$GroovyFileFilter = class$("groovy.ui.GroovyFileFilter");
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
         private static Class $get$$class$groovy$ui$GroovyFileFilter$_accept_closure1() {
            Class var10000 = $class$groovy$ui$GroovyFileFilter$_accept_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$GroovyFileFilter$_accept_closure1 = class$("groovy.ui.GroovyFileFilter$_accept_closure1");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }))) ? Boolean.TRUE : Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public String getDescription() {
      CallSite[] var1 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{GROOVY_SOURCE_EXT_DESC}, new String[]{"Groovy Source Files (", ")"}), $get$$class$java$lang$String());
   }

   public static String getExtension(Object f) {
      CallSite[] var1 = $getCallSiteArray();
      Object ext = null;
      Object s = var1[2].call(f);
      Object i = var1[3].call(s, (Object)".");
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareGreaterThan(i, $const$0) && ScriptBytecodeAdapter.compareLessThan(i, var1[4].call(var1[5].call(s), (Object)$const$1)) ? Boolean.TRUE : Boolean.FALSE)) {
         ext = var1[6].call(var1[7].call(s, i));
      }

      return (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{ext}, new String[]{"*", ""}), $get$$class$java$lang$String());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$GroovyFileFilter()) {
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
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$ui$GroovyFileFilter();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$GroovyFileFilter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$GroovyFileFilter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   static {
      GROOVY_SOURCE_EXT_DESC = $getCallSiteArray()[8].call(GROOVY_SOURCE_EXTENSIONS, (Object)",");
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
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "isDirectory";
      var0[1] = "find";
      var0[2] = "getName";
      var0[3] = "lastIndexOf";
      var0[4] = "minus";
      var0[5] = "length";
      var0[6] = "toLowerCase";
      var0[7] = "substring";
      var0[8] = "join";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[9];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$GroovyFileFilter(), var0);
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
   private static Class $get$$class$groovy$ui$GroovyFileFilter() {
      Class var10000 = $class$groovy$ui$GroovyFileFilter;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$GroovyFileFilter = class$("groovy.ui.GroovyFileFilter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
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
