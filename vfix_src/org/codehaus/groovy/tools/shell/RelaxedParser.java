package org.codehaus.groovy.tools.shell;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovyjarjarantlr.collections.AST;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.util.Logger;

public final class RelaxedParser implements GroovyObject {
   private final Logger log;
   private SourceBuffer sourceBuffer;
   private String[] tokenNames;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204029L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204029 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$ParseCode;
   // $FF: synthetic field
   private static Class $class$java$io$StringReader;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Parser;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$antlr$SourceBuffer;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class array$$class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$antlr$parser$GroovyRecognizer;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$antlr$UnicodeEscapingReader;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$ParseStatus;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$RelaxedParser;
   // $FF: synthetic field
   private static Class $class$antlr$TokenStreamException;
   // $FF: synthetic field
   private static Class $class$antlr$RecognitionException;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$antlr$collections$AST;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$antlr$parser$GroovyLexer;

   public RelaxedParser() {
      CallSite[] var1 = $getCallSiteArray();
      this.log = (Logger)ScriptBytecodeAdapter.castToType((Logger)ScriptBytecodeAdapter.castToType(var1[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), (Object)var1[1].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$util$Logger()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public ParseStatus parse(List param1) {
      // $FF: Couldn't be decompiled
   }

   protected AST doParse(UnicodeEscapingReader reader) throws Exception {
      CallSite[] var2 = $getCallSiteArray();
      Object lexer = var2[22].callConstructor($get$$class$org$codehaus$groovy$antlr$parser$GroovyLexer(), (Object)reader);
      var2[23].call(reader, (Object)lexer);
      Object parser = var2[24].call($get$$class$org$codehaus$groovy$antlr$parser$GroovyRecognizer(), (Object)lexer);
      var2[25].call(parser, (Object)this.sourceBuffer);
      this.tokenNames = (String[])ScriptBytecodeAdapter.castToType((String[])ScriptBytecodeAdapter.castToType(var2[26].call(parser), $get$array$$class$java$lang$String()), $get$array$$class$java$lang$String());
      var2[27].call(parser);
      return (AST)ScriptBytecodeAdapter.castToType(var2[28].call(parser), $get$$class$antlr$collections$AST());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$RelaxedParser()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$RelaxedParser();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$RelaxedParser(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$RelaxedParser(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "create";
      var0[1] = "class";
      var0[2] = "<$constructor$>";
      var0[3] = "join";
      var0[4] = "NEWLINE";
      var0[5] = "debug";
      var0[6] = "doParse";
      var0[7] = "<$constructor$>";
      var0[8] = "<$constructor$>";
      var0[9] = "debug";
      var0[10] = "<$constructor$>";
      var0[11] = "COMPLETE";
      var0[12] = "class";
      var0[13] = "debug";
      var0[14] = "name";
      var0[15] = "class";
      var0[16] = "<$constructor$>";
      var0[17] = "INCOMPLETE";
      var0[18] = "debug";
      var0[19] = "name";
      var0[20] = "class";
      var0[21] = "<$constructor$>";
      var0[22] = "<$constructor$>";
      var0[23] = "setLexer";
      var0[24] = "make";
      var0[25] = "setSourceBuffer";
      var0[26] = "getTokenNames";
      var0[27] = "compilationUnit";
      var0[28] = "getAST";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[29];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$RelaxedParser(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$ParseCode() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$ParseCode;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$ParseCode = class$("org.codehaus.groovy.tools.shell.ParseCode");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$StringReader() {
      Class var10000 = $class$java$io$StringReader;
      if (var10000 == null) {
         var10000 = $class$java$io$StringReader = class$("java.io.StringReader");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Parser() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Parser;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Parser = class$("org.codehaus.groovy.tools.shell.Parser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$antlr$SourceBuffer() {
      Class var10000 = $class$org$codehaus$groovy$antlr$SourceBuffer;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$antlr$SourceBuffer = class$("org.codehaus.groovy.antlr.SourceBuffer");
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
   private static Class $get$array$$class$java$lang$String() {
      Class var10000 = array$$class$java$lang$String;
      if (var10000 == null) {
         var10000 = array$$class$java$lang$String = class$("[Ljava.lang.String;");
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
   private static Class $get$$class$org$codehaus$groovy$antlr$parser$GroovyRecognizer() {
      Class var10000 = $class$org$codehaus$groovy$antlr$parser$GroovyRecognizer;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$antlr$parser$GroovyRecognizer = class$("org.codehaus.groovy.antlr.parser.GroovyRecognizer");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$antlr$UnicodeEscapingReader() {
      Class var10000 = $class$org$codehaus$groovy$antlr$UnicodeEscapingReader;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$antlr$UnicodeEscapingReader = class$("org.codehaus.groovy.antlr.UnicodeEscapingReader");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$ParseStatus() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$ParseStatus;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$ParseStatus = class$("org.codehaus.groovy.tools.shell.ParseStatus");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$RelaxedParser() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$RelaxedParser;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$RelaxedParser = class$("org.codehaus.groovy.tools.shell.RelaxedParser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$antlr$TokenStreamException() {
      Class var10000 = $class$antlr$TokenStreamException;
      if (var10000 == null) {
         var10000 = $class$antlr$TokenStreamException = class$("groovyjarjarantlr.TokenStreamException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$antlr$RecognitionException() {
      Class var10000 = $class$antlr$RecognitionException;
      if (var10000 == null) {
         var10000 = $class$antlr$RecognitionException = class$("groovyjarjarantlr.RecognitionException");
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
   private static Class $get$$class$antlr$collections$AST() {
      Class var10000 = $class$antlr$collections$AST;
      if (var10000 == null) {
         var10000 = $class$antlr$collections$AST = class$("groovyjarjarantlr.collections.AST");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$antlr$parser$GroovyLexer() {
      Class var10000 = $class$org$codehaus$groovy$antlr$parser$GroovyLexer;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$antlr$parser$GroovyLexer = class$("org.codehaus.groovy.antlr.parser.GroovyLexer");
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
