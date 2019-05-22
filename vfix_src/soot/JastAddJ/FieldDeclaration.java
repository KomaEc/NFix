package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.Scene;
import soot.SootField;
import soot.SootFieldRef;
import soot.Type;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;

public class FieldDeclaration extends MemberDecl implements Cloneable, SimpleSet, Iterator, Variable {
   private FieldDeclaration iterElem;
   private FieldDecl fieldDecl;
   public SootField sootField;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected Map accessibleFrom_TypeDecl_values;
   protected boolean exceptions_computed;
   protected Collection exceptions_value;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean constant_computed;
   protected Constant constant_value;
   protected boolean usesTypeVariable_computed;
   protected boolean usesTypeVariable_value;
   protected boolean sourceVariableDecl_computed;
   protected Variable sourceVariableDecl_value;
   protected boolean sootRef_computed;
   protected SootFieldRef sootRef_value;
   protected boolean throwTypes_computed;
   protected Collection<TypeDecl> throwTypes_value;

   public void flushCache() {
      super.flushCache();
      this.accessibleFrom_TypeDecl_values = null;
      this.exceptions_computed = false;
      this.exceptions_value = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.constant_computed = false;
      this.constant_value = null;
      this.usesTypeVariable_computed = false;
      this.sourceVariableDecl_computed = false;
      this.sourceVariableDecl_value = null;
      this.sootRef_computed = false;
      this.sootRef_value = null;
      this.throwTypes_computed = false;
      this.throwTypes_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public FieldDeclaration clone() throws CloneNotSupportedException {
      FieldDeclaration node = (FieldDeclaration)super.clone();
      node.accessibleFrom_TypeDecl_values = null;
      node.exceptions_computed = false;
      node.exceptions_value = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.constant_computed = false;
      node.constant_value = null;
      node.usesTypeVariable_computed = false;
      node.sourceVariableDecl_computed = false;
      node.sourceVariableDecl_value = null;
      node.sootRef_computed = false;
      node.sootRef_value = null;
      node.throwTypes_computed = false;
      node.throwTypes_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public FieldDeclaration copy() {
      try {
         FieldDeclaration node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public FieldDeclaration fullCopy() {
      FieldDeclaration tree = this.copy();
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

   public Access createQualifiedBoundAccess() {
      return this.isStatic() ? this.hostType().createQualifiedAccess().qualifiesAccess(new BoundFieldAccess(this)) : (new ThisAccess("this")).qualifiesAccess(new BoundFieldAccess(this));
   }

   public Access createBoundFieldAccess() {
      return this.createQualifiedBoundAccess();
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

   public void definiteAssignment() {
      super.definiteAssignment();
      int i;
      FieldDeclaration f;
      if (this.isBlank() && this.isFinal() && this.isClassVariable()) {
         boolean found = false;
         TypeDecl typeDecl = this.hostType();

         for(i = 0; i < typeDecl.getNumBodyDecl(); ++i) {
            if (typeDecl.getBodyDecl(i) instanceof StaticInitializer) {
               StaticInitializer s = (StaticInitializer)typeDecl.getBodyDecl(i);
               if (s.isDAafter(this)) {
                  found = true;
               }
            } else if (typeDecl.getBodyDecl(i) instanceof FieldDeclaration) {
               f = (FieldDeclaration)typeDecl.getBodyDecl(i);
               if (f.isStatic() && f.isDAafter(this)) {
                  found = true;
               }
            }
         }

         if (!found) {
            this.error("blank final class variable " + this.name() + " in " + this.hostType().typeName() + " is not definitely assigned in static initializer");
         }
      }

      if (this.isBlank() && this.isFinal() && this.isInstanceVariable()) {
         TypeDecl typeDecl = this.hostType();
         boolean found = false;

         for(i = 0; !found && i < typeDecl.getNumBodyDecl(); ++i) {
            if (typeDecl.getBodyDecl(i) instanceof FieldDeclaration) {
               f = (FieldDeclaration)typeDecl.getBodyDecl(i);
               if (!f.isStatic() && f.isDAafter(this)) {
                  found = true;
               }
            } else if (typeDecl.getBodyDecl(i) instanceof InstanceInitializer) {
               InstanceInitializer ii = (InstanceInitializer)typeDecl.getBodyDecl(i);
               if (ii.getBlock().isDAafter(this)) {
                  found = true;
               }
            }
         }

         Iterator iter = typeDecl.constructors().iterator();

         while(!found && iter.hasNext()) {
            ConstructorDecl c = (ConstructorDecl)iter.next();
            if (!c.isDAafter(this)) {
               this.error("blank final instance variable " + this.name() + " in " + this.hostType().typeName() + " is not definitely assigned after " + c.signature());
            }
         }
      }

      if (this.isBlank() && this.hostType().isInterfaceDecl()) {
         this.error("variable  " + this.name() + " in " + this.hostType().typeName() + " which is an interface must have an initializer");
      }

   }

   public void checkModifiers() {
      super.checkModifiers();
      if (this.hostType().isInterfaceDecl()) {
         if (this.isProtected()) {
            this.error("an interface field may not be protected");
         }

         if (this.isPrivate()) {
            this.error("an interface field may not be private");
         }

         if (this.isTransient()) {
            this.error("an interface field may not be transient");
         }

         if (this.isVolatile()) {
            this.error("an interface field may not be volatile");
         }
      }

   }

   public void nameCheck() {
      super.nameCheck();
      Iterator iter = this.hostType().memberFields(this.name()).iterator();

      while(iter.hasNext()) {
         Variable v = (Variable)iter.next();
         if (v != this && v.hostType() == this.hostType()) {
            this.error("field named " + this.name() + " is multiply declared in type " + this.hostType().typeName());
         }
      }

   }

   public FieldDeclaration(Modifiers m, Access type, String name) {
      this(m, type, name, new Opt());
   }

   public FieldDeclaration(Modifiers m, Access type, String name, Expr init) {
      this(m, type, name, new Opt(init));
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getModifiers().toString(s);
      this.getTypeAccess().toString(s);
      s.append(" " + this.name());
      if (this.hasInit()) {
         s.append(" = ");
         this.getInit().toString(s);
      }

      s.append(";");
   }

   public void typeCheck() {
      if (this.hasInit()) {
         TypeDecl source = this.getInit().type();
         TypeDecl dest = this.type();
         if (!source.assignConversionTo(dest, this.getInit())) {
            this.error("can not assign field " + this.name() + " of type " + dest.typeName() + " a value of type " + source.typeName());
         }
      }

   }

   public FieldDecl getFieldDecl() {
      return this.fieldDecl;
   }

   public void setFieldDecl(FieldDecl fieldDecl) {
      this.fieldDecl = fieldDecl;
   }

   public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
      FieldDeclaration f = new FieldDeclarationSubstituted(this.getModifiers().fullCopy(), this.getTypeAccess().type().substituteReturnType(parTypeDecl), this.getID(), new Opt(), this);
      return f;
   }

   public MethodDecl createAccessor(TypeDecl fieldQualifier) {
      MethodDecl m = (MethodDecl)fieldQualifier.getAccessor(this, "field_read");
      if (m != null) {
         return m;
      } else {
         int accessorIndex = fieldQualifier.accessorCounter++;
         Modifiers modifiers = new Modifiers(new List());
         modifiers.addModifier(new Modifier("static"));
         modifiers.addModifier(new Modifier("synthetic"));
         modifiers.addModifier(new Modifier("public"));
         List parameters = new List();
         if (!this.isStatic()) {
            parameters.add(new ParameterDeclaration(fieldQualifier.createQualifiedAccess(), "that"));
         }

         m = new MethodDecl(modifiers, this.type().createQualifiedAccess(), "get$" + this.name() + "$access$" + accessorIndex, parameters, new List(), new Opt(new Block((new List()).add(new ReturnStmt(this.createAccess())))));
         m = fieldQualifier.addMemberMethod(m);
         fieldQualifier.addAccessor(this, "field_read", m);
         return m;
      }
   }

   public MethodDecl createAccessorWrite(TypeDecl fieldQualifier) {
      MethodDecl m = (MethodDecl)fieldQualifier.getAccessor(this, "field_write");
      if (m != null) {
         return m;
      } else {
         int accessorIndex = fieldQualifier.accessorCounter++;
         Modifiers modifiers = new Modifiers(new List());
         modifiers.addModifier(new Modifier("static"));
         modifiers.addModifier(new Modifier("synthetic"));
         modifiers.addModifier(new Modifier("public"));
         List parameters = new List();
         if (!this.isStatic()) {
            parameters.add(new ParameterDeclaration(fieldQualifier.createQualifiedAccess(), "that"));
         }

         parameters.add(new ParameterDeclaration(this.type().createQualifiedAccess(), "value"));
         m = new MethodDecl(modifiers, this.type().createQualifiedAccess(), "set$" + this.name() + "$access$" + accessorIndex, parameters, new List(), new Opt(new Block((new List()).add(new ExprStmt(new AssignSimpleExpr(this.createAccess(), new VarAccess("value")))).add(new ReturnStmt(new Opt(new VarAccess("value")))))));
         m = fieldQualifier.addMemberMethod(m);
         fieldQualifier.addAccessor(this, "field_write", m);
         return m;
      }
   }

   private Access createAccess() {
      Access fieldAccess = new BoundFieldAccess(this);
      return (Access)(this.isStatic() ? fieldAccess : (new VarAccess("that")).qualifiesAccess(fieldAccess));
   }

   public void jimplify1phase2() {
      String name = this.name();
      Type type = this.type().getSootType();
      int modifiers = this.sootTypeModifiers();
      if (!this.hostType().getSootClassDecl().declaresFieldByName(name)) {
         SootField f = Scene.v().makeSootField(name, type, modifiers);
         this.hostType().getSootClassDecl().addField(f);
         if (this.isStatic() && this.isFinal() && this.isConstant() && (this.type().isPrimitive() || this.type().isString())) {
            if (this.type().isString()) {
               f.addTag(new StringConstantValueTag(this.constant().stringValue()));
            } else if (this.type().isLong()) {
               f.addTag(new LongConstantValueTag(this.constant().longValue()));
            } else if (this.type().isDouble()) {
               f.addTag(new DoubleConstantValueTag(this.constant().doubleValue()));
            } else if (this.type().isFloat()) {
               f.addTag(new FloatConstantValueTag(this.constant().floatValue()));
            } else if (this.type().isIntegralType()) {
               f.addTag(new IntegerConstantValueTag(this.constant().intValue()));
            }
         }

         this.sootField = f;
      } else {
         this.sootField = this.hostType().getSootClassDecl().getFieldByName(name);
      }

      this.addAttributes();
   }

   public void addAttributes() {
      super.addAttributes();
      ArrayList c = new ArrayList();
      this.getModifiers().addRuntimeVisibleAnnotationsAttribute(c);
      this.getModifiers().addRuntimeInvisibleAnnotationsAttribute(c);
      this.getModifiers().addSourceOnlyAnnotations(c);
      Iterator iter = c.iterator();

      while(iter.hasNext()) {
         Tag tag = (Tag)iter.next();
         this.sootField.addTag(tag);
      }

   }

   public void checkWarnings() {
      if (this.hasInit() && !this.suppressWarnings("unchecked")) {
         this.checkUncheckedConversion(this.getInit().type(), this.type());
      }

   }

   public FieldDeclaration() {
      this.fieldDecl = null;
      this.exceptions_computed = false;
      this.constant_computed = false;
      this.usesTypeVariable_computed = false;
      this.sourceVariableDecl_computed = false;
      this.sootRef_computed = false;
      this.throwTypes_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 2);
   }

   public FieldDeclaration(Modifiers p0, Access p1, String p2, Opt<Expr> p3) {
      this.fieldDecl = null;
      this.exceptions_computed = false;
      this.constant_computed = false;
      this.usesTypeVariable_computed = false;
      this.sourceVariableDecl_computed = false;
      this.sootRef_computed = false;
      this.throwTypes_computed = false;
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
   }

   public FieldDeclaration(Modifiers p0, Access p1, Symbol p2, Opt<Expr> p3) {
      this.fieldDecl = null;
      this.exceptions_computed = false;
      this.constant_computed = false;
      this.usesTypeVariable_computed = false;
      this.sourceVariableDecl_computed = false;
      this.sootRef_computed = false;
      this.throwTypes_computed = false;
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
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

   public void setInitOpt(Opt<Expr> opt) {
      this.setChild(opt, 2);
   }

   public boolean hasInit() {
      return this.getInitOpt().getNumChild() != 0;
   }

   public Expr getInit() {
      return (Expr)this.getInitOpt().getChild(0);
   }

   public void setInit(Expr node) {
      this.getInitOpt().setChild(node, 0);
   }

   public Opt<Expr> getInitOpt() {
      return (Opt)this.getChild(2);
   }

   public Opt<Expr> getInitOptNoTransform() {
      return (Opt)this.getChildNoTransform(2);
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return this.isFinal() && this.hasInit() && this.getInit().isConstant() && (this.type() instanceof PrimitiveType || this.type().isString());
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

   public Collection exceptions() {
      if (this.exceptions_computed) {
         return this.exceptions_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.exceptions_value = this.exceptions_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.exceptions_computed = true;
         }

         return this.exceptions_value;
      }
   }

   private Collection exceptions_compute() {
      HashSet set = new HashSet();
      if (this.isInstanceVariable() && this.hasInit()) {
         this.collectExceptions(set, this);
         Iterator iter = set.iterator();

         while(iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)iter.next();
            if (!this.getInit().reachedException(typeDecl)) {
               iter.remove();
            }
         }
      }

      return set;
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
      if (v == this) {
         return this.hasInit();
      } else {
         return this.hasInit() ? this.getInit().isDAafter(v) : this.isDAbefore(v);
      }
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
      if (v == this) {
         return !this.hasInit();
      } else {
         return this.hasInit() ? this.getInit().isDUafter(v) : this.isDUbefore(v);
      }
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

   public boolean isStatic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isStatic() || this.hostType().isInterfaceDecl();
   }

   public boolean isFinal() {
      ASTNode$State state = this.state();
      return this.getModifiers().isFinal() || this.hostType().isInterfaceDecl();
   }

   public boolean isTransient() {
      ASTNode$State state = this.state();
      return this.getModifiers().isTransient();
   }

   public boolean isVolatile() {
      ASTNode$State state = this.state();
      return this.getModifiers().isVolatile();
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getID() + "]";
   }

   public TypeDecl type() {
      ASTNode$State state = this.state();
      return this.getTypeAccess().type();
   }

   public boolean isVoid() {
      ASTNode$State state = this.state();
      return this.type().isVoid();
   }

   public boolean isParameter() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isClassVariable() {
      ASTNode$State state = this.state();
      return this.isStatic() || this.hostType().isInterfaceDecl();
   }

   public boolean isInstanceVariable() {
      ASTNode$State state = this.state();
      return (this.hostType().isClassDecl() || this.hostType().isAnonymous()) && !this.isStatic();
   }

   public boolean isMethodParameter() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isConstructorParameter() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isExceptionHandlerParameter() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isLocalVariable() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isBlank() {
      ASTNode$State state = this.state();
      return !this.hasInit();
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public Constant constant() {
      if (this.constant_computed) {
         return this.constant_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.constant_value = this.constant_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.constant_computed = true;
         }

         return this.constant_value;
      }
   }

   private Constant constant_compute() {
      return this.type().cast(this.getInit().constant());
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
      return this.getTypeAccess().usesTypeVariable();
   }

   public Variable sourceVariableDecl() {
      if (this.sourceVariableDecl_computed) {
         return this.sourceVariableDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sourceVariableDecl_value = this.sourceVariableDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sourceVariableDecl_computed = true;
         }

         return this.sourceVariableDecl_value;
      }
   }

   private Variable sourceVariableDecl_compute() {
      return this;
   }

   public boolean visibleTypeParameters() {
      ASTNode$State state = this.state();
      return !this.isStatic();
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

      if (this.isFinal()) {
         result |= 16;
      }

      if (this.isStatic()) {
         result |= 8;
      }

      return result;
   }

   public SootFieldRef sootRef() {
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

   private SootFieldRef sootRef_compute() {
      return Scene.v().makeFieldRef(this.hostType().getSootClassDecl(), this.name(), this.type().getSootType(), this.isStatic());
   }

   public FieldDeclaration erasedField() {
      ASTNode$State state = this.state();
      return this;
   }

   public Collection<TypeDecl> throwTypes() {
      if (this.throwTypes_computed) {
         return this.throwTypes_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.throwTypes_value = this.throwTypes_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.throwTypes_computed = true;
         }

         return this.throwTypes_value;
      }
   }

   private Collection<TypeDecl> throwTypes_compute() {
      Collection<TypeDecl> tts = new LinkedList();
      tts.add(this.type());
      return tts;
   }

   public boolean hasAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.getModifiers().hasAnnotationSafeVarargs();
   }

   public boolean suppressWarnings(String type) {
      ASTNode$State state = this.state();
      return this.hasAnnotationSuppressWarnings(type) || this.withinSuppressWarnings(type);
   }

   public boolean handlesException(TypeDecl exceptionType) {
      ASTNode$State state = this.state();
      boolean handlesException_TypeDecl_value = this.getParent().Define_boolean_handlesException(this, (ASTNode)null, exceptionType);
      return handlesException_TypeDecl_value;
   }

   public boolean withinSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      boolean withinSuppressWarnings_String_value = this.getParent().Define_boolean_withinSuppressWarnings(this, (ASTNode)null, s);
      return withinSuppressWarnings_String_value;
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getInitOptNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller == this.getInitOptNoTransform()) {
         if (this.hostType().isAnonymous()) {
            return true;
         } else if (!exceptionType.isUncheckedException()) {
            return true;
         } else {
            Iterator iter = this.hostType().constructors().iterator();

            ConstructorDecl decl;
            do {
               if (!iter.hasNext()) {
                  return true;
               }

               decl = (ConstructorDecl)iter.next();
            } while(decl.throwsException(exceptionType));

            return false;
         }
      } else {
         return this.getParent().Define_boolean_handlesException(this, caller, exceptionType);
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

   public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeStatic(this, caller);
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeFinal(this, caller);
   }

   public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeTransient(this, caller);
   }

   public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeVolatile(this, caller);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? this.type() : this.getParent().Define_TypeDecl_declType(this, caller);
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      if (caller != this.getInitOptNoTransform()) {
         return this.getParent().Define_boolean_inStaticContext(this, caller);
      } else {
         return this.isStatic() || this.hostType().isInterfaceDecl();
      }
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      return caller == this.getModifiersNoTransform() ? name.equals("FIELD") : this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
   }

   public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? this.type() : this.getParent().Define_TypeDecl_assignConvertedType(this, caller);
   }

   public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? this.type().componentType() : this.getParent().Define_TypeDecl_expectedType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
