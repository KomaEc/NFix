package org.codehaus.groovy.control;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.DynamicVariable;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.Variable;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.AnnotationConstantExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.control.messages.ExceptionMessage;

public class ResolveVisitor extends ClassCodeExpressionTransformer {
   private ClassNode currentClass;
   public static final String[] DEFAULT_IMPORTS = new String[]{"java.lang.", "java.io.", "java.net.", "java.util.", "groovy.lang.", "groovy.util."};
   private CompilationUnit compilationUnit;
   private Map cachedClasses = new HashMap();
   private static final Object NO_CLASS = new Object();
   private static final Object SCRIPT = new Object();
   private SourceUnit source;
   private VariableScope currentScope;
   private boolean isTopLevelProperty = true;
   private boolean inPropertyExpression = false;
   private boolean inClosure = false;
   private boolean isSpecialConstructorCall = false;
   private Map<String, GenericsType> genericParameterNames = new HashMap();
   private Set<FieldNode> fieldTypesChecked = new HashSet();
   private boolean checkingVariableTypeInDeclaration = false;
   private ImportNode currImportNode = null;

   public ResolveVisitor(CompilationUnit cu) {
      this.compilationUnit = cu;
   }

   public void startResolving(ClassNode node, SourceUnit source) {
      this.source = source;
      this.visitClass(node);
   }

   protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
      VariableScope oldScope = this.currentScope;
      this.currentScope = node.getVariableScope();
      Map<String, GenericsType> oldPNames = this.genericParameterNames;
      this.genericParameterNames = new HashMap(this.genericParameterNames);
      this.resolveGenericsHeader(node.getGenericsTypes());
      Parameter[] paras = node.getParameters();
      Parameter[] arr$ = paras;
      int len$ = paras.length;

      int len$;
      for(len$ = 0; len$ < len$; ++len$) {
         Parameter p = arr$[len$];
         p.setInitialExpression(this.transform(p.getInitialExpression()));
         this.resolveOrFail(p.getType(), p.getType());
         this.visitAnnotations(p);
      }

