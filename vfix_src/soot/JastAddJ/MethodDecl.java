package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.Scene;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.coffi.CoffiMethodSource;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.tagkit.AnnotationTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;

public class MethodDecl extends MemberDecl implements Cloneable, SimpleSet, Iterator {
   private MethodDecl iterElem;
   public SootMethod sootMethod;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected Map accessibleFrom_TypeDecl_values;
   protected Map throwsException_TypeDecl_values;
   protected boolean signature_computed = false;
   protected String signature_value;
   protected Map moreSpecificThan_MethodDecl_values;
   protected Map overrides_MethodDecl_values;
   protected Map hides_MethodDecl_values;
   protected Map parameterDeclaration_String_values;
   protected boolean type_computed = false;
   protected TypeDecl type_value;
   protected boolean usesTypeVariable_computed = false;
   protected boolean usesTypeVariable_value;
   protected boolean sourceMethodDecl_computed = false;
   protected MethodDecl sourceMethodDecl_value;
   protected boolean sootMethod_computed = false;
   protected SootMethod sootMethod_value;
   protected boolean sootRef_computed = false;
   protected SootMethodRef sootRef_value;
   protected boolean offsetBeforeParameters_computed = false;
   protected int offsetBeforeParameters_value;
   protected boolean offsetAfterParameters_computed = false;
   protected int offsetAfterParameters_value;
   protected Map handlesException_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.accessibleFrom_TypeDecl_values = null;
      this.throwsException_TypeDecl_values = null;
      this.signature_computed = false;
      this.signature_value = null;
      this.moreSpecificThan_MethodDecl_values = null;
      this.overrides_MethodDecl_values = null;
      this.hides_MethodDecl_values = null;
      this.parameterDeclaration_String_values = null;
      this.type_computed = false;
      this.type_value = null;
      this.usesTypeVariable_computed = false;
      this.sourceMethodDecl_computed = false;
      this.sourceMethodDecl_value = null;
      this.sootMethod_computed = false;
      this.sootMethod_value = null;
      this.sootRef_computed = false;
      this.sootRef_value = null;
      this.offsetBeforeParameters_computed = false;
      this.offsetAfterParameters_computed = false;
      this.handlesException_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public MethodDecl clone() throws CloneNotSupportedException {
      MethodDecl node = (MethodDecl)super.clone();
      node.accessibleFrom_TypeDecl_values = null;
      node.throwsException_TypeDecl_values = null;
      node.signature_computed = false;
      node.signature_value = null;
      node.moreSpecificThan_MethodDecl_values = null;
      node.overrides_MethodDecl_values = null;
      node.hides_MethodDecl_values = null;
      node.parameterDeclaration_String_values = null;
      node.type_computed = false;
      node.type_value = null;
      node.usesTypeVariable_computed = false;
      node.sourceMethodDecl_computed = false;
      node.sourceMethodDecl_value = null;
      node.sootMethod_computed = false;
      node.sootMethod_value = null;
      node.sootRef_computed = false;
      node.sootRef_value = null;
      node.offsetBeforeParameters_computed = false;
      node.offsetAfterParameters_computed = false;
      node.handlesException_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public MethodDecl copy() {
      try {
         MethodDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public MethodDecl fullCopy() {
      MethodDecl tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            ASTNode child = this.children[i];
            if (child != null) {
               child = child.fullCopy();
               tree.setChild(child, i);
            }
         }
      }

      return tree;
   }

   public Access createBoundAccess(List args) {
      return (Access)(this.isStatic() ? this.hostType().createQualifiedAccess().qualifiesAccess(new BoundMethodAccess(this.name(), args, this)) : new BoundMethodAccess(this.name(), args, this));
   }

   public SimpleSet add(Object o) {
      return (new SimpleSet.SimpleSetImpl()).add(this).add(o);
   }

   public boolean isSingleton() {
      return true;
   }

   public boolean isSingleton(Object o) {
      return this.contains(o);
   }

   public Iterator iterator() {
      this.iterElem = this;
      return this;
   }

   public boolean hasNext() {
      return this.iterElem != null;
   }

   public Object next() {
      Object o = this.iterElem;
      this.iterElem = null;
      return o;
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public void nameCheck() {
      if (!this.hostType().methodsSignature(this.signature()).contains(this)) {
         this.error("method with signature " + this.signature() + " is multiply declared in type " + this.hostType().typeName());
      }

      if (this.isNative() && this.hasBlock()) {
         this.error("native methods must have an empty semicolon body");
      }

      if (this.isAbstract() && this.hasBlock()) {
         this.error("abstract methods must have an empty semicolon body");
      }

      if (!this.hasBlock() && !this.isNative() && !this.isAbstract()) {
         this.error("only abstract and native methods may have an empty semicolon body");
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getModifiers().toString(s);
      this.getTypeAccess().toString(s);
      s.append(" " + this.name() + "(");
      int i;
      if (this.getNumParameter() > 0) {
         this.getParameter(0).toString(s);

         for(i = 1; i < this.getNumParameter(); ++i) {
            s.append(", ");
            this.getParameter(i).toString(s);
         }
      }

      s.append(")");
      if (this.getNumException() > 0) {
         s.append(" throws ");
         this.getException(0).toString(s);

         for(i = 1; i < this.getNumException(); ++i) {
            s.append(", ");
            this.getException(i).toString(s);
         }
      }

      if (this.hasBlock()) {
         s.append(" ");
         this.getBlock().toString(s);
      } else {
         s.append(";");
      }

   }

   public void typeCheck() {
      TypeDecl exceptionType = this.typeThrowable();

      for(int i = 0; i < this.getNumException(); ++i) {
         TypeDecl typeDecl = this.getException(i).type();
         if (!typeDecl.instanceOf(exceptionType)) {
            this.error(this.signature() + " throws non throwable type " + typeDecl.fullName());
         }
      }

      if (!this.isVoid() && this.hasBlock() && this.getBlock().canCompleteNormally()) {
         this.error("the body of a non void method may not complete normally");
      }

   }

   public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
      MethodDecl m = new MethodDeclSubstituted(this.getModifiers().fullCopy(), this.getTypeAccess().type().substituteReturnType(parTypeDecl), this.getID(), this.getParameterList().substitute(parTypeDecl), this.getExceptionList().substitute(parTypeDecl), this.substituteBody(parTypeDecl), this);
      return m;
   }

   public Opt substituteBody(Parameterization parTypeDecl) {
      return new Opt();
   }

   public MethodDecl createAccessor(TypeDecl methodQualifier) {
      MethodDecl m = (MethodDecl)methodQualifier.getAccessor(this, "method");
      if (m != null) {
         return m;
      } else {
         int accessorIndex = methodQualifier.accessorCounter++;
         List parameterList = new List();

         for(int i = 0; i < this.getNumParameter(); ++i) {
            parameterList.add(new ParameterDeclaration(this.getParameter(i).type().createQualifiedAccess(), this.getParameter(i).name()));
         }

         List exceptionList = new List();

         for(int i = 0; i < this.getNumException(); ++i) {
            exceptionList.add((Access)this.getException(i).fullCopy());
         }

         Modifiers modifiers = new Modifiers(new List());
         if (this.getModifiers().isStatic()) {
            modifiers.addModifier(new Modifier("static"));
         }

         modifiers.addModifier(new Modifier("synthetic"));
         modifiers.addModifier(new Modifier("public"));
         m = new MethodDecl(modifiers, this.getTypeAccess().type().createQualifiedAccess(), this.name() + "$access$" + accessorIndex, parameterList, exceptionList, new Opt(new Block((new List()).add(this.createAccessorStmt()))));
         m = methodQualifier.addMemberMethod(m);
         methodQualifier.addAccessor(this, "method", m);
         return m;
      }
   }

   private Stmt createAccessorStmt() {
      List argumentList = new List();

      for(int i = 0; i < this.getNumParameter(); ++i) {
         argumentList.add(new VarAccess(this.getParameter(i).name()));
      }

      Access access = new BoundMethodAccess(this.name(), argumentList, this);
      if (!this.isStatic()) {
         access = (new ThisAccess("this")).qualifiesAccess((Access)access);
      }

      return (Stmt)(this.isVoid() ? new ExprStmt((Expr)access) : new ReturnStmt(new Opt((ASTNode)access)));
   }

   public MethodDecl createSuperAccessor(TypeDecl methodQualifier) {
      MethodDecl m = (MethodDecl)methodQualifier.getAccessor(this, "method_super");
      if (m != null) {
         return m;
      } else {
         int accessorIndex = methodQualifier.accessorCounter++;
         List parameters = new List();
         List args = new List();

         for(int i = 0; i < this.getNumParameter(); ++i) {
            parameters.add(new ParameterDeclaration(this.getParameter(i).type(), this.getParameter(i).name()));
            args.add(new VarAccess(this.getParameter(i).name()));
         }

         Object stmt;
         if (this.type().isVoid()) {
            stmt = new ExprStmt((new SuperAccess("super")).qualifiesAccess(new MethodAccess(this.name(), args)));
         } else {
            stmt = new ReturnStmt(new Opt((new SuperAccess("super")).qualifiesAccess(new MethodAccess(this.name(), args))));
         }

         m = new MethodDecl(new Modifiers((new List()).add(new Modifier("synthetic"))), this.type().createQualifiedAccess(), this.name() + "$access$" + accessorIndex, parameters, new List(), new Opt(new Block((new List()).add((ASTNode)stmt))));
         m = methodQualifier.addMemberMethod(m);
         methodQualifier.addAccessor(this, "method_super", m);
         return m;
      }
   }

   public void jimplify1phase2() {
      String name = this.name();
      ArrayList parameters = new ArrayList();
      ArrayList paramnames = new ArrayList();

      for(int i = 0; i < this.getNumParameter(); ++i) {
         parameters.add(this.getParameter(i).type().getSootType());
         paramnames.add(this.getParameter(i).name());
      }

      Type returnType = this.type().getSootType();
      int modifiers = this.sootTypeModifiers();
      ArrayList throwtypes = new ArrayList();

      for(int i = 0; i < this.getNumException(); ++i) {
         throwtypes.add(this.getException(i).type().getSootClassDecl());
      }

      String signature = SootMethod.getSubSignature(name, parameters, returnType);
      if (!this.hostType().getSootClassDecl().declaresMethod(signature)) {
         SootMethod m = Scene.v().makeSootMethod(name, parameters, returnType, modifiers, throwtypes);
         this.hostType().getSootClassDecl().addMethod(m);
         m.addTag(new ParamNamesTag(paramnames));
         this.sootMethod = m;
      } else {
         this.sootMethod = this.hostType().getSootClassDecl().getMethod(signature);
      }

      this.addAttributes();
   }

   public void addAttributes() {
      super.addAttributes();
      ArrayList c = new ArrayList();
      this.getModifiers().addRuntimeVisibleAnnotationsAttribute(c);
      this.getModifiers().addRuntimeInvisibleAnnotationsAttribute(c);
      this.addRuntimeVisibleParameterAnnotationsAttribute(c);
      this.addRuntimeInvisibleParameterAnnotationsAttribute(c);
      this.addSourceLevelParameterAnnotationsAttribute(c);
      this.getModifiers().addSourceOnlyAnnotations(c);
      Iterator iter = c.iterator();

      while(iter.hasNext()) {
         Tag tag = (Tag)iter.next();
         this.sootMethod.addTag(tag);
      }

   }

   public void addRuntimeVisibleParameterAnnotationsAttribute(Collection c) {
      boolean foundVisibleAnnotations = false;
      Collection annotations = new ArrayList(this.getNumParameter());

      for(int i = 0; i < this.getNumParameter(); ++i) {
         Collection a = this.getParameter(i).getModifiers().runtimeVisibleAnnotations();
         if (!a.isEmpty()) {
            foundVisibleAnnotations = true;
         }

         VisibilityAnnotationTag tag = new VisibilityAnnotationTag(0);
         Iterator iter = a.iterator();

         while(iter.hasNext()) {
            Annotation annotation = (Annotation)iter.next();
            ArrayList elements = new ArrayList(1);
            annotation.appendAsAttributeTo(elements);
            tag.addAnnotation((AnnotationTag)elements.get(0));
         }

         annotations.add(tag);
      }

      if (foundVisibleAnnotations) {
         VisibilityParameterAnnotationTag tag = new VisibilityParameterAnnotationTag(annotations.size(), 0);
         Iterator iter = annotations.iterator();

         while(iter.hasNext()) {
            tag.addVisibilityAnnotation((VisibilityAnnotationTag)iter.next());
         }

         c.add(tag);
      }

   }

   public void addRuntimeInvisibleParameterAnnotationsAttribute(Collection c) {
      boolean foundVisibleAnnotations = false;
      Collection annotations = new ArrayList(this.getNumParameter());

      for(int i = 0; i < this.getNumParameter(); ++i) {
         Collection a = this.getParameter(i).getModifiers().runtimeInvisibleAnnotations();
         if (!a.isEmpty()) {
            foundVisibleAnnotations = true;
         }

         VisibilityAnnotationTag tag = new VisibilityAnnotationTag(1);
         Iterator iter = a.iterator();

         while(iter.hasNext()) {
            Annotation annotation = (Annotation)iter.next();
            ArrayList elements = new ArrayList(1);
            annotation.appendAsAttributeTo(elements);
            tag.addAnnotation((AnnotationTag)elements.get(0));
         }

         annotations.add(tag);
      }

      if (foundVisibleAnnotations) {
         VisibilityParameterAnnotationTag tag = new VisibilityParameterAnnotationTag(annotations.size(), 1);
         Iterator iter = annotations.iterator();

         while(iter.hasNext()) {
            tag.addVisibilityAnnotation((VisibilityAnnotationTag)iter.next());
         }

         c.add(tag);
      }

   }

   public void addSourceLevelParameterAnnotationsAttribute(Collection c) {
      boolean foundVisibleAnnotations = false;
      new ArrayList(this.getNumParameter());

      for(int i = 0; i < this.getNumParameter(); ++i) {
         this.getParameter(i).getModifiers().addSourceOnlyAnnotations(c);
      }

   }

   public void transformation() {
      super.transformation();
      HashSet processed = new HashSet();
      Iterator iter = this.hostType().bridgeCandidates(this.signature()).iterator();

      while(true) {
         MethodDecl erased;
         String key;
         do {
            do {
               MethodDecl m;
               do {
                  if (!iter.hasNext()) {
                     return;
                  }

                  m = (MethodDecl)iter.next();
               } while(!this.overrides(m));

               erased = m.erasedMethod();
            } while(erased.signature().equals(this.signature()) && erased.type().erasure() == this.type().erasure());

            StringBuffer keyBuffer = new StringBuffer();

            for(int i = 0; i < this.getNumParameter(); ++i) {
               keyBuffer.append(erased.getParameter(i).type().erasure().fullName());
            }

            keyBuffer.append(erased.type().erasure().fullName());
            key = keyBuffer.toString();
         } while(processed.contains(key));

         processed.add(key);
         List args = new List();
         List parameters = new List();

         for(int i = 0; i < this.getNumParameter(); ++i) {
            args.add(new CastExpr(this.getParameter(i).type().erasure().createBoundAccess(), new VarAccess("p" + i)));
            parameters.add(new ParameterDeclaration(erased.getParameter(i).type().erasure(), "p" + i));
         }

         Object stmt;
         if (this.type().isVoid()) {
            stmt = new ExprStmt(this.createBoundAccess(args));
         } else {
            stmt = new ReturnStmt(this.createBoundAccess(args));
         }

         List modifiersList = new List();
         if (this.isPublic()) {
            modifiersList.add(new Modifier("public"));
         } else if (this.isProtected()) {
            modifiersList.add(new Modifier("protected"));
         } else if (this.isPrivate()) {
            modifiersList.add(new Modifier("private"));
         }

         MethodDecl bridge = new BridgeMethodDecl(new Modifiers(modifiersList), erased.type().erasure().createBoundAccess(), erased.name(), parameters, this.getExceptionList().fullCopy(), new Opt(new Block((new List()).add((ASTNode)stmt))));
         this.hostType().addBodyDecl(bridge);
      }
   }

   public void checkWarnings() {
      super.checkWarnings();
      if (!this.suppressWarnings("unchecked") && !this.hasAnnotationSafeVarargs() && this.isVariableArity() && !this.getParameter(this.getNumParameter() - 1).type().isReifiable()) {
         this.warning("possible heap pollution for variable arity parameter");
      }

   }

   public MethodDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[5];
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
      this.setChild(new Opt(), 4);
   }

   public MethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
   }

