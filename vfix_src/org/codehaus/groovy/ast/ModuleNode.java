package org.codehaus.groovy.ast;

import groovy.lang.Binding;
import groovyjarjarasm.asm.Opcodes;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.InvokerHelper;

public class ModuleNode extends ASTNode implements Opcodes {
   private BlockStatement statementBlock = new BlockStatement();
   List<ClassNode> classes = new LinkedList();
   private List<MethodNode> methods = new ArrayList();
   private Map<String, ImportNode> imports = new HashMap();
   private List<ImportNode> starImports = new ArrayList();
   private Map<String, ImportNode> staticImports = new LinkedHashMap();
   private Map<String, ImportNode> staticStarImports = new LinkedHashMap();
   private CompileUnit unit;
   private PackageNode packageNode;
   private String description;
   private boolean createClassForStatements = true;
   private transient SourceUnit context;
   private boolean importsResolved = false;
   private ClassNode scriptDummy;
   private String mainClassName = null;

   public ModuleNode(SourceUnit context) {
      this.context = context;
   }

   public ModuleNode(CompileUnit unit) {
      this.unit = unit;
   }

   public BlockStatement getStatementBlock() {
      return this.statementBlock;
   }

   public List<MethodNode> getMethods() {
      return this.methods;
   }

   public List<ClassNode> getClasses() {
      if (this.createClassForStatements && (!this.statementBlock.isEmpty() || !this.methods.isEmpty() || this.isPackageInfo())) {
         ClassNode mainClass = this.createStatementsClass();
         this.mainClassName = mainClass.getName();
         this.createClassForStatements = false;
         this.classes.add(0, mainClass);
         mainClass.setModule(this);
         this.addToCompileUnit(mainClass);
      }

      return this.classes;
   }

   private boolean isPackageInfo() {
      return this.context != null && this.context.getName() != null && this.context.getName().endsWith("package-info.groovy");
   }

   public List<ImportNode> getImports() {
      return new ArrayList(this.imports.values());
   }

   /** @deprecated */
   @Deprecated
   public List<String> getImportPackages() {
      List<String> result = new ArrayList();
      Iterator i$ = this.starImports.iterator();

      while(i$.hasNext()) {
         ImportNode importStarNode = (ImportNode)i$.next();
         result.add(importStarNode.getPackageName());
      }

      return result;
   }

   public List<ImportNode> getStarImports() {
      return this.starImports;
   }

   public ClassNode getImportType(String alias) {
      ImportNode importNode = (ImportNode)this.imports.get(alias);
      return importNode == null ? null : importNode.getType();
   }

   public ImportNode getImport(String alias) {
      return (ImportNode)this.imports.get(alias);
   }

   public void addImport(String alias, ClassNode type) {
      this.addImport(alias, type, new ArrayList());
   }

   public void addImport(String alias, ClassNode type, List<AnnotationNode> annotations) {
      ImportNode importNode = new ImportNode(type, alias);
      this.imports.put(alias, importNode);
      importNode.addAnnotations(annotations);
   }

   /** @deprecated */
   @Deprecated
   public String[] addImportPackage(String packageName) {
      this.addStarImport(packageName);
      return new String[0];
   }

   public void addStarImport(String packageName) {
      this.addStarImport(packageName, new ArrayList());
   }

   public void addStarImport(String packageName, List<AnnotationNode> annotations) {
      ImportNode importNode = new ImportNode(packageName);
      importNode.addAnnotations(annotations);
      this.starImports.add(importNode);
   }

   public void addStatement(Statement node) {
      this.statementBlock.addStatement(node);
   }

   public void addClass(ClassNode node) {
      if (this.classes.isEmpty()) {
         this.mainClassName = node.getName();
      }

      this.classes.add(node);
      node.setModule(this);
      this.addToCompileUnit(node);
   }

   private void addToCompileUnit(ClassNode node) {
      if (this.unit != null) {
         this.unit.addClass(node);
      }

   }

   public void addMethod(MethodNode node) {
      this.methods.add(node);
   }

   public void visit(GroovyCodeVisitor visitor) {
   }

   public String getPackageName() {
      return this.packageNode == null ? null : this.packageNode.getName();
   }

   public PackageNode getPackage() {
      return this.packageNode;
   }

   public void setPackage(PackageNode packageNode) {
      this.packageNode = packageNode;
   }

   public void setPackageName(String packageName) {
      this.packageNode = new PackageNode(packageName);
   }