      ClassNode[] exceptions = node.getExceptions();
      ClassNode[] arr$ = exceptions;
      len$ = exceptions.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassNode t = arr$[i$];
         this.resolveOrFail(t, node);
      }

      this.resolveOrFail(node.getReturnType(), node);
      super.visitConstructorOrMethod(node, isConstructor);
      this.genericParameterNames = oldPNames;
      this.currentScope = oldScope;
   }

   public void visitField(FieldNode node) {
      ClassNode t = node.getType();
      if (!this.fieldTypesChecked.contains(node)) {
         this.resolveOrFail(t, node);
      }

      super.visitField(node);
   }

   public void visitProperty(PropertyNode node) {
      ClassNode t = node.getType();
      this.resolveOrFail(t, node);
      super.visitProperty(node);
      this.fieldTypesChecked.add(node.getField());
   }

   private boolean resolveToInner(ClassNode type) {
      if (type instanceof ResolveVisitor.ConstructedClassWithPackage) {
         return false;
      } else {
         String name = type.getName();
         String saved = name;

         do {
            int len = name.lastIndexOf(46);
            if (len == -1) {
               if (this.resolveToInnerEnum(type)) {
                  return true;
               } else {
                  type.setName(saved);
                  return false;
               }
            }

            name = name.substring(0, len) + "$" + name.substring(len + 1);
            type.setName(name);
         } while(!this.resolve(type));

         return true;
      }
   }

   private boolean resolveToInnerEnum(ClassNode type) {
      String name = type.getName();
      if (this.currentClass != type && !name.contains(".") && type.getClass().equals(ClassNode.class)) {
         type.setName(this.currentClass.getName() + "$" + name);
         if (this.resolve(type)) {
            return true;
         }
      }

      return false;
   }

   private void resolveOrFail(ClassNode type, String msg, ASTNode node) {
      if (!this.resolve(type)) {
         if (!this.resolveToInner(type)) {
            this.addError("unable to resolve class " + type.getName() + " " + msg, node);
         }
      }
   }

   private void resolveOrFail(ClassNode type, ASTNode node, boolean prefereImports) {
      this.resolveGenericsTypes(type.getGenericsTypes());
      if (!prefereImports || !this.resolveAliasFromModule(type)) {
         this.resolveOrFail(type, node);
      }
   }

   private void resolveOrFail(ClassNode type, ASTNode node) {
      this.resolveOrFail(type, "", node);
   }

   private boolean resolve(ClassNode type) {
      return this.resolve(type, true, true, true);
   }

   private boolean resolve(ClassNode type, boolean testModuleImports, boolean testDefaultImports, boolean testStaticInnerClasses) {
      this.resolveGenericsTypes(type.getGenericsTypes());
      if (!type.isResolved() && !type.isPrimaryClassNode()) {
         if (type.isArray()) {
            ClassNode element = type.getComponentType();
            boolean resolved = this.resolve(element, testModuleImports, testDefaultImports, testStaticInnerClasses);
            if (resolved) {
               ClassNode cn = element.makeArray();
               type.setRedirect(cn);
            }

            return resolved;
         } else if (this.currentClass == type) {
            return true;
         } else if (this.genericParameterNames.get(type.getName()) != null) {
            GenericsType gt = (GenericsType)this.genericParameterNames.get(type.getName());
            type.setRedirect(gt.getType());
            type.setGenericsTypes(new GenericsType[]{gt});
            type.setGenericsPlaceHolder(true);
            return true;
         } else if (this.currentClass.getNameWithoutPackage().equals(type.getName())) {
            type.setRedirect(this.currentClass);
            return true;
         } else {
            return this.resolveNestedClass(type) || this.resolveFromModule(type, testModuleImports) || this.resolveFromCompileUnit(type) || this.resolveFromDefaultImports(type, testDefaultImports) || this.resolveFromStaticInnerClasses(type, testStaticInnerClasses) || this.resolveFromClassCache(type) || this.resolveToClass(type) || this.resolveToScript(type);
         }
      } else {
         return true;
      }
   }

   private boolean resolveNestedClass(ClassNode type) {
      Map<String, ClassNode> hierClasses = new LinkedHashMap();

      for(ClassNode classToCheck = this.currentClass; classToCheck != ClassHelper.OBJECT_TYPE && classToCheck != null && !hierClasses.containsKey(classToCheck.getName()); classToCheck = classToCheck.getSuperClass()) {
         hierClasses.put(classToCheck.getName(), classToCheck);
      }

      Iterator i$ = hierClasses.values().iterator();

      ClassNode outer;
      String name;
      ClassNode val;
      while(i$.hasNext()) {
         outer = (ClassNode)i$.next();
         name = outer.getName() + "$" + type.getName();
         val = ClassHelper.make(name);
         if (this.resolveFromCompileUnit(val)) {
            type.setRedirect(val);
            return true;
         }
      }

      if (!(this.currentClass instanceof InnerClassNode)) {
         return false;
      } else {
         LinkedList<ClassNode> outerClasses = new LinkedList();

         for(outer = this.currentClass.getOuterClass(); outer != null; outer = outer.getOuterClass()) {
            outerClasses.addFirst(outer);
         }

         Iterator i$ = outerClasses.iterator();

         do {
            if (!i$.hasNext()) {
               return false;
            }

            ClassNode testNode = (ClassNode)i$.next();
            name = testNode.getName() + "$" + type.getName();
            val = ClassHelper.make(name);
         } while(!this.resolveFromCompileUnit(val));

         type.setRedirect(val);
         return true;
      }
   }

   private boolean resolveFromClassCache(ClassNode type) {
      String name = type.getName();
      Object val = this.cachedClasses.get(name);
      if (val != null && val != NO_CLASS) {
         type.setRedirect((ClassNode)val);
         return true;
      } else {
         return false;
      }
   }

   private long getTimeStamp(Class cls) {
      return Verifier.getTimestamp(cls);
   }

   private boolean isSourceNewer(URL source, Class cls) {
      try {
         long lastMod;
         if (source.getProtocol().equals("file")) {
            String path = source.getPath().replace('/', File.separatorChar).replace('|', ':');
            File file = new File(path);
            lastMod = file.lastModified();
         } else {
            URLConnection conn = source.openConnection();
            lastMod = conn.getLastModified();
            conn.getInputStream().close();
         }

         return lastMod > this.getTimeStamp(cls);
      } catch (IOException var8) {
         return false;
      }
   }

   private boolean resolveToScript(ClassNode type) {
      String name = type.getName();
      if (type instanceof ResolveVisitor.LowerCaseClass) {
         this.cachedClasses.put(name, NO_CLASS);
      }

      if (this.cachedClasses.get(name) == NO_CLASS) {
         return false;
      } else {
         if (this.cachedClasses.get(name) == SCRIPT) {
            this.cachedClasses.put(name, NO_CLASS);
         }

         if (name.startsWith("java.")) {
            return type.isResolved();
         } else if (name.indexOf(36) != -1) {
            return type.isResolved();
         } else {
            ModuleNode module = this.currentClass.getModule();
            if (module.hasPackageName() && name.indexOf(46) == -1) {
               return type.isResolved();
            } else {
               GroovyClassLoader gcl = this.compilationUnit.getClassLoader();
               URL url = null;

               try {
                  url = gcl.getResourceLoader().loadGroovySource(name);
               } catch (MalformedURLException var7) {
               }

               if (url != null) {
                  if (type.isResolved()) {
                     Class cls = type.getTypeClass();
                     if (!this.isSourceNewer(url, cls)) {
                        return true;
                     }

                     this.cachedClasses.remove(type.getName());
                     type.setRedirect((ClassNode)null);
                  }

                  SourceUnit su = this.compilationUnit.addSource(url);
                  this.currentClass.getCompileUnit().addClassNodeToCompile(type, su);
                  return true;
               } else {
                  return type.isResolved();
               }
            }
         }
      }
   }

   private String replaceLastPoint(String name) {
      int lastPoint = name.lastIndexOf(46);
      name = name.substring(0, lastPoint) + "$" + name.substring(lastPoint + 1);
      return name;
   }

   private boolean resolveFromStaticInnerClasses(ClassNode type, boolean testStaticInnerClasses) {
      if (type instanceof ResolveVisitor.LowerCaseClass) {
         return false;
      } else {
         testStaticInnerClasses &= type.hasPackageName();
         if (testStaticInnerClasses) {
            String savedName;
            if (type instanceof ResolveVisitor.ConstructedClassWithPackage) {
               ResolveVisitor.ConstructedClassWithPackage tmp = (ResolveVisitor.ConstructedClassWithPackage)type;
               savedName = tmp.className;
               tmp.className = this.replaceLastPoint(savedName);
               if (this.resolve(tmp, false, true, true)) {
                  type.setRedirect(tmp.redirect());
                  return true;
               }

               tmp.className = savedName;
            } else {
               String savedName = type.getName();
               savedName = this.replaceLastPoint(savedName);
               type.setName(savedName);
               if (this.resolve(type, false, true, true)) {
                  return true;
               }

               type.setName(savedName);
            }
         }

         return false;
      }
   }

   private boolean resolveFromDefaultImports(ClassNode type, boolean testDefaultImports) {
      testDefaultImports &= !type.hasPackageName();
      testDefaultImports &= !(type instanceof ResolveVisitor.LowerCaseClass);
      if (testDefaultImports) {
         int i = 0;

         for(int size = DEFAULT_IMPORTS.length; i < size; ++i) {
            String packagePrefix = DEFAULT_IMPORTS[i];
            String name = type.getName();
            ResolveVisitor.ConstructedClassWithPackage tmp = new ResolveVisitor.ConstructedClassWithPackage(packagePrefix, name);
            if (this.resolve(tmp, false, false, false)) {
               type.setRedirect(tmp.redirect());
               return true;
            }
         }

         String name = type.getName();
         if (name.equals("BigInteger")) {
            type.setRedirect(ClassHelper.BigInteger_TYPE);
            return true;
         }

         if (name.equals("BigDecimal")) {
            type.setRedirect(ClassHelper.BigDecimal_TYPE);
            return true;
         }
      }

      return false;
   }

   private boolean resolveFromCompileUnit(ClassNode type) {
      CompileUnit compileUnit = this.currentClass.getCompileUnit();
      if (compileUnit == null) {
         return false;
      } else {
         ClassNode cuClass = compileUnit.getClass(type.getName());
         if (cuClass != null) {
            if (type != cuClass) {
               type.setRedirect(cuClass);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   private void ambiguousClass(ClassNode type, ClassNode iType, String name) {
      if (type.getName().equals(iType.getName())) {
         this.addError("reference to " + name + " is ambiguous, both class " + type.getName() + " and " + iType.getName() + " match", type);
      } else {
         type.setRedirect(iType);
      }

   }

   private boolean resolveAliasFromModule(ClassNode type) {
      if (type instanceof ResolveVisitor.ConstructedClassWithPackage) {
         return false;
      } else {
         ModuleNode module = this.currentClass.getModule();
         if (module == null) {
            return false;
         } else {
            String name = type.getName();
            int index = name.length();

            do {
               String pname = name.substring(0, index);
               ClassNode aliasedNode = null;
               ImportNode importNode = module.getImport(pname);
               if (importNode != null && importNode != this.currImportNode) {
                  aliasedNode = importNode.getType();
               }

               if (aliasedNode == null) {
                  importNode = (ImportNode)module.getStaticImports().get(pname);
                  if (importNode != null && importNode != this.currImportNode) {
                     ClassNode tmp = ClassHelper.make(importNode.getType().getName() + "$" + importNode.getFieldName());
                     if (this.resolve(tmp, false, false, true) && (tmp.getModifiers() & 8) != 0) {
                        type.setRedirect(tmp.redirect());
                        return true;
                     }
                  }
               }

               if (aliasedNode != null) {
                  if (pname.length() == name.length()) {
                     type.setRedirect(aliasedNode);
                     return true;
                  }

                  String className = aliasedNode.getNameWithoutPackage() + '$' + name.substring(pname.length() + 1).replace('.', '$');
                  ResolveVisitor.ConstructedClassWithPackage tmp = new ResolveVisitor.ConstructedClassWithPackage(aliasedNode.getPackageName() + ".", className);
                  if (this.resolve(tmp, true, true, false)) {
                     type.setRedirect(tmp.redirect());
                     return true;
                  }
               }

               index = pname.lastIndexOf(46);
            } while(index != -1);

            return false;
         }
      }
   }

   private boolean resolveFromModule(ClassNode type, boolean testModuleImports) {
      if (type instanceof ResolveVisitor.LowerCaseClass) {
         return this.resolveAliasFromModule(type);
      } else {
         String name = type.getName();
         ModuleNode module = this.currentClass.getModule();
         if (module == null) {
            return false;
         } else {
            boolean newNameUsed = false;
            if (!type.hasPackageName() && module.hasPackageName() && !(type instanceof ResolveVisitor.ConstructedClassWithPackage)) {
               type.setName(module.getPackageName() + name);
               newNameUsed = true;
            }

            List<ClassNode> moduleClasses = module.getClasses();
            Iterator i$ = moduleClasses.iterator();

            while(i$.hasNext()) {
               ClassNode mClass = (ClassNode)i$.next();
               if (mClass.getName().equals(type.getName())) {
                  if (mClass != type) {
                     type.setRedirect(mClass);
                  }

                  return true;
               }
            }

            if (newNameUsed) {
               type.setName(name);
            }

            if (testModuleImports) {
               if (this.resolveAliasFromModule(type)) {
                  return true;
               }

               if (module.hasPackageName()) {
                  ResolveVisitor.ConstructedClassWithPackage tmp = new ResolveVisitor.ConstructedClassWithPackage(module.getPackageName(), name);
                  if (this.resolve(tmp, false, false, false)) {
                     this.ambiguousClass(type, tmp, name);
                     type.setRedirect(tmp.redirect());
                     return true;
                  }
               }

               i$ = module.getStaticImports().values().iterator();

               ClassNode tmp;
               ImportNode importNode;
               while(i$.hasNext()) {
                  importNode = (ImportNode)i$.next();
                  if (importNode.getFieldName().equals(name)) {
                     tmp = ClassHelper.make(importNode.getType().getName() + "$" + name);
                     if (this.resolve(tmp, false, false, true) && (tmp.getModifiers() & 8) != 0) {
                        type.setRedirect(tmp.redirect());
                        return true;
                     }
                  }
               }

               i$ = module.getStarImports().iterator();

               while(i$.hasNext()) {
                  importNode = (ImportNode)i$.next();
                  String packagePrefix = importNode.getPackageName();
                  ResolveVisitor.ConstructedClassWithPackage tmp = new ResolveVisitor.ConstructedClassWithPackage(packagePrefix, name);
                  if (this.resolve(tmp, false, false, true)) {
                     this.ambiguousClass(type, tmp, name);
                     type.setRedirect(tmp.redirect());
                     return true;
                  }
               }

               i$ = module.getStaticStarImports().values().iterator();

               while(i$.hasNext()) {
                  importNode = (ImportNode)i$.next();
                  tmp = ClassHelper.make(importNode.getClassName() + "$" + name);
                  if (this.resolve(tmp, false, false, true) && (tmp.getModifiers() & 8) != 0) {
                     this.ambiguousClass(type, tmp, name);
                     type.setRedirect(tmp.redirect());
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   private boolean resolveToClass(ClassNode type) {
      String name = type.getName();
      if (type instanceof ResolveVisitor.LowerCaseClass) {
         this.cachedClasses.put(name, NO_CLASS);
      }

      Object cached = this.cachedClasses.get(name);
      if (cached == NO_CLASS) {
         return false;
      } else if (cached == SCRIPT) {
         throw new GroovyBugError("name " + name + " was marked as script, but was not resolved as such");
      } else if (cached != null) {
         return true;
      } else if (this.currentClass.getModule().hasPackageName() && name.indexOf(46) == -1) {
         return false;
      } else {
         GroovyClassLoader loader = this.compilationUnit.getClassLoader();

         Class cls;
         try {
            cls = loader.loadClass(name, false, true);
         } catch (ClassNotFoundException var7) {
            this.cachedClasses.put(name, SCRIPT);
            return false;
         } catch (CompilationFailedException var8) {
            this.compilationUnit.getErrorCollector().addErrorAndContinue(new ExceptionMessage(var8, true, this.source));
            return false;
         }

         if (cls == null) {
            return false;
         } else {
            ClassNode cn = ClassHelper.make(cls);
            this.cachedClasses.put(name, cn);
            type.setRedirect(cn);
            return cls.getClassLoader() == loader;
         }
      }
   }

   public Expression transform(Expression exp) {
      if (exp == null) {
         return null;
      } else {
         Expression ret = null;
         if (exp instanceof VariableExpression) {
            ret = this.transformVariableExpression((VariableExpression)exp);
         } else if (exp.getClass() == PropertyExpression.class) {
            ret = this.transformPropertyExpression((PropertyExpression)exp);
         } else if (exp instanceof DeclarationExpression) {
            ret = this.transformDeclarationExpression((DeclarationExpression)exp);
         } else if (exp instanceof BinaryExpression) {
            ret = this.transformBinaryExpression((BinaryExpression)exp);
         } else if (exp instanceof MethodCallExpression) {
            ret = this.transformMethodCallExpression((MethodCallExpression)exp);
         } else if (exp instanceof ClosureExpression) {
            ret = this.transformClosureExpression((ClosureExpression)exp);
         } else if (exp instanceof ConstructorCallExpression) {
            ret = this.transformConstructorCallExpression((ConstructorCallExpression)exp);
         } else if (exp instanceof AnnotationConstantExpression) {
            ret = this.transformAnnotationConstantExpression((AnnotationConstantExpression)exp);
         } else {
            this.resolveOrFail(exp.getType(), exp);
            ret = exp.transformExpression(this);
         }

         if (ret != null && ret != exp) {
            ret.setSourcePosition(exp);
         }

         return ret;
      }
   }

   private String lookupClassName(PropertyExpression pe) {
      boolean doInitialClassTest = true;
      String name = "";
      Object it = pe;

      while(true) {
         if (it != null) {
            String propertyPart;
            if (!(it instanceof VariableExpression)) {
               if (it.getClass() != PropertyExpression.class) {
                  return null;
               }

               PropertyExpression current = (PropertyExpression)it;
               propertyPart = current.getPropertyAsString();
               if (propertyPart != null && !propertyPart.equals("class")) {
                  if (doInitialClassTest) {
                     if (!this.testVanillaNameForClass(propertyPart)) {
                        return null;
                     }

                     doInitialClassTest = false;
                     name = propertyPart;
                  } else {
                     name = propertyPart + "." + name;
                  }

                  it = ((PropertyExpression)it).getObjectExpression();
                  continue;
               }

               return null;
            }

            VariableExpression ve = (VariableExpression)it;
            if (ve.isSuperExpression() || ve.isThisExpression()) {
               return null;
            }

            propertyPart = ve.getName();
            if (doInitialClassTest) {
               if (!this.testVanillaNameForClass(propertyPart)) {
                  return null;
               }

               doInitialClassTest = false;
               name = propertyPart;
            } else {
               name = propertyPart + "." + name;
            }
         }

         if (name.length() == 0) {
            return null;
         }

         return name;
      }
   }

   private Expression correctClassClassChain(PropertyExpression pe) {
      LinkedList<Expression> stack = new LinkedList();
      ClassExpression found = null;

      Object stackElement;
      for(stackElement = pe; stackElement != null; stackElement = ((PropertyExpression)stackElement).getObjectExpression()) {
         if (stackElement instanceof ClassExpression) {
            found = (ClassExpression)stackElement;
            break;
         }

         if (stackElement.getClass() != PropertyExpression.class) {
            return pe;
         }

         stack.addFirst(stackElement);
      }

      if (found == null) {
         return pe;
      } else if (stack.isEmpty()) {
         return pe;
      } else {
         stackElement = stack.removeFirst();
         if (stackElement.getClass() != PropertyExpression.class) {
            return pe;
         } else {
            PropertyExpression classPropertyExpression = (PropertyExpression)stackElement;
            String propertyNamePart = classPropertyExpression.getPropertyAsString();
            if (propertyNamePart != null && propertyNamePart.equals("class")) {
               found.setSourcePosition(classPropertyExpression);
               if (stack.isEmpty()) {
                  return found;
               } else {
                  stackElement = stack.removeFirst();
                  if (stackElement.getClass() != PropertyExpression.class) {
                     return pe;
                  } else {
                     PropertyExpression classPropertyExpressionContainer = (PropertyExpression)stackElement;
                     classPropertyExpressionContainer.setObjectExpression(found);
                     return pe;
                  }
               }
            } else {
               return pe;
            }
         }
      }
   }

   protected Expression transformPropertyExpression(PropertyExpression pe) {
      boolean itlp = this.isTopLevelProperty;
      boolean ipe = this.inPropertyExpression;
      Expression objectExpression = pe.getObjectExpression();
      this.inPropertyExpression = true;
      this.isTopLevelProperty = objectExpression.getClass() != PropertyExpression.class;
      objectExpression = this.transform(objectExpression);
      this.inPropertyExpression = false;
      Expression property = this.transform(pe.getProperty());
      this.isTopLevelProperty = itlp;
      this.inPropertyExpression = ipe;
      boolean spreadSafe = pe.isSpreadSafe();
      PropertyExpression old = pe;
      pe = new PropertyExpression(objectExpression, property, pe.isSafe());
      pe.setSpreadSafe(spreadSafe);
      pe.setSourcePosition(old);
      String className = this.lookupClassName(pe);
      if (className != null) {
         ClassNode type = ClassHelper.make(className);
         if (this.resolve(type)) {
            Expression ret = new ClassExpression(type);
            ret.setSourcePosition(pe);
            return ret;
         }
      }

      if (objectExpression instanceof ClassExpression && pe.getPropertyAsString() != null) {
         ClassExpression ce = (ClassExpression)objectExpression;
         ClassNode type = ClassHelper.make(ce.getType().getName() + "$" + pe.getPropertyAsString());
         if (this.resolve(type, false, false, false)) {
            Expression ret = new ClassExpression(type);
            ret.setSourcePosition(ce);
            return ret;
         }
      }

      Expression ret = pe;
      this.checkThisAndSuperAsPropertyAccess(pe);
      if (this.isTopLevelProperty) {
         ret = this.correctClassClassChain(pe);
      }

      return (Expression)ret;
   }

   private void checkThisAndSuperAsPropertyAccess(PropertyExpression expression) {
      if (!expression.isImplicitThis()) {
         String prop = expression.getPropertyAsString();
         if (prop != null) {
            if (prop.equals("this") || prop.equals("super")) {
               if (prop.equals("super")) {
                  this.addError("Inner classes referencing outer classes using super is not supported yet.", expression);
               }

               if (!(expression.getObjectExpression() instanceof ClassExpression)) {
                  this.addError("The usage of '.this' or '.super' requires an explicit class in front.", expression);
               } else if (!(this.currentClass instanceof InnerClassNode)) {
                  this.addError("The usage of '.this' and '.super' is only allowed in a inner class", expression);
               } else {
                  ClassNode type = expression.getObjectExpression().getType();

                  ClassNode iterType;
                  for(iterType = this.currentClass; iterType != null && !iterType.equals(type); iterType = iterType.getOuterClass()) {
                  }

                  if (iterType == null) {
                     this.addError("The class '" + type.getName() + "' needs to be an " + "outer class of '" + this.currentClass.getName() + "'.", expression);
                  }

                  if ((this.currentClass.getModifiers() & 8) != 0) {
                     if (this.currentScope.isInStaticContext()) {
                        this.addError("The usage of '.this' and '.super' is only in nonstatic context", expression);
                     }
                  }
               }
            }
         }
      }
   }

   protected Expression transformVariableExpression(VariableExpression ve) {
      Variable v = ve.getAccessedVariable();
      if (!(v instanceof DynamicVariable) && !this.checkingVariableTypeInDeclaration) {
         return ve;
      } else {
         if (v instanceof DynamicVariable) {
            String name = ve.getName();
            ClassNode t = ClassHelper.make(name);
            boolean isClass = ((ClassNode)t).isResolved();
            if (!isClass) {
               if (Character.isLowerCase(name.charAt(0))) {
                  t = new ResolveVisitor.LowerCaseClass(name);
               }

               isClass = this.resolve((ClassNode)t);
               if (!isClass) {
                  isClass = this.resolveToInnerEnum((ClassNode)t);
               }
            }

            if (isClass) {
               for(VariableScope scope = this.currentScope; scope != null && !scope.isRoot() && !scope.isRoot() && scope.removeReferencedClassVariable(ve.getName()) != null; scope = scope.getParent()) {
               }

               ClassExpression ce = new ClassExpression((ClassNode)t);
               ce.setSourcePosition(ve);
               return ce;
            }
         }

         this.resolveOrFail(ve.getType(), ve);
         return ve;
      }
   }

   private boolean testVanillaNameForClass(String name) {
      if (name != null && name.length() != 0) {
         return !Character.isLowerCase(name.charAt(0));
      } else {
         return false;
      }
   }

   protected Expression transformBinaryExpression(BinaryExpression be) {
      Expression left = this.transform(be.getLeftExpression());
      int type = be.getOperation().getType();
      if ((type == 1100 || type == 100) && left instanceof ClassExpression) {
         ClassExpression ce = (ClassExpression)left;
         String error = "you tried to assign a value to the class '" + ce.getType().getName() + "'";
         if (ce.getType().isScript()) {
            error = error + ". Do you have a script with this name?";
         }

         this.addError(error, be.getLeftExpression());
         return be;
      } else {
         if (left instanceof ClassExpression) {
            if (be.getRightExpression() instanceof ListExpression) {
               ListExpression list = (ListExpression)be.getRightExpression();
               if (list.getExpressions().isEmpty()) {
                  ClassExpression ce = new ClassExpression(left.getType().makeArray());
                  ce.setSourcePosition(be);
                  return ce;
               }

               boolean map = true;
               Iterator i$ = list.getExpressions().iterator();

               while(i$.hasNext()) {
                  Expression expression = (Expression)i$.next();
                  if (!(expression instanceof MapEntryExpression)) {
                     map = false;
                     break;
                  }
               }

               if (map) {
                  MapExpression me = new MapExpression();
                  Iterator i$ = list.getExpressions().iterator();

                  while(i$.hasNext()) {
                     Expression expression = (Expression)i$.next();
                     me.addMapEntryExpression((MapEntryExpression)expression);
                  }

                  me.setSourcePosition(list);
                  CastExpression ce = new CastExpression(left.getType(), me);
                  ce.setSourcePosition(be);
                  return ce;
               }
            }

            if (be.getRightExpression() instanceof MapEntryExpression) {
               MapExpression me = new MapExpression();
               me.addMapEntryExpression((MapEntryExpression)be.getRightExpression());
               me.setSourcePosition(be.getRightExpression());
               CastExpression ce = new CastExpression(left.getType(), me);
               ce.setSourcePosition(be);
               return ce;
            }
         }

         Expression right = this.transform(be.getRightExpression());
         be.setLeftExpression(left);
         be.setRightExpression(right);
         return be;
      }
   }

   protected Expression transformClosureExpression(ClosureExpression ce) {
      boolean oldInClosure = this.inClosure;
      this.inClosure = true;
      Parameter[] paras = ce.getParameters();
      if (paras != null) {
         Parameter[] arr$ = paras;
         int len$ = paras.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Parameter para = arr$[i$];
            ClassNode t = para.getType();
            this.resolveOrFail(t, ce);
            if (para.hasInitialExpression()) {
               Object initialVal = para.getInitialExpression();
               if (initialVal instanceof Expression) {
                  para.setInitialExpression(this.transform((Expression)initialVal));
               }
            }

            this.visitAnnotations(para);
         }
      }

      Statement code = ce.getCode();
      if (code != null) {
         code.visit(this);
      }

      this.inClosure = oldInClosure;
      return ce;
   }

   protected Expression transformConstructorCallExpression(ConstructorCallExpression cce) {
      ClassNode type = cce.getType();
      this.resolveOrFail(type, cce);
      if (Modifier.isAbstract(type.getModifiers())) {
         this.addError("You cannot create an instance from the abstract " + this.getDescription(type) + ".", cce);
      }

      this.isSpecialConstructorCall = cce.isSpecialCall();
      Expression ret = cce.transformExpression(this);
      this.isSpecialConstructorCall = false;
      return ret;
   }

   private String getDescription(ClassNode node) {
      return (node.isInterface() ? "interface" : "class") + " '" + node.getName() + "'";
   }

   protected Expression transformMethodCallExpression(MethodCallExpression mce) {
      Expression args = this.transform(mce.getArguments());
      Expression method = this.transform(mce.getMethod());
      Expression object = this.transform(mce.getObjectExpression());
      this.resolveGenericsTypes(mce.getGenericsTypes());
      MethodCallExpression result = new MethodCallExpression(object, method, args);
      result.setSafe(mce.isSafe());
      result.setImplicitThis(mce.isImplicitThis());
      result.setSpreadSafe(mce.isSpreadSafe());
      result.setSourcePosition(mce);
      result.setGenericsTypes(mce.getGenericsTypes());
      return result;
   }

   protected Expression transformDeclarationExpression(DeclarationExpression de) {
      Expression oldLeft = de.getLeftExpression();
      this.checkingVariableTypeInDeclaration = true;
      Expression left = this.transform(oldLeft);
      this.checkingVariableTypeInDeclaration = false;
      if (left instanceof ClassExpression) {
         ClassExpression ce = (ClassExpression)left;
         this.addError("you tried to assign a value to the class " + ce.getType().getName(), oldLeft);
         return de;
      } else {
         Expression right = this.transform(de.getRightExpression());
         if (right == de.getRightExpression()) {
            return de;
         } else {
            DeclarationExpression newDeclExpr = new DeclarationExpression(left, de.getOperation(), right);
            newDeclExpr.setSourcePosition(de);
            return newDeclExpr;
         }
      }
   }

   protected Expression transformAnnotationConstantExpression(AnnotationConstantExpression ace) {
      AnnotationNode an = (AnnotationNode)ace.getValue();
      ClassNode type = an.getClassNode();
      this.resolveOrFail(type, ", unable to find class for annotation", an);
      Iterator i$ = an.getMembers().entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, Expression> member = (Entry)i$.next();
         member.setValue(this.transform((Expression)member.getValue()));
      }

      return ace;
   }

   public void visitAnnotations(AnnotatedNode node) {
      List<AnnotationNode> annotations = node.getAnnotations();
      if (!annotations.isEmpty()) {
         Map<String, AnnotationNode> tmpAnnotations = new HashMap();
         Iterator i$ = annotations.iterator();

         while(true) {
            AnnotationNode an;
            do {
               if (!i$.hasNext()) {
                  return;
               }

               an = (AnnotationNode)i$.next();
            } while(an.isBuiltIn());

            ClassNode annType = an.getClassNode();
            this.resolveOrFail(annType, ",  unable to find class for annotation", an);
            Iterator i$ = an.getMembers().entrySet().iterator();

            while(i$.hasNext()) {
               Entry<String, Expression> member = (Entry)i$.next();
               Expression newValue = this.transform((Expression)member.getValue());
               newValue = this.transformInlineConstants(newValue);
               member.setValue(newValue);
               this.checkAnnotationMemberValue(newValue);
            }

            if (annType.isResolved()) {
               Class annTypeClass = annType.getTypeClass();
               Retention retAnn = (Retention)annTypeClass.getAnnotation(Retention.class);
               if (retAnn != null && retAnn.value().equals(RetentionPolicy.RUNTIME)) {
                  AnnotationNode anyPrevAnnNode = (AnnotationNode)tmpAnnotations.put(annTypeClass.getName(), an);
                  if (anyPrevAnnNode != null) {
                     this.addError("Cannot specify duplicate annotation on the same member : " + annType.getName(), an);
                  }
               }
            }
         }
      }
   }

   private Expression transformInlineConstants(Expression exp) {
      if (exp instanceof PropertyExpression) {
         PropertyExpression pe = (PropertyExpression)exp;
         if (pe.getObjectExpression() instanceof ClassExpression) {
            ClassExpression ce = (ClassExpression)pe.getObjectExpression();
            ClassNode type = ce.getType();
            if (type.isEnum()) {
               return exp;
            }

            FieldNode fn = type.getField(pe.getPropertyAsString());
            if (fn != null && !fn.isEnum() && fn.isStatic() && fn.isFinal() && fn.getInitialValueExpression() instanceof ConstantExpression) {
               return fn.getInitialValueExpression();
            }
         }
      } else {
         Iterator i$;
         if (exp instanceof ListExpression) {
            ListExpression le = (ListExpression)exp;
            ListExpression result = new ListExpression();
            i$ = le.getExpressions().iterator();

            while(i$.hasNext()) {
               Expression e = (Expression)i$.next();
               result.addExpression(this.transformInlineConstants(e));
            }

            return result;
         }

         if (exp instanceof AnnotationConstantExpression) {
            ConstantExpression ce = (ConstantExpression)exp;
            if (ce.getValue() instanceof AnnotationNode) {
               AnnotationNode an = (AnnotationNode)ce.getValue();
               i$ = an.getMembers().entrySet().iterator();

               while(i$.hasNext()) {
                  Entry<String, Expression> member = (Entry)i$.next();
                  member.setValue(this.transformInlineConstants((Expression)member.getValue()));
               }
            }
         }
      }

      return exp;
   }

   private void checkAnnotationMemberValue(Expression newValue) {
      if (newValue instanceof PropertyExpression) {
         PropertyExpression pe = (PropertyExpression)newValue;
         if (!(pe.getObjectExpression() instanceof ClassExpression)) {
            this.addError("unable to find class '" + pe.getText() + "' for annotation attribute constant", pe.getObjectExpression());
         }
      } else if (newValue instanceof ListExpression) {
         ListExpression le = (ListExpression)newValue;
         Iterator i$ = le.getExpressions().iterator();

         while(i$.hasNext()) {
            Expression e = (Expression)i$.next();
            this.checkAnnotationMemberValue(e);
         }
      }

   }

   public void visitClass(ClassNode node) {
      ClassNode oldNode = this.currentClass;
      Map<String, GenericsType> oldPNames = this.genericParameterNames;
      this.genericParameterNames = new HashMap(this.genericParameterNames);
      this.currentClass = node;
      this.resolveGenericsHeader(node.getGenericsTypes());
      ModuleNode module = node.getModule();
      if (!module.hasImportsResolved()) {
         List l = module.getImports();
         Iterator i$ = module.getImports().iterator();

         ImportNode importNode;
         ClassNode type;
         while(i$.hasNext()) {
            importNode = (ImportNode)i$.next();
            this.currImportNode = importNode;
            type = importNode.getType();
            if (this.resolve(type, false, false, true)) {
               this.currImportNode = null;
            } else {
               this.currImportNode = null;
               this.addError("unable to resolve class " + type.getName(), type);
            }
         }

         i$ = module.getStaticStarImports().values().iterator();

         label76:
         while(true) {
            while(true) {
               do {
                  if (!i$.hasNext()) {
                     i$ = module.getStaticImports().values().iterator();

                     while(i$.hasNext()) {
                        importNode = (ImportNode)i$.next();
                        type = importNode.getType();
                        if (!this.resolve(type, true, true, true)) {
                           this.addError("unable to resolve class " + type.getName(), type);
                        }
                     }

                     i$ = module.getStaticStarImports().values().iterator();

                     while(i$.hasNext()) {
                        importNode = (ImportNode)i$.next();
                        type = importNode.getType();
                        if (!this.resolve(type, true, true, true)) {
                           this.addError("unable to resolve class " + type.getName(), type);
                        }
                     }

                     module.setImportsResolved(true);
                     break label76;
                  }

                  importNode = (ImportNode)i$.next();
                  type = importNode.getType();
               } while(this.resolve(type, false, false, true));

               if (type.getPackageName() != null || node.getPackageName() == null) {
                  break;
               }

               String oldTypeName = type.getName();
               type.setName(node.getPackageName() + "." + oldTypeName);
               if (!this.resolve(type, false, false, true)) {
                  type.setName(oldTypeName);
                  break;
               }
            }

            this.addError("unable to resolve class " + type.getName(), type);
         }
      }

      ClassNode sn = node.getUnresolvedSuperClass();
      if (sn != null) {
         this.resolveOrFail(sn, node, true);
      }

      ClassNode[] arr$ = node.getInterfaces();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassNode anInterface = arr$[i$];
         this.resolveOrFail(anInterface, node, true);
      }

      this.checkCyclicInheritence(node, node.getUnresolvedSuperClass(), node.getInterfaces());
      super.visitClass(node);
      this.genericParameterNames = oldPNames;
      this.currentClass = oldNode;
   }

   private void checkCyclicInheritence(ClassNode originalNode, ClassNode parentToCompare, ClassNode[] interfacesToCompare) {
      ClassNode[] arr$;
      int len$;
      int i$;
      ClassNode intfToCompare;
      if (!originalNode.isInterface()) {
         if (parentToCompare == null) {
            return;
         }

         if (originalNode == parentToCompare.redirect()) {
            this.addError("Cyclic inheritance involving " + parentToCompare.getName() + " in class " + originalNode.getName(), originalNode);
            return;
         }

         if (interfacesToCompare != null && interfacesToCompare.length > 0) {
            arr$ = interfacesToCompare;
            len$ = interfacesToCompare.length;

            for(i$ = 0; i$ < len$; ++i$) {
               intfToCompare = arr$[i$];
               if (originalNode == intfToCompare.redirect()) {
                  this.addError("Cycle detected: the type " + originalNode.getName() + " cannot implement itself", originalNode);
                  return;
               }
            }
         }

         if (parentToCompare == ClassHelper.OBJECT_TYPE) {
            return;
         }

         this.checkCyclicInheritence(originalNode, parentToCompare.getUnresolvedSuperClass(), (ClassNode[])null);
      } else {
         if (interfacesToCompare == null || interfacesToCompare.length <= 0) {
            return;
         }

         arr$ = interfacesToCompare;
         len$ = interfacesToCompare.length;

         for(i$ = 0; i$ < len$; ++i$) {
            intfToCompare = arr$[i$];
            if (originalNode == intfToCompare.redirect()) {
               this.addError("Cyclic inheritance involving " + intfToCompare.getName() + " in interface " + originalNode.getName(), originalNode);
               return;
            }
         }

         arr$ = interfacesToCompare;
         len$ = interfacesToCompare.length;

         for(i$ = 0; i$ < len$; ++i$) {
            intfToCompare = arr$[i$];
            this.checkCyclicInheritence(originalNode, (ClassNode)null, intfToCompare.getInterfaces());
         }
      }

   }

   public void visitCatchStatement(CatchStatement cs) {
      this.resolveOrFail(cs.getExceptionType(), cs);
      if (cs.getExceptionType() == ClassHelper.DYNAMIC_TYPE) {
         cs.getVariable().setType(ClassHelper.make(Exception.class));
      }

      super.visitCatchStatement(cs);
   }

   public void visitForLoop(ForStatement forLoop) {
      this.resolveOrFail(forLoop.getVariableType(), forLoop);
      super.visitForLoop(forLoop);
   }

   public void visitBlockStatement(BlockStatement block) {
      VariableScope oldScope = this.currentScope;
      this.currentScope = block.getVariableScope();
      super.visitBlockStatement(block);
      this.currentScope = oldScope;
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   private void resolveGenericsTypes(GenericsType[] types) {
      if (types != null) {
         this.currentClass.setUsingGenerics(true);
         GenericsType[] arr$ = types;
         int len$ = types.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            GenericsType type = arr$[i$];
            this.resolveGenericsType(type);
         }

      }
   }

   private void resolveGenericsHeader(GenericsType[] types) {
      if (types != null) {
         this.currentClass.setUsingGenerics(true);
         GenericsType[] arr$ = types;
         int len$ = types.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            GenericsType type = arr$[i$];
            ClassNode classNode = type.getType();
            String name = type.getName();
            ClassNode[] bounds = type.getUpperBounds();
            if (bounds == null) {
               this.genericParameterNames.put(name, type);
               classNode.setRedirect(ClassHelper.OBJECT_TYPE);
               type.setPlaceholder(true);
            } else {
               boolean nameAdded = false;
               ClassNode[] arr$ = bounds;
               int len$ = bounds.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  ClassNode upperBound = arr$[i$];
                  if (!nameAdded && upperBound != null || !this.resolve(classNode)) {
                     this.genericParameterNames.put(name, type);
                     type.setPlaceholder(true);
                     classNode.setRedirect(upperBound);
                     nameAdded = true;
                  }

                  this.resolveOrFail(upperBound, classNode);
               }
            }
         }

      }
   }

   private void resolveGenericsType(GenericsType genericsType) {
      if (!genericsType.isResolved()) {
         this.currentClass.setUsingGenerics(true);
         ClassNode type = genericsType.getType();
         String name = type.getName();
         ClassNode[] bounds = genericsType.getUpperBounds();
         if (!this.genericParameterNames.containsKey(name)) {
            if (bounds != null) {
               ClassNode[] arr$ = bounds;
               int len$ = bounds.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  ClassNode upperBound = arr$[i$];
                  this.resolveOrFail(upperBound, genericsType);
                  type.setRedirect(upperBound);
                  this.resolveGenericsTypes(upperBound.getGenericsTypes());
               }
            } else if (genericsType.isWildcard()) {
               type.setRedirect(ClassHelper.OBJECT_TYPE);
            } else {
               this.resolveOrFail(type, genericsType);
            }
         } else {
            GenericsType gt = (GenericsType)this.genericParameterNames.get(name);
            type.setRedirect(gt.getType());
            genericsType.setPlaceholder(true);
         }

         if (genericsType.getLowerBound() != null) {
            this.resolveOrFail(genericsType.getLowerBound(), genericsType);
         }

         this.resolveGenericsTypes(type.getGenericsTypes());
         genericsType.setResolved(genericsType.getType().isResolved());
      }
   }

   private static class LowerCaseClass extends ClassNode {
      String className;

      public LowerCaseClass(String name) {
         super(name, 1, ClassHelper.OBJECT_TYPE);
         this.isPrimaryNode = false;
         this.className = name;
      }

      public String getName() {
         return this.redirect() != this ? super.getName() : this.className;
      }

      public boolean hasPackageName() {
         return this.redirect() != this ? super.hasPackageName() : false;
      }

      public String setName(String name) {
         if (this.redirect() != this) {
            return super.setName(name);
         } else {
            throw new GroovyBugError("ConstructedClassWithPackage#setName should not be called");
         }
      }
   }

   private static class ConstructedClassWithPackage extends ClassNode {
      String prefix;
      String className;

      public ConstructedClassWithPackage(String pkg, String name) {
         super(pkg + name, 1, ClassHelper.OBJECT_TYPE);
         this.isPrimaryNode = false;
         this.prefix = pkg;
         this.className = name;
      }

      public String getName() {
         return this.redirect() != this ? super.getName() : this.prefix + this.className;
      }

      public boolean hasPackageName() {
         if (this.redirect() != this) {
            return super.hasPackageName();
         } else {
            return this.className.indexOf(46) != -1;
         }
      }

      public String setName(String name) {
         if (this.redirect() != this) {
            return super.setName(name);
         } else {
            throw new GroovyBugError("ConstructedClassWithPackage#setName should not be called");
         }
      }
   }
}
