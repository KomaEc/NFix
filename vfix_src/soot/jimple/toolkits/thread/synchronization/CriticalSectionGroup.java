package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Value;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;
import soot.jimple.toolkits.pointer.RWSet;

class CriticalSectionGroup implements Iterable<CriticalSection> {
   int groupNum;
   List<CriticalSection> criticalSections;
   RWSet rwSet;
   public boolean isDynamicLock;
   public boolean useDynamicLock;
   public Value lockObject;
   public boolean useLocksets;

   public CriticalSectionGroup(int groupNum) {
      this.groupNum = groupNum;
      this.criticalSections = new ArrayList();
      this.rwSet = new CodeBlockRWSet();
      this.isDynamicLock = false;
      this.useDynamicLock = false;
      this.lockObject = null;
      this.useLocksets = false;
   }

   public int num() {
      return this.groupNum;
   }

   public int size() {
      return this.criticalSections.size();
   }

   public void add(CriticalSection tn) {
      tn.setNumber = this.groupNum;
      tn.group = this;
      if (!this.criticalSections.contains(tn)) {
         this.criticalSections.add(tn);
      }

   }

   public boolean contains(CriticalSection tn) {
      return this.criticalSections.contains(tn);
   }

   public Iterator<CriticalSection> iterator() {
      return this.criticalSections.iterator();
   }

   public void mergeGroups(CriticalSectionGroup other) {
      if (other != this) {
         Iterator tnIt = other.criticalSections.iterator();

         while(tnIt.hasNext()) {
            CriticalSection tn = (CriticalSection)tnIt.next();
            this.add(tn);
         }

         other.criticalSections.clear();
      }
   }
}
