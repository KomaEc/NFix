package soot.jimple;

import java.util.HashMap;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.baf.BafBody;

public class JimpleToBafContext {
   private Map<Local, Local> jimpleLocalToBafLocal = new HashMap();
   private BafBody bafBody;
   private Unit mCurrentUnit;

   public JimpleToBafContext(int localCount) {
      this.jimpleLocalToBafLocal = new HashMap(localCount * 2 + 1, 0.7F);
   }

   public void setCurrentUnit(Unit u) {
      this.mCurrentUnit = u;
   }

   public Unit getCurrentUnit() {
      return this.mCurrentUnit;
   }

   public Local getBafLocalOfJimpleLocal(Local jimpleLocal) {
      return (Local)this.jimpleLocalToBafLocal.get(jimpleLocal);
   }

   public void setBafLocalOfJimpleLocal(Local jimpleLocal, Local bafLocal) {
      this.jimpleLocalToBafLocal.put(jimpleLocal, bafLocal);
   }

   public BafBody getBafBody() {
      return this.bafBody;
   }

   public void setBafBody(BafBody bafBody) {
      this.bafBody = bafBody;
   }
}
