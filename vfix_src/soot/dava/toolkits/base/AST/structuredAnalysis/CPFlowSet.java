package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.HashMap;
import soot.dava.DecompilationException;
import soot.toolkits.scalar.FlowSet;

public class CPFlowSet extends DavaFlowSet<CPTuple> {
   public CPFlowSet() {
   }

   public CPFlowSet(CPFlowSet other) {
      this.numElements = other.numElements;
      this.maxElements = other.maxElements;
      this.elements = new CPTuple[other.getElementCount()];

      for(int i = 0; i < other.getElementCount(); ++i) {
         if (other.getElementAt(i) != null) {
            ((CPTuple[])this.elements)[i] = ((CPTuple)other.getElementAt(i)).clone();
         } else {
            ((CPTuple[])this.elements)[i] = null;
         }
      }

      this.breakList = (HashMap)other.breakList.clone();
      this.continueList = (HashMap)other.continueList.clone();
      this.implicitBreaks = (HashMap)other.implicitBreaks.clone();
      this.implicitContinues = (HashMap)other.implicitContinues.clone();
   }

   public Object contains(String className, String localOrField) {
      for(int i = 0; i < this.numElements; ++i) {
         CPTuple current = (CPTuple)this.getElementAt(i);
         if (current.getSootClassName().equals(className)) {
            if (current.containsField()) {
               if (current.getVariable().getSootField().getName().equals(localOrField)) {
                  return current.getValue();
               }
            } else if (current.containsLocal() && current.getVariable().getLocal().getName().equals(localOrField)) {
               return current.getValue();
            }
         }
      }

      return null;
   }

   public void addIfNotPresent(CPTuple newTuple) {
      for(int i = 0; i < this.numElements; ++i) {
         CPTuple current = ((CPTuple[])this.elements)[i];
         if (current.getSootClassName().equals(newTuple.getSootClassName())) {
            CPVariable curVar = current.getVariable();
            CPVariable newTupleVar = newTuple.getVariable();
            if (curVar.equals(newTupleVar)) {
               current.setValue(newTuple.getValue());
               return;
            }
         }
      }

      this.add(newTuple);
   }

   public void addIfNotPresentButDontUpdate(CPTuple newTuple) {
      for(int i = 0; i < this.numElements; ++i) {
         CPTuple current = ((CPTuple[])this.elements)[i];
         if (current.getSootClassName().equals(newTuple.getSootClassName())) {
            CPVariable curVar = current.getVariable();
            CPVariable newTupleVar = newTuple.getVariable();
            if (curVar.equals(newTupleVar)) {
               if (current.isTop()) {
                  current.setValue(newTuple.getValue());
               }

               return;
            }
         }
      }

   }

   public void intersection(FlowSet otherFlow, FlowSet destFlow) {
      if (otherFlow instanceof CPFlowSet && destFlow instanceof CPFlowSet) {
         CPFlowSet other = (CPFlowSet)otherFlow;
         CPFlowSet dest = (CPFlowSet)destFlow;
         CPFlowSet workingSet;
         if (dest != other && dest != this) {
            workingSet = dest;
            dest.clear();
         } else {
            workingSet = new CPFlowSet();
         }

         int i;
         CPTuple thisTuple;
         String className;
         CPVariable thisVar;
         String thisClassName;
         for(i = 0; i < this.numElements; ++i) {
            thisTuple = (CPTuple)this.getElementAt(i);
            className = thisTuple.getSootClassName();
            thisVar = thisTuple.getVariable();
            CPTuple matchFound = null;
            CPTuple otherTuple = null;

            for(int j = 0; j < other.numElements; ++j) {
               otherTuple = (CPTuple)other.getElementAt(j);
               thisClassName = otherTuple.getSootClassName();
               if (thisClassName.equals(className) && otherTuple.getVariable().equals(thisVar)) {
                  matchFound = otherTuple;
                  break;
               }
            }

            if (matchFound != null) {
               if (thisTuple.isTop()) {
                  workingSet.add(thisTuple.clone());
               } else if (matchFound.isTop()) {
                  workingSet.add(matchFound.clone());
               } else {
                  if (matchFound.isTop() || thisTuple.isTop()) {
                     throw new DecompilationException("Ran out of cases in CPVariable values...report bug to developer");
                  }

                  Object matchedValue = matchFound.getValue();
                  Object thisValue = thisTuple.getValue();
                  if (matchedValue.equals(thisValue)) {
                     workingSet.add(thisTuple.clone());
                  } else {
                     workingSet.add(new CPTuple(className, thisVar, true));
                  }
               }
            } else {
               workingSet.add(thisTuple.clone());
            }
         }

         for(i = 0; i < other.numElements; ++i) {
            thisTuple = (CPTuple)other.getElementAt(i);
            className = thisTuple.getSootClassName();
            thisVar = thisTuple.getVariable();
            boolean inBoth = false;

            for(int j = 0; j < this.numElements; ++j) {
               CPTuple thisTuple = (CPTuple)this.getElementAt(j);
               thisClassName = thisTuple.getSootClassName();
               CPVariable thisVar = thisTuple.getVariable();
               if (className.equals(thisClassName) && thisVar.equals(thisVar)) {
                  inBoth = true;
                  break;
               }
            }

            if (!inBoth) {
               workingSet.add(thisTuple.clone());
            }
         }

      } else {
         super.intersection(otherFlow, destFlow);
      }
   }

   public CPFlowSet clone() {
      return new CPFlowSet(this);
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append("Printing CPFlowSet: ");

      for(int i = 0; i < this.numElements; ++i) {
         b.append("\n" + ((CPTuple[])this.elements)[i].toString());
      }

      b.append("\n");
      return b.toString();
   }
}
