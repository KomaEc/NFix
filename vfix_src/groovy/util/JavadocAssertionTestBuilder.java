package groovy.util;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JavadocAssertionTestBuilder implements GroovyObject {
   private static Pattern javadocPattern = (Pattern)((Pattern)ScriptBytecodeAdapter.castToType($getCallSiteArray()[33].call($get$$class$java$util$regex$Pattern(), (Object)"(?ims)/\\*\\*.*?\\*/"), $get$$class$java$util$regex$Pattern()));
   private static Pattern assertionPattern = (Pattern)((Pattern)ScriptBytecodeAdapter.castToType($getCallSiteArray()[34].call($get$$class$java$util$regex$Pattern(), (Object)"(?ims)<([a-z]+)\\s+class\\s*=\\s*['\"]groovyTestCase['\"]\\s*>.*?<\\s*/\\s*\\1>"), $get$$class$java$util$regex$Pattern()));
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
   public static Long __timeStamp = (Long)1292524203649L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203649 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$util$regex$Pattern;
   // $FF: synthetic field
   private static Class $class$groovy$util$JavadocAssertionTestBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$java$util$ArrayList;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$java$util$LinkedHashMap;

   public JavadocAssertionTestBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Class buildTest(String filename, String code) {
      CallSite[] var3 = $getCallSiteArray();
      Class test = (Class)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Class());
      List assertionTags = (List)ScriptBytecodeAdapter.castToType(var3[0].callCurrent(this, (Object)code), $get$$class$java$util$List());
      if (DefaultTypeTransformation.booleanUnbox(assertionTags)) {
         String testName = (String)ScriptBytecodeAdapter.castToType(var3[1].callCurrent(this, (Object)filename), $get$$class$java$lang$String());
         Map lineNumberToAssertions = (Map)ScriptBytecodeAdapter.castToType(var3[2].callCurrent(this, code, assertionTags), $get$$class$java$util$Map());
         List testMethods = (List)ScriptBytecodeAdapter.castToType(var3[3].callCurrent(this, lineNumberToAssertions, filename), $get$$class$java$util$List());
         String testCode = (String)ScriptBytecodeAdapter.castToType(var3[4].callCurrent(this, testName, testMethods), $get$$class$java$lang$String());
         test = (Class)ScriptBytecodeAdapter.castToType(var3[5].callCurrent(this, (Object)testCode), $get$$class$java$lang$Class());
      }

      return (Class)ScriptBytecodeAdapter.castToType(test, $get$$class$java$lang$Class());
   }

   private List getAssertionTags(String code) {
      CallSite[] var2 = $getCallSiteArray();
      List assertions = new Reference(var2[6].callConstructor($get$$class$java$util$ArrayList()));
      var2[7].call(code, javadocPattern, new GeneratedClosure(this, this, assertions) {
         private Reference<T> assertions;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$util$JavadocAssertionTestBuilder$_getAssertionTags_closure1;
         // $FF: synthetic field
         private static Class $class$java$util$List;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.assertions = (Reference)assertions;
         }

         public Object doCall(Object javadoc) {
            Object javadocx = new Reference(javadoc);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(this.assertions.get(), var3[1].call(javadocx.get(), var3[2].callGroovyObjectGetProperty(this)));
         }

         public List getAssertions() {
            CallSite[] var1 = $getCallSiteArray();
            return (List)ScriptBytecodeAdapter.castToType(this.assertions.get(), $get$$class$java$util$List());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$JavadocAssertionTestBuilder$_getAssertionTags_closure1()) {
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
            var0[1] = "findAll";
            var0[2] = "assertionPattern";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$JavadocAssertionTestBuilder$_getAssertionTags_closure1(), var0);
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
         private static Class $get$$class$groovy$util$JavadocAssertionTestBuilder$_getAssertionTags_closure1() {
            Class var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getAssertionTags_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getAssertionTags_closure1 = class$("groovy.util.JavadocAssertionTestBuilder$_getAssertionTags_closure1");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      });
      return (List)ScriptBytecodeAdapter.castToType(assertions.get(), $get$$class$java$util$List());
   }

   private String getTestName(String filename) {
      CallSite[] var2 = $getCallSiteArray();
      String filenameWithoutPath = (String)ScriptBytecodeAdapter.castToType(var2[8].callGetProperty(var2[9].callConstructor($get$$class$java$io$File(), (Object)filename)), $get$$class$java$lang$String());
      String testName = (String)ScriptBytecodeAdapter.castToType(var2[10].call(var2[11].call(filenameWithoutPath, $const$0, var2[12].call(filenameWithoutPath, (Object)".")), (Object)"JavadocAssertionTest"), $get$$class$java$lang$String());
      return (String)ScriptBytecodeAdapter.castToType(testName, $get$$class$java$lang$String());
   }

   private Map getLineNumberToAssertionsMap(String code, List assertionTags) {
      String code = new Reference(code);
      CallSite[] var4 = $getCallSiteArray();
      Map lineNumberToAssertions = new Reference((LinkedHashMap)ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$java$util$LinkedHashMap()));
      Integer codeIndex = new Reference($const$0);
      var4[13].call(assertionTags, (Object)(new GeneratedClosure(this, this, lineNumberToAssertions, codeIndex, code) {
         private Reference<T> lineNumberToAssertions;
         private Reference<T> codeIndex;
         private Reference<T> code;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$groovy$util$JavadocAssertionTestBuilder$_getLineNumberToAssertionsMap_closure2;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$util$Map;

         public {
            CallSite[] var6 = $getCallSiteArray();
            this.lineNumberToAssertions = (Reference)lineNumberToAssertions;
            this.codeIndex = (Reference)codeIndex;
            this.code = (Reference)code;
         }

         public Object doCall(Object tag) {
            Object tagx = new Reference(tag);
            CallSite[] var3 = $getCallSiteArray();
            this.codeIndex.set((Integer)ScriptBytecodeAdapter.castToType(var3[0].call(this.code.get(), tagx.get(), this.codeIndex.get()), $get$$class$java$lang$Integer()));
            Integer lineNumber = (Integer)ScriptBytecodeAdapter.castToType(var3[1].call(var3[2].call(var3[3].call(this.code.get(), $const$0, this.codeIndex.get()), (Object)"(?m)^")), $get$$class$java$lang$Integer());
            this.codeIndex.set((Integer)ScriptBytecodeAdapter.castToType(var3[4].call(this.codeIndex.get(), var3[5].call(tagx.get())), $get$$class$java$lang$Integer()));
            String assertion = (String)ScriptBytecodeAdapter.castToType(var3[6].callCurrent(this, (Object)tagx.get()), $get$$class$java$lang$String());
            return var3[7].call(var3[8].call(this.lineNumberToAssertions.get(), lineNumber, ScriptBytecodeAdapter.createList(new Object[0])), (Object)assertion);
         }

         public Map getLineNumberToAssertions() {
            CallSite[] var1 = $getCallSiteArray();
            return (Map)ScriptBytecodeAdapter.castToType(this.lineNumberToAssertions.get(), $get$$class$java$util$Map());
         }

         public Integer getCodeIndex() {
            CallSite[] var1 = $getCallSiteArray();
            return (Integer)ScriptBytecodeAdapter.castToType(this.codeIndex.get(), $get$$class$java$lang$Integer());
         }

         public String getCode() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.code.get(), $get$$class$java$lang$String());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$JavadocAssertionTestBuilder$_getLineNumberToAssertionsMap_closure2()) {
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
            var0[0] = "indexOf";
            var0[1] = "size";
            var0[2] = "findAll";
            var0[3] = "substring";
            var0[4] = "plus";
            var0[5] = "size";
            var0[6] = "getAssertion";
            var0[7] = "leftShift";
            var0[8] = "get";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[9];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$JavadocAssertionTestBuilder$_getLineNumberToAssertionsMap_closure2(), var0);
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
         private static Class $get$$class$groovy$util$JavadocAssertionTestBuilder$_getLineNumberToAssertionsMap_closure2() {
            Class var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getLineNumberToAssertionsMap_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getLineNumberToAssertionsMap_closure2 = class$("groovy.util.JavadocAssertionTestBuilder$_getLineNumberToAssertionsMap_closure2");
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
         private static Class $get$$class$java$util$Map() {
            Class var10000 = $class$java$util$Map;
            if (var10000 == null) {
               var10000 = $class$java$util$Map = class$("java.util.Map");
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
      return (Map)ScriptBytecodeAdapter.castToType(lineNumberToAssertions.get(), $get$$class$java$util$Map());
   }

   private String getAssertion(String tag) {
      CallSite[] var2 = $getCallSiteArray();
      String tagInner = (String)ScriptBytecodeAdapter.castToType(var2[14].call(tag, var2[15].call(var2[16].call(tag, (Object)">"), (Object)$const$1), var2[17].call(tag, (Object)"<")), $get$$class$java$lang$String());
      String htmlAssertion = (String)ScriptBytecodeAdapter.castToType(var2[18].call(tagInner, "(?m)^\\s*\\*", ""), $get$$class$java$lang$String());
      String assertion = new Reference(htmlAssertion);
      var2[19].call(ScriptBytecodeAdapter.createMap(new Object[]{"nbsp", " ", "gt", ">", "lt", "<", "quot", "\"", "apos", "'", "at", "@", "ndash", "-", "amp", "&"}), (Object)(new GeneratedClosure(this, this, assertion) {
         private Reference<T> assertion;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$util$JavadocAssertionTestBuilder$_getAssertion_closure3;
         // $FF: synthetic field
         private static Class $class$java$lang$String;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.assertion = (Reference)assertion;
         }

         public Object doCall(Object key, Object value) {
            Object keyx = new Reference(key);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            String var10000 = (String)ScriptBytecodeAdapter.castToType(var5[0].call(this.assertion.get(), new GStringImpl(new Object[]{keyx.get()}, new String[]{"(?i)&", ";"}), valuex.get()), $get$$class$java$lang$String());
            this.assertion.set(var10000);
            return var10000;
         }

         public Object call(Object key, Object value) {
            Object keyx = new Reference(key);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            return var5[1].callCurrent(this, keyx.get(), valuex.get());
         }

         public String getAssertion() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.assertion.get(), $get$$class$java$lang$String());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$JavadocAssertionTestBuilder$_getAssertion_closure3()) {
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
            var0[0] = "replaceAll";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$JavadocAssertionTestBuilder$_getAssertion_closure3(), var0);
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
         private static Class $get$$class$groovy$util$JavadocAssertionTestBuilder$_getAssertion_closure3() {
            Class var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getAssertion_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getAssertion_closure3 = class$("groovy.util.JavadocAssertionTestBuilder$_getAssertion_closure3");
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
      return (String)ScriptBytecodeAdapter.castToType(assertion.get(), $get$$class$java$lang$String());
   }

   private List getTestMethods(Map lineNumberToAssertions, String filename) {
      String filename = new Reference(filename);
      CallSite[] var4 = $getCallSiteArray();
      List testMethods = (List)ScriptBytecodeAdapter.castToType(var4[20].call(var4[21].call(lineNumberToAssertions, (Object)(new GeneratedClosure(this, this, filename) {
         private Reference<T> filename;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4;
         // $FF: synthetic field
         private static Class $class$java$lang$Character;
         // $FF: synthetic field
         private static Class $class$java$lang$String;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.filename = (Reference)filename;
         }

         public Object doCall(Object lineNumber, Object assertions) {
            Object lineNumberx = new Reference(lineNumber);
            Object assertionsx = new Reference(assertions);
            CallSite[] var5 = $getCallSiteArray();
            Character differentiator = new Reference((Character)ScriptBytecodeAdapter.castToType("a", $get$$class$java$lang$Character()));
            CallSite var10000 = var5[0];
            Object var10001 = assertionsx.get();
            Object var10005 = this.getThisObject();
            Reference filename = this.filename;
            return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, differentiator, assertionsx, filename, lineNumberx) {
               private Reference<T> differentiator;
               private Reference<T> assertions;
               private Reference<T> filename;
               private Reference<T> lineNumber;
               // $FF: synthetic field
               private static final Integer $const$0 = (Integer)1;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Character;
               // $FF: synthetic field
               private static Class $class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4_closure5;
               // $FF: synthetic field
               private static Class $class$java$lang$String;

               public {
                  Reference filename = new Reference(filenamex);
                  CallSite[] var8 = $getCallSiteArray();
                  this.differentiator = (Reference)differentiator;
                  this.assertions = (Reference)assertions;
                  this.filename = (Reference)((Reference)filename.get());
                  this.lineNumber = (Reference)lineNumber;
               }

               public Object doCall(Object assertion) {
                  Object assertionx = new Reference(assertion);
                  CallSite[] var3 = $getCallSiteArray();
                  String suffix = (String)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareGreaterThan(var3[0].call(this.assertions.get()), $const$0) ? new GStringImpl(new Object[]{this.lineNumber.get(), this.differentiator.get()}, new String[]{"", "", ""}) : this.lineNumber.get(), $get$$class$java$lang$String());
                  Object var5 = this.differentiator.get();
                  this.differentiator.set(var3[1].call(this.differentiator.get()));
                  return var3[2].callCurrent(this, suffix, assertionx.get(), var3[3].callCurrent(this, (Object)this.filename.get()));
               }

               public Character getDifferentiator() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Character)ScriptBytecodeAdapter.castToType(this.differentiator.get(), $get$$class$java$lang$Character());
               }

               public Object getAssertions() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.assertions.get();
               }

               public String getFilename() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (String)ScriptBytecodeAdapter.castToType(this.filename.get(), $get$$class$java$lang$String());
               }

               public Object getLineNumber() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.lineNumber.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4_closure5()) {
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
                  var0[1] = "next";
                  var0[2] = "getTestMethodCodeForAssertion";
                  var0[3] = "basename";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4_closure5(), var0);
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
               private static Class $get$$class$java$lang$Character() {
                  Class var10000 = $class$java$lang$Character;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$Character = class$("java.lang.Character");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4_closure5() {
                  Class var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4_closure5;
                  if (var10000 == null) {
                     var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4_closure5 = class$("groovy.util.JavadocAssertionTestBuilder$_getTestMethods_closure4_closure5");
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

         public Object call(Object lineNumber, Object assertions) {
            Object lineNumberx = new Reference(lineNumber);
            Object assertionsx = new Reference(assertions);
            CallSite[] var5 = $getCallSiteArray();
            return var5[1].callCurrent(this, lineNumberx.get(), assertionsx.get());
         }

         public String getFilename() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.filename.get(), $get$$class$java$lang$String());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4()) {
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
            var0[0] = "collect";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4(), var0);
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
         private static Class $get$$class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4() {
            Class var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$util$JavadocAssertionTestBuilder$_getTestMethods_closure4 = class$("groovy.util.JavadocAssertionTestBuilder$_getTestMethods_closure4");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$lang$Character() {
            Class var10000 = $class$java$lang$Character;
            if (var10000 == null) {
               var10000 = $class$java$lang$Character = class$("java.lang.Character");
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
      }))), $get$$class$java$util$List());
      return (List)ScriptBytecodeAdapter.castToType(testMethods, $get$$class$java$util$List());
   }

   private String basename(String fullPath) {
      CallSite[] var2 = $getCallSiteArray();
      Object path = var2[22].callConstructor($get$$class$java$io$File(), (Object)fullPath);
      Object fullName = var2[23].callGetProperty(path);
      return (String)ScriptBytecodeAdapter.castToType(var2[24].call(fullName, $const$0, var2[25].call(fullName, (Object)".")), $get$$class$java$lang$String());
   }

   private String getTestMethodCodeForAssertion(String suffix, String assertion, String basename) {
      CallSite[] var4 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{basename, suffix, var4[26].call((List)ScriptBytecodeAdapter.asType(var4[27].call(assertion, (Object)"UTF-8"), $get$$class$java$util$List()), (Object)", ")}, new String[]{"\n            public void testAssertionFrom", "Line", "() {\n                byte[] bytes = [ ", " ] as byte[]\n                Eval.me(new String(bytes, \"UTF-8\"))\n            }\n        "}), $get$$class$java$lang$String());
   }

   private String getTestCode(String testName, List testMethods) {
      CallSite[] var3 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(var3[28].call(var3[29].call(new GStringImpl(new Object[]{testName}, new String[]{"\n            class ", " extends junit.framework.TestCase {\n                "}), (Object)var3[30].call(testMethods, (Object)"\r\n")), (Object)"\n            }\n        "), $get$$class$java$lang$String());
   }

   private Class createClass(String testCode) {
      CallSite[] var2 = $getCallSiteArray();
      return (Class)ScriptBytecodeAdapter.castToType(var2[31].call(var2[32].callConstructor($get$$class$groovy$lang$GroovyClassLoader()), (Object)testCode), $get$$class$java$lang$Class());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$util$JavadocAssertionTestBuilder()) {
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
      Class var10000 = $get$$class$groovy$util$JavadocAssertionTestBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$util$JavadocAssertionTestBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$util$JavadocAssertionTestBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public List this$2$getAssertionTags(String var1) {
      return this.getAssertionTags(var1);
   }

   // $FF: synthetic method
   public String this$2$getTestName(String var1) {
      return this.getTestName(var1);
   }

   // $FF: synthetic method
   public Map this$2$getLineNumberToAssertionsMap(String var1, List var2) {
      return this.getLineNumberToAssertionsMap(var1, var2);
   }

   // $FF: synthetic method
   public String this$2$getAssertion(String var1) {
      return this.getAssertion(var1);
   }

   // $FF: synthetic method
   public List this$2$getTestMethods(Map var1, String var2) {
      return this.getTestMethods(var1, var2);
   }

   // $FF: synthetic method
   public String this$2$basename(String var1) {
      return this.basename(var1);
   }

   // $FF: synthetic method
   public String this$2$getTestMethodCodeForAssertion(String var1, String var2, String var3) {
      return this.getTestMethodCodeForAssertion(var1, var2, var3);
   }

   // $FF: synthetic method
   public String this$2$getTestCode(String var1, List var2) {
      return this.getTestCode(var1, var2);
   }

   // $FF: synthetic method
   public Class this$2$createClass(String var1) {
      return this.createClass(var1);
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
      var0[0] = "getAssertionTags";
      var0[1] = "getTestName";
      var0[2] = "getLineNumberToAssertionsMap";
      var0[3] = "getTestMethods";
      var0[4] = "getTestCode";
      var0[5] = "createClass";
      var0[6] = "<$constructor$>";
      var0[7] = "eachMatch";
      var0[8] = "name";
      var0[9] = "<$constructor$>";
      var0[10] = "plus";
      var0[11] = "substring";
      var0[12] = "lastIndexOf";
      var0[13] = "each";
      var0[14] = "substring";
      var0[15] = "plus";
      var0[16] = "indexOf";
      var0[17] = "lastIndexOf";
      var0[18] = "replaceAll";
      var0[19] = "each";
      var0[20] = "flatten";
      var0[21] = "collect";
      var0[22] = "<$constructor$>";
      var0[23] = "name";
      var0[24] = "substring";
      var0[25] = "lastIndexOf";
      var0[26] = "join";
      var0[27] = "getBytes";
      var0[28] = "plus";
      var0[29] = "plus";
      var0[30] = "join";
      var0[31] = "parseClass";
      var0[32] = "<$constructor$>";
      var0[33] = "compile";
      var0[34] = "compile";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[35];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$util$JavadocAssertionTestBuilder(), var0);
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
   private static Class $get$$class$java$util$regex$Pattern() {
      Class var10000 = $class$java$util$regex$Pattern;
      if (var10000 == null) {
         var10000 = $class$java$util$regex$Pattern = class$("java.util.regex.Pattern");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$JavadocAssertionTestBuilder() {
      Class var10000 = $class$groovy$util$JavadocAssertionTestBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$util$JavadocAssertionTestBuilder = class$("groovy.util.JavadocAssertionTestBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyClassLoader() {
      Class var10000 = $class$groovy$lang$GroovyClassLoader;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyClassLoader = class$("groovy.lang.GroovyClassLoader");
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
   private static Class $get$$class$java$util$Map() {
      Class var10000 = $class$java$util$Map;
      if (var10000 == null) {
         var10000 = $class$java$util$Map = class$("java.util.Map");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Class() {
      Class var10000 = $class$java$lang$Class;
      if (var10000 == null) {
         var10000 = $class$java$lang$Class = class$("java.lang.Class");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$LinkedHashMap() {
      Class var10000 = $class$java$util$LinkedHashMap;
      if (var10000 == null) {
         var10000 = $class$java$util$LinkedHashMap = class$("java.util.LinkedHashMap");
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
