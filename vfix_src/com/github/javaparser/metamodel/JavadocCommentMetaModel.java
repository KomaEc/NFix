package com.github.javaparser.metamodel;

import com.github.javaparser.ast.comments.JavadocComment;
import java.util.Optional;

public class JavadocCommentMetaModel extends CommentMetaModel {
   JavadocCommentMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, JavadocComment.class, "JavadocComment", "com.github.javaparser.ast.comments", false, false);
   }
}
