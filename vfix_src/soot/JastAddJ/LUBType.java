package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.SootClass;

public class LUBType extends ReferenceType implements Cloneable {
   protected boolean lub_computed = false;
   protected TypeDecl lub_value;
   protected Map subtype_TypeDecl_values;
   protected boolean getSootClassDecl_computed = false;
   protected SootClass getSootClassDecl_value;

   public void flushCache() {
      super.flushCache();
      this.lub_computed = false;
      this.lub_value = null;
      this.subtype_TypeDecl_values = null;
      this.getSootClassDecl_computed = false;
      this.getSootClassDecl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public LUBType clone() throws CloneNotSupportedException {
      LUBType node = (LUBType)super.clone();
      node.lub_computed = false;
      node.lub_value = null;
      node.subtype_TypeDecl_values = null;
      node.getSootClassDecl_computed = false;
      node.getSootClassDecl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public LUBType copy() {
      try {
         LUBType node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public LUBType fullCopy() {
      LUBType tree = this.copy();
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

   public static HashSet EC(ArrayList list) {
      HashSet result = new HashSet();
      boolean first = true;
      Iterator iter = list.iterator();

      while(iter.hasNext()) {
         TypeDecl U = (TypeDecl)iter.next();
         HashSet EST = EST(U);
         if (first) {
            result.addAll(EST);
            first = false;
         } else {
            result.retainAll(EST);
         }
      }

      return result;
   }

   public static HashSet MEC(ArrayList list) {
      HashSet EC = EC(list);
      if (EC.size() == 1) {
         return EC;
      } else {
         HashSet MEC = new HashSet();
         Iterator iter = EC.iterator();

         while(iter.hasNext()) {
            TypeDecl V = (TypeDecl)iter.next();
            boolean keep = true;
            Iterator i2 = EC.iterator();

            while(i2.hasNext()) {
               TypeDecl W = (TypeDecl)i2.next();
               if (!(V instanceof TypeVariable) && V != W && W.instanceOf(V)) {
                  keep = false;
               }
            }

            if (keep) {
               MEC.add(V);
            }
         }

         return MEC;
      }
   }

   public static HashSet Inv(TypeDecl G, ArrayList Us) {
      HashSet result = new HashSet();
      Iterator iter = Us.iterator();

      while(iter.hasNext()) {
         TypeDecl U = (TypeDecl)iter.next();
         Iterator i2 = ST(U).iterator();

         while(i2.hasNext()) {
            TypeDecl V = (TypeDecl)i2.next();
            if (V instanceof ParTypeDecl && !V.isRawType() && ((ParTypeDecl)V).genericDecl() == G) {
               result.add(V);
            }
         }
      }

      return result;
   }

   public TypeDecl lci(HashSet set, TypeDecl G) {
      ArrayList list = new ArrayList();
      boolean first = true;
      Iterator iter = set.iterator();

      while(true) {
         while(iter.hasNext()) {
            ParTypeDecl decl = (ParTypeDecl)iter.next();
            int i;
            if (first) {
               first = false;

               for(i = 0; i < decl.getNumArgument(); ++i) {
                  list.add(decl.getArgument(i).type());
               }
            } else {
               for(i = 0; i < decl.getNumArgument(); ++i) {
                  list.set(i, this.lcta((TypeDecl)list.get(i), decl.getArgument(i).type()));
               }
            }
         }

         return ((GenericTypeDecl)G).lookupParTypeDecl(list);
      }
   }

   public TypeDecl lcta(TypeDecl X, TypeDecl Y) {
      if (!X.isWildcard() && !Y.isWildcard()) {
         return X == Y ? X : this.lub(X, Y).asWildcardExtends();
      } else {
         TypeDecl V;
         if (!X.isWildcard() && Y instanceof WildcardExtendsType) {
            V = ((WildcardExtendsType)Y).getAccess().type();
            return this.lub(X, V).asWildcardExtends();
         } else {
            ArrayList bounds;
            if (!X.isWildcard() && Y instanceof WildcardSuperType) {
               V = ((WildcardSuperType)Y).getAccess().type();
               bounds = new ArrayList();
               bounds.add(X);
               bounds.add(V);
               return GLBTypeFactory.glb(bounds).asWildcardSuper();
            } else {
               TypeDecl U;
               if (X instanceof WildcardExtendsType && Y instanceof WildcardExtendsType) {
                  U = ((WildcardExtendsType)X).getAccess().type();
                  V = ((WildcardExtendsType)Y).getAccess().type();
                  return this.lub(U, V).asWildcardExtends();
               } else if (X instanceof WildcardExtendsType && Y instanceof WildcardSuperType) {
                  U = ((WildcardExtendsType)X).getAccess().type();
                  V = ((WildcardSuperType)Y).getAccess().type();
                  return U == V ? U : U.typeWildcard();
               } else if (X instanceof WildcardSuperType && Y instanceof WildcardSuperType) {
                  U = ((WildcardSuperType)X).getAccess().type();
                  V = ((WildcardSuperType)Y).getAccess().type();
                  bounds = new ArrayList();
                  bounds.add(U);
                  bounds.add(V);
                  return GLBTypeFactory.glb(bounds).asWildcardSuper();
               } else {
                  throw new Error("lcta not defined for (" + X.getClass().getName() + ", " + Y.getClass().getName());
               }
            }
         }
      }
   }

   public TypeDecl lub(TypeDecl X, TypeDecl Y) {
      ArrayList list = new ArrayList(2);
      list.add(X);
      list.add(Y);
      return this.lub(list);
   }

   public TypeDecl lub(ArrayList list) {
      return this.lookupLUBType(list);
   }

   public static HashSet EST(TypeDecl t) {
      HashSet result = new HashSet();
      Iterator iter = ST(t).iterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         if (typeDecl instanceof TypeVariable) {
            result.add(typeDecl);
         } else {
            result.add(typeDecl.erasure());
         }
      }

      return result;
   }

   public static HashSet ST(TypeDecl t) {
      HashSet result = new HashSet();
      addSupertypes(result, t);
      return result;
   }

   public static void addSupertypes(HashSet set, TypeDecl t) {
      set.add(t);
      int i;
      if (t instanceof ClassDecl) {
         ClassDecl type = (ClassDecl)t;
         if (type.hasSuperclass()) {
            addSupertypes(set, type.superclass());
         }

         for(i = 0; i < type.getNumImplements(); ++i) {
            addSupertypes(set, type.getImplements(i).type());
         }
      } else if (t instanceof InterfaceDecl) {
         InterfaceDecl type = (InterfaceDecl)t;

         for(i = 0; i < type.getNumSuperInterfaceId(); ++i) {
            addSupertypes(set, type.getSuperInterfaceId(i).type());
         }

         if (type.getNumSuperInterfaceId() == 0) {
            set.add(type.typeObject());
         }
      } else if (t instanceof TypeVariable) {
         TypeVariable type = (TypeVariable)t;

         for(i = 0; i < type.getNumTypeBound(); ++i) {
            addSupertypes(set, type.getTypeBound(i).type());
         }

         if (type.getNumTypeBound() == 0) {
            set.add(type.typeObject());
         }
      } else {
         if (!(t instanceof LUBType)) {
            throw new Error("Operation not supported for " + t.fullName() + ", " + t.getClass().getName());
         }

         LUBType type = (LUBType)t;

         for(i = 0; i < type.getNumTypeBound(); ++i) {
            addSupertypes(set, type.getTypeBound(i).type());
         }

         if (type.getNumTypeBound() == 0) {
            set.add(type.typeObject());
         }
      }

   }

   public HashSet implementedInterfaces() {
      HashSet ret = new HashSet();

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         ret.addAll(this.getTypeBound(i).type().implementedInterfaces());
      }

      return ret;
   }

   public LUBType() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
   }

   public LUBType(Modifiers p0, String p1, List<BodyDecl> p2, List<Access> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public LUBType(Modifiers p0, Symbol p1, List<BodyDecl> p2, List<Access> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
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

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 1);
   }

   public int getNumBodyDecl() {
      return this.getBodyDeclList().getNumChild();
   }

   public int getNumBodyDeclNoTransform() {
      return this.getBodyDeclListNoTransform().getNumChildNoTransform();
   }

   public BodyDecl getBodyDecl(int i) {
      return (BodyDecl)this.getBodyDeclList().getChild(i);
   }

   public void addBodyDecl(BodyDecl node) {
      List<BodyDecl> list = this.parent != null && state != null ? this.getBodyDeclList() : this.getBodyDeclListNoTransform();
      list.addChild(node);
   }

   public void addBodyDeclNoTransform(BodyDecl node) {
      List<BodyDecl> list = this.getBodyDeclListNoTransform();
      list.addChild(node);
   }

   public void setBodyDecl(BodyDecl node, int i) {
      List<BodyDecl> list = this.getBodyDeclList();
      list.setChild(node, i);
   }

   public List<BodyDecl> getBodyDecls() {
      return this.getBodyDeclList();
   }

   public List<BodyDecl> getBodyDeclsNoTransform() {
      return this.getBodyDeclListNoTransform();
   }

   public List<BodyDecl> getBodyDeclList() {
      List<BodyDecl> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setTypeBoundList(List<Access> list) {
      this.setChild(list, 2);
   }

   public int getNumTypeBound() {
      return this.getTypeBoundList().getNumChild();
   }

   public int getNumTypeBoundNoTransform() {
      return this.getTypeBoundListNoTransform().getNumChildNoTransform();
   }

   public Access getTypeBound(int i) {
      return (Access)this.getTypeBoundList().getChild(i);
   }

   public void addTypeBound(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getTypeBoundList() : this.getTypeBoundListNoTransform();
      list.addChild(node);
   }

   public void addTypeBoundNoTransform(Access node) {
      List<Access> list = this.getTypeBoundListNoTransform();
      list.addChild(node);
   }

   public void setTypeBound(Access node, int i) {
      List<Access> list = this.getTypeBoundList();
      list.setChild(node, i);
   }

   public List<Access> getTypeBounds() {
      return this.getTypeBoundList();
   }

   public List<Access> getTypeBoundsNoTransform() {
      return this.getTypeBoundListNoTransform();
   }

   public List<Access> getTypeBoundList() {
      List<Access> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<Access> getTypeBoundListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public TypeDecl lub() {
      if (this.lub_computed) {
         return this.lub_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.lub_value = this.lub_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lub_computed = true;
         }

         return this.lub_value;
      }
   }

   private TypeDecl lub_compute() {
      ArrayList list = new ArrayList();

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         list.add(this.getTypeBound(i).type());
      }

      ArrayList bounds = new ArrayList();
      Iterator iter = MEC(list).iterator();

      while(iter.hasNext()) {
         TypeDecl W = (TypeDecl)iter.next();
         TypeDecl C = W instanceof GenericTypeDecl ? this.lci(Inv(W, list), W) : W;
         bounds.add(C);
      }

      if (bounds.size() == 1) {
         return (TypeDecl)bounds.iterator().next();
      } else {
         return this.lookupLUBType(bounds);
      }
   }

   public String typeName() {
      ASTNode$State state = this.state();
      if (this.getNumTypeBound() == 0) {
         return "<NOTYPE>";
      } else {
         StringBuffer s = new StringBuffer();
         s.append(this.getTypeBound(0).type().typeName());

         for(int i = 1; i < this.getNumTypeBound(); ++i) {
            s.append(" & " + this.getTypeBound(i).type().typeName());
         }

         return s.toString();
      }
   }

   public boolean subtype(TypeDecl type) {
      if (this.subtype_TypeDecl_values == null) {
         this.subtype_TypeDecl_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.subtype_TypeDecl_values.containsKey(type)) {
         Object _o = this.subtype_TypeDecl_values.get(type);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.subtype_TypeDecl_values.put(type, _value);
         _value.value = true;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_subtype_TypeDecl_value = this.subtype_compute(type);
            if (state.RESET_CYCLE) {
               this.subtype_TypeDecl_values.remove(type);
            } else if (new_subtype_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_subtype_TypeDecl_value;
            }

            return new_subtype_TypeDecl_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_subtype_TypeDecl_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_subtype_TypeDecl_value = this.subtype_compute(type);
            if (new_subtype_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_subtype_TypeDecl_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.subtype_TypeDecl_values.put(type, new_subtype_TypeDecl_value);
         } else {
            this.subtype_TypeDecl_values.remove(type);
            state.RESET_CYCLE = true;
            this.subtype_compute(type);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_subtype_TypeDecl_value;
      }
   }

   private boolean subtype_compute(TypeDecl type) {
      return type.supertypeLUBType(this);
   }

   public boolean supertypeClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();
      return type.subtype(this.lub());
   }

   public boolean supertypeInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      return type.subtype(this.lub());
   }

   public boolean supertypeGLBType(GLBType type) {
      ASTNode$State state = this.state();
      ArrayList bounds = new ArrayList(this.getNumTypeBound());

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         bounds.add(this.getTypeBound(i));
      }

      return type == this.lookupGLBType(bounds);
   }

   public SootClass getSootClassDecl() {
      if (this.getSootClassDecl_computed) {
         return this.getSootClassDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getSootClassDecl_value = this.getSootClassDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSootClassDecl_computed = true;
         }

         return this.getSootClassDecl_value;
      }
   }

   private SootClass getSootClassDecl_compute() {
      return this.typeObject().getSootClassDecl();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
