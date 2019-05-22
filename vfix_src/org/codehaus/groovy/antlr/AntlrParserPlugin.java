package org.codehaus.groovy.antlr;

import com.thoughtworks.xstream.XStream;
import groovyjarjarantlr.RecognitionException;
import groovyjarjarantlr.TokenStreamException;
import groovyjarjarantlr.TokenStreamRecognitionException;
import groovyjarjarantlr.collections.AST;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;
import org.codehaus.groovy.antlr.treewalker.CompositeVisitor;
import org.codehaus.groovy.antlr.treewalker.MindMapPrinter;
import org.codehaus.groovy.antlr.treewalker.NodeAsHTMLPrinter;
import org.codehaus.groovy.antlr.treewalker.PreOrderTraversal;
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal;
import org.codehaus.groovy.antlr.treewalker.SourcePrinter;
import org.codehaus.groovy.antlr.treewalker.Visitor;
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.EnumConstantClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.MixinNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.PackageNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.expr.AnnotationConstantExpression;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.AttributeExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BitwiseNegationExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ClosureListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.ElvisOperatorExpression;
import org.codehaus.groovy.ast.expr.EmptyExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ExpressionTransformer;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.RangeExpression;
import org.codehaus.groovy.ast.expr.SpreadExpression;
import org.codehaus.groovy.ast.expr.SpreadMapExpression;
import org.codehaus.groovy.ast.expr.TernaryExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.UnaryMinusExpression;
import org.codehaus.groovy.ast.expr.UnaryPlusExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ContinueStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.ParserPlugin;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.ASTHelper;
import org.codehaus.groovy.syntax.Numbers;
import org.codehaus.groovy.syntax.ParserException;
import org.codehaus.groovy.syntax.Reduction;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;

public class AntlrParserPlugin extends ASTHelper implements ParserPlugin, GroovyTokenTypes {
   protected AST ast;
   private ClassNode classNode;
   private String[] tokenNames;
   private int innerClassCounter = 1;
   private boolean enumConstantBeingDef = false;
   private boolean forStatementBeingDef = false;
   private boolean firstParamIsVarArg = false;
   private boolean firstParam = false;

   public Reduction parseCST(SourceUnit sourceUnit, Reader reader) throws CompilationFailedException {
      SourceBuffer sourceBuffer = new SourceBuffer();
      this.transformCSTIntoAST(sourceUnit, reader, sourceBuffer);
      this.processAST();
      return this.outputAST(sourceUnit, sourceBuffer);
   }

   protected void transformCSTIntoAST(SourceUnit sourceUnit, Reader reader, SourceBuffer sourceBuffer) throws CompilationFailedException {
      this.ast = null;
      this.setController(sourceUnit);
      UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(reader, sourceBuffer);
      GroovyLexer lexer = new GroovyLexer(unicodeReader);
      unicodeReader.setLexer(lexer);
      GroovyRecognizer parser = GroovyRecognizer.make(lexer);
      parser.setSourceBuffer(sourceBuffer);
      this.tokenNames = parser.getTokenNames();
      parser.setFilename(sourceUnit.getName());

      try {
         parser.compilationUnit();
      } catch (TokenStreamRecognitionException var10) {
         RecognitionException e = var10.recog;
         SyntaxException se = new SyntaxException(e.getMessage(), e, e.getLine(), e.getColumn());
         se.setFatal(true);
         sourceUnit.addError(se);
      } catch (RecognitionException var11) {
         SyntaxException se = new SyntaxException(var11.getMessage(), var11, var11.getLine(), var11.getColumn());
         se.setFatal(true);
         sourceUnit.addError(se);
      } catch (TokenStreamException var12) {
         sourceUnit.addException(var12);
      }

      this.ast = parser.getAST();
   }

   protected void processAST() {
      AntlrASTProcessor snippets = new AntlrASTProcessSnippets();
      this.ast = snippets.process(this.ast);
   }

