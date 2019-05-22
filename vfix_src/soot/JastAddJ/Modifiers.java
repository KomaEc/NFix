package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.PhaseOptions;
import soot.options.JBOptions;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;

public class Modifiers extends ASTNode<ASTNode> implements Cloneable {
   public static final int ACC_ANNOTATION = 8192;
   public static final int ACC_ENUM = 16384;
   public static final int ACC_BRIDGE = 64;
   public static final int ACC_VARARGS = 128;
   protected boolean isPublic_computed = false;
   protected boolean isPublic_value;
   protected boolean isPrivate_computed = false;
   protected boolean isPrivate_value;
   protected boolean isProtected_computed = false;
   protected boolean isProtected_value;
   protected boolean isStatic_computed = false;
   protected boolean isStatic_value;
   protected boolean isFinal_computed = false;
   protected boolean isFinal_value;
   protected boolean isAbstract_computed = false;
   protected boolean isAbstract_value;
   protected boolean isVolatile_computed = false;
   protected boolean isVolatile_value;
   protected boolean isTransient_computed = false;
   protected boolean isTransient_value;
   protected boolean isStrictfp_computed = false;
   protected boolean isStrictfp_value;
   protected boolean isSynchronized_computed = false;
   protected boolean isSynchronized_value;
   protected boolean isNative_computed = false;
   protected boolean isNative_value;
   protected boolean isSynthetic_computed = false;
   protected boolean isSynthetic_value;
   protected Map numModifier_String_values;

