package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import soot.tagkit.AnnotationTag;

public class Annotation extends Modifier implements Cloneable {
   protected boolean decl_computed = false;
   protected TypeDecl decl_value;

   public void flushCache() {
      super.flushCache();
      this.decl_computed = false;
      this.decl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Annotation clone() throws CloneNotSupportedException {
      Annotation node = (Annotation)super.clone();
      node.decl_computed = false;
      node.decl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Annotation copy() {
      try {
         Annotation node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public Annotation fullCopy() {
      Annotation tree = this.copy();
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

   public void checkModifiers() {
      super.checkModifiers();
      if (this.decl() instanceof AnnotationDecl) {
         AnnotationDecl T = (AnnotationDecl)this.decl();
         Annotation m = T.annotation(this.lookupType("java.lang.annotation", "Target"));
         if (m != null && m.getNumElementValuePair() == 1 && m.getElementValuePair(0).getName().equals("value")) {
            ElementValue v = m.getElementValuePair(0).getElementValue();
            if (!v.validTarget(this)) {
               this.error("annotation type " + T.typeName() + " is not applicable to this kind of declaration");
            }
         }
      }

   }

   public void typeCheck() {
      if (!this.decl().isAnnotationDecl()) {
         if (!this.decl().isUnknown()) {
            this.error(this.decl().typeName() + " is not an annotation type");
         }
      } else {
         TypeDecl typeDecl = this.decl();
         if (this.lookupAnnotation(typeDecl) != this) {
            this.error("duplicate annotation " + typeDecl.typeName());
         }

         int i;
         for(i = 0; i < typeDecl.getNumBodyDecl(); ++i) {
            if (typeDecl.getBodyDecl(i) instanceof MethodDecl) {
               MethodDecl decl = (MethodDecl)typeDecl.getBodyDecl(i);
               if (this.elementValueFor(decl.name()) == null && (!(decl instanceof AnnotationMethodDecl) || !((AnnotationMethodDecl)decl).hasDefaultValue())) {
                  this.error("missing value for " + decl.name());
               }
            }
         }

         for(i = 0; i < this.getNumElementValuePair(); ++i) {
            ElementValuePair pair = this.getElementValuePair(i);
            if (typeDecl.memberMethods(pair.getName()).isEmpty()) {
               this.error("can not find element named " + pair.getName() + " in " + typeDecl.typeName());
            }
         }
      }

      this.checkOverride();
   }

   public void toString(StringBuffer s) {
      s.append("@");
      this.getAccess().toString(s);
      s.append("(");

      for(int i = 0; i < this.getNumElementValuePair(); ++i) {
         if (i != 0) {
            s.append(", ");
         }

         this.getElementValuePair(i).toString(s);
      }

      s.append(")");
   }

   public void appendAsAttributeTo(Collection list) {
      AnnotationTag tag = new AnnotationTag(this.decl().typeDescriptor(), this.getNumElementValuePair());
      ArrayList elements = new ArrayList(this.getNumElementValuePair());

      for(int i = 0; i < this.getNumElementValuePair(); ++i) {
         String name = this.getElementValuePair(i).getName();
         ElementValue value = this.getElementValuePair(i).getElementValue();
         value.appendAsAttributeTo(elements, name);
      }

      tag.setElems(elements);
      list.add(tag);
   }

   public Annotation() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 1);
   }

   public Annotation(String p0, Access p1, List<ElementValuePair> p2) {
      this.setID(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   public Annotation(Symbol p0, Access p1, List<ElementValuePair> p2) {
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

   public void setAccess(Access node) {
      this.setChild(node, 0);
   }

   public Access getAccess() {
      return (Access)this.getChild(0);
   }

   public Access getAccessNoTransform() {
      return (Access)this.getChildNoTransform(0);
   }

   public void setElementValuePairList(List<ElementValuePair> list) {
      this.setChild(list, 1);
   }

   public int getNumElementValuePair() {
      return this.getElementValuePairList().getNumChild();
   }

   public int getNumElementValuePairNoTransform() {
      return this.getElementValuePairListNoTransform().getNumChildNoTransform();
   }

   public ElementValuePair getElementValuePair(int i) {
      return (ElementValuePair)this.getElementValuePairList().getChild(i);
   }

   public void addElementValuePair(ElementValuePair node) {
      List<ElementValuePair> list = this.parent != null && state != null ? this.getElementValuePairList() : this.getElementValuePairListNoTransform();
      list.addChild(node);
   }

   public void addElementValuePairNoTransform(ElementValuePair node) {
      List<ElementValuePair> list = this.getElementValuePairListNoTransform();
      list.addChild(node);
   }

   public void setElementValuePair(ElementValuePair node, int i) {
      List<ElementValuePair> list = this.getElementValuePairList();
      list.setChild(node, i);
   }

   public List<ElementValuePair> getElementValuePairs() {
      return this.getElementValuePairList();
   }

   public List<ElementValuePair> getElementValuePairsNoTransform() {
      return this.getElementValuePairListNoTransform();
   }

   public List<ElementValuePair> getElementValuePairList() {
      List<ElementValuePair> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<ElementValuePair> getElementValuePairListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void checkOverride() {
      if (this.decl().fullName().equals("java.lang.Override") && this.enclosingBodyDecl() instanceof MethodDecl) {
         MethodDecl method = (MethodDecl)this.enclosingBodyDecl();
         TypeDecl host = method.hostType();
         SimpleSet ancestors = host.ancestorMethods(method.signature());
         boolean found = false;
         Iterator iter = ancestors.iterator();

         while(iter.hasNext()) {
            MethodDecl decl = (MethodDecl)iter.next();
            if (method.overrides(decl)) {
               found = true;
               break;
            }
         }

         if (!found) {
            TypeDecl typeObject = this.lookupType("java.lang", "Object");
            SimpleSet overrides = typeObject.localMethodsSignature(method.signature());
            if (overrides.isEmpty() || !((MethodDecl)overrides.iterator().next()).isPublic()) {
               this.error("method does not override a method from a supertype");
            }
         }
      }

   }

   public TypeDecl decl() {
      if (this.decl_computed) {
         return this.decl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.decl_value = this.decl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.decl_computed = true;
         }

         return this.decl_value;
      }
   }

   private TypeDecl decl_compute() {
      return this.getAccess().type();
   }

   public ElementValue elementValueFor(String name) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumElementValuePair(); ++i) {
         ElementValuePair pair = this.getElementValuePair(i);
         if (pair.getName().equals(name)) {
            return pair.getElementValue();
         }
      }

      return null;
   }

   public TypeDecl type() {
      ASTNode$State state = this.state();
      return this.getAccess().type();
   }

   public boolean isMetaAnnotation() {
      ASTNode$State state = this.state();
      return this.hostType().isAnnotationDecl();
   }

   public boolean isRuntimeVisible() {
      ASTNode$State state = this.state();
      Annotation a = this.decl().annotation(this.lookupType("java.lang.annotation", "Retention"));
      if (a == null) {
         return false;
      } else {
         ElementConstantValue value = (ElementConstantValue)a.getElementValuePair(0).getElementValue();
         Variable v = value.getExpr().varDecl();
         return v != null && v.name().equals("RUNTIME");
      }
   }

   public boolean isRuntimeInvisible() {
      ASTNode$State state = this.state();
      Annotation a = this.decl().annotation(this.lookupType("java.lang.annotation", "Retention"));
      if (a == null) {
         return true;
      } else {
         ElementConstantValue value = (ElementConstantValue)a.getElementValuePair(0).getElementValue();
         Variable v = value.getExpr().varDecl();
         return v != null && v.name().equals("CLASS");
      }
   }

   public TypeDecl lookupType(String packageName, String typeName) {
      ASTNode$State state = this.state();
      TypeDecl lookupType_String_String_value = this.getParent().Define_TypeDecl_lookupType(this, (ASTNode)null, packageName, typeName);
      return lookupType_String_String_value;
   }

   public boolean mayUseAnnotationTarget(String name) {
      ASTNode$State state = this.state();
      boolean mayUseAnnotationTarget_String_value = this.getParent().Define_boolean_mayUseAnnotationTarget(this, (ASTNode)null, name);
      return mayUseAnnotationTarget_String_value;
   }

   public BodyDecl enclosingBodyDecl() {
      ASTNode$State state = this.state();
      BodyDecl enclosingBodyDecl_value = this.getParent().Define_BodyDecl_enclosingBodyDecl(this, (ASTNode)null);
      return enclosingBodyDecl_value;
   }

   public Annotation lookupAnnotation(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      Annotation lookupAnnotation_TypeDecl_value = this.getParent().Define_Annotation_lookupAnnotation(this, (ASTNode)null, typeDecl);
      return lookupAnnotation_TypeDecl_value;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public TypeDecl Define_TypeDecl_enclosingAnnotationDecl(ASTNode caller, ASTNode child) {
      if (caller == this.getElementValuePairListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.decl();
      } else {
         return this.getParent().Define_TypeDecl_enclosingAnnotationDecl(this, caller);
      }
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
