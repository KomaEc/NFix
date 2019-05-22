package soot.JastAddJ;

public class ConstCase extends Case implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ConstCase clone() throws CloneNotSupportedException {
      ConstCase node = (ConstCase)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ConstCase copy() {
      try {
         ConstCase node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ConstCase fullCopy() {
      ConstCase tree = this.copy();
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
      if (this.getValue().isConstant() && this.bind(this) != this) {
         this.error("constant expression " + this.getValue() + " is multiply declared in two case statements");
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("case ");
      this.getValue().toString(s);
      s.append(":");
   }

   public void transformation() {
      if (this.getValue() instanceof VarAccess && this.getValue().varDecl() instanceof EnumConstant) {
         int i = this.hostType().createEnumIndex((EnumConstant)this.getValue().varDecl());
         this.setValue(new IntegerLiteral((new Integer(i)).toString()));
      }

      super.transformation();
   }

   public ConstCase() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ConstCase(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setValue(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getValue() {
      return (Expr)this.getChild(0);
   }

   public Expr getValueNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void refined_Enums_ConstCase_typeCheck() {
      boolean isEnumConstant = this.getValue().isEnumConstant();
      if (this.switchType().isEnumDecl() && !isEnumConstant) {
         this.error("Unqualified enumeration constant required");
      } else {
         TypeDecl switchType = this.switchType();
         TypeDecl type = this.getValue().type();
         if (!type.assignConversionTo(switchType, this.getValue())) {
            this.error("Constant expression must be assignable to Expression");
         }

         if (!this.getValue().isConstant() && !this.getValue().type().isUnknown() && !isEnumConstant) {
            this.error("Switch expression must be constant");
         }
      }

   }

   public void typeCheck() {
      boolean isEnumConstant = this.getValue().isEnumConstant();
      TypeDecl switchType = this.switchType();
      TypeDecl type = this.getValue().type();
      if (switchType.isEnumDecl() && !isEnumConstant) {
         this.error("Unqualified enumeration constant required");
      }

      if (!type.assignConversionTo(switchType, this.getValue())) {
         this.error("Case label has incompatible type " + switchType.name() + ", expected type compatible with " + type.name());
      }

      if (!this.getValue().isConstant() && !this.getValue().type().isUnknown() && !isEnumConstant) {
         this.error("Case label must have constant expression");
      }

   }

   private boolean refined_NameCheck_ConstCase_constValue_Case(Case c) {
      if (c instanceof ConstCase && this.getValue().isConstant()) {
         if (this.getValue().type().assignableToInt() && ((ConstCase)c).getValue().type().assignableToInt()) {
            return this.getValue().constant().intValue() == ((ConstCase)c).getValue().constant().intValue();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean refined_Enums_ConstCase_constValue_Case(Case c) {
      if (this.switchType().isEnumDecl()) {
         if (c instanceof ConstCase && this.getValue().isConstant()) {
            return this.getValue().varDecl() == ((ConstCase)c).getValue().varDecl();
         } else {
            return false;
         }
      } else {
         return this.refined_NameCheck_ConstCase_constValue_Case(c);
      }
   }

   public boolean constValue(Case c) {
      ASTNode$State state = this.state();
      if (!this.isDefaultCase() && !c.isDefaultCase()) {
         Expr myValue = this.getValue();
         Expr otherValue = ((ConstCase)c).getValue();
         TypeDecl myType = myValue.type();
         TypeDecl otherType = otherValue.type();
         if (!myType.isString() && !otherType.isString()) {
            return this.refined_Enums_ConstCase_constValue_Case(c);
         } else if (myType.isString() && otherType.isString()) {
            return myValue.isConstant() && otherValue.isConstant() ? myValue.constant().stringValue().equals(otherValue.constant().stringValue()) : false;
         } else {
            return false;
         }
      } else {
         return this.isDefaultCase() && c.isDefaultCase();
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getValueNoTransform()) {
         return this.switchType().isEnumDecl() ? this.switchType().memberFields(name) : this.lookupVariable(name);
      } else {
         return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