   public void flushCache() {
      super.flushCache();
      this.isPublic_computed = false;
      this.isPrivate_computed = false;
      this.isProtected_computed = false;
      this.isStatic_computed = false;
      this.isFinal_computed = false;
      this.isAbstract_computed = false;
      this.isVolatile_computed = false;
      this.isTransient_computed = false;
      this.isStrictfp_computed = false;
      this.isSynchronized_computed = false;
      this.isNative_computed = false;
      this.isSynthetic_computed = false;
      this.numModifier_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Modifiers clone() throws CloneNotSupportedException {
      Modifiers node = (Modifiers)super.clone();
      node.isPublic_computed = false;
      node.isPrivate_computed = false;
      node.isProtected_computed = false;
      node.isStatic_computed = false;
      node.isFinal_computed = false;
      node.isAbstract_computed = false;
      node.isVolatile_computed = false;
      node.isTransient_computed = false;
      node.isStrictfp_computed = false;
      node.isSynchronized_computed = false;
      node.isNative_computed = false;
      node.isSynthetic_computed = false;
      node.numModifier_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Modifiers copy() {
      try {
         Modifiers node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public Modifiers fullCopy() {
      Modifiers tree = this.copy();
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
      if (this.numProtectionModifiers() > 1) {
         this.error("only one public, protected, private allowed");
      }

      if (this.numModifier("static") > 1) {
         this.error("only one static allowed");
      }

      if (this.numCompletenessModifiers() > 1) {
         this.error("only one of final, abstract, volatile allowed");
      }

      if (this.numModifier("synchronized") > 1) {
         this.error("only one synchronized allowed");
      }

      if (this.numModifier("transient") > 1) {
         this.error("only one transient allowed");
      }

      if (this.numModifier("native") > 1) {
         this.error("only one native allowed");
      }

      if (this.numModifier("strictfp") > 1) {
         this.error("only one strictfp allowed");
      }

      if (this.isPublic() && !this.mayBePublic()) {
         this.error("modifier public not allowed in this context");
      }

      if (this.isPrivate() && !this.mayBePrivate()) {
         this.error("modifier private not allowed in this context");
      }

      if (this.isProtected() && !this.mayBeProtected()) {
         this.error("modifier protected not allowed in this context");
      }

      if (this.isStatic() && !this.mayBeStatic()) {
         this.error("modifier static not allowed in this context");
      }

      if (this.isFinal() && !this.mayBeFinal()) {
         this.error("modifier final not allowed in this context");
      }

      if (this.isAbstract() && !this.mayBeAbstract()) {
         this.error("modifier abstract not allowed in this context");
      }

      if (this.isVolatile() && !this.mayBeVolatile()) {
         this.error("modifier volatile not allowed in this context");
      }

      if (this.isTransient() && !this.mayBeTransient()) {
         this.error("modifier transient not allowed in this context");
      }

      if (this.isStrictfp() && !this.mayBeStrictfp()) {
         this.error("modifier strictfp not allowed in this context");
      }

      if (this.isSynchronized() && !this.mayBeSynchronized()) {
         this.error("modifier synchronized not allowed in this context");
      }

      if (this.isNative() && !this.mayBeNative()) {
         this.error("modifier native not allowed in this context");
      }

   }

   public void toString(StringBuffer s) {
      for(int i = 0; i < this.getNumModifier(); ++i) {
         this.getModifier(i).toString(s);
         s.append(" ");
      }

   }

   public void addSourceOnlyAnnotations(Collection c) {
      if ((new JBOptions(PhaseOptions.v().getPhaseOptions("jb"))).preserve_source_annotations()) {
         for(int i = 0; i < this.getNumModifier(); ++i) {
            if (this.getModifier(i) instanceof Annotation) {
               Annotation a = (Annotation)this.getModifier(i);
               if (!a.isRuntimeVisible() && !a.isRuntimeInvisible()) {
                  VisibilityAnnotationTag tag = new VisibilityAnnotationTag(2);
                  ArrayList elements = new ArrayList(1);
                  a.appendAsAttributeTo(elements);
                  tag.addAnnotation((AnnotationTag)elements.get(0));
                  c.add(tag);
               }
            }
         }
      }

   }

   public void addAllAnnotations(Collection c) {
      for(int i = 0; i < this.getNumModifier(); ++i) {
         if (this.getModifier(i) instanceof Annotation) {
            Annotation a = (Annotation)this.getModifier(i);
            a.appendAsAttributeTo(c);
         }
      }

   }

   public void addRuntimeVisibleAnnotationsAttribute(Collection c) {
      Collection annotations = this.runtimeVisibleAnnotations();
      if (!annotations.isEmpty()) {
         VisibilityAnnotationTag tag = new VisibilityAnnotationTag(0);
         Iterator iter = annotations.iterator();

         while(iter.hasNext()) {
            Annotation annotation = (Annotation)iter.next();
            ArrayList elements = new ArrayList(1);
            annotation.appendAsAttributeTo(elements);
            tag.addAnnotation((AnnotationTag)elements.get(0));
         }

         c.add(tag);
      }

   }

   public void addRuntimeInvisibleAnnotationsAttribute(Collection c) {
      Collection annotations = this.runtimeInvisibleAnnotations();
      if (!annotations.isEmpty()) {
         VisibilityAnnotationTag tag = new VisibilityAnnotationTag(1);
         Iterator iter = annotations.iterator();

         while(iter.hasNext()) {
            Annotation annotation = (Annotation)iter.next();
            ArrayList elements = new ArrayList(1);
            annotation.appendAsAttributeTo(elements);
            tag.addAnnotation((AnnotationTag)elements.get(0));
         }

         c.add(tag);
      }

   }

   public Collection runtimeVisibleAnnotations() {
      Collection annotations = new ArrayList();

      for(int i = 0; i < this.getNumModifier(); ++i) {
         if (this.getModifier(i).isRuntimeVisible()) {
            annotations.add(this.getModifier(i));
         }
      }

      return annotations;
   }

   public Collection runtimeInvisibleAnnotations() {
      Collection annotations = new ArrayList();

      for(int i = 0; i < this.getNumModifier(); ++i) {
         if (this.getModifier(i).isRuntimeInvisible()) {
            annotations.add(this.getModifier(i));
         }
      }

      return annotations;
   }

   public Modifiers() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public Modifiers(List<Modifier> p0) {
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

   public boolean isPublic() {
      if (this.isPublic_computed) {
         return this.isPublic_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isPublic_value = this.isPublic_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isPublic_computed = true;
         }

         return this.isPublic_value;
      }
   }

   private boolean isPublic_compute() {
      return this.numModifier("public") != 0;
   }

   public boolean isPrivate() {
      if (this.isPrivate_computed) {
         return this.isPrivate_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isPrivate_value = this.isPrivate_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isPrivate_computed = true;
         }

         return this.isPrivate_value;
      }
   }

   private boolean isPrivate_compute() {
      return this.numModifier("private") != 0;
   }

   public boolean isProtected() {
      if (this.isProtected_computed) {
         return this.isProtected_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isProtected_value = this.isProtected_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isProtected_computed = true;
         }

         return this.isProtected_value;
      }
   }

   private boolean isProtected_compute() {
      return this.numModifier("protected") != 0;
   }

   public boolean isStatic() {
      if (this.isStatic_computed) {
         return this.isStatic_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isStatic_value = this.isStatic_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isStatic_computed = true;
         }

         return this.isStatic_value;
      }
   }

   private boolean isStatic_compute() {
      return this.numModifier("static") != 0;
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
      return this.numModifier("final") != 0;
   }

   public boolean isAbstract() {
      if (this.isAbstract_computed) {
         return this.isAbstract_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isAbstract_value = this.isAbstract_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isAbstract_computed = true;
         }

         return this.isAbstract_value;
      }
   }

