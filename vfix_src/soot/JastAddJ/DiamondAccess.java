package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class DiamondAccess extends Access implements Cloneable {
   protected boolean type_computed = false;
   protected TypeDecl type_value;
   protected Map typeArguments_MethodDecl_values;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
      this.typeArguments_MethodDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public DiamondAccess clone() throws CloneNotSupportedException {
      DiamondAccess node = (DiamondAccess)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.typeArguments_MethodDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public DiamondAccess copy() {
      try {
         DiamondAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public DiamondAccess fullCopy() {
      DiamondAccess tree = this.copy();
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

   protected static SimpleSet mostSpecific(SimpleSet maxSpecific, MethodDecl decl) {
      if (maxSpecific.isEmpty()) {
         maxSpecific = maxSpecific.add(decl);
      } else if (decl.moreSpecificThan((MethodDecl)maxSpecific.iterator().next())) {
         maxSpecific = SimpleSet.emptySet.add(decl);
      } else if (!((MethodDecl)maxSpecific.iterator().next()).moreSpecificThan(decl)) {
         maxSpecific = maxSpecific.add(decl);
      }

      return maxSpecific;
   }

   protected SimpleSet chooseConstructor() {
      ClassInstanceExpr instanceExpr = this.getClassInstanceExpr();
      TypeDecl type = this.getTypeAccess().type();

      assert instanceExpr != null;

      assert type instanceof ParClassDecl;

      GenericClassDecl genericType = (GenericClassDecl)((ParClassDecl)type).genericDecl();
      List<PlaceholderMethodDecl> placeholderMethods = genericType.getPlaceholderMethodList();
      SimpleSet maxSpecific = SimpleSet.emptySet;
      Collection<MethodDecl> potentiallyApplicable = this.potentiallyApplicable(placeholderMethods);
      Iterator var7 = potentiallyApplicable.iterator();

      while(true) {
         MethodDecl candidate;
         do {
            if (!var7.hasNext()) {
               return maxSpecific;
            }

            candidate = (MethodDecl)var7.next();
         } while(!this.applicableBySubtyping(instanceExpr, candidate) && !this.applicableByMethodInvocationConversion(instanceExpr, candidate) && !this.applicableByVariableArity(instanceExpr, candidate));

         maxSpecific = mostSpecific(maxSpecific, candidate);
      }
   }

   protected Collection<MethodDecl> potentiallyApplicable(List<PlaceholderMethodDecl> candidates) {
      Collection<MethodDecl> potentiallyApplicable = new LinkedList();
      Iterator var3 = candidates.iterator();

      while(var3.hasNext()) {
         GenericMethodDecl candidate = (GenericMethodDecl)var3.next();
         if (this.potentiallyApplicable(candidate)) {
            MethodDecl decl = candidate.lookupParMethodDecl(this.typeArguments(candidate));
            potentiallyApplicable.add(decl);
         }
      }

      return potentiallyApplicable;
   }

   protected boolean potentiallyApplicable(GenericMethodDecl candidate) {
      if (candidate.isVariableArity() && this.getClassInstanceExpr().arity() < candidate.arity() - 1) {
         return false;
      } else if (!candidate.isVariableArity() && this.getClassInstanceExpr().arity() != candidate.arity()) {
         return false;
      } else {
         java.util.List<TypeDecl> typeArgs = this.typeArguments(candidate);
         if (typeArgs.size() != 0) {
            if (candidate.getNumTypeParameter() != typeArgs.size()) {
               return false;
            }

            for(int i = 0; i < candidate.getNumTypeParameter(); ++i) {
               if (!((TypeDecl)typeArgs.get(i)).subtype(candidate.original().getTypeParameter(i))) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public Collection<TypeDecl> computeConstraints(GenericMethodDecl decl) {
      Constraints c = new Constraints();

      for(int i = 0; i < decl.original().getNumTypeParameter(); ++i) {
         c.addTypeVariable(decl.original().getTypeParameter(i));
      }

      ClassInstanceExpr instanceExpr = this.getClassInstanceExpr();

      TypeDecl R;
      for(int i = 0; i < instanceExpr.getNumArg(); ++i) {
         R = instanceExpr.getArg(i).type();
         int index = i >= decl.getNumParameter() ? decl.getNumParameter() - 1 : i;
         TypeDecl F = decl.getParameter(index).type();
         if (decl.getParameter(index) instanceof VariableArityParameterDeclaration && (instanceExpr.getNumArg() != decl.getNumParameter() || !R.isArrayDecl())) {
            F = F.componentType();
         }

         c.convertibleTo(R, F);
      }

      if (c.rawAccess) {
         return new ArrayList();
      } else {
         c.resolveEqualityConstraints();
         c.resolveSupertypeConstraints();
         if (c.unresolvedTypeArguments()) {
            TypeDecl S = this.assignConvertedType();
            if (S.isUnboxedPrimitive()) {
               S = S.boxed();
            }

            R = decl.type();
            if (R.isVoid()) {
               R = this.typeObject();
            }

            c.convertibleFrom(S, R);
            c.resolveEqualityConstraints();
            c.resolveSupertypeConstraints();
            c.resolveSubtypeConstraints();
         }

         return c.typeArguments();
      }
   }

   protected boolean applicableBySubtyping(ClassInstanceExpr expr, MethodDecl method) {
      if (method.getNumParameter() != expr.getNumArg()) {
         return false;
      } else {
         for(int i = 0; i < method.getNumParameter(); ++i) {
            if (!expr.getArg(i).type().instanceOf(method.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   protected boolean applicableByMethodInvocationConversion(ClassInstanceExpr expr, MethodDecl method) {
      if (method.getNumParameter() != expr.getNumArg()) {
         return false;
      } else {
         for(int i = 0; i < method.getNumParameter(); ++i) {
            if (!expr.getArg(i).type().methodInvocationConversionTo(method.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   protected boolean applicableByVariableArity(ClassInstanceExpr expr, MethodDecl method) {
      int i;
      for(i = 0; i < method.getNumParameter() - 1; ++i) {
         if (!expr.getArg(i).type().methodInvocationConversionTo(method.getParameter(i).type())) {
            return false;
         }
      }

      for(i = method.getNumParameter() - 1; i < expr.getNumArg(); ++i) {
         if (!expr.getArg(i).type().methodInvocationConversionTo(method.lastParameter().type().componentType())) {
            return false;
         }
      }

      return true;
   }

   public void typeCheck() {
      if (this.isAnonymousDecl()) {
         this.error("the diamond operator can not be used with anonymous classes");
      }

      if (this.isExplicitGenericConstructorAccess()) {
         this.error("the diamond operator may not be used with generic constructors with explicit type parameters");
      }

      if (this.getClassInstanceExpr() == null) {
         this.error("the diamond operator can only be used in class instance expressions");
      }

      if (!(this.getTypeAccess().type() instanceof ParClassDecl)) {
         this.error("the diamond operator can only be used to instantiate generic classes");
      }

   }

   public void toString(StringBuffer sb) {
      this.getTypeAccess().toString(sb);
      sb.append("<>");
   }

   public DiamondAccess() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public DiamondAccess(Access p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setTypeAccess(Access node) {
      this.setChild(node, 0);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(0);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(0);
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
      TypeDecl accessType = this.getTypeAccess().type();
      if (this.isAnonymousDecl()) {
         return accessType;
      } else if (this.getClassInstanceExpr() == null) {
         return accessType;
      } else if (!(accessType instanceof ParClassDecl)) {
         return accessType;
      } else {
         SimpleSet maxSpecific = this.chooseConstructor();
         if (maxSpecific.isEmpty()) {
            return this.getTypeAccess().type();
         } else {
            MethodDecl constructor = (MethodDecl)maxSpecific.iterator().next();
            return constructor.type();
         }
      }
   }

   public boolean isDiamond() {
      ASTNode$State state = this.state();
      return true;
   }

   public java.util.List<TypeDecl> typeArguments(MethodDecl decl) {
      if (this.typeArguments_MethodDecl_values == null) {
         this.typeArguments_MethodDecl_values = new HashMap(4);
      }

      if (this.typeArguments_MethodDecl_values.containsKey(decl)) {
         return (java.util.List)this.typeArguments_MethodDecl_values.get(decl);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         java.util.List<TypeDecl> typeArguments_MethodDecl_value = this.typeArguments_compute(decl);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeArguments_MethodDecl_values.put(decl, typeArguments_MethodDecl_value);
         }

         return typeArguments_MethodDecl_value;
      }
   }

   private java.util.List<TypeDecl> typeArguments_compute(MethodDecl decl) {
      java.util.List<TypeDecl> typeArguments = new LinkedList();
      if (decl instanceof GenericMethodDecl) {
         GenericMethodDecl method = (GenericMethodDecl)decl;
         Collection<TypeDecl> arguments = this.computeConstraints(method);
         if (arguments.isEmpty()) {
            return typeArguments;
         }

         int i = 0;

         for(Iterator var6 = arguments.iterator(); var6.hasNext(); ++i) {
            TypeDecl argument = (TypeDecl)var6.next();
            if (argument == null) {
               TypeVariable v = method.original().getTypeParameter(i);
               if (v.getNumTypeBound() == 0) {
                  argument = this.typeObject();
               } else if (v.getNumTypeBound() == 1) {
                  argument = v.getTypeBound(0).type();
               } else {
                  argument = v.lubType();
               }
            }

            typeArguments.add(argument);
         }
      }

      return typeArguments;
   }

   public ClassInstanceExpr getClassInstanceExpr() {
      ASTNode$State state = this.state();
      ClassInstanceExpr getClassInstanceExpr_value = this.getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, (ASTNode)null);
      return getClassInstanceExpr_value;
   }

   public TypeDecl typeObject() {
      ASTNode$State state = this.state();
      TypeDecl typeObject_value = this.getParent().Define_TypeDecl_typeObject(this, (ASTNode)null);
      return typeObject_value;
   }

   public boolean isAnonymousDecl() {
      ASTNode$State state = this.state();
      boolean isAnonymousDecl_value = this.getParent().Define_boolean_isAnonymousDecl(this, (ASTNode)null);
      return isAnonymousDecl_value;
   }

   public boolean isExplicitGenericConstructorAccess() {
      ASTNode$State state = this.state();
      boolean isExplicitGenericConstructorAccess_value = this.getParent().Define_boolean_isExplicitGenericConstructorAccess(this, (ASTNode)null);
      return isExplicitGenericConstructorAccess_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
