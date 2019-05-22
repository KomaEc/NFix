package com.github.javaparser.metamodel;

import com.github.javaparser.ast.comments.BlockComment;
import java.util.Optional;

public class BlockCommentMetaModel extends CommentMetaModel {
   BlockCommentMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, BlockComment.class, "BlockComment", "com.github.javaparser.ast.comments", false, false);
   }
}
