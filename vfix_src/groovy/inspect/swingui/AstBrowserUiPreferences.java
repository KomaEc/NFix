package groovy.inspect.swingui;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.prefs.Preferences;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AstBrowserUiPreferences implements GroovyObject {
   private final Object frameLocation;
   private final Object frameSize;
   private final Object verticalDividerLocation;
   private final Object horizontalDividerLocation;
   private final boolean showScriptFreeForm;
   private final boolean showScriptClass;
   private int decompiledSourceFontSize;
   private final CompilePhaseAdapter selectedPhase;
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
   private static final Integer $const$3 = (Integer)12;
   // $FF: synthetic field
   private static final Integer $const$4 = (Integer)100;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202690L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202690 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$CompilePhaseAdapter;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$AstBrowserUiPreferences;
   // $FF: synthetic field
   private static Class $class$java$util$prefs$Preferences;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$Phases;

   public AstBrowserUiPreferences() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      Preferences prefs = (Preferences)ScriptBytecodeAdapter.castToType(var1[0].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$inspect$swingui$AstBrowserUiPreferences()), $get$$class$java$util$prefs$Preferences());
      this.frameLocation = ScriptBytecodeAdapter.createList(new Object[]{var1[1].call(prefs, "frameX", $const$0), var1[2].call(prefs, "frameY", $const$0)});
      this.frameSize = ScriptBytecodeAdapter.createList(new Object[]{var1[3].call(prefs, "frameWidth", $const$1), var1[4].call(prefs, "frameHeight", $const$2)});
      this.decompiledSourceFontSize = DefaultTypeTransformation.intUnbox(var1[5].call(prefs, "decompiledFontSize", $const$3));
      this.verticalDividerLocation = var1[6].call(prefs, "verticalSplitterLocation", $const$4);
      this.horizontalDividerLocation = var1[7].call(prefs, "horizontalSplitterLocation", $const$4);
      this.showScriptFreeForm = DefaultTypeTransformation.booleanUnbox(var1[8].call(prefs, "showScriptFreeForm", Boolean.FALSE));
      this.showScriptClass = DefaultTypeTransformation.booleanUnbox(var1[9].call(prefs, "showScriptClass", Boolean.TRUE));
      Integer phase = new Reference((Integer)ScriptBytecodeAdapter.castToType(var1[10].call(prefs, "compilerPhase", var1[11].callGetProperty($get$$class$org$codehaus$groovy$control$Phases())), $get$$class$java$lang$Integer()));
      this.selectedPhase = (CompilePhaseAdapter)ScriptBytecodeAdapter.castToType((CompilePhaseAdapter)ScriptBytecodeAdapter.castToType(var1[12].call(var1[13].call($get$$class$groovy$inspect$swingui$CompilePhaseAdapter()), (Object)(new AstBrowserUiPreferences._closure1(this, this, phase))), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter()), $get$$class$groovy$inspect$swingui$CompilePhaseAdapter());
   }

   public Object save(Object frame, Object vSplitter, Object hSplitter, Object scriptFreeFormPref, Object scriptClassPref, CompilePhaseAdapter phase) {
      CallSite[] var7 = $getCallSiteArray();
      Preferences prefs = (Preferences)ScriptBytecodeAdapter.castToType(var7[14].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$inspect$swingui$AstBrowserUiPreferences()), $get$$class$java$util$prefs$Preferences());
      var7[15].call(prefs, "decompiledFontSize", ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(DefaultTypeTransformation.box(this.decompiledSourceFontSize), $get$$class$java$lang$Integer()), Integer.TYPE));
      var7[16].call(prefs, "frameX", ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(var7[17].callGetProperty(var7[18].callGetProperty(frame)), $get$$class$java$lang$Integer()), Integer.TYPE));
      var7[19].call(prefs, "frameY", ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(var7[20].callGetProperty(var7[21].callGetProperty(frame)), $get$$class$java$lang$Integer()), Integer.TYPE));
      var7[22].call(prefs, "frameWidth", ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(var7[23].callGetProperty(var7[24].callGetProperty(frame)), $get$$class$java$lang$Integer()), Integer.TYPE));
      var7[25].call(prefs, "frameHeight", ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(var7[26].callGetProperty(var7[27].callGetProperty(frame)), $get$$class$java$lang$Integer()), Integer.TYPE));
      var7[28].call(prefs, "verticalSplitterLocation", var7[29].callGetProperty(vSplitter));
      var7[30].call(prefs, "horizontalSplitterLocation", var7[31].callGetProperty(hSplitter));
      var7[32].call(prefs, "showScriptFreeForm", scriptFreeFormPref);
      var7[33].call(prefs, "showScriptClass", scriptClassPref);
      return var7[34].call(prefs, "compilerPhase", var7[35].callGroovyObjectGetProperty(phase));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserUiPreferences()) {
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
      Class var10000 = $get$$class$groovy$inspect$swingui$AstBrowserUiPreferences();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$AstBrowserUiPreferences(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$AstBrowserUiPreferences(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public final Object getFrameLocation() {
      return this.frameLocation;
   }

   public final Object getFrameSize() {
      return this.frameSize;
   }

   public final Object getVerticalDividerLocation() {
      return this.verticalDividerLocation;
   }

   public final Object getHorizontalDividerLocation() {
      return this.horizontalDividerLocation;
   }

   public final boolean getShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public final boolean isShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public final boolean getShowScriptClass() {
      return this.showScriptClass;
   }

   public final boolean isShowScriptClass() {
      return this.showScriptClass;
   }

   public int getDecompiledSourceFontSize() {
      return this.decompiledSourceFontSize;
   }

   public void setDecompiledSourceFontSize(int var1) {
      this.decompiledSourceFontSize = var1;
   }

   public final CompilePhaseAdapter getSelectedPhase() {
      return this.selectedPhase;
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
      var0[0] = "userNodeForPackage";
      var0[1] = "getInt";
      var0[2] = "getInt";
      var0[3] = "getInt";
      var0[4] = "getInt";
      var0[5] = "getInt";
      var0[6] = "getInt";
      var0[7] = "getInt";
      var0[8] = "getBoolean";
      var0[9] = "getBoolean";
      var0[10] = "getInt";
      var0[11] = "SEMANTIC_ANALYSIS";
      var0[12] = "find";
      var0[13] = "values";
      var0[14] = "userNodeForPackage";
      var0[15] = "putInt";
      var0[16] = "putInt";
      var0[17] = "x";
      var0[18] = "location";
      var0[19] = "putInt";
      var0[20] = "y";
      var0[21] = "location";
      var0[22] = "putInt";
      var0[23] = "width";
      var0[24] = "size";
      var0[25] = "putInt";
      var0[26] = "height";
      var0[27] = "size";
      var0[28] = "putInt";
      var0[29] = "dividerLocation";
      var0[30] = "putInt";
      var0[31] = "dividerLocation";
      var0[32] = "putBoolean";
      var0[33] = "putBoolean";
      var0[34] = "putInt";
      var0[35] = "phaseId";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[36];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserUiPreferences(), var0);
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
   private static Class $get$$class$groovy$inspect$swingui$AstBrowserUiPreferences() {
      Class var10000 = $class$groovy$inspect$swingui$AstBrowserUiPreferences;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$AstBrowserUiPreferences = class$("groovy.inspect.swingui.AstBrowserUiPreferences");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$prefs$Preferences() {
      Class var10000 = $class$java$util$prefs$Preferences;
      if (var10000 == null) {
         var10000 = $class$java$util$prefs$Preferences = class$("java.util.prefs.Preferences");
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

   class _closure1 extends Closure implements GeneratedClosure {
      private Reference<T> phase;
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$java$lang$Integer;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;
      // $FF: synthetic field
      private static Class $class$groovy$inspect$swingui$AstBrowserUiPreferences$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject, Reference<T> phase) {
         CallSite[] var4 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
         this.phase = (Reference)phase;
      }

      public Object doCall(Object it) {
         Object itx = new Reference(it);
         CallSite[] var3 = $getCallSiteArray();
         return ScriptBytecodeAdapter.compareEqual(var3[0].callGetProperty(itx.get()), this.phase.get()) ? Boolean.TRUE : Boolean.FALSE;
      }

      public Integer getPhase() {
         CallSite[] var1 = $getCallSiteArray();
         return (Integer)ScriptBytecodeAdapter.castToType(this.phase.get(), $get$$class$java$lang$Integer());
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserUiPreferences$_closure1()) {
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
         var0[0] = "phaseId";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserUiPreferences$_closure1(), var0);
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
      private static Class $get$$class$java$lang$Object() {
         Class var10000 = $class$java$lang$Object;
         if (var10000 == null) {
            var10000 = $class$java$lang$Object = class$("java.lang.Object");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$groovy$inspect$swingui$AstBrowserUiPreferences$_closure1() {
         Class var10000 = $class$groovy$inspect$swingui$AstBrowserUiPreferences$_closure1;
         if (var10000 == null) {
            var10000 = $class$groovy$inspect$swingui$AstBrowserUiPreferences$_closure1 = class$("groovy.inspect.swingui.AstBrowserUiPreferences$_closure1");
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
}
