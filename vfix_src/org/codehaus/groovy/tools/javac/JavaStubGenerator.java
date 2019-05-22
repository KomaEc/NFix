package org.codehaus.groovy.tools.javac;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.control.ResolveVisitor;
import org.codehaus.groovy.tools.Utilities;

public class JavaStubGenerator {
   private boolean java5;
   private boolean requireSuperResolved;
   private File outputPath;
   private List<String> toCompile;
   private ArrayList<MethodNode> propertyMethods;
   private Map<String, MethodNode> propertyMethodsWithSigs;
   private ArrayList<ConstructorNode> constructors;
   private ModuleNode currentModule;

   public JavaStubGenerator(File outputPath, boolean requireSuperResolved, boolean java5) {
      this.java5 = false;
      this.requireSuperResolved = false;
      this.toCompile = new ArrayList();
      this.propertyMethods = new ArrayList();
      this.propertyMethodsWithSigs = new HashMap();
      this.constructors = new ArrayList();
      this.outputPath = outputPath;
      this.requireSuperResolved = requireSuperResolved;
      this.java5 = java5;
      outputPath.mkdirs();
   }

   public JavaStubGenerator(File outputPath) {
      this(outputPath, false, false);
   }

   private void mkdirs(File parent, String relativeFile) {
      int index = relativeFile.lastIndexOf(47);
      if (index != -1) {
         File dir = new File(parent, relativeFile.substring(0, index));
         dir.mkdirs();
      }
   }

   public void generateClass(ClassNode classNode) throws FileNotFoundException {
      if (!this.requireSuperResolved || classNode.getSuperClass().isResolved()) {
         if (!(classNode instanceof InnerClassNode)) {
            if ((classNode.getModifiers() & 2) == 0) {
               String fileName = classNode.getName().replace('.', '/');
               this.mkdirs(this.outputPath, fileName);
               this.toCompile.add(fileName);
               File file = new File(this.outputPath, fileName + ".java");
               FileOutputStream fos = new FileOutputStream(file);
               PrintWriter out = new PrintWriter(fos);

               try {
                  String packageName = classNode.getPackageName();
                  if (packageName != null) {
                     out.println("package " + packageName + ";\n");
                  }

                  this.genImports(classNode, out);
                  this.genClassInner(classNode, out);
               } finally {
                  try {
                     out.close();
                  } catch (Exception var15) {
                  }

                  try {
                     fos.close();
                  } catch (IOException var14) {
                  }

               }

            }
         }
      }
   }

