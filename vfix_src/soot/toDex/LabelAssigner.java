package soot.toDex;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.builder.MethodImplementationBuilder;
import soot.jimple.Stmt;
import soot.toDex.instructions.AbstractPayload;
import soot.toDex.instructions.SwitchPayload;

public class LabelAssigner {
   private final MethodImplementationBuilder builder;
   private int lastLabelId = 0;
   private Map<Stmt, Label> stmtToLabel = new HashMap();
   private Map<Stmt, String> stmtToLabelName = new HashMap();
   private Map<AbstractPayload, Label> payloadToLabel = new HashMap();
   private Map<AbstractPayload, String> payloadToLabelName = new HashMap();

   public LabelAssigner(MethodImplementationBuilder builder) {
      this.builder = builder;
   }

   public Label getOrCreateLabel(Stmt stmt) {
      if (stmt == null) {
         throw new RuntimeException("Cannot create label for NULL statement");
      } else {
         Label lbl = (Label)this.stmtToLabel.get(stmt);
         if (lbl == null) {
            String labelName = "l" + this.lastLabelId++;
            lbl = this.builder.getLabel(labelName);
            this.stmtToLabel.put(stmt, lbl);
            this.stmtToLabelName.put(stmt, labelName);
         }

         return lbl;
      }
   }

   public Label getOrCreateLabel(AbstractPayload payload) {
      if (payload == null) {
         throw new RuntimeException("Cannot create label for NULL payload");
      } else {
         Label lbl = (Label)this.payloadToLabel.get(payload);
         if (lbl == null) {
            String labelName = "l" + this.lastLabelId++;
            lbl = this.builder.getLabel(labelName);
            this.payloadToLabel.put(payload, lbl);
            this.payloadToLabelName.put(payload, labelName);
         }

         return lbl;
      }
   }

   public Label getLabel(Stmt stmt) {
      Label lbl = this.getLabelUnsafe(stmt);
      if (lbl == null) {
         throw new RuntimeException("Statement has no label: " + stmt);
      } else {
         return lbl;
      }
   }

   public Label getLabelUnsafe(Stmt stmt) {
      return (Label)this.stmtToLabel.get(stmt);
   }

   public Label getLabel(SwitchPayload payload) {
      Label lbl = (Label)this.payloadToLabel.get(payload);
      if (lbl == null) {
         throw new RuntimeException("Switch payload has no label: " + payload);
      } else {
         return lbl;
      }
   }

   public String getLabelName(Stmt stmt) {
      return (String)this.stmtToLabelName.get(stmt);
   }

   public String getLabelName(AbstractPayload payload) {
      return (String)this.payloadToLabelName.get(payload);
   }

   public Label getLabelAtAddress(int address) {
      Iterator var2 = this.stmtToLabel.values().iterator();

      Label lb;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         lb = (Label)var2.next();
      } while(!lb.isPlaced() || lb.getCodeAddress() != address);

      return lb;
   }

   public Collection<Label> getAllLabels() {
      return this.stmtToLabel.values();
   }
}
