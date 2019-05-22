package soot.jimple.spark.ondemand;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;

public final class AllocAndContextSet extends ArraySet<AllocAndContext> implements EqualsSupportingPointsToSet {
   public boolean hasNonEmptyIntersection(PointsToSet other) {
      if (other instanceof AllocAndContextSet) {
         return this.nonEmptyHelper((AllocAndContextSet)other);
      } else if (other instanceof WrappedPointsToSet) {
         return this.hasNonEmptyIntersection(((WrappedPointsToSet)other).getWrapped());
      } else if (other instanceof PointsToSetInternal) {
         return ((PointsToSetInternal)other).forall(new P2SetVisitor() {
            public void visit(Node n) {
               if (!this.returnValue) {
                  Iterator var2 = AllocAndContextSet.this.iterator();

                  while(var2.hasNext()) {
                     AllocAndContext allocAndContext = (AllocAndContext)var2.next();
                     if (n.equals(allocAndContext.alloc)) {
                        this.returnValue = true;
                        break;
                     }
                  }
               }

            }
         });
      } else {
         throw new UnsupportedOperationException("can't check intersection with set of type " + other.getClass());
      }
   }

   private boolean nonEmptyHelper(AllocAndContextSet other) {
      Iterator var2 = other.iterator();

      while(var2.hasNext()) {
         AllocAndContext otherAllocAndContext = (AllocAndContext)var2.next();
         Iterator var4 = this.iterator();

         while(var4.hasNext()) {
            AllocAndContext myAllocAndContext = (AllocAndContext)var4.next();
            if (otherAllocAndContext.alloc.equals(myAllocAndContext.alloc)) {
               ImmutableStack<Integer> myContext = myAllocAndContext.context;
               ImmutableStack<Integer> otherContext = otherAllocAndContext.context;
               if (myContext.topMatches(otherContext) || otherContext.topMatches(myContext)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public Set<ClassConstant> possibleClassConstants() {
      Set<ClassConstant> res = new HashSet();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         AllocAndContext allocAndContext = (AllocAndContext)var2.next();
         AllocNode n = allocAndContext.alloc;
         if (!(n instanceof ClassConstantNode)) {
            return null;
         }

         res.add(((ClassConstantNode)n).getClassConstant());
      }

      return res;
   }

   public Set<String> possibleStringConstants() {
      Set<String> res = new HashSet();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         AllocAndContext allocAndContext = (AllocAndContext)var2.next();
         AllocNode n = allocAndContext.alloc;
         if (!(n instanceof StringConstantNode)) {
            return null;
         }

         res.add(((StringConstantNode)n).getString());
      }

      return res;
   }

   public Set<Type> possibleTypes() {
      Set res = new HashSet();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         AllocAndContext allocAndContext = (AllocAndContext)var2.next();
         res.add(allocAndContext.alloc.getType());
      }

      return res;
   }

   public int pointsToSetHashCode() {
      int PRIME = true;
      int result = 1;

      AllocAndContext elem;
      for(Iterator var3 = this.iterator(); var3.hasNext(); result = 31 * result + elem.hashCode()) {
         elem = (AllocAndContext)var3.next();
      }

      return result;
   }

   public boolean pointsToSetEquals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof AllocAndContextSet)) {
         return false;
      } else {
         AllocAndContextSet otherPts = (AllocAndContextSet)other;
         return this.superSetOf(otherPts, this) && this.superSetOf(this, otherPts);
      }
   }

   private boolean superSetOf(AllocAndContextSet onePts, AllocAndContextSet otherPts) {
      return onePts.containsAll(otherPts);
   }
}
