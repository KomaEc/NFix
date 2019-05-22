package groovy.inspect.swingui;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public enum CompilePhaseAdapter implements GroovyObject {
   INITIALIZATION = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[11].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "INITIALIZATION", 0, $getCallSiteArray()[12].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Initialization"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter())),
   PARSING = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[13].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "PARSING", 1, $getCallSiteArray()[14].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Parsing"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter())),
   CONVERSION = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[15].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "CONVERSION", 2, $getCallSiteArray()[16].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Conversion"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter())),
   SEMANTIC_ANALYSIS = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[17].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "SEMANTIC_ANALYSIS", 3, $getCallSiteArray()[18].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Semantic Analysis"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter())),
   CANONICALIZATION = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[19].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "CANONICALIZATION", 4, $getCallSiteArray()[20].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Canonicalization"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter())),
   INSTRUCTION_SELECTION = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[21].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "INSTRUCTION_SELECTION", 5, $getCallSiteArray()[22].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Instruction Selection"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter())),
   CLASS_GENERATION = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[23].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "CLASS_GENERATION", 6, $getCallSiteArray()[24].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Class Generation"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter())),
   OUTPUT = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[25].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "OUTPUT", 7, $getCallSiteArray()[26].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Output"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter())),
   FINALIZATION = (CompilePhaseAdapter)((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType($getCallSiteArray()[27].callStatic($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), "FINALIZATION", 8, $getCallSiteArray()[28].callGetProperty($get$$class$org$codehaus$groovy$control$Phases()), "Finalization"), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter()));

   private final int phaseId;
   private final String string;
   public static final CompilePhaseAdapter MIN_VALUE = (CompilePhaseAdapter)INITIALIZATION;
   public static final CompilePhaseAdapter MAX_VALUE = (CompilePhaseAdapter)FINALIZATION;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)3;
   // $FF: synthetic field
   private static final Integer $const$4 = (Integer)4;
   // $FF: synthetic field
   private static final Integer $const$5 = (Integer)5;
   // $FF: synthetic field
   private static final Integer $const$6 = (Integer)6;
   // $FF: synthetic field
   private static final Integer $const$7 = (Integer)7;
   // $FF: synthetic field
   private static final Integer $const$8 = (Integer)8;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204108L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204108 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$CompilePhaseAdapter;
   // $FF: synthetic field
   private static Class array$$class$groovy$inspect$swingui$CompilePhaseAdapter;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Enum;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$Phases;

   public CompilePhaseAdapter(Object phaseId, Object string) {
      CallSite[] var5 = $getCallSiteArray();
      Object[] var10000 = new Object[]{__str, DefaultTypeTransformation.box(__int)};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$java$lang$Enum());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((String)var10001[0], DefaultTypeTransformation.intUnbox(var10001[1]));
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         this.phaseId = DefaultTypeTransformation.intUnbox(phaseId);
         this.string = (String)ScriptBytecodeAdapter.castToType(string, $get$$class$java$lang$String());
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   public String toString() {
      CallSite[] var1 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(this.string, $get$$class$java$lang$String());
   }

   // $FF: synthetic method
   public CompilePhaseAdapter next() {
      CallSite[] var1 = $getCallSiteArray();
      Object ordinal = var1[1].call(var1[2].callCurrent(this));
      if (ScriptBytecodeAdapter.compareGreaterThanEqual(ordinal, var1[3].call($VALUES))) {
         ordinal = $const$0;
      }

      return (CompilePhaseAdapter)ScriptBytecodeAdapter.castToType(var1[4].call($VALUES, (Object)ordinal), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter());
   }

   // $FF: synthetic method
   public CompilePhaseAdapter previous() {
      CallSite[] var1 = $getCallSiteArray();
      Object ordinal = var1[5].call(var1[6].callCurrent(this));
      if (ScriptBytecodeAdapter.compareLessThan(ordinal, $const$0)) {
         ordinal = var1[7].call(var1[8].call($VALUES), (Object)$const$1);
      }

      return (CompilePhaseAdapter)ScriptBytecodeAdapter.castToType(var1[9].call($VALUES, (Object)ordinal), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter());
   }

   // $FF: synthetic method
   public static final CompilePhaseAdapter $INIT(Object... para) {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[0];
      Object[] var10001 = new Object[]{para};
      int[] var2 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      var10000 = ScriptBytecodeAdapter.despreadList(var10000, var10001, var2);
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$groovy$inspect$swingui$CompilePhaseAdapter());
      var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      CompilePhaseAdapter var10003 = new CompilePhaseAdapter;
      switch(var10002 >> 8) {
      case 0:
         var10003.<init>(var10001[2], var10001[3]);
         return (CompilePhaseAdapter)ScriptBytecodeAdapter.castToType(var10003, $get$$class$groovy$inspect$swingui$CompilePhaseAdapter());
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$CompilePhaseAdapter()) {
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

   public final int getPhaseId() {
      return this.phaseId;
   }

   public final String getString() {
      return this.string;
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public int super$2$compareTo(Object var1) {
      return super.compareTo(var1);
   }

   // $FF: synthetic method
   public String super$2$toString() {
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
   public int super$2$ordinal() {
      return super.ordinal();
   }

   // $FF: synthetic method
   public String super$2$name() {
      return super.name();
   }

   // $FF: synthetic method
   public void super$2$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public int super$2$compareTo(Enum var1) {
      return super.compareTo(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$2$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$2$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$2$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public Class super$2$getDeclaringClass() {
      return super.getDeclaringClass();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "clone";
      var0[1] = "next";
      var0[2] = "ordinal";
      var0[3] = "size";
      var0[4] = "getAt";
      var0[5] = "previous";
      var0[6] = "ordinal";
      var0[7] = "minus";
      var0[8] = "size";
      var0[9] = "getAt";
      var0[10] = "valueOf";
      var0[11] = "$INIT";
      var0[12] = "INITIALIZATION";
      var0[13] = "$INIT";
      var0[14] = "PARSING";
      var0[15] = "$INIT";
      var0[16] = "CONVERSION";
      var0[17] = "$INIT";
      var0[18] = "SEMANTIC_ANALYSIS";
      var0[19] = "$INIT";
      var0[20] = "CANONICALIZATION";
      var0[21] = "$INIT";
      var0[22] = "INSTRUCTION_SELECTION";
      var0[23] = "$INIT";
      var0[24] = "CLASS_GENERATION";
      var0[25] = "$INIT";
      var0[26] = "OUTPUT";
      var0[27] = "$INIT";
      var0[28] = "FINALIZATION";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[29];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$CompilePhaseAdapter(), var0);
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
   private static Class $get$array$$class$groovy$inspect$swingui$CompilePhaseAdapter() {
      Class var10000 = array$$class$groovy$inspect$swingui$CompilePhaseAdapter;
      if (var10000 == null) {
         var10000 = array$$class$groovy$inspect$swingui$CompilePhaseAdapter = class$("[Lgroovy.inspect.swingui.CompilePhaseAdapter;");
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
   private static Class $get$$class$java$lang$Enum() {
      Class var10000 = $class$java$lang$Enum;
      if (var10000 == null) {
         var10000 = $class$java$lang$Enum = class$("java.lang.Enum");
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
   private static Class $get$$class$org$codehaus$groovy$control$Phases() {
      Class var10000 = $class$org$codehaus$groovy$control$Phases;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$Phases = class$("org.codehaus.groovy.control.Phases");
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
