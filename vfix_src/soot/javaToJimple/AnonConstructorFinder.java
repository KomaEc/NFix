package soot.javaToJimple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.ast.Expr;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

public class AnonConstructorFinder extends ContextVisitor {
   private static final Logger logger = LoggerFactory.getLogger(AnonConstructorFinder.class);

   public AnonConstructorFinder(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf);
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof New && ((New)n).anonType() != null) {
         try {
            List<Type> argTypes = new ArrayList();
            Iterator it = ((New)n).arguments().iterator();

            while(it.hasNext()) {
               argTypes.add(((Expr)it.next()).type());
            }

            ConstructorInstance ci = this.typeSystem().findConstructor(((New)n).anonType().superType().toClass(), argTypes, (ClassType)((New)n).anonType().superType().toClass());
            InitialResolver.v().addToAnonConstructorMap((New)n, ci);
         } catch (SemanticException var5) {
            System.out.println(var5.getMessage());
            logger.error((String)var5.getMessage(), (Throwable)var5);
         }
      }

      return this;
   }
}
