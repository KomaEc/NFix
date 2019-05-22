package soot.JastAddJ;

public class ResourceModifiers extends Modifiers implements Cloneable {
   protected boolean isFinal_computed = false;
   protected boolean isFinal_value;

   public void flushCache() {
      super.flushCache();
      this.isFinal_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ResourceModifiers clone() throws CloneNotSupportedException {
      ResourceModifiers node = (ResourceModifiers)super.clone();
      node.isFinal_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ResourceModifiers copy() {
      try {
         ResourceModifiers node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ResourceModifiers fullCopy() {
      ResourceModifiers tree = this.copy();
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

   public ResourceModifiers() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public ResourceModifiers(List<Modifier> p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setModifierList(List<Modifier> list) {
      this.setChild(list, 0);
   }

   public int getNumModifier() {
      return this.getModifierList().getNumChild();
   }

   public int getNumModifierNoTransform() {
      return this.getModifierListNoTransform().getNumChildNoTransform();
   }

   public Modifier getModifier(int i) {
      return (Modifier)this.getModifierList().getChild(i);
   }

   public void addModifier(Modifier node) {
      List<Modifier> list = this.parent != null && state != null ? this.getModifierList() : this.getModifierListNoTransform();
      list.addChild(node);
   }

   public void addModifierNoTransform(Modifier node) {
      List<Modifier> list = this.getModifierListNoTransform();
      list.addChild(node);
   }

   public void setModifier(Modifier node, int i) {
      List<Modifier> list = this.getModifierList();
      list.setChild(node, i);
   }

   public List<Modifier> getModifiers() {
      return this.getModifierList();
   }

   public List<Modifier> getModifiersNoTransform() {
      return this.getModifierListNoTransform();
   }

   public List<Modifier> getModifierList() {
      List<Modifier> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<Modifier> getModifierListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public boolean isFinal() {
      if (this.isFinal_computed) {
         return this.isFinal_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isFinal_value = this.isFinal_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isFinal_computed = true;
         }

         return this.isFinal_value;
      }
   }

   private boolean isFinal_compute() {
      return true;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
