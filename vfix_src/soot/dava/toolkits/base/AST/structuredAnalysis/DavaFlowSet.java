package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.traversals.ClosestAbruptTargetFinder;
import soot.toolkits.scalar.AbstractFlowSet;
import soot.toolkits.scalar.FlowSet;

public class DavaFlowSet<T> extends AbstractFlowSet<T> {
   static final int DEFAULT_SIZE = 8;
   int numElements;
   int maxElements;
   protected T[] elements;
   HashMap<Serializable, List<DavaFlowSet<T>>> breakList;
   HashMap<Serializable, List<DavaFlowSet<T>>> continueList;
   HashMap<Serializable, List<DavaFlowSet<T>>> implicitBreaks;
   HashMap<Serializable, List<DavaFlowSet<T>>> implicitContinues;

   public DavaFlowSet() {
      this.maxElements = 8;
      this.elements = (Object[])(new Object[8]);
      this.numElements = 0;
      this.breakList = new HashMap();
      this.continueList = new HashMap();
      this.implicitBreaks = new HashMap();
      this.implicitContinues = new HashMap();
   }

   public DavaFlowSet(DavaFlowSet<T> other) {
      this.numElements = other.numElements;
      this.maxElements = other.maxElements;
      this.elements = (Object[])other.elements.clone();
      this.breakList = (HashMap)other.breakList.clone();
      this.continueList = (HashMap)other.continueList.clone();
      this.implicitBreaks = (HashMap)other.implicitBreaks.clone();
      this.implicitContinues = (HashMap)other.implicitContinues.clone();
   }

   private boolean sameType(Object flowSet) {
      return flowSet instanceof DavaFlowSet;
   }

   public DavaFlowSet<T> clone() {
      return new DavaFlowSet(this);
   }

   public FlowSet<T> emptySet() {
      return new DavaFlowSet();
   }

   public void clear() {
      this.numElements = 0;
   }

   public int size() {
      return this.numElements;
   }

   public boolean isEmpty() {
      return this.numElements == 0;
   }

   public List<T> toList() {
      T[] copiedElements = (Object[])(new Object[this.numElements]);
      System.arraycopy(this.elements, 0, copiedElements, 0, this.numElements);
      return Arrays.asList(copiedElements);
   }

   public void add(T e) {
      if (!this.contains(e)) {
         if (this.numElements == this.maxElements) {
            this.doubleCapacity();
         }

         this.elements[this.numElements++] = e;
      }

   }

   private void doubleCapacity() {
      int newSize = this.maxElements * 2;
      T[] newElements = (Object[])(new Object[newSize]);
      System.arraycopy(this.elements, 0, newElements, 0, this.numElements);
      this.elements = newElements;
      this.maxElements = newSize;
   }

   public void remove(Object obj) {
      for(int i = 0; i < this.numElements; ++i) {
         if (this.elements[i].equals(obj)) {
            this.remove(i);
            break;
         }
      }

   }

   public void remove(int idx) {
      this.elements[idx] = this.elements[--this.numElements];
   }

