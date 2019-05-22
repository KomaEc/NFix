package soot.jimple.spark.sets;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.PointsToSet;
import soot.RefType;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.StringConstantNode;
import soot.util.BitVector;

public abstract class PointsToSetInternal implements PointsToSet, EqualsSupportingPointsToSet {
   private static final Logger logger = LoggerFactory.getLogger(PointsToSetInternal.class);
   protected Type type;

   public boolean addAll(PointsToSetInternal other, final PointsToSetInternal exclude) {
      if (other instanceof DoublePointsToSet) {
         return this.addAll(other.getNewSet(), exclude) | this.addAll(other.getOldSet(), exclude);
      } else if (other instanceof EmptyPointsToSet) {
         return false;
      } else if (exclude instanceof EmptyPointsToSet) {
         return this.addAll(other, (PointsToSetInternal)null);
      } else {
         if (!G.v().PointsToSetInternal_warnedAlready) {
            logger.warn("using default implementation of addAll. You should implement a faster specialized implementation.");
            logger.debug("this is of type " + this.getClass().getName());
            logger.debug("other is of type " + other.getClass().getName());
            if (exclude == null) {
               logger.debug("exclude is null");
            } else {
               logger.debug("exclude is of type " + exclude.getClass().getName());
            }

            G.v().PointsToSetInternal_warnedAlready = true;
         }

         return other.forall(new P2SetVisitor() {
            public final void visit(Node n) {
               if (exclude == null || !exclude.contains(n)) {
                  this.returnValue |= PointsToSetInternal.this.add(n);
               }

            }
         });
      }
   }

   public abstract boolean forall(P2SetVisitor var1);

   public abstract boolean add(Node var1);

   public PointsToSetInternal getNewSet() {
      return this;
   }

   public PointsToSetInternal getOldSet() {
      return EmptyPointsToSet.v();
   }

   public void flushNew() {
   }

   public void unFlushNew() {
   }

   public void mergeWith(PointsToSetInternal other) {
      this.addAll(other, (PointsToSetInternal)null);
   }

   public abstract boolean contains(Node var1);

   public PointsToSetInternal(Type type) {
      this.type = type;
   }

   public boolean hasNonEmptyIntersection(PointsToSet other) {
      final PointsToSetInternal o = (PointsToSetInternal)other;
      return this.forall(new P2SetVisitor() {
         public void visit(Node n) {
            if (o.contains(n)) {
               this.returnValue = true;
            }

         }
      });
   }

   public Set<Type> possibleTypes() {
      final HashSet<Type> ret = new HashSet();
      this.forall(new P2SetVisitor() {
         public void visit(Node n) {
            Type t = n.getType();
            if (t instanceof RefType) {
               RefType rt = (RefType)t;
               if (rt.getSootClass().isAbstract()) {
                  return;
               }
            }

            ret.add(t);
         }
      });
      return ret;
   }

   public Type getType() {
      return this.type;
   }

   public void setType(Type type) {
      this.type = type;
   }

   public int size() {
      final int[] ret = new int[1];
      this.forall(new P2SetVisitor() {
         public void visit(Node n) {
            int var10002 = ret[0]++;
         }
      });
      return ret[0];
   }

   public String toString() {
      final StringBuffer ret = new StringBuffer();
      this.forall(new P2SetVisitor() {
         public final void visit(Node n) {
            ret.append("" + n + ",");
         }
      });
      return ret.toString();
   }

   public Set<String> possibleStringConstants() {
      final HashSet<String> ret = new HashSet();
      return this.forall(new P2SetVisitor() {
         public final void visit(Node n) {
            if (n instanceof StringConstantNode) {
               ret.add(((StringConstantNode)n).getString());
            } else {
               this.returnValue = true;
            }

         }
      }) ? null : ret;
   }

   public Set<ClassConstant> possibleClassConstants() {
      final HashSet<ClassConstant> ret = new HashSet();
      return this.forall(new P2SetVisitor() {
         public final void visit(Node n) {
            if (n instanceof ClassConstantNode) {
               ret.add(((ClassConstantNode)n).getClassConstant());
            } else {
               this.returnValue = true;
            }

         }
      }) ? null : ret;
   }

   protected BitVector getBitMask(PointsToSetInternal other, PAG pag) {
      BitVector mask = null;
      TypeManager typeManager = pag.getTypeManager();
      if (!typeManager.castNeverFails(other.getType(), this.getType())) {
         mask = typeManager.get(this.getType());
      }

      return mask;
   }

   public int pointsToSetHashCode() {
      PointsToSetInternal.P2SetVisitorInt visitor = new PointsToSetInternal.P2SetVisitorInt(1) {
         final int PRIME = 31;

         public void visit(Node n) {
            this.intValue = 31 * this.intValue + n.hashCode();
         }
      };
      this.forall(visitor);
      return visitor.intValue;
   }

   public boolean pointsToSetEquals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof PointsToSetInternal)) {
         return false;
      } else {
         PointsToSetInternal otherPts = (PointsToSetInternal)other;
         return this.superSetOf(otherPts, this) && this.superSetOf(this, otherPts);
      }
   }

   private boolean superSetOf(PointsToSetInternal onePts, final PointsToSetInternal otherPts) {
      return onePts.forall(new PointsToSetInternal.P2SetVisitorDefaultTrue() {
         public final void visit(Node n) {
            this.returnValue = this.returnValue && otherPts.contains(n);
         }
      });
   }

   public abstract static class P2SetVisitorInt extends P2SetVisitor {
      protected int intValue = 1;

      public P2SetVisitorInt(int i) {
      }
   }

   public abstract static class P2SetVisitorDefaultTrue extends P2SetVisitor {
      public P2SetVisitorDefaultTrue() {
         this.returnValue = true;
      }
   }
}