   private void genClassInner(ClassNode classNode, PrintWriter out) throws FileNotFoundException {
      if (!(classNode instanceof InnerClassNode) || !((InnerClassNode)classNode).isAnonymous()) {
         try {
            Verifier verifier = new Verifier() {
               public void addCovariantMethods(ClassNode cn) {
               }

               protected void addTimeStamp(ClassNode node) {
               }

               protected void addInitialization(ClassNode node) {
               }

               protected void addPropertyMethod(MethodNode method) {
                  this.doAddMethod(method);
               }

               protected void addReturnIfNeeded(MethodNode node) {
               }

               protected void addMethod(ClassNode node, boolean shouldBeSynthetic, String name, int modifiers, ClassNode returnType, Parameter[] parameters, ClassNode[] exceptions, Statement code) {
                  this.doAddMethod(new MethodNode(name, modifiers, returnType, parameters, exceptions, code));
               }

               protected void addConstructor(Parameter[] newParams, ConstructorNode ctor, Statement code, ClassNode node) {
                  if (code instanceof ExpressionStatement) {
                     Statement temp = code;
                     code = new BlockStatement();
                     ((BlockStatement)code).addStatement((Statement)temp);
                  }

                  ConstructorNode ctrNode = new ConstructorNode(ctor.getModifiers(), newParams, ctor.getExceptions(), (Statement)code);
                  ctrNode.setDeclaringClass(node);
                  JavaStubGenerator.this.constructors.add(ctrNode);
               }

               protected void addDefaultParameters(Verifier.DefaultArgsAction action, MethodNode method) {
                  Parameter[] parameters = method.getParameters();
                  Expression[] saved = new Expression[parameters.length];

                  int i;
                  for(i = 0; i < parameters.length; ++i) {
                     if (parameters[i].hasInitialExpression()) {
                        saved[i] = parameters[i].getInitialExpression();
                     }
                  }

                  super.addDefaultParameters(action, method);

                  for(i = 0; i < parameters.length; ++i) {
                     if (saved[i] != null) {
                        parameters[i].setInitialExpression(saved[i]);
                     }
                  }

               }

               private void doAddMethod(MethodNode method) {
                  String sig = method.getTypeDescriptor();
                  if (!JavaStubGenerator.this.propertyMethodsWithSigs.containsKey(sig)) {
                     JavaStubGenerator.this.propertyMethods.add(method);
                     JavaStubGenerator.this.propertyMethodsWithSigs.put(sig, method);
                  }
               }
            };
            verifier.visitClass(classNode);
            this.currentModule = classNode.getModule();
            boolean isInterface = classNode.isInterface();
            boolean isEnum = (classNode.getModifiers() & 16384) != 0;
            boolean isAnnotationDefinition = classNode.isAnnotationDefinition();
            this.printAnnotations(out, classNode);
            this.printModifiers(out, classNode.getModifiers() & ~(isInterface ? 1024 : 0));
            if (isInterface) {
               if (isAnnotationDefinition) {
                  out.print("@");
               }

               out.print("interface ");
            } else if (isEnum) {
               out.print("enum ");
            } else {
               out.print("class ");
            }

            String className = classNode.getNameWithoutPackage();
            if (classNode instanceof InnerClassNode) {
               className = className.substring(className.lastIndexOf("$") + 1);
            }

            out.println(className);
            this.writeGenericsBounds(out, classNode, true);
            ClassNode superClass = classNode.getUnresolvedSuperClass(false);
            if (!isInterface && !isEnum) {
               out.print("  extends ");
               this.printType(superClass, out);
            }

            ClassNode[] interfaces = classNode.getInterfaces();
            if (interfaces != null && interfaces.length > 0 && !isAnnotationDefinition) {
               if (isInterface) {
                  out.println("  extends");
               } else {
                  out.println("  implements");
               }

               for(int i = 0; i < interfaces.length - 1; ++i) {
                  out.print("    ");
                  this.printType(interfaces[i], out);
                  out.print(",");
               }

               out.print("    ");
               this.printType(interfaces[interfaces.length - 1], out);
            }

            out.println(" {");
            this.genFields(classNode, out);
            this.genMethods(classNode, out, isEnum);
            Iterator inner = classNode.getInnerClasses();

            while(inner.hasNext()) {
               this.propertyMethods.clear();
               this.propertyMethodsWithSigs.clear();
               this.constructors.clear();
               this.genClassInner((ClassNode)inner.next(), out);
            }

            out.println("}");
         } finally {
            this.propertyMethods.clear();
            this.propertyMethodsWithSigs.clear();
            this.constructors.clear();
            this.currentModule = null;
         }
      }
   }

   private void genMethods(ClassNode classNode, PrintWriter out, boolean isEnum) {
      if (!isEnum) {
         this.getConstructors(classNode, out);
      }

      List<MethodNode> methods = (List)this.propertyMethods.clone();
      methods.addAll(classNode.getMethods());
      Iterator i$ = methods.iterator();

      while(true) {
         MethodNode method;
         String name;
         Parameter[] params;
         do {
            if (!i$.hasNext()) {
               return;
            }

            method = (MethodNode)i$.next();
            if (!isEnum || !method.isSynthetic()) {
               break;
            }

            name = method.getName();
            params = method.getParameters();
         } while(name.equals("values") && params.length == 0 || name.equals("valueOf") && params.length == 1 && params[0].getType().equals(ClassHelper.STRING_TYPE));

         this.genMethod(classNode, method, out);
      }
   }

