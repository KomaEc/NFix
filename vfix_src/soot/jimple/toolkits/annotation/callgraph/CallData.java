package soot.jimple.toolkits.annotation.callgraph;

import java.util.ArrayList;
import java.util.HashMap;

public class CallData {
   private final HashMap<Object, CallData> map = new HashMap();
   private final ArrayList<CallData> children = new ArrayList();
   private final ArrayList<CallData> outputs = new ArrayList();
   private String data;

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("Data: ");
      sb.append(this.data);
      return sb.toString();
   }

   public void addChild(CallData cd) {
      this.children.add(cd);
   }

   public void addOutput(CallData cd) {
      if (!this.outputs.contains(cd)) {
         this.outputs.add(cd);
      }

   }

   public void setData(String d) {
      this.data = d;
   }

   public String getData() {
      return this.data;
   }

   public ArrayList<CallData> getChildren() {
      return this.children;
   }

   public ArrayList<CallData> getOutputs() {
      return this.outputs;
   }

   public void addToMap(Object key, CallData val) {
      this.map.put(key, val);
   }

   public HashMap<Object, CallData> getMap() {
      return this.map;
   }
}
