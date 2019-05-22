package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Local;
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

public class ConstructorDecl extends BodyDecl implements Cloneable {
   private boolean isDefaultConstructor = false;
   protected boolean addEnclosingVariables = true;
   public SootMethod sootMethod;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected Map accessibleFrom_TypeDecl_values;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected Map throwsException_TypeDecl_values;
   protected boolean name_computed = false;
   protected String name_value;
   protected boolean signature_computed = false;
   protected String signature_value;
   protected Map sameSignature_ConstructorDecl_values;
   protected Map moreSpecificThan_ConstructorDecl_values;
   protected Map parameterDeclaration_String_values;
   protected Map circularThisInvocation_ConstructorDecl_values;
   protected boolean sourceConstructorDecl_computed = false;
   protected ConstructorDecl sourceConstructorDecl_value;
   protected boolean sootMethod_computed = false;
   protected SootMethod sootMethod_value;
   protected boolean sootRef_computed = false;
   protected SootMethodRef sootRef_value;
   protected boolean localNumOfFirstParameter_computed = false;
   protected int localNumOfFirstParameter_value;
   protected boolean offsetFirstEnclosingVariable_computed = false;
   protected int offsetFirstEnclosingVariable_value;
   protected Map handlesException_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.accessibleFrom_TypeDecl_values = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.throwsException_TypeDecl_values = null;
      this.name_computed = false;
      this.name_value = null;
      this.signature_computed = false;
      this.signature_value = null;
      this.sameSignature_ConstructorDecl_values = null;
      this.moreSpecificThan_ConstructorDecl_values = null;
      this.parameterDeclaration_String_values = null;
      this.circularThisInvocation_ConstructorDecl_values = null;
      this.sourceConstructorDecl_computed = false;
      this.sourceConstructorDecl_value = null;
      this.sootMethod_computed = false;
      this.sootMethod_value = null;
      this.sootRef_computed = false;
      this.sootRef_value = null;
      this.localNumOfFirstParameter_computed = false;
      this.offsetFirstEnclosingVariable_computed = false;
      this.handlesException_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ConstructorDecl clone() throws CloneNotSupportedException {
      ConstructorDecl node = (ConstructorDecl)super.clone();
      node.accessibleFrom_TypeDecl_values = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.throwsException_TypeDecl_values = null;
      node.name_computed = false;
      node.name_value = null;
      node.signature_computed = false;
      node.signature_value = null;
      node.sameSignature_ConstructorDecl_values = null;
      node.moreSpecificThan_ConstructorDecl_values = null;
      node.parameterDeclaration_String_values = null;
      node.circularThisInvocation_ConstructorDecl_values = null;
      node.sourceConstructorDecl_computed = false;
      node.sourceConstructorDecl_value = null;
      node.sootMethod_computed = false;
      node.sootMethod_value = null;
      node.sootRef_computed = false;
      node.sootRef_value = null;
      node.localNumOfFirstParameter_computed = false;
      node.offsetFirstEnclosingVariable_computed = false;
      node.handlesException_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ConstructorDecl copy() {
      try {
         ConstructorDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ConstructorDecl fullCopy() {
      ConstructorDecl tree = this.copy();
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

   public boolean applicable(List argList) {
      if (this.getNumParameter() != argList.getNumChild()) {
         return false;
      } else {
         for(int i = 0; i < this.getNumParameter(); ++i) {
            TypeDecl arg = ((Expr)argList.getChild(i)).type();
            TypeDecl parameter = this.getParameter(i).type();
            if (!arg.instanceOf(parameter)) {
               return false;
            }
         }

         return true;
      }
   }

   public void setDefaultConstructor() {
      this.isDefaultConstructor = true;
   }

   public void checkModifiers() {
      super.checkModifiers();
   }

   public void nameCheck() {
      super.nameCheck();
      if (!this.hostType().name().equals(this.name())) {
         this.error("constructor " + this.name() + " does not have the same name as the simple name of the host class " + this.hostType().name());
      }

      if (this.hostType().lookupConstructor(this) != this) {
         this.error("constructor with signature " + this.signature() + " is multiply declared in type " + this.hostType().typeName());
      }

      if (this.circularThisInvocation(this)) {
         this.error("The constructor " + this.signature() + " may not directly or indirectly invoke itself");
      }

   }

   public void toString(StringBuffer s) {
      if (!this.isDefaultConstructor()) {
         s.append(this.indent());
         this.getModifiers().toString(s);
         s.append(this.name() + "(");
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

         s.append(" {");
         if (this.hasConstructorInvocation()) {
            this.getConstructorInvocation().toString(s);
         }

         for(i = 0; i < this.getBlock().getNumStmt(); ++i) {
            this.getBlock().getStmt(i).toString(s);
         }

         s.append(this.indent());
         s.append("}");
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

   }

   protected void transformEnumConstructors() {
      Modifiers newModifiers = new Modifiers(new List());

      for(int i = 0; i < this.getModifiers().getNumModifier(); ++i) {
         String modifier = this.getModifiers().getModifier(i).getID();
         if (!modifier.equals("public") && !modifier.equals("private") && !modifier.equals("protected")) {
            newModifiers.addModifier(new Modifier(modifier));
         }
      }

      newModifiers.addModifier(new Modifier("private"));
      this.setModifiers(newModifiers);
      if (!this.hasConstructorInvocation()) {
         this.setConstructorInvocation(new ExprStmt(new SuperConstructorAccess("super", new List())));
      }

      super.transformEnumConstructors();
      this.getParameterList().insertChild(new ParameterDeclaration(new TypeAccess("java.lang", "String"), "@p0"), 0);
      this.getParameterList().insertChild(new ParameterDeclaration(new TypeAccess("int"), "@p1"), 1);
   }

   public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
      ConstructorDecl c = new ConstructorDeclSubstituted(this.getModifiers().fullCopy(), this.getID(), this.getParameterList().substitute(parTypeDecl), this.getExceptionList().substitute(parTypeDecl), new Opt(), new Block(), this);
      return c;
   }

   public void addEnclosingVariables() {
      if (this.addEnclosingVariables) {
         this.addEnclosingVariables = false;
         this.hostType().addEnclosingVariables();
         Iterator iter = this.hostType().enclosingVariables().iterator();

         while(iter.hasNext()) {
            Variable v = (Variable)iter.next();
            this.getParameterList().add(new ParameterDeclaration(v.type(), "val$" + v.name()));
         }

      }
   }

   public ConstructorDecl createAccessor() {
      ConstructorDecl c = (ConstructorDecl)this.hostType().getAccessor(this, "constructor");
      if (c != null) {
         return c;
      } else {
         this.addEnclosingVariables();
         Modifiers modifiers = new Modifiers(new List());
         modifiers.addModifier(new Modifier("synthetic"));
         modifiers.addModifier(new Modifier("public"));
         List parameters = this.createAccessorParameters();
         List exceptionList = new List();

         for(int i = 0; i < this.getNumException(); ++i) {
            exceptionList.add(this.getException(i).type().createQualifiedAccess());
         }

         List args = new List();

         for(int i = 0; i < parameters.getNumChildNoTransform() - 1; ++i) {
            args.add(new VarAccess(((ParameterDeclaration)parameters.getChildNoTransform(i)).name()));
         }

         ConstructorAccess access = new ConstructorAccess("this", args);
         access.addEnclosingVariables = false;
         c = new ConstructorDecl(modifiers, this.name(), parameters, exceptionList, new Opt(new ExprStmt(access)), new Block((new List()).add(new ReturnStmt(new Opt()))));
         c = this.hostType().addConstructor(c);
         c.addEnclosingVariables = false;
         this.hostType().addAccessor(this, "constructor", c);
         return c;
      }
   }

   protected List createAccessorParameters() {
      List parameters = new List();

      for(int i = 0; i < this.getNumParameter(); ++i) {
         parameters.add(new ParameterDeclaration(this.getParameter(i).type(), this.getParameter(i).name()));
      }

      parameters.add(new ParameterDeclaration(this.createAnonymousJavaTypeDecl().createBoundAccess(), "p" + this.getNumParameter()));
      return parameters;
   }

   protected TypeDecl createAnonymousJavaTypeDecl() {
      ClassDecl classDecl = new ClassDecl(new Modifiers((new List()).add(new Modifier("synthetic"))), "" + this.hostType().nextAnonymousIndex(), new Opt(), new List(), new List());
      classDecl = this.hostType().addMemberClass(classDecl);
      this.hostType().addNestedType(classDecl);
      return classDecl;
   }

   public void transformation() {
      this.addEnclosingVariables();
      super.transformation();
   }

   public void jimplify1phase2() {
      String name = "<init>";
      ArrayList parameters = new ArrayList();
      ArrayList paramnames = new ArrayList();
      TypeDecl typeDecl = this.hostType();
      if (typeDecl.needsEnclosing()) {
         parameters.add(typeDecl.enclosingType().getSootType());
      }

      if (typeDecl.needsSuperEnclosing()) {
         TypeDecl superClass = ((ClassDecl)typeDecl).superclass();
         parameters.add(superClass.enclosingType().getSootType());
      }

      for(int i = 0; i < this.getNumParameter(); ++i) {
         parameters.add(this.getParameter(i).type().getSootType());
         paramnames.add(this.getParameter(i).name());
      }

      Type returnType = soot.VoidType.v();
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

   public ConstructorDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[5];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
      this.setChild(new Opt(), 3);
   }

   public ConstructorDecl(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
   }

   public ConstructorDecl(Modifiers p0, Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
   }

   protected int numChildren() {
      return 5;
   }

   public boolean mayHaveRewrite() {
      return true;
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
      this.setChild(list, 1);
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
      List<ParameterDeclaration> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<ParameterDeclaration> getParameterListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setExceptionList(List<Access> list) {
      this.setChild(list, 2);
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
      List<Access> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<Access> getExceptionListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setConstructorInvocationOpt(Opt<Stmt> opt) {
      this.setChild(opt, 3);
   }

   public boolean hasConstructorInvocation() {
      return this.getConstructorInvocationOpt().getNumChild() != 0;
   }

   public Stmt getConstructorInvocation() {
      return (Stmt)this.getConstructorInvocationOpt().getChild(0);
   }

   public void setConstructorInvocation(Stmt node) {
      this.getConstructorInvocationOpt().setChild(node, 0);
   }

   public Opt<Stmt> getConstructorInvocationOpt() {
      return (Opt)this.getChild(3);
   }

   public Opt<Stmt> getConstructorInvocationOptNoTransform() {
      return (Opt)this.getChildNoTransform(3);
   }

   public void setBlock(Block node) {
      this.setChild(node, 4);
   }

   public Block getBlock() {
      return (Block)this.getChild(4);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(4);
   }

   public void jimplify2() {
      if (this.generate() && !this.sootMethod().hasActiveBody() && (this.sootMethod().getSource() == null || !(this.sootMethod().getSource() instanceof CoffiMethodSource))) {
         JimpleBody body = Jimple.v().newBody(this.sootMethod());
         this.sootMethod().setActiveBody(body);
         Body b = new Body(this.hostType(), body, this);
         b.setLine(this);

         for(int i = 0; i < this.getNumParameter(); ++i) {
            this.getParameter(i).jimplify2(b);
         }

         boolean needsInit = true;
         if (this.hasConstructorInvocation()) {
            this.getConstructorInvocation().jimplify2(b);
            Stmt stmt = this.getConstructorInvocation();
            if (stmt instanceof ExprStmt) {
               ExprStmt exprStmt = (ExprStmt)stmt;
               Expr expr = exprStmt.getExpr();
               if (!expr.isSuperConstructorAccess()) {
                  needsInit = false;
               }
            }
         }

         TypeDecl typeDecl;
         if (this.hostType().needsEnclosing()) {
            typeDecl = this.hostType().enclosingType();
            b.add(Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(b.emitThis(this.hostType()), this.hostType().getSootField("this$0", typeDecl).makeRef()), this.asLocal(b, Jimple.v().newParameterRef(typeDecl.getSootType(), 0))));
         }

         Iterator iter = this.hostType().enclosingVariables().iterator();

         while(iter.hasNext()) {
            Variable v = (Variable)iter.next();
            ParameterDeclaration p = (ParameterDeclaration)this.parameterDeclaration("val$" + v.name()).iterator().next();
            b.add(Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(b.emitThis(this.hostType()), Scene.v().makeFieldRef(this.hostType().getSootClassDecl(), "val$" + v.name(), v.type().getSootType(), false)), p.local));
         }

         if (needsInit) {
            typeDecl = this.hostType();

            for(int i = 0; i < typeDecl.getNumBodyDecl(); ++i) {
               BodyDecl bodyDecl = typeDecl.getBodyDecl(i);
               if (bodyDecl instanceof FieldDeclaration && bodyDecl.generate()) {
                  FieldDeclaration f = (FieldDeclaration)bodyDecl;
                  if (!f.isStatic() && f.hasInit()) {
                     Local base = b.emitThis(this.hostType());
                     Local l = this.asLocal(b, f.getInit().type().emitCastTo(b, f.getInit(), f.type()), f.type().getSootType());
                     b.setLine(f);
                     b.add(Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(base, f.sootRef()), l));
                  }
               } else if (bodyDecl instanceof InstanceInitializer && bodyDecl.generate()) {
                  bodyDecl.jimplify2(b);
               }
            }
         }

         this.getBlock().jimplify2(b);
         b.add(Jimple.v().newReturnVoidStmt());
      }
   }

   private boolean refined_ConstructorDecl_ConstructorDecl_moreSpecificThan_ConstructorDecl(ConstructorDecl m) {
      for(int i = 0; i < this.getNumParameter(); ++i) {
         if (!this.getParameter(i).type().instanceOf(m.getParameter(i).type())) {
            return false;
         }
      }

      return true;
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
      if (!this.hostType().accessibleFrom(type)) {
         return false;
      } else if (this.isPublic()) {
         return true;
      } else if (this.isProtected()) {
         return true;
      } else if (this.isPrivate()) {
         return this.hostType().topLevelType() == type.topLevelType();
      } else {
         return this.hostPackage().equals(type.hostPackage());
      }
   }

   public boolean isDAafter(Variable v) {
      if (this.isDAafter_Variable_values == null) {
         this.isDAafter_Variable_values = new HashMap(4);
      }

      if (this.isDAafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafter_Variable_value = this.isDAafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafter_Variable_values.put(v, isDAafter_Variable_value);
         }

         return isDAafter_Variable_value;
      }
   }

   private boolean isDAafter_compute(Variable v) {
      return this.getBlock().isDAafter(v) && this.getBlock().checkReturnDA(v);
   }

   public boolean isDUafter(Variable v) {
      if (this.isDUafter_Variable_values == null) {
         this.isDUafter_Variable_values = new HashMap(4);
      }

      if (this.isDUafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUafter_Variable_value = this.isDUafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUafter_Variable_values.put(v, isDUafter_Variable_value);
         }

         return isDUafter_Variable_value;
      }
   }

   private boolean isDUafter_compute(Variable v) {
      return this.getBlock().isDUafter(v) && this.getBlock().checkReturnDU(v);
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
      if (this.name_computed) {
         return this.name_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.name_value = this.name_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.name_computed = true;
         }

         return this.name_value;
      }
   }

