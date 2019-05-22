package org.codehaus.groovy.tools.groovydoc;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Properties;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.IO;
import org.codehaus.groovy.tools.shell.util.MessageSource;

public class Main implements GroovyObject {
   private static final MessageSource messages;
   private static File styleSheetFile;
   private static File overviewFile;
   private static File destDir;
   private static String windowTitle;
   private static String docTitle;
   private static String header;
   private static String footer;
   private static Boolean author;
   private static Boolean noScripts;
   private static Boolean noMainForScripts;
   private static Boolean privateScope;
   private static Boolean packageScope;
   private static Boolean publicScope;
   private static Boolean protectedScope;
   private static Boolean debug;
   private static String[] sourcepath;
   private static List<String> sourceFilesToDoc;
   private static List<String> remainingArgs;
   private static List<String> exclusions;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203900L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203900 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$groovydoc$Main;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$groovydoc$FileOutputTool;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$groovydoc$ClasspathResourceManager;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$groovydoc$GroovyDocTool;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$IO$Verbosity;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class array$$class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$MessageSource;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$java$util$ArrayList;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$groovydoc$gstringTemplates$GroovyDocTemplateInfo;
   // $FF: synthetic field
   private static Class $class$groovy$util$CliBuilder;
   // $FF: synthetic field
   private static Class $class$java$util$Properties;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$IO;

