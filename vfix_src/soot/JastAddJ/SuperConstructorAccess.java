package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.jimple.NullConstant;

public class SuperConstructorAccess extends ConstructorAccess implements Cloneable {
   protected boolean decls_computed = false;
   protected SimpleSet decls_value;

   public void flushCache() {
      super.flushCache();
      this.decls_computed = false;
      this.decls_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public SuperConstructorAccess clone() throws CloneNotSupportedException {
      SuperConstructorAccess node = (SuperConstructorAccess)super.clone();
      node.decls_computed = false;
      node.decls_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public SuperConstructorAccess copy() {
      try {
         SuperConstructorAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public SuperConstructorAccess fullCopy() {
      SuperConstructorAccess tree = this.copy();
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

   public void nameCheck() {
      super.nameCheck();
      TypeDecl c = this.hostType();
      TypeDecl s = c.isClassDecl() && ((ClassDecl)c).hasSuperclass() ? ((ClassDecl)c).superclass() : this.unknownType();
      if (this.isQualified()) {
         if (((TypeDecl)s).isInnerType() && !((TypeDecl)s).inStaticContext()) {
            if (!this.qualifier().type().instanceOf(((TypeDecl)s).enclosingType())) {
               this.error("The type of this primary expression, " + this.qualifier().type().typeName() + " is not enclosing the super type, " + ((TypeDecl)s).typeName() + ", of " + c.typeName());
            }
         } else {
            this.error("the super type " + ((TypeDecl)s).typeName() + " of " + c.typeName() + " is not an inner class");
         }
      }

      if (!this.isQualified() && ((TypeDecl)s).isInnerType() && !c.isInnerType()) {
         this.error("no enclosing instance for " + ((TypeDecl)s).typeName() + " when accessed in " + this);
      }

      if (((TypeDecl)s).isInnerType() && this.hostType().instanceOf(((TypeDecl)s).enclosingType())) {
         this.error("cannot reference this before supertype constructor has been called");
      }

   }

   public void transformation() {
      this.addEnclosingVariables();
      if (this.decl().isPrivate() && this.decl().hostType() != this.hostType()) {
         this.decl().createAccessor();
      }

      super.transformation();
   }

   public void collectTypesToSignatures(Collection<Type> set) {
      super.collectTypesToSignatures(set);
      this.addDependencyIfNeeded(set, this.decl().erasedConstructor().hostType());
   }

   public SuperConstructorAccess() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public SuperConstructorAccess(String p0, List<Expr> p1) {
      this.setID(p0);
      this.setChild(p1, 0);
   }

   public SuperConstructorAccess(Symbol p0, List<Expr> p1) {
      this.setID(p0);
      this.setChild(p1, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
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

   public void setArgList(List<Expr> list) {
      this.setChild(list, 0);
   }

   public int getNumArg() {
      return this.getArgList().getNumChild();
   }

   public int getNumArgNoTransform() {
      return this.getArgListNoTransform().getNumChildNoTransform();
   }

   public Expr getArg(int i) {
      return (Expr)this.getArgList().getChild(i);
   }

   public void addArg(Expr node) {
      List<Expr> list = this.parent != null && state != null ? this.getArgList() : this.getArgListNoTransform();
      list.addChild(node);
   }

   public void addArgNoTransform(Expr node) {
      List<Expr> list = this.getArgListNoTransform();
      list.addChild(node);
   }

   public void setArg(Expr node, int i) {
      List<Expr> list = this.getArgList();
      list.setChild(node, i);
   }

   public List<Expr> getArgs() {
      return this.getArgList();
   }

   public List<Expr> getArgsNoTransform() {
      return this.getArgListNoTransform();
   }

   public List<Expr> getArgList() {
      List<Expr> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<Expr> getArgListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public Value eval(Body b) {
      ConstructorDecl c = this.decl().erasedConstructor();
      Local base = b.emitThis(this.hostType());
      int index = false;
      ArrayList list = new ArrayList();
      if (c.needsEnclosing()) {
         if (this.hasPrevExpr() && !this.prevExpr().isTypeAccess()) {
            list.add(this.asImmediate(b, this.prevExpr().eval(b)));
         } else if (this.hostType().needsSuperEnclosing()) {
            Type type = ((ClassDecl)this.hostType()).superclass().enclosingType().getSootType();
            if (this.hostType().needsEnclosing()) {
               list.add(this.asImmediate(b, b.newParameterRef(type, 1, this)));
            } else {
               list.add(this.asImmediate(b, b.newParameterRef(type, 0, this)));
            }
         } else {
            list.add(this.emitThis(b, this.superConstructorQualifier(c.hostType().enclosingType())));
         }
      }

      for(int i = 0; i < this.getNumArg(); ++i) {
         list.add(this.asImmediate(b, this.getArg(i).type().emitCastTo(b, this.getArg(i), c.getParameter(i).type())));
      }

      if (this.decl().isPrivate() && this.decl().hostType() != this.hostType()) {
         list.add(this.asImmediate(b, NullConstant.v()));
         b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(base, this.decl().erasedConstructor().createAccessor().sootRef(), (java.util.List)list, this), this));
         return base;
      } else {
         return b.newSpecialInvokeExpr(base, c.sootRef(), (java.util.List)list, this);
      }
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAbefore(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUbefore(v);
   }

   public SimpleSet decls() {
      if (this.decls_computed) {
         return this.decls_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.decls_value = this.decls_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.decls_computed = true;
         }

         return this.decls_value;
      }
   }

   private SimpleSet decls_compute() {
      Collection c = this.hasPrevExpr() && !this.prevExpr().isTypeAccess() ? this.hostType().lookupSuperConstructor() : this.lookupSuperConstructor();
      return this.chooseConstructor(c, this.getArgList());
   }

   public String name() {
      ASTNode$State state = this.state();
      return "super";
   }

   public boolean isSuperConstructorAccess() {
      ASTNode$State state = this.state();
      return true;
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.EXPRESSION_NAME;
   }

   public Collection lookupSuperConstructor() {
      ASTNode$State state = this.state();
      Collection lookupSuperConstructor_value = this.getParent().Define_Collection_lookupSuperConstructor(this, (ASTNode)null);
      return lookupSuperConstructor_value;
   }

   public TypeDecl enclosingInstance() {
      ASTNode$State state = this.state();
      TypeDecl enclosingInstance_value = this.getParent().Define_TypeDecl_enclosingInstance(this, (ASTNode)null);
      return enclosingInstance_value;
   }

   public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().hasPackage(packageName);
      } else {
         return super.Define_boolean_hasPackage(caller, child, packageName);
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().lookupVariable(name);
      } else {
         return super.Define_SimpleSet_lookupVariable(caller, child, name);
      }
   }

   public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return super.Define_boolean_inExplicitConstructorInvocation(caller, child);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
