package soot.JastAddJ;

public class List<T extends ASTNode> extends ASTNode<T> implements Cloneable {
   private boolean list$touched = true;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public List<T> clone() throws CloneNotSupportedException {
      List node = (List)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public List<T> copy() {
      try {
         List node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public List<T> fullCopy() {
      List tree = this.copy();
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

   public List substitute(Parameterization parTypeDecl) {
      List list = new List();

      for(int i = 0; i < this.getNumChild(); ++i) {
         ASTNode node = this.getChild(i);
         if (node instanceof Access) {
            Access a = (Access)node;
            list.add(a.type().substitute(parTypeDecl));
         } else if (node instanceof VariableArityParameterDeclaration) {
            VariableArityParameterDeclaration p = (VariableArityParameterDeclaration)node;
            list.add(new VariableArityParameterDeclarationSubstituted(p.getModifiers().fullCopy(), p.getTypeAccess().type().substituteParameterType(parTypeDecl), p.getID(), p));
         } else {
            if (!(node instanceof ParameterDeclaration)) {
               throw new Error("Can only substitute lists of access nodes but node number " + i + " is of type " + node.getClass().getName());
            }

            ParameterDeclaration p = (ParameterDeclaration)node;
            list.add(new ParameterDeclarationSubstituted(p.getModifiers().fullCopy(), p.type().substituteParameterType(parTypeDecl), p.getID(), p));
         }
      }

      return list;
   }

   public void init$Children() {
   }

   public List<T> add(T node) {
      this.addChild(node);
      return this;
   }

   public void insertChild(ASTNode node, int i) {
      this.list$touched = true;
      super.insertChild(node, i);
   }

   public void addChild(T node) {
      this.list$touched = true;
      super.addChild(node);
   }

   public void removeChild(int i) {
      this.list$touched = true;
      super.removeChild(i);
   }

   public int getNumChild() {
      if (this.list$touched) {
         for(int i = 0; i < this.getNumChildNoTransform(); ++i) {
            this.getChild(i);
         }

         this.list$touched = false;
      }

      return this.getNumChildNoTransform();
   }

   public boolean mayHaveRewrite() {
      return true;
   }

   public boolean requiresDefaultConstructor() {
      ASTNode$State state = this.state();
      if (!(this.getParent() instanceof ClassDecl)) {
         return false;
      } else {
         ClassDecl c = (ClassDecl)this.getParent();
         return c.noConstructor() && c.getBodyDeclListNoTransform() == this && !(c instanceof AnonymousDecl);
      }
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return this.getParent().definesLabel();
   }

   public ASTNode rewriteTo() {
      if (!this.list$touched) {
         if (this.requiresDefaultConstructor()) {
            ++this.state().duringImplicitConstructor;
            ASTNode result = this.rewriteRule0();
            --this.state().duringImplicitConstructor;
            return result;
         } else {
            return super.rewriteTo();
         }
      } else {
         for(int i = 0; i < this.getNumChildNoTransform(); ++i) {
            this.getChild(i);
         }

         this.list$touched = false;
         return this;
      }
   }

   private List rewriteRule0() {
      ClassDecl c = (ClassDecl)this.getParent();
      Modifiers m = new Modifiers();
      if (c.isPublic()) {
         m.addModifier(new Modifier("public"));
      } else if (c.isProtected()) {
         m.addModifier(new Modifier("protected"));
      } else if (c.isPrivate()) {
         m.addModifier(new Modifier("private"));
      }

      ConstructorDecl constructor = new ConstructorDecl(m, c.name(), new List(), new List(), new Opt(), new Block());
      constructor.setDefaultConstructor();
      c.addBodyDecl(constructor);
      return this;
   }
}
