package soot.shimple;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.SootMethod;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.StmtBody;
import soot.options.Options;
import soot.options.ShimpleOptions;
import soot.shimple.internal.SPatchingChain;
import soot.shimple.internal.ShimpleBodyBuilder;
import soot.util.HashChain;

public class ShimpleBody extends StmtBody {
   private static final Logger logger = LoggerFactory.getLogger(ShimpleBody.class);
   protected ShimpleOptions options;
   protected ShimpleBodyBuilder sbb;
   protected boolean isExtendedSSA = false;
   protected boolean isSSA = false;

   ShimpleBody(SootMethod m, Map options) {
      super(m);
      this.options = new ShimpleOptions(options);
      this.setSSA(true);
      this.isExtendedSSA = this.options.extended();
      this.unitChain = new SPatchingChain(this, new HashChain());
      this.sbb = new ShimpleBodyBuilder(this);
   }

   ShimpleBody(Body body, Map options) {
      super(body.getMethod());
      if (!(body instanceof JimpleBody) && !(body instanceof ShimpleBody)) {
         throw new RuntimeException("Cannot construct ShimpleBody from given Body type.");
      } else {
         if (Options.v().verbose()) {
            logger.debug("[" + this.getMethod().getName() + "] Constructing ShimpleBody...");
         }

         this.options = new ShimpleOptions(options);
         this.unitChain = new SPatchingChain(this, new HashChain());
         this.importBodyContentsFrom(body);
         this.sbb = new ShimpleBodyBuilder(this);
         if (body instanceof ShimpleBody) {
            this.rebuild(true);
         } else {
            this.rebuild(false);
         }

      }
   }

   public void rebuild() {
      this.rebuild(true);
   }

   public void rebuild(boolean hasPhiNodes) {
      this.isExtendedSSA = this.options.extended();
      this.sbb.transform();
      this.setSSA(true);
   }

   public JimpleBody toJimpleBody() {
      ShimpleBody sBody = (ShimpleBody)this.clone();
      sBody.eliminateNodes();
      JimpleBody jBody = Jimple.v().newBody(sBody.getMethod());
      jBody.importBodyContentsFrom(sBody);
      return jBody;
   }

   public void eliminatePhiNodes() {
      this.sbb.preElimOpt();
      this.sbb.eliminatePhiNodes();
      this.sbb.postElimOpt();
      this.setSSA(false);
   }

   public void eliminatePiNodes() {
      this.sbb.eliminatePiNodes();
   }

   public void eliminateNodes() {
      this.sbb.preElimOpt();
      this.sbb.eliminatePhiNodes();
      if (this.isExtendedSSA) {
         this.sbb.eliminatePiNodes();
      }

      this.sbb.postElimOpt();
      this.setSSA(false);
   }

   public Object clone() {
      Body b = Shimple.v().newBody(this.getMethod());
      b.importBodyContentsFrom(this);
      return b;
   }

   public void setSSA(boolean isSSA) {
      this.isSSA = isSSA;
   }

   public boolean isSSA() {
      return this.isSSA;
   }

   public boolean isExtendedSSA() {
      return this.isExtendedSSA;
   }

   public ShimpleOptions getOptions() {
      return this.options;
   }

   public void makeUniqueLocalNames() {
      this.sbb.makeUniqueLocalNames();
   }
}
