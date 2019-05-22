package soot.jimple.spark.pag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.Context;
import soot.RefLikeType;
import soot.Type;
import soot.toolkits.scalar.Pair;
import soot.util.Numberable;

public abstract class VarNode extends ValNode implements Comparable {
   private static final Logger logger = LoggerFactory.getLogger(VarNode.class);
   protected Object variable;
   protected Map<SparkField, FieldRefNode> fields;
   protected int finishingNumber = 0;
   protected boolean interProcTarget = false;
   protected boolean interProcSource = false;
   protected int numDerefs = 0;

   public Context context() {
      return null;
   }

   public Collection<FieldRefNode> getAllFieldRefs() {
      return (Collection)(this.fields == null ? Collections.emptyList() : this.fields.values());
   }

   public FieldRefNode dot(SparkField field) {
      return this.fields == null ? null : (FieldRefNode)this.fields.get(field);
   }

   public int compareTo(Object o) {
      VarNode other = (VarNode)o;
      if (other.finishingNumber == this.finishingNumber && other != this) {
         logger.debug("This is: " + this + " with id " + this.getNumber() + " and number " + this.finishingNumber);
         logger.debug("Other is: " + other + " with id " + other.getNumber() + " and number " + other.finishingNumber);
         throw new RuntimeException("Comparison error");
      } else {
         return other.finishingNumber - this.finishingNumber;
      }
   }

   public void setFinishingNumber(int i) {
      this.finishingNumber = i;
      if (i > this.pag.maxFinishNumber) {
         this.pag.maxFinishNumber = i;
      }

   }

   public Object getVariable() {
      return this.variable;
   }

   public void setInterProcTarget() {
      this.interProcTarget = true;
   }

   public boolean isInterProcTarget() {
      return this.interProcTarget;
   }

   public void setInterProcSource() {
      this.interProcSource = true;
   }

   public boolean isInterProcSource() {
      return this.interProcSource;
   }

   public boolean isThisPtr() {
      if (this.variable instanceof Pair) {
         Pair o = (Pair)this.variable;
         return o.isThisParameter();
      } else {
         return false;
      }
   }

   VarNode(PAG pag, Object variable, Type t) {
      super(pag, t);
      if (t instanceof RefLikeType && !(t instanceof AnySubType)) {
         this.variable = variable;
         pag.getVarNodeNumberer().add((Numberable)this);
         this.setFinishingNumber(++pag.maxFinishNumber);
      } else {
         throw new RuntimeException("Attempt to create VarNode of type " + t);
      }
   }

   void addField(FieldRefNode frn, SparkField field) {
      if (this.fields == null) {
         this.fields = new HashMap();
      }

      this.fields.put(field, frn);
   }
}