   public MethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
   }

   protected int numChildren() {
      return 5;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setModifiers(Modifiers node) {
      this.setChild(node, 0);
   }

   public Modifiers getModifiers() {
      return (Modifiers)this.getChild(0);
   }

   public Modifiers getModifiersNoTransform() {
      return (Modifiers)this.getChildNoTransform(0);
   }

   public void setTypeAccess(Access node) {
      this.setChild(node, 1);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(1);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(1);
   }

   public void setID(String value) {
      this.tokenString_ID = value;
   }

   public void setID(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setID is only valid for String lexemes");
      } else {
         this.tokenString_ID = (String)symbol.value;
         this.IDstart = symbol.getStart();
         this.IDend = symbol.getEnd();
      }
   }

   public String getID() {
      return this.tokenString_ID != null ? this.tokenString_ID : "";
   }

   public void setParameterList(List<ParameterDeclaration> list) {
      this.setChild(list, 2);
   }

   public int getNumParameter() {
      return this.getParameterList().getNumChild();
   }

   public int getNumParameterNoTransform() {
      return this.getParameterListNoTransform().getNumChildNoTransform();
   }

   public ParameterDeclaration getParameter(int i) {
      return (ParameterDeclaration)this.getParameterList().getChild(i);
   }

   public void addParameter(ParameterDeclaration node) {
      List<ParameterDeclaration> list = this.parent != null && state != null ? this.getParameterList() : this.getParameterListNoTransform();
      list.addChild(node);
   }

   public void addParameterNoTransform(ParameterDeclaration node) {
      List<ParameterDeclaration> list = this.getParameterListNoTransform();
      list.addChild(node);
   }

   public void setParameter(ParameterDeclaration node, int i) {
      List<ParameterDeclaration> list = this.getParameterList();
      list.setChild(node, i);
   }

   public List<ParameterDeclaration> getParameters() {
      return this.getParameterList();
   }

   public List<ParameterDeclaration> getParametersNoTransform() {
      return this.getParameterListNoTransform();
   }

   public List<ParameterDeclaration> getParameterList() {
      List<ParameterDeclaration> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<ParameterDeclaration> getParameterListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setExceptionList(List<Access> list) {
      this.setChild(list, 3);
   }

   public int getNumException() {
      return this.getExceptionList().getNumChild();
   }

   public int getNumExceptionNoTransform() {
      return this.getExceptionListNoTransform().getNumChildNoTransform();
   }

   public Access getException(int i) {
      return (Access)this.getExceptionList().getChild(i);
   }

   public void addException(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getExceptionList() : this.getExceptionListNoTransform();
      list.addChild(node);
   }

   public void addExceptionNoTransform(Access node) {
      List<Access> list = this.getExceptionListNoTransform();
      list.addChild(node);
   }

   public void setException(Access node, int i) {
      List<Access> list = this.getExceptionList();
      list.setChild(node, i);
   }

   public List<Access> getExceptions() {
      return this.getExceptionList();
   }

   public List<Access> getExceptionsNoTransform() {
      return this.getExceptionListNoTransform();
   }

   public List<Access> getExceptionList() {
      List<Access> list = (List)this.getChild(3);
      list.getNumChild();
      return list;
   }

   public List<Access> getExceptionListNoTransform() {
      return (List)this.getChildNoTransform(3);
   }

   public void setBlockOpt(Opt<Block> opt) {
      this.setChild(opt, 4);
   }

   public boolean hasBlock() {
      return this.getBlockOpt().getNumChild() != 0;
   }

   public Block getBlock() {
      return (Block)this.getBlockOpt().getChild(0);
   }

   public void setBlock(Block node) {
      this.getBlockOpt().setChild(node, 0);
   }

   public Opt<Block> getBlockOpt() {
      return (Opt)this.getChild(4);
   }

   public Opt<Block> getBlockOptNoTransform() {
      return (Opt)this.getChildNoTransform(4);
   }

   public void checkModifiers() {
      super.checkModifiers();
      if (this.hostType().isClassDecl()) {
         if (!this.hostType().isEnumDecl() && this.isAbstract() && !this.hostType().isAbstract()) {
            this.error("class must be abstract to include abstract methods");
         }

         if (this.isAbstract() && this.isPrivate()) {
            this.error("method may not be abstract and private");
         }

         if (this.isAbstract() && this.isStatic()) {
            this.error("method may not be abstract and static");
         }

         if (this.isAbstract() && this.isSynchronized()) {
            this.error("method may not be abstract and synchronized");
         }

         if (this.isAbstract() && this.isNative()) {
            this.error("method may not be abstract and native");
         }

         if (this.isAbstract() && this.isStrictfp()) {
            this.error("method may not be abstract and strictfp");
         }

         if (this.isNative() && this.isStrictfp()) {
            this.error("method may not be native and strictfp");
         }
      }

      if (this.hostType().isInterfaceDecl()) {
         if (this.isStatic()) {
            this.error("interface method " + this.signature() + " in " + this.hostType().typeName() + " may not be static");
         }

         if (this.isStrictfp()) {
            this.error("interface method " + this.signature() + " in " + this.hostType().typeName() + " may not be strictfp");
         }

         if (this.isNative()) {
            this.error("interface method " + this.signature() + " in " + this.hostType().typeName() + " may not be native");
         }

         if (this.isSynchronized()) {
            this.error("interface method " + this.signature() + " in " + this.hostType().typeName() + " may not be synchronized");
         }

         if (this.isProtected()) {
            this.error("interface method " + this.signature() + " in " + this.hostType().typeName() + " may not be protected");
         }

         if (this.isPrivate()) {
            this.error("interface method " + this.signature() + " in " + this.hostType().typeName() + " may not be private");
         } else if (this.isFinal()) {
            this.error("interface method " + this.signature() + " in " + this.hostType().typeName() + " may not be final");
         }
      }

   }

   public void jimplify2() {
      if (this.generate() && !this.sootMethod().hasActiveBody() && (this.sootMethod().getSource() == null || !(this.sootMethod().getSource() instanceof CoffiMethodSource))) {
         try {
            if (this.hasBlock() && !this.hostType().isInterfaceDecl()) {
               JimpleBody body = Jimple.v().newBody(this.sootMethod());
               this.sootMethod().setActiveBody(body);
               Body b = new Body(this.hostType(), body, this);
               b.setLine(this);

               for(int i = 0; i < this.getNumParameter(); ++i) {
                  this.getParameter(i).jimplify2(b);
               }

               this.getBlock().jimplify2(b);
               if (this.type() instanceof VoidType) {
                  b.add(Jimple.v().newReturnVoidStmt());
               }
            }

         } catch (RuntimeException var4) {
            System.err.println("Error generating " + this.hostType().typeName() + ": " + this);
            throw var4;
         }
      }
   }

   private boolean refined_MethodDecl_MethodDecl_moreSpecificThan_MethodDecl(MethodDecl m) {
      if (this.getNumParameter() == 0) {
         return false;
      } else {
         for(int i = 0; i < this.getNumParameter(); ++i) {
            if (!this.getParameter(i).type().instanceOf(m.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   private int refined_EmitJimple_MethodDecl_sootTypeModifiers() {
      int result = 0;
      if (this.isPublic()) {
         result |= 1;
      }

      if (this.isProtected()) {
         result |= 4;
      }

      if (this.isPrivate()) {
         result |= 2;
      }

      if (this.isFinal()) {
         result |= 16;
      }

      if (this.isStatic()) {
         result |= 8;
      }

      if (this.isAbstract()) {
         result |= 1024;
      }

      if (this.isSynchronized()) {
         result |= 32;
      }

      if (this.isStrictfp()) {
         result |= 2048;
      }

      if (this.isNative()) {
         result |= 256;
      }

      return result;
   }

   public boolean accessibleFrom(TypeDecl type) {
      if (this.accessibleFrom_TypeDecl_values == null) {
         this.accessibleFrom_TypeDecl_values = new HashMap(4);
      }

      if (this.accessibleFrom_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.accessibleFrom_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean accessibleFrom_TypeDecl_value = this.accessibleFrom_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.accessibleFrom_TypeDecl_values.put(type, accessibleFrom_TypeDecl_value);
         }

         return accessibleFrom_TypeDecl_value;
      }
   }

   private boolean accessibleFrom_compute(TypeDecl type) {
      if (this.isPublic()) {
         return true;
      } else if (this.isProtected()) {
         if (this.hostPackage().equals(type.hostPackage())) {
            return true;
         } else {
            return type.withinBodyThatSubclasses(this.hostType()) != null;
         }
      } else if (this.isPrivate()) {
         return this.hostType().topLevelType() == type.topLevelType();
      } else {
         return this.hostPackage().equals(type.hostPackage());
      }
   }

   public int size() {
      ASTNode$State state = this.state();
      return 1;
   }

   public boolean isEmpty() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean contains(Object o) {
      ASTNode$State state = this.state();
      return this == o;
   }

   public int lineNumber() {
      ASTNode$State state = this.state();
      return getLine(this.IDstart);
   }

   public boolean throwsException(TypeDecl exceptionType) {
      if (this.throwsException_TypeDecl_values == null) {
         this.throwsException_TypeDecl_values = new HashMap(4);
      }

      if (this.throwsException_TypeDecl_values.containsKey(exceptionType)) {
         return (Boolean)this.throwsException_TypeDecl_values.get(exceptionType);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean throwsException_TypeDecl_value = this.throwsException_compute(exceptionType);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.throwsException_TypeDecl_values.put(exceptionType, throwsException_TypeDecl_value);
         }

         return throwsException_TypeDecl_value;
      }
   }

   private boolean throwsException_compute(TypeDecl exceptionType) {
      for(int i = 0; i < this.getNumException(); ++i) {
         if (exceptionType.instanceOf(this.getException(i).type())) {
            return true;
         }
      }

      return false;
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public String signature() {
      if (this.signature_computed) {
         return this.signature_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.signature_value = this.signature_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.signature_computed = true;
         }

         return this.signature_value;
      }
   }

   private String signature_compute() {
      StringBuffer s = new StringBuffer();
      s.append(this.name() + "(");

      for(int i = 0; i < this.getNumParameter(); ++i) {
         if (i != 0) {
            s.append(", ");
         }

         s.append(this.getParameter(i).type().erasure().typeName());
      }

      s.append(")");
      return s.toString();
   }

   public boolean sameSignature(MethodDecl m) {
      ASTNode$State state = this.state();
      return this.signature().equals(m.signature());
   }

   public boolean moreSpecificThan(MethodDecl m) {
      if (this.moreSpecificThan_MethodDecl_values == null) {
         this.moreSpecificThan_MethodDecl_values = new HashMap(4);
      }

      if (this.moreSpecificThan_MethodDecl_values.containsKey(m)) {
         return (Boolean)this.moreSpecificThan_MethodDecl_values.get(m);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean moreSpecificThan_MethodDecl_value = this.moreSpecificThan_compute(m);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.moreSpecificThan_MethodDecl_values.put(m, moreSpecificThan_MethodDecl_value);
         }

         return moreSpecificThan_MethodDecl_value;
      }
   }

   private boolean moreSpecificThan_compute(MethodDecl m) {
      if (!this.isVariableArity() && !m.isVariableArity()) {
         return this.refined_MethodDecl_MethodDecl_moreSpecificThan_MethodDecl(m);
      } else {
         int num = Math.max(this.getNumParameter(), m.getNumParameter());

         for(int i = 0; i < num; ++i) {
            TypeDecl t1 = i < this.getNumParameter() - 1 ? this.getParameter(i).type() : this.getParameter(this.getNumParameter() - 1).type().componentType();
            TypeDecl t2 = i < m.getNumParameter() - 1 ? m.getParameter(i).type() : m.getParameter(m.getNumParameter() - 1).type().componentType();
            if (!t1.instanceOf(t2)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean overrides(MethodDecl m) {
      if (this.overrides_MethodDecl_values == null) {
         this.overrides_MethodDecl_values = new HashMap(4);
      }

      if (this.overrides_MethodDecl_values.containsKey(m)) {
         return (Boolean)this.overrides_MethodDecl_values.get(m);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean overrides_MethodDecl_value = this.overrides_compute(m);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.overrides_MethodDecl_values.put(m, overrides_MethodDecl_value);
         }

         return overrides_MethodDecl_value;
      }
   }

   private boolean overrides_compute(MethodDecl m) {
      return !this.isStatic() && !m.isPrivate() && m.accessibleFrom(this.hostType()) && this.hostType().instanceOf(m.hostType()) && m.signature().equals(this.signature());
   }

   public boolean hides(MethodDecl m) {
      if (this.hides_MethodDecl_values == null) {
         this.hides_MethodDecl_values = new HashMap(4);
      }

      if (this.hides_MethodDecl_values.containsKey(m)) {
         return (Boolean)this.hides_MethodDecl_values.get(m);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean hides_MethodDecl_value = this.hides_compute(m);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.hides_MethodDecl_values.put(m, hides_MethodDecl_value);
         }

         return hides_MethodDecl_value;
      }
   }

   private boolean hides_compute(MethodDecl m) {
      return this.isStatic() && !m.isPrivate() && m.accessibleFrom(this.hostType()) && this.hostType().instanceOf(m.hostType()) && m.signature().equals(this.signature());
   }

   public SimpleSet parameterDeclaration(String name) {
      if (this.parameterDeclaration_String_values == null) {
         this.parameterDeclaration_String_values = new HashMap(4);
      }

      if (this.parameterDeclaration_String_values.containsKey(name)) {
         return (SimpleSet)this.parameterDeclaration_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet parameterDeclaration_String_value = this.parameterDeclaration_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.parameterDeclaration_String_values.put(name, parameterDeclaration_String_value);
         }

         return parameterDeclaration_String_value;
      }
   }

   private SimpleSet parameterDeclaration_compute(String name) {
      for(int i = 0; i < this.getNumParameter(); ++i) {
         if (this.getParameter(i).name().equals(name)) {
            return this.getParameter(i);
         }
      }

      return SimpleSet.emptySet;
   }

   public boolean isSynthetic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isSynthetic();
   }

   public boolean isPublic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isPublic() || this.hostType().isInterfaceDecl();
   }

   public boolean isPrivate() {
      ASTNode$State state = this.state();
      return this.getModifiers().isPrivate();
   }

   public boolean isProtected() {
      ASTNode$State state = this.state();
      return this.getModifiers().isProtected();
   }

   public boolean isAbstract() {
      ASTNode$State state = this.state();
      return this.getModifiers().isAbstract() || this.hostType().isInterfaceDecl();
   }

   public boolean isStatic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isStatic();
   }

   public boolean isFinal() {
      ASTNode$State state = this.state();
      return this.getModifiers().isFinal() || this.hostType().isFinal() || this.isPrivate();
   }

   public boolean isSynchronized() {
      ASTNode$State state = this.state();
      return this.getModifiers().isSynchronized();
   }

   public boolean isNative() {
      ASTNode$State state = this.state();
      return this.getModifiers().isNative();
   }

   public boolean isStrictfp() {
      ASTNode$State state = this.state();
      return this.getModifiers().isStrictfp();
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getID() + "]";
   }

   public TypeDecl type() {
      if (this.type_computed) {
         return this.type_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.type_value = this.type_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.type_computed = true;
         }

         return this.type_value;
      }
   }

   private TypeDecl type_compute() {
      return this.getTypeAccess().type();
   }

   public boolean isVoid() {
      ASTNode$State state = this.state();
      return this.type().isVoid();
   }

   public boolean mayOverrideReturn(MethodDecl m) {
      ASTNode$State state = this.state();
      return this.type().instanceOf(m.type());
   }

   public boolean annotationMethodOverride() {
      ASTNode$State state = this.state();
      return !this.hostType().ancestorMethods(this.signature()).isEmpty();
   }

   public boolean hasAnnotationSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      return this.getModifiers().hasAnnotationSuppressWarnings(s);
   }

   public boolean isDeprecated() {
      ASTNode$State state = this.state();
      return this.getModifiers().hasDeprecatedAnnotation();
   }

   public boolean usesTypeVariable() {
      if (this.usesTypeVariable_computed) {
         return this.usesTypeVariable_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.usesTypeVariable_value = this.usesTypeVariable_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.usesTypeVariable_computed = true;
         }

         return this.usesTypeVariable_value;
      }
   }

   private boolean usesTypeVariable_compute() {
      return this.getModifiers().usesTypeVariable() || this.getTypeAccess().usesTypeVariable() || this.getParameterList().usesTypeVariable() || this.getExceptionList().usesTypeVariable();
   }

   public MethodDecl sourceMethodDecl() {
      if (this.sourceMethodDecl_computed) {
         return this.sourceMethodDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sourceMethodDecl_value = this.sourceMethodDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sourceMethodDecl_computed = true;
         }

         return this.sourceMethodDecl_value;
      }
   }

   private MethodDecl sourceMethodDecl_compute() {
      return this;
   }

   public boolean visibleTypeParameters() {
      ASTNode$State state = this.state();
      return !this.isStatic();
   }

   public int arity() {
      ASTNode$State state = this.state();
      return this.getNumParameter();
   }

   public boolean isVariableArity() {
      ASTNode$State state = this.state();
      return this.getNumParameter() == 0 ? false : this.getParameter(this.getNumParameter() - 1).isVariableArity();
   }

   public ParameterDeclaration lastParameter() {
      ASTNode$State state = this.state();
      return this.getParameter(this.getNumParameter() - 1);
   }

   public int sootTypeModifiers() {
      ASTNode$State state = this.state();
      int res = this.refined_EmitJimple_MethodDecl_sootTypeModifiers();
      if (this.isVariableArity()) {
         res |= 128;
      }

      return res;
   }

   public SootMethod sootMethod() {
      if (this.sootMethod_computed) {
         return this.sootMethod_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sootMethod_value = this.sootMethod_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sootMethod_computed = true;
         }

         return this.sootMethod_value;
      }
   }

   private SootMethod sootMethod_compute() {
      ArrayList list = new ArrayList();

      for(int i = 0; i < this.getNumParameter(); ++i) {
         list.add(this.getParameter(i).type().getSootType());
      }

      return this.hostType().isArrayDecl() ? this.typeObject().getSootClassDecl().getMethod(this.name(), list, this.type().getSootType()) : this.hostType().getSootClassDecl().getMethod(this.name(), list, this.type().getSootType());
   }

   public SootMethodRef sootRef() {
      if (this.sootRef_computed) {
         return this.sootRef_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sootRef_value = this.sootRef_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sootRef_computed = true;
         }

         return this.sootRef_value;
      }
   }

   private SootMethodRef sootRef_compute() {
      ArrayList parameters = new ArrayList();

      for(int i = 0; i < this.getNumParameter(); ++i) {
         parameters.add(this.getParameter(i).type().getSootType());
      }

      SootMethodRef ref = Scene.v().makeMethodRef(this.hostType().getSootClassDecl(), this.name(), parameters, this.type().getSootType(), this.isStatic());
      return ref;
   }

   public int offsetBeforeParameters() {
      if (this.offsetBeforeParameters_computed) {
         return this.offsetBeforeParameters_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.offsetBeforeParameters_value = this.offsetBeforeParameters_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.offsetBeforeParameters_computed = true;
         }

         return this.offsetBeforeParameters_value;
      }
   }

   private int offsetBeforeParameters_compute() {
      return 0;
   }

   public int offsetAfterParameters() {
      if (this.offsetAfterParameters_computed) {
         return this.offsetAfterParameters_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.offsetAfterParameters_value = this.offsetAfterParameters_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.offsetAfterParameters_computed = true;
         }

         return this.offsetAfterParameters_value;
      }
   }

   private int offsetAfterParameters_compute() {
      return this.getNumParameter() == 0 ? this.offsetBeforeParameters() : this.getParameter(this.getNumParameter() - 1).localNum() + this.getParameter(this.getNumParameter() - 1).type().variableSize();
   }

   public MethodDecl erasedMethod() {
      ASTNode$State state = this.state();
      return this;
   }

   public boolean hasAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.getModifiers().hasAnnotationSafeVarargs();
   }

   public boolean hasIllegalAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.hasAnnotationSafeVarargs() && (!this.isVariableArity() || !this.isFinal() && !this.isStatic());
   }

   public boolean suppressWarnings(String type) {
      ASTNode$State state = this.state();
      return this.hasAnnotationSuppressWarnings(type) || this.withinSuppressWarnings(type);
   }

   public boolean handlesException(TypeDecl exceptionType) {
      if (this.handlesException_TypeDecl_values == null) {
         this.handlesException_TypeDecl_values = new HashMap(4);
      }

      if (this.handlesException_TypeDecl_values.containsKey(exceptionType)) {
         return (Boolean)this.handlesException_TypeDecl_values.get(exceptionType);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean handlesException_TypeDecl_value = this.getParent().Define_boolean_handlesException(this, (ASTNode)null, exceptionType);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.handlesException_TypeDecl_values.put(exceptionType, handlesException_TypeDecl_value);
         }

         return handlesException_TypeDecl_value;
      }
   }

   public MethodDecl unknownMethod() {
      ASTNode$State state = this.state();
      MethodDecl unknownMethod_value = this.getParent().Define_MethodDecl_unknownMethod(this, (ASTNode)null);
      return unknownMethod_value;
   }

   public TypeDecl typeObject() {
      ASTNode$State state = this.state();
      TypeDecl typeObject_value = this.getParent().Define_TypeDecl_typeObject(this, (ASTNode)null);
      return typeObject_value;
   }

   public boolean withinSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      boolean withinSuppressWarnings_String_value = this.getParent().Define_boolean_withinSuppressWarnings(this, (ASTNode)null, s);
      return withinSuppressWarnings_String_value;
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller != this.getBlockOptNoTransform()) {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      } else {
         return !v.isFinal() || !v.isClassVariable() && !v.isInstanceVariable() ? this.isDAbefore(v) : true;
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller != this.getBlockOptNoTransform()) {
         return this.getParent().Define_boolean_isDUbefore(this, caller, v);
      } else {
         return !v.isFinal() || !v.isClassVariable() && !v.isInstanceVariable();
      }
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller != this.getBlockOptNoTransform()) {
         return this.getParent().Define_boolean_handlesException(this, caller, exceptionType);
      } else {
         return this.throwsException(exceptionType) || this.handlesException(exceptionType);
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.parameterDeclaration(name);
      } else if (caller == this.getBlockOptNoTransform()) {
         SimpleSet set = this.parameterDeclaration(name);
         return !set.isEmpty() ? set : this.lookupVariable(name);
      } else {
         return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBePublic(this, caller);
   }

   public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeProtected(this, caller);
   }

   public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBePrivate(this, caller);
   }

   public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeAbstract(this, caller);
   }

   public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeStatic(this, caller);
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeFinal(this, caller);
   }

   public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeSynchronized(this, caller);
   }

   public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeNative(this, caller);
   }

   public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeStrictfp(this, caller);
   }

   public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
      return (ASTNode)(caller == this.getBlockOptNoTransform() ? this : this.getParent().Define_ASTNode_enclosingBlock(this, caller));
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getExceptionListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
      }
   }

   public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
      return caller == this.getBlockOptNoTransform() ? this.type() : this.getParent().Define_TypeDecl_returnType(this, caller);
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      return caller == this.getBlockOptNoTransform() ? this.isStatic() : this.getParent().Define_boolean_inStaticContext(this, caller);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      return caller == this.getBlockOptNoTransform() ? true : this.getParent().Define_boolean_reachable(this, caller);
   }

   public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return this.getParent().Define_boolean_isMethodParameter(this, caller);
      }
   }

   public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isConstructorParameter(this, caller);
      }
   }

   public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
      }
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      return caller == this.getModifiersNoTransform() ? name.equals("METHOD") : this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
   }

   public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
      if (caller == this.getParameterListNoTransform()) {
         int i = caller.getIndexOfChild(child);
         return i == this.getNumParameter() - 1;
      } else {
         return this.getParent().Define_boolean_variableArityValid(this, caller);
      }
   }

   public int Define_int_localNum(ASTNode caller, ASTNode child) {
      if (caller == this.getParameterListNoTransform()) {
         int index = caller.getIndexOfChild(child);
         return index == 0 ? this.offsetBeforeParameters() : this.getParameter(index - 1).localNum() + this.getParameter(index - 1).type().variableSize();
      } else {
         return this.getParent().Define_int_localNum(this, caller);
      }
   }

   public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
      if (caller == this.getBlockOptNoTransform()) {
         return this.getNumException() != 0;
      } else {
         return this.getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
      }
   }

   public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.getBlock().modifiedInScope(var);
      } else {
         return this.getParent().Define_boolean_inhModifiedInScope(this, caller, var);
      }
   }

   public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isCatchParam(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
