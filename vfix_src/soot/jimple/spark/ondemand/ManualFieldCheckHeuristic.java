package soot.jimple.spark.ondemand;

import soot.SootField;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.SparkField;

public class ManualFieldCheckHeuristic implements FieldCheckHeuristic {
   private boolean allNotBothEnds = false;
   private static final String[] importantTypes = new String[]{"java.util.Vector", "java.util.Hashtable", "java.util.Hashtable$Entry", "java.util.Hashtable$Enumerator", "java.util.LinkedList", "java.util.LinkedList$Entry", "java.util.AbstractList$Itr", "java.util.Vector$1", "java.util.ArrayList"};
   private static final String[] notBothEndsTypes = new String[]{"java.util.Hashtable$Entry", "java.util.LinkedList$Entry"};

   public boolean runNewPass() {
      if (!this.allNotBothEnds) {
         this.allNotBothEnds = true;
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
         String fieldTypeStr = sootField.getDeclaringClass().getType().toString();
         String[] var4 = importantTypes;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String typeName = var4[var6];
            if (fieldTypeStr.equals(typeName)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean validFromBothEnds(SparkField field) {
      if (this.allNotBothEnds) {
         return false;
      } else {
         if (field instanceof SootField) {
            SootField sootField = (SootField)field;
            String fieldTypeStr = sootField.getDeclaringClass().getType().toString();
            String[] var4 = notBothEndsTypes;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String typeName = var4[var6];
               if (fieldTypeStr.equals(typeName)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public String toString() {
      return "Manual annotations";
   }
}
