package soot.toolkits.graph.pdg;

import java.util.List;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.UnitGraph;

public interface IRegion {
   SootMethod getSootMethod();

   SootClass getSootClass();

   UnitGraph getUnitGraph();

   List<Unit> getUnits();

   List<Unit> getUnits(Unit var1, Unit var2);

   List<Block> getBlocks();

   Unit getLast();

   Unit getFirst();

   int getID();

   boolean occursBefore(Unit var1, Unit var2);

   void setParent(IRegion var1);

   IRegion getParent();

   void addChildRegion(IRegion var1);

   List<IRegion> getChildRegions();
}
