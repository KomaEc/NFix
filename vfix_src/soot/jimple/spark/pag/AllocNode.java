package soot.jimple.spark.pag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import soot.Context;
import soot.PhaseOptions;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.options.CGOptions;
import soot.util.Numberable;

public class AllocNode extends Node implements Context {
   protected Object newExpr;
   protected Map<SparkField, AllocDotField> fields;
   private SootMethod method;

   public Object getNewExpr() {
      return this.newExpr;
   }

   public Collection<AllocDotField> getAllFieldRefs() {
      return (Collection)(this.fields == null ? Collections.emptySet() : this.fields.values());
   }

   public AllocDotField dot(SparkField field) {
      return this.fields == null ? null : (AllocDotField)this.fields.get(field);
   }

   public String toString() {
      return "AllocNode " + this.getNumber() + " " + this.newExpr + " in method " + this.method;
   }

   AllocNode(PAG pag, Object newExpr, Type t, SootMethod m) {
      super(pag, t);
      this.method = m;
      if (t instanceof RefType) {
         RefType rt = (RefType)t;
         if (rt.getSootClass().isAbstract()) {
            boolean usesReflectionLog = (new CGOptions(PhaseOptions.v().getPhaseOptions("cg"))).reflection_log() != null;
            if (!usesReflectionLog) {
               throw new RuntimeException("Attempt to create allocnode with abstract type " + t);
            }
         }
      }

      this.newExpr = newExpr;
      if (newExpr instanceof ContextVarNode) {
         throw new RuntimeException();
      } else {
         pag.getAllocNodeNumberer().add((Numberable)this);
      }
   }

   void addField(AllocDotField adf, SparkField field) {
      if (this.fields == null) {
         this.fields = new HashMap();
      }

      this.fields.put(field, adf);
   }

   public Set<AllocDotField> getFields() {
      return (Set)(this.fields == null ? Collections.emptySet() : new HashSet(this.fields.values()));
   }

   public SootMethod getMethod() {
      return this.method;
   }
}
