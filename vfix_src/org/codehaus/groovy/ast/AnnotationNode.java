package org.codehaus.groovy.ast;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.expr.Expression;

public class AnnotationNode extends ASTNode {
   public static final int TYPE_TARGET = 1;
   public static final int CONSTRUCTOR_TARGET = 2;
   public static final int METHOD_TARGET = 4;
   public static final int FIELD_TARGET = 8;
   public static final int PARAMETER_TARGET = 16;
   public static final int LOCAL_VARIABLE_TARGET = 32;
   public static final int ANNOTATION_TARGET = 64;
   public static final int PACKAGE_TARGET = 128;
   private static final int ALL_TARGETS = 255;
   private final ClassNode classNode;
   private Map<String, Expression> members = new HashMap();
   private boolean runtimeRetention = false;
   private boolean sourceRetention = false;
   private boolean classRetention = false;
   private int allowedTargets = 255;

   public AnnotationNode(ClassNode classNode) {
      this.classNode = classNode;
   }

   public ClassNode getClassNode() {
      return this.classNode;
   }

   public Map<String, Expression> getMembers() {
      return this.members;
   }

   public Expression getMember(String name) {
      return (Expression)this.members.get(name);
   }

   public void addMember(String name, Expression value) {
      Expression oldValue = (Expression)this.members.get(name);
      if (oldValue == null) {
         this.members.put(name, value);
      } else {
         throw new GroovyBugError(String.format("Annotation member %s has already been added", name));
      }
   }

   public void setMember(String name, Expression value) {
      this.members.put(name, value);
   }

   public boolean isBuiltIn() {
      return false;
   }

   public boolean hasRuntimeRetention() {
      return this.runtimeRetention;
   }

   public void setRuntimeRetention(boolean flag) {
      this.runtimeRetention = flag;
   }

   public boolean hasSourceRetention() {
      return !this.runtimeRetention && !this.classRetention ? true : this.sourceRetention;
   }

   public void setSourceRetention(boolean flag) {
      this.sourceRetention = flag;
   }

   public boolean hasClassRetention() {
      return this.classRetention;
   }

   public void setClassRetention(boolean flag) {
      this.classRetention = flag;
   }

   public void setAllowedTargets(int bitmap) {
      this.allowedTargets = bitmap;
   }

   public boolean isTargetAllowed(int target) {
      return (this.allowedTargets & target) == target;
   }

   public static String targetToName(int target) {
      switch(target) {
      case 1:
         return "TYPE";
      case 2:
         return "CONSTRUCTOR";
      case 4:
         return "METHOD";
      case 8:
         return "FIELD";
      case 16:
         return "PARAMETER";
      case 32:
         return "LOCAL_VARIABLE";
      case 64:
         return "ANNOTATION";
      case 128:
         return "PACKAGE";
      default:
         return "unknown target";
      }
   }
}