   public Reduction outputAST(final SourceUnit sourceUnit, final SourceBuffer sourceBuffer) {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            AntlrParserPlugin.this.outputASTInVariousFormsIfNeeded(sourceUnit, sourceBuffer);
            return null;
         }
      });
      return null;
   }

   private void outputASTInVariousFormsIfNeeded(SourceUnit sourceUnit, SourceBuffer sourceBuffer) {
      if ("xml".equals(System.getProperty("groovyjarjarantlr.ast"))) {
         this.saveAsXML(sourceUnit.getName(), this.ast);
      }

      PrintStream out;
      if ("groovy".equals(System.getProperty("groovyjarjarantlr.ast"))) {
         try {
            out = new PrintStream(new FileOutputStream(sourceUnit.getName() + ".pretty.groovy"));
            Visitor visitor = new SourcePrinter(out, this.tokenNames);
            AntlrASTProcessor treewalker = new SourceCodeTraversal(visitor);
            treewalker.process(this.ast);
         } catch (FileNotFoundException var10) {
            System.out.println("Cannot create " + sourceUnit.getName() + ".pretty.groovy");
         }
      }

      MindMapPrinter visitor;
      PreOrderTraversal treewalker;
      if ("mindmap".equals(System.getProperty("groovyjarjarantlr.ast"))) {
         try {
            out = new PrintStream(new FileOutputStream(sourceUnit.getName() + ".mm"));
            visitor = new MindMapPrinter(out, this.tokenNames);
            treewalker = new PreOrderTraversal(visitor);
            treewalker.process(this.ast);
         } catch (FileNotFoundException var9) {
            System.out.println("Cannot create " + sourceUnit.getName() + ".mm");
         }
      }

      if ("extendedMindmap".equals(System.getProperty("groovyjarjarantlr.ast"))) {
         try {
            out = new PrintStream(new FileOutputStream(sourceUnit.getName() + ".mm"));
            visitor = new MindMapPrinter(out, this.tokenNames, sourceBuffer);
            treewalker = new PreOrderTraversal(visitor);
            treewalker.process(this.ast);
         } catch (FileNotFoundException var8) {
            System.out.println("Cannot create " + sourceUnit.getName() + ".mm");
         }
      }

      if ("html".equals(System.getProperty("groovyjarjarantlr.ast"))) {
         try {
            out = new PrintStream(new FileOutputStream(sourceUnit.getName() + ".html"));
            List<VisitorAdapter> v = new ArrayList();
            v.add(new NodeAsHTMLPrinter(out, this.tokenNames));
            v.add(new SourcePrinter(out, this.tokenNames));
            Visitor visitors = new CompositeVisitor(v);
            AntlrASTProcessor treewalker = new SourceCodeTraversal(visitors);
            treewalker.process(this.ast);
         } catch (FileNotFoundException var7) {
            System.out.println("Cannot create " + sourceUnit.getName() + ".html");
         }
      }

   }

   private void saveAsXML(String name, AST ast) {
      XStream xstream = new XStream();

      try {
         xstream.toXML(ast, new FileWriter(name + ".antlr.xml"));
         System.out.println("Written AST to " + name + ".antlr.xml");
      } catch (Exception var5) {
         System.out.println("Couldn't write to " + name + ".antlr.xml");
         var5.printStackTrace();
      }

   }

   public ModuleNode buildAST(SourceUnit sourceUnit, ClassLoader classLoader, Reduction cst) throws ParserException {
      this.setClassLoader(classLoader);
      this.makeModule();

      try {
         this.convertGroovy(this.ast);
         if (this.output.getStatementBlock().isEmpty() && this.output.getMethods().isEmpty() && this.output.getClasses().isEmpty()) {
            this.output.addStatement(ReturnStatement.RETURN_NULL_OR_VOID);
         }
      } catch (ASTRuntimeException var5) {
         throw new ASTParserException(var5.getMessage() + ". File: " + sourceUnit.getName(), var5);
      }

      return this.output;
   }

   protected void convertGroovy(AST node) {
      for(; node != null; node = node.getNextSibling()) {
         int type = node.getType();
         switch(type) {
         case 8:
            this.methodDef(node);
            break;
         case 13:
            this.classDef(node);
            break;
         case 14:
            this.interfaceDef(node);
            break;
         case 15:
            this.packageDef(node);
            break;
         case 28:
         case 59:
            this.importDef(node);
            break;
         case 60:
            this.enumDef(node);
            break;
         case 63:
            this.annotationDef(node);
            break;
         default:
            Statement statement = this.statement(node);
            this.output.addStatement(statement);
         }
      }

   }

   protected void packageDef(AST packageDef) {
      List<AnnotationNode> annotations = new ArrayList();
      AST node = packageDef.getFirstChild();
      if (isType(64, node)) {
         this.processAnnotations(annotations, node);
         node = node.getNextSibling();
      }

      String name = qualifiedName(node);
      PackageNode packageNode = this.setPackage(name, annotations);
      this.configureAST(packageNode, packageDef);
   }

   protected void importDef(AST importNode) {
      boolean isStatic = importNode.getType() == 59;
      List<AnnotationNode> annotations = new ArrayList();
      AST node = importNode.getFirstChild();
      if (isType(64, node)) {
         this.processAnnotations(annotations, node);
         node = node.getNextSibling();
      }

      String alias = null;
      AST packageNode;
      if (isType(110, node)) {
         node = node.getFirstChild();
         packageNode = node.getNextSibling();
         alias = this.identifier(packageNode);
      }

      if (node.getNumberOfChildren() == 0) {
         String name = this.identifier(node);
         ClassNode type = ClassHelper.make(name);
         this.configureAST(type, importNode);
         this.addImport(type, name, alias, annotations);
      } else {
         packageNode = node.getFirstChild();
         String packageName = qualifiedName(packageNode);
         AST nameNode = packageNode.getNextSibling();
         if (isType(109, nameNode)) {
            if (isStatic) {
               ClassNode type = ClassHelper.make(packageName);
               this.configureAST(type, importNode);
               this.addStaticStarImport(type, packageName, annotations);
            } else {
               this.addStarImport(packageName, annotations);
            }

            if (alias != null) {
               throw new GroovyBugError("imports like 'import foo.* as Bar' are not supported and should be caught by the grammar");
            }
         } else {
            String name = this.identifier(nameNode);
            ClassNode type;
            if (isStatic) {
               type = ClassHelper.make(packageName);
               this.configureAST(type, importNode);
               this.addStaticImport(type, name, alias, annotations);
            } else {
               type = ClassHelper.make(packageName + "." + name);
               this.configureAST(type, importNode);
               this.addImport(type, name, alias, annotations);
            }
         }

      }
   }

   private void processAnnotations(List<AnnotationNode> annotations, AST node) {
      for(AST child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (isType(65, child)) {
            annotations.add(this.annotation(child));
         }
      }

   }

   protected void annotationDef(AST classDef) {
      List<AnnotationNode> annotations = new ArrayList();
      AST node = classDef.getFirstChild();
      int modifiers = 1;
      if (isType(5, node)) {
         modifiers = this.modifiers(node, annotations, modifiers);
         this.checkNoInvalidModifier(classDef, "Annotation Definition", modifiers, 32, "synchronized");
         node = node.getNextSibling();
      }

      modifiers |= 9728;
      String name = this.identifier(node);
      node = node.getNextSibling();
      ClassNode superClass = ClassHelper.OBJECT_TYPE;
      GenericsType[] genericsType = null;
      if (isType(71, node)) {
         genericsType = this.makeGenericsType(node);
         node = node.getNextSibling();
      }

      ClassNode[] interfaces = ClassNode.EMPTY_ARRAY;
      if (isType(17, node)) {
         interfaces = this.interfaces(node);
         node = node.getNextSibling();
      }

      this.classNode = new ClassNode(dot(this.getPackageName(), name), modifiers, superClass, interfaces, (MixinNode[])null);
      this.classNode.addAnnotations(annotations);
      this.classNode.setGenericsTypes(genericsType);
      this.classNode.addInterface(ClassHelper.Annotation_TYPE);
      this.configureAST(this.classNode, classDef);
      this.assertNodeType(6, node);
      this.objectBlock(node);
      this.output.addClass(this.classNode);
      this.classNode = null;
   }

   protected void interfaceDef(AST classDef) {
      int oldInnerClassCounter = this.innerClassCounter;
      this.innerInterfaceDef(classDef);
      this.classNode = null;
      this.innerClassCounter = oldInnerClassCounter;
   }

   protected void innerInterfaceDef(AST classDef) {
      List<AnnotationNode> annotations = new ArrayList();
      AST node = classDef.getFirstChild();
      int modifiers = 1;
      if (isType(5, node)) {
         modifiers = this.modifiers(node, annotations, modifiers);
         this.checkNoInvalidModifier(classDef, "Interface", modifiers, 32, "synchronized");
         node = node.getNextSibling();
      }

      modifiers |= 1536;
      String name = this.identifier(node);
      node = node.getNextSibling();
      ClassNode superClass = ClassHelper.OBJECT_TYPE;
      GenericsType[] genericsType = null;
      if (isType(71, node)) {
         genericsType = this.makeGenericsType(node);
         node = node.getNextSibling();
      }

      ClassNode[] interfaces = ClassNode.EMPTY_ARRAY;
      if (isType(17, node)) {
         interfaces = this.interfaces(node);
         node = node.getNextSibling();
      }

      ClassNode outerClass = this.classNode;
      if (this.classNode != null) {
         name = this.classNode.getNameWithoutPackage() + "$" + name;
         String fullName = dot(this.classNode.getPackageName(), name);
         this.classNode = new InnerClassNode(this.classNode, fullName, modifiers, superClass, interfaces, (MixinNode[])null);
      } else {
         this.classNode = new ClassNode(dot(this.getPackageName(), name), modifiers, superClass, interfaces, (MixinNode[])null);
      }

      this.classNode.addAnnotations(annotations);
      this.classNode.setGenericsTypes(genericsType);
      this.configureAST(this.classNode, classDef);
      int oldClassCount = this.innerClassCounter;
      this.assertNodeType(6, node);
      this.objectBlock(node);
      this.output.addClass(this.classNode);
      this.classNode = outerClass;
      this.innerClassCounter = oldClassCount;
   }

   protected void classDef(AST classDef) {
      int oldInnerClassCounter = this.innerClassCounter;
      this.innerClassDef(classDef);
      this.classNode = null;
      this.innerClassCounter = oldInnerClassCounter;
   }

   private ClassNode getClassOrScript(ClassNode node) {
      return node != null ? node : this.output.getScriptClassDummy();
   }

   protected Expression anonymousInnerClassDef(AST node) {
      ClassNode oldNode = this.classNode;
      ClassNode outerClass = this.getClassOrScript(oldNode);
      String fullName = outerClass.getName() + '$' + this.innerClassCounter;
      ++this.innerClassCounter;
      if (this.enumConstantBeingDef) {
         this.classNode = new EnumConstantClassNode(outerClass, fullName, 1, ClassHelper.OBJECT_TYPE);
      } else {
         this.classNode = new InnerClassNode(outerClass, fullName, 1, ClassHelper.OBJECT_TYPE);
      }

      ((InnerClassNode)this.classNode).setAnonymous(true);
      this.assertNodeType(6, node);
      this.objectBlock(node);
      this.output.addClass(this.classNode);
      AntlrParserPlugin.AnonymousInnerClassCarrier ret = new AntlrParserPlugin.AnonymousInnerClassCarrier();
      ret.innerClass = this.classNode;
      this.classNode = oldNode;
      return ret;
   }

   protected void innerClassDef(AST classDef) {
      List<AnnotationNode> annotations = new ArrayList();
      AST node = classDef.getFirstChild();
      int modifiers = 1;
      if (isType(5, node)) {
         modifiers = this.modifiers(node, annotations, modifiers);
         this.checkNoInvalidModifier(classDef, "Class", modifiers, 32, "synchronized");
         node = node.getNextSibling();
      }

      String name = this.identifier(node);
      node = node.getNextSibling();
      GenericsType[] genericsType = null;
      if (isType(71, node)) {
         genericsType = this.makeGenericsType(node);
         node = node.getNextSibling();
      }

      ClassNode superClass = null;
      if (isType(17, node)) {
         superClass = this.makeTypeWithArguments(node);
         node = node.getNextSibling();
      }

      ClassNode[] interfaces = ClassNode.EMPTY_ARRAY;
      if (isType(18, node)) {
         interfaces = this.interfaces(node);
         node = node.getNextSibling();
      }

      MixinNode[] mixins = new MixinNode[0];
      ClassNode outerClass = this.classNode;
      if (this.classNode != null) {
         name = this.classNode.getNameWithoutPackage() + "$" + name;
         String fullName = dot(this.classNode.getPackageName(), name);
         this.classNode = new InnerClassNode(this.classNode, fullName, modifiers, superClass, interfaces, mixins);
      } else {
         this.classNode = new ClassNode(dot(this.getPackageName(), name), modifiers, superClass, interfaces, mixins);
      }

      this.classNode.addAnnotations(annotations);
      this.classNode.setGenericsTypes(genericsType);
      this.configureAST(this.classNode, classDef);
      this.output.addClass(this.classNode);
      int oldClassCount = this.innerClassCounter;
      this.assertNodeType(6, node);
      this.objectBlock(node);
      this.classNode = outerClass;
      this.innerClassCounter = oldClassCount;
   }

   protected void objectBlock(AST objectBlock) {
      for(AST node = objectBlock.getFirstChild(); node != null; node = node.getNextSibling()) {
         int type = node.getType();
         switch(type) {
         case 6:
            this.objectBlock(node);
            break;
         case 8:
         case 67:
            this.methodDef(node);
            break;
         case 9:
            this.fieldDef(node);
            break;
         case 10:
            this.objectInit(node);
            break;
         case 11:
            this.staticInit(node);
            break;
         case 13:
            this.innerClassDef(node);
            break;
         case 14:
            this.innerInterfaceDef(node);
            break;
         case 45:
            this.constructorDef(node);
            break;
         case 60:
            this.enumDef(node);
            break;
         case 61:
            this.enumConstantDef(node);
            break;
         default:
            this.unknownAST(node);
         }
      }

   }

   protected void enumDef(AST enumNode) {
      this.assertNodeType(60, enumNode);
      List<AnnotationNode> annotations = new ArrayList();
      AST node = enumNode.getFirstChild();
      int modifiers = 1;
      if (isType(5, node)) {
         modifiers = this.modifiers(node, annotations, modifiers);
         node = node.getNextSibling();
      }

      String name = this.identifier(node);
      node = node.getNextSibling();
      ClassNode[] interfaces = this.interfaces(node);
      node = node.getNextSibling();
      String enumName = this.classNode != null ? name : dot(this.getPackageName(), name);
      ClassNode enumClass = EnumHelper.makeEnumNode(enumName, modifiers, interfaces, this.classNode);
      ClassNode oldNode = this.classNode;
      this.classNode = enumClass;
      this.assertNodeType(6, node);
      this.objectBlock(node);
      this.classNode = oldNode;
      this.output.addClass(enumClass);
   }

   protected void enumConstantDef(AST node) {
      this.enumConstantBeingDef = true;
      this.assertNodeType(61, node);
      AST element = node.getFirstChild();
      if (isType(64, element)) {
         element = element.getNextSibling();
      }

      String identifier = this.identifier(element);
      Expression init = null;
      element = element.getNextSibling();
      if (element != null) {
         init = this.expression(element);
         ClassNode innerClass = this.getAnonymousInnerClassNode((Expression)init);
         if (innerClass != null) {
            innerClass.setSuperClass(this.classNode);
            innerClass.setModifiers(this.classNode.getModifiers() | 16);
            init = new ClassExpression(innerClass);
            this.classNode.setModifiers(this.classNode.getModifiers() & -17);
         } else if (isType(32, element) && init instanceof ListExpression && !((ListExpression)init).isWrapped()) {
            ListExpression le = new ListExpression();
            le.addExpression((Expression)init);
            init = le;
         }
      }

      EnumHelper.addEnumConstant(this.classNode, identifier, (Expression)init);
      this.enumConstantBeingDef = false;
   }

   protected void throwsList(AST node, List list) {
      String name;
      if (isType(87, node)) {
         name = qualifiedName(node);
      } else {
         name = this.identifier(node);
      }

      ClassNode exception = ClassHelper.make(name);
      this.configureAST(exception, node);
      list.add(exception);
      AST next = node.getNextSibling();
      if (next != null) {
         this.throwsList(next, list);
      }

   }

   protected void methodDef(AST methodDef) {
      List<AnnotationNode> annotations = new ArrayList();
      AST node = methodDef.getFirstChild();
      GenericsType[] generics = null;
      if (isType(71, node)) {
         generics = this.makeGenericsType(node);
         node = node.getNextSibling();
      }

      int modifiers = 1;
      if (isType(5, node)) {
         modifiers = this.modifiers(node, annotations, modifiers);
         this.checkNoInvalidModifier(methodDef, "Method", modifiers, 64, "volatile");
         node = node.getNextSibling();
      }

      if (this.isAnInterface()) {
         modifiers |= 1024;
      }

      ClassNode returnType = null;
      if (isType(12, node)) {
         returnType = this.makeTypeWithArguments(node);
         node = node.getNextSibling();
      }

      String name = this.identifier(node);
      if (this.classNode != null && !this.classNode.isAnnotationDefinition() && this.classNode.getNameWithoutPackage().equals(name)) {
         if (this.isAnInterface()) {
            throw new ASTRuntimeException(methodDef, "Constructor not permitted within an interface.");
         } else {
            throw new ASTRuntimeException(methodDef, "Invalid constructor format. Remove '" + returnType.getName() + "' as the return type if you want a constructor, or use a different name if you want a method.");
         }
      } else {
         node = node.getNextSibling();
         Parameter[] parameters = Parameter.EMPTY_ARRAY;
         ClassNode[] exceptions = ClassNode.EMPTY_ARRAY;
         if (this.classNode == null || !this.classNode.isAnnotationDefinition()) {
            this.assertNodeType(19, node);
            parameters = this.parameters(node);
            if (parameters == null) {
               parameters = Parameter.EMPTY_ARRAY;
            }

            node = node.getNextSibling();
            if (isType(126, node)) {
               AST throwsNode = node.getFirstChild();
               List exceptionList = new ArrayList();
               this.throwsList(throwsNode, exceptionList);
               exceptions = (ClassNode[])((ClassNode[])exceptionList.toArray(exceptions));
               node = node.getNextSibling();
            }
         }

         boolean hasAnnotationDefault = false;
         Statement code = null;
         if ((modifiers & 1024) == 0) {
            if (node == null) {
               throw new ASTRuntimeException(methodDef, "You defined a method without body. Try adding a body, or declare it abstract.");
            }

            this.assertNodeType(7, node);
            code = this.statementList(node);
         } else if (node != null && this.classNode.isAnnotationDefinition()) {
            code = this.statement(node);
            hasAnnotationDefault = true;
         } else if ((modifiers & 1024) > 0 && node != null) {
            throw new ASTRuntimeException(methodDef, "Abstract methods do not define a body.");
         }

         MethodNode methodNode = new MethodNode(name, modifiers, returnType, parameters, exceptions, code);
         methodNode.addAnnotations(annotations);
         methodNode.setGenericsTypes(generics);
         methodNode.setAnnotationDefault(hasAnnotationDefault);
         this.configureAST(methodNode, methodDef);
         if (this.classNode != null) {
            this.classNode.addMethod(methodNode);
         } else {
            this.output.addMethod(methodNode);
         }

      }
   }

   private void checkNoInvalidModifier(AST node, String nodeType, int modifiers, int modifier, String modifierText) {
      if ((modifiers & modifier) != 0) {
         throw new ASTRuntimeException(node, nodeType + " has an incorrect modifier '" + modifierText + "'.");
      }
   }

   private boolean isAnInterface() {
      return this.classNode != null && (this.classNode.getModifiers() & 512) > 0;
   }

   protected void staticInit(AST staticInit) {
      BlockStatement code = (BlockStatement)this.statementList(staticInit);
      this.classNode.addStaticInitializerStatements(code.getStatements(), false);
   }

   protected void objectInit(AST init) {
      BlockStatement code = (BlockStatement)this.statementList(init);
      this.classNode.addObjectInitializerStatements(code);
   }

   protected void constructorDef(AST constructorDef) {
      List<AnnotationNode> annotations = new ArrayList();
      AST node = constructorDef.getFirstChild();
      int modifiers = 1;
      if (isType(5, node)) {
         modifiers = this.modifiers(node, annotations, modifiers);
         node = node.getNextSibling();
      }

      this.assertNodeType(19, node);
      Parameter[] parameters = this.parameters(node);
      if (parameters == null) {
         parameters = Parameter.EMPTY_ARRAY;
      }

      node = node.getNextSibling();
      ClassNode[] exceptions = ClassNode.EMPTY_ARRAY;
      if (isType(126, node)) {
         AST throwsNode = node.getFirstChild();
         List exceptionList = new ArrayList();
         this.throwsList(throwsNode, exceptionList);
         exceptions = (ClassNode[])((ClassNode[])exceptionList.toArray(exceptions));
         node = node.getNextSibling();
      }

      this.assertNodeType(7, node);
      Statement code = this.statementList(node);
      ConstructorNode constructorNode = this.classNode.addConstructor(modifiers, parameters, exceptions, code);
      constructorNode.addAnnotations(annotations);
      this.configureAST(constructorNode, constructorDef);
   }

   protected void fieldDef(AST fieldDef) {
      List<AnnotationNode> annotations = new ArrayList();
      AST node = fieldDef.getFirstChild();
      int modifiers = 0;
      if (isType(5, node)) {
         modifiers = this.modifiers(node, annotations, modifiers);
         node = node.getNextSibling();
      }

      if (this.classNode.isInterface()) {
         modifiers |= 24;
         if ((modifiers & 6) == 0) {
            modifiers |= 1;
         }
      }

      ClassNode type = null;
      if (isType(12, node)) {
         type = this.makeTypeWithArguments(node);
         node = node.getNextSibling();
      }

      String name = this.identifier(node);
      node = node.getNextSibling();
      Expression initialValue = null;
      if (node != null) {
         this.assertNodeType(120, node);
         initialValue = this.expression(node.getFirstChild());
      }

      if (this.classNode.isInterface() && initialValue == null && type != null) {
         if (type == ClassHelper.int_TYPE) {
            initialValue = new ConstantExpression(0);
         } else if (type == ClassHelper.long_TYPE) {
            initialValue = new ConstantExpression(0L);
         } else if (type == ClassHelper.double_TYPE) {
            initialValue = new ConstantExpression(0.0D);
         } else if (type == ClassHelper.float_TYPE) {
            initialValue = new ConstantExpression(0.0F);
         } else if (type == ClassHelper.boolean_TYPE) {
            initialValue = ConstantExpression.FALSE;
         } else if (type == ClassHelper.short_TYPE) {
            initialValue = new ConstantExpression(Short.valueOf((short)0));
         } else if (type == ClassHelper.byte_TYPE) {
            initialValue = new ConstantExpression((byte)0);
         } else if (type == ClassHelper.char_TYPE) {
            initialValue = new ConstantExpression('\u0000');
         }
      }

      FieldNode fieldNode = new FieldNode(name, modifiers, type, this.classNode, (Expression)initialValue);
      fieldNode.addAnnotations(annotations);
      this.configureAST(fieldNode, fieldDef);
      if (!this.hasVisibility(modifiers)) {
         int fieldModifiers = 0;
         int flags = 216;
         if (!this.hasVisibility(modifiers)) {
            modifiers |= 1;
            fieldModifiers |= 2;
         }

         fieldModifiers |= modifiers & flags;
         fieldNode.setModifiers(fieldModifiers);
         fieldNode.setSynthetic(true);
         FieldNode storedNode = this.classNode.getDeclaredField(fieldNode.getName());
         if (storedNode != null && !this.classNode.hasProperty(name)) {
            fieldNode = storedNode;
            this.classNode.getFields().remove(storedNode);
         }

         PropertyNode propertyNode = new PropertyNode(fieldNode, modifiers, (Statement)null, (Statement)null);
         this.configureAST(propertyNode, fieldDef);
         this.classNode.addProperty(propertyNode);
      } else {
         fieldNode.setModifiers(modifiers);
         PropertyNode pn = this.classNode.getProperty(name);
         if (pn != null && pn.getField().isSynthetic()) {
            this.classNode.getFields().remove(pn.getField());
            pn.setField(fieldNode);
         }

         this.classNode.addField(fieldNode);
      }

   }

   protected ClassNode[] interfaces(AST node) {
      List<ClassNode> interfaceList = new ArrayList();

      for(AST implementNode = node.getFirstChild(); implementNode != null; implementNode = implementNode.getNextSibling()) {
         interfaceList.add(this.makeTypeWithArguments(implementNode));
      }

      ClassNode[] interfaces = ClassNode.EMPTY_ARRAY;
      if (!interfaceList.isEmpty()) {
         interfaces = new ClassNode[interfaceList.size()];
         interfaceList.toArray(interfaces);
      }

      return interfaces;
   }

   protected Parameter[] parameters(AST parametersNode) {
      AST node = parametersNode.getFirstChild();
      this.firstParam = false;
      this.firstParamIsVarArg = false;
      if (node == null) {
         return isType(50, parametersNode) ? Parameter.EMPTY_ARRAY : null;
      } else {
         List<Parameter> parameters = new ArrayList();
         AST firstParameterNode = null;

         do {
            this.firstParam = firstParameterNode == null;
            if (firstParameterNode == null) {
               firstParameterNode = node;
            }

            parameters.add(this.parameter(node));
            node = node.getNextSibling();
         } while(node != null);

         this.verifyParameters(parameters, firstParameterNode);
         Parameter[] answer = new Parameter[parameters.size()];
         parameters.toArray(answer);
         return answer;
      }
   }

   private void verifyParameters(List<Parameter> parameters, AST firstParameterNode) {
      if (parameters.size() > 1) {
         Parameter first = (Parameter)parameters.get(0);
         if (this.firstParamIsVarArg) {
            throw new ASTRuntimeException(firstParameterNode, "The var-arg parameter " + first.getName() + " must be the last parameter.");
         }
      }
   }

   protected Parameter parameter(AST paramNode) {
      List<AnnotationNode> annotations = new ArrayList();
      boolean variableParameterDef = isType(46, paramNode);
      AST node = paramNode.getFirstChild();
      int modifiers = 0;
      if (isType(5, node)) {
         this.modifiers(node, annotations, modifiers);
         node = node.getNextSibling();
      }

      ClassNode type = ClassHelper.DYNAMIC_TYPE;
      if (isType(12, node)) {
         type = this.makeTypeWithArguments(node);
         if (variableParameterDef) {
            type = type.makeArray();
         }

         node = node.getNextSibling();
      }

      String name = this.identifier(node);
      node = node.getNextSibling();
      VariableExpression leftExpression = new VariableExpression(name, type);
      this.configureAST(leftExpression, paramNode);
      Parameter parameter = null;
      if (node != null) {
         this.assertNodeType(120, node);
         Expression rightExpression = this.expression(node.getFirstChild());
         if (this.isAnInterface()) {
            throw new ASTRuntimeException(node, "Cannot specify default value for method parameter '" + name + " = " + rightExpression.getText() + "' inside an interface");
         }

         parameter = new Parameter(type, name, rightExpression);
      } else {
         parameter = new Parameter(type, name);
      }

      if (this.firstParam) {
         this.firstParamIsVarArg = variableParameterDef;
      }

      this.configureAST(parameter, paramNode);
      parameter.addAnnotations(annotations);
      return parameter;
   }

   protected int modifiers(AST modifierNode, List<AnnotationNode> annotations, int defaultModifiers) {
      this.assertNodeType(5, modifierNode);
      boolean access = false;
      int answer = 0;

      for(AST node = modifierNode.getFirstChild(); node != null; node = node.getNextSibling()) {
         int type = node.getType();
         switch(type) {
         case 37:
            answer = this.setModifierBit(node, answer, 16);
            break;
         case 38:
            answer = this.setModifierBit(node, answer, 1024);
            break;
         case 42:
            answer = this.setModifierBit(node, answer, 2048);
         case 59:
            break;
         case 65:
            annotations.add(this.annotation(node));
            break;
         case 80:
            answer = this.setModifierBit(node, answer, 8);
            break;
         case 111:
            answer = this.setModifierBit(node, answer, 2);
            access = this.setAccessTrue(node, access);
            break;
         case 112:
            answer = this.setModifierBit(node, answer, 1);
            access = this.setAccessTrue(node, access);
            break;
         case 113:
            answer = this.setModifierBit(node, answer, 4);
            access = this.setAccessTrue(node, access);
            break;
         case 114:
            answer = this.setModifierBit(node, answer, 128);
            break;
         case 115:
            answer = this.setModifierBit(node, answer, 256);
            break;
         case 117:
            answer = this.setModifierBit(node, answer, 32);
            break;
         case 118:
            answer = this.setModifierBit(node, answer, 64);
            break;
         default:
            this.unknownAST(node);
         }
      }

      if (!access) {
         answer |= defaultModifiers;
      }

      return answer;
   }

   protected boolean setAccessTrue(AST node, boolean access) {
      if (!access) {
         return true;
      } else {
         throw new ASTRuntimeException(node, "Cannot specify modifier: " + node.getText() + " when access scope has already been defined");
      }
   }

   protected int setModifierBit(AST node, int answer, int bit) {
      if ((answer & bit) != 0) {
         throw new ASTRuntimeException(node, "Cannot repeat modifier: " + node.getText());
      } else {
         return answer | bit;
      }
   }

   protected AnnotationNode annotation(AST annotationNode) {
      AST node = annotationNode.getFirstChild();
      String name = qualifiedName(node);
      AnnotationNode annotatedNode = new AnnotationNode(ClassHelper.make(name));
      this.configureAST(annotatedNode, annotationNode);

      while(true) {
         node = node.getNextSibling();
         if (!isType(66, node)) {
            return annotatedNode;
         }

         AST memberNode = node.getFirstChild();
         String param = this.identifier(memberNode);
         Expression expression = this.expression(memberNode.getNextSibling());
         if (annotatedNode.getMember(param) != null) {
            throw new ASTRuntimeException(memberNode, "Annotation member '" + param + "' has already been associated with a value");
         }

         annotatedNode.setMember(param, expression);
      }
   }

   protected Statement statement(AST node) {
      Statement statement = null;
      int type = node.getType();
      switch(type) {
      case 7:
      case 147:
         statement = this.statementList(node);
         break;
      case 9:
         statement = this.variableDef(node);
         break;
      case 21:
         statement = this.labelledStatement(node);
         break;
      case 26:
         statement = this.methodCall(node);
         break;
      case 117:
         statement = this.synchronizedStatement(node);
         break;
      case 132:
         statement = this.ifStatement(node);
         break;
      case 134:
         statement = this.whileStatement(node);
         break;
      case 135:
         statement = this.switchStatement(node);
         break;
      case 136:
         statement = this.forStatement(node);
         break;
      case 138:
         statement = this.returnStatement(node);
         break;
      case 139:
         statement = this.breakStatement(node);
         break;
      case 140:
         statement = this.continueStatement(node);
         break;
      case 141:
         statement = this.throwStatement(node);
         break;
      case 142:
         statement = this.assertStatement(node);
         break;
      case 146:
         statement = this.tryStatement(node);
         break;
      default:
         statement = new ExpressionStatement(this.expression(node));
      }

      if (statement != null) {
         this.configureAST((ASTNode)statement, node);
      }

      return (Statement)statement;
   }

   protected Statement statementList(AST code) {
      return this.statementListNoChild(code.getFirstChild(), code);
   }

   protected Statement statementListNoChild(AST node, AST alternativeConfigureNode) {
      BlockStatement block = new BlockStatement();
      if (node != null) {
         this.configureAST(block, node);
      } else {
         this.configureAST(block, alternativeConfigureNode);
      }

      while(node != null) {
         block.addStatement(this.statement(node));
         node = node.getNextSibling();
      }

      return block;
   }

   protected Statement assertStatement(AST assertNode) {
      AST node = assertNode.getFirstChild();
      BooleanExpression booleanExpression = this.booleanExpression(node);
      Expression messageExpression = null;
      node = node.getNextSibling();
      if (node != null) {
         messageExpression = this.expression(node);
      } else {
         messageExpression = ConstantExpression.NULL;
      }

      AssertStatement assertStatement = new AssertStatement(booleanExpression, (Expression)messageExpression);
      this.configureAST(assertStatement, assertNode);
      return assertStatement;
   }

   protected Statement breakStatement(AST node) {
      BreakStatement breakStatement = new BreakStatement(this.label(node));
      this.configureAST(breakStatement, node);
      return breakStatement;
   }

   protected Statement continueStatement(AST node) {
      ContinueStatement continueStatement = new ContinueStatement(this.label(node));
      this.configureAST(continueStatement, node);
      return continueStatement;
   }

   protected Statement forStatement(AST forNode) {
      AST inNode = forNode.getFirstChild();
      Object collectionExpression;
      Parameter forParameter;
      AST node;
      if (isType(76, inNode)) {
         this.forStatementBeingDef = true;
         ClosureListExpression clist = this.closureListExpression(inNode);
         this.forStatementBeingDef = false;
         int size = clist.getExpressions().size();
         if (size != 3) {
            throw new ASTRuntimeException(inNode, "3 expressions are required for the classic for loop, you gave " + size);
         }

         collectionExpression = clist;
         forParameter = ForStatement.FOR_LOOP_DUMMY;
      } else {
         node = inNode.getFirstChild();
         AST collectionNode = node.getNextSibling();
         ClassNode type = ClassHelper.OBJECT_TYPE;
         if (isType(9, node)) {
            AST node = node.getFirstChild();
            if (isType(5, node)) {
               int modifiersMask = this.modifiers(node, new ArrayList(), 0);
               if ((modifiersMask & -17) != 0) {
                  throw new ASTRuntimeException(node, "Only the 'final' modifier is allowed in front of the for loop variable.");
               }

               node = node.getNextSibling();
            }

            type = this.makeTypeWithArguments(node);
            node = node.getNextSibling();
         }

         String variable = this.identifier(node);
         collectionExpression = this.expression(collectionNode);
         forParameter = new Parameter(type, variable);
         this.configureAST(forParameter, node);
      }

      node = inNode.getNextSibling();
      Object block;
      if (isType(124, node)) {
         block = EmptyStatement.INSTANCE;
      } else {
         block = this.statement(node);
      }

      ForStatement forStatement = new ForStatement(forParameter, (Expression)collectionExpression, (Statement)block);
      this.configureAST(forStatement, forNode);
      return forStatement;
   }

   protected Statement ifStatement(AST ifNode) {
      AST node = ifNode.getFirstChild();
      this.assertNodeType(27, node);
      BooleanExpression booleanExpression = this.booleanExpression(node);
      node = node.getNextSibling();
      Statement ifBlock = this.statement(node);
      Statement elseBlock = EmptyStatement.INSTANCE;
      node = node.getNextSibling();
      if (node != null) {
         elseBlock = this.statement(node);
      }

      IfStatement ifStatement = new IfStatement(booleanExpression, ifBlock, (Statement)elseBlock);
      this.configureAST(ifStatement, ifNode);
      return ifStatement;
   }

   protected Statement labelledStatement(AST labelNode) {
      AST node = labelNode.getFirstChild();
      String label = this.identifier(node);
      Statement statement = this.statement(node.getNextSibling());
      if (statement.getStatementLabel() == null) {
         statement.setStatementLabel(label);
      }

      return statement;
   }

   protected Statement methodCall(AST code) {
      Expression expression = this.methodCallExpression(code);
      ExpressionStatement expressionStatement = new ExpressionStatement(expression);
      this.configureAST(expressionStatement, code);
      return expressionStatement;
   }

   protected Expression declarationExpression(AST variableDef) {
      AST node = variableDef.getFirstChild();
      ClassNode type = null;
      List<AnnotationNode> annotations = new ArrayList();
      boolean staticVariable = false;
      AST modifierNode = null;
      if (isType(5, node)) {
         int modifiers = this.modifiers(node, annotations, 0);
         if ((modifiers & 8) != 0) {
            modifierNode = node;
            staticVariable = true;
         }

         node = node.getNextSibling();
      }

      if (isType(12, node)) {
         type = this.makeTypeWithArguments(node);
         node = node.getNextSibling();
      }

      Expression rightExpression = ConstantExpression.NULL;
      AST right;
      Object leftExpression;
      if (isType(120, node)) {
         node = node.getFirstChild();
         AST left = node.getFirstChild();
         ArgumentListExpression alist = new ArgumentListExpression();

         for(AST varDef = left; varDef != null; varDef = varDef.getNextSibling()) {
            this.assertNodeType(9, varDef);
            DeclarationExpression de = (DeclarationExpression)this.declarationExpression(varDef);
            alist.addExpression(de.getVariableExpression());
         }

         leftExpression = alist;
         right = node.getNextSibling();
         if (right != null) {
            rightExpression = this.expression(right);
         }
      } else {
         if (staticVariable) {
            throw new ASTRuntimeException(modifierNode, "Variable definition has an incorrect modifier 'static'.");
         }

         String name = this.identifier(node);
         VariableExpression ve = new VariableExpression(name, type);
         ve.addAnnotations(annotations);
         leftExpression = ve;
         right = node.getNextSibling();
         if (right != null) {
            this.assertNodeType(120, right);
            rightExpression = this.expression(right.getFirstChild());
         }
      }

      this.configureAST((ASTNode)leftExpression, node);
      Token token = makeToken(100, variableDef);
      DeclarationExpression expression = new DeclarationExpression((Expression)leftExpression, token, (Expression)rightExpression);
      this.configureAST(expression, variableDef);
      ExpressionStatement expressionStatement = new ExpressionStatement(expression);
      this.configureAST(expressionStatement, variableDef);
      return expression;
   }

   protected Statement variableDef(AST variableDef) {
      ExpressionStatement expressionStatement = new ExpressionStatement(this.declarationExpression(variableDef));
      this.configureAST(expressionStatement, variableDef);
      return expressionStatement;
   }

   protected Statement returnStatement(AST node) {
      AST exprNode = node.getFirstChild();
      Expression expression = exprNode == null ? ConstantExpression.NULL : this.expression(exprNode);
      ReturnStatement returnStatement = new ReturnStatement((Expression)expression);
      this.configureAST(returnStatement, node);
      return returnStatement;
   }

   protected Statement switchStatement(AST switchNode) {
      AST node = switchNode.getFirstChild();
      Expression expression = this.expression(node);
      Statement defaultStatement = EmptyStatement.INSTANCE;
      List list = new ArrayList();

      for(node = node.getNextSibling(); isType(31, node); node = node.getNextSibling()) {
         AST child = node.getFirstChild();
         Statement tmpDefaultStatement;
         if (isType(145, child)) {
            List cases = new LinkedList();
            tmpDefaultStatement = this.caseStatements(child, cases);
            list.addAll(cases);
         } else {
            tmpDefaultStatement = this.statement(child.getNextSibling());
         }

         if (tmpDefaultStatement != EmptyStatement.INSTANCE) {
            if (defaultStatement != EmptyStatement.INSTANCE) {
               throw new ASTRuntimeException(switchNode, "The default case is already defined.");
            }

            defaultStatement = tmpDefaultStatement;
         }
      }

      if (node != null) {
         this.unknownAST(node);
      }

      SwitchStatement switchStatement = new SwitchStatement(expression, list, (Statement)defaultStatement);
      this.configureAST(switchStatement, switchNode);
      return switchStatement;
   }

   protected Statement caseStatements(AST node, List cases) {
      List<Expression> expressions = new LinkedList();
      Statement statement = EmptyStatement.INSTANCE;
      Statement defaultStatement = EmptyStatement.INSTANCE;
      AST nextSibling = node;

      do {
         Expression expression = this.expression(nextSibling.getFirstChild());
         expressions.add(expression);
         nextSibling = nextSibling.getNextSibling();
      } while(isType(145, nextSibling));

      if (nextSibling != null) {
         if (isType(125, nextSibling)) {
            defaultStatement = this.statement(nextSibling.getNextSibling());
            statement = EmptyStatement.INSTANCE;
         } else {
            statement = this.statement(nextSibling);
         }
      }

      Iterator iterator = expressions.iterator();

      while(iterator.hasNext()) {
         Expression expr = (Expression)iterator.next();
         CaseStatement stmt;
         if (iterator.hasNext()) {
            stmt = new CaseStatement(expr, EmptyStatement.INSTANCE);
         } else {
            stmt = new CaseStatement(expr, (Statement)statement);
         }

         this.configureAST(stmt, node);
         cases.add(stmt);
      }

      return (Statement)defaultStatement;
   }

   protected Statement synchronizedStatement(AST syncNode) {
      AST node = syncNode.getFirstChild();
      Expression expression = this.expression(node);
      Statement code = this.statement(node.getNextSibling());
      SynchronizedStatement synchronizedStatement = new SynchronizedStatement(expression, code);
      this.configureAST(synchronizedStatement, syncNode);
      return synchronizedStatement;
   }

   protected Statement throwStatement(AST node) {
      AST expressionNode = node.getFirstChild();
      if (expressionNode == null) {
         expressionNode = node.getNextSibling();
      }

      if (expressionNode == null) {
         throw new ASTRuntimeException(node, "No expression available");
      } else {
         ThrowStatement throwStatement = new ThrowStatement(this.expression(expressionNode));
         this.configureAST(throwStatement, node);
         return throwStatement;
      }
   }

   protected Statement tryStatement(AST tryStatementNode) {
      AST tryNode = tryStatementNode.getFirstChild();
      Statement tryStatement = this.statement(tryNode);
      Statement finallyStatement = EmptyStatement.INSTANCE;
      AST node = tryNode.getNextSibling();

      ArrayList catches;
      for(catches = new ArrayList(); node != null && isType(148, node); node = node.getNextSibling()) {
         catches.add(this.catchStatement(node));
      }

      if (isType(147, node)) {
         finallyStatement = this.statement(node);
         node = node.getNextSibling();
      }

      if (finallyStatement instanceof EmptyStatement && catches.size() == 0) {
         throw new ASTRuntimeException(tryStatementNode, "A try statement must have at least one catch or finally block.");
      } else {
         TryCatchStatement tryCatchStatement = new TryCatchStatement(tryStatement, (Statement)finallyStatement);
         this.configureAST(tryCatchStatement, tryStatementNode);
         Iterator i$ = catches.iterator();

         while(i$.hasNext()) {
            CatchStatement statement = (CatchStatement)i$.next();
            tryCatchStatement.addCatch(statement);
         }

         return tryCatchStatement;
      }
   }

   protected CatchStatement catchStatement(AST catchNode) {
      AST node = catchNode.getFirstChild();
      Parameter parameter = this.parameter(node);
      ClassNode exceptionType = parameter.getType();
      String variable = parameter.getName();
      node = node.getNextSibling();
      Statement code = this.statement(node);
      Parameter catchParameter = new Parameter(exceptionType, variable);
      CatchStatement answer = new CatchStatement(catchParameter, code);
      this.configureAST(answer, catchNode);
      return answer;
   }

   protected Statement whileStatement(AST whileNode) {
      AST node = whileNode.getFirstChild();
      this.assertNodeType(27, node);
      if (isType(9, node.getFirstChild())) {
         throw new ASTRuntimeException(whileNode, "While loop condition contains a declaration; this is currently unsupported.");
      } else {
         BooleanExpression booleanExpression = this.booleanExpression(node);
         node = node.getNextSibling();
         Object block;
         if (isType(124, node)) {
            block = EmptyStatement.INSTANCE;
         } else {
            block = this.statement(node);
         }

         WhileStatement whileStatement = new WhileStatement(booleanExpression, (Statement)block);
         this.configureAST(whileStatement, whileNode);
         return whileStatement;
      }
   }

   protected Expression expression(AST node) {
      return this.expression(node, false);
   }

   protected Expression expression(AST node, boolean convertToConstant) {
      Expression expression = this.expressionSwitch(node);
      if (convertToConstant && expression instanceof VariableExpression) {
         VariableExpression ve = (VariableExpression)expression;
         if (!ve.isThisExpression() && !ve.isSuperExpression()) {
            expression = new ConstantExpression(ve.getName());
         }
      }

      this.configureAST((ASTNode)expression, node);
      return (Expression)expression;
   }

   protected Expression expressionSwitch(AST node) {
      int type = node.getType();
      switch(type) {
      case 6:
         return this.anonymousInnerClassDef(node);
      case 7:
         return this.blockExpression(node);
      case 8:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 28:
      case 31:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 45:
      case 46:
      case 48:
      case 50:
      case 51:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 77:
      case 78:
      case 79:
      case 80:
      case 81:
      case 83:
      case 89:
      case 90:
      case 91:
      case 92:
      case 94:
      case 96:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 119:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 145:
      case 146:
      case 147:
      case 148:
      case 193:
      case 194:
      default:
         this.unknownAST(node);
         return null;
      case 9:
         return this.declarationExpression(node);
      case 22:
         return this.castExpression(node);
      case 23:
         return this.indexExpression(node);
      case 24:
         return this.postfixExpression(node, 250);
      case 25:
         return this.postfixExpression(node, 260);
      case 26:
         return this.methodCallExpression(node);
      case 27:
         return this.expression(node.getFirstChild());
      case 29:
         return this.unaryMinusExpression(node);
      case 30:
         return this.unaryPlusExpression(node);
      case 32:
         return this.expressionList(node);
      case 43:
         return this.specialConstructorCallExpression(node, ClassNode.SUPER);
      case 44:
         return this.specialConstructorCallExpression(node, ClassNode.THIS);
      case 47:
         return this.gstring(node);
      case 49:
         return this.closureExpression(node);
      case 52:
         return this.dynamicMemberExpression(node);
      case 53:
         return this.mapEntryExpression(node);
      case 54:
         return this.spreadExpression(node);
      case 55:
         return this.spreadMapExpression(node);
      case 56:
         return this.listExpression(node);
      case 57:
         return this.mapExpression(node);
      case 65:
         return new AnnotationConstantExpression(this.annotation(node));
      case 76:
         return this.closureListExpression(node);
      case 82:
      case 88:
         return this.tupleExpression(node);
      case 84:
      case 95:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 128:
         return this.variableExpression(node);
      case 85:
         return this.literalExpression(node, node.getText());
      case 86:
         return this.binaryExpression(124, node);
      case 87:
      case 149:
      case 150:
         return this.dotExpression(node);
      case 93:
      case 169:
         return this.ternaryExpression(node);
      case 97:
         return this.binaryExpression(126, node);
      case 98:
         return this.binaryExpression(281, node);
      case 99:
         return this.binaryExpression(282, node);
      case 109:
         return this.binaryExpression(202, node);
      case 110:
         return this.asExpression(node);
      case 120:
         return this.binaryExpression(100, node);
      case 121:
         return this.binaryExpression(341, node);
      case 137:
         return this.binaryExpression(573, node);
      case 143:
         return this.binaryExpression(200, node);
      case 144:
         return this.binaryExpression(201, node);
      case 151:
         return this.methodPointerExpression(node);
      case 152:
         return this.literalExpression(node, Boolean.FALSE);
      case 153:
         return this.instanceofExpression(node);
      case 154:
         return this.constructorCallExpression(node);
      case 155:
         return this.literalExpression(node, (Object)null);
      case 156:
         return this.literalExpression(node, Boolean.TRUE);
      case 157:
         return this.binaryExpression(210, node);
      case 158:
         return this.binaryExpression(211, node);
      case 159:
         return this.binaryExpression(212, node);
      case 160:
         return this.binaryExpression(213, node);
      case 161:
         return this.binaryExpression(215, node);
      case 162:
         return this.binaryExpression(286, node);
      case 163:
         return this.binaryExpression(287, node);
      case 164:
         return this.binaryExpression(285, node);
      case 165:
         return this.binaryExpression(351, node);
      case 166:
         return this.binaryExpression(352, node);
      case 167:
         return this.binaryExpression(350, node);
      case 168:
         return this.binaryExpression(216, node);
      case 170:
         return this.binaryExpression(162, node);
      case 171:
         return this.binaryExpression(164, node);
      case 172:
         return this.binaryExpression(340, node);
      case 173:
         return this.binaryExpression(342, node);
      case 174:
         return this.binaryExpression(90, node);
      case 175:
         return this.binaryExpression(94, node);
      case 176:
         return this.binaryExpression(120, node);
      case 177:
         return this.binaryExpression(123, node);
      case 178:
         return this.binaryExpression(121, node);
      case 179:
         return this.binaryExpression(122, node);
      case 180:
         return this.binaryExpression(128, node);
      case 181:
         return this.binaryExpression(125, node);
      case 182:
         return this.binaryExpression(127, node);
      case 183:
         return this.binaryExpression(280, node);
      case 184:
         return this.rangeExpression(node, true);
      case 185:
         return this.rangeExpression(node, false);
      case 186:
         return this.prefixExpression(node, 250);
      case 187:
         return this.binaryExpression(203, node);
      case 188:
         return this.binaryExpression(205, node);
      case 189:
         return this.prefixExpression(node, 260);
      case 190:
         return this.binaryExpression(206, node);
      case 191:
         BitwiseNegationExpression bitwiseNegationExpression = new BitwiseNegationExpression(this.expression(node.getFirstChild()));
         this.configureAST(bitwiseNegationExpression, node);
         return bitwiseNegationExpression;
      case 192:
         NotExpression notExpression = new NotExpression(this.expression(node.getFirstChild()));
         this.configureAST(notExpression, node);
         return notExpression;
      case 195:
      case 197:
      case 199:
         return this.integerExpression(node);
      case 196:
      case 198:
      case 200:
         return this.decimalExpression(node);
      }
   }

   private TupleExpression tupleExpression(AST node) {
      TupleExpression exp = new TupleExpression();
      this.configureAST(exp, node);

      for(node = node.getFirstChild(); node != null; node = node.getNextSibling()) {
         this.assertNodeType(9, node);
         AST nameNode = node.getFirstChild().getNextSibling();
         VariableExpression varExp = new VariableExpression(nameNode.getText());
         this.configureAST(varExp, nameNode);
         exp.addExpression(varExp);
      }

      return exp;
   }

   private ClosureListExpression closureListExpression(AST node) {
      this.isClosureListExpressionAllowedHere(node);
      AST exprNode = node.getFirstChild();

      LinkedList list;
      for(list = new LinkedList(); exprNode != null; exprNode = exprNode.getNextSibling()) {
         if (isType(27, exprNode)) {
            Expression expr = this.expression(exprNode);
            this.configureAST(expr, exprNode);
            list.add(expr);
         } else {
            this.assertNodeType(36, exprNode);
            list.add(EmptyExpression.INSTANCE);
         }
      }

      ClosureListExpression cle = new ClosureListExpression(list);
      this.configureAST(cle, node);
      return cle;
   }

   private void isClosureListExpressionAllowedHere(AST node) {
      if (!this.forStatementBeingDef) {
         throw new ASTRuntimeException(node, "Expression list of the form (a; b; c) is not supported in this context.");
      }
   }

   protected Expression dynamicMemberExpression(AST dynamicMemberNode) {
      AST node = dynamicMemberNode.getFirstChild();
      return this.expression(node);
   }

   protected Expression ternaryExpression(AST ternaryNode) {
      AST node = ternaryNode.getFirstChild();
      Expression base = this.expression(node);
      node = node.getNextSibling();
      Expression left = this.expression(node);
      node = node.getNextSibling();
      Object ret;
      if (node == null) {
         ret = new ElvisOperatorExpression(base, left);
      } else {
         Expression right = this.expression(node);
         BooleanExpression booleanExpression = new BooleanExpression(base);
         booleanExpression.setSourcePosition(base);
         ret = new TernaryExpression(booleanExpression, left, right);
      }

      this.configureAST((ASTNode)ret, ternaryNode);
      return (Expression)ret;
   }

   protected Expression variableExpression(AST node) {
      String text = node.getText();
      VariableExpression variableExpression = new VariableExpression(text);
      this.configureAST(variableExpression, node);
      return variableExpression;
   }

   protected Expression literalExpression(AST node, Object value) {
      ConstantExpression constantExpression = new ConstantExpression(value);
      this.configureAST(constantExpression, node);
      return constantExpression;
   }

   protected Expression rangeExpression(AST rangeNode, boolean inclusive) {
      AST node = rangeNode.getFirstChild();
      Expression left = this.expression(node);
      Expression right = this.expression(node.getNextSibling());
      RangeExpression rangeExpression = new RangeExpression(left, right, inclusive);
      this.configureAST(rangeExpression, rangeNode);
      return rangeExpression;
   }

   protected Expression spreadExpression(AST node) {
      AST exprNode = node.getFirstChild();
      AST listNode = exprNode.getFirstChild();
      Expression right = this.expression(listNode);
      SpreadExpression spreadExpression = new SpreadExpression(right);
      this.configureAST(spreadExpression, node);
      return spreadExpression;
   }

   protected Expression spreadMapExpression(AST node) {
      AST exprNode = node.getFirstChild();
      Expression expr = this.expression(exprNode);
      SpreadMapExpression spreadMapExpression = new SpreadMapExpression(expr);
      this.configureAST(spreadMapExpression, node);
      return spreadMapExpression;
   }

   protected Expression methodPointerExpression(AST node) {
      AST exprNode = node.getFirstChild();
      Expression objectExpression = this.expression(exprNode);
      AST mNode = exprNode.getNextSibling();
      Object methodName;
      if (isType(52, mNode)) {
         methodName = this.expression(mNode);
      } else {
         methodName = new ConstantExpression(this.identifier(mNode));
      }

      this.configureAST((ASTNode)methodName, mNode);
      MethodPointerExpression methodPointerExpression = new MethodPointerExpression(objectExpression, (Expression)methodName);
      this.configureAST(methodPointerExpression, node);
      return methodPointerExpression;
   }

   protected Expression listExpression(AST listNode) {
      List<Expression> expressions = new ArrayList();
      AST elist = listNode.getFirstChild();
      this.assertNodeType(32, elist);

      for(AST node = elist.getFirstChild(); node != null; node = node.getNextSibling()) {
         switch(node.getType()) {
         case 53:
            this.assertNodeType(96, node);
            break;
         case 55:
            this.assertNodeType(54, node);
         }

         expressions.add(this.expression(node));
      }

      ListExpression listExpression = new ListExpression(expressions);
      this.configureAST(listExpression, listNode);
      return listExpression;
   }

   protected Expression mapExpression(AST mapNode) {
      List expressions = new ArrayList();
      AST elist = mapNode.getFirstChild();
      if (elist != null) {
         this.assertNodeType(32, elist);

         for(AST node = elist.getFirstChild(); node != null; node = node.getNextSibling()) {
            switch(node.getType()) {
            case 53:
            case 55:
               break;
            case 54:
               this.assertNodeType(55, node);
               break;
            default:
               this.assertNodeType(53, node);
            }

            expressions.add(this.mapEntryExpression(node));
         }
      }

      MapExpression mapExpression = new MapExpression(expressions);
      this.configureAST(mapExpression, mapNode);
      return mapExpression;
   }

   protected MapEntryExpression mapEntryExpression(AST node) {
      AST keyNode;
      Expression keyExpression;
      if (node.getType() == 55) {
         keyNode = node.getFirstChild();
         keyExpression = this.spreadMapExpression(node);
         Expression rightExpression = this.expression(keyNode);
         MapEntryExpression mapEntryExpression = new MapEntryExpression(keyExpression, rightExpression);
         this.configureAST(mapEntryExpression, node);
         return mapEntryExpression;
      } else {
         keyNode = node.getFirstChild();
         keyExpression = this.expression(keyNode);
         AST valueNode = keyNode.getNextSibling();
         Expression valueExpression = this.expression(valueNode);
         MapEntryExpression mapEntryExpression = new MapEntryExpression(keyExpression, valueExpression);
         this.configureAST(mapEntryExpression, node);
         return mapEntryExpression;
      }
   }

   protected Expression instanceofExpression(AST node) {
      AST leftNode = node.getFirstChild();
      Expression leftExpression = this.expression(leftNode);
      AST rightNode = leftNode.getNextSibling();
      ClassNode type = this.buildName(rightNode);
      this.assertTypeNotNull(type, rightNode);
      Expression rightExpression = new ClassExpression(type);
      this.configureAST(rightExpression, rightNode);
      BinaryExpression binaryExpression = new BinaryExpression(leftExpression, makeToken(544, node), rightExpression);
      this.configureAST(binaryExpression, node);
      return binaryExpression;
   }

   protected void assertTypeNotNull(ClassNode type, AST rightNode) {
      if (type == null) {
         throw new ASTRuntimeException(rightNode, "No type available for: " + qualifiedName(rightNode));
      }
   }

   protected Expression asExpression(AST node) {
      AST leftNode = node.getFirstChild();
      Expression leftExpression = this.expression(leftNode);
      AST rightNode = leftNode.getNextSibling();
      ClassNode type = this.makeTypeWithArguments(rightNode);
      return CastExpression.asExpression(type, leftExpression);
   }

   protected Expression castExpression(AST castNode) {
      AST node = castNode.getFirstChild();
      ClassNode type = this.makeTypeWithArguments(node);
      this.assertTypeNotNull(type, node);
      AST expressionNode = node.getNextSibling();
      Expression expression = this.expression(expressionNode);
      CastExpression castExpression = new CastExpression(type, expression);
      this.configureAST(castExpression, castNode);
      return castExpression;
   }

   protected Expression indexExpression(AST indexNode) {
      AST bracket = indexNode.getFirstChild();
      AST leftNode = bracket.getNextSibling();
      Expression leftExpression = this.expression(leftNode);
      AST rightNode = leftNode.getNextSibling();
      Expression rightExpression = this.expression(rightNode);
      BinaryExpression binaryExpression = new BinaryExpression(leftExpression, makeToken(30, bracket), rightExpression);
      this.configureAST(binaryExpression, indexNode);
      return binaryExpression;
   }

   protected Expression binaryExpression(int type, AST node) {
      Token token = makeToken(type, node);
      AST leftNode = node.getFirstChild();
      Expression leftExpression = this.expression(leftNode);
      AST rightNode = leftNode.getNextSibling();
      if (rightNode == null) {
         return leftExpression;
      } else {
         Expression leftexp;
         if (Types.ofType(type, 1100) && !(leftExpression instanceof VariableExpression) && leftExpression.getClass() != PropertyExpression.class && !(leftExpression instanceof FieldExpression) && !(leftExpression instanceof AttributeExpression) && !(leftExpression instanceof DeclarationExpression) && !(leftExpression instanceof TupleExpression)) {
            if (leftExpression instanceof ConstantExpression) {
               throw new ASTRuntimeException(node, "\n[" + ((ConstantExpression)leftExpression).getValue() + "] is a constant expression, but it should be a variable expression");
            }

            if (!(leftExpression instanceof BinaryExpression)) {
               if (leftExpression instanceof GStringExpression) {
                  throw new ASTRuntimeException(node, "\n\"" + ((GStringExpression)leftExpression).getText() + "\" is a GString expression, but it should be a variable expression");
               }

               if (leftExpression instanceof MethodCallExpression) {
                  throw new ASTRuntimeException(node, "\n\"" + ((MethodCallExpression)leftExpression).getText() + "\" is a method call expression, but it should be a variable expression");
               }

               if (leftExpression instanceof MapExpression) {
                  throw new ASTRuntimeException(node, "\n'" + ((MapExpression)leftExpression).getText() + "' is a map expression, but it should be a variable expression");
               }

               throw new ASTRuntimeException(node, "\n" + leftExpression.getClass() + ", with its value '" + leftExpression.getText() + "', is a bad expression as the left hand side of an assignment operator");
            }

            leftexp = ((BinaryExpression)leftExpression).getLeftExpression();
            int lefttype = ((BinaryExpression)leftExpression).getOperation().getType();
            if (!Types.ofType(lefttype, 1100) && lefttype != 30) {
               throw new ASTRuntimeException(node, "\n" + ((BinaryExpression)leftExpression).getText() + " is a binary expression, but it should be a variable expression");
            }
         }

         leftexp = this.expression(rightNode);
         BinaryExpression binaryExpression = new BinaryExpression(leftExpression, token, leftexp);
         this.configureAST(binaryExpression, node);
         return binaryExpression;
      }
   }

   protected Expression prefixExpression(AST node, int token) {
      Expression expression = this.expression(node.getFirstChild());
      PrefixExpression prefixExpression = new PrefixExpression(makeToken(token, node), expression);
      this.configureAST(prefixExpression, node);
      return prefixExpression;
   }

   protected Expression postfixExpression(AST node, int token) {
      Expression expression = this.expression(node.getFirstChild());
      PostfixExpression postfixExpression = new PostfixExpression(expression, makeToken(token, node));
      this.configureAST(postfixExpression, node);
      return postfixExpression;
   }

   protected BooleanExpression booleanExpression(AST node) {
      BooleanExpression booleanExpression = new BooleanExpression(this.expression(node));
      this.configureAST(booleanExpression, node);
      return booleanExpression;
   }

   protected Expression dotExpression(AST node) {
      AST leftNode = node.getFirstChild();
      if (leftNode != null) {
         AST identifierNode = leftNode.getNextSibling();
         if (identifierNode != null) {
            Expression leftExpression = this.expression(leftNode);
            if (isType(51, identifierNode)) {
               Expression field = this.expression(identifierNode.getFirstChild(), true);
               AttributeExpression attributeExpression = new AttributeExpression(leftExpression, field, node.getType() != 87);
               if (node.getType() == 149) {
                  attributeExpression.setSpreadSafe(true);
               }

               this.configureAST(attributeExpression, node);
               return attributeExpression;
            }

            if (isType(7, identifierNode)) {
               Statement code = this.statementList(identifierNode);
               ClosureExpression closureExpression = new ClosureExpression(Parameter.EMPTY_ARRAY, code);
               this.configureAST(closureExpression, identifierNode);
               PropertyExpression propertyExpression = new PropertyExpression(leftExpression, closureExpression);
               if (node.getType() == 149) {
                  propertyExpression.setSpreadSafe(true);
               }

               this.configureAST(propertyExpression, node);
               return propertyExpression;
            }

            Expression property = this.expression(identifierNode, true);
            if (property instanceof VariableExpression) {
               VariableExpression ve = (VariableExpression)property;
               property = new ConstantExpression(ve.getName());
            }

            PropertyExpression propertyExpression = new PropertyExpression(leftExpression, (Expression)property, node.getType() != 87);
            if (node.getType() == 149) {
               propertyExpression.setSpreadSafe(true);
            }

            this.configureAST(propertyExpression, node);
            return propertyExpression;
         }
      }

      return this.methodCallExpression(node);
   }

   protected Expression specialConstructorCallExpression(AST methodCallNode, ClassNode special) {
      AST node = methodCallNode.getFirstChild();
      Expression arguments = this.arguments(node);
      ConstructorCallExpression expression = new ConstructorCallExpression(special, arguments);
      this.configureAST(expression, methodCallNode);
      return expression;
   }

   private int getTypeInParenthesis(AST node) {
      if (!isType(27, node)) {
         node = node.getFirstChild();
      }

      while(node != null && isType(27, node) && node.getNextSibling() == null) {
         node = node.getFirstChild();
      }

      return node == null ? -1 : node.getType();
   }

   protected Expression methodCallExpression(AST methodCallNode) {
      AST node = methodCallNode.getFirstChild();
      AST elist = node.getNextSibling();
      List<GenericsType> typeArgumentList = null;
      boolean implicitThis = false;
      boolean safe = isType(150, node);
      boolean spreadSafe = isType(149, node);
      AST name;
      Object objectExpression;
      AST selector;
      if (!isType(87, node) && !safe && !spreadSafe) {
         implicitThis = true;
         objectExpression = VariableExpression.THIS_EXPRESSION;
         selector = node;
      } else {
         name = node.getFirstChild();
         objectExpression = this.expression(name);
         selector = name.getNextSibling();
      }

      if (isType(69, selector)) {
         typeArgumentList = this.getTypeArgumentsList(selector);
         selector = selector.getNextSibling();
      }

      name = null;
      Expression arguments;
      Object name;
      if (isType(95, selector)) {
         implicitThis = true;
         name = new ConstantExpression("super");
         if (objectExpression instanceof VariableExpression && ((VariableExpression)objectExpression).isThisExpression()) {
            objectExpression = VariableExpression.SUPER_EXPRESSION;
         }
      } else {
         if (this.isPrimitiveTypeLiteral(selector)) {
            throw new ASTRuntimeException(selector, "Primitive type literal: " + selector.getText() + " cannot be used as a method name");
         }

         if (isType(51, selector)) {
            arguments = this.expression(selector.getFirstChild(), true);
            AttributeExpression attributeExpression = new AttributeExpression((Expression)objectExpression, arguments, node.getType() != 87);
            this.configureAST(attributeExpression, node);
            Expression arguments = this.arguments(elist);
            MethodCallExpression expression = new MethodCallExpression(attributeExpression, "call", arguments);
            this.setTypeArgumentsOnMethodCallExpression(expression, typeArgumentList);
            this.configureAST(expression, methodCallNode);
            return expression;
         }

         if (!isType(52, selector) && !isType(84, selector) && !isType(47, selector) && !isType(85, selector)) {
            implicitThis = false;
            name = new ConstantExpression("call");
            objectExpression = this.expression(selector, true);
         } else {
            name = this.expression(selector, true);
         }
      }

      if (!selector.getText().equals("this") && !selector.getText().equals("super")) {
         arguments = this.arguments(elist);
         MethodCallExpression expression = new MethodCallExpression((Expression)objectExpression, (Expression)name, arguments);
         expression.setSafe(safe);
         expression.setSpreadSafe(spreadSafe);
         expression.setImplicitThis(implicitThis);
         this.setTypeArgumentsOnMethodCallExpression(expression, typeArgumentList);
         Expression ret = expression;
         if (implicitThis && "this".equals(expression.getMethodAsString())) {
            ret = new ConstructorCallExpression(this.classNode, arguments);
         }

         this.configureAST((ASTNode)ret, methodCallNode);
         return (Expression)ret;
      } else {
         throw new ASTRuntimeException(elist, "Constructor call must be the first statement in a constructor.");
      }
   }

   private void setTypeArgumentsOnMethodCallExpression(MethodCallExpression expression, List<GenericsType> typeArgumentList) {
      if (typeArgumentList != null && typeArgumentList.size() > 0) {
         expression.setGenericsTypes((GenericsType[])typeArgumentList.toArray(new GenericsType[typeArgumentList.size()]));
      }

   }

   protected Expression constructorCallExpression(AST node) {
      AST constructorCallNode = node;
      ClassNode type = this.makeTypeWithArguments(node);
      if (isType(44, node) || isType(154, node)) {
         node = node.getFirstChild();
      }

      AST elist = node.getNextSibling();
      if (elist == null && isType(32, node)) {
         elist = node;
         if ("(".equals(type.getName())) {
            type = this.classNode;
         }
      }

      if (isType(16, elist)) {
         AST expressionNode = elist.getFirstChild();
         if (expressionNode == null) {
            throw new ASTRuntimeException(elist, "No expression for the array constructor call");
         } else {
            List size = this.arraySizeExpression(expressionNode);
            ArrayExpression arrayExpression = new ArrayExpression(type, (List)null, size);
            this.configureAST(arrayExpression, constructorCallNode);
            return arrayExpression;
         }
      } else {
         Expression arguments = this.arguments(elist);
         ClassNode innerClass = this.getAnonymousInnerClassNode(arguments);
         ConstructorCallExpression ret = new ConstructorCallExpression(type, arguments);
         if (innerClass != null) {
            ret.setType(innerClass);
            ret.setUsingAnonymousInnerClass(true);
            innerClass.setUnresolvedSuperClass(type);
         }

         this.configureAST(ret, constructorCallNode);
         return ret;
      }
   }

   private ClassNode getAnonymousInnerClassNode(Expression arguments) {
      if (arguments instanceof TupleExpression) {
         TupleExpression te = (TupleExpression)arguments;
         List<Expression> expressions = te.getExpressions();
         if (expressions.size() == 0) {
            return null;
         }

         Expression last = (Expression)expressions.remove(expressions.size() - 1);
         if (last instanceof AntlrParserPlugin.AnonymousInnerClassCarrier) {
            AntlrParserPlugin.AnonymousInnerClassCarrier carrier = (AntlrParserPlugin.AnonymousInnerClassCarrier)last;
            return carrier.innerClass;
         }

         expressions.add(last);
      } else if (arguments instanceof AntlrParserPlugin.AnonymousInnerClassCarrier) {
         AntlrParserPlugin.AnonymousInnerClassCarrier carrier = (AntlrParserPlugin.AnonymousInnerClassCarrier)arguments;
         return carrier.innerClass;
      }

      return null;
   }

   protected List arraySizeExpression(AST node) {
      Expression size = null;
      Object list;
      if (isType(16, node)) {
         AST right = node.getNextSibling();
         if (right != null) {
            size = this.expression(right);
         } else {
            size = ConstantExpression.EMPTY_EXPRESSION;
         }

         list = this.arraySizeExpression(node.getFirstChild());
      } else {
         size = this.expression(node);
         list = new ArrayList();
      }

      ((List)list).add(size);
      return (List)list;
   }

   protected Expression arguments(AST elist) {
      List expressionList = new ArrayList();
      boolean namedArguments = false;

      for(AST node = elist; node != null; node = node.getNextSibling()) {
         if (isType(32, node)) {
            for(AST child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
               namedArguments |= this.addArgumentExpression(child, expressionList);
            }
         } else {
            namedArguments |= this.addArgumentExpression(node, expressionList);
         }
      }

      if (!namedArguments) {
         ArgumentListExpression argumentListExpression = new ArgumentListExpression(expressionList);
         this.configureAST(argumentListExpression, elist);
         return argumentListExpression;
      } else {
         if (!expressionList.isEmpty()) {
            List argumentList = new ArrayList();
            Iterator iter = expressionList.iterator();

            while(iter.hasNext()) {
               Expression expression = (Expression)iter.next();
               if (!(expression instanceof MapEntryExpression)) {
                  argumentList.add(expression);
               }
            }

            if (!argumentList.isEmpty()) {
               expressionList.removeAll(argumentList);
               this.checkDuplicateNamedParams(elist, expressionList);
               MapExpression mapExpression = new MapExpression(expressionList);
               this.configureAST(mapExpression, elist);
               argumentList.add(0, mapExpression);
               ArgumentListExpression argumentListExpression = new ArgumentListExpression(argumentList);
               this.configureAST(argumentListExpression, elist);
               return argumentListExpression;
            }
         }

         this.checkDuplicateNamedParams(elist, expressionList);
         NamedArgumentListExpression namedArgumentListExpression = new NamedArgumentListExpression(expressionList);
         this.configureAST(namedArgumentListExpression, elist);
         return namedArgumentListExpression;
      }
   }

   private void checkDuplicateNamedParams(AST elist, List expressionList) {
      if (!expressionList.isEmpty()) {
         Set<String> namedArgumentNames = new HashSet();
         Iterator iter = expressionList.iterator();

         while(iter.hasNext()) {
            MapEntryExpression meExp = (MapEntryExpression)iter.next();
            if (meExp.getKeyExpression() instanceof ConstantExpression) {
               String argName = ((ConstantExpression)meExp.getKeyExpression()).getText();
               if (namedArgumentNames.contains(argName)) {
                  throw new ASTRuntimeException(elist, "Duplicate named parameter '" + argName + "' found.");
               }

               namedArgumentNames.add(argName);
            }
         }

      }
   }

   protected boolean addArgumentExpression(AST node, List expressionList) {
      if (node.getType() == 55) {
         AST rightNode = node.getFirstChild();
         Expression keyExpression = this.spreadMapExpression(node);
         Expression rightExpression = this.expression(rightNode);
         MapEntryExpression mapEntryExpression = new MapEntryExpression(keyExpression, rightExpression);
         expressionList.add(mapEntryExpression);
         return true;
      } else {
         Expression expression = this.expression(node);
         expressionList.add(expression);
         return expression instanceof MapEntryExpression;
      }
   }

   protected Expression expressionList(AST node) {
      List expressionList = new ArrayList();

      for(AST child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
         expressionList.add(this.expression(child));
      }

      if (expressionList.size() == 1) {
         return (Expression)expressionList.get(0);
      } else {
         ListExpression listExpression = new ListExpression(expressionList);
         listExpression.setWrapped(true);
         this.configureAST(listExpression, node);
         return listExpression;
      }
   }

   protected ClosureExpression closureExpression(AST node) {
      AST paramNode = node.getFirstChild();
      Parameter[] parameters = null;
      AST codeNode = paramNode;
      if (isType(19, paramNode) || isType(50, paramNode)) {
         parameters = this.parameters(paramNode);
         codeNode = paramNode.getNextSibling();
      }

      Statement code = this.statementListNoChild(codeNode, node);
      ClosureExpression closureExpression = new ClosureExpression(parameters, code);
      this.configureAST(closureExpression, node);
      return closureExpression;
   }

   protected Expression blockExpression(AST node) {
      AST codeNode = node.getFirstChild();
      if (codeNode == null) {
         return ConstantExpression.NULL;
      } else if (codeNode.getType() == 27 && codeNode.getNextSibling() == null) {
         return this.expression(codeNode);
      } else {
         Parameter[] parameters = Parameter.EMPTY_ARRAY;
         Statement code = this.statementListNoChild(codeNode, node);
         ClosureExpression closureExpression = new ClosureExpression(parameters, code);
         this.configureAST(closureExpression, node);
         String callName = "call";
         Expression noArguments = new ArgumentListExpression();
         MethodCallExpression call = new MethodCallExpression(closureExpression, callName, noArguments);
         this.configureAST(call, node);
         return call;
      }
   }

   protected Expression unaryMinusExpression(AST unaryMinusExpr) {
      AST node = unaryMinusExpr.getFirstChild();
      String text = node.getText();
      switch(node.getType()) {
      case 195:
      case 197:
      case 199:
         ConstantExpression constantLongExpression = new ConstantExpression(Numbers.parseInteger("-" + text));
         this.configureAST(constantLongExpression, unaryMinusExpr);
         return constantLongExpression;
      case 196:
      case 198:
      case 200:
         ConstantExpression constantExpression = new ConstantExpression(Numbers.parseDecimal("-" + text));
         this.configureAST(constantExpression, unaryMinusExpr);
         return constantExpression;
      default:
         UnaryMinusExpression unaryMinusExpression = new UnaryMinusExpression(this.expression(node));
         this.configureAST(unaryMinusExpression, unaryMinusExpr);
         return unaryMinusExpression;
      }
   }

   protected Expression unaryPlusExpression(AST unaryPlusExpr) {
      AST node = unaryPlusExpr.getFirstChild();
      switch(node.getType()) {
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         return this.expression(node);
      default:
         UnaryPlusExpression unaryPlusExpression = new UnaryPlusExpression(this.expression(node));
         this.configureAST(unaryPlusExpression, unaryPlusExpr);
         return unaryPlusExpression;
      }
   }

   protected ConstantExpression decimalExpression(AST node) {
      String text = node.getText();
      ConstantExpression constantExpression = new ConstantExpression(Numbers.parseDecimal(text));
      this.configureAST(constantExpression, node);
      return constantExpression;
   }

   protected ConstantExpression integerExpression(AST node) {
      String text = node.getText();
      ConstantExpression constantExpression = new ConstantExpression(Numbers.parseInteger(text));
      this.configureAST(constantExpression, node);
      return constantExpression;
   }

   protected Expression gstring(AST gstringNode) {
      List strings = new ArrayList();
      List values = new ArrayList();
      StringBuffer buffer = new StringBuffer();
      boolean isPrevString = false;

      for(AST node = gstringNode.getFirstChild(); node != null; node = node.getNextSibling()) {
         int type = node.getType();
         String text = null;
         switch(type) {
         case 85:
            if (isPrevString) {
               this.assertNodeType(84, node);
            }

            isPrevString = true;
            text = node.getText();
            ConstantExpression constantExpression = new ConstantExpression(text);
            this.configureAST(constantExpression, node);
            strings.add(constantExpression);
            buffer.append(text);
            break;
         default:
            if (!isPrevString) {
               this.assertNodeType(84, node);
            }

            isPrevString = false;
            Expression expression = this.expression(node);
            values.add(expression);
            buffer.append("$");
            buffer.append(expression.getText());
         }
      }

      GStringExpression gStringExpression = new GStringExpression(buffer.toString(), strings, values);
      this.configureAST(gStringExpression, gstringNode);
      return gStringExpression;
   }

   protected ClassNode type(AST typeNode) {
      return this.buildName(typeNode.getFirstChild());
   }

   public static String qualifiedName(AST qualifiedNameNode) {
      if (isType(84, qualifiedNameNode)) {
         return qualifiedNameNode.getText();
      } else if (!isType(87, qualifiedNameNode)) {
         return qualifiedNameNode.getText();
      } else {
         AST node = qualifiedNameNode.getFirstChild();
         StringBuffer buffer = new StringBuffer();

         for(boolean first = true; node != null && !isType(69, node); node = node.getNextSibling()) {
            if (first) {
               first = false;
            } else {
               buffer.append(".");
            }

            buffer.append(qualifiedName(node));
         }

         return buffer.toString();
      }
   }

   private static AST getTypeArgumentsNode(AST root) {
      while(root != null && !isType(69, root)) {
         root = root.getNextSibling();
      }

      return root;
   }

   private int getBoundType(AST node) {
      if (node == null) {
         return -1;
      } else if (isType(74, node)) {
         return 74;
      } else if (isType(75, node)) {
         return 75;
      } else {
         throw new ASTRuntimeException(node, "Unexpected node type: " + this.getTokenName(node) + " found when expecting type: " + this.getTokenName(74) + " or type: " + this.getTokenName(75));
      }
   }

   private GenericsType makeGenericsArgumentType(AST typeArgument) {
      AST rootNode = typeArgument.getFirstChild();
      ClassNode base;
      GenericsType gt;
      if (isType(73, rootNode)) {
         base = ClassHelper.makeWithoutCaching("?");
         if (rootNode.getNextSibling() != null) {
            int boundType = this.getBoundType(rootNode.getNextSibling());
            ClassNode[] gts = this.makeGenericsBounds(rootNode, boundType);
            if (boundType == 74) {
               gt = new GenericsType(base, gts, (ClassNode)null);
            } else {
               gt = new GenericsType(base, (ClassNode[])null, gts[0]);
            }
         } else {
            gt = new GenericsType(base, (ClassNode[])null, (ClassNode)null);
         }

         gt.setName("?");
         gt.setWildcard(true);
      } else {
         base = this.makeTypeWithArguments(rootNode);
         gt = new GenericsType(base);
      }

      this.configureAST(gt, typeArgument);
      return gt;
   }

   protected ClassNode makeTypeWithArguments(AST rootNode) {
      ClassNode basicType = this.makeType(rootNode);
      AST node = rootNode.getFirstChild();
      if (node != null && !isType(23, node) && !isType(16, node)) {
         if (!isType(87, node)) {
            node = node.getFirstChild();
            return node == null ? basicType : this.addTypeArguments(basicType, node);
         } else {
            for(node = node.getFirstChild(); node != null && !isType(69, node); node = node.getNextSibling()) {
            }

            return node == null ? basicType : this.addTypeArguments(basicType, node);
         }
      } else {
         return basicType;
      }
   }

   private ClassNode addTypeArguments(ClassNode basicType, AST node) {
      List<GenericsType> typeArgumentList = this.getTypeArgumentsList(node);
      if (typeArgumentList.size() > 0) {
         basicType.setGenericsTypes((GenericsType[])typeArgumentList.toArray(new GenericsType[typeArgumentList.size()]));
      }

      return basicType;
   }

   private List<GenericsType> getTypeArgumentsList(AST node) {
      this.assertNodeType(69, node);
      List<GenericsType> typeArgumentList = new LinkedList();

      for(AST typeArgument = node.getFirstChild(); typeArgument != null; typeArgument = typeArgument.getNextSibling()) {
         this.assertNodeType(70, typeArgument);
         GenericsType gt = this.makeGenericsArgumentType(typeArgument);
         typeArgumentList.add(gt);
      }

      return typeArgumentList;
   }

   private ClassNode[] makeGenericsBounds(AST rn, int boundType) {
      AST boundsRoot = rn.getNextSibling();
      if (boundsRoot == null) {
         return null;
      } else {
         this.assertNodeType(boundType, boundsRoot);
         LinkedList bounds = new LinkedList();

         for(AST boundsNode = boundsRoot.getFirstChild(); boundsNode != null; boundsNode = boundsNode.getNextSibling()) {
            ClassNode bound = null;
            bound = this.makeTypeWithArguments(boundsNode);
            this.configureAST(bound, boundsNode);
            bounds.add(bound);
         }

         return bounds.size() == 0 ? null : (ClassNode[])((ClassNode[])bounds.toArray(new ClassNode[bounds.size()]));
      }
   }

   protected GenericsType[] makeGenericsType(AST rootNode) {
      AST typeParameter = rootNode.getFirstChild();
      LinkedList ret = new LinkedList();
      this.assertNodeType(72, typeParameter);

      while(isType(72, typeParameter)) {
         AST typeNode = typeParameter.getFirstChild();
         ClassNode type = this.makeType(typeParameter);
         GenericsType gt = new GenericsType(type, this.makeGenericsBounds(typeNode, 74), (ClassNode)null);
         this.configureAST(gt, typeParameter);
         ret.add(gt);
         typeParameter = typeParameter.getNextSibling();
      }

      return (GenericsType[])((GenericsType[])ret.toArray(new GenericsType[0]));
   }

   protected ClassNode makeType(AST typeNode) {
      ClassNode answer = ClassHelper.DYNAMIC_TYPE;
      AST node = typeNode.getFirstChild();
      if (node != null) {
         if (!isType(23, node) && !isType(16, node)) {
            answer = ClassHelper.make(qualifiedName(node));
            if (answer.isUsingGenerics()) {
               ClassNode newAnswer = ClassHelper.makeWithoutCaching(answer.getName());
               newAnswer.setRedirect(answer);
               answer = newAnswer;
            }
         } else {
            answer = this.makeType(node).makeArray();
         }

         this.configureAST(answer, node);
      }

      return answer;
   }

   protected ClassNode buildName(AST node) {
      if (isType(12, node)) {
         node = node.getFirstChild();
      }

      ClassNode answer = null;
      AST child;
      if (!isType(87, node) && !isType(150, node)) {
         if (this.isPrimitiveTypeLiteral(node)) {
            answer = ClassHelper.make(node.getText());
         } else {
            if (isType(23, node) || isType(16, node)) {
               child = node.getFirstChild();
               answer = this.buildName(child).makeArray();
               this.configureAST(answer, node);
               return answer;
            }

            String identifier = node.getText();
            answer = ClassHelper.make(identifier);
         }
      } else {
         answer = ClassHelper.make(qualifiedName(node));
      }

      child = node.getNextSibling();
      if (!isType(23, child) && !isType(16, node)) {
         this.configureAST(answer, node);
         return answer;
      } else {
         answer = answer.makeArray();
         this.configureAST(answer, node);
         return answer;
      }
   }

   protected boolean isPrimitiveTypeLiteral(AST node) {
      int type = node.getType();
      switch(type) {
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         return true;
      default:
         return false;
      }
   }

   protected String identifier(AST node) {
      this.assertNodeType(84, node);
      return node.getText();
   }

   protected String label(AST labelNode) {
      AST node = labelNode.getFirstChild();
      return node == null ? null : this.identifier(node);
   }

   protected boolean hasVisibility(int modifiers) {
      return (modifiers & 7) != 0;
   }

   protected void configureAST(ASTNode node, AST ast) {
      if (ast == null) {
         throw new ASTRuntimeException(ast, "PARSER BUG: Tried to configure " + node.getClass().getName() + " with null Node");
      } else {
         node.setColumnNumber(ast.getColumn());
         node.setLineNumber(ast.getLine());
         if (ast instanceof GroovySourceAST) {
            node.setLastColumnNumber(((GroovySourceAST)ast).getColumnLast());
            node.setLastLineNumber(((GroovySourceAST)ast).getLineLast());
         }

      }
   }

   protected static Token makeToken(int typeCode, AST node) {
      return Token.newSymbol(typeCode, node.getLine(), node.getColumn());
   }

   protected String getFirstChildText(AST node) {
      AST child = node.getFirstChild();
      return child != null ? child.getText() : null;
   }

   public static boolean isType(int typeCode, AST node) {
      return node != null && node.getType() == typeCode;
   }

   private String getTokenName(int token) {
      return this.tokenNames == null ? "" + token : this.tokenNames[token];
   }

   private String getTokenName(AST node) {
      return node == null ? "null" : this.getTokenName(node.getType());
   }

   protected void assertNodeType(int type, AST node) {
      if (node == null) {
         throw new ASTRuntimeException(node, "No child node available in AST when expecting type: " + this.getTokenName(type));
      } else if (node.getType() != type) {
         throw new ASTRuntimeException(node, "Unexpected node type: " + this.getTokenName(node) + " found when expecting type: " + this.getTokenName(type));
      }
   }

   protected void notImplementedYet(AST node) {
      throw new ASTRuntimeException(node, "AST node not implemented yet for type: " + this.getTokenName(node));
   }

   protected void unknownAST(AST node) {
      if (node.getType() == 13) {
         throw new ASTRuntimeException(node, "Class definition not expected here. Perhaps try using a closure instead.");
      } else {
         throw new ASTRuntimeException(node, "Unknown type: " + this.getTokenName(node));
      }
   }

   protected void dumpTree(AST ast) {
      for(AST node = ast.getFirstChild(); node != null; node = node.getNextSibling()) {
         this.dump(node);
      }

   }

   protected void dump(AST node) {
      System.out.println("Type: " + this.getTokenName(node) + " text: " + node.getText());
   }

   private static class AnonymousInnerClassCarrier extends Expression {
      ClassNode innerClass;

      private AnonymousInnerClassCarrier() {
      }

      public Expression transformExpression(ExpressionTransformer transformer) {
         return null;
      }

      // $FF: synthetic method
      AnonymousInnerClassCarrier(Object x0) {
         this();
      }
   }
}