   public Main() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      IO io = var1[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$IO());
      ScriptBytecodeAdapter.setProperty(io, $get$$class$org$codehaus$groovy$tools$groovydoc$Main(), $get$$class$org$codehaus$groovy$tools$shell$util$Logger(), "io");
      Object cli = var1[1].callConstructor($get$$class$groovy$util$CliBuilder(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"usage", "groovydoc [options] [packagenames] [sourcefiles]", "writer", var1[2].callGetProperty(io), "posix", Boolean.FALSE}));
      var1[3].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "help"}), var1[4].call(messages, (Object)"cli.option.help.description"));
      var1[5].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "version"}), var1[6].call(messages, (Object)"cli.option.version.description"));
      var1[7].call(cli, var1[8].call(messages, (Object)"cli.option.verbose.description"));
      var1[9].call(cli, var1[10].call(messages, (Object)"cli.option.quiet.description"));
      var1[11].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "debug"}), var1[12].call(messages, (Object)"cli.option.debug.description"));
      var1[13].call(cli, var1[14].call(messages, (Object)"cli.option.classpath.description"));
      var1[15].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "classpath"}), var1[16].call(messages, (Object)"cli.option.cp.description"));
      var1[17].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "destdir", "args", $const$0, "argName", "dir"}), var1[18].call(messages, (Object)"cli.option.destdir.description"));
      var1[19].call(cli, var1[20].call(messages, (Object)"cli.option.author.description"));
      var1[21].call(cli, var1[22].call(messages, (Object)"cli.option.noscripts.description"));
      var1[23].call(cli, var1[24].call(messages, (Object)"cli.option.nomainforscripts.description"));
      var1[25].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"args", $const$0, "argName", "file"}), var1[26].call(messages, (Object)"cli.option.overview.description"));
      var1[27].call(cli, var1[28].call(messages, (Object)"cli.option.public.description"));
      var1[29].call(cli, var1[30].call(messages, (Object)"cli.option.protected.description"));
      var1[31].call(cli, var1[32].call(messages, (Object)"cli.option.package.description"));
      var1[33].call(cli, var1[34].call(messages, (Object)"cli.option.private.description"));
      var1[35].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"args", $const$0, "argName", "text"}), var1[36].call(messages, (Object)"cli.option.windowtitle.description"));
      var1[37].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"args", $const$0, "argName", "html"}), var1[38].call(messages, (Object)"cli.option.doctitle.description"));
      var1[39].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"args", $const$0, "argName", "html"}), var1[40].call(messages, (Object)"cli.option.header.description"));
      var1[41].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"args", $const$0, "argName", "html"}), var1[42].call(messages, (Object)"cli.option.footer.description"));
      var1[43].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"args", $const$0, "argName", "pkglist"}), var1[44].call(messages, (Object)"cli.option.exclude.description"));
      var1[45].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"args", $const$0, "argName", "path"}), var1[46].call(messages, (Object)"cli.option.stylesheetfile.description"));
      var1[47].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"args", $const$0, "argName", "pathlist"}), var1[48].call(messages, (Object)"cli.option.sourcepath.description"));
      Object options = var1[49].call(cli, (Object)args);
      if (DefaultTypeTransformation.booleanUnbox(var1[50].callGetProperty(options))) {
         var1[51].call(cli);
      } else if (DefaultTypeTransformation.booleanUnbox(var1[52].callGetProperty(options))) {
         var1[53].call(var1[54].callGetProperty(io), var1[55].call(messages, "cli.info.version", var1[56].callGetProperty($get$$class$org$codehaus$groovy$runtime$InvokerHelper())));
      } else {
         if (DefaultTypeTransformation.booleanUnbox(var1[57].callGetProperty(options))) {
            styleSheetFile = (File)var1[58].callConstructor($get$$class$java$io$File(), (Object)var1[59].callGetProperty(options));
         }

         if (DefaultTypeTransformation.booleanUnbox(var1[60].callGetProperty(options))) {
            overviewFile = (File)var1[61].callConstructor($get$$class$java$io$File(), (Object)var1[62].callGetProperty(options));
         }

         CallSite var10000 = var1[63];
         Class var10001 = $get$$class$java$io$File();
         Object var10002 = var1[64].callGetProperty(options);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = ".";
         }

         destDir = (File)var10000.callConstructor(var10001, (Object)var10002);
         if (DefaultTypeTransformation.booleanUnbox(var1[65].callGetProperty(options))) {
            exclusions = (List)((List)ScriptBytecodeAdapter.castToType(var1[66].call(var1[67].callGetProperty(options), (Object)":"), $get$$class$java$util$List()));
         }

         if (DefaultTypeTransformation.booleanUnbox(var1[68].callGetProperty(options))) {
            Object list = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
            var1[69].call(var1[70].callGetProperty(options), (Object)(new GeneratedClosure($get$$class$org$codehaus$groovy$tools$groovydoc$Main(), $get$$class$org$codehaus$groovy$tools$groovydoc$Main(), list) {
               private Reference<T> list;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure1;
               // $FF: synthetic field
               private static Class $class$java$io$File;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.list = (Reference)list;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(this.list.get(), var3[1].call(itx.get(), var3[2].callGetProperty($get$$class$java$io$File())));
               }

               public Object getList() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.list.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure1()) {
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
                  var0[0] = "addAll";
                  var0[1] = "tokenize";
                  var0[2] = "pathSeparator";
                  var0[3] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure1(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure1() {
                  Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure1;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure1 = class$("org.codehaus.groovy.tools.groovydoc.Main$_main_closure1");
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
            }));
            sourcepath = (String[])((String[])ScriptBytecodeAdapter.castToType(var1[71].call(list.get()), $get$array$$class$java$lang$String()));
         }

         Object var9 = var1[72].call($get$$class$java$lang$Boolean(), (Object)var1[73].callGetProperty(options));
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = Boolean.FALSE;
         }

         author = (Boolean)((Boolean)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$Boolean()));
         var9 = var1[74].call($get$$class$java$lang$Boolean(), (Object)var1[75].callGetProperty(options));
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = Boolean.FALSE;
         }

         noScripts = (Boolean)((Boolean)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$Boolean()));
         var9 = var1[76].call($get$$class$java$lang$Boolean(), (Object)var1[77].callGetProperty(options));
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = Boolean.FALSE;
         }

         noMainForScripts = (Boolean)((Boolean)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$Boolean()));
         var9 = var1[78].call($get$$class$java$lang$Boolean(), (Object)var1[79].callGetProperty(options));
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = Boolean.FALSE;
         }

         packageScope = (Boolean)((Boolean)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$Boolean()));
         var9 = var1[80].call($get$$class$java$lang$Boolean(), (Object)var1[81].callGetProperty(options));
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = Boolean.FALSE;
         }

         privateScope = (Boolean)((Boolean)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$Boolean()));
         var9 = var1[82].call($get$$class$java$lang$Boolean(), (Object)var1[83].callGetProperty(options));
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = Boolean.FALSE;
         }

         protectedScope = (Boolean)((Boolean)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$Boolean()));
         var9 = var1[84].call($get$$class$java$lang$Boolean(), (Object)var1[85].callGetProperty(options));
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = Boolean.FALSE;
         }

         publicScope = (Boolean)((Boolean)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$Boolean()));
         Integer scopeCount = $const$1;
         if (DefaultTypeTransformation.booleanUnbox(packageScope)) {
            scopeCount = var1[86].call(scopeCount);
         }

         if (DefaultTypeTransformation.booleanUnbox(privateScope)) {
            scopeCount = var1[87].call(scopeCount);
         }

         if (DefaultTypeTransformation.booleanUnbox(protectedScope)) {
            scopeCount = var1[88].call(scopeCount);
         }

         if (DefaultTypeTransformation.booleanUnbox(publicScope)) {
            scopeCount = var1[89].call(scopeCount);
         }

         if (ScriptBytecodeAdapter.compareEqual(scopeCount, $const$1)) {
            protectedScope = (Boolean)Boolean.TRUE;
         } else if (ScriptBytecodeAdapter.compareGreaterThan(scopeCount, $const$0)) {
            var1[90].call(var1[91].callGetProperty($get$$class$java$lang$System()), (Object)"groovydoc: Error - More than one of -public, -private, -package, or -protected specified.");
            var1[92].call(cli);
            return;
         }

         var9 = var1[93].callGetProperty(options);
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = "";
         }

         windowTitle = (String)((String)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$String()));
         var9 = var1[94].callGetProperty(options);
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = "";
         }

         docTitle = (String)((String)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$String()));
         var9 = var1[95].callGetProperty(options);
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = "";
         }

         header = (String)((String)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$String()));
         var9 = var1[96].callGetProperty(options);
         if (!DefaultTypeTransformation.booleanUnbox(var9)) {
            var9 = "";
         }

         footer = (String)((String)ScriptBytecodeAdapter.castToType(var9, $get$$class$java$lang$String()));
         if (DefaultTypeTransformation.booleanUnbox(var1[97].callGetProperty(options))) {
            Object values = var1[98].callGetProperty(options);
            var1[99].call(values, (Object)(new GeneratedClosure($get$$class$org$codehaus$groovy$tools$groovydoc$Main(), $get$$class$org$codehaus$groovy$tools$groovydoc$Main()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure2;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$java$lang$String;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.asType(itx.get(), $get$$class$java$lang$String()), $get$$class$java$lang$String()));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure2()) {
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
                  var0[0] = "setSystemProperty";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure2(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure2() {
                  Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure2;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_main_closure2 = class$("org.codehaus.groovy.tools.groovydoc.Main$_main_closure2");
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
         }

         if (DefaultTypeTransformation.booleanUnbox(var1[100].callGetProperty(options))) {
            ScriptBytecodeAdapter.setProperty(var1[101].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$IO$Verbosity()), $get$$class$org$codehaus$groovy$tools$groovydoc$Main(), io, "verbosity");
         }

         if (DefaultTypeTransformation.booleanUnbox(var1[102].callGetProperty(options))) {
            ScriptBytecodeAdapter.setProperty(var1[103].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$IO$Verbosity()), $get$$class$org$codehaus$groovy$tools$groovydoc$Main(), io, "verbosity");
            debug = (Boolean)Boolean.TRUE;
         }

         if (DefaultTypeTransformation.booleanUnbox(var1[104].callGetProperty(options))) {
            ScriptBytecodeAdapter.setProperty(var1[105].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$IO$Verbosity()), $get$$class$org$codehaus$groovy$tools$groovydoc$Main(), io, "verbosity");
         }

         remainingArgs = (List)((List)ScriptBytecodeAdapter.castToType(var1[106].call(options), $get$$class$java$util$List()));
         if (!DefaultTypeTransformation.booleanUnbox(remainingArgs)) {
            var1[107].call(var1[108].callGetProperty($get$$class$java$lang$System()), (Object)"groovydoc: Error - No packages or classes specified.");
            var1[109].call(cli);
         } else {
            var1[110].callStatic($get$$class$org$codehaus$groovy$tools$groovydoc$Main());
         }
      }
   }

   public static void execute() {
      CallSite[] var0 = $getCallSiteArray();
      Properties properties = var0[111].callConstructor($get$$class$java$util$Properties());
      var0[112].call(properties, "windowTitle", windowTitle);
      var0[113].call(properties, "docTitle", docTitle);
      var0[114].call(properties, "footer", footer);
      var0[115].call(properties, "header", header);
      var0[116].call(properties, "privateScope", var0[117].call(privateScope));
      var0[118].call(properties, "protectedScope", var0[119].call(protectedScope));
      var0[120].call(properties, "publicScope", var0[121].call(publicScope));
      var0[122].call(properties, "packageScope", var0[123].call(packageScope));
      var0[124].call(properties, "author", var0[125].call(author));
      var0[126].call(properties, "processScripts", var0[127].call(!DefaultTypeTransformation.booleanUnbox(noScripts) ? Boolean.TRUE : Boolean.FALSE));
      var0[128].call(properties, "includeMainForScripts", var0[129].call(!DefaultTypeTransformation.booleanUnbox(noMainForScripts) ? Boolean.TRUE : Boolean.FALSE));
      CallSite var10000 = var0[130];
      Object var10003 = var0[131].callGetPropertySafe(overviewFile);
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = "";
      }

      var10000.call(properties, "overviewFile", var10003);
      Object links = var0[132].callConstructor($get$$class$java$util$ArrayList());
      var0[133].callStatic($get$$class$org$codehaus$groovy$tools$groovydoc$Main(), remainingArgs, sourcepath, exclusions);
      GroovyDocTool htmlTool = var0[134].callConstructor($get$$class$org$codehaus$groovy$tools$groovydoc$GroovyDocTool(), (Object[])ArrayUtil.createArray(var0[135].callConstructor($get$$class$org$codehaus$groovy$tools$groovydoc$ClasspathResourceManager()), sourcepath, var0[136].callGetProperty($get$$class$org$codehaus$groovy$tools$groovydoc$gstringTemplates$GroovyDocTemplateInfo()), var0[137].callGetProperty($get$$class$org$codehaus$groovy$tools$groovydoc$gstringTemplates$GroovyDocTemplateInfo()), var0[138].callGetProperty($get$$class$org$codehaus$groovy$tools$groovydoc$gstringTemplates$GroovyDocTemplateInfo()), links, properties));
      var0[139].call(htmlTool, (Object)sourceFilesToDoc);
      FileOutputTool output = var0[140].callConstructor($get$$class$org$codehaus$groovy$tools$groovydoc$FileOutputTool());
      var0[141].call(htmlTool, output, var0[142].callGetProperty(destDir));
      if (ScriptBytecodeAdapter.compareNotEqual(styleSheetFile, (Object)null)) {
         try {
            ScriptBytecodeAdapter.setProperty(var0[143].callGetProperty(styleSheetFile), $get$$class$org$codehaus$groovy$tools$groovydoc$Main(), var0[144].callConstructor($get$$class$java$io$File(), destDir, "stylesheet.css"), "text");
         } catch (IOException var8) {
            var0[145].callStatic($get$$class$org$codehaus$groovy$tools$groovydoc$Main(), var0[146].call(var0[147].call(var0[148].call("Warning: Unable to copy specified stylesheet '", (Object)var0[149].callGetProperty(styleSheetFile)), (Object)"'. Using default stylesheet instead. Due to: "), var0[150].callGetProperty(var8)));
         } finally {
            ;
         }
      }

   }

   public static Object collectSourceFileNames(List<String> remainingArgs, String[] sourceDirs, List<String> exclusions) {
      String[] sourceDirs = new Reference(sourceDirs);
      List exclusions = new Reference(exclusions);
      CallSite[] var5 = $getCallSiteArray();
      sourceFilesToDoc = (List)ScriptBytecodeAdapter.createList(new Object[0]);
      return var5[151].call(remainingArgs, (Object)(new GeneratedClosure($get$$class$org$codehaus$groovy$tools$groovydoc$Main(), $get$$class$org$codehaus$groovy$tools$groovydoc$Main(), sourceDirs, exclusions) {
         private Reference<T> sourceDirs;
         private Reference<T> exclusions;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3;
         // $FF: synthetic field
         private static Class $class$java$util$List;
         // $FF: synthetic field
         private static Class $class$java$io$File;
         // $FF: synthetic field
         private static Class array$$class$java$lang$String;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.sourceDirs = (Reference)sourceDirs;
            this.exclusions = (Reference)exclusions;
         }

         public Object doCall(String pkgOrFile) {
            String pkgOrFilex = new Reference(pkgOrFile);
            CallSite[] var3 = $getCallSiteArray();
            if (ScriptBytecodeAdapter.isCase(pkgOrFilex.get(), this.exclusions.get())) {
               return null;
            } else {
               File srcFile = var3[0].callConstructor($get$$class$java$io$File(), (Object)pkgOrFilex.get());
               if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var3[1].call(srcFile)) && DefaultTypeTransformation.booleanUnbox(var3[2].call(srcFile)) ? Boolean.TRUE : Boolean.FALSE)) {
                  var3[3].call(var3[4].callGroovyObjectGetProperty(this), pkgOrFilex.get());
                  return null;
               } else {
                  return var3[5].call(this.sourceDirs.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), pkgOrFilex) {
                     private Reference<T> pkgOrFile;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4;
                     // $FF: synthetic field
                     private static Class $class$java$lang$String;
                     // $FF: synthetic field
                     private static Class $class$groovy$io$FileType;
                     // $FF: synthetic field
                     private static Class $class$java$io$File;

                     public {
                        CallSite[] var4 = $getCallSiteArray();
                        this.pkgOrFile = (Reference)pkgOrFile;
                     }

                     public Object doCall(Object dirStr) {
                        Object dirStrx = new Reference(dirStr);
                        CallSite[] var3 = $getCallSiteArray();
                        Object dir = var3[0].callConstructor($get$$class$java$io$File(), (Object)dirStrx.get());
                        Object pkgOrFileSlashes = new Reference(var3[1].call(this.pkgOrFile.get(), ".", "/"));
                        Object candidate = new Reference(var3[2].callConstructor($get$$class$java$io$File(), dir, this.pkgOrFile.get()));
                        if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var3[3].call(candidate.get())) && DefaultTypeTransformation.booleanUnbox(var3[4].call(candidate.get())) ? Boolean.TRUE : Boolean.FALSE)) {
                           var3[5].call(var3[6].callGroovyObjectGetProperty(this), this.pkgOrFile.get());
                        }

                        candidate.set(var3[7].callConstructor($get$$class$java$io$File(), dir, pkgOrFileSlashes.get()));
                        return DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var3[8].call(candidate.get())) && DefaultTypeTransformation.booleanUnbox(var3[9].call(candidate.get())) ? Boolean.TRUE : Boolean.FALSE) ? var3[10].call(candidate.get(), var3[11].callGetProperty($get$$class$groovy$io$FileType()), ScriptBytecodeAdapter.bitwiseNegate(".*\\.(?:groovy|java)"), new GeneratedClosure(this, this.getThisObject(), pkgOrFileSlashes) {
                           private Reference<T> pkgOrFileSlashes;
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4_closure5;

                           public {
                              CallSite[] var4 = $getCallSiteArray();
                              this.pkgOrFileSlashes = (Reference)pkgOrFileSlashes;
                           }

                           public Object doCall(File f) {
                              File fx = new Reference(f);
                              CallSite[] var3 = $getCallSiteArray();
                              return var3[0].call(var3[1].callGroovyObjectGetProperty(this), var3[2].call(var3[3].call(this.pkgOrFileSlashes.get(), (Object)"/"), var3[4].call(fx.get())));
                           }

                           public Object call(File f) {
                              File fx = new Reference(f);
                              CallSite[] var3 = $getCallSiteArray();
                              return var3[5].callCurrent(this, (Object)fx.get());
                           }

                           public Object getPkgOrFileSlashes() {
                              CallSite[] var1 = $getCallSiteArray();
                              return this.pkgOrFileSlashes.get();
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4_closure5()) {
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
                              var0[0] = "leftShift";
                              var0[1] = "sourceFilesToDoc";
                              var0[2] = "plus";
                              var0[3] = "plus";
                              var0[4] = "getName";
                              var0[5] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[6];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4_closure5(), var0);
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
                           private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4_closure5() {
                              Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4_closure5;
                              if (var10000 == null) {
                                 var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4_closure5 = class$("org.codehaus.groovy.tools.groovydoc.Main$_collectSourceFileNames_closure3_closure4_closure5");
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
                        }) : null;
                     }

                     public String getPkgOrFile() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (String)ScriptBytecodeAdapter.castToType(this.pkgOrFile.get(), $get$$class$java$lang$String());
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4()) {
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
                        var0[1] = "replace";
                        var0[2] = "<$constructor$>";
                        var0[3] = "exists";
                        var0[4] = "isFile";
                        var0[5] = "leftShift";
                        var0[6] = "sourceFilesToDoc";
                        var0[7] = "<$constructor$>";
                        var0[8] = "exists";
                        var0[9] = "isDirectory";
                        var0[10] = "eachFileMatch";
                        var0[11] = "FILES";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[12];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4(), var0);
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
                     private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4() {
                        Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4;
                        if (var10000 == null) {
                           var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3_closure4 = class$("org.codehaus.groovy.tools.groovydoc.Main$_collectSourceFileNames_closure3_closure4");
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
                     private static Class $get$$class$groovy$io$FileType() {
                        Class var10000 = $class$groovy$io$FileType;
                        if (var10000 == null) {
                           var10000 = $class$groovy$io$FileType = class$("groovy.io.FileType");
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
                  }));
               }
            }
         }

         public Object call(String pkgOrFile) {
            String pkgOrFilex = new Reference(pkgOrFile);
            CallSite[] var3 = $getCallSiteArray();
            return var3[6].callCurrent(this, (Object)pkgOrFilex.get());
         }

         public String[] getSourceDirs() {
            CallSite[] var1 = $getCallSiteArray();
            return (String[])ScriptBytecodeAdapter.castToType(this.sourceDirs.get(), $get$array$$class$java$lang$String());
         }

         public List<String> getExclusions() {
            CallSite[] var1 = $getCallSiteArray();
            return (List)ScriptBytecodeAdapter.castToType(this.exclusions.get(), $get$$class$java$util$List());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3()) {
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
            var0[1] = "exists";
            var0[2] = "isFile";
            var0[3] = "leftShift";
            var0[4] = "sourceFilesToDoc";
            var0[5] = "each";
            var0[6] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[7];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3() {
            Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main$_collectSourceFileNames_closure3 = class$("org.codehaus.groovy.tools.groovydoc.Main$_collectSourceFileNames_closure3");
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
         private static Class $get$$class$java$io$File() {
            Class var10000 = $class$java$io$File;
            if (var10000 == null) {
               var10000 = $class$java$io$File = class$("java.io.File");
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
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$groovydoc$Main()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$groovydoc$Main();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$groovydoc$Main(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$groovydoc$Main(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      debug = (Boolean)Boolean.FALSE;
      messages = (MessageSource)$getCallSiteArray()[152].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$MessageSource(), (Object)$get$$class$org$codehaus$groovy$tools$groovydoc$Main());
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
      var0[3] = "help";
      var0[4] = "getAt";
      var0[5] = "_";
      var0[6] = "getAt";
      var0[7] = "verbose";
      var0[8] = "getAt";
      var0[9] = "quiet";
      var0[10] = "getAt";
      var0[11] = "_";
      var0[12] = "getAt";
      var0[13] = "classpath";
      var0[14] = "getAt";
      var0[15] = "cp";
      var0[16] = "getAt";
      var0[17] = "d";
      var0[18] = "getAt";
      var0[19] = "author";
      var0[20] = "getAt";
      var0[21] = "noscripts";
      var0[22] = "getAt";
      var0[23] = "nomainforscripts";
      var0[24] = "getAt";
      var0[25] = "overview";
      var0[26] = "getAt";
      var0[27] = "public";
      var0[28] = "getAt";
      var0[29] = "protected";
      var0[30] = "getAt";
      var0[31] = "package";
      var0[32] = "getAt";
      var0[33] = "private";
      var0[34] = "getAt";
      var0[35] = "windowtitle";
      var0[36] = "getAt";
      var0[37] = "doctitle";
      var0[38] = "getAt";
      var0[39] = "header";
      var0[40] = "getAt";
      var0[41] = "footer";
      var0[42] = "getAt";
      var0[43] = "exclude";
      var0[44] = "getAt";
      var0[45] = "stylesheetfile";
      var0[46] = "getAt";
      var0[47] = "sourcepath";
      var0[48] = "getAt";
      var0[49] = "parse";
      var0[50] = "help";
      var0[51] = "usage";
      var0[52] = "version";
      var0[53] = "println";
      var0[54] = "out";
      var0[55] = "format";
      var0[56] = "version";
      var0[57] = "stylesheetfile";
      var0[58] = "<$constructor$>";
      var0[59] = "stylesheetfile";
      var0[60] = "overview";
      var0[61] = "<$constructor$>";
      var0[62] = "overview";
      var0[63] = "<$constructor$>";
      var0[64] = "d";
      var0[65] = "exclude";
      var0[66] = "tokenize";
      var0[67] = "exclude";
      var0[68] = "sourcepath";
      var0[69] = "each";
      var0[70] = "sourcepaths";
      var0[71] = "toArray";
      var0[72] = "valueOf";
      var0[73] = "author";
      var0[74] = "valueOf";
      var0[75] = "noscripts";
      var0[76] = "valueOf";
      var0[77] = "nomainforscripts";
      var0[78] = "valueOf";
      var0[79] = "package";
      var0[80] = "valueOf";
      var0[81] = "private";
      var0[82] = "valueOf";
      var0[83] = "protected";
      var0[84] = "valueOf";
      var0[85] = "public";
      var0[86] = "next";
      var0[87] = "next";
      var0[88] = "next";
      var0[89] = "next";
      var0[90] = "println";
      var0[91] = "err";
      var0[92] = "usage";
      var0[93] = "windowtitle";
      var0[94] = "doctitle";
      var0[95] = "header";
      var0[96] = "footer";
      var0[97] = "Ds";
      var0[98] = "Ds";
      var0[99] = "each";
      var0[100] = "verbose";
      var0[101] = "VERBOSE";
      var0[102] = "debug";
      var0[103] = "DEBUG";
      var0[104] = "quiet";
      var0[105] = "QUIET";
      var0[106] = "arguments";
      var0[107] = "println";
      var0[108] = "err";
      var0[109] = "usage";
      var0[110] = "execute";
      var0[111] = "<$constructor$>";
      var0[112] = "put";
      var0[113] = "put";
      var0[114] = "put";
      var0[115] = "put";
      var0[116] = "put";
      var0[117] = "toString";
      var0[118] = "put";
      var0[119] = "toString";
      var0[120] = "put";
      var0[121] = "toString";
      var0[122] = "put";
      var0[123] = "toString";
      var0[124] = "put";
      var0[125] = "toString";
      var0[126] = "put";
      var0[127] = "toString";
      var0[128] = "put";
      var0[129] = "toString";
      var0[130] = "put";
      var0[131] = "absolutePath";
      var0[132] = "<$constructor$>";
      var0[133] = "collectSourceFileNames";
      var0[134] = "<$constructor$>";
      var0[135] = "<$constructor$>";
      var0[136] = "DEFAULT_DOC_TEMPLATES";
      var0[137] = "DEFAULT_PACKAGE_TEMPLATES";
      var0[138] = "DEFAULT_CLASS_TEMPLATES";
      var0[139] = "add";
      var0[140] = "<$constructor$>";
      var0[141] = "renderToOutput";
      var0[142] = "canonicalPath";
      var0[143] = "text";
      var0[144] = "<$constructor$>";
      var0[145] = "println";
      var0[146] = "plus";
      var0[147] = "plus";
      var0[148] = "plus";
      var0[149] = "absolutePath";
      var0[150] = "message";
      var0[151] = "each";
      var0[152] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[153];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$groovydoc$Main(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$Main() {
      Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$groovydoc$Main = class$("org.codehaus.groovy.tools.groovydoc.Main");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$FileOutputTool() {
      Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$FileOutputTool;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$groovydoc$FileOutputTool = class$("org.codehaus.groovy.tools.groovydoc.FileOutputTool");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$ClasspathResourceManager() {
      Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$ClasspathResourceManager;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$groovydoc$ClasspathResourceManager = class$("org.codehaus.groovy.tools.groovydoc.ClasspathResourceManager");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$GroovyDocTool() {
      Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$GroovyDocTool;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$groovydoc$GroovyDocTool = class$("org.codehaus.groovy.tools.groovydoc.GroovyDocTool");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$IO$Verbosity() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$IO$Verbosity;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$IO$Verbosity = class$("org.codehaus.groovy.tools.shell.IO$Verbosity");
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
   private static Class $get$array$$class$java$lang$String() {
      Class var10000 = array$$class$java$lang$String;
      if (var10000 == null) {
         var10000 = array$$class$java$lang$String = class$("[Ljava.lang.String;");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$MessageSource() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$MessageSource;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$MessageSource = class$("org.codehaus.groovy.tools.shell.util.MessageSource");
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
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$ArrayList() {
      Class var10000 = $class$java$util$ArrayList;
      if (var10000 == null) {
         var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
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
   private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
      Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$groovydoc$gstringTemplates$GroovyDocTemplateInfo() {
      Class var10000 = $class$org$codehaus$groovy$tools$groovydoc$gstringTemplates$GroovyDocTemplateInfo;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$groovydoc$gstringTemplates$GroovyDocTemplateInfo = class$("org.codehaus.groovy.tools.groovydoc.gstringTemplates.GroovyDocTemplateInfo");
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
   private static Class $get$$class$java$util$Properties() {
      Class var10000 = $class$java$util$Properties;
      if (var10000 == null) {
         var10000 = $class$java$util$Properties = class$("java.util.Properties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$IO() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$IO;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$IO = class$("org.codehaus.groovy.tools.shell.IO");
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
