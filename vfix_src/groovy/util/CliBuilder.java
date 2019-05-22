package groovy.util;

import com.gzoltar.shaded.org.apache.commons.cli.CommandLineParser;
import com.gzoltar.shaded.org.apache.commons.cli.HelpFormatter;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import com.gzoltar.shaded.org.apache.commons.cli.Options;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class CliBuilder implements GroovyObject {
   private String usage;
   private CommandLineParser parser;
   private boolean posix;
   private boolean expandArgumentFiles;
   private HelpFormatter formatter;
   private PrintWriter writer;
   private String header;
   private String footer;
   private boolean stopAtNonOption;
   private int width;
   private Options options;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)2;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203566L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203566 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$util$OptionAccessor;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$GnuParser;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$OptionBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$Options;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$HelpFormatter;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$CommandLineParser;
   // $FF: synthetic field
   private static Class array$$class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$Option;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$groovy$util$CliBuilder;
   // $FF: synthetic field
   private static Class $class$java$io$PrintWriter;
   // $FF: synthetic field
   private static Class $class$org$apache$commons$cli$PosixParser;

   public CliBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.usage = (String)ScriptBytecodeAdapter.castToType("groovy", $get$$class$java$lang$String());
      this.parser = (CommandLineParser)ScriptBytecodeAdapter.castToType((CommandLineParser)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$apache$commons$cli$CommandLineParser()), $get$$class$org$apache$commons$cli$CommandLineParser());
      this.posix = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
      this.expandArgumentFiles = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
      this.formatter = (HelpFormatter)ScriptBytecodeAdapter.castToType(var1[0].callConstructor($get$$class$org$apache$commons$cli$HelpFormatter()), $get$$class$org$apache$commons$cli$HelpFormatter());
      this.writer = (PrintWriter)ScriptBytecodeAdapter.castToType(var1[1].callConstructor($get$$class$java$io$PrintWriter(), (Object)var1[2].callGetProperty($get$$class$java$lang$System())), $get$$class$java$io$PrintWriter());
      this.header = (String)ScriptBytecodeAdapter.castToType("", $get$$class$java$lang$String());
      this.footer = (String)ScriptBytecodeAdapter.castToType("", $get$$class$java$lang$String());
      this.stopAtNonOption = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
      this.width = DefaultTypeTransformation.intUnbox(var1[3].callGetProperty(this.formatter));
      this.options = (Options)ScriptBytecodeAdapter.castToType(var1[4].callConstructor($get$$class$org$apache$commons$cli$Options()), $get$$class$org$apache$commons$cli$Options());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object invokeMethod(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      if (args instanceof Object[]) {
         if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var3[5].call(args), $const$0) && var3[6].call(args, (Object)$const$1) instanceof String ? Boolean.TRUE : Boolean.FALSE)) {
            var3[7].call(this.options, (Object)var3[8].callCurrent(this, name, ScriptBytecodeAdapter.createMap(new Object[0]), var3[9].call(args, (Object)$const$1)));
            return null;
         }

         if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var3[10].call(args), $const$0) && var3[11].call(args, (Object)$const$1) instanceof Option ? Boolean.TRUE : Boolean.FALSE) && ScriptBytecodeAdapter.compareEqual(name, "leftShift") ? Boolean.TRUE : Boolean.FALSE)) {
            var3[12].call(this.options, (Object)var3[13].call(args, (Object)$const$1));
            return null;
         }

         if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var3[14].call(args), $const$2) && var3[15].call(args, (Object)$const$1) instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
            var3[16].call(this.options, (Object)var3[17].callCurrent(this, name, var3[18].call(args, (Object)$const$1), var3[19].call(args, (Object)$const$0)));
            return null;
         }
      }

      return var3[20].call(var3[21].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), (Object)this), this, name, args);
   }

   public OptionAccessor parse(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public void usage() {
      CallSite[] var1 = $getCallSiteArray();
      var1[31].call(this.formatter, (Object[])ArrayUtil.createArray(this.writer, DefaultTypeTransformation.box(this.width), this.usage, this.header, this.options, var1[32].callGetProperty(this.formatter), var1[33].callGetProperty(this.formatter), this.footer));
      var1[34].call(this.writer);
   }

   public Option option(Object shortname, Map details, Object info) {
      CallSite[] var4 = $getCallSiteArray();
      Option option = new Reference((Option)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$apache$commons$cli$Option()));
      if (ScriptBytecodeAdapter.compareEqual(shortname, "_")) {
         option.set((Option)ScriptBytecodeAdapter.castToType(var4[35].call(var4[36].call(var4[37].call($get$$class$org$apache$commons$cli$OptionBuilder(), (Object)info), var4[38].callGetProperty(details))), $get$$class$org$apache$commons$cli$Option()));
         var4[39].call(details, (Object)"longOpt");
      } else {
         option.set(var4[40].callConstructor($get$$class$org$apache$commons$cli$Option(), shortname, info));
      }

      var4[41].call(details, (Object)(new GeneratedClosure(this, this, option) {
         private Reference<T> option;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$apache$commons$cli$Option;
         // $FF: synthetic field
         private static Class $class$groovy$util$CliBuilder$_option_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.option = (Reference)option;
         }

         public Object doCall(Object key, Object value) {
            Object keyx = new Reference(key);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            CallSite var10000 = var5[0];
            Object var10001 = this.option.get();
            Object var10002 = keyx.get();
            Object var6 = valuex.get();
            var10000.call(var10001, var10002, var6);
            return var6;
         }

         public Object call(Object key, Object value) {
            Object keyx = new Reference(key);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            return var5[1].callCurrent(this, keyx.get(), valuex.get());
         }

         public Option getOption() {
            CallSite[] var1 = $getCallSiteArray();
            return (Option)ScriptBytecodeAdapter.castToType(this.option.get(), $get$$class$org$apache$commons$cli$Option());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$CliBuilder$_option_closure1()) {
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
            var0[0] = "putAt";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$CliBuilder$_option_closure1(), var0);
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
         private static Class $get$$class$org$apache$commons$cli$Option() {
            Class var10000 = $class$org$apache$commons$cli$Option;
            if (var10000 == null) {
               var10000 = $class$org$apache$commons$cli$Option = class$("com.gzoltar.shaded.org.apache.commons.cli.Option");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$util$CliBuilder$_option_closure1() {
            Class var10000 = $class$groovy$util$CliBuilder$_option_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$util$CliBuilder$_option_closure1 = class$("groovy.util.CliBuilder$_option_closure1");
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
      return (Option)ScriptBytecodeAdapter.castToType(option.get(), $get$$class$org$apache$commons$cli$Option());
   }

   public static Object expandArgumentFiles(Object args) throws IOException {
      CallSite[] var1 = $getCallSiteArray();
      Object result = ScriptBytecodeAdapter.createList(new Object[0]);
      Object arg = null;
      Object var4 = var1[42].call(args);

      while(true) {
         while(((Iterator)var4).hasNext()) {
            arg = ((Iterator)var4).next();
            if (ScriptBytecodeAdapter.compareEqual(var1[43].call(arg, (Object)$const$1), "@")) {
               arg = var1[44].call(arg, (Object)$const$0);
               if (ScriptBytecodeAdapter.compareNotEqual(var1[45].call(arg, (Object)$const$1), "@")) {
                  var1[46].callStatic($get$$class$groovy$util$CliBuilder(), arg, result);
                  continue;
               }
            }

            var1[47].call(result, (Object)arg);
         }

         return result;
      }
   }

   private static Object expandArgumentFile(Object name, Object args) throws IOException {
      Object args = new Reference(args);
      CallSite[] var3 = $getCallSiteArray();
      Object charAsInt = new Reference(new GeneratedClosure($get$$class$groovy$util$CliBuilder(), $get$$class$groovy$util$CliBuilder()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$groovy$util$CliBuilder$_expandArgumentFile_closure2;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(String s) {
            String sx = new Reference(s);
            CallSite[] var3 = $getCallSiteArray();
            return (Integer)ScriptBytecodeAdapter.asType(var3[0].call(sx.get()), $get$$class$java$lang$Integer());
         }

         public Object call(String s) {
            String sx = new Reference(s);
            CallSite[] var3 = $getCallSiteArray();
            return var3[1].callCurrent(this, (Object)sx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure2()) {
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
            var0[0] = "toCharacter";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure2(), var0);
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
         private static Class $get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure2() {
            Class var10000 = $class$groovy$util$CliBuilder$_expandArgumentFile_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$util$CliBuilder$_expandArgumentFile_closure2 = class$("groovy.util.CliBuilder$_expandArgumentFile_closure2");
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
      });
      return var3[48].call(var3[49].callConstructor($get$$class$java$io$File(), (Object)name), (Object)(new GeneratedClosure($get$$class$groovy$util$CliBuilder(), $get$$class$groovy$util$CliBuilder(), args, charAsInt) {
         private Reference<T> args;
         private Reference<T> charAsInt;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$io$StreamTokenizer;
         // $FF: synthetic field
         private static Class $class$groovy$util$CliBuilder$_expandArgumentFile_closure3;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.args = (Reference)args;
            this.charAsInt = (Reference)charAsInt;
         }

         public Object doCall(Object r) {
            Object rx = new Reference(r);
            CallSite[] var3 = $getCallSiteArray();
            CallSite var10000 = var3[0];
            Object var10001 = var3[1].callConstructor($get$$class$java$io$StreamTokenizer(), (Object)rx.get());
            Object var10005 = this.getThisObject();
            Reference args = this.args;
            Reference charAsInt = this.charAsInt;
            return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, args, charAsInt) {
               private Reference<T> args;
               private Reference<T> charAsInt;
               // $FF: synthetic field
               private static final Integer $const$0 = (Integer)255;
               // $FF: synthetic field
               private static final Integer $const$1 = (Integer)0;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$java$io$StreamTokenizer;
               // $FF: synthetic field
               private static Class $class$groovy$util$CliBuilder$_expandArgumentFile_closure3_closure4;

               public {
                  Reference argsx = new Reference(args);
                  Reference charAsInt = new Reference(charAsIntx);
                  CallSite[] var7 = $getCallSiteArray();
                  this.args = (Reference)((Reference)argsx.get());
                  this.charAsInt = (Reference)((Reference)charAsInt.get());
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this);
                  var2[1].callCurrent(this, var2[2].call(this.charAsInt.get(), (Object)" "), $const$0);
                  var2[3].callCurrent(this, $const$1, var2[4].call(this.charAsInt.get(), (Object)" "));
                  var2[5].callCurrent(this, (Object)var2[6].call(this.charAsInt.get(), (Object)"#"));
                  var2[7].callCurrent(this, (Object)var2[8].call(this.charAsInt.get(), (Object)"\""));
                  var2[9].callCurrent(this, (Object)var2[10].call(this.charAsInt.get(), (Object)"'"));

                  while(ScriptBytecodeAdapter.compareNotEqual(var2[11].callCurrent(this), var2[12].callGetProperty($get$$class$java$io$StreamTokenizer()))) {
                     var2[13].call(this.args.get(), var2[14].callGroovyObjectGetProperty(this));
                  }

                  return null;
               }

               public Object getArgs() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.args.get();
               }

               public Object getCharAsInt() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.charAsInt.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[15].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure3_closure4()) {
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
                  var0[0] = "resetSyntax";
                  var0[1] = "wordChars";
                  var0[2] = "call";
                  var0[3] = "whitespaceChars";
                  var0[4] = "call";
                  var0[5] = "commentChar";
                  var0[6] = "call";
                  var0[7] = "quoteChar";
                  var0[8] = "call";
                  var0[9] = "quoteChar";
                  var0[10] = "call";
                  var0[11] = "nextToken";
                  var0[12] = "TT_EOF";
                  var0[13] = "leftShift";
                  var0[14] = "sval";
                  var0[15] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[16];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure3_closure4(), var0);
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
               private static Class $get$$class$java$io$StreamTokenizer() {
                  Class var10000 = $class$java$io$StreamTokenizer;
                  if (var10000 == null) {
                     var10000 = $class$java$io$StreamTokenizer = class$("java.io.StreamTokenizer");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure3_closure4() {
                  Class var10000 = $class$groovy$util$CliBuilder$_expandArgumentFile_closure3_closure4;
                  if (var10000 == null) {
                     var10000 = $class$groovy$util$CliBuilder$_expandArgumentFile_closure3_closure4 = class$("groovy.util.CliBuilder$_expandArgumentFile_closure3_closure4");
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

         public Object getArgs() {
            CallSite[] var1 = $getCallSiteArray();
            return this.args.get();
         }

         public Object getCharAsInt() {
            CallSite[] var1 = $getCallSiteArray();
            return this.charAsInt.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure3()) {
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
            var0[0] = "with";
            var0[1] = "<$constructor$>";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure3(), var0);
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
         private static Class $get$$class$java$io$StreamTokenizer() {
            Class var10000 = $class$java$io$StreamTokenizer;
            if (var10000 == null) {
               var10000 = $class$java$io$StreamTokenizer = class$("java.io.StreamTokenizer");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$util$CliBuilder$_expandArgumentFile_closure3() {
            Class var10000 = $class$groovy$util$CliBuilder$_expandArgumentFile_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$util$CliBuilder$_expandArgumentFile_closure3 = class$("groovy.util.CliBuilder$_expandArgumentFile_closure3");
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

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$util$CliBuilder()) {
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
      Class var10000 = $get$$class$groovy$util$CliBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$util$CliBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$util$CliBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public String getUsage() {
      return this.usage;
   }

   public void setUsage(String var1) {
      this.usage = var1;
   }

   public CommandLineParser getParser() {
      return this.parser;
   }

   public void setParser(CommandLineParser var1) {
      this.parser = var1;
   }

   public boolean getPosix() {
      return this.posix;
   }

   public boolean isPosix() {
      return this.posix;
   }

   public void setPosix(boolean var1) {
      this.posix = var1;
   }

   public boolean getExpandArgumentFiles() {
      return this.expandArgumentFiles;
   }

   public boolean isExpandArgumentFiles() {
      return this.expandArgumentFiles;
   }

   public void setExpandArgumentFiles(boolean var1) {
      this.expandArgumentFiles = var1;
   }

   public HelpFormatter getFormatter() {
      return this.formatter;
   }

   public void setFormatter(HelpFormatter var1) {
      this.formatter = var1;
   }

   public PrintWriter getWriter() {
      return this.writer;
   }

   public void setWriter(PrintWriter var1) {
      this.writer = var1;
   }

   public String getHeader() {
      return this.header;
   }

   public void setHeader(String var1) {
      this.header = var1;
   }

   public String getFooter() {
      return this.footer;
   }

   public void setFooter(String var1) {
      this.footer = var1;
   }

   public boolean getStopAtNonOption() {
      return this.stopAtNonOption;
   }

   public boolean isStopAtNonOption() {
      return this.stopAtNonOption;
   }

   public void setStopAtNonOption(boolean var1) {
      this.stopAtNonOption = var1;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }

   public Options getOptions() {
      return this.options;
   }

   public void setOptions(Options var1) {
      this.options = var1;
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
      var0[0] = "<$constructor$>";
      var0[1] = "<$constructor$>";
      var0[2] = "out";
      var0[3] = "defaultWidth";
      var0[4] = "<$constructor$>";
      var0[5] = "size";
      var0[6] = "getAt";
      var0[7] = "addOption";
      var0[8] = "option";
      var0[9] = "getAt";
      var0[10] = "size";
      var0[11] = "getAt";
      var0[12] = "addOption";
      var0[13] = "getAt";
      var0[14] = "size";
      var0[15] = "getAt";
      var0[16] = "addOption";
      var0[17] = "option";
      var0[18] = "getAt";
      var0[19] = "getAt";
      var0[20] = "invokeMethod";
      var0[21] = "getMetaClass";
      var0[22] = "expandArgumentFiles";
      var0[23] = "<$constructor$>";
      var0[24] = "<$constructor$>";
      var0[25] = "<$constructor$>";
      var0[26] = "parse";
      var0[27] = "println";
      var0[28] = "plus";
      var0[29] = "message";
      var0[30] = "usage";
      var0[31] = "printHelp";
      var0[32] = "defaultLeftPad";
      var0[33] = "defaultDescPad";
      var0[34] = "flush";
      var0[35] = "create";
      var0[36] = "withLongOpt";
      var0[37] = "withDescription";
      var0[38] = "longOpt";
      var0[39] = "remove";
      var0[40] = "<$constructor$>";
      var0[41] = "each";
      var0[42] = "iterator";
      var0[43] = "getAt";
      var0[44] = "substring";
      var0[45] = "getAt";
      var0[46] = "expandArgumentFile";
      var0[47] = "leftShift";
      var0[48] = "withReader";
      var0[49] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[50];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$util$CliBuilder(), var0);
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
   private static Class $get$$class$groovy$util$OptionAccessor() {
      Class var10000 = $class$groovy$util$OptionAccessor;
      if (var10000 == null) {
         var10000 = $class$groovy$util$OptionAccessor = class$("groovy.util.OptionAccessor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$commons$cli$GnuParser() {
      Class var10000 = $class$org$apache$commons$cli$GnuParser;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$GnuParser = class$("com.gzoltar.shaded.org.apache.commons.cli.GnuParser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$commons$cli$OptionBuilder() {
      Class var10000 = $class$org$apache$commons$cli$OptionBuilder;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$OptionBuilder = class$("com.gzoltar.shaded.org.apache.commons.cli.OptionBuilder");
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
   private static Class $get$$class$org$apache$commons$cli$Options() {
      Class var10000 = $class$org$apache$commons$cli$Options;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$Options = class$("com.gzoltar.shaded.org.apache.commons.cli.Options");
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
   private static Class $get$$class$org$apache$commons$cli$HelpFormatter() {
      Class var10000 = $class$org$apache$commons$cli$HelpFormatter;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$HelpFormatter = class$("com.gzoltar.shaded.org.apache.commons.cli.HelpFormatter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$commons$cli$CommandLineParser() {
      Class var10000 = $class$org$apache$commons$cli$CommandLineParser;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$CommandLineParser = class$("com.gzoltar.shaded.org.apache.commons.cli.CommandLineParser");
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
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$commons$cli$Option() {
      Class var10000 = $class$org$apache$commons$cli$Option;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$Option = class$("com.gzoltar.shaded.org.apache.commons.cli.Option");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
      Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$CliBuilder() {
      Class var10000 = $class$groovy$util$CliBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$util$CliBuilder = class$("groovy.util.CliBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$PrintWriter() {
      Class var10000 = $class$java$io$PrintWriter;
      if (var10000 == null) {
         var10000 = $class$java$io$PrintWriter = class$("java.io.PrintWriter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$commons$cli$PosixParser() {
      Class var10000 = $class$org$apache$commons$cli$PosixParser;
      if (var10000 == null) {
         var10000 = $class$org$apache$commons$cli$PosixParser = class$("com.gzoltar.shaded.org.apache.commons.cli.PosixParser");
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
