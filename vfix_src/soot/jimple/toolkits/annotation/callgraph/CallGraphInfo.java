package soot.jimple.toolkits.annotation.callgraph;

import java.util.ArrayList;
import soot.SootMethod;

public class CallGraphInfo {
   private ArrayList<MethInfo> inputs = new ArrayList();
   private ArrayList<MethInfo> outputs = new ArrayList();
   private SootMethod center;

   public CallGraphInfo(SootMethod sm, ArrayList<MethInfo> outputs, ArrayList<MethInfo> inputs) {
      this.setCenter(sm);
      this.setOutputs(outputs);
      this.setInputs(inputs);
   }

   public void setCenter(SootMethod sm) {
      this.center = sm;
   }

   public SootMethod getCenter() {
      return this.center;
   }

   public ArrayList<MethInfo> getInputs() {
      return this.inputs;
   }

   public void setInputs(ArrayList<MethInfo> list) {
      this.inputs = list;
   }

   public ArrayList<MethInfo> getOutputs() {
      return this.outputs;
   }

   public void setOutputs(ArrayList<MethInfo> list) {
      this.outputs = list;
   }
}
