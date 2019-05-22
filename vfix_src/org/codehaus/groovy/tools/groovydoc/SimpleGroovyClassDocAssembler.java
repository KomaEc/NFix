package org.codehaus.groovy.tools.groovydoc;

import groovyjarjarantlr.collections.AST;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.LineColumn;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter;
import org.codehaus.groovy.control.ResolveVisitor;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyConstructorDoc;
import org.codehaus.groovy.groovydoc.GroovyFieldDoc;
import org.codehaus.groovy.groovydoc.GroovyMethodDoc;
import org.codehaus.groovy.groovydoc.GroovyType;

public class SimpleGroovyClassDocAssembler extends VisitorAdapter implements GroovyTokenTypes {
   private static final String FS = "/";
   private static final Pattern PREV_JAVADOC_COMMENT_PATTERN = Pattern.compile("(?s)/\\*\\*(.*?)\\*/");
   private final Stack<GroovySourceAST> stack;
   private Map<String, GroovyClassDoc> classDocs;
   private List<String> importedClassesAndPackages;
   private List<LinkArgument> links;
   private Properties properties;
   private SimpleGroovyFieldDoc currentFieldDoc;
   private SourceBuffer sourceBuffer;
   private String packagePath;
   private LineColumn lastLineCol;
   private boolean insideEnum;
   private Map<String, SimpleGroovyClassDoc> foundClasses;
   private boolean isGroovy;
   private boolean deferSetup;
   private String className;

   public SimpleGroovyClassDocAssembler(String packagePath, String file, SourceBuffer sourceBuffer, List<LinkArgument> links, Properties properties, boolean isGroovy) {
      this.sourceBuffer = sourceBuffer;
      this.packagePath = packagePath;
      this.links = links;
      this.properties = properties;
      this.isGroovy = isGroovy;
      this.stack = new Stack();
      this.classDocs = new HashMap();
      this.className = file;
      if (file != null) {
         int idx = file.lastIndexOf(".");
         this.className = file.substring(0, idx);
      }

      this.deferSetup = packagePath.equals("DefaultPackage");
      this.importedClassesAndPackages = new ArrayList();
      if (!this.deferSetup) {
         this.setUpImports(packagePath, links, isGroovy, this.className);
      }

      this.lastLineCol = new LineColumn(1, 1);
   }

   private void setUpImports(String packagePath, List<LinkArgument> links, boolean isGroovy, String className) {
      this.importedClassesAndPackages.add(packagePath + "/*");
      if (isGroovy) {
         String[] arr$ = ResolveVisitor.DEFAULT_IMPORTS;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String pkg = arr$[i$];
            this.importedClassesAndPackages.add(pkg.replace('.', '/') + "*");
         }
      } else {
         this.importedClassesAndPackages.add("java/lang/*");
      }