   private void getConstructors(ClassNode classNode, PrintWriter out) {
      List<ConstructorNode> constrs = (List)this.constructors.clone();
      constrs.addAll(classNode.getDeclaredConstructors());
      if (constrs != null) {
         Iterator i$ = constrs.iterator();

         while(i$.hasNext()) {
            ConstructorNode constr = (ConstructorNode)i$.next();
            this.genConstructor(classNode, constr, out);
         }
      }

   }

   private void genFields(ClassNode classNode, PrintWriter out) {
      boolean isInterface = classNode.isInterface();
      List<FieldNode> fields = classNode.getFields();
      if (fields != null) {
         List<FieldNode> enumFields = new ArrayList(fields.size());
         List<FieldNode> normalFields = new ArrayList(fields.size());
         Iterator i$ = fields.iterator();

         FieldNode field;
         while(i$.hasNext()) {
            field = (FieldNode)i$.next();
            boolean isSynthetic = (field.getModifiers() & 4096) != 0;
            if (field.isEnum()) {
               enumFields.add(field);
            } else if (!isSynthetic) {
               normalFields.add(field);
            }
         }

         this.genEnumFields(enumFields, out);
         i$ = normalFields.iterator();

         while(i$.hasNext()) {
            field = (FieldNode)i$.next();
            this.genField(field, out, isInterface);
         }

      }
   }

   private void genEnumFields(List<FieldNode> fields, PrintWriter out) {
      if (fields.size() != 0) {
         boolean first = true;

         FieldNode field;
         for(Iterator i$ = fields.iterator(); i$.hasNext(); out.print(field.getName())) {
            field = (FieldNode)i$.next();
            if (!first) {
               out.print(", ");
            } else {
               first = false;
            }
         }

         out.println(";");
      }
   }

   private void genField(FieldNode fieldNode, PrintWriter out, boolean isInterface) {
      if ((fieldNode.getModifiers() & 2) == 0) {
         this.printAnnotations(out, fieldNode);
         if (!isInterface) {
            this.printModifiers(out, fieldNode.getModifiers());
         }

         ClassNode type = fieldNode.getType();
         this.printType(type, out);
         out.print(" ");
         out.print(fieldNode.getName());
         if (isInterface) {
            out.print(" = ");
            if (ClassHelper.isPrimitiveType(type)) {
               String val = type == ClassHelper.boolean_TYPE ? "false" : "0";
               out.print("new " + ClassHelper.getWrapper(type) + "((" + type + ")" + val + ")");
            } else {
               out.print("null");
            }
         }

         out.println(";");
      }
   }

   private ConstructorCallExpression getConstructorCallExpression(ConstructorNode constructorNode) {
      Statement code = constructorNode.getCode();
      if (!(code instanceof BlockStatement)) {
         return null;
      } else {
         BlockStatement block = (BlockStatement)code;
         List stats = block.getStatements();
         if (stats != null && stats.size() != 0) {
            Statement stat = (Statement)stats.get(0);
            if (!(stat instanceof ExpressionStatement)) {
               return null;
            } else {
               Expression expr = ((ExpressionStatement)stat).getExpression();
               return !(expr instanceof ConstructorCallExpression) ? null : (ConstructorCallExpression)expr;
            }
         } else {
            return null;
         }
      }
   }

   private void genConstructor(ClassNode clazz, ConstructorNode constructorNode, PrintWriter out) {
      this.printAnnotations(out, constructorNode);
      out.print("public ");
      String className = clazz.getNameWithoutPackage();
      if (clazz instanceof InnerClassNode) {
         className = className.substring(className.lastIndexOf("$") + 1);
      }

      out.println(className);
      this.printParams(constructorNode, out);
      ConstructorCallExpression constrCall = this.getConstructorCallExpression(constructorNode);
      if (constrCall != null && constrCall.isSpecialCall()) {
         out.println(" {");
         this.genSpecialConstructorArgs(out, constructorNode, constrCall);
         out.println("}");
      } else {
         out.println(" {}");
      }

   }

