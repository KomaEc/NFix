package soot.baf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.DoubleType;
import soot.Local;
import soot.LongType;
import soot.PackManager;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnknownType;
import soot.baf.internal.BafLocal;
import soot.jimple.ConvertToBaf;
import soot.jimple.JimpleBody;
import soot.jimple.JimpleToBafContext;
import soot.jimple.Stmt;
import soot.options.Options;

public class BafBody extends Body {
   private static final Logger logger = LoggerFactory.getLogger(BafBody.class);
   private JimpleToBafContext jimpleToBafContext;

   public JimpleToBafContext getContext() {
      return this.jimpleToBafContext;
   }

   public Object clone() {
      Body b = new BafBody(this.getMethod());
      b.importBodyContentsFrom(this);
      return b;
   }

   BafBody(SootMethod m) {
      super(m);
   }

   public BafBody(Body body, Map<String, String> options) {
      super(body.getMethod());
      if (Options.v().verbose()) {
         logger.debug("[" + this.getMethod().getName() + "] Constructing BafBody...");
      }

      if (!(body instanceof JimpleBody)) {
         throw new RuntimeException("Can only construct BafBody's directly from JimpleBody's.");
      } else {
         JimpleBody jimpleBody = (JimpleBody)body;
         jimpleBody.validate();
         JimpleToBafContext context = new JimpleToBafContext(jimpleBody.getLocalCount());
         this.jimpleToBafContext = context;
         Iterator var5 = jimpleBody.getLocals().iterator();

         while(var5.hasNext()) {
            Local l = (Local)var5.next();
            Type t = l.getType();
            Local newLocal = Baf.v().newLocal(l.getName(), UnknownType.v());
            if (!t.equals(DoubleType.v()) && !t.equals(LongType.v())) {
               newLocal.setType(WordType.v());
            } else {
               newLocal.setType(DoubleWordType.v());
            }

            context.setBafLocalOfJimpleLocal(l, newLocal);
            ((BafLocal)newLocal).setOriginalLocal(l);
            this.getLocals().add(newLocal);
         }

         Map<Stmt, Unit> stmtToFirstInstruction = new HashMap();
         Iterator var11 = jimpleBody.getUnits().iterator();

         while(var11.hasNext()) {
            Unit u = (Unit)var11.next();
            Stmt s = (Stmt)u;
            List<Unit> conversionList = new ArrayList();
            context.setCurrentUnit(s);
            ((ConvertToBaf)s).convertToBaf(context, conversionList);
            stmtToFirstInstruction.put(s, conversionList.get(0));
            this.getUnits().addAll(conversionList);
         }

         var11 = this.getAllUnitBoxes().iterator();

         while(var11.hasNext()) {
            UnitBox box = (UnitBox)var11.next();
            if (box.getUnit() instanceof PlaceholderInst) {
               Unit source = ((PlaceholderInst)box.getUnit()).getSource();
               box.setUnit((Unit)stmtToFirstInstruction.get(source));
            }
         }

         var11 = jimpleBody.getTraps().iterator();

         while(var11.hasNext()) {
            Trap trap = (Trap)var11.next();
            this.getTraps().add(Baf.v().newTrap(trap.getException(), (Unit)stmtToFirstInstruction.get(trap.getBeginUnit()), (Unit)stmtToFirstInstruction.get(trap.getEndUnit()), (Unit)stmtToFirstInstruction.get(trap.getHandlerUnit())));
         }

         PackManager.v().getPack("bb").apply(this);
      }
   }
}