   public boolean hasPackageName() {
      return this.packageNode != null && this.packageNode.getName() != null;
   }

   public boolean hasPackage() {
      return this.packageNode != null;
   }

   public SourceUnit getContext() {
      return this.context;
   }

   public String getDescription() {
      return this.context != null ? this.context.getName() : this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public CompileUnit getUnit() {
      return this.unit;
   }

   void setUnit(CompileUnit unit) {
      this.unit = unit;
   }

   public ClassNode getScriptClassDummy() {
      if (this.scriptDummy != null) {
         this.setScriptBaseClassFromConfig(this.scriptDummy);
         return this.scriptDummy;
      } else {
         String name = this.getPackageName();
         if (name == null) {
            name = "";
         }

         if (this.getDescription() == null) {
            throw new RuntimeException("Cannot generate main(String[]) class for statements when we have no file description");
         } else {
            name = name + this.extractClassFromFileDescription();
            ClassNode classNode;
            if (this.isPackageInfo()) {
               classNode = new ClassNode(name, 1536, ClassHelper.OBJECT_TYPE);
            } else {
               classNode = new ClassNode(name, 1, ClassHelper.SCRIPT_TYPE);
               this.setScriptBaseClassFromConfig(classNode);
               classNode.setScript(true);
               classNode.setScriptBody(true);
            }

            this.scriptDummy = classNode;
            return classNode;
         }
      }
   }

   private void setScriptBaseClassFromConfig(ClassNode cn) {
      if (this.unit != null) {
         String baseClassName = this.unit.getConfig().getScriptBaseClass();
         if (baseClassName != null && !cn.getSuperClass().getName().equals(baseClassName)) {
            cn.setSuperClass(ClassHelper.make(baseClassName));
         }
      }

   }

   protected ClassNode createStatementsClass() {
      ClassNode classNode = this.getScriptClassDummy();
      if (classNode.getName().endsWith("package-info")) {
         return classNode;
      } else {
         this.handleMainMethodIfPresent(this.methods);
         classNode.addMethod(new MethodNode("main", 9, ClassHelper.VOID_TYPE, new Parameter[]{new Parameter(ClassHelper.STRING_TYPE.makeArray(), "args")}, ClassNode.EMPTY_ARRAY, new ExpressionStatement(new MethodCallExpression(new ClassExpression(ClassHelper.make(InvokerHelper.class)), "runScript", new ArgumentListExpression(new ClassExpression(classNode), new VariableExpression("args"))))));
         classNode.addMethod(new MethodNode("run", 1, ClassHelper.OBJECT_TYPE, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, this.statementBlock));
         classNode.addConstructor(1, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, new BlockStatement());
         Statement stmt = new ExpressionStatement(new MethodCallExpression(new VariableExpression("super"), "setBinding", new ArgumentListExpression(new VariableExpression("context"))));
         classNode.addConstructor(1, new Parameter[]{new Parameter(ClassHelper.make(Binding.class), "context")}, ClassNode.EMPTY_ARRAY, stmt);
         Iterator i$ = this.methods.iterator();

         while(i$.hasNext()) {
            MethodNode node = (MethodNode)i$.next();
            int modifiers = node.getModifiers();
            if ((modifiers & 1024) != 0) {
               throw new RuntimeException("Cannot use abstract methods in a script, they are only available inside classes. Method: " + node.getName());
            }

            node.setModifiers(modifiers);
            classNode.addMethod(node);
         }

         return classNode;
      }
   }

   private void handleMainMethodIfPresent(List methods) {
      boolean found = false;
      Iterator iter = methods.iterator();

      while(true) {
         MethodNode node;
         do {
            do {
               do {
                  if (!iter.hasNext()) {
                     return;
                  }

                  node = (MethodNode)iter.next();
               } while(!node.getName().equals("main"));

               int modifiers = node.getModifiers();
            } while(!node.isStatic());
         } while(node.getParameters().length != 1);

         ClassNode argType = node.getParameters()[0].getType();
         ClassNode retType = node.getReturnType();
         boolean argTypeMatches = argType.equals(ClassHelper.OBJECT_TYPE) || argType.getName().contains("String[]");
         boolean retTypeMatches = retType == ClassHelper.VOID_TYPE || retType == ClassHelper.OBJECT_TYPE;
         if (retTypeMatches && argTypeMatches) {
            if (found) {
               throw new RuntimeException("Repetitive main method found.");
            }

            found = true;
            if (this.statementBlock.isEmpty()) {
               this.addStatement(node.getCode());
            }

            iter.remove();
         }
      }
   }

   protected String extractClassFromFileDescription() {
      String answer = this.getDescription();
      int slashIdx = answer.lastIndexOf(47);
      int separatorIdx = answer.lastIndexOf(File.separatorChar);
      int dotIdx = answer.lastIndexOf(46);
      if (dotIdx > 0 && dotIdx > Math.max(slashIdx, separatorIdx)) {
         answer = answer.substring(0, dotIdx);
      }

      if (slashIdx >= 0) {
         answer = answer.substring(slashIdx + 1);
      }

      separatorIdx = answer.lastIndexOf(File.separatorChar);
      if (separatorIdx >= 0) {
         answer = answer.substring(separatorIdx + 1);
      }

      return answer;
   }

   public boolean isEmpty() {
      return this.classes.isEmpty() && this.statementBlock.getStatements().isEmpty();
   }

   public void sortClasses() {
      if (!this.isEmpty()) {
         List<ClassNode> classes = this.getClasses();
         LinkedList<ClassNode> sorted = new LinkedList();

         label44:
         for(int level = 1; !classes.isEmpty(); ++level) {
            Iterator cni = classes.iterator();

            while(true) {
               ClassNode cn;
               ClassNode sn;
               do {
                  if (!cni.hasNext()) {
                     continue label44;
                  }

                  cn = (ClassNode)cni.next();
                  sn = cn;

                  for(int i = 0; sn != null && i < level; ++i) {
                     sn = sn.getSuperClass();
                  }
               } while(sn != null && sn.isPrimaryClassNode());

               cni.remove();
               sorted.addLast(cn);
            }
         }

         this.classes = sorted;
      }
   }

   public boolean hasImportsResolved() {
      return this.importsResolved;
   }

   public void setImportsResolved(boolean importsResolved) {
      this.importsResolved = importsResolved;
   }

   /** @deprecated */
   @Deprecated
   public Map<String, ClassNode> getStaticImportAliases() {
      Map<String, ClassNode> result = new HashMap();
      Iterator i$ = this.staticImports.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, ImportNode> entry = (Entry)i$.next();
         result.put(entry.getKey(), ((ImportNode)entry.getValue()).getType());
      }

      return result;
   }