   private Parameter[] selectAccessibleConstructorFromSuper(ConstructorNode node) {
      ClassNode type = node.getDeclaringClass();
      ClassNode superType = type.getSuperClass();
      Iterator i$ = superType.getDeclaredConstructors().iterator();

      ConstructorNode c;
      do {
         if (!i$.hasNext()) {
            if (superType.isPrimaryClassNode()) {
               return Parameter.EMPTY_ARRAY;
            }

            return null;
         }

         c = (ConstructorNode)i$.next();
      } while(!c.isPublic() && !c.isProtected());

      return c.getParameters();
   }

   private void genSpecialConstructorArgs(PrintWriter out, ConstructorNode node, ConstructorCallExpression constrCall) {
      Parameter[] params = this.selectAccessibleConstructorFromSuper(node);
      if (params != null) {
         out.print("super (");

         for(int i = 0; i < params.length; ++i) {
            this.printDefaultValue(out, params[i].getType());
            if (i + 1 < params.length) {
               out.print(", ");
            }
         }

         out.println(");");
      } else {
         Expression arguments = constrCall.getArguments();
         if (constrCall.isSuperCall()) {
            out.print("super(");
         } else {
            out.print("this(");
         }

         if (arguments instanceof ArgumentListExpression) {
            ArgumentListExpression argumentListExpression = (ArgumentListExpression)arguments;
            List<Expression> args = argumentListExpression.getExpressions();
            Iterator i$ = args.iterator();

            while(i$.hasNext()) {
               Expression arg = (Expression)i$.next();
               if (arg instanceof ConstantExpression) {
                  ConstantExpression expression = (ConstantExpression)arg;
                  Object o = expression.getValue();
                  if (o instanceof String) {
                     out.print("(String)null");
                  } else {
                     out.print(expression.getText());
                  }
               } else {
                  ClassNode type = this.getConstructorArgumentType(arg, node);
                  this.printDefaultValue(out, type);
               }

               if (arg != args.get(args.size() - 1)) {
                  out.print(", ");
               }
            }
         }

         out.println(");");
      }
   }

