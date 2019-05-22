package org.codehaus.groovy.classgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.stmt.Statement;

public class BytecodeSequence extends Statement {
   private final List<BytecodeInstruction> instructions;

   public BytecodeSequence(List instructions) {
      this.instructions = instructions;
   }

   public BytecodeSequence(BytecodeInstruction instruction) {
      this.instructions = new ArrayList(1);
      this.instructions.add(instruction);
   }

   public void visit(GroovyCodeVisitor visitor) {
      if (visitor instanceof ClassGenerator) {
         ClassGenerator gen = (ClassGenerator)visitor;
         gen.visitBytecodeSequence(this);
      } else {
         Iterator iterator = this.instructions.iterator();

         while(iterator.hasNext()) {
            Object part = iterator.next();
            if (part instanceof ASTNode) {
               ((ASTNode)part).visit(visitor);
            }
         }

      }
   }

   public List getInstructions() {
      return this.instructions;
   }
}
