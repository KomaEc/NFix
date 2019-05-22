package soot.dava.toolkits.base.renamer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Local;

public class heuristicSet {
   HashMap<Local, heuristicTuple> set = new HashMap();

   private heuristicTuple getTuple(Local var) {
      return (heuristicTuple)this.set.get(var);
   }

   public void add(Local var, int bits) {
      heuristicTuple temp = new heuristicTuple(bits);
      this.set.put(var, temp);
   }

   public void addCastString(Local var, String castString) {
      heuristicTuple retrieved = this.getTuple(var);
      retrieved.addCastString(castString);
   }

   public List<String> getCastStrings(Local var) {
      heuristicTuple retrieved = this.getTuple(var);
      return retrieved.getCastStrings();
   }

   public void setFieldName(Local var, String fieldName) {
      heuristicTuple retrieved = this.getTuple(var);
      retrieved.setFieldName(fieldName);
   }

   public List<String> getFieldName(Local var) {
      heuristicTuple retrieved = this.getTuple(var);
      return retrieved.getFieldName();
   }

   public void setObjectClassName(Local var, String objectClassName) {
      heuristicTuple retrieved = this.getTuple(var);
      retrieved.setObjectClassName(objectClassName);
   }

   public List<String> getObjectClassName(Local var) {
      heuristicTuple retrieved = this.getTuple(var);
      return retrieved.getObjectClassName();
   }

   public void setMethodName(Local var, String methodName) {
      heuristicTuple retrieved = this.getTuple(var);
      retrieved.setMethodName(methodName);
   }

   public List<String> getMethodName(Local var) {
      heuristicTuple retrieved = this.getTuple(var);
      return retrieved.getMethodName();
   }

   public void setHeuristic(Local var, int bitIndex) {
      heuristicTuple retrieved = this.getTuple(var);
      retrieved.setHeuristic(bitIndex);
   }

   public boolean getHeuristic(Local var, int bitIndex) {
      heuristicTuple retrieved = this.getTuple(var);
      return retrieved.getHeuristic(bitIndex);
   }

   public boolean isAnyHeuristicSet(Local var) {
      heuristicTuple retrieved = this.getTuple(var);
      return retrieved.isAnyHeuristicSet();
   }

   public void print() {
      Iterator it = this.set.keySet().iterator();

      while(it.hasNext()) {
         Object local = it.next();
         heuristicTuple temp = (heuristicTuple)this.set.get(local);
         String tuple = temp.getPrint();
         System.out.println(local + "  " + tuple + " DefinedType: " + ((Local)local).getType());
      }

   }

   public Iterator<Local> getLocalsIterator() {
      return this.set.keySet().iterator();
   }

   public boolean contains(Local var) {
      return this.set.get(var) != null;
   }
}