   private ClassNode getConstructorArgumentType(Expression arg, ConstructorNode node) {
      if (!(arg instanceof VariableExpression)) {
         return arg.getType();
      } else {
         VariableExpression vexp = (VariableExpression)arg;
         String name = vexp.getName();
         Parameter[] arr$ = node.getParameters();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Parameter param = arr$[i$];
            if (param.getName().equals(name)) {
               return param.getType();
            }
         }

         return vexp.getType();
      }
   }

   private void genMethod(ClassNode clazz, MethodNode methodNode, PrintWriter out) {
      if (!methodNode.getName().equals("<clinit>")) {
         if (!methodNode.isPrivate() && Utilities.isJavaIdentifier(methodNode.getName())) {
            if (!methodNode.isSynthetic() || !methodNode.getName().equals("$getStaticMetaClass")) {
               this.printAnnotations(out, methodNode);
               if (!clazz.isInterface()) {
                  this.printModifiers(out, methodNode.getModifiers());
               }

               this.writeGenericsBounds(out, methodNode.getGenericsTypes());
               out.print(" ");
               this.printType(methodNode.getReturnType(), out);
               out.print(" ");
               out.print(methodNode.getName());
               this.printParams(methodNode, out);
               ClassNode[] exceptions = methodNode.getExceptions();

               for(int i = 0; i < exceptions.length; ++i) {
                  ClassNode exception = exceptions[i];
                  if (i == 0) {
                     out.print("throws ");
                  } else {
                     out.print(", ");
                  }

                  this.printType(exception, out);
               }

               if ((methodNode.getModifiers() & 1024) != 0) {
                  out.println(";");
               } else {
                  out.print(" { ");
                  ClassNode retType = methodNode.getReturnType();
                  this.printReturn(out, retType);
                  out.println("}");
               }

            }
         }
      }
   }

   private void printReturn(PrintWriter out, ClassNode retType) {
      String retName = retType.getName();
      if (!retName.equals("void")) {
         out.print("return ");
         this.printDefaultValue(out, retType);
         out.print(";");
      }

   }

   private void printDefaultValue(PrintWriter out, ClassNode type) {
      if (type.redirect() != ClassHelper.OBJECT_TYPE) {
         out.print("(");
         this.printType(type, out);
         out.print(")");
      }

      if (ClassHelper.isPrimitiveType(type)) {
         if (type == ClassHelper.boolean_TYPE) {
            out.print("false");
         } else {
            out.print("0");
         }
      } else {
         out.print("null");
      }

   }

   private void printType(ClassNode type, PrintWriter out) {
      if (type.isArray()) {
         this.printType(type.getComponentType(), out);
         out.print("[]");
      } else if (this.java5 && type.isGenericsPlaceHolder()) {
         out.print(type.getGenericsTypes()[0].getName());
      } else {
         this.writeGenericsBounds(out, type, false);
      }

   }

   private void printTypeName(ClassNode type, PrintWriter out) {
      if (ClassHelper.isPrimitiveType(type)) {
         if (type == ClassHelper.boolean_TYPE) {
            out.print("boolean");
         } else if (type == ClassHelper.char_TYPE) {
            out.print("char");
         } else if (type == ClassHelper.int_TYPE) {
            out.print("int");
         } else if (type == ClassHelper.short_TYPE) {
            out.print("short");
         } else if (type == ClassHelper.long_TYPE) {
            out.print("long");
         } else if (type == ClassHelper.float_TYPE) {
            out.print("float");
         } else if (type == ClassHelper.double_TYPE) {
            out.print("double");
         } else if (type == ClassHelper.byte_TYPE) {
            out.print("byte");
         } else {
            out.print("void");
         }
      } else {
         String name = type.getName();
         ClassNode alias = this.currentModule.getImportType(name);
         if (alias != null) {
            name = alias.getName();
         }

         out.print(name.replace('$', '.'));
      }

   }

   private void writeGenericsBounds(PrintWriter out, ClassNode type, boolean skipName) {
      if (!skipName) {
         this.printTypeName(type, out);
      }

      if (this.java5) {
         if (!ClassHelper.isCachedType(type)) {
            this.writeGenericsBounds(out, type.getGenericsTypes());
         }

      }
   }

   private void writeGenericsBounds(PrintWriter out, GenericsType[] genericsTypes) {
      if (genericsTypes != null && genericsTypes.length != 0) {
         out.print('<');

         for(int i = 0; i < genericsTypes.length; ++i) {
            if (i != 0) {
               out.print(", ");
            }

            this.writeGenericsBounds(out, genericsTypes[i]);
         }

         out.print('>');
      }
   }

   private void writeGenericsBounds(PrintWriter out, GenericsType genericsType) {
      if (genericsType.isPlaceholder()) {
         out.print(genericsType.getName());
      } else {
         this.printType(genericsType.getType(), out);
      }

      ClassNode[] upperBounds = genericsType.getUpperBounds();
      ClassNode lowerBound = genericsType.getLowerBound();
      if (upperBounds != null) {
         out.print(" extends ");

         for(int i = 0; i < upperBounds.length; ++i) {
            this.printType(upperBounds[i], out);
            if (i + 1 < upperBounds.length) {
               out.print(" & ");
            }
         }
      } else if (lowerBound != null) {
         out.print(" super ");
         this.printType(lowerBound, out);
      }

   }

   private void printParams(MethodNode methodNode, PrintWriter out) {
      out.print("(");
      Parameter[] parameters = methodNode.getParameters();
      if (parameters != null && parameters.length != 0) {
         for(int i = 0; i != parameters.length; ++i) {
            this.printType(parameters[i].getType(), out);
            out.print(" ");
            out.print(parameters[i].getName());
            if (i + 1 < parameters.length) {
               out.print(", ");
            }
         }
      }

      out.print(")");
   }

   private void printAnnotations(PrintWriter out, AnnotatedNode annotated) {
      if (this.java5) {
         Iterator i$ = annotated.getAnnotations().iterator();

         while(i$.hasNext()) {
            AnnotationNode annotation = (AnnotationNode)i$.next();
            out.print("@" + annotation.getClassNode().getName() + "(");
            boolean first = true;
            Map<String, Expression> members = annotation.getMembers();

            String key;
            for(Iterator i$ = members.keySet().iterator(); i$.hasNext(); out.print(key + "=" + this.getAnnotationValue(members.get(key)))) {
               key = (String)i$.next();
               if (first) {
                  first = false;
               } else {
                  out.print(", ");
               }
            }

            out.print(") ");
         }

      }
   }

   private String getAnnotationValue(Object memberValue) {
      String val = "null";
      if (memberValue instanceof ListExpression) {
         StringBuilder sb = new StringBuilder("{");
         boolean first = true;
         ListExpression le = (ListExpression)memberValue;

         Expression e;
         for(Iterator i$ = le.getExpressions().iterator(); i$.hasNext(); sb.append(this.getAnnotationValue(e))) {
            e = (Expression)i$.next();
            if (first) {
               first = false;
            } else {
               sb.append(",");
            }
         }

         sb.append("}");
         val = sb.toString();
      } else if (memberValue instanceof ConstantExpression) {
         ConstantExpression ce = (ConstantExpression)memberValue;
         Object constValue = ce.getValue();
         if (!(constValue instanceof Number) && !(constValue instanceof Boolean)) {
            val = "\"" + constValue + "\"";
         } else {
            val = constValue.toString();
         }
      } else if (!(memberValue instanceof PropertyExpression) && !(memberValue instanceof VariableExpression)) {
         if (memberValue instanceof ClassExpression) {
            val = ((Expression)memberValue).getText() + ".class";
         }
      } else {
         val = ((Expression)memberValue).getText();
      }

      return val;
   }

   private void printModifiers(PrintWriter out, int modifiers) {
      if ((modifiers & 1) != 0) {
         out.print("public ");
      }

      if ((modifiers & 4) != 0) {
         out.print("protected ");
      }

      if ((modifiers & 2) != 0) {
         out.print("private ");
      }

      if ((modifiers & 8) != 0) {
         out.print("static ");
      }

      if ((modifiers & 32) != 0) {
         out.print("synchronized ");
      }

      if ((modifiers & 1024) != 0) {
         out.print("abstract ");
      }

   }

   private void genImports(ClassNode classNode, PrintWriter out) {
      List<String> imports = new ArrayList();
      ModuleNode moduleNode = classNode.getModule();
      Iterator i$ = moduleNode.getStarImports().iterator();

      ImportNode imp;
      while(i$.hasNext()) {
         imp = (ImportNode)i$.next();
         imports.add(imp.getPackageName());
      }

      i$ = moduleNode.getImports().iterator();

      while(i$.hasNext()) {
         imp = (ImportNode)i$.next();
         if (imp.getAlias() == null) {
            imports.add(imp.getType().getName());
         }
      }

      imports.addAll(Arrays.asList(ResolveVisitor.DEFAULT_IMPORTS));
      i$ = imports.iterator();

      while(i$.hasNext()) {
         String imp = (String)i$.next();
         String s = ("import " + imp + (imp.charAt(imp.length() - 1) == '.' ? "*;" : ";")).replace('$', '.');
         out.println(s);
      }

      out.println();
   }

   public void clean() {
      Iterator i$ = this.toCompile.iterator();

      while(i$.hasNext()) {
         String path = (String)i$.next();
         (new File(this.outputPath, path + ".java")).delete();
      }

   }
}
