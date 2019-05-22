package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParMethodAccess extends MethodAccess implements Cloneable {
   protected Map typeArguments_MethodDecl_values;

   public void flushCache() {
      super.flushCache();
      this.typeArguments_MethodDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ParMethodAccess clone() throws CloneNotSupportedException {
      ParMethodAccess node = (ParMethodAccess)super.clone();
      node.typeArguments_MethodDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParMethodAccess copy() {
      try {
         ParMethodAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParMethodAccess fullCopy() {
      ParMethodAccess tree = this.copy();
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

   public void typeCheck() {
      super.typeCheck();
      if (!this.decl().hostType().isUnknown()) {
         if (!(this.decl() instanceof ParMethodDecl)) {
            this.error("can not have type parameters on a non generic method");
         } else {
            ParMethodDecl m = (ParMethodDecl)this.decl();
            if (!(m instanceof RawMethodDecl) && m.numTypeParameter() != this.getNumTypeArgument()) {
               this.error("generic method " + m.signature() + " requires " + m.numTypeParameter() + " type arguments");
            }
         }
      }

   }

   public void toString(StringBuffer s) {
      s.append("<");

      for(int i = 0; i < this.getNumTypeArgument(); ++i) {
         if (i != 0) {
            s.append(", ");
         }

         this.getTypeArgument(i).toString(s);
      }

      s.append(">");
      super.toString(s);
   }

   public ParMethodAccess() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 0);
      this.setChild(new List(), 1);
   }

   public ParMethodAccess(String p0, List<Expr> p1, List<Access> p2) {
      this.setID(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   public ParMethodAccess(Symbol p0, List<Expr> p1, List<Access> p2) {
      this.setID(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   protected int numChildren() {
      return 2;
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

   public void setTypeArgumentList(List<Access> list) {
      this.setChild(list, 1);
   }

   public int getNumTypeArgument() {
      return this.getTypeArgumentList().getNumChild();
   }

   public int getNumTypeArgumentNoTransform() {
      return this.getTypeArgumentListNoTransform().getNumChildNoTransform();
   }

   public Access getTypeArgument(int i) {
      return (Access)this.getTypeArgumentList().getChild(i);
   }

   public void addTypeArgument(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getTypeArgumentList() : this.getTypeArgumentListNoTransform();
      list.addChild(node);
   }

   public void addTypeArgumentNoTransform(Access node) {
      List<Access> list = this.getTypeArgumentListNoTransform();
      list.addChild(node);
   }

   public void setTypeArgument(Access node, int i) {
      List<Access> list = this.getTypeArgumentList();
      list.setChild(node, i);
   }

   public List<Access> getTypeArguments() {
      return this.getTypeArgumentList();
   }

   public List<Access> getTypeArgumentsNoTransform() {
      return this.getTypeArgumentListNoTransform();
   }

   public List<Access> getTypeArgumentList() {
      List<Access> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<Access> getTypeArgumentListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public ArrayList typeArguments(MethodDecl m) {
      if (this.typeArguments_MethodDecl_values == null) {
         this.typeArguments_MethodDecl_values = new HashMap(4);
      }

      if (this.typeArguments_MethodDecl_values.containsKey(m)) {
         return (ArrayList)this.typeArguments_MethodDecl_values.get(m);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         ArrayList typeArguments_MethodDecl_value = this.typeArguments_compute(m);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeArguments_MethodDecl_values.put(m, typeArguments_MethodDecl_value);
         }

         return typeArguments_MethodDecl_value;
      }
   }

   private ArrayList typeArguments_compute(MethodDecl m) {
      ArrayList typeArguments = new ArrayList();

      for(int i = 0; i < this.getNumTypeArgument(); ++i) {
         typeArguments.add(this.getTypeArgument(i).type());
      }

      return typeArguments;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeArgumentListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return super.Define_NameType_nameType(caller, child);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getTypeArgumentListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().lookupType(name);
      } else {
         return super.Define_SimpleSet_lookupType(caller, child, name);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
