package soot.JastAddJ;

public class FieldDecl extends MemberDecl implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public FieldDecl clone() throws CloneNotSupportedException {
      FieldDecl node = (FieldDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public FieldDecl copy() {
      try {
         FieldDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public FieldDecl fullCopy() {
      FieldDecl tree = this.copy();
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

   public FieldDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 2);
   }

   public FieldDecl(Modifiers p0, Access p1, List<VariableDecl> p2) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setChild(p2, 2);
   }

   protected int numChildren() {
      return 3;
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

   public void setTypeAccess(Access node) {
      this.setChild(node, 1);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(1);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(1);
   }

   public void setVariableDeclList(List<VariableDecl> list) {
      this.setChild(list, 2);
   }

   public int getNumVariableDecl() {
      return this.getVariableDeclList().getNumChild();
   }

   public int getNumVariableDeclNoTransform() {
      return this.getVariableDeclListNoTransform().getNumChildNoTransform();
   }

   public VariableDecl getVariableDecl(int i) {
      return (VariableDecl)this.getVariableDeclList().getChild(i);
   }

   public void addVariableDecl(VariableDecl node) {
      List<VariableDecl> list = this.parent != null && state != null ? this.getVariableDeclList() : this.getVariableDeclListNoTransform();
      list.addChild(node);
   }

   public void addVariableDeclNoTransform(VariableDecl node) {
      List<VariableDecl> list = this.getVariableDeclListNoTransform();
      list.addChild(node);
   }

   public void setVariableDecl(VariableDecl node, int i) {
      List<VariableDecl> list = this.getVariableDeclList();
      list.setChild(node, i);
   }

   public List<VariableDecl> getVariableDecls() {
      return this.getVariableDeclList();
   }

   public List<VariableDecl> getVariableDeclsNoTransform() {
      return this.getVariableDeclListNoTransform();
   }

   public List<VariableDecl> getVariableDeclList() {
      List<VariableDecl> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<VariableDecl> getVariableDeclListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public boolean isStatic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isStatic();
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
      if (caller == this.getVariableDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return null;
      } else {
         return this.getParent().Define_TypeDecl_declType(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      if (this.getNumVariableDecl() == 1) {
         ++this.state().duringVariableDeclarationTransformation;
         ASTNode result = this.rewriteRule0();
         --this.state().duringVariableDeclarationTransformation;
         return result;
      } else if (this.getParent().getParent() instanceof TypeDecl && ((TypeDecl)this.getParent().getParent()).getBodyDeclListNoTransform() == this.getParent() && this.getNumVariableDecl() > 1) {
         ++this.state().duringVariableDeclarationTransformation;
         List list = (List)this.getParent();
         int i = list.getIndexOfChild(this);
         List newList = this.rewriteTypeDecl_getBodyDecl();

         for(int j = 1; j < newList.getNumChildNoTransform(); ++j) {
            ASTNode var10001 = newList.getChildNoTransform(j);
            ++i;
            list.insertChild(var10001, i);
         }

         --this.state().duringVariableDeclarationTransformation;
         return newList.getChildNoTransform(0);
      } else {
         return super.rewriteTo();
      }
   }

   private FieldDeclaration rewriteRule0() {
      FieldDeclaration decl = this.getVariableDecl(0).createFieldDeclarationFrom(this.getModifiers(), this.getTypeAccess());
      decl.setStart(this.start);
      decl.setEnd(this.end);
      return decl;
   }

   private List rewriteTypeDecl_getBodyDecl() {
      List varList = new List();

      for(int j = 0; j < this.getNumVariableDecl(); ++j) {
         FieldDeclaration f = this.getVariableDecl(j).createFieldDeclarationFrom(this.getModifiers().fullCopy(), (Access)this.getTypeAccess().fullCopy());
         if (j == 0) {
            f.setStart(this.start);
         } else {
            f.getModifiersNoTransform().clearLocations();
            f.getTypeAccessNoTransform().clearLocations();
         }

         f.setFieldDecl(this);
         varList.add(f);
      }

      return varList;
   }
}
