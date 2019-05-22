package org.codehaus.groovy.tools.shell;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.util.Logger;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class BufferManager implements GroovyObject {
   protected final Logger log;
   private final List buffers;
   private int selected;
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
   public static Long __timeStamp = (Long)1292524203942L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203942 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$BufferManager;

   public BufferManager() {
      CallSite[] var1 = $getCallSiteArray();
      this.log = (Logger)ScriptBytecodeAdapter.castToType((Logger)ScriptBytecodeAdapter.castToType(var1[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), (Object)var1[1].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$util$Logger()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.buffers = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      var1[2].callCurrent(this);
   }

   public void reset() {
      CallSite[] var1 = $getCallSiteArray();
      var1[3].call(this.buffers);
      var1[4].callCurrent(this, (Object)Boolean.TRUE);
      var1[5].call(this.log, (Object)"Buffers reset");
   }

   public List current() {
      CallSite[] var1 = $getCallSiteArray();
      ValueRecorder var2 = new ValueRecorder();

      try {
         boolean var10000 = !DefaultTypeTransformation.booleanUnbox(var2.record(var1[6].call(var2.record(var2.record(var2.record(this.buffers, -1), -1), 9)), 17));
         var2.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 8);
         if (var10000) {
            var2.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert !buffers.isEmpty()", var2), (Object)null);
         }
      } catch (Throwable var4) {
         var2.clear();
         throw var4;
      }

      return (List)ScriptBytecodeAdapter.castToType(var1[7].call(this.buffers, (Object)DefaultTypeTransformation.box(this.selected)), $get$$class$java$util$List());
   }

   public void select(int index) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         Boolean var6;
         label66: {
            boolean var10000 = ScriptBytecodeAdapter.compareGreaterThanEqual(var3.record(DefaultTypeTransformation.box(index), 8), $const$0);
            var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 14);
            if (var10000) {
               var10000 = ScriptBytecodeAdapter.compareLessThan(var3.record(DefaultTypeTransformation.box(index), 22), var3.record(var2[8].call(var3.record(var3.record(var3.record(this.buffers, -1), -1), 30)), 38));
               var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 28);
               if (var10000) {
                  var6 = Boolean.TRUE;
                  break label66;
               }
            }

            var6 = Boolean.FALSE;
         }

         if (DefaultTypeTransformation.booleanUnbox(var3.record(var6, 19))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert index >= 0 && index < buffers.size()", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      this.selected = DefaultTypeTransformation.intUnbox(DefaultTypeTransformation.box(index));
   }

   public int create(boolean select) {
      CallSite[] var2 = $getCallSiteArray();
      var2[9].call(this.buffers, (Object)ScriptBytecodeAdapter.createList(new Object[0]));
      Object i = var2[10].call(var2[11].call(this.buffers), (Object)$const$1);
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(select))) {
         var2[12].callCurrent(this, (Object)i);
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[13].callGetProperty(this.log))) {
         var2[14].call(this.log, (Object)(new GStringImpl(new Object[]{i}, new String[]{"Created new buffer with index: ", ""})));
      }

      return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(i, $get$$class$java$lang$Integer()));
   }

   public void delete(int index) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         Boolean var6;
         label74: {
            boolean var10000 = ScriptBytecodeAdapter.compareGreaterThanEqual(var3.record(DefaultTypeTransformation.box(index), 8), $const$0);
            var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 14);
            if (var10000) {
               var10000 = ScriptBytecodeAdapter.compareLessThan(var3.record(DefaultTypeTransformation.box(index), 22), var3.record(var2[15].call(var3.record(var3.record(var3.record(this.buffers, -1), -1), 30)), 38));
               var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 28);
               if (var10000) {
                  var6 = Boolean.TRUE;
                  break label74;
               }
            }

            var6 = Boolean.FALSE;
         }

         if (DefaultTypeTransformation.booleanUnbox(var3.record(var6, 19))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert index >= 0 && index < buffers.size()", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      var2[16].call(this.buffers, (Object)DefaultTypeTransformation.box(index));
      if (DefaultTypeTransformation.booleanUnbox(var2[17].callGetProperty(this.log))) {
         var2[18].call(this.log, (Object)(new GStringImpl(new Object[]{DefaultTypeTransformation.box(index)}, new String[]{"Deleted buffer with index: ", ""})));
      }

   }

   public int size() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(var1[19].call(this.buffers), $get$$class$java$lang$Integer()));
   }

   public void deleteSelected() {
      CallSite[] var1 = $getCallSiteArray();
      var1[20].callCurrent(this, (Object)DefaultTypeTransformation.box(this.selected));
      Object i = var1[21].call(DefaultTypeTransformation.box(this.selected), (Object)$const$1);
      if (ScriptBytecodeAdapter.compareLessThan(i, $const$0)) {
         var1[22].callCurrent(this, (Object)$const$0);
      } else {
         var1[23].callCurrent(this, (Object)i);
      }

   }

   public void clearSelected() {
      CallSite[] var1 = $getCallSiteArray();
      var1[24].call(var1[25].callCurrent(this));
   }

   public void updateSelected(List buffer) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var3.record(buffer, 8), (Object)null);
         var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 15);
         if (var10000) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert buffer != null", var3), (Object)null);
         }
      } catch (Throwable var6) {
         var3.clear();
         throw var6;
      }

      var2[26].call(this.buffers, DefaultTypeTransformation.box(this.selected), buffer);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$BufferManager()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$BufferManager();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$BufferManager(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$BufferManager(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public final List getBuffers() {
      return this.buffers;
   }

   public int getSelected() {
      return this.selected;
   }

   public void setSelected(int var1) {
      this.selected = var1;
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
      var0[0] = "create";
      var0[1] = "class";
      var0[2] = "reset";
      var0[3] = "clear";
      var0[4] = "create";
      var0[5] = "debug";
      var0[6] = "isEmpty";
      var0[7] = "getAt";
      var0[8] = "size";
      var0[9] = "leftShift";
      var0[10] = "minus";
      var0[11] = "size";
      var0[12] = "select";
      var0[13] = "debugEnabled";
      var0[14] = "debug";
      var0[15] = "size";
      var0[16] = "remove";
      var0[17] = "debugEnabled";
      var0[18] = "debug";
      var0[19] = "size";
      var0[20] = "delete";
      var0[21] = "minus";
      var0[22] = "select";
      var0[23] = "select";
      var0[24] = "clear";
      var0[25] = "current";
      var0[26] = "putAt";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[27];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$BufferManager(), var0);
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
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Logger() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Logger;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$Logger = class$("org.codehaus.groovy.tools.shell.util.Logger");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$BufferManager() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$BufferManager;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$BufferManager = class$("org.codehaus.groovy.tools.shell.BufferManager");
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
