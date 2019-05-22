package com.github.javaparser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.utils.Utils;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ParseResult<T> {
   private final T result;
   private final List<Problem> problems;
   private final List<JavaToken> tokens;
   private final CommentsCollection commentsCollection;

   public ParseResult(T result, List<Problem> problems, List<JavaToken> tokens, CommentsCollection commentsCollection) {
      this.commentsCollection = commentsCollection;
      this.result = result;
      this.problems = problems;
      this.tokens = tokens;
   }

   public boolean isSuccessful() {
      return this.problems.isEmpty() && this.result != null;
   }

   public void ifSuccessful(Consumer<T> consumer) {
      if (this.isSuccessful()) {
         consumer.accept(this.result);
      }

   }

   public List<Problem> getProblems() {
      return this.problems;
   }

   public Problem getProblem(int i) {
      return (Problem)this.getProblems().get(i);
   }

   /** @deprecated */
   @Deprecated
   public Optional<List<JavaToken>> getTokens() {
      return Optional.ofNullable(this.tokens);
   }

   public Optional<CommentsCollection> getCommentsCollection() {
      return Optional.ofNullable(this.commentsCollection);
   }

   public Optional<T> getResult() {
      return Optional.ofNullable(this.result);
   }

   public String toString() {
      if (this.isSuccessful()) {
         return "Parsing successful";
      } else {
         StringBuilder message = (new StringBuilder("Parsing failed:")).append(Utils.EOL);
         Iterator var2 = this.problems.iterator();

         while(var2.hasNext()) {
            Problem problem = (Problem)var2.next();
            message.append(problem.toString()).append(Utils.EOL);
         }

         return message.toString();
      }
   }

   public interface PostProcessor {
      void process(ParseResult<? extends Node> result, ParserConfiguration configuration);
   }
}
