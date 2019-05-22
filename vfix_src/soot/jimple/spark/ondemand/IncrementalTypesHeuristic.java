package soot.jimple.spark.ondemand;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.RefType;
import soot.SootField;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.ondemand.pautil.SootUtil;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.SparkField;

public class IncrementalTypesHeuristic implements FieldCheckHeuristic {
   private final TypeManager manager;
   private static final boolean EXCLUDE_TYPES = false;
   private static final String[] EXCLUDED_NAMES = new String[]{"ca.mcgill.sable.soot.SootMethod"};
   private Set<RefType> typesToCheck = new HashSet();
   private Set<RefType> notBothEndsTypes = new HashSet();
   private RefType newTypeOnQuery = null;

   public boolean runNewPass() {
      if (this.newTypeOnQuery != null) {
         boolean added = this.typesToCheck.add(this.newTypeOnQuery);
         if (SootUtil.hasRecursiveField(this.newTypeOnQuery.getSootClass())) {
            this.notBothEndsTypes.add(this.newTypeOnQuery);
         }

         this.newTypeOnQuery = null;
         return added;
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
         Iterator var4 = this.typesToCheck.iterator();

         RefType typeToCheck;
         do {
            if (!var4.hasNext()) {
               if (this.newTypeOnQuery == null) {
                  this.newTypeOnQuery = declaringType;
               }

               return false;
            }

            typeToCheck = (RefType)var4.next();
         } while(!this.manager.castNeverFails(declaringType, typeToCheck));

         return true;
      }
   }

   public IncrementalTypesHeuristic(TypeManager manager) {
      this.manager = manager;
   }

   public String toString() {
      StringBuffer ret = new StringBuffer();
      ret.append("types ");
      ret.append(this.typesToCheck.toString());
      if (!this.notBothEndsTypes.isEmpty()) {
         ret.append(" not both ");
         ret.append(this.notBothEndsTypes.toString());
      }

      return ret.toString();
   }

   public boolean validFromBothEnds(SparkField field) {
      if (field instanceof SootField) {
         SootField sootField = (SootField)field;
         RefType declaringType = sootField.getDeclaringClass().getType();
         Iterator var4 = this.notBothEndsTypes.iterator();

         while(var4.hasNext()) {
            RefType type = (RefType)var4.next();
            if (this.manager.castNeverFails(declaringType, type)) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean refineVirtualCall(SootUtil.CallSiteAndContext callSiteAndContext) {
      return true;
   }
}