      SimpleGroovyClassDoc currentClassDoc = new SimpleGroovyClassDoc(this.importedClassesAndPackages, className, links);
      currentClassDoc.setFullPathName(packagePath + "/" + className);
      currentClassDoc.setGroovy(isGroovy);
      this.classDocs.put(currentClassDoc.getFullPathName(), currentClassDoc);
   }

   public Map<String, GroovyClassDoc> getGroovyClassDocs() {
      this.postProcessClassDocs();
      return this.classDocs;
   }

   public void visitInterfaceDef(GroovySourceAST t, int visit) {
      this.visitClassDef(t, visit);
   }

   public void visitEnumDef(GroovySourceAST t, int visit) {
      this.visitClassDef(t, visit);
      SimpleGroovyClassDoc currentClassDoc = this.getCurrentOrTopLevelClassDoc(t);
      if (visit == 4 && currentClassDoc != null) {
         this.adjustForAutomaticEnumMethods(currentClassDoc);
      }

   }

   public void visitAnnotationDef(GroovySourceAST t, int visit) {
      this.visitClassDef(t, visit);
   }

   public void visitClassDef(GroovySourceAST t, int visit) {
      if (visit == 1) {
         SimpleGroovyClassDoc parent = this.getCurrentClassDoc();
         String shortName = this.getIdentFor(t);
         String className = shortName;
         if (parent != null && this.isNested() && !this.insideAnonymousInnerClass()) {
            className = parent.name() + "." + shortName;
         } else {
            this.foundClasses = new HashMap();
         }

         SimpleGroovyClassDoc current = (SimpleGroovyClassDoc)this.classDocs.get(this.packagePath + "/" + className);
         if (current == null) {
            current = new SimpleGroovyClassDoc(this.importedClassesAndPackages, className, this.links);
            current.setGroovy(this.isGroovy);
         }

         current.setRawCommentText(this.getJavaDocCommentsBeforeNode(t));
         current.setFullPathName(this.packagePath + "/" + current.name());
         current.setTokenType(t.getType());
         this.processAnnotations(t, current);
         this.processModifiers(t, current);
         this.classDocs.put(current.getFullPathName(), current);
         this.foundClasses.put(shortName, current);
         if (parent != null) {
            parent.addNested(current);
            current.setOuter(parent);
         }
      }

   }

   public void visitPackageDef(GroovySourceAST t, int visit) {
      if (visit == 1 && this.deferSetup) {
         String packageWithSlashes = this.extractImportPath(t);
         this.setUpImports(packageWithSlashes, this.links, this.isGroovy, this.className);
      }

   }

   public void visitImport(GroovySourceAST t, int visit) {
      if (visit == 1) {
         String importTextWithSlashesInsteadOfDots = this.extractImportPath(t);
         this.importedClassesAndPackages.add(importTextWithSlashesInsteadOfDots);
      }

   }

   public void visitExtendsClause(GroovySourceAST t, int visit) {
      SimpleGroovyClassDoc currentClassDoc = this.getCurrentClassDoc();
      if (visit == 1) {
         Iterator i$ = this.findTypeNames(t).iterator();

         while(i$.hasNext()) {
            GroovySourceAST superClassNode = (GroovySourceAST)i$.next();
            String superClassName = this.extractName(superClassNode);
            if (currentClassDoc.isInterface()) {
               currentClassDoc.addInterfaceName(superClassName);
            } else {
               currentClassDoc.setSuperClassName(superClassName);
            }
         }
      }

   }

   public void visitImplementsClause(GroovySourceAST t, int visit) {
      if (visit == 1) {
         Iterator i$ = this.findTypeNames(t).iterator();

         while(i$.hasNext()) {
            GroovySourceAST classNode = (GroovySourceAST)i$.next();
            this.getCurrentClassDoc().addInterfaceName(this.extractName(classNode));
         }
      }

   }

   private List<GroovySourceAST> findTypeNames(GroovySourceAST t) {
      List<GroovySourceAST> types = new ArrayList();

      for(AST child = t.getFirstChild(); child != null; child = child.getNextSibling()) {
         GroovySourceAST groovySourceAST = (GroovySourceAST)child;
         if (groovySourceAST.getType() == 12) {
            types.add((GroovySourceAST)groovySourceAST.getFirstChild());
         } else {
            types.add(groovySourceAST);
         }
      }

      return types;
   }

   public void visitCtorIdent(GroovySourceAST t, int visit) {
      if (visit == 1 && !this.insideEnum && !this.insideAnonymousInnerClass()) {
         SimpleGroovyClassDoc currentClassDoc = this.getCurrentClassDoc();
         SimpleGroovyConstructorDoc currentConstructorDoc = new SimpleGroovyConstructorDoc(currentClassDoc.name(), currentClassDoc);
         currentConstructorDoc.setRawCommentText(this.getJavaDocCommentsBeforeNode(t));
         this.processModifiers(t, currentConstructorDoc);
         this.addParametersTo(t, currentConstructorDoc);
         this.processAnnotations(t, currentConstructorDoc);
         currentClassDoc.add((GroovyConstructorDoc)currentConstructorDoc);
      }

   }

   public void visitMethodDef(GroovySourceAST t, int visit) {
      if (visit == 1 && !this.insideEnum && !this.insideAnonymousInnerClass()) {
         SimpleGroovyClassDoc currentClassDoc = this.getCurrentClassDoc();
         if (currentClassDoc == null) {
            if (!"true".equals(this.properties.getProperty("processScripts", "true"))) {
               return;
            }

            currentClassDoc = new SimpleGroovyClassDoc(this.importedClassesAndPackages, this.className, this.links);
            currentClassDoc.setFullPathName(this.packagePath + "/" + this.className);
            currentClassDoc.setPublic(true);
            currentClassDoc.setScript(true);
            currentClassDoc.setGroovy(this.isGroovy);
            currentClassDoc.setSuperClassName("groovy/lang/Script");
            if ("true".equals(this.properties.getProperty("includeMainForScripts", "true"))) {
               currentClassDoc.add(this.createMainMethod(currentClassDoc));
            }

            this.classDocs.put(currentClassDoc.getFullPathName(), currentClassDoc);
            if (this.foundClasses == null) {
               this.foundClasses = new HashMap();
            }

            this.foundClasses.put(this.className, currentClassDoc);
         }

         String methodName = this.getIdentFor(t);
         SimpleGroovyMethodDoc currentMethodDoc = new SimpleGroovyMethodDoc(methodName, currentClassDoc);
         currentMethodDoc.setRawCommentText(this.getJavaDocCommentsBeforeNode(t));
         this.processModifiers(t, currentMethodDoc);
         currentMethodDoc.setReturnType(new SimpleGroovyType(this.getTypeOrDefault(t)));
         this.addParametersTo(t, currentMethodDoc);
         this.processAnnotations(t, currentMethodDoc);
         currentClassDoc.add((GroovyMethodDoc)currentMethodDoc);
      }

   }

   private GroovyMethodDoc createMainMethod(SimpleGroovyClassDoc currentClassDoc) {
      SimpleGroovyMethodDoc mainMethod = new SimpleGroovyMethodDoc("main", currentClassDoc);
      mainMethod.setPublic(true);
      mainMethod.setStatic(true);
      mainMethod.setCommentText("Implicit main method for Groovy Scripts");
      mainMethod.setFirstSentenceCommentText(mainMethod.commentText());
      SimpleGroovyParameter args = new SimpleGroovyParameter("args");
      GroovyType argsType = new SimpleGroovyType("java.lang.String[]");
      args.setType(argsType);
      mainMethod.add(args);
      GroovyType returnType = new SimpleGroovyType("void");
      mainMethod.setReturnType(returnType);
      return mainMethod;
   }

   public void visitAnnotationFieldDef(GroovySourceAST t, int visit) {
      if (visit == 1) {
         this.visitVariableDef(t, visit);
         String defaultText = this.getDefaultValue(t);
         if (defaultText != null) {
            this.currentFieldDoc.setConstantValueExpression(defaultText);
            String orig = this.currentFieldDoc.getRawCommentText();
            this.currentFieldDoc.setRawCommentText(orig + "\n* @default " + defaultText);
         }
      }

   }

   public void visitEnumConstantDef(GroovySourceAST t, int visit) {
      if (visit == 1) {
         SimpleGroovyClassDoc currentClassDoc = this.getCurrentClassDoc();
         this.insideEnum = true;
         String enumConstantName = this.getIdentFor(t);
         SimpleGroovyFieldDoc currentEnumConstantDoc = new SimpleGroovyFieldDoc(enumConstantName, currentClassDoc);
         currentEnumConstantDoc.setRawCommentText(this.getJavaDocCommentsBeforeNode(t));
         this.processModifiers(t, currentEnumConstantDoc);
         String typeName = this.getTypeNodeAsText(t.childOfType(12), currentClassDoc.getTypeDescription());
         currentEnumConstantDoc.setType(new SimpleGroovyType(typeName));
         currentClassDoc.addEnumConstant(currentEnumConstantDoc);
      } else if (visit == 4) {
         this.insideEnum = false;
      }

   }

   public void visitVariableDef(GroovySourceAST t, int visit) {
      if (visit == 1 && !this.insideAnonymousInnerClass() && this.isFieldDefinition()) {
         SimpleGroovyClassDoc currentClassDoc = this.getCurrentClassDoc();
         if (currentClassDoc != null) {
            String fieldName = this.getIdentFor(t);
            this.currentFieldDoc = new SimpleGroovyFieldDoc(fieldName, currentClassDoc);
            this.currentFieldDoc.setRawCommentText(this.getJavaDocCommentsBeforeNode(t));
            boolean isProp = this.processModifiers(t, this.currentFieldDoc);
            this.currentFieldDoc.setType(new SimpleGroovyType(this.getTypeOrDefault(t)));
            this.processAnnotations(t, this.currentFieldDoc);
            if (isProp) {
               currentClassDoc.addProperty(this.currentFieldDoc);
            } else {
               currentClassDoc.add((GroovyFieldDoc)this.currentFieldDoc);
            }
         }
      }

   }

   public void visitAssign(GroovySourceAST t, int visit) {
      this.gobbleComments(t, visit);
   }

   public void visitMethodCall(GroovySourceAST t, int visit) {
      this.gobbleComments(t, visit);
   }

   private void gobbleComments(GroovySourceAST t, int visit) {
      if (visit == 1) {
         SimpleGroovyClassDoc currentClassDoc = this.getCurrentClassDoc();
         if ((currentClassDoc == null || currentClassDoc.isScript()) && (t.getLine() > this.lastLineCol.getLine() || t.getLine() == this.lastLineCol.getLine() && t.getColumn() > this.lastLineCol.getColumn())) {
            this.getJavaDocCommentsBeforeNode(t);
            this.lastLineCol = new LineColumn(t.getLine(), t.getColumn());
         }
      }

   }

   private void postProcessClassDocs() {
      Iterator i$ = this.classDocs.values().iterator();

      while(i$.hasNext()) {
         GroovyClassDoc groovyClassDoc = (GroovyClassDoc)i$.next();
         SimpleGroovyClassDoc classDoc = (SimpleGroovyClassDoc)groovyClassDoc;
         if (classDoc.isClass()) {
            GroovyConstructorDoc[] constructors = classDoc.constructors();
            if (constructors != null && constructors.length == 0) {
               GroovyConstructorDoc constructorDoc = new SimpleGroovyConstructorDoc(classDoc.name(), classDoc);
               classDoc.add((GroovyConstructorDoc)constructorDoc);
            }
         }
      }

   }

   private boolean isNested() {
      return this.getCurrentClassDoc() != null;
   }

   private boolean isTopLevelConstruct(GroovySourceAST node) {
      if (node == null) {
         return false;
      } else {
         int type = node.getType();
         return type == 13 || type == 14 || type == 63 || type == 60;
      }
   }

   private void adjustForAutomaticEnumMethods(SimpleGroovyClassDoc currentClassDoc) {
      SimpleGroovyMethodDoc valueOf = new SimpleGroovyMethodDoc("valueOf", currentClassDoc);
      valueOf.setRawCommentText("Returns the enum constant of this type with the specified name.");
      SimpleGroovyParameter parameter = new SimpleGroovyParameter("name");
      parameter.setTypeName("String");
      valueOf.add(parameter);
      valueOf.setReturnType(new SimpleGroovyType(currentClassDoc.name()));
      currentClassDoc.add((GroovyMethodDoc)valueOf);
      SimpleGroovyMethodDoc values = new SimpleGroovyMethodDoc("values", currentClassDoc);
      values.setRawCommentText("Returns an array containing the constants of this enum type, in the order they are declared.");
      values.setReturnType(new SimpleGroovyType(currentClassDoc.name() + "[]"));
      currentClassDoc.add((GroovyMethodDoc)values);
   }

   private String extractImportPath(GroovySourceAST t) {
      GroovySourceAST child = t.childOfType(87);
      if (child == null) {
         child = t.childOfType(84);
      }

      return this.recurseDownImportBranch(child);
   }

   private String recurseDownImportBranch(GroovySourceAST t) {
      if (t != null) {
         if (t.getType() == 87) {
            GroovySourceAST firstChild = (GroovySourceAST)t.getFirstChild();
            GroovySourceAST secondChild = (GroovySourceAST)firstChild.getNextSibling();
            return this.recurseDownImportBranch(firstChild) + "/" + this.recurseDownImportBranch(secondChild);
         }

         if (t.getType() == 84) {
            return t.getText();
         }

         if (t.getType() == 109) {
            return t.getText();
         }
      }

      return "";
   }

   private void addAnnotationRef(SimpleGroovyProgramElementDoc node, GroovySourceAST t) {
      GroovySourceAST classNode = t.childOfType(84);
      if (classNode != null) {
         node.addAnnotationRef(new SimpleGroovyAnnotationRef(this.extractName(classNode), this.getChildTextFromSource(t).trim()));
      }

   }

   private void addAnnotationRef(SimpleGroovyParameter node, GroovySourceAST t) {
      GroovySourceAST classNode = t.childOfType(84);
      if (classNode != null) {
         node.addAnnotationRef(new SimpleGroovyAnnotationRef(this.extractName(classNode), this.getChildTextFromSource(t).trim()));
      }

   }

   private void addAnnotationRefs(SimpleGroovyProgramElementDoc node, List<GroovySourceAST> nodes) {
      Iterator i$ = nodes.iterator();

      while(i$.hasNext()) {
         GroovySourceAST t = (GroovySourceAST)i$.next();
         this.addAnnotationRef(node, t);
      }

   }

   private void processAnnotations(GroovySourceAST t, SimpleGroovyProgramElementDoc node) {
      GroovySourceAST modifiers = t.childOfType(5);
      if (modifiers != null) {
         this.addAnnotationRefs(node, modifiers.childrenOfType(65));
      }

   }

   private String getDefaultValue(GroovySourceAST t) {
      GroovySourceAST child = (GroovySourceAST)t.getFirstChild();
      if (t.getNumberOfChildren() != 4) {
         return null;
      } else {
         for(int i = 1; i < t.getNumberOfChildren(); ++i) {
            child = (GroovySourceAST)child.getNextSibling();
         }

         GroovySourceAST nodeToProcess = child;
         if (child.getNumberOfChildren() > 0) {
            nodeToProcess = (GroovySourceAST)child.getFirstChild();
         }

         return this.getChildTextFromSource(nodeToProcess, ";");
      }
   }

   private String getChildTextFromSource(GroovySourceAST child) {
      return this.sourceBuffer.getSnippet(new LineColumn(child.getLine(), child.getColumn()), new LineColumn(child.getLineLast(), child.getColumnLast()));
   }

   private String getChildTextFromSource(GroovySourceAST child, String tokens) {
      String text = this.sourceBuffer.getSnippet(new LineColumn(child.getLine(), child.getColumn()), new LineColumn(child.getLine() + 1, 0));
      StringTokenizer st = new StringTokenizer(text, tokens);
      return st.nextToken();
   }

   private boolean isFieldDefinition() {
      GroovySourceAST parentNode = this.getParentNode();
      return parentNode != null && parentNode.getType() == 6;
   }

   private boolean insideAnonymousInnerClass() {
      GroovySourceAST grandParentNode = this.getGrandParentNode();
      return grandParentNode != null && grandParentNode.getType() == 154;
   }

   private boolean processModifiers(GroovySourceAST t, SimpleGroovyAbstractableElementDoc memberOrClass) {
      GroovySourceAST modifiers = t.childOfType(5);
      boolean hasNonPublicVisibility = false;
      boolean hasPublicVisibility = false;
      if (modifiers != null) {
         for(AST currentModifier = modifiers.getFirstChild(); currentModifier != null; currentModifier = currentModifier.getNextSibling()) {
            int type = currentModifier.getType();
            switch(type) {
            case 37:
               memberOrClass.setFinal(true);
               break;
            case 38:
               memberOrClass.setAbstract(true);
               break;
            case 80:
               memberOrClass.setStatic(true);
               break;
            case 111:
               memberOrClass.setPrivate(true);
               hasNonPublicVisibility = true;
               break;
            case 112:
               memberOrClass.setPublic(true);
               hasPublicVisibility = true;
               break;
            case 113:
               memberOrClass.setProtected(true);
               hasNonPublicVisibility = true;
            }
         }

         if (!hasNonPublicVisibility && this.isGroovy && !(memberOrClass instanceof GroovyFieldDoc)) {
            memberOrClass.setPublic(true);
         } else if (!hasNonPublicVisibility && !hasPublicVisibility && !this.isGroovy) {
            if (this.insideInterface(memberOrClass)) {
               memberOrClass.setPublic(true);
            } else {
               memberOrClass.setPackagePrivate(true);
            }
         }

         if (memberOrClass instanceof GroovyFieldDoc && !hasNonPublicVisibility && !hasPublicVisibility && this.isGroovy) {
            return true;
         }
      } else if (this.isGroovy && !(memberOrClass instanceof GroovyFieldDoc)) {
         memberOrClass.setPublic(true);
      } else if (!this.isGroovy) {
         if (this.insideInterface(memberOrClass)) {
            memberOrClass.setPublic(true);
         } else {
            memberOrClass.setPackagePrivate(true);
         }
      }

      return memberOrClass instanceof GroovyFieldDoc && this.isGroovy && !hasNonPublicVisibility & !hasPublicVisibility;
   }

   private boolean insideInterface(SimpleGroovyAbstractableElementDoc memberOrClass) {
      SimpleGroovyClassDoc current = this.getCurrentClassDoc();
      return current != null && current != memberOrClass ? current.isInterface() : false;
   }

   private String getJavaDocCommentsBeforeNode(GroovySourceAST t) {
      String result = "";
      LineColumn thisLineCol = new LineColumn(t.getLine(), t.getColumn());
      String text = this.sourceBuffer.getSnippet(this.lastLineCol, thisLineCol);
      if (text != null) {
         Matcher m = PREV_JAVADOC_COMMENT_PATTERN.matcher(text);
         if (m.find()) {
            result = m.group(1);
         }
      }

      if (this.isMajorType(t)) {
         this.lastLineCol = thisLineCol;
      }

      return result;
   }

   private boolean isMajorType(GroovySourceAST t) {
      if (t == null) {
         return false;
      } else {
         int tt = t.getType();
         return tt == 13 || tt == 14 || tt == 8 || tt == 63 || tt == 60 || tt == 9 || tt == 67 || tt == 61 || tt == 45;
      }
   }

   private String getText(GroovySourceAST node) {
      String returnValue = null;
      if (node != null) {
         returnValue = node.getText();
      }

      return returnValue;
   }

   private String extractName(GroovySourceAST typeNode) {
      String typeName = this.buildName(typeNode);
      if (typeName.indexOf("/") == -1) {
         String slashName = "/" + typeName;
         Iterator i$ = this.importedClassesAndPackages.iterator();

         while(i$.hasNext()) {
            String name = (String)i$.next();
            if (name.endsWith(slashName)) {
               typeName = name;
            }
         }
      }

      return typeName;
   }

   private String buildName(GroovySourceAST t) {
      if (t != null) {
         if (t.getType() == 87) {
            GroovySourceAST firstChild = (GroovySourceAST)t.getFirstChild();
            GroovySourceAST secondChild = (GroovySourceAST)firstChild.getNextSibling();
            return this.buildName(firstChild) + "/" + this.buildName(secondChild);
         }

         if (t.getType() == 84) {
            return t.getText();
         }
      }

      return "";
   }

   private String getTypeOrDefault(GroovySourceAST t) {
      GroovySourceAST typeNode = t.childOfType(12);
      return this.getTypeNodeAsText(typeNode, "def");
   }

   private String getTypeNodeAsText(GroovySourceAST typeNode, String defaultText) {
      return typeNode != null && typeNode.getType() == 12 && typeNode.getNumberOfChildren() > 0 ? this.getAsText(typeNode, defaultText) : defaultText;
   }

   private String getAsText(GroovySourceAST typeNode, String defaultText) {
      String returnValue = defaultText;
      GroovySourceAST child = (GroovySourceAST)typeNode.getFirstChild();
      switch(child.getType()) {
      case 16:
         String componentType = this.getAsText(child, defaultText);
         if (!componentType.equals("def")) {
            returnValue = componentType + "[]";
         }
         break;
      case 84:
         returnValue = child.getText();
         break;
      case 100:
         returnValue = "void";
         break;
      case 101:
         returnValue = "boolean";
         break;
      case 102:
         returnValue = "byte";
         break;
      case 103:
         returnValue = "char";
         break;
      case 104:
         returnValue = "short";
         break;
      case 105:
         returnValue = "int";
         break;
      case 106:
         returnValue = "float";
         break;
      case 107:
         returnValue = "long";
         break;
      case 108:
         returnValue = "double";
      }

      return returnValue;
   }

   private void addParametersTo(GroovySourceAST t, SimpleGroovyExecutableMemberDoc executableMemberDoc) {
      GroovySourceAST parametersNode = t.childOfType(19);
      if (parametersNode != null && parametersNode.getNumberOfChildren() > 0) {
         for(GroovySourceAST currentNode = (GroovySourceAST)parametersNode.getFirstChild(); currentNode != null; currentNode = (GroovySourceAST)currentNode.getNextSibling()) {
            String parameterTypeName = this.getTypeOrDefault(currentNode);
            String parameterName = this.getText(currentNode.childOfType(84));
            SimpleGroovyParameter parameter = new SimpleGroovyParameter(parameterName);
            parameter.setTypeName(parameterTypeName);
            GroovySourceAST modifiers = currentNode.childOfType(5);
            if (modifiers != null) {
               List<GroovySourceAST> annotations = modifiers.childrenOfType(65);
               Iterator i$ = annotations.iterator();

               while(i$.hasNext()) {
                  GroovySourceAST a = (GroovySourceAST)i$.next();
                  this.addAnnotationRef(parameter, a);
               }
            }

            executableMemberDoc.add(parameter);
            if (currentNode.getNumberOfChildren() == 4) {
               this.handleDefaultValue(currentNode, parameter);
            }
         }
      }

   }

   private void handleDefaultValue(GroovySourceAST currentNode, SimpleGroovyParameter parameter) {
      GroovySourceAST paramPart = (GroovySourceAST)currentNode.getFirstChild();

      for(int i = 1; i < currentNode.getNumberOfChildren(); ++i) {
         paramPart = (GroovySourceAST)paramPart.getNextSibling();
      }

      GroovySourceAST nodeToProcess = paramPart;
      if (paramPart.getNumberOfChildren() > 0) {
         nodeToProcess = (GroovySourceAST)paramPart.getFirstChild();
      }

      parameter.setDefaultValue(this.getChildTextFromSource(nodeToProcess, ",)"));
   }

   public void push(GroovySourceAST t) {
      this.stack.push(t);
   }

   public GroovySourceAST pop() {
      return !this.stack.empty() ? (GroovySourceAST)this.stack.pop() : null;
   }

   private GroovySourceAST getParentNode() {
      GroovySourceAST parentNode = null;
      GroovySourceAST currentNode = (GroovySourceAST)this.stack.pop();
      if (!this.stack.empty()) {
         parentNode = (GroovySourceAST)this.stack.peek();
      }

      this.stack.push(currentNode);
      return parentNode;
   }

   private GroovySourceAST getGrandParentNode() {
      GroovySourceAST grandParentNode = null;
      GroovySourceAST currentNode = (GroovySourceAST)this.stack.pop();
      if (!this.stack.empty()) {
         GroovySourceAST parentNode = (GroovySourceAST)this.stack.pop();
         if (!this.stack.empty()) {
            grandParentNode = (GroovySourceAST)this.stack.peek();
         }

         this.stack.push(parentNode);
      }

      this.stack.push(currentNode);
      return grandParentNode;
   }

   private SimpleGroovyClassDoc getCurrentOrTopLevelClassDoc(GroovySourceAST node) {
      SimpleGroovyClassDoc current = this.getCurrentClassDoc();
      return current != null ? current : (SimpleGroovyClassDoc)this.foundClasses.get(this.getIdentFor(node));
   }

   private SimpleGroovyClassDoc getCurrentClassDoc() {
      if (this.stack.isEmpty()) {
         return null;
      } else {
         GroovySourceAST node = this.getParentNode();
         if (this.isTopLevelConstruct(node)) {
            return (SimpleGroovyClassDoc)this.foundClasses.get(this.getIdentFor(node));
         } else {
            GroovySourceAST saved = (GroovySourceAST)this.stack.pop();
            SimpleGroovyClassDoc result = this.getCurrentClassDoc();
            this.stack.push(saved);
            return result;
         }
      }
   }

   private String getIdentFor(GroovySourceAST gpn) {
      return gpn.childOfType(84).getText();
   }
}
