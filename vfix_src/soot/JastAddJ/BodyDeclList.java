package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BodyDeclList extends List implements Cloneable {
   protected Map localMethodSignatureCopy_MethodDecl_MemberSubstitutor_values;
   protected List localMethodSignatureCopy_MethodDecl_MemberSubstitutor_list;
   protected Map localFieldCopy_FieldDeclaration_MemberSubstitutor_values;
   protected List localFieldCopy_FieldDeclaration_MemberSubstitutor_list;
   protected Map localClassDeclCopy_ClassDecl_MemberSubstitutor_values;
   protected List localClassDeclCopy_ClassDecl_MemberSubstitutor_list;
   protected Map localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_values;
   protected List localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_list;
   protected Map constructorCopy_ConstructorDecl_MemberSubstitutor_values;
   protected List constructorCopy_ConstructorDecl_MemberSubstitutor_list;

   public void flushCache() {
      super.flushCache();
      this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_values = null;
      this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_list = null;
      this.localFieldCopy_FieldDeclaration_MemberSubstitutor_values = null;
      this.localFieldCopy_FieldDeclaration_MemberSubstitutor_list = null;
      this.localClassDeclCopy_ClassDecl_MemberSubstitutor_values = null;
      this.localClassDeclCopy_ClassDecl_MemberSubstitutor_list = null;
      this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_values = null;
      this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_list = null;
      this.constructorCopy_ConstructorDecl_MemberSubstitutor_values = null;
      this.constructorCopy_ConstructorDecl_MemberSubstitutor_list = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public BodyDeclList clone() throws CloneNotSupportedException {
      BodyDeclList node = (BodyDeclList)super.clone();
      node.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_values = null;
      node.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_list = null;
      node.localFieldCopy_FieldDeclaration_MemberSubstitutor_values = null;
      node.localFieldCopy_FieldDeclaration_MemberSubstitutor_list = null;
      node.localClassDeclCopy_ClassDecl_MemberSubstitutor_values = null;
      node.localClassDeclCopy_ClassDecl_MemberSubstitutor_list = null;
      node.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_values = null;
      node.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_list = null;
      node.constructorCopy_ConstructorDecl_MemberSubstitutor_values = null;
      node.constructorCopy_ConstructorDecl_MemberSubstitutor_list = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public BodyDeclList copy() {
      try {
         BodyDeclList node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public BodyDeclList fullCopy() {
      BodyDeclList tree = this.copy();
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

   public BodyDeclList() {
      this.is$Final(true);
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return true;
   }

   public BodyDecl localMethodSignatureCopy(MethodDecl originalMethod, MemberSubstitutor m) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(originalMethod);
      _parameters.add(m);
      if (this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_values == null) {
         this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_values = new HashMap(4);
      }

      if (this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_values.containsKey(_parameters)) {
         return (BodyDecl)this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         BodyDecl localMethodSignatureCopy_MethodDecl_MemberSubstitutor_value = this.localMethodSignatureCopy_compute(originalMethod, m);
         if (this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_list == null) {
            this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_list = new List();
            this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_list.is$Final = true;
            this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_list.setParent(this);
         }

         this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_list.add(localMethodSignatureCopy_MethodDecl_MemberSubstitutor_value);
         if (localMethodSignatureCopy_MethodDecl_MemberSubstitutor_value != null) {
            localMethodSignatureCopy_MethodDecl_MemberSubstitutor_value.is$Final = true;
         }

         this.localMethodSignatureCopy_MethodDecl_MemberSubstitutor_values.put(_parameters, localMethodSignatureCopy_MethodDecl_MemberSubstitutor_value);
         return localMethodSignatureCopy_MethodDecl_MemberSubstitutor_value;
      }
   }

   private BodyDecl localMethodSignatureCopy_compute(MethodDecl originalMethod, MemberSubstitutor m) {
      return originalMethod.substitutedBodyDecl(m);
   }

   public BodyDecl localFieldCopy(FieldDeclaration originalDecl, MemberSubstitutor m) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(originalDecl);
      _parameters.add(m);
      if (this.localFieldCopy_FieldDeclaration_MemberSubstitutor_values == null) {
         this.localFieldCopy_FieldDeclaration_MemberSubstitutor_values = new HashMap(4);
      }

      if (this.localFieldCopy_FieldDeclaration_MemberSubstitutor_values.containsKey(_parameters)) {
         return (BodyDecl)this.localFieldCopy_FieldDeclaration_MemberSubstitutor_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         BodyDecl localFieldCopy_FieldDeclaration_MemberSubstitutor_value = this.localFieldCopy_compute(originalDecl, m);
         if (this.localFieldCopy_FieldDeclaration_MemberSubstitutor_list == null) {
            this.localFieldCopy_FieldDeclaration_MemberSubstitutor_list = new List();
            this.localFieldCopy_FieldDeclaration_MemberSubstitutor_list.is$Final = true;
            this.localFieldCopy_FieldDeclaration_MemberSubstitutor_list.setParent(this);
         }

         this.localFieldCopy_FieldDeclaration_MemberSubstitutor_list.add(localFieldCopy_FieldDeclaration_MemberSubstitutor_value);
         if (localFieldCopy_FieldDeclaration_MemberSubstitutor_value != null) {
            localFieldCopy_FieldDeclaration_MemberSubstitutor_value.is$Final = true;
         }

         this.localFieldCopy_FieldDeclaration_MemberSubstitutor_values.put(_parameters, localFieldCopy_FieldDeclaration_MemberSubstitutor_value);
         return localFieldCopy_FieldDeclaration_MemberSubstitutor_value;
      }
   }

   private BodyDecl localFieldCopy_compute(FieldDeclaration originalDecl, MemberSubstitutor m) {
      return originalDecl.substitutedBodyDecl(m);
   }

   public MemberClassDecl localClassDeclCopy(ClassDecl originalDecl, MemberSubstitutor m) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(originalDecl);
      _parameters.add(m);
      if (this.localClassDeclCopy_ClassDecl_MemberSubstitutor_values == null) {
         this.localClassDeclCopy_ClassDecl_MemberSubstitutor_values = new HashMap(4);
      }

      if (this.localClassDeclCopy_ClassDecl_MemberSubstitutor_values.containsKey(_parameters)) {
         return (MemberClassDecl)this.localClassDeclCopy_ClassDecl_MemberSubstitutor_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         MemberClassDecl localClassDeclCopy_ClassDecl_MemberSubstitutor_value = this.localClassDeclCopy_compute(originalDecl, m);
         if (this.localClassDeclCopy_ClassDecl_MemberSubstitutor_list == null) {
            this.localClassDeclCopy_ClassDecl_MemberSubstitutor_list = new List();
            this.localClassDeclCopy_ClassDecl_MemberSubstitutor_list.is$Final = true;
            this.localClassDeclCopy_ClassDecl_MemberSubstitutor_list.setParent(this);
         }

         this.localClassDeclCopy_ClassDecl_MemberSubstitutor_list.add(localClassDeclCopy_ClassDecl_MemberSubstitutor_value);
         if (localClassDeclCopy_ClassDecl_MemberSubstitutor_value != null) {
            localClassDeclCopy_ClassDecl_MemberSubstitutor_value.is$Final = true;
         }

         this.localClassDeclCopy_ClassDecl_MemberSubstitutor_values.put(_parameters, localClassDeclCopy_ClassDecl_MemberSubstitutor_value);
         return localClassDeclCopy_ClassDecl_MemberSubstitutor_value;
      }
   }

   private MemberClassDecl localClassDeclCopy_compute(ClassDecl originalDecl, MemberSubstitutor m) {
      ClassDecl copy = originalDecl.substitutedClassDecl(m);
      return new MemberClassDecl(copy);
   }

   public MemberInterfaceDecl localInterfaceDeclCopy(InterfaceDecl originalDecl, MemberSubstitutor m) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(originalDecl);
      _parameters.add(m);
      if (this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_values == null) {
         this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_values = new HashMap(4);
      }

      if (this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_values.containsKey(_parameters)) {
         return (MemberInterfaceDecl)this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         MemberInterfaceDecl localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_value = this.localInterfaceDeclCopy_compute(originalDecl, m);
         if (this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_list == null) {
            this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_list = new List();
            this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_list.is$Final = true;
            this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_list.setParent(this);
         }

         this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_list.add(localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_value);
         if (localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_value != null) {
            localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_value.is$Final = true;
         }

         this.localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_values.put(_parameters, localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_value);
         return localInterfaceDeclCopy_InterfaceDecl_MemberSubstitutor_value;
      }
   }

   private MemberInterfaceDecl localInterfaceDeclCopy_compute(InterfaceDecl originalDecl, MemberSubstitutor m) {
      InterfaceDecl copy = originalDecl.substitutedInterfaceDecl(m);
      return new MemberInterfaceDecl(copy);
   }

   public BodyDecl constructorCopy(ConstructorDecl originalDecl, MemberSubstitutor m) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(originalDecl);
      _parameters.add(m);
      if (this.constructorCopy_ConstructorDecl_MemberSubstitutor_values == null) {
         this.constructorCopy_ConstructorDecl_MemberSubstitutor_values = new HashMap(4);
      }

      if (this.constructorCopy_ConstructorDecl_MemberSubstitutor_values.containsKey(_parameters)) {
         return (BodyDecl)this.constructorCopy_ConstructorDecl_MemberSubstitutor_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         BodyDecl constructorCopy_ConstructorDecl_MemberSubstitutor_value = this.constructorCopy_compute(originalDecl, m);
         if (this.constructorCopy_ConstructorDecl_MemberSubstitutor_list == null) {
            this.constructorCopy_ConstructorDecl_MemberSubstitutor_list = new List();
            this.constructorCopy_ConstructorDecl_MemberSubstitutor_list.is$Final = true;
            this.constructorCopy_ConstructorDecl_MemberSubstitutor_list.setParent(this);
         }

         this.constructorCopy_ConstructorDecl_MemberSubstitutor_list.add(constructorCopy_ConstructorDecl_MemberSubstitutor_value);
         if (constructorCopy_ConstructorDecl_MemberSubstitutor_value != null) {
            constructorCopy_ConstructorDecl_MemberSubstitutor_value.is$Final = true;
         }

         this.constructorCopy_ConstructorDecl_MemberSubstitutor_values.put(_parameters, constructorCopy_ConstructorDecl_MemberSubstitutor_value);
         return constructorCopy_ConstructorDecl_MemberSubstitutor_value;
      }
   }

   private BodyDecl constructorCopy_compute(ConstructorDecl originalDecl, MemberSubstitutor m) {
      return originalDecl.substitutedBodyDecl(m);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
