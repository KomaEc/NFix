package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;

public interface NodeWithBlockStmt<N extends Node> {
   BlockStmt getBody();

   N setBody(BlockStmt block);

   default BlockStmt createBody() {
      BlockStmt block = new BlockStmt();
      this.setBody(block);
      return block;
   }
}
