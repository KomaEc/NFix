package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.validator.Java10Validator;
import com.github.javaparser.ast.validator.Java11Validator;
import com.github.javaparser.ast.validator.Java1_0Validator;
import com.github.javaparser.ast.validator.Java1_1Validator;
import com.github.javaparser.ast.validator.Java1_2Validator;
import com.github.javaparser.ast.validator.Java1_3Validator;
import com.github.javaparser.ast.validator.Java1_4Validator;
import com.github.javaparser.ast.validator.Java5Validator;
import com.github.javaparser.ast.validator.Java6Validator;
import com.github.javaparser.ast.validator.Java7Validator;
import com.github.javaparser.ast.validator.Java8Validator;
import com.github.javaparser.ast.validator.Java9Validator;
import com.github.javaparser.ast.validator.NoProblemsValidator;
import com.github.javaparser.ast.validator.ProblemReporter;
import com.github.javaparser.ast.validator.Validator;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.utils.Utils;
import com.github.javaparser.version.Java10PostProcessor;
import com.github.javaparser.version.Java11PostProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParserConfiguration {
   private boolean storeTokens = true;
   private boolean attributeComments = true;
   private boolean doNotAssignCommentsPrecedingEmptyLines = true;
   private boolean ignoreAnnotationsWhenAttributingComments = false;
   private boolean lexicalPreservationEnabled = false;
   private SymbolResolver symbolResolver = null;
   private int tabSize = 1;
   private ParserConfiguration.LanguageLevel languageLevel;
   private final List<ParseResult.PostProcessor> postProcessors;

   public ParserConfiguration() {
      this.languageLevel = ParserConfiguration.LanguageLevel.CURRENT;
      this.postProcessors = new ArrayList();
      this.postProcessors.add((result, configuration) -> {
         if (configuration.isLexicalPreservationEnabled() && configuration.isLexicalPreservationEnabled()) {
            result.ifSuccessful(LexicalPreservingPrinter::setup);
         }

      });
      this.postProcessors.add((result, configuration) -> {
         if (configuration.isAttributeComments()) {
            result.ifSuccessful((resultNode) -> {
               result.getCommentsCollection().ifPresent((comments) -> {
                  (new CommentsInserter(configuration)).insertComments(resultNode, comments.copy().getComments());
               });
            });
         }

      });
      this.postProcessors.add((result, configuration) -> {
         ParserConfiguration.LanguageLevel languageLevel = this.getLanguageLevel();
         if (languageLevel.postProcessor != null) {
            languageLevel.postProcessor.process(result, configuration);
         }

         if (languageLevel.validator != null) {
            languageLevel.validator.accept((Node)result.getResult().get(), new ProblemReporter((newProblem) -> {
               result.getProblems().add(newProblem);
            }));
         }

      });
      this.postProcessors.add((result, configuration) -> {
         configuration.getSymbolResolver().ifPresent((symbolResolver) -> {
            result.ifSuccessful((resultNode) -> {
               if (resultNode instanceof CompilationUnit) {
                  resultNode.setData(Node.SYMBOL_RESOLVER_KEY, symbolResolver);
               }

            });
         });
      });
   }

   public boolean isAttributeComments() {
      return this.attributeComments;
   }

   public ParserConfiguration setAttributeComments(boolean attributeComments) {
      this.attributeComments = attributeComments;
      return this;
   }

   public boolean isDoNotAssignCommentsPrecedingEmptyLines() {
      return this.doNotAssignCommentsPrecedingEmptyLines;
   }

   public ParserConfiguration setDoNotAssignCommentsPrecedingEmptyLines(boolean doNotAssignCommentsPrecedingEmptyLines) {
      this.doNotAssignCommentsPrecedingEmptyLines = doNotAssignCommentsPrecedingEmptyLines;
      return this;
   }

   /** @deprecated */
   @Deprecated
   public boolean isDoNotConsiderAnnotationsAsNodeStartForCodeAttribution() {
      return this.isIgnoreAnnotationsWhenAttributingComments();
   }

   /** @deprecated */
   @Deprecated
   public ParserConfiguration setDoNotConsiderAnnotationsAsNodeStartForCodeAttribution(boolean doNotConsiderAnnotationsAsNodeStartForCodeAttribution) {
      return this.setIgnoreAnnotationsWhenAttributingComments(doNotConsiderAnnotationsAsNodeStartForCodeAttribution);
   }

   public boolean isIgnoreAnnotationsWhenAttributingComments() {
      return this.ignoreAnnotationsWhenAttributingComments;
   }

   public ParserConfiguration setIgnoreAnnotationsWhenAttributingComments(boolean ignoreAnnotationsWhenAttributingComments) {
      this.ignoreAnnotationsWhenAttributingComments = ignoreAnnotationsWhenAttributingComments;
      return this;
   }

   public ParserConfiguration setStoreTokens(boolean storeTokens) {
      this.storeTokens = storeTokens;
      if (!storeTokens) {
         this.setAttributeComments(false);
      }

      return this;
   }

   public boolean isStoreTokens() {
      return this.storeTokens;
   }

   public int getTabSize() {
      return this.tabSize;
   }

   public ParserConfiguration setTabSize(int tabSize) {
      this.tabSize = tabSize;
      return this;
   }

   /** @deprecated */
   @Deprecated
   public Optional<Validator> getValidator() {
      throw new IllegalStateException("method is deprecated");
   }

   /** @deprecated */
   @Deprecated
   public ParserConfiguration setValidator(Validator validator) {
      if (validator instanceof Java10Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_10);
      } else if (validator instanceof Java9Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_9);
      } else if (validator instanceof Java8Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8);
      } else if (validator instanceof Java7Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_7);
      } else if (validator instanceof Java6Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_6);
      } else if (validator instanceof Java5Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_5);
      } else if (validator instanceof Java1_4Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_1_4);
      } else if (validator instanceof Java1_3Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_1_3);
      } else if (validator instanceof Java1_2Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_1_2);
      } else if (validator instanceof Java1_1Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_1_1);
      } else if (validator instanceof Java1_0Validator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_1_0);
      } else if (validator instanceof NoProblemsValidator) {
         this.setLanguageLevel(ParserConfiguration.LanguageLevel.RAW);
      }

      return this;
   }

   public ParserConfiguration setLexicalPreservationEnabled(boolean lexicalPreservationEnabled) {
      this.lexicalPreservationEnabled = lexicalPreservationEnabled;
      return this;
   }

   public boolean isLexicalPreservationEnabled() {
      return this.lexicalPreservationEnabled;
   }

   public Optional<SymbolResolver> getSymbolResolver() {
      return Optional.ofNullable(this.symbolResolver);
   }

   public ParserConfiguration setSymbolResolver(SymbolResolver symbolResolver) {
      this.symbolResolver = symbolResolver;
      return this;
   }

   public List<ParseResult.PostProcessor> getPostProcessors() {
      return this.postProcessors;
   }

   public ParserConfiguration setLanguageLevel(ParserConfiguration.LanguageLevel languageLevel) {
      this.languageLevel = (ParserConfiguration.LanguageLevel)Utils.assertNotNull(languageLevel);
      return this;
   }

   public ParserConfiguration.LanguageLevel getLanguageLevel() {
      return this.languageLevel;
   }

   public static enum LanguageLevel {
      RAW((Validator)null, (ParseResult.PostProcessor)null),
      POPULAR(new Java8Validator(), (ParseResult.PostProcessor)null),
      CURRENT(new Java8Validator(), (ParseResult.PostProcessor)null),
      BLEEDING_EDGE(new Java11Validator(), new Java11PostProcessor()),
      JAVA_1_0(new Java1_0Validator(), (ParseResult.PostProcessor)null),
      JAVA_1_1(new Java1_1Validator(), (ParseResult.PostProcessor)null),
      JAVA_1_2(new Java1_2Validator(), (ParseResult.PostProcessor)null),
      JAVA_1_3(new Java1_3Validator(), (ParseResult.PostProcessor)null),
      JAVA_1_4(new Java1_4Validator(), (ParseResult.PostProcessor)null),
      JAVA_5(new Java5Validator(), (ParseResult.PostProcessor)null),
      JAVA_6(new Java6Validator(), (ParseResult.PostProcessor)null),
      JAVA_7(new Java7Validator(), (ParseResult.PostProcessor)null),
      JAVA_8(new Java8Validator(), (ParseResult.PostProcessor)null),
      JAVA_9(new Java9Validator(), (ParseResult.PostProcessor)null),
      JAVA_10(new Java10Validator(), new Java10PostProcessor()),
      JAVA_11(new Java11Validator(), new Java11PostProcessor());

      final Validator validator;
      final ParseResult.PostProcessor postProcessor;

      private LanguageLevel(Validator validator, ParseResult.PostProcessor postProcessor) {
         this.validator = validator;
         this.postProcessor = postProcessor;
      }
   }
}