   /** @deprecated */
   @Deprecated
   public Map<String, ClassNode> getStaticImportClasses() {
      Map<String, ClassNode> result = new HashMap();
      Iterator i$ = this.staticStarImports.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, ImportNode> entry = (Entry)i$.next();
         result.put(entry.getKey(), ((ImportNode)entry.getValue()).getType());
      }

      return result;
   }

   /** @deprecated */
   @Deprecated
   public Map<String, String> getStaticImportFields() {
      Map<String, String> result = new HashMap();
      Iterator i$ = this.staticImports.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, ImportNode> entry = (Entry)i$.next();
         result.put(entry.getKey(), ((ImportNode)entry.getValue()).getFieldName());
      }

      return result;
   }

   public Map<String, ImportNode> getStaticImports() {
      return this.staticImports;
   }

   public Map<String, ImportNode> getStaticStarImports() {
      return this.staticStarImports;
   }

   /** @deprecated */
   @Deprecated
   public void addStaticMethodOrField(ClassNode type, String fieldName, String alias) {
      this.addStaticImport(type, fieldName, alias);
   }

   public void addStaticImport(ClassNode type, String fieldName, String alias) {
      this.addStaticImport(type, fieldName, alias, new ArrayList());
   }

   public void addStaticImport(ClassNode type, String fieldName, String alias, List<AnnotationNode> annotations) {
      ImportNode node = new ImportNode(type, fieldName, alias);
      node.addAnnotations(annotations);
      this.staticImports.put(alias, node);
   }

   /** @deprecated */
   @Deprecated
   public void addStaticImportClass(String name, ClassNode type) {
      this.addStaticStarImport(name, type);
   }

   public void addStaticStarImport(String name, ClassNode type) {
      this.addStaticStarImport(name, type, new ArrayList());
   }

   public void addStaticStarImport(String name, ClassNode type, List<AnnotationNode> annotations) {
      ImportNode node = new ImportNode(type);
      node.addAnnotations(annotations);
      this.staticStarImports.put(name, node);
   }

   public String getMainClassName() {
      return this.mainClassName;
   }
}
