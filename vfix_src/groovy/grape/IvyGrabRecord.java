package groovy.grape;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.List;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class IvyGrabRecord implements GroovyObject {
   private ModuleRevisionId mrid;
   private List<String> conf;
   private boolean changing;
   private boolean transitive;
   private boolean force;
   private String classifier;
   private String ext;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Long $const$0 = (Long)2863311530L;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1431655765;
   // $FF: synthetic field
   private static final Long $const$2 = (Long)3149642683L;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)1717986918;
   // $FF: synthetic field
   private static final Long $const$4 = (Long)3435973836L;
   // $FF: synthetic field
   private static final Integer $const$5 = (Integer)2004318071;
   // $FF: synthetic field
   private static final Integer $const$6 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202603L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202603 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$groovy$grape$IvyGrabRecord;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public IvyGrabRecord() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public int hashCode() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(var1[0].call(var1[1].call(var1[2].call(var1[3].call(var1[4].call(var1[5].call(var1[6].call(this.mrid), var1[7].call(this.conf)), DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.changing)) ? $const$0 : $const$1), DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.transitive)) ? $const$2 : $const$3), DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.force)) ? $const$4 : $const$5), DefaultTypeTransformation.booleanUnbox(this.classifier) ? var1[8].call(this.classifier) : $const$6), DefaultTypeTransformation.booleanUnbox(this.ext) ? var1[9].call(this.ext) : $const$6), $get$$class$java$lang$Integer()));
   }

   public boolean equals(Object o) {
      CallSite[] var2 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var2[10].callGetProperty(o), $get$$class$groovy$grape$IvyGrabRecord()) && ScriptBytecodeAdapter.compareEqual(DefaultTypeTransformation.box(this.changing), var2[11].callGetProperty(o)) ? Boolean.TRUE : Boolean.FALSE) && ScriptBytecodeAdapter.compareEqual(DefaultTypeTransformation.box(this.transitive), var2[12].callGetProperty(o)) ? Boolean.TRUE : Boolean.FALSE) && ScriptBytecodeAdapter.compareEqual(DefaultTypeTransformation.box(this.force), var2[13].callGetProperty(o)) ? Boolean.TRUE : Boolean.FALSE) && ScriptBytecodeAdapter.compareEqual(this.mrid, var2[14].callGetProperty(o)) ? Boolean.TRUE : Boolean.FALSE) && ScriptBytecodeAdapter.compareEqual(this.conf, var2[15].callGetProperty(o)) ? Boolean.TRUE : Boolean.FALSE) && ScriptBytecodeAdapter.compareEqual(this.classifier, var2[16].callGetProperty(o)) ? Boolean.TRUE : Boolean.FALSE) && ScriptBytecodeAdapter.compareEqual(this.ext, var2[17].callGetProperty(o)) ? Boolean.TRUE : Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$grape$IvyGrabRecord()) {
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
      Class var10000 = $get$$class$groovy$grape$IvyGrabRecord();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$grape$IvyGrabRecord(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$grape$IvyGrabRecord(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public ModuleRevisionId getMrid() {
      return this.mrid;
   }

   public void setMrid(ModuleRevisionId var1) {
      this.mrid = var1;
   }

   public List<String> getConf() {
      return this.conf;
   }

   public void setConf(List<String> var1) {
      this.conf = var1;
   }

   public boolean getChanging() {
      return this.changing;
   }

   public boolean isChanging() {
      return this.changing;
   }

   public void setChanging(boolean var1) {
      this.changing = var1;
   }

   public boolean getTransitive() {
      return this.transitive;
   }

   public boolean isTransitive() {
      return this.transitive;
   }

   public void setTransitive(boolean var1) {
      this.transitive = var1;
   }

   public boolean getForce() {
      return this.force;
   }

   public boolean isForce() {
      return this.force;
   }

   public void setForce(boolean var1) {
      this.force = var1;
   }

   public String getClassifier() {
      return this.classifier;
   }

   public void setClassifier(String var1) {
      this.classifier = var1;
   }

   public String getExt() {
      return this.ext;
   }

   public void setExt(String var1) {
      this.ext = var1;
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
      var0[0] = "xor";
      var0[1] = "xor";
      var0[2] = "xor";
      var0[3] = "xor";
      var0[4] = "xor";
      var0[5] = "xor";
      var0[6] = "hashCode";
      var0[7] = "hashCode";
      var0[8] = "hashCode";
      var0[9] = "hashCode";
      var0[10] = "class";
      var0[11] = "changing";
      var0[12] = "transitive";
      var0[13] = "force";
      var0[14] = "mrid";
      var0[15] = "conf";
      var0[16] = "classifier";
      var0[17] = "ext";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[18];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$grape$IvyGrabRecord(), var0);
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
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$grape$IvyGrabRecord() {
      Class var10000 = $class$groovy$grape$IvyGrabRecord;
      if (var10000 == null) {
         var10000 = $class$groovy$grape$IvyGrabRecord = class$("groovy.grape.IvyGrabRecord");
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
