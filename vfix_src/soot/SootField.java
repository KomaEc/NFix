package soot;

import soot.jimple.paddle.PaddleField;
import soot.jimple.spark.pag.SparkField;
import soot.tagkit.AbstractHost;
import soot.util.Numberable;

public class SootField extends AbstractHost implements ClassMember, SparkField, Numberable, PaddleField {
   protected String name;
   protected Type type;
   protected int modifiers;
   protected boolean isDeclared = false;
   protected SootClass declaringClass;
   protected boolean isPhantom = false;
   private int number = 0;

   public SootField(String name, Type type, int modifiers) {
      this.name = name;
      this.type = type;
      this.modifiers = modifiers;
   }

   public SootField(String name, Type type) {
      this.name = name;
      this.type = type;
      this.modifiers = 0;
   }

   public int equivHashCode() {
      return this.type.hashCode() * 101 + this.modifiers * 17 + this.name.hashCode();
   }

   public String getName() {
      return this.name;
   }

   public String getSignature() {
      return getSignature(this.declaringClass, this.getName(), this.getType());
   }

   public static String getSignature(SootClass cl, String name, Type type) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("<" + Scene.v().quotedNameOf(cl.getName()) + ": ");
      buffer.append(type.toQuotedString() + " " + Scene.v().quotedNameOf(name) + ">");
      return buffer.toString();
   }

   public String getSubSignature() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(this.getType() + " " + Scene.v().quotedNameOf(this.getName()));
      return buffer.toString();
   }

   public SootClass getDeclaringClass() {
      if (!this.isDeclared) {
         throw new RuntimeException("not declared: " + this.getName() + " " + this.getType());
      } else {
         return this.declaringClass;
      }
   }

   public boolean isPhantom() {
      return this.isPhantom;
   }

   public void setPhantom(boolean value) {
      if (value) {
         if (!Scene.v().allowsPhantomRefs()) {
            throw new RuntimeException("Phantom refs not allowed");
         }

         if (this.declaringClass != null && !this.declaringClass.isPhantom()) {
            throw new RuntimeException("Declaring class would have to be phantom");
         }
      }

      this.isPhantom = value;
   }

   public boolean isDeclared() {
      return this.isDeclared;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Type getType() {
      return this.type;
   }

   public void setType(Type t) {
      this.type = t;
   }

   public boolean isPublic() {
      return Modifier.isPublic(this.getModifiers());
   }

   public boolean isProtected() {
      return Modifier.isProtected(this.getModifiers());
   }

   public boolean isPrivate() {
      return Modifier.isPrivate(this.getModifiers());
   }

   public boolean isStatic() {
      return Modifier.isStatic(this.getModifiers());
   }

   public boolean isFinal() {
      return Modifier.isFinal(this.getModifiers());
   }

   public void setModifiers(int modifiers) {
      if (!this.declaringClass.isApplicationClass()) {
         throw new RuntimeException("Cannot set modifiers of a field from a non-app class!");
      } else {
         this.modifiers = modifiers;
      }
   }

   public int getModifiers() {
      return this.modifiers;
   }

   public String toString() {
      return this.getSignature();
   }

   private String getOriginalStyleDeclaration() {
      String qualifiers = Modifier.toString(this.modifiers) + " " + this.type.toQuotedString();
      qualifiers = qualifiers.trim();
      return qualifiers.isEmpty() ? Scene.v().quotedNameOf(this.name) : qualifiers + " " + Scene.v().quotedNameOf(this.name) + "";
   }

   public String getDeclaration() {
      return this.getOriginalStyleDeclaration();
   }

   public final int getNumber() {
      return this.number;
   }

   public final void setNumber(int number) {
      this.number = number;
   }

   public SootFieldRef makeRef() {
      return Scene.v().makeFieldRef(this.declaringClass, this.name, this.type, this.isStatic());
   }

   public void setDeclared(boolean declared) {
      this.isDeclared = declared;
   }

   public void setDeclaringClass(SootClass sc) {
      this.declaringClass = sc;
      if (this.type instanceof RefLikeType) {
         Scene.v().getFieldNumberer().add(this);
      }

   }
}
