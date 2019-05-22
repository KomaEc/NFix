package soot.dava.toolkits.base.renamer;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class heuristicTuple {
   BitSet heuristics;
   int bitSetSize;
   Vector<String> methodName;
   Vector<String> objectClassName;
   Vector<String> fieldName;
   Vector<String> castStrings;

   public heuristicTuple(int bits) {
      this.heuristics = new BitSet(bits);
      this.methodName = new Vector();
      this.objectClassName = new Vector();
      this.fieldName = new Vector();
      this.castStrings = new Vector();
      this.bitSetSize = bits;
   }

   public void addCastString(String castString) {
      this.castStrings.add(castString);
      this.setHeuristic(10);
   }

   public List<String> getCastStrings() {
      return this.castStrings;
   }

   public void setFieldName(String fieldName) {
      this.fieldName.add(fieldName);
      this.setHeuristic(8);
   }

   public List<String> getFieldName() {
      return this.fieldName;
   }

   public void setObjectClassName(String objectClassName) {
      this.objectClassName.add(objectClassName);
      this.setHeuristic(0);
   }

   public List<String> getObjectClassName() {
      return this.objectClassName;
   }

   public void setMethodName(String methodName) {
      this.methodName.add(methodName);
      this.setHeuristic(1);
      if (methodName.startsWith("get") || methodName.startsWith("set")) {
         this.setHeuristic(2);
      }

   }

   public List<String> getMethodName() {
      return this.methodName;
   }

   public void setHeuristic(int bitIndex) {
      this.heuristics.set(bitIndex);
   }

   public boolean getHeuristic(int bitIndex) {
      return this.heuristics.get(bitIndex);
   }

   public boolean isAnyHeuristicSet() {
      return !this.heuristics.isEmpty();
   }

   public String getPrint() {
      String temp = "BitSet: ";

      for(int i = 0; i < this.bitSetSize; ++i) {
         if (this.getHeuristic(i)) {
            temp = temp.concat("1");
         } else {
            temp = temp.concat("0");
         }
      }

      temp = temp.concat("  Field: " + this.fieldName.toString());
      temp = temp.concat("  Method: ");

      for(Iterator it = this.getMethodName().iterator(); it.hasNext(); temp = temp.concat((String)it.next() + " , ")) {
      }

      temp = temp.concat("  Class: " + this.objectClassName.toString());
      return temp;
   }
}
