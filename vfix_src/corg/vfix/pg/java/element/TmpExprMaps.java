package corg.vfix.pg.java.element;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import corg.vfix.fl.stack.StackTrace;
import corg.vfix.sa.vfg.build.NPELocator;
import java.util.HashMap;
import soot.Local;
import soot.SootMethod;

public class TmpExprMaps {
   private static HashMap<SootMethod, TmpExprMap> map = new HashMap();

   public static Expression getTmpName(SootMethod mtd, Local local) {
      if (local.equals(StackTrace.getNullPointer()) && NPELocator.getNullName().charAt(0) != '$') {
         return new NameExpr(NPELocator.getNullName());
      } else {
         TmpExprMap extractor;
         if (!map.containsKey(mtd)) {
            extractor = new TmpExprMap(mtd);
            map.put(mtd, extractor);
         } else {
            extractor = (TmpExprMap)map.get(mtd);
         }

         return extractor.getTmpName(local);
      }
   }

   public static boolean isTmpLocal(Local local) {
      String nullName = StackTrace.getNullPointer() == null ? "" : StackTrace.getNullPointer().toString();
      return nullName.equals(local.getName()) || local.getName().charAt(0) == '$';
   }

   public static boolean isTmpLocal(String name) {
      String nullName = StackTrace.getNullPointer() == null ? "" : StackTrace.getNullPointer().toString();
      return name.charAt(0) == '$' || nullName.equals(name);
   }

   public static boolean hasVarNum(String name) {
      return name.contains("#");
   }

   public static String removeVarNum(String name) {
      if (!hasVarNum(name)) {
         return name;
      } else {
         String newName = name;
         int start = name.indexOf("#");

         int i;
         for(i = start + 1; i < newName.length(); ++i) {
            char c = newName.charAt(i);
            if (c < '0' || c > '9') {
               break;
            }
         }

         System.out.println("End Index:" + i);
         String subStr = newName.substring(start, i);
         newName = newName.replaceFirst(subStr, "");
         return removeVarNum(newName);
      }
   }

   public static void main(String[] args) {
      String a = "temp#2+temp#245";
      System.out.println(removeVarNum(a));
   }
}
