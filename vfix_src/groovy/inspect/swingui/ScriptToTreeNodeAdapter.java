package groovy.inspect.swingui;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Writable;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;
import java.io.File;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.swing.tree.TreeNode;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ScriptToTreeNodeAdapter implements GroovyObject {
   private static Properties classNameToStringForm;
   private boolean showScriptFreeForm;
   private boolean showScriptClass;
   private final GroovyClassLoader classLoader;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202765L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202765 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$text$Template;
   // $FF: synthetic field
   private static Class $class$javax$swing$tree$TreeNode;
   // $FF: synthetic field
   private static Class $class$java$io$StringWriter;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$groovy$util$ConfigSlurper;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$javax$swing$tree$DefaultMutableTreeNode;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$lang$ClassLoader;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$TreeNodeWithProperties;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Writable;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilerConfiguration;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$groovy$text$GStringTemplateEngine;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyCodeSource;
   // $FF: synthetic field
   private static Class $class$java$util$Properties;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilationUnit;
   // $FF: synthetic field
   private static Class $class$java$net$URL;

   public ScriptToTreeNodeAdapter(Object classLoader, Object showScriptFreeForm, Object showScriptClass) {
      CallSite[] var4 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.classLoader = (GroovyClassLoader)ScriptBytecodeAdapter.castToType(classLoader, $get$$class$groovy$lang$GroovyClassLoader());
      this.showScriptFreeForm = DefaultTypeTransformation.booleanUnbox(showScriptFreeForm);
      this.showScriptClass = DefaultTypeTransformation.booleanUnbox(showScriptClass);
   }

   static {
      URL url = (URL)ScriptBytecodeAdapter.castToType($getCallSiteArray()[0].call($get$$class$java$lang$ClassLoader(), (Object)"groovy/inspect/swingui/AstBrowserProperties.groovy"), $get$$class$java$net$URL());
      Object config = $getCallSiteArray()[1].call($getCallSiteArray()[2].callConstructor($get$$class$groovy$util$ConfigSlurper()), (Object)url);
      classNameToStringForm = (Properties)((Properties)ScriptBytecodeAdapter.castToType($getCallSiteArray()[3].call(config), $get$$class$java$util$Properties()));
      String home = (String)ScriptBytecodeAdapter.castToType($getCallSiteArray()[4].call($get$$class$java$lang$System(), (Object)"user.home"), $get$$class$java$lang$String());
      if (DefaultTypeTransformation.booleanUnbox(home)) {
         File userFile = $getCallSiteArray()[5].callConstructor($get$$class$java$io$File(), (Object)$getCallSiteArray()[6].call($getCallSiteArray()[7].call(home, (Object)$getCallSiteArray()[8].callGetProperty($get$$class$java$io$File())), (Object)".groovy/AstBrowserProperties.groovy"));
         if (DefaultTypeTransformation.booleanUnbox($getCallSiteArray()[9].call(userFile))) {
            Object customConfig = $getCallSiteArray()[10].call($getCallSiteArray()[11].callConstructor($get$$class$groovy$util$ConfigSlurper()), $getCallSiteArray()[12].call(userFile));
            $getCallSiteArray()[13].call(classNameToStringForm, (Object)$getCallSiteArray()[14].call(customConfig));
         }
      }

   }

   public TreeNode compile(String script, int compilePhase) {
      CallSite[] var3 = $getCallSiteArray();
      Object scriptName = var3[15].call(var3[16].call("script", (Object)var3[17].call($get$$class$java$lang$System())), (Object)".groovy");
      GroovyCodeSource codeSource = var3[18].callConstructor($get$$class$groovy$lang$GroovyCodeSource(), script, scriptName, "/groovy/script");
      CompilationUnit cu = var3[19].callConstructor($get$$class$org$codehaus$groovy$control$CompilationUnit(), var3[20].callGetProperty($get$$class$org$codehaus$groovy$control$CompilerConfiguration()), var3[21].callGetProperty(codeSource), this.classLoader);
      TreeNodeBuildingNodeOperation operation = new Reference(var3[22].callConstructor($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation(), this, DefaultTypeTransformation.box(this.showScriptFreeForm), DefaultTypeTransformation.box(this.showScriptClass)));
      var3[23].call(cu, operation.get(), DefaultTypeTransformation.box(compilePhase));
      var3[24].call(cu, var3[25].call(codeSource), script);

      try {
         var3[26].call(cu, DefaultTypeTransformation.box(compilePhase));
      } catch (CompilationFailedException var12) {
         var3[27].call(var3[28].callGetProperty(operation.get()), var3[29].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)"Unable to produce AST for this phase due to earlier compilation error:"));
         var3[30].call(var3[31].callGetProperty(var12), (Object)(new GeneratedClosure(this, this, operation) {
            private Reference<T> operation;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_compile_closure1;
            // $FF: synthetic field
            private static Class $class$javax$swing$tree$DefaultMutableTreeNode;
            // $FF: synthetic field
            private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.operation = (Reference)operation;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].call(var3[1].callGetProperty(this.operation.get()), var3[2].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)itx.get()));
            }

            public TreeNodeBuildingNodeOperation getOperation() {
               CallSite[] var1 = $getCallSiteArray();
               return (TreeNodeBuildingNodeOperation)ScriptBytecodeAdapter.castToType(this.operation.get(), $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_compile_closure1()) {
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
               var0[0] = "add";
               var0[1] = "root";
               var0[2] = "<$constructor$>";
               var0[3] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[4];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_compile_closure1(), var0);
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
            private static Class $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_compile_closure1() {
               Class var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_compile_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_compile_closure1 = class$("groovy.inspect.swingui.ScriptToTreeNodeAdapter$_compile_closure1");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$javax$swing$tree$DefaultMutableTreeNode() {
               Class var10000 = $class$javax$swing$tree$DefaultMutableTreeNode;
               if (var10000 == null) {
                  var10000 = $class$javax$swing$tree$DefaultMutableTreeNode = class$("javax.swing.tree.DefaultMutableTreeNode");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation() {
               Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation;
               if (var10000 == null) {
                  var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation = class$("groovy.inspect.swingui.TreeNodeBuildingNodeOperation");
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
         var3[32].call(var3[33].callGetProperty(operation.get()), var3[34].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)"Fix the above error(s) and then press Refresh"));
      } catch (Throwable var13) {
         var3[35].call(var3[36].callGetProperty(operation.get()), var3[37].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)"Unable to produce AST for this phase due to an error:"));
         var3[38].call(var3[39].callGetProperty(operation.get()), var3[40].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)var13));
         var3[41].call(var3[42].callGetProperty(operation.get()), var3[43].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)"Fix the above error(s) and then press Refresh"));
      } finally {
         ;
      }

      return (TreeNode)ScriptBytecodeAdapter.castToType(var3[44].callGetProperty(operation.get()), $get$$class$javax$swing$tree$TreeNode());
   }

   public TreeNode make(Object node) {
      CallSite[] var2 = $getCallSiteArray();
      return (TreeNode)ScriptBytecodeAdapter.castToType(var2[45].callConstructor($get$$class$groovy$inspect$swingui$TreeNodeWithProperties(), var2[46].callCurrent(this, (Object)node), var2[47].callCurrent(this, (Object)node)), $get$$class$javax$swing$tree$TreeNode());
   }

   private List<List<String>> getPropertyTable(Object node) {
      Object node = new Reference(node);
      CallSite[] var3 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var3[48].callSafe(var3[49].callSafe(var3[50].callSafe(var3[51].callGetProperty(var3[52].callGetProperty(node.get())), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure2;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].callGetProperty(itx.get());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure2()) {
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
            var0[0] = "getter";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure2(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure2() {
            Class var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure2 = class$("groovy.inspect.swingui.ScriptToTreeNodeAdapter$_getPropertyTable_closure2");
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
      })), (Object)(new GeneratedClosure(this, this, node) {
         private Reference<T> node;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure3;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.node = (Reference)node;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            Object name = new Reference(var3[0].call(var3[1].callGetProperty(itx.get())));
            Reference value = new Reference((Object)null);

            try {
               if (DefaultTypeTransformation.booleanUnbox(this.node.get() instanceof DeclarationExpression && DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(name.get(), "variableExpression") && !ScriptBytecodeAdapter.compareEqual(name.get(), "tupleExpression") ? Boolean.FALSE : Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE)) {
                  value.set(var3[2].call(var3[3].callGetProperty(this.node.get())));
               } else {
                  value.set(var3[4].call(var3[5].call(itx.get(), this.node.get())));
               }
            } catch (GroovyBugError var10) {
               value.set((Object)null);
            } finally {
               ;
            }

            Object type = var3[6].call(var3[7].callGetProperty(var3[8].callGetProperty(itx.get())));
            return ScriptBytecodeAdapter.createList(new Object[]{name.get(), value.get(), type});
         }

         public Object getNode() {
            CallSite[] var1 = $getCallSiteArray();
            return this.node.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure3()) {
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
            var0[0] = "toString";
            var0[1] = "name";
            var0[2] = "toString";
            var0[3] = "leftExpression";
            var0[4] = "toString";
            var0[5] = "getProperty";
            var0[6] = "toString";
            var0[7] = "simpleName";
            var0[8] = "type";
            var0[9] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[10];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure3(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure3() {
            Class var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure3 = class$("groovy.inspect.swingui.ScriptToTreeNodeAdapter$_getPropertyTable_closure3");
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
      })), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure4;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(itx.get(), (Object)$const$0);
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure4()) {
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
            var0[0] = "getAt";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure4(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure4() {
            Class var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter$_getPropertyTable_closure4 = class$("groovy.inspect.swingui.ScriptToTreeNodeAdapter$_getPropertyTable_closure4");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      })), $get$$class$java$util$List());
   }

   private String getStringForm(Object node) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var2[53].call(classNameToStringForm, (Object)var2[54].callGetProperty(var2[55].callGetProperty(node))))) {
         GStringTemplateEngine engine = var2[56].callConstructor($get$$class$groovy$text$GStringTemplateEngine());
         Object script = var2[57].call(classNameToStringForm, (Object)var2[58].callGetProperty(var2[59].callGetProperty(node)));
         Template template = (Template)ScriptBytecodeAdapter.castToType(var2[60].call(engine, script), $get$$class$groovy$text$Template());
         Writable writable = (Writable)ScriptBytecodeAdapter.castToType(var2[61].call(template, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"expression", node})), $get$$class$groovy$lang$Writable());
         StringWriter result = var2[62].callConstructor($get$$class$java$io$StringWriter());
         var2[63].call(writable, (Object)result);
         return (String)ScriptBytecodeAdapter.castToType(var2[64].call(result), $get$$class$java$lang$String());
      } else {
         return (String)ScriptBytecodeAdapter.castToType(var2[65].callGetProperty(var2[66].callGetProperty(node)), $get$$class$java$lang$String());
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter()) {
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
      Class var10000 = $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public static Properties getClassNameToStringForm() {
      return classNameToStringForm;
   }

   public static void setClassNameToStringForm(Properties var0) {
      classNameToStringForm = var0;
   }

   public boolean getShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public boolean isShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public void setShowScriptFreeForm(boolean var1) {
      this.showScriptFreeForm = var1;
   }

   public boolean getShowScriptClass() {
      return this.showScriptClass;
   }

   public boolean isShowScriptClass() {
      return this.showScriptClass;
   }

   public void setShowScriptClass(boolean var1) {
      this.showScriptClass = var1;
   }

   public final GroovyClassLoader getClassLoader() {
      return this.classLoader;
   }

   // $FF: synthetic method
   public List this$2$getPropertyTable(Object var1) {
      return this.getPropertyTable(var1);
   }

   // $FF: synthetic method
   public String this$2$getStringForm(Object var1) {
      return this.getStringForm(var1);
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
      var0[0] = "getSystemResource";
      var0[1] = "parse";
      var0[2] = "<$constructor$>";
      var0[3] = "toProperties";
      var0[4] = "getProperty";
      var0[5] = "<$constructor$>";
      var0[6] = "plus";
      var0[7] = "plus";
      var0[8] = "separator";
      var0[9] = "exists";
      var0[10] = "parse";
      var0[11] = "<$constructor$>";
      var0[12] = "toURL";
      var0[13] = "putAll";
      var0[14] = "toProperties";
      var0[15] = "plus";
      var0[16] = "plus";
      var0[17] = "currentTimeMillis";
      var0[18] = "<$constructor$>";
      var0[19] = "<$constructor$>";
      var0[20] = "DEFAULT";
      var0[21] = "codeSource";
      var0[22] = "<$constructor$>";
      var0[23] = "addPhaseOperation";
      var0[24] = "addSource";
      var0[25] = "getName";
      var0[26] = "compile";
      var0[27] = "add";
      var0[28] = "root";
      var0[29] = "<$constructor$>";
      var0[30] = "eachLine";
      var0[31] = "message";
      var0[32] = "add";
      var0[33] = "root";
      var0[34] = "<$constructor$>";
      var0[35] = "add";
      var0[36] = "root";
      var0[37] = "<$constructor$>";
      var0[38] = "add";
      var0[39] = "root";
      var0[40] = "<$constructor$>";
      var0[41] = "add";
      var0[42] = "root";
      var0[43] = "<$constructor$>";
      var0[44] = "root";
      var0[45] = "<$constructor$>";
      var0[46] = "getStringForm";
      var0[47] = "getPropertyTable";
      var0[48] = "sort";
      var0[49] = "collect";
      var0[50] = "findAll";
      var0[51] = "properties";
      var0[52] = "metaClass";
      var0[53] = "getAt";
      var0[54] = "name";
      var0[55] = "class";
      var0[56] = "<$constructor$>";
      var0[57] = "getAt";
      var0[58] = "name";
      var0[59] = "class";
      var0[60] = "createTemplate";
      var0[61] = "make";
      var0[62] = "<$constructor$>";
      var0[63] = "writeTo";
      var0[64] = "toString";
      var0[65] = "simpleName";
      var0[66] = "class";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[67];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter(), var0);
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
   private static Class $get$$class$groovy$text$Template() {
      Class var10000 = $class$groovy$text$Template;
      if (var10000 == null) {
         var10000 = $class$groovy$text$Template = class$("groovy.text.Template");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$tree$TreeNode() {
      Class var10000 = $class$javax$swing$tree$TreeNode;
      if (var10000 == null) {
         var10000 = $class$javax$swing$tree$TreeNode = class$("javax.swing.tree.TreeNode");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$StringWriter() {
      Class var10000 = $class$java$io$StringWriter;
      if (var10000 == null) {
         var10000 = $class$java$io$StringWriter = class$("java.io.StringWriter");
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
   private static Class $get$$class$groovy$util$ConfigSlurper() {
      Class var10000 = $class$groovy$util$ConfigSlurper;
      if (var10000 == null) {
         var10000 = $class$groovy$util$ConfigSlurper = class$("groovy.util.ConfigSlurper");
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
   private static Class $get$$class$javax$swing$tree$DefaultMutableTreeNode() {
      Class var10000 = $class$javax$swing$tree$DefaultMutableTreeNode;
      if (var10000 == null) {
         var10000 = $class$javax$swing$tree$DefaultMutableTreeNode = class$("javax.swing.tree.DefaultMutableTreeNode");
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
   private static Class $get$$class$java$lang$ClassLoader() {
      Class var10000 = $class$java$lang$ClassLoader;
      if (var10000 == null) {
         var10000 = $class$java$lang$ClassLoader = class$("java.lang.ClassLoader");
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
   private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation() {
      Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation = class$("groovy.inspect.swingui.TreeNodeBuildingNodeOperation");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$TreeNodeWithProperties() {
      Class var10000 = $class$groovy$inspect$swingui$TreeNodeWithProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$TreeNodeWithProperties = class$("groovy.inspect.swingui.TreeNodeWithProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter() {
      Class var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter = class$("groovy.inspect.swingui.ScriptToTreeNodeAdapter");
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
   private static Class $get$$class$groovy$lang$Writable() {
      Class var10000 = $class$groovy$lang$Writable;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Writable = class$("groovy.lang.Writable");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$control$CompilerConfiguration() {
      Class var10000 = $class$org$codehaus$groovy$control$CompilerConfiguration;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$CompilerConfiguration = class$("org.codehaus.groovy.control.CompilerConfiguration");
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
   private static Class $get$$class$groovy$text$GStringTemplateEngine() {
      Class var10000 = $class$groovy$text$GStringTemplateEngine;
      if (var10000 == null) {
         var10000 = $class$groovy$text$GStringTemplateEngine = class$("groovy.text.GStringTemplateEngine");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyCodeSource() {
      Class var10000 = $class$groovy$lang$GroovyCodeSource;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyCodeSource = class$("groovy.lang.GroovyCodeSource");
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
   private static Class $get$$class$org$codehaus$groovy$control$CompilationUnit() {
      Class var10000 = $class$org$codehaus$groovy$control$CompilationUnit;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$CompilationUnit = class$("org.codehaus.groovy.control.CompilationUnit");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$net$URL() {
      Class var10000 = $class$java$net$URL;
      if (var10000 == null) {
         var10000 = $class$java$net$URL = class$("java.net.URL");
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