   public void union(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         DavaFlowSet<T> other = (DavaFlowSet)otherFlow;
         DavaFlowSet<T> dest = (DavaFlowSet)destFlow;
         int i;
         if (dest == other) {
            for(i = 0; i < this.numElements; ++i) {
               dest.add(this.elements[i]);
            }
         } else {
            if (this != dest) {
               this.copy(dest);
            }

            for(i = 0; i < other.numElements; ++i) {
               dest.add(other.elements[i]);
            }
         }
      } else {
         super.union(otherFlow, destFlow);
      }

   }

   public void intersection(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         DavaFlowSet<T> other = (DavaFlowSet)otherFlow;
         DavaFlowSet<T> dest = (DavaFlowSet)destFlow;
         DavaFlowSet workingSet;
         if (dest != other && dest != this) {
            workingSet = dest;
            dest.clear();
         } else {
            workingSet = new DavaFlowSet();
         }

         for(int i = 0; i < this.numElements; ++i) {
            if (other.contains(this.elements[i])) {
               workingSet.add(this.elements[i]);
            }
         }

         if (workingSet != dest) {
            workingSet.copy(dest);
         }
      } else {
         super.intersection(otherFlow, destFlow);
      }

   }

   public void difference(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         DavaFlowSet<T> other = (DavaFlowSet)otherFlow;
         DavaFlowSet<T> dest = (DavaFlowSet)destFlow;
         DavaFlowSet workingSet;
         if (dest != other && dest != this) {
            workingSet = dest;
            dest.clear();
         } else {
            workingSet = new DavaFlowSet();
         }

         for(int i = 0; i < this.numElements; ++i) {
            if (!other.contains(this.elements[i])) {
               workingSet.add(this.elements[i]);
            }
         }

         if (workingSet != dest) {
            workingSet.copy(dest);
         }
      } else {
         super.difference(otherFlow, destFlow);
      }

   }

   public boolean contains(Object obj) {
      for(int i = 0; i < this.numElements; ++i) {
         if (this.elements[i].equals(obj)) {
            return true;
         }
      }

      return false;
   }

   public boolean equals(Object otherFlow) {
      if (this.sameType(otherFlow)) {
         DavaFlowSet<T> other = (DavaFlowSet)otherFlow;
         if (other.numElements != this.numElements) {
            return false;
         } else {
            int size = this.numElements;

            for(int i = 0; i < size; ++i) {
               if (!other.contains(this.elements[i])) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return super.equals(otherFlow);
      }
   }

   public void copy(FlowSet<T> destFlow) {
      if (this != destFlow) {
         if (this.sameType(destFlow)) {
            DavaFlowSet dest = (DavaFlowSet)destFlow;

            while(dest.maxElements < this.maxElements) {
               dest.doubleCapacity();
            }

            dest.numElements = this.numElements;
            System.arraycopy(this.elements, 0, dest.elements, 0, this.numElements);
         } else {
            super.copy(destFlow);
         }

      }
   }

   private List<DavaFlowSet<T>> addIfNotDuplicate(List<DavaFlowSet<T>> into, DavaFlowSet<T> addThis) {
      Iterator<DavaFlowSet<T>> it = into.iterator();
      boolean found = false;

      while(it.hasNext()) {
         DavaFlowSet<T> temp = (DavaFlowSet)it.next();
         if (temp.equals(addThis) && temp.internalDataMatchesTo(addThis)) {
            found = true;
            break;
         }
      }

      if (!found) {
         into.add(addThis);
      }

      return into;
   }

   public void addToBreakList(String labelBroken, DavaFlowSet<T> set) {
      List<DavaFlowSet<T>> labelsBreakList = (List)this.breakList.get(labelBroken);
      if (labelsBreakList == null) {
         List<DavaFlowSet<T>> labelsBreakList = new ArrayList();
         labelsBreakList.add(set);
         this.breakList.put(labelBroken, labelsBreakList);
      } else {
         this.breakList.put(labelBroken, this.addIfNotDuplicate(labelsBreakList, set));
      }

   }

   public void addToContinueList(String labelContinued, DavaFlowSet<T> set) {
      List<DavaFlowSet<T>> labelsContinueList = (List)this.continueList.get(labelContinued);
      if (labelsContinueList == null) {
         List<DavaFlowSet<T>> labelsContinueList = new ArrayList();
         labelsContinueList.add(set);
         this.continueList.put(labelContinued, labelsContinueList);
      } else {
         this.continueList.put(labelContinued, this.addIfNotDuplicate(labelsContinueList, set));
      }

   }

   private boolean checkImplicit(DAbruptStmt ab) {
      SETNodeLabel label = ab.getLabel();
      if (label == null) {
         return true;
      } else {
         return label.toString() == null;
      }
   }

   public void addToImplicitBreaks(DAbruptStmt ab, DavaFlowSet<T> set) {
      if (!this.checkImplicit(ab)) {
         throw new RuntimeException("Tried to add explicit break statement in the implicit list in");
      } else if (!ab.is_Break()) {
         throw new RuntimeException("Tried to add continue statement in the break list in DavaFlowSet.addToImplicitBreaks");
      } else {
         ASTNode node = ClosestAbruptTargetFinder.v().getTarget(ab);
         List<DavaFlowSet<T>> listSets = (List)this.implicitBreaks.get(node);
         if (listSets == null) {
            listSets = new ArrayList();
         }

         this.implicitBreaks.put(node, this.addIfNotDuplicate((List)listSets, set));
      }
   }

   public void addToImplicitContinues(DAbruptStmt ab, DavaFlowSet<T> set) {
      if (!this.checkImplicit(ab)) {
         throw new RuntimeException("Tried to add explicit continue statement in the implicit list ");
      } else if (!ab.is_Continue()) {
         throw new RuntimeException("Tried to add break statement in the continue list");
      } else {
         ASTNode node = ClosestAbruptTargetFinder.v().getTarget(ab);
         List<DavaFlowSet<T>> listSets = (List)this.implicitContinues.get(node);
         if (listSets == null) {
            listSets = new ArrayList();
         }

         this.implicitContinues.put(node, this.addIfNotDuplicate((List)listSets, set));
      }
   }

   private HashMap<Serializable, List<DavaFlowSet<T>>> getBreakList() {
      return this.breakList;
   }

   private HashMap<Serializable, List<DavaFlowSet<T>>> getContinueList() {
      return this.continueList;
   }

   public HashMap<Serializable, List<DavaFlowSet<T>>> getImplicitBreaks() {
      return this.implicitBreaks;
   }

   public HashMap<Serializable, List<DavaFlowSet<T>>> getImplicitContinues() {
      return this.implicitContinues;
   }

   public List<DavaFlowSet<T>> getImplicitlyBrokenSets(ASTNode node) {
      List<DavaFlowSet<T>> toReturn = (List)this.implicitBreaks.get(node);
      return toReturn != null ? toReturn : null;
   }

   public List<DavaFlowSet<T>> getImplicitlyContinuedSets(ASTNode node) {
      List<DavaFlowSet<T>> toReturn = (List)this.implicitContinues.get(node);
      return toReturn != null ? toReturn : null;
   }

   private List<DavaFlowSet<T>> copyDavaFlowSetList(List<DavaFlowSet<T>> currentList, List<DavaFlowSet<T>> temp) {
      Iterator tempIt = temp.iterator();

      while(tempIt.hasNext()) {
         DavaFlowSet<T> check = (DavaFlowSet)tempIt.next();
         Iterator<DavaFlowSet<T>> currentListIt = currentList.iterator();
         boolean found = false;

         while(currentListIt.hasNext()) {
            DavaFlowSet<T> currentSet = (DavaFlowSet)currentListIt.next();
            if (check.equals(currentSet) && check.internalDataMatchesTo(currentSet)) {
               found = true;
               break;
            }
         }

         if (!found) {
            currentList.add(check);
         }
      }

      return currentList;
   }

   public void copyInternalDataFrom(DavaFlowSet<T> fromThis) {
      if (this.sameType(fromThis)) {
         Map<Serializable, List<DavaFlowSet<T>>> copyThis = fromThis.getBreakList();
         Iterator it = copyThis.keySet().iterator();

         String labelContinued;
         List fromDavaFlowSets;
         List toDavaFlowSets;
         List complete;
         while(it.hasNext()) {
            labelContinued = (String)it.next();
            fromDavaFlowSets = (List)copyThis.get(labelContinued);
            toDavaFlowSets = (List)this.breakList.get(labelContinued);
            if (toDavaFlowSets == null) {
               this.breakList.put(labelContinued, fromDavaFlowSets);
            } else {
               complete = this.copyDavaFlowSetList(toDavaFlowSets, fromDavaFlowSets);
               this.breakList.put(labelContinued, complete);
            }
         }

         copyThis = fromThis.getContinueList();
         it = copyThis.keySet().iterator();

         while(it.hasNext()) {
            labelContinued = (String)it.next();
            fromDavaFlowSets = (List)copyThis.get(labelContinued);
            toDavaFlowSets = (List)this.continueList.get(labelContinued);
            if (toDavaFlowSets == null) {
               this.continueList.put(labelContinued, fromDavaFlowSets);
            } else {
               complete = this.copyDavaFlowSetList(toDavaFlowSets, fromDavaFlowSets);
               this.continueList.put(labelContinued, complete);
            }
         }

         copyThis = fromThis.getImplicitBreaks();
         it = copyThis.keySet().iterator();

         ASTNode node;
         while(it.hasNext()) {
            node = (ASTNode)it.next();
            fromDavaFlowSets = (List)copyThis.get(node);
            toDavaFlowSets = (List)this.implicitBreaks.get(node);
            if (toDavaFlowSets == null) {
               this.implicitBreaks.put(node, fromDavaFlowSets);
            } else {
               complete = this.copyDavaFlowSetList(toDavaFlowSets, fromDavaFlowSets);
               this.implicitBreaks.put(node, complete);
            }
         }

         copyThis = fromThis.getImplicitContinues();
         it = copyThis.keySet().iterator();

         while(it.hasNext()) {
            node = (ASTNode)it.next();
            fromDavaFlowSets = (List)copyThis.get(node);
            toDavaFlowSets = (List)this.implicitContinues.get(node);
            if (toDavaFlowSets == null) {
               this.implicitContinues.put(node, fromDavaFlowSets);
            } else {
               complete = this.copyDavaFlowSetList(toDavaFlowSets, fromDavaFlowSets);
               this.implicitContinues.put(node, complete);
            }
         }

      }
   }

   private <X> boolean compareLists(List<X> listOne, List<X> listTwo) {
      if (listOne == null && listTwo == null) {
         return true;
      } else if (listOne != null && listTwo != null) {
         if (listOne.size() != listTwo.size()) {
            return false;
         } else {
            Iterator<X> listOneIt = listOne.iterator();

            for(boolean found = false; listOneIt.hasNext(); found = false) {
               Object listOneObj = listOneIt.next();
               Iterator listTwoIt = listTwo.iterator();

               while(listTwoIt.hasNext()) {
                  Object listTwoObj = listTwoIt.next();
                  if (listOneObj.equals(listTwoObj)) {
                     found = true;
                     break;
                  }
               }

               if (!found) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public boolean internalDataMatchesTo(Object otherObj) {
      if (!(otherObj instanceof DavaFlowSet)) {
         return false;
      } else {
         DavaFlowSet<T> other = (DavaFlowSet)otherObj;
         HashMap<Serializable, List<DavaFlowSet<T>>> otherMap = other.getBreakList();
         if (!this.compareHashMaps(this.breakList, otherMap)) {
            return false;
         } else {
            otherMap = other.getContinueList();
            if (!this.compareHashMaps(this.continueList, otherMap)) {
               return false;
            } else {
               otherMap = other.getImplicitBreaks();
               if (!this.compareHashMaps(this.implicitBreaks, otherMap)) {
                  return false;
               } else {
                  otherMap = other.getImplicitContinues();
                  return this.compareHashMaps(this.implicitContinues, otherMap);
               }
            }
         }
      }
   }

   private boolean compareHashMaps(HashMap<Serializable, List<DavaFlowSet<T>>> thisMap, HashMap<Serializable, List<DavaFlowSet<T>>> otherMap) {
      List<String> otherKeyList = new ArrayList();
      Iterator keys = otherMap.keySet().iterator();

      String key;
      while(keys.hasNext()) {
         key = (String)keys.next();
         otherKeyList.add(key);
         List<DavaFlowSet<T>> listOther = (List)otherMap.get(key);
         List<DavaFlowSet<T>> listThis = (List)thisMap.get(key);
         if (!this.compareLists(listOther, listThis)) {
            return false;
         }
      }

      keys = thisMap.keySet().iterator();

      boolean alreadyDone;
      do {
         if (!keys.hasNext()) {
            return true;
         }

         key = (String)keys.next();
         Iterator<String> keyListIt = otherKeyList.iterator();
         alreadyDone = false;

         while(keyListIt.hasNext()) {
            String doneKey = (String)keyListIt.next();
            if (key.equals(doneKey)) {
               alreadyDone = true;
               break;
            }
         }
      } while(alreadyDone);

      return false;
   }

   public List<DavaFlowSet<T>> getContinueSet(String label) {
      return (List)this.continueList.remove(label);
   }

   public List<DavaFlowSet<T>> getBreakSet(String label) {
      return (List)this.breakList.remove(label);
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append(" SET={");

      for(int i = 0; i < this.numElements; ++i) {
         if (i != 0) {
            b.append(" , ");
         }

         b.append(this.elements[i].toString());
      }

      b.append(" }");
      return b.toString();
   }

   public Iterator<T> iterator() {
      return new Iterator<T>() {
         int lastIdx = 0;

         public boolean hasNext() {
            return this.lastIdx < DavaFlowSet.this.numElements;
         }

         public T next() {
            return DavaFlowSet.this.elements[this.lastIdx++];
         }

         public void remove() {
            DavaFlowSet.this.remove(--this.lastIdx);
         }
      };
   }

   public int getElementCount() {
      return this.elements.length;
   }

   public T getElementAt(int idx) {
      return this.elements[idx];
   }
}
