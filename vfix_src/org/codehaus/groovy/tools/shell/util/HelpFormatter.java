package org.codehaus.groovy.tools.shell.util;

import com.gzoltar.shaded.org.apache.commons.cli.Options;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.util.Comparator;
import java.util.List;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class HelpFormatter extends com.gzoltar.shaded.org.apache.commons.cli.HelpFormatter implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)4;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204866L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204866 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$HelpFormatter;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$lang$StringBuffer;
   // $FF: synthetic field
   private static Class $class$jline$Terminal;

   public HelpFormatter() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ScriptBytecodeAdapter.setGroovyObjectProperty($const$0, $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter(), this, "defaultLeftPad");
      ScriptBytecodeAdapter.setGroovyObjectProperty($const$1, $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter(), this, "defaultDescPad");
   }

   public int getDefaultWidth() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(var1[0].call(var1[1].callGetProperty(var1[2].callGetProperty($get$$class$jline$Terminal())), (Object)$const$2), $get$$class$java$lang$Integer()));
   }

   protected StringBuffer renderOptions(StringBuffer sb, int width, Options options, int leftPad, int descPad) {
      StringBuffer sb = new Reference(sb);
      Integer width = new Reference(DefaultTypeTransformation.box(width));
      Integer descPad = new Reference(DefaultTypeTransformation.box(descPad));
      CallSite[] var9 = $getCallSiteArray();
      ValueRecorder var10 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var10.record(sb.get(), 8), (Object)null);
         var10.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 11);
         if (var10000) {
            var10.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert sb != null", var10), (Object)null);
         }
      } catch (Throwable var22) {
         var10.clear();
         throw var22;
      }

      ValueRecorder var11 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var11.record(options, 8))) {
            var11.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert options", var11), (Object)null);
         }
      } catch (Throwable var21) {
         var11.clear();
         throw var21;
      }

      List prefixes = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
      String lpad = new Reference((String)ScriptBytecodeAdapter.castToType(var9[3].call(" ", (Object)DefaultTypeTransformation.box(leftPad)), $get$$class$java$lang$String()));
      List opts = new Reference((List)ScriptBytecodeAdapter.castToType(var9[4].call(var9[5].call(var9[6].callGetProperty(options)), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object a, Object b) {
            Object ax = new Reference(a);
            Object bx = new Reference(b);
            CallSite[] var5 = $getCallSiteArray();
            return ScriptBytecodeAdapter.compareTo(ScriptBytecodeAdapter.compareEqual(var5[0].callGetProperty(ax.get()), " ") ? var5[1].callGetProperty(ax.get()) : var5[2].callGetProperty(ax.get()), ScriptBytecodeAdapter.compareEqual(var5[3].callGetProperty(bx.get()), " ") ? var5[4].callGetProperty(bx.get()) : var5[5].callGetProperty(bx.get()));
         }

         public Object call(Object a, Object b) {
            Object ax = new Reference(a);
            Object bx = new Reference(b);
            CallSite[] var5 = $getCallSiteArray();
            return var5[6].callCurrent(this, ax.get(), bx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure1()) {
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
            var0[0] = "opt";
            var0[1] = "longOpt";
            var0[2] = "opt";
            var0[3] = "opt";
            var0[4] = "longOpt";
            var0[5] = "opt";
            var0[6] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[7];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure1() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure1 = class$("org.codehaus.groovy.tools.shell.util.HelpFormatter$_renderOptions_closure1");
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
      })), $get$$class$java$util$List()));
      var9[7].call(opts.get(), (Object)(new GeneratedClosure(this, this, lpad, prefixes) {
         private Reference<T> lpad;
         private Reference<T> prefixes;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)8;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure2;
         // $FF: synthetic field
         private static Class $class$java$util$List;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$StringBuffer;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.lpad = (Reference)lpad;
            this.prefixes = (Reference)prefixes;
         }

         public Object doCall(Object option) {
            Object optionx = new Reference(option);
            CallSite[] var3 = $getCallSiteArray();
            Object buff = new Reference(var3[0].callConstructor($get$$class$java$lang$StringBuffer(), (Object)$const$0));
            if (ScriptBytecodeAdapter.compareEqual(var3[1].callGetProperty(optionx.get()), " ")) {
               var3[2].call(buff.get(), (Object)(new GStringImpl(new Object[]{this.lpad.get(), var3[3].callGroovyObjectGetProperty(this), var3[4].callGetProperty(optionx.get())}, new String[]{"", "    ", "", ""})));
            } else {
               var3[5].call(buff.get(), (Object)(new GStringImpl(new Object[]{this.lpad.get(), var3[6].callGroovyObjectGetProperty(this), var3[7].callGetProperty(optionx.get())}, new String[]{"", "", "", ""})));
               if (DefaultTypeTransformation.booleanUnbox(var3[8].call(optionx.get()))) {
                  var3[9].call(buff.get(), (Object)(new GStringImpl(new Object[]{var3[10].callGroovyObjectGetProperty(this), var3[11].callGetProperty(optionx.get())}, new String[]{", ", "", ""})));
               }
            }

            if (DefaultTypeTransformation.booleanUnbox(var3[12].call(optionx.get()))) {
               if (DefaultTypeTransformation.booleanUnbox(var3[13].call(optionx.get()))) {
                  if (DefaultTypeTransformation.booleanUnbox(var3[14].call(optionx.get()))) {
                     var3[15].call(buff.get(), (Object)(new GStringImpl(new Object[]{var3[16].callGetProperty(optionx.get())}, new String[]{"[=", "]"})));
                  } else {
                     var3[17].call(buff.get(), (Object)(new GStringImpl(new Object[]{var3[18].callGetProperty(optionx.get())}, new String[]{"=", ""})));
                  }
               } else {
                  var3[19].call(buff.get(), (Object)" ");
               }
            }

            return var3[20].call(this.prefixes.get(), buff.get());
         }

         public String getLpad() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.lpad.get(), $get$$class$java$lang$String());
         }

         public List getPrefixes() {
            CallSite[] var1 = $getCallSiteArray();
            return (List)ScriptBytecodeAdapter.castToType(this.prefixes.get(), $get$$class$java$util$List());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure2()) {
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
            var0[1] = "opt";
            var0[2] = "leftShift";
            var0[3] = "defaultLongOptPrefix";
            var0[4] = "longOpt";
            var0[5] = "leftShift";
            var0[6] = "defaultOptPrefix";
            var0[7] = "opt";
            var0[8] = "hasLongOpt";
            var0[9] = "leftShift";
            var0[10] = "defaultLongOptPrefix";
            var0[11] = "longOpt";
            var0[12] = "hasArg";
            var0[13] = "hasArgName";
            var0[14] = "hasOptionalArg";
            var0[15] = "leftShift";
            var0[16] = "argName";
            var0[17] = "leftShift";
            var0[18] = "argName";
            var0[19] = "leftShift";
            var0[20] = "leftShift";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[21];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure2(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure2() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure2;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure2 = class$("org.codehaus.groovy.tools.shell.util.HelpFormatter$_renderOptions_closure2");
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
         private static Class $get$$class$java$lang$StringBuffer() {
            Class var10000 = $class$java$lang$StringBuffer;
            if (var10000 == null) {
               var10000 = $class$java$lang$StringBuffer = class$("java.lang.StringBuffer");
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
      Integer maxPrefix = new Reference((Integer)ScriptBytecodeAdapter.castToType(var9[8].call(var9[9].call(prefixes.get(), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure3;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object a, Object b) {
            Object ax = new Reference(a);
            Object bx = new Reference(b);
            CallSite[] var5 = $getCallSiteArray();
            return ScriptBytecodeAdapter.compareTo(var5[0].call(ax.get()), var5[1].call(bx.get()));
         }

         public Object call(Object a, Object b) {
            Object ax = new Reference(a);
            Object bx = new Reference(b);
            CallSite[] var5 = $getCallSiteArray();
            return var5[2].callCurrent(this, ax.get(), bx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure3()) {
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
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure3(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure3() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure3;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure3 = class$("org.codehaus.groovy.tools.shell.util.HelpFormatter$_renderOptions_closure3");
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
      }))), $get$$class$java$lang$Integer()));
      String dpad = new Reference((String)ScriptBytecodeAdapter.castToType(var9[10].call(" ", (Object)descPad.get()), $get$$class$java$lang$String()));
      var9[11].call(opts.get(), (Object)(new GeneratedClosure(this, this, opts, descPad, dpad, width, sb, maxPrefix, prefixes) {
         private Reference<T> opts;
         private Reference<T> descPad;
         private Reference<T> dpad;
         private Reference<T> width;
         private Reference<T> sb;
         private Reference<T> maxPrefix;
         private Reference<T> prefixes;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)1;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure4;
         // $FF: synthetic field
         private static Class $class$java$util$List;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$StringBuffer;

         public {
            CallSite[] var10 = $getCallSiteArray();
            this.opts = (Reference)opts;
            this.descPad = (Reference)descPad;
            this.dpad = (Reference)dpad;
            this.width = (Reference)width;
            this.sb = (Reference)sb;
            this.maxPrefix = (Reference)maxPrefix;
            this.prefixes = (Reference)prefixes;
         }

         public Object doCall(Object option, Object i) {
            Object optionx = new Reference(option);
            Object ix = new Reference(i);
            CallSite[] var5 = $getCallSiteArray();
            Object buff = new Reference(var5[0].callConstructor($get$$class$java$lang$StringBuffer(), (Object)var5[1].call(var5[2].call(this.prefixes.get(), ix.get()))));
            if (ScriptBytecodeAdapter.compareLessThan(var5[3].call(buff.get()), this.maxPrefix.get())) {
               var5[4].call(buff.get(), var5[5].call(" ", (Object)var5[6].call(this.maxPrefix.get(), var5[7].call(buff.get()))));
            }

            var5[8].call(buff.get(), this.dpad.get());
            Integer nextLineTabStop = (Integer)ScriptBytecodeAdapter.castToType(var5[9].call(this.maxPrefix.get(), this.descPad.get()), $get$$class$java$lang$Integer());
            String text = (String)ScriptBytecodeAdapter.castToType(var5[10].call(buff.get(), var5[11].callGetProperty(optionx.get())), $get$$class$java$lang$String());
            var5[12].callCurrent(this, this.sb.get(), this.width.get(), nextLineTabStop, text);
            return ScriptBytecodeAdapter.compareLessThan(ix.get(), var5[13].call(var5[14].call(this.opts.get()), (Object)$const$0)) ? var5[15].call(this.sb.get(), var5[16].callGroovyObjectGetProperty(this)) : null;
         }

         public Object call(Object option, Object i) {
            Object optionx = new Reference(option);
            Object ix = new Reference(i);
            CallSite[] var5 = $getCallSiteArray();
            return var5[17].callCurrent(this, optionx.get(), ix.get());
         }

         public List getOpts() {
            CallSite[] var1 = $getCallSiteArray();
            return (List)ScriptBytecodeAdapter.castToType(this.opts.get(), $get$$class$java$util$List());
         }

         public int getDescPad() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.descPad.get(), $get$$class$java$lang$Integer()));
         }

         public String getDpad() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.dpad.get(), $get$$class$java$lang$String());
         }

         public int getWidth() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.width.get(), $get$$class$java$lang$Integer()));
         }

         public StringBuffer getSb() {
            CallSite[] var1 = $getCallSiteArray();
            return (StringBuffer)ScriptBytecodeAdapter.castToType(this.sb.get(), $get$$class$java$lang$StringBuffer());
         }

         public Integer getMaxPrefix() {
            CallSite[] var1 = $getCallSiteArray();
            return (Integer)ScriptBytecodeAdapter.castToType(this.maxPrefix.get(), $get$$class$java$lang$Integer());
         }

         public List getPrefixes() {
            CallSite[] var1 = $getCallSiteArray();
            return (List)ScriptBytecodeAdapter.castToType(this.prefixes.get(), $get$$class$java$util$List());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure4()) {
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
            var0[1] = "toString";
            var0[2] = "getAt";
            var0[3] = "size";
            var0[4] = "leftShift";
            var0[5] = "multiply";
            var0[6] = "minus";
            var0[7] = "size";
            var0[8] = "leftShift";
            var0[9] = "plus";
            var0[10] = "leftShift";
            var0[11] = "description";
            var0[12] = "renderWrappedText";
            var0[13] = "minus";
            var0[14] = "size";
            var0[15] = "leftShift";
            var0[16] = "defaultNewLine";
            var0[17] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[18];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure4(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure4() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure4;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter$_renderOptions_closure4 = class$("org.codehaus.groovy.tools.shell.util.HelpFormatter$_renderOptions_closure4");
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
         private static Class $get$$class$java$lang$StringBuffer() {
            Class var10000 = $class$java$lang$StringBuffer;
            if (var10000 == null) {
               var10000 = $class$java$lang$StringBuffer = class$("java.lang.StringBuffer");
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
      return (StringBuffer)ScriptBytecodeAdapter.castToType(sb.get(), $get$$class$java$lang$StringBuffer());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public void super$2$setOptPrefix(String var1) {
      super.setOptPrefix(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public String super$2$getArgName() {
      return super.getArgName();
   }

   // $FF: synthetic method
   public void super$2$printHelp(int var1, String var2, String var3, Options var4, String var5, boolean var6) {
      super.printHelp(var1, var2, var3, var4, var5, var6);
   }

   // $FF: synthetic method
   public void super$2$setLeftPadding(int var1) {
      super.setLeftPadding(var1);
   }

   // $FF: synthetic method
   public void super$2$setArgName(String var1) {
      super.setArgName(var1);
   }

   // $FF: synthetic method
   public void super$2$printHelp(String var1, String var2, Options var3, String var4, boolean var5) {
      super.printHelp(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public Comparator super$2$getOptionComparator() {
      return super.getOptionComparator();
   }

   // $FF: synthetic method
   public int super$2$getWidth() {
      return super.getWidth();
   }

   // $FF: synthetic method
   public String super$2$getOptPrefix() {
      return super.getOptPrefix();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$2$printUsage(PrintWriter var1, int var2, String var3) {
      super.printUsage(var1, var2, var3);
   }

   // $FF: synthetic method
   public String super$2$getSyntaxPrefix() {
      return super.getSyntaxPrefix();
   }

   // $FF: synthetic method
   public void super$2$printHelp(int var1, String var2, String var3, Options var4, String var5) {
      super.printHelp(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$2$printOptions(PrintWriter var1, int var2, Options var3, int var4, int var5) {
      super.printOptions(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public void super$2$setNewLine(String var1) {
      super.setNewLine(var1);
   }

   // $FF: synthetic method
   public int super$2$getLeftPadding() {
      return super.getLeftPadding();
   }

   // $FF: synthetic method
   public void super$2$setWidth(int var1) {
      super.setWidth(var1);
   }

   // $FF: synthetic method
   public void super$2$setSyntaxPrefix(String var1) {
      super.setSyntaxPrefix(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$2$setDescPadding(int var1) {
      super.setDescPadding(var1);
   }

   // $FF: synthetic method
   public StringBuffer super$2$renderOptions(StringBuffer var1, int var2, Options var3, int var4, int var5) {
      return super.renderOptions(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public String super$2$getNewLine() {
      return super.getNewLine();
   }

   // $FF: synthetic method
   public void super$2$setOptionComparator(Comparator var1) {
      super.setOptionComparator(var1);
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
   public String super$2$getLongOptPrefix() {
      return super.getLongOptPrefix();
   }

   // $FF: synthetic method
   public void super$2$printWrapped(PrintWriter var1, int var2, int var3, String var4) {
      super.printWrapped(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void super$2$printHelp(String var1, Options var2) {
      super.printHelp(var1, var2);
   }

   // $FF: synthetic method
   public String super$2$createPadding(int var1) {
      return super.createPadding(var1);
   }

   // $FF: synthetic method
   public int super$2$findWrapPos(String var1, int var2, int var3) {
      return super.findWrapPos(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$printUsage(PrintWriter var1, int var2, String var3, Options var4) {
      super.printUsage(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void super$2$printHelp(String var1, Options var2, boolean var3) {
      super.printHelp(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$printWrapped(PrintWriter var1, int var2, String var3) {
      super.printWrapped(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public String super$2$rtrim(String var1) {
      return super.rtrim(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$2$printHelp(PrintWriter var1, int var2, String var3, String var4, Options var5, int var6, int var7, String var8, boolean var9) {
      super.printHelp(var1, var2, var3, var4, var5, var6, var7, var8, var9);
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
   public void super$2$printHelp(PrintWriter var1, int var2, String var3, String var4, Options var5, int var6, int var7, String var8) {
      super.printHelp(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   // $FF: synthetic method
   public StringBuffer super$2$renderWrappedText(StringBuffer var1, int var2, int var3, String var4) {
      return super.renderWrappedText(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void super$2$setLongOptPrefix(String var1) {
      super.setLongOptPrefix(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public void super$2$printHelp(String var1, String var2, Options var3, String var4) {
      super.printHelp(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public int super$2$getDescPadding() {
      return super.getDescPadding();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "minus";
      var0[1] = "terminalWidth";
      var0[2] = "terminal";
      var0[3] = "multiply";
      var0[4] = "sort";
      var0[5] = "values";
      var0[6] = "shortOpts";
      var0[7] = "each";
      var0[8] = "size";
      var0[9] = "max";
      var0[10] = "multiply";
      var0[11] = "eachWithIndex";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[12];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter = class$("org.codehaus.groovy.tools.shell.util.HelpFormatter");
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
   private static Class $get$$class$java$lang$StringBuffer() {
      Class var10000 = $class$java$lang$StringBuffer;
      if (var10000 == null) {
         var10000 = $class$java$lang$StringBuffer = class$("java.lang.StringBuffer");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$jline$Terminal() {
      Class var10000 = $class$jline$Terminal;
      if (var10000 == null) {
         var10000 = $class$jline$Terminal = class$("com.gzoltar.shaded.jline.Terminal");
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