   private String name_compute() {
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
         s.append(this.getParameter(i));
         if (i != this.getNumParameter() - 1) {
            s.append(", ");
         }
      }

      s.append(")");
      return s.toString();
   }

   public boolean sameSignature(ConstructorDecl c) {
      if (this.sameSignature_ConstructorDecl_values == null) {
         this.sameSignature_ConstructorDecl_values = new HashMap(4);
      }

      if (this.sameSignature_ConstructorDecl_values.containsKey(c)) {
         return (Boolean)this.sameSignature_ConstructorDecl_values.get(c);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean sameSignature_ConstructorDecl_value = this.sameSignature_compute(c);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sameSignature_ConstructorDecl_values.put(c, sameSignature_ConstructorDecl_value);
         }

         return sameSignature_ConstructorDecl_value;
      }
   }

   private boolean sameSignature_compute(ConstructorDecl c) {
      if (!this.name().equals(c.name())) {
         return false;
      } else if (c.getNumParameter() != this.getNumParameter()) {
         return false;
      } else {
         for(int i = 0; i < this.getNumParameter(); ++i) {
            if (!c.getParameter(i).type().equals(this.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean moreSpecificThan(ConstructorDecl m) {
      if (this.moreSpecificThan_ConstructorDecl_values == null) {
         this.moreSpecificThan_ConstructorDecl_values = new HashMap(4);
      }

      if (this.moreSpecificThan_ConstructorDecl_values.containsKey(m)) {
         return (Boolean)this.moreSpecificThan_ConstructorDecl_values.get(m);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean moreSpecificThan_ConstructorDecl_value = this.moreSpecificThan_compute(m);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.moreSpecificThan_ConstructorDecl_values.put(m, moreSpecificThan_ConstructorDecl_value);
         }

         return moreSpecificThan_ConstructorDecl_value;
      }
   }

   private boolean moreSpecificThan_compute(ConstructorDecl m) {
      if (!this.isVariableArity() && !m.isVariableArity()) {
         return this.refined_ConstructorDecl_ConstructorDecl_moreSpecificThan_ConstructorDecl(m);
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

   public boolean isDefaultConstructor() {
      ASTNode$State state = this.state();
      return this.isDefaultConstructor;
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
      return this.getModifiers().isPublic();
   }

   public boolean isPrivate() {
      ASTNode$State state = this.state();
      return this.getModifiers().isPrivate();
   }

   public boolean isProtected() {
      ASTNode$State state = this.state();
      return this.getModifiers().isProtected();
   }

   public boolean circularThisInvocation(ConstructorDecl decl) {
      if (this.circularThisInvocation_ConstructorDecl_values == null) {
         this.circularThisInvocation_ConstructorDecl_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.circularThisInvocation_ConstructorDecl_values.containsKey(decl)) {
         Object _o = this.circularThisInvocation_ConstructorDecl_values.get(decl);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.circularThisInvocation_ConstructorDecl_values.put(decl, _value);
         _value.value = true;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_circularThisInvocation_ConstructorDecl_value = this.circularThisInvocation_compute(decl);
            if (state.RESET_CYCLE) {
               this.circularThisInvocation_ConstructorDecl_values.remove(decl);
            } else if (new_circularThisInvocation_ConstructorDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_circularThisInvocation_ConstructorDecl_value;
            }

            return new_circularThisInvocation_ConstructorDecl_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_circularThisInvocation_ConstructorDecl_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_circularThisInvocation_ConstructorDecl_value = this.circularThisInvocation_compute(decl);
            if (new_circularThisInvocation_ConstructorDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_circularThisInvocation_ConstructorDecl_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.circularThisInvocation_ConstructorDecl_values.put(decl, new_circularThisInvocation_ConstructorDecl_value);
         } else {
            this.circularThisInvocation_ConstructorDecl_values.remove(decl);
            state.RESET_CYCLE = true;
            this.circularThisInvocation_compute(decl);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_circularThisInvocation_ConstructorDecl_value;
      }
   }

   private boolean circularThisInvocation_compute(ConstructorDecl decl) {
      if (this.hasConstructorInvocation()) {
         Expr e = ((ExprStmt)this.getConstructorInvocation()).getExpr();
         if (e instanceof ConstructorAccess) {
            ConstructorDecl constructorDecl = ((ConstructorAccess)e).decl();
            if (constructorDecl == decl) {
               return true;
            }

            return constructorDecl.circularThisInvocation(decl);
         }
      }

      return false;
   }

   public TypeDecl type() {
      ASTNode$State state = this.state();
      return this.unknownType();
   }

   public boolean isVoid() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean hasAnnotationSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      return this.getModifiers().hasAnnotationSuppressWarnings(s);
   }

   public boolean isDeprecated() {
      ASTNode$State state = this.state();
      return this.getModifiers().hasDeprecatedAnnotation();
   }

   public ConstructorDecl sourceConstructorDecl() {
      if (this.sourceConstructorDecl_computed) {
         return this.sourceConstructorDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sourceConstructorDecl_value = this.sourceConstructorDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sourceConstructorDecl_computed = true;
         }

         return this.sourceConstructorDecl_value;
      }
   }

   private ConstructorDecl sourceConstructorDecl_compute() {
      return this;
   }

   public boolean applicableBySubtyping(List argList) {
      ASTNode$State state = this.state();
      if (this.getNumParameter() != argList.getNumChild()) {
         return false;
      } else {
         for(int i = 0; i < this.getNumParameter(); ++i) {
            TypeDecl arg = ((Expr)argList.getChild(i)).type();
            if (!arg.instanceOf(this.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean applicableByMethodInvocationConversion(List argList) {
      ASTNode$State state = this.state();
      if (this.getNumParameter() != argList.getNumChild()) {
         return false;
      } else {
         for(int i = 0; i < this.getNumParameter(); ++i) {
            TypeDecl arg = ((Expr)argList.getChild(i)).type();
            if (!arg.methodInvocationConversionTo(this.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean applicableVariableArity(List argList) {
      ASTNode$State state = this.state();

      int i;
      TypeDecl arg;
      for(i = 0; i < this.getNumParameter() - 1; ++i) {
         arg = ((Expr)argList.getChild(i)).type();
         if (!arg.methodInvocationConversionTo(this.getParameter(i).type())) {
            return false;
         }
      }

      for(i = this.getNumParameter() - 1; i < argList.getNumChild(); ++i) {
         arg = ((Expr)argList.getChild(i)).type();
         if (!arg.methodInvocationConversionTo(this.lastParameter().type().componentType())) {
            return false;
         }
      }

      return true;
   }

   public boolean potentiallyApplicable(List argList) {
      ASTNode$State state = this.state();
      if (this.isVariableArity() && argList.getNumChild() < this.arity() - 1) {
         return false;
      } else {
         return this.isVariableArity() || this.arity() == argList.getNumChild();
      }
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

   public boolean needsEnclosing() {
      ASTNode$State state = this.state();
      return this.hostType().needsEnclosing();
   }

   public boolean needsSuperEnclosing() {
      ASTNode$State state = this.state();
      return this.hostType().needsSuperEnclosing();
   }

   public TypeDecl enclosing() {
      ASTNode$State state = this.state();
      return this.hostType().enclosing();
   }

   public TypeDecl superEnclosing() {
      ASTNode$State state = this.state();
      return this.hostType().superEnclosing();
   }

   public int sootTypeModifiers() {
      ASTNode$State state = this.state();
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

      return result;
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
      TypeDecl typeDecl = this.hostType();
      if (typeDecl.needsEnclosing()) {
         list.add(typeDecl.enclosingType().getSootType());
      }

      if (typeDecl.needsSuperEnclosing()) {
         TypeDecl superClass = ((ClassDecl)typeDecl).superclass();
         list.add(superClass.enclosingType().getSootType());
      }

      for(int i = 0; i < this.getNumParameter(); ++i) {
         list.add(this.getParameter(i).type().getSootType());
      }

      return this.hostType().getSootClassDecl().getMethod("<init>", list, soot.VoidType.v());
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
      TypeDecl typeDecl = this.hostType();
      if (typeDecl.needsEnclosing()) {
         parameters.add(typeDecl.enclosingType().getSootType());
      }

      if (typeDecl.needsSuperEnclosing()) {
         TypeDecl superClass = ((ClassDecl)typeDecl).superclass();
         parameters.add(superClass.enclosingType().getSootType());
      }

      for(int i = 0; i < this.getNumParameter(); ++i) {
         parameters.add(this.getParameter(i).type().getSootType());
      }

      SootMethodRef ref = Scene.v().makeConstructorRef(this.hostType().getSootClassDecl(), parameters);
      return ref;
   }

   public int localNumOfFirstParameter() {
      if (this.localNumOfFirstParameter_computed) {
         return this.localNumOfFirstParameter_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.localNumOfFirstParameter_value = this.localNumOfFirstParameter_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localNumOfFirstParameter_computed = true;
         }

         return this.localNumOfFirstParameter_value;
      }
   }

   private int localNumOfFirstParameter_compute() {
      int i = 0;
      if (this.hostType().needsEnclosing()) {
         ++i;
      }

      if (this.hostType().needsSuperEnclosing()) {
         ++i;
      }

      return i;
   }

   public int offsetFirstEnclosingVariable() {
      if (this.offsetFirstEnclosingVariable_computed) {
         return this.offsetFirstEnclosingVariable_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.offsetFirstEnclosingVariable_value = this.offsetFirstEnclosingVariable_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.offsetFirstEnclosingVariable_computed = true;
         }

         return this.offsetFirstEnclosingVariable_value;
      }
   }

   private int offsetFirstEnclosingVariable_compute() {
      return this.getNumParameter() == 0 ? this.localNumOfFirstParameter() : this.getParameter(this.getNumParameter() - 1).localNum() + this.getParameter(this.getNumParameter() - 1).type().variableSize();
   }

   public ConstructorDecl erasedConstructor() {
      ASTNode$State state = this.state();
      return this;
   }

   public boolean hasAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.getModifiers().hasAnnotationSafeVarargs();
   }

   public boolean hasIllegalAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.hasAnnotationSafeVarargs() && !this.isVariableArity();
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

   public TypeDecl unknownType() {
      ASTNode$State state = this.state();
      TypeDecl unknownType_value = this.getParent().Define_TypeDecl_unknownType(this, (ASTNode)null);
      return unknownType_value;
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getBlockNoTransform()) {
         return this.hasConstructorInvocation() ? this.getConstructorInvocation().isDAafter(v) : this.isDAbefore(v);
      } else {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getBlockNoTransform()) {
         return this.hasConstructorInvocation() ? this.getConstructorInvocation().isDUafter(v) : this.isDUbefore(v);
      } else {
         return this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller == this.getConstructorInvocationOptNoTransform()) {
         return this.throwsException(exceptionType) || this.handlesException(exceptionType);
      } else if (caller != this.getBlockNoTransform()) {
         return this.getParent().Define_boolean_handlesException(this, caller, exceptionType);
      } else {
         return this.throwsException(exceptionType) || this.handlesException(exceptionType);
      }
   }

   public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
      if (caller != this.getConstructorInvocationOptNoTransform()) {
         return this.getParent().Define_Collection_lookupMethod(this, caller, name);
      } else {
         Collection c = new ArrayList();
         Iterator iter = this.lookupMethod(name).iterator();

         while(true) {
            MethodDecl m;
            do {
               if (!iter.hasNext()) {
                  return c;
               }

               m = (MethodDecl)iter.next();
            } while(this.hostType().memberMethods(name).contains(m) && !m.isStatic());

            c.add(m);
         }
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.parameterDeclaration(name);
      } else {
         SimpleSet set;
         if (caller != this.getConstructorInvocationOptNoTransform()) {
            if (caller == this.getBlockNoTransform()) {
               set = this.parameterDeclaration(name);
               return !set.isEmpty() ? set : this.lookupVariable(name);
            } else {
               return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
            }
         } else {
            set = this.parameterDeclaration(name);
            if (!set.isEmpty()) {
               return set;
            } else {
               Iterator iter = this.lookupVariable(name).iterator();

               while(true) {
                  Variable v;
                  do {
                     if (!iter.hasNext()) {
                        return set;
                     }

                     v = (Variable)iter.next();
                  } while(this.hostType().memberFields(name).contains(v) && !v.isStatic());

                  set = set.add(v);
               }
            }
         }
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

   public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
      return (ASTNode)(caller == this.getBlockNoTransform() ? this : this.getParent().Define_ASTNode_enclosingBlock(this, caller));
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getConstructorInvocationOptNoTransform()) {
         return NameType.EXPRESSION_NAME;
      } else if (caller == this.getExceptionListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return this.getParent().Define_NameType_nameType(this, caller);
      }
   }

   public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
      return caller == this.getConstructorInvocationOptNoTransform() ? this.unknownType() : this.getParent().Define_TypeDecl_enclosingInstance(this, caller);
   }

   public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
      return caller == this.getConstructorInvocationOptNoTransform() ? true : this.getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      if (caller == this.getConstructorInvocationOptNoTransform()) {
         return false;
      } else {
         return caller == this.getBlockNoTransform() ? false : this.getParent().Define_boolean_inStaticContext(this, caller);
      }
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      if (caller == this.getBlockNoTransform()) {
         return !this.hasConstructorInvocation() ? true : this.getConstructorInvocation().canCompleteNormally();
      } else {
         return caller == this.getConstructorInvocationOptNoTransform() ? true : this.getParent().Define_boolean_reachable(this, caller);
      }
   }

   public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isMethodParameter(this, caller);
      }
   }

   public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
      if (caller == this.getParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
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
      return caller == this.getModifiersNoTransform() ? name.equals("CONSTRUCTOR") : this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
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
         return index == 0 ? this.localNumOfFirstParameter() : this.getParameter(index - 1).localNum() + this.getParameter(index - 1).type().variableSize();
      } else {
         return this.getParent().Define_int_localNum(this, caller);
      }
   }

   public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
      if (caller == this.getBlockNoTransform()) {
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
      if (!this.hasConstructorInvocation() && !this.hostType().isObject()) {
         ++this.state().duringImplicitConstructor;
         ASTNode result = this.rewriteRule0();
         --this.state().duringImplicitConstructor;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private ConstructorDecl rewriteRule0() {
      this.setConstructorInvocation(new ExprStmt(new SuperConstructorAccess("super", new List())));
      return this;
   }
}