   private boolean isAbstract_compute() {
      return this.numModifier("abstract") != 0;
   }

   public boolean isVolatile() {
      if (this.isVolatile_computed) {
         return this.isVolatile_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isVolatile_value = this.isVolatile_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isVolatile_computed = true;
         }

         return this.isVolatile_value;
      }
   }

   private boolean isVolatile_compute() {
      return this.numModifier("volatile") != 0;
   }

   public boolean isTransient() {
      if (this.isTransient_computed) {
         return this.isTransient_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isTransient_value = this.isTransient_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isTransient_computed = true;
         }

         return this.isTransient_value;
      }
   }

   private boolean isTransient_compute() {
      return this.numModifier("transient") != 0;
   }

   public boolean isStrictfp() {
      if (this.isStrictfp_computed) {
         return this.isStrictfp_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isStrictfp_value = this.isStrictfp_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isStrictfp_computed = true;
         }

         return this.isStrictfp_value;
      }
   }

   private boolean isStrictfp_compute() {
      return this.numModifier("strictfp") != 0;
   }

   public boolean isSynchronized() {
      if (this.isSynchronized_computed) {
         return this.isSynchronized_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isSynchronized_value = this.isSynchronized_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isSynchronized_computed = true;
         }

         return this.isSynchronized_value;
      }
   }

   private boolean isSynchronized_compute() {
      return this.numModifier("synchronized") != 0;
   }

   public boolean isNative() {
      if (this.isNative_computed) {
         return this.isNative_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isNative_value = this.isNative_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isNative_computed = true;
         }

         return this.isNative_value;
      }
   }

   private boolean isNative_compute() {
      return this.numModifier("native") != 0;
   }

   public boolean isSynthetic() {
      if (this.isSynthetic_computed) {
         return this.isSynthetic_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isSynthetic_value = this.isSynthetic_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isSynthetic_computed = true;
         }

         return this.isSynthetic_value;
      }
   }

   private boolean isSynthetic_compute() {
      return this.numModifier("synthetic") != 0;
   }

   public int numProtectionModifiers() {
      ASTNode$State state = this.state();
      return this.numModifier("public") + this.numModifier("protected") + this.numModifier("private");
   }

   public int numCompletenessModifiers() {
      ASTNode$State state = this.state();
      return this.numModifier("abstract") + this.numModifier("final") + this.numModifier("volatile");
   }

   public int numModifier(String name) {
      if (this.numModifier_String_values == null) {
         this.numModifier_String_values = new HashMap(4);
      }

      if (this.numModifier_String_values.containsKey(name)) {
         return (Integer)this.numModifier_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         int numModifier_String_value = this.numModifier_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.numModifier_String_values.put(name, numModifier_String_value);
         }

         return numModifier_String_value;
      }
   }

   private int numModifier_compute(String name) {
      int n = 0;

      for(int i = 0; i < this.getNumModifier(); ++i) {
         String s = this.getModifier(i).getID();
         if (s.equals(name)) {
            ++n;
         }
      }

      return n;
   }

   public Annotation annotation(TypeDecl typeDecl) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumModifier(); ++i) {
         if (this.getModifier(i) instanceof Annotation) {
            Annotation a = (Annotation)this.getModifier(i);
            if (a.type() == typeDecl) {
               return a;
            }
         }
      }

      return null;
   }

   public boolean hasAnnotationSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      Annotation a = this.annotation(this.lookupType("java.lang", "SuppressWarnings"));
      return a != null && a.getNumElementValuePair() == 1 && a.getElementValuePair(0).getName().equals("value") ? a.getElementValuePair(0).getElementValue().hasValue(s) : false;
   }

   public boolean hasDeprecatedAnnotation() {
      ASTNode$State state = this.state();
      return this.annotation(this.lookupType("java.lang", "Deprecated")) != null;
   }

   public boolean hasAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.annotation(this.lookupType("java.lang", "SafeVarargs")) != null;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public boolean mayBePublic() {
      ASTNode$State state = this.state();
      boolean mayBePublic_value = this.getParent().Define_boolean_mayBePublic(this, (ASTNode)null);
      return mayBePublic_value;
   }

   public boolean mayBePrivate() {
      ASTNode$State state = this.state();
      boolean mayBePrivate_value = this.getParent().Define_boolean_mayBePrivate(this, (ASTNode)null);
      return mayBePrivate_value;
   }

   public boolean mayBeProtected() {
      ASTNode$State state = this.state();
      boolean mayBeProtected_value = this.getParent().Define_boolean_mayBeProtected(this, (ASTNode)null);
      return mayBeProtected_value;
   }

   public boolean mayBeStatic() {
      ASTNode$State state = this.state();
      boolean mayBeStatic_value = this.getParent().Define_boolean_mayBeStatic(this, (ASTNode)null);
      return mayBeStatic_value;
   }

   public boolean mayBeFinal() {
      ASTNode$State state = this.state();
      boolean mayBeFinal_value = this.getParent().Define_boolean_mayBeFinal(this, (ASTNode)null);
      return mayBeFinal_value;
   }

   public boolean mayBeAbstract() {
      ASTNode$State state = this.state();
      boolean mayBeAbstract_value = this.getParent().Define_boolean_mayBeAbstract(this, (ASTNode)null);
      return mayBeAbstract_value;
   }

   public boolean mayBeVolatile() {
      ASTNode$State state = this.state();
      boolean mayBeVolatile_value = this.getParent().Define_boolean_mayBeVolatile(this, (ASTNode)null);
      return mayBeVolatile_value;
   }

   public boolean mayBeTransient() {
      ASTNode$State state = this.state();
      boolean mayBeTransient_value = this.getParent().Define_boolean_mayBeTransient(this, (ASTNode)null);
      return mayBeTransient_value;
   }

   public boolean mayBeStrictfp() {
      ASTNode$State state = this.state();
      boolean mayBeStrictfp_value = this.getParent().Define_boolean_mayBeStrictfp(this, (ASTNode)null);
      return mayBeStrictfp_value;
   }

   public boolean mayBeSynchronized() {
      ASTNode$State state = this.state();
      boolean mayBeSynchronized_value = this.getParent().Define_boolean_mayBeSynchronized(this, (ASTNode)null);
      return mayBeSynchronized_value;
   }

   public boolean mayBeNative() {
      ASTNode$State state = this.state();
      boolean mayBeNative_value = this.getParent().Define_boolean_mayBeNative(this, (ASTNode)null);
      return mayBeNative_value;
   }

   public TypeDecl lookupType(String packageName, String typeName) {
      ASTNode$State state = this.state();
      TypeDecl lookupType_String_String_value = this.getParent().Define_TypeDecl_lookupType(this, (ASTNode)null, packageName, typeName);
      return lookupType_String_String_value;
   }

   public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
      if (caller == this.getModifierListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.annotation(typeDecl);
      } else {
         return this.getParent().Define_Annotation_lookupAnnotation(this, caller, typeDecl);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
