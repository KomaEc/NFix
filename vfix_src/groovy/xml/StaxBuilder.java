package groovy.xml;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.BuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class StaxBuilder extends BuilderSupport {
   private Object writer;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205403L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205403 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$xml$StaxBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public StaxBuilder(Object xmlStreamWriter) {
      CallSite[] var2 = $getCallSiteArray();
      this.writer = xmlStreamWriter;
      var2[0].call(this.writer);
   }

   protected Object createNode(Object name) {
      CallSite[] var2 = $getCallSiteArray();
      return var2[1].callCurrent(this, name, (Object)null, (Object)null);
   }

   protected Object createNode(Object name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      return var3[2].callCurrent(this, name, (Object)null, value);
   }

   protected Object createNode(Object name, Map attributes) {
      CallSite[] var3 = $getCallSiteArray();
      return var3[3].callCurrent(this, name, attributes, (Object)null);
   }

   protected Object createNode(Object name, Map attributes, Object value) {
      CallSite[] var4 = $getCallSiteArray();
      var4[4].call(this.writer, var4[5].call(name));
      if (DefaultTypeTransformation.booleanUnbox(attributes)) {
         var4[6].call(attributes, (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StaxBuilder$_createNode_closure1;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object k, Object v) {
               Object kx = new Reference(k);
               Object vx = new Reference(v);
               CallSite[] var5 = $getCallSiteArray();
               return var5[0].call(var5[1].callGroovyObjectGetProperty(this), var5[2].call(kx.get()), var5[3].call(vx.get()));
            }

            public Object call(Object k, Object v) {
               Object kx = new Reference(k);
               Object vx = new Reference(v);
               CallSite[] var5 = $getCallSiteArray();
               return var5[4].callCurrent(this, kx.get(), vx.get());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StaxBuilder$_createNode_closure1()) {
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
               var0[0] = "writeAttribute";
               var0[1] = "writer";
               var0[2] = "toString";
               var0[3] = "toString";
               var0[4] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StaxBuilder$_createNode_closure1(), var0);
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
            private static Class $get$$class$groovy$xml$StaxBuilder$_createNode_closure1() {
               Class var10000 = $class$groovy$xml$StaxBuilder$_createNode_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StaxBuilder$_createNode_closure1 = class$("groovy.xml.StaxBuilder$_createNode_closure1");
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

      if (DefaultTypeTransformation.booleanUnbox(value)) {
         var4[7].call(this.writer, var4[8].call(value));
      }

      return name;
   }

   protected void nodeCompleted(Object parent, Object node) {
      CallSite[] var3 = $getCallSiteArray();
      var3[9].call(this.writer);
      if (!DefaultTypeTransformation.booleanUnbox(parent)) {
         var3[10].call(this.writer);
         var3[11].call(this.writer);
      }

   }

   protected void setParent(Object parent, Object child) {
      CallSite[] var3 = $getCallSiteArray();
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$xml$StaxBuilder()) {
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
      Class var10000 = $get$$class$groovy$xml$StaxBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$xml$StaxBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$xml$StaxBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object getWriter() {
      return this.writer;
   }

   public void setWriter(Object var1) {
      this.writer = var1;
   }

   // $FF: synthetic method
   public Object super$2$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$2$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public Object super$3$getName(String var1) {
      return super.getName(var1);
   }

   // $FF: synthetic method
   public Object super$3$getCurrent() {
      return super.getCurrent();
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1) {
      return super.invokeMethod(var1);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$3$nodeCompleted(Object var1, Object var2) {
      super.nodeCompleted(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$3$setCurrent(Object var1) {
      super.setCurrent(var1);
   }

   // $FF: synthetic method
   public Object super$3$postNodeCompletion(Object var1, Object var2) {
      return super.postNodeCompletion(var1, var2);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public Object super$3$doInvokeMethod(String var1, Object var2, Object var3) {
      return super.doInvokeMethod(var1, var2, var3);
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
   public void super$3$setClosureDelegate(Closure var1, Object var2) {
      super.setClosureDelegate(var1, var2);
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
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "writeStartDocument";
      var0[1] = "createNode";
      var0[2] = "createNode";
      var0[3] = "createNode";
      var0[4] = "writeStartElement";
      var0[5] = "toString";
      var0[6] = "each";
      var0[7] = "writeCharacters";
      var0[8] = "toString";
      var0[9] = "writeEndElement";
      var0[10] = "writeEndDocument";
      var0[11] = "flush";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[12];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$xml$StaxBuilder(), var0);
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
   private static Class $get$$class$groovy$xml$StaxBuilder() {
      Class var10000 = $class$groovy$xml$StaxBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$xml$StaxBuilder = class$("groovy.xml.StaxBuilder");
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
