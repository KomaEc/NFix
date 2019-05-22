package soot.jimple.toolkits.thread.synchronization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import soot.EquivalentValue;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;

class CriticalSection extends SynchronizedRegion {
   public static int nextIDNum = 1;
   public int IDNum;
   public int nestLevel;
   public String name;
   public Value origLock;
   public CodeBlockRWSet read;
   public CodeBlockRWSet write;
   public HashSet<Unit> invokes;
   public HashSet<Unit> units;
   public HashMap<Unit, CodeBlockRWSet> unitToRWSet;
   public HashMap<Unit, List> unitToUses;
   public boolean wholeMethod;
   public SootMethod method;
   public int setNumber;
   public CriticalSectionGroup group;
   public HashSet<CriticalSectionDataDependency> edges;
   public HashSet<Unit> waits;
   public HashSet<Unit> notifys;
   public HashSet<MethodOrMethodContext> transitiveTargets;
   public Value lockObject;
   public Value lockObjectArrayIndex;
   public List<EquivalentValue> lockset;

   CriticalSection(boolean wholeMethod, SootMethod method, int nestLevel) {
      this.IDNum = nextIDNum++;
      this.nestLevel = nestLevel;
      this.read = new CodeBlockRWSet();
      this.write = new CodeBlockRWSet();
      this.invokes = new HashSet();
      this.units = new HashSet();
      this.unitToRWSet = new HashMap();
      this.unitToUses = new HashMap();
      this.wholeMethod = wholeMethod;
      this.method = method;
      this.setNumber = 0;
      this.group = null;
      this.edges = new HashSet();
      this.waits = new HashSet();
      this.notifys = new HashSet();
      this.transitiveTargets = null;
      this.lockObject = null;
      this.lockObjectArrayIndex = null;
      this.lockset = null;
   }

   CriticalSection(CriticalSection tn) {
      super(tn);
      this.IDNum = tn.IDNum;
      this.nestLevel = tn.nestLevel;
      this.origLock = tn.origLock;
      this.read = new CodeBlockRWSet();
      this.read.union(tn.read);
      this.write = new CodeBlockRWSet();
      this.write.union(tn.write);
      this.invokes = (HashSet)tn.invokes.clone();
      this.units = (HashSet)tn.units.clone();
      this.unitToRWSet = (HashMap)tn.unitToRWSet.clone();
      this.unitToUses = (HashMap)tn.unitToUses.clone();
      this.wholeMethod = tn.wholeMethod;
      this.method = tn.method;
      this.setNumber = tn.setNumber;
      this.group = tn.group;
      this.edges = (HashSet)tn.edges.clone();
      this.waits = (HashSet)tn.waits.clone();
      this.notifys = (HashSet)tn.notifys.clone();
      this.transitiveTargets = (HashSet)((HashSet)(tn.transitiveTargets == null ? null : tn.transitiveTargets.clone()));
      this.lockObject = tn.lockObject;
      this.lockObjectArrayIndex = tn.lockObjectArrayIndex;
      this.lockset = tn.lockset;
   }

   protected Object clone() {
      return new CriticalSection(this);
   }

   public String toString() {
      return this.name;
   }
}
