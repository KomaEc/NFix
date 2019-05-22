package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Hierarchy;
import soot.Local;
import soot.PointsToAnalysis;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;
import soot.jimple.toolkits.thread.mhp.MhpTester;

public class CriticalSectionInterferenceGraph {
   int nextGroup;
   List<CriticalSectionGroup> groups;
   List<CriticalSection> criticalSections;
   MhpTester mhp;
   PointsToAnalysis pta;
   boolean optionOneGlobalLock = false;
   boolean optionLeaveOriginalLocks = false;
   boolean optionIncludeEmptyPossibleEdges = false;

   public CriticalSectionInterferenceGraph(List<CriticalSection> criticalSections, MhpTester mhp, boolean optionOneGlobalLock, boolean optionLeaveOriginalLocks, boolean optionIncludeEmptyPossibleEdges) {
      this.criticalSections = criticalSections;
      this.mhp = mhp;
      this.pta = Scene.v().getPointsToAnalysis();
      this.optionOneGlobalLock = optionOneGlobalLock;
      this.optionLeaveOriginalLocks = optionLeaveOriginalLocks;
      this.optionIncludeEmptyPossibleEdges = optionIncludeEmptyPossibleEdges;
      this.calculateGroups();
   }

   public int groupCount() {
      return this.nextGroup;
   }

   public List<CriticalSectionGroup> groups() {
      return this.groups;
   }

   public void calculateGroups() {
      this.nextGroup = 1;
      this.groups = new ArrayList();
      this.groups.add(new CriticalSectionGroup(0));
      if (this.optionOneGlobalLock) {
         CriticalSectionGroup onlyGroup = new CriticalSectionGroup(this.nextGroup);
         Iterator tnIt1 = this.criticalSections.iterator();

         while(tnIt1.hasNext()) {
            CriticalSection tn1 = (CriticalSection)tnIt1.next();
            onlyGroup.add(tn1);
         }

         ++this.nextGroup;
         this.groups.add(onlyGroup);
      } else {
         Iterator tnIt1 = this.criticalSections.iterator();

         while(true) {
            label196:
            while(true) {
               CriticalSection tn1;
               do {
                  if (!tnIt1.hasNext()) {
                     return;
                  }

                  tn1 = (CriticalSection)tnIt1.next();
               } while(tn1.setNumber == -1);

               if (tn1.read.size() == 0 && tn1.write.size() == 0 && !this.optionLeaveOriginalLocks) {
                  tn1.setNumber = -1;
               } else {
                  Iterator tnIt2 = this.criticalSections.iterator();

                  while(true) {
                     CriticalSection tn2;
                     boolean typeCompatible;
                     boolean emptyEdge;
                     RefLikeType typeOne;
                     do {
                        do {
                           do {
                              if (!tnIt2.hasNext()) {
                                 if (tn1.setNumber == 0) {
                                    tn1.setNumber = -1;
                                 }
                                 continue label196;
                              }

                              tn2 = (CriticalSection)tnIt2.next();
                           } while(tn2.setNumber == -1);
                        } while(!this.mayHappenInParallel(tn1, tn2));

                        SootClass classOne = null;
                        SootClass classTwo = null;
                        typeCompatible = false;
                        emptyEdge = false;
                        if (tn1.origLock != null && tn2.origLock != null) {
                           if (tn1.origLock != null && tn2.origLock != null) {
                              if (tn1.origLock instanceof Local && tn2.origLock instanceof Local) {
                                 emptyEdge = !this.pta.reachingObjects((Local)tn1.origLock).hasNonEmptyIntersection(this.pta.reachingObjects((Local)tn2.origLock));
                              } else {
                                 emptyEdge = !tn1.origLock.equals(tn2.origLock);
                              }
                           } else {
                              emptyEdge = true;
                           }

                           typeOne = (RefLikeType)tn1.origLock.getType();
                           RefLikeType typeTwo = (RefLikeType)tn2.origLock.getType();
                           classOne = typeOne instanceof RefType ? ((RefType)typeOne).getSootClass() : null;
                           classTwo = typeTwo instanceof RefType ? ((RefType)typeTwo).getSootClass() : null;
                           if (classOne != null && classTwo != null) {
                              Hierarchy h = Scene.v().getActiveHierarchy();
                              if (classOne.isInterface()) {
                                 if (!classTwo.isInterface()) {
                                    typeCompatible = h.getImplementersOf(classOne).contains(classTwo);
                                 } else {
                                    typeCompatible = h.getSubinterfacesOfIncluding(classOne).contains(classTwo) || h.getSubinterfacesOfIncluding(classTwo).contains(classOne);
                                 }
                              } else if (classTwo.isInterface()) {
                                 typeCompatible = h.getImplementersOf(classTwo).contains(classOne);
                              } else {
                                 typeCompatible = classOne != null && Scene.v().getActiveHierarchy().getSubclassesOfIncluding(classOne).contains(classTwo) || classTwo != null && Scene.v().getActiveHierarchy().getSubclassesOfIncluding(classTwo).contains(classOne);
                              }
                           }
                        }
                     } while((this.optionLeaveOriginalLocks || !tn1.write.hasNonEmptyIntersection(tn2.write) && !tn1.write.hasNonEmptyIntersection(tn2.read) && !tn1.read.hasNonEmptyIntersection(tn2.write)) && (!this.optionLeaveOriginalLocks || !typeCompatible || !this.optionIncludeEmptyPossibleEdges && emptyEdge));

                     typeOne = null;
                     CodeBlockRWSet rw;
                     int size;
                     if (this.optionLeaveOriginalLocks) {
                        rw = new CodeBlockRWSet();
                        size = emptyEdge ? 0 : 1;
                     } else {
                        rw = tn1.write.intersection(tn2.write);
                        rw.union(tn1.write.intersection(tn2.read));
                        rw.union(tn1.read.intersection(tn2.write));
                        size = rw.size();
                     }

                     tn1.edges.add(new CriticalSectionDataDependency(tn2, size, rw));
                     if (size > 0) {
                        if (tn1.setNumber > 0) {
                           if (tn2.setNumber == 0) {
                              tn1.group.add(tn2);
                           } else if (tn2.setNumber > 0 && tn1.setNumber != tn2.setNumber) {
                              tn1.group.mergeGroups(tn2.group);
                           }
                        } else if (tn1.setNumber == 0) {
                           if (tn2.setNumber == 0) {
                              CriticalSectionGroup newGroup = new CriticalSectionGroup(this.nextGroup);
                              newGroup.add(tn1);
                              newGroup.add(tn2);
                              this.groups.add(newGroup);
                              ++this.nextGroup;
                           } else if (tn2.setNumber > 0) {
                              tn2.group.add(tn1);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public boolean mayHappenInParallel(CriticalSection tn1, CriticalSection tn2) {
      if (this.mhp == null) {
         if (this.optionLeaveOriginalLocks) {
            return true;
         } else {
            ReachableMethods rm = Scene.v().getReachableMethods();
            return rm.contains(tn1.method) && rm.contains(tn2.method);
         }
      } else {
         return this.mhp.mayHappenInParallel(tn1.method, tn2.method);
      }
   }
}
