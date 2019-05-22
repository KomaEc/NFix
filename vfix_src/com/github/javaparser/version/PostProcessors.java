package com.github.javaparser.version;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostProcessors implements ParseResult.PostProcessor {
   private final List<ParseResult.PostProcessor> postProcessors = new ArrayList();

   public PostProcessors(ParseResult.PostProcessor... postProcessors) {
      this.postProcessors.addAll(Arrays.asList(postProcessors));
   }

   public List<ParseResult.PostProcessor> getPostProcessors() {
      return this.postProcessors;
   }

   public PostProcessors remove(ParseResult.PostProcessor postProcessor) {
      if (!this.postProcessors.remove(postProcessor)) {
         throw new AssertionError("Trying to remove a post processor that isn't there.");
      } else {
         return this;
      }
   }

   public PostProcessors replace(ParseResult.PostProcessor oldProcessor, ParseResult.PostProcessor newProcessor) {
      this.remove(oldProcessor);
      this.add(newProcessor);
      return this;
   }

   public PostProcessors add(ParseResult.PostProcessor newProcessor) {
      this.postProcessors.add(newProcessor);
      return this;
   }

   public void process(ParseResult<? extends Node> result, ParserConfiguration configuration) {
      this.postProcessors.forEach((pp) -> {
         pp.process(result, configuration);
      });
   }
}
