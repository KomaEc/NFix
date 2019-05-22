package soot.jimple.spark.ondemand;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.ondemand.genericutil.Util;
import soot.jimple.spark.ondemand.pautil.SootUtil;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.SparkField;

public class InnerTypesIncrementalHeuristic implements FieldCheckHeuristic {
   private final TypeManager manager;
   private final Set<RefType> typesToCheck = new HashSet();
   private String newTypeOnQuery = null;
   private final Set<RefType> bothEndsTypes = new HashSet();
   private final Set<RefType> notBothEndsTypes = new HashSet();
   private int numPasses = 0;
   private final int passesInDirection;
   private boolean allNotBothEnds = false;

   public InnerTypesIncrementalHeuristic(TypeManager manager, int maxPasses) {
      this.manager = manager;
      this.passesInDirection = maxPasses / 2;
   }

   public boolean runNewPass() {
      ++this.numPasses;
      if (this.numPasses == this.passesInDirection) {
         return this.switchToNotBothEnds();
      } else if (this.newTypeOnQuery != null) {
         String topLevelTypeStr = Util.topLevelTypeString(this.newTypeOnQuery);
         boolean added;
         if (Scene.v().containsType(topLevelTypeStr)) {
            RefType refType = Scene.v().getRefType(topLevelTypeStr);
            added = this.typesToCheck.add(refType);
         } else {
            added = false;
         }

         this.newTypeOnQuery = null;
         return added;
      } else {
         return this.switchToNotBothEnds();
      }
   }

   private boolean switchToNotBothEnds() {
      if (!this.allNotBothEnds) {
         this.numPasses = 0;
         this.allNotBothEnds = true;
         this.newTypeOnQuery = null;
         this.typesToCheck.clear();
         return true;
      } else {
         return false;
      }
   }

   public boolean validateMatchesForField(SparkField field) {
      if (field instanceof ArrayElement) {
         return true;
      } else {
         SootField sootField = (SootField)field;
         RefType declaringType = sootField.getDeclaringClass().getType();
         String declaringTypeStr = declaringType.toString();
         String topLevel = Util.topLevelTypeString(declaringTypeStr);
         RefType refType;
         if (Scene.v().containsType(topLevel)) {
            refType = Scene.v().getRefType(topLevel);
         } else {
            refType = null;
         }

         Iterator var7 = this.typesToCheck.iterator();

         RefType checkedType;
         do {
            if (!var7.hasNext()) {
               if (this.newTypeOnQuery == null) {
                  this.newTypeOnQuery = declaringTypeStr;
               }

               return false;
            }

            checkedType = (RefType)var7.next();
         } while(!this.manager.castNeverFails(checkedType, refType));

         return true;
      }
   }

   public boolean validFromBothEnds(SparkField field) {
      if (this.allNotBothEnds) {
         return false;
      } else if (field instanceof ArrayElement) {
         return true;
      } else {
         SootField sootField = (SootField)field;
         RefType declaringType = sootField.getDeclaringClass().getType();
         if (this.bothEndsTypes.contains(declaringType)) {
            return true;
         } else if (this.notBothEndsTypes.contains(declaringType)) {
            return false;
         } else if (SootUtil.hasRecursiveField(declaringType.getSootClass())) {
            this.notBothEndsTypes.add(declaringType);
            return false;
         } else {
            this.bothEndsTypes.add(declaringType);
            return true;
         }
      }
   }

   public String toString() {
      return this.typesToCheck.toString();
   }
}
