package soot.jimple.spark.geom.geomPA;

import soot.jimple.spark.pag.Node;

public abstract class IEncodingBroker {
   public abstract IVarAbstraction generateNode(Node var1);

   public abstract void initFlowGraph(GeomPointsTo var1);

   public abstract String getSignature();
}
