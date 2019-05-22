package polyglot.ast;

import java.util.List;

public interface Try extends CompoundStmt {
   Block tryBlock();

   Try tryBlock(Block var1);

   List catchBlocks();

   Try catchBlocks(List var1);

   Block finallyBlock();

   Try finallyBlock(Block var1);
}
