package groovy.inspect.swingui;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

private class TreeNodeBuildingNodeOperation extends CompilationUnit.PrimaryClassNodeOperation implements GroovyObject {
   private final Object root;
   private final Object sourceCollected;
   private final ScriptToTreeNodeAdapter adapter;
   private final Object showScriptFreeForm;
   private final Object showScriptClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204219L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204219 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter;
   // $FF: synthetic field
   private static Class $class$java$util$concurrent$atomic$AtomicBoolean;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$ModuleNode;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalArgumentException;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$javax$swing$tree$DefaultMutableTreeNode;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation;

   public TreeNodeBuildingNodeOperation(ScriptToTreeNodeAdapter adapter, Object showScriptFreeForm, Object showScriptClass) {
      CallSite[] var4 = $getCallSiteArray();
      this.root = var4[0].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)"root");
      this.sourceCollected = var4[1].callConstructor($get$$class$java$util$concurrent$atomic$AtomicBoolean(), (Object)Boolean.FALSE);
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      if (!DefaultTypeTransformation.booleanUnbox(adapter)) {
         throw (Throwable)var4[2].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: adapter");
      } else {
         this.adapter = (ScriptToTreeNodeAdapter)ScriptBytecodeAdapter.castToType(adapter, $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter());
         this.showScriptFreeForm = showScriptFreeForm;
         this.showScriptClass = showScriptClass;
      }
   }

   public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var4[3].call(this.sourceCollected, (Object)Boolean.TRUE)) && DefaultTypeTransformation.booleanUnbox(this.showScriptFreeForm) ? Boolean.TRUE : Boolean.FALSE)) {
         ModuleNode ast = (ModuleNode)ScriptBytecodeAdapter.castToType(var4[4].call(source), $get$$class$org$codehaus$groovy$ast$ModuleNode());
         TreeNodeBuildingVisitor visitor = var4[5].callConstructor($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), (Object)this.adapter);
         var4[6].call(var4[7].call(ast), visitor);
         if (DefaultTypeTransformation.booleanUnbox(var4[8].callGetProperty(visitor))) {
            var4[9].call(this.root, var4[10].callGetProperty(visitor));
         }

         var4[11].callCurrent(this, "Methods", var4[12].call(ast));
      }

      if (!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var4[13].call(classNode)) && !DefaultTypeTransformation.booleanUnbox(this.showScriptClass) ? Boolean.TRUE : Boolean.FALSE)) {
         Object child = var4[14].call(this.adapter, (Object)classNode);
         var4[15].call(this.root, child);
         var4[16].callCurrent(this, child, "Constructors", classNode);
         var4[17].callCurrent(this, child, "Methods", classNode);
         var4[18].callCurrent(this, child, "Fields", classNode);
         var4[19].callCurrent(this, child, "Properties", classNode);
         var4[20].callCurrent(this, child, "Annotations", classNode);
      }
   }

   private List collectAnnotationData(TreeNodeWithProperties parent, String name, ClassNode classNode) {
      CallSite[] var4 = $getCallSiteArray();
      Object allAnnotations = new Reference(var4[21].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)name));
      if (DefaultTypeTransformation.booleanUnbox(var4[22].callGetProperty(classNode))) {
         var4[23].call(parent, (Object)allAnnotations.get());
      }

      return (List)ScriptBytecodeAdapter.castToType(var4[24].callSafe(var4[25].callGetProperty(classNode), (Object)(new GeneratedClosure(this, this, allAnnotations) {
         private Reference<T> allAnnotations;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectAnnotationData_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.allAnnotations = (Reference)allAnnotations;
         }

         public Object doCall(AnnotationNode annotationNode) {
            AnnotationNode annotationNodex = new Reference(annotationNode);
            CallSite[] var3 = $getCallSiteArray();
            Object ggrandchild = var3[0].call(var3[1].callGroovyObjectGetProperty(this), annotationNodex.get());
            return var3[2].call(this.allAnnotations.get(), ggrandchild);
         }

         public Object call(AnnotationNode annotationNode) {
            AnnotationNode annotationNodex = new Reference(annotationNode);
            CallSite[] var3 = $getCallSiteArray();
            return var3[3].callCurrent(this, (Object)annotationNodex.get());
         }

         public Object getAllAnnotations() {
            CallSite[] var1 = $getCallSiteArray();
            return this.allAnnotations.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectAnnotationData_closure1()) {
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
            var0[0] = "make";
            var0[1] = "adapter";
            var0[2] = "add";
            var0[3] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectAnnotationData_closure1(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectAnnotationData_closure1() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectAnnotationData_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectAnnotationData_closure1 = class$("groovy.inspect.swingui.TreeNodeBuildingNodeOperation$_collectAnnotationData_closure1");
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

   private Object collectPropertyData(TreeNodeWithProperties parent, String name, ClassNode classNode) {
      CallSite[] var4 = $getCallSiteArray();
      Object allProperties = new Reference(var4[26].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)name));
      if (DefaultTypeTransformation.booleanUnbox(var4[27].callGetProperty(classNode))) {
         var4[28].call(parent, (Object)allProperties.get());
      }

      return var4[29].callSafe(var4[30].callGetProperty(classNode), (Object)(new GeneratedClosure(this, this, allProperties) {
         private Reference<T> allProperties;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectPropertyData_closure2;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.allProperties = (Reference)allProperties;
         }

         public Object doCall(PropertyNode propertyNode) {
            PropertyNode propertyNodex = new Reference(propertyNode);
            CallSite[] var3 = $getCallSiteArray();
            Object ggrandchild = new Reference(var3[0].call(var3[1].callGroovyObjectGetProperty(this), propertyNodex.get()));
            var3[2].call(this.allProperties.get(), ggrandchild.get());
            TreeNodeBuildingVisitor visitor = new Reference(var3[3].callConstructor($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), (Object)var3[4].callGroovyObjectGetProperty(this)));
            if (DefaultTypeTransformation.booleanUnbox(var3[5].callGetPropertySafe(var3[6].callGetProperty(propertyNodex.get())))) {
               var3[7].call(var3[8].callGetProperty(var3[9].callGetProperty(propertyNodex.get())), visitor.get());
               return var3[10].call(ggrandchild.get(), var3[11].callGetProperty(visitor.get()));
            } else {
               return null;
            }
         }

         public Object call(PropertyNode propertyNode) {
            PropertyNode propertyNodex = new Reference(propertyNode);
            CallSite[] var3 = $getCallSiteArray();
            return var3[12].callCurrent(this, (Object)propertyNodex.get());
         }

         public Object getAllProperties() {
            CallSite[] var1 = $getCallSiteArray();
            return this.allProperties.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectPropertyData_closure2()) {
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
            var0[0] = "make";
            var0[1] = "adapter";
            var0[2] = "add";
            var0[3] = "<$constructor$>";
            var0[4] = "adapter";
            var0[5] = "initialValueExpression";
            var0[6] = "field";
            var0[7] = "visit";
            var0[8] = "initialValueExpression";
            var0[9] = "field";
            var0[10] = "add";
            var0[11] = "currentNode";
            var0[12] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[13];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectPropertyData_closure2(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectPropertyData_closure2() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectPropertyData_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectPropertyData_closure2 = class$("groovy.inspect.swingui.TreeNodeBuildingNodeOperation$_collectPropertyData_closure2");
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

   private Object collectFieldData(TreeNodeWithProperties parent, String name, ClassNode classNode) {
      CallSite[] var4 = $getCallSiteArray();
      Object allFields = new Reference(var4[31].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)name));
      if (DefaultTypeTransformation.booleanUnbox(var4[32].callGetProperty(classNode))) {
         var4[33].call(parent, (Object)allFields.get());
      }

      return var4[34].callSafe(var4[35].callGetProperty(classNode), (Object)(new GeneratedClosure(this, this, allFields) {
         private Reference<T> allFields;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectFieldData_closure3;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.allFields = (Reference)allFields;
         }

         public Object doCall(FieldNode fieldNode) {
            FieldNode fieldNodex = new Reference(fieldNode);
            CallSite[] var3 = $getCallSiteArray();
            Object ggrandchild = new Reference(var3[0].call(var3[1].callGroovyObjectGetProperty(this), fieldNodex.get()));
            var3[2].call(this.allFields.get(), ggrandchild.get());
            TreeNodeBuildingVisitor visitor = new Reference(var3[3].callConstructor($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), (Object)var3[4].callGroovyObjectGetProperty(this)));
            if (DefaultTypeTransformation.booleanUnbox(var3[5].callGetProperty(fieldNodex.get()))) {
               var3[6].call(var3[7].callGetProperty(fieldNodex.get()), visitor.get());
               return DefaultTypeTransformation.booleanUnbox(var3[8].callGetProperty(visitor.get())) ? var3[9].call(ggrandchild.get(), var3[10].callGetProperty(visitor.get())) : null;
            } else {
               return null;
            }
         }

         public Object call(FieldNode fieldNode) {
            FieldNode fieldNodex = new Reference(fieldNode);
            CallSite[] var3 = $getCallSiteArray();
            return var3[11].callCurrent(this, (Object)fieldNodex.get());
         }

         public Object getAllFields() {
            CallSite[] var1 = $getCallSiteArray();
            return this.allFields.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectFieldData_closure3()) {
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
            var0[0] = "make";
            var0[1] = "adapter";
            var0[2] = "add";
            var0[3] = "<$constructor$>";
            var0[4] = "adapter";
            var0[5] = "initialValueExpression";
            var0[6] = "visit";
            var0[7] = "initialValueExpression";
            var0[8] = "currentNode";
            var0[9] = "add";
            var0[10] = "currentNode";
            var0[11] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[12];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectFieldData_closure3(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectFieldData_closure3() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectFieldData_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectFieldData_closure3 = class$("groovy.inspect.swingui.TreeNodeBuildingNodeOperation$_collectFieldData_closure3");
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

   private Object collectMethodData(TreeNodeWithProperties parent, String name, ClassNode classNode) {
      CallSite[] var4 = $getCallSiteArray();
      Object allMethods = var4[36].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)name);
      if (DefaultTypeTransformation.booleanUnbox(var4[37].callGetProperty(classNode))) {
         var4[38].call(parent, (Object)allMethods);
      }

      return var4[39].callCurrent(this, allMethods, var4[40].callGetProperty(classNode));
   }

   private Object collectModuleNodeMethodData(String name, List methods) {
      CallSite[] var3 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(methods)) {
         return null;
      } else {
         Object allMethods = var3[41].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)name);
         var3[42].call(this.root, allMethods);
         return var3[43].callCurrent(this, allMethods, methods);
      }
   }

   private Object doCollectMethodData(DefaultMutableTreeNode allMethods, List methods) {
      DefaultMutableTreeNode allMethods = new Reference(allMethods);
      CallSite[] var4 = $getCallSiteArray();
      return var4[44].callSafe(methods, (Object)(new GeneratedClosure(this, this, allMethods) {
         private Reference<T> allMethods;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
         // $FF: synthetic field
         private static Class $class$javax$swing$tree$DefaultMutableTreeNode;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.allMethods = (Reference)allMethods;
         }

         public Object doCall(MethodNode methodNode) {
            MethodNode methodNodex = new Reference(methodNode);
            CallSite[] var3 = $getCallSiteArray();
            Object ggrandchild = new Reference(var3[0].call(var3[1].callGroovyObjectGetProperty(this), methodNodex.get()));
            var3[2].call(this.allMethods.get(), ggrandchild.get());
            var3[3].callSafe(var3[4].callGetProperty(methodNodex.get()), (Object)(new GeneratedClosure(this, this.getThisObject(), ggrandchild) {
               private Reference<T> ggrandchild;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4_closure6;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.ggrandchild = (Reference)ggrandchild;
               }

               public Object doCall(Parameter parameter) {
                  Parameter parameterx = new Reference(parameter);
                  CallSite[] var3 = $getCallSiteArray();
                  Object gggrandchild = new Reference(var3[0].call(var3[1].callGroovyObjectGetProperty(this), parameterx.get()));
                  var3[2].call(this.ggrandchild.get(), gggrandchild.get());
                  if (DefaultTypeTransformation.booleanUnbox(var3[3].callGetProperty(parameterx.get()))) {
                     TreeNodeBuildingVisitor visitor = var3[4].callConstructor($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), (Object)var3[5].callGroovyObjectGetProperty(this));
                     var3[6].call(var3[7].callGetProperty(parameterx.get()), visitor);
                     return DefaultTypeTransformation.booleanUnbox(var3[8].callGetProperty(visitor)) ? var3[9].call(gggrandchild.get(), var3[10].callGetProperty(visitor)) : null;
                  } else {
                     return null;
                  }
               }

               public Object call(Parameter parameter) {
                  Parameter parameterx = new Reference(parameter);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[11].callCurrent(this, (Object)parameterx.get());
               }

               public Object getGgrandchild() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.ggrandchild.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4_closure6()) {
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
                  var0[0] = "make";
                  var0[1] = "adapter";
                  var0[2] = "add";
                  var0[3] = "initialExpression";
                  var0[4] = "<$constructor$>";
                  var0[5] = "adapter";
                  var0[6] = "visit";
                  var0[7] = "initialExpression";
                  var0[8] = "currentNode";
                  var0[9] = "add";
                  var0[10] = "currentNode";
                  var0[11] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[12];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4_closure6(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor() {
                  Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4_closure6() {
                  Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4_closure6;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4_closure6 = class$("groovy.inspect.swingui.TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4_closure6");
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
            TreeNodeBuildingVisitor visitor = new Reference(var3[5].callConstructor($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), (Object)var3[6].callGroovyObjectGetProperty(this)));
            if (DefaultTypeTransformation.booleanUnbox(var3[7].callGetProperty(methodNodex.get()))) {
               var3[8].call(var3[9].callGetProperty(methodNodex.get()), visitor.get());
               return DefaultTypeTransformation.booleanUnbox(var3[10].callGetProperty(visitor.get())) ? var3[11].call(ggrandchild.get(), var3[12].callGetProperty(visitor.get())) : null;
            } else {
               return null;
            }
         }

         public Object call(MethodNode methodNode) {
            MethodNode methodNodex = new Reference(methodNode);
            CallSite[] var3 = $getCallSiteArray();
            return var3[13].callCurrent(this, (Object)methodNodex.get());
         }

         public DefaultMutableTreeNode getAllMethods() {
            CallSite[] var1 = $getCallSiteArray();
            return (DefaultMutableTreeNode)ScriptBytecodeAdapter.castToType(this.allMethods.get(), $get$$class$javax$swing$tree$DefaultMutableTreeNode());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4()) {
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
            var0[0] = "make";
            var0[1] = "adapter";
            var0[2] = "add";
            var0[3] = "each";
            var0[4] = "parameters";
            var0[5] = "<$constructor$>";
            var0[6] = "adapter";
            var0[7] = "code";
            var0[8] = "visit";
            var0[9] = "code";
            var0[10] = "currentNode";
            var0[11] = "add";
            var0[12] = "currentNode";
            var0[13] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[14];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4 = class$("groovy.inspect.swingui.TreeNodeBuildingNodeOperation$_doCollectMethodData_closure4");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
   }

   private Object collectConstructorData(TreeNodeWithProperties parent, String name, ClassNode classNode) {
      CallSite[] var4 = $getCallSiteArray();
      Object allCtors = new Reference(var4[45].callConstructor($get$$class$javax$swing$tree$DefaultMutableTreeNode(), (Object)name));
      if (DefaultTypeTransformation.booleanUnbox(var4[46].callGetProperty(classNode))) {
         var4[47].call(parent, (Object)allCtors.get());
      }

      return var4[48].callSafe(var4[49].callGetProperty(classNode), (Object)(new GeneratedClosure(this, this, allCtors) {
         private Reference<T> allCtors;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectConstructorData_closure5;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.allCtors = (Reference)allCtors;
         }

         public Object doCall(ConstructorNode ctorNode) {
            ConstructorNode ctorNodex = new Reference(ctorNode);
            CallSite[] var3 = $getCallSiteArray();
            Object ggrandchild = new Reference(var3[0].call(var3[1].callGroovyObjectGetProperty(this), ctorNodex.get()));
            var3[2].call(this.allCtors.get(), ggrandchild.get());
            TreeNodeBuildingVisitor visitor = new Reference(var3[3].callConstructor($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), (Object)var3[4].callGroovyObjectGetProperty(this)));
            if (DefaultTypeTransformation.booleanUnbox(var3[5].callGetProperty(ctorNodex.get()))) {
               var3[6].call(var3[7].callGetProperty(ctorNodex.get()), visitor.get());
               return DefaultTypeTransformation.booleanUnbox(var3[8].callGetProperty(visitor.get())) ? var3[9].call(ggrandchild.get(), var3[10].callGetProperty(visitor.get())) : null;
            } else {
               return null;
            }
         }

         public Object call(ConstructorNode ctorNode) {
            ConstructorNode ctorNodex = new Reference(ctorNode);
            CallSite[] var3 = $getCallSiteArray();
            return var3[11].callCurrent(this, (Object)ctorNodex.get());
         }

         public Object getAllCtors() {
            CallSite[] var1 = $getCallSiteArray();
            return this.allCtors.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectConstructorData_closure5()) {
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
            var0[0] = "make";
            var0[1] = "adapter";
            var0[2] = "add";
            var0[3] = "<$constructor$>";
            var0[4] = "adapter";
            var0[5] = "code";
            var0[6] = "visit";
            var0[7] = "code";
            var0[8] = "currentNode";
            var0[9] = "add";
            var0[10] = "currentNode";
            var0[11] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[12];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectConstructorData_closure5(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectConstructorData_closure5() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectConstructorData_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation$_collectConstructorData_closure5 = class$("groovy.inspect.swingui.TreeNodeBuildingNodeOperation$_collectConstructorData_closure5");
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
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation()) {
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

   public final Object getRoot() {
      return this.root;
   }

   public final Object getSourceCollected() {
      return this.sourceCollected;
   }

   public final ScriptToTreeNodeAdapter getAdapter() {
      return this.adapter;
   }

   public final Object getShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public final Object getShowScriptClass() {
      return this.showScriptClass;
   }

   // $FF: synthetic method
   public List this$3$collectAnnotationData(TreeNodeWithProperties var1, String var2, ClassNode var3) {
      return this.collectAnnotationData(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object this$3$collectPropertyData(TreeNodeWithProperties var1, String var2, ClassNode var3) {
      return this.collectPropertyData(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object this$3$collectFieldData(TreeNodeWithProperties var1, String var2, ClassNode var3) {
      return this.collectFieldData(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object this$3$collectMethodData(TreeNodeWithProperties var1, String var2, ClassNode var3) {
      return this.collectMethodData(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object this$3$collectModuleNodeMethodData(String var1, List var2) {
      return this.collectModuleNodeMethodData(var1, var2);
   }

   // $FF: synthetic method
   public Object this$3$doCollectMethodData(DefaultMutableTreeNode var1, List var2) {
      return this.doCollectMethodData(var1, var2);
   }

   // $FF: synthetic method
   public Object this$3$collectConstructorData(TreeNodeWithProperties var1, String var2, ClassNode var3) {
      return this.collectConstructorData(var1, var2, var3);
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
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public boolean super$2$needSortedInput() {
      return super.needSortedInput();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "<$constructor$>";
      var0[2] = "<$constructor$>";
      var0[3] = "getAndSet";
      var0[4] = "getAST";
      var0[5] = "<$constructor$>";
      var0[6] = "visit";
      var0[7] = "getStatementBlock";
      var0[8] = "currentNode";
      var0[9] = "add";
      var0[10] = "currentNode";
      var0[11] = "collectModuleNodeMethodData";
      var0[12] = "getMethods";
      var0[13] = "isScript";
      var0[14] = "make";
      var0[15] = "add";
      var0[16] = "collectConstructorData";
      var0[17] = "collectMethodData";
      var0[18] = "collectFieldData";
      var0[19] = "collectPropertyData";
      var0[20] = "collectAnnotationData";
      var0[21] = "<$constructor$>";
      var0[22] = "annotations";
      var0[23] = "add";
      var0[24] = "each";
      var0[25] = "annotations";
      var0[26] = "<$constructor$>";
      var0[27] = "properties";
      var0[28] = "add";
      var0[29] = "each";
      var0[30] = "properties";
      var0[31] = "<$constructor$>";
      var0[32] = "fields";
      var0[33] = "add";
      var0[34] = "each";
      var0[35] = "fields";
      var0[36] = "<$constructor$>";
      var0[37] = "methods";
      var0[38] = "add";
      var0[39] = "doCollectMethodData";
      var0[40] = "methods";
      var0[41] = "<$constructor$>";
      var0[42] = "add";
      var0[43] = "doCollectMethodData";
      var0[44] = "each";
      var0[45] = "<$constructor$>";
      var0[46] = "declaredConstructors";
      var0[47] = "add";
      var0[48] = "each";
      var0[49] = "declaredConstructors";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[50];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingNodeOperation(), var0);
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
   private static Class $get$$class$groovy$inspect$swingui$ScriptToTreeNodeAdapter() {
      Class var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$ScriptToTreeNodeAdapter = class$("groovy.inspect.swingui.ScriptToTreeNodeAdapter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$concurrent$atomic$AtomicBoolean() {
      Class var10000 = $class$java$util$concurrent$atomic$AtomicBoolean;
      if (var10000 == null) {
         var10000 = $class$java$util$concurrent$atomic$AtomicBoolean = class$("java.util.concurrent.atomic.AtomicBoolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor() {
      Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor");
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
   private static Class $get$$class$org$codehaus$groovy$ast$ModuleNode() {
      Class var10000 = $class$org$codehaus$groovy$ast$ModuleNode;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$ModuleNode = class$("org.codehaus.groovy.ast.ModuleNode");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$IllegalArgumentException() {
      Class var10000 = $class$java$lang$IllegalArgumentException;
      if (var10000 == null) {
         var10000 = $class$java$lang$IllegalArgumentException = class$("java.lang.IllegalArgumentException");
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
}
