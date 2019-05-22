package soot.javaToJimple;

import polyglot.types.ClassType;
import soot.Body;
import soot.PackManager;
import soot.SootMethod;
import soot.Type;
import soot.jimple.JimpleBody;

public class AnonClassInitMethodSource extends PolyglotMethodSource {
   private boolean hasOuterRef;
   private boolean hasQualifier;
   private boolean inStaticMethod;
   private boolean isSubType = false;
   private Type superOuterType = null;
   private Type thisOuterType = null;
   private ClassType polyglotType;
   private ClassType anonType;
   private Type outerClassType;

   public void hasOuterRef(boolean b) {
      this.hasOuterRef = b;
   }

   public boolean hasOuterRef() {
      return this.hasOuterRef;
   }

   public void hasQualifier(boolean b) {
      this.hasQualifier = b;
   }

   public boolean hasQualifier() {
      return this.hasQualifier;
   }

   public void inStaticMethod(boolean b) {
      this.inStaticMethod = b;
   }

   public boolean inStaticMethod() {
      return this.inStaticMethod;
   }

   public void isSubType(boolean b) {
      this.isSubType = b;
   }

   public boolean isSubType() {
      return this.isSubType;
   }

   public void superOuterType(Type t) {
      this.superOuterType = t;
   }

   public Type superOuterType() {
      return this.superOuterType;
   }

   public void thisOuterType(Type t) {
      this.thisOuterType = t;
   }

   public Type thisOuterType() {
      return this.thisOuterType;
   }

   public void polyglotType(ClassType type) {
      this.polyglotType = type;
   }

   public ClassType polyglotType() {
      return this.polyglotType;
   }

   public void anonType(ClassType type) {
      this.anonType = type;
   }

   public ClassType anonType() {
      return this.anonType;
   }

   public Body getBody(SootMethod sootMethod, String phaseName) {
      AnonInitBodyBuilder aibb = new AnonInitBodyBuilder();
      JimpleBody body = aibb.createBody(sootMethod);
      PackManager.v().getPack("jj").apply(body);
      return body;
   }

   public Type outerClassType() {
      return this.outerClassType;
   }

   public void outerClassType(Type type) {
      this.outerClassType = type;
   }
}
