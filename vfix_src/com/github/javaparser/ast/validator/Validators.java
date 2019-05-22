package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Validators implements Validator {
   private final List<Validator> validators = new ArrayList();

   public Validators(Validator... validators) {
      this.validators.addAll(Arrays.asList(validators));
   }

   public List<Validator> getValidators() {
      return this.validators;
   }

   public Validators remove(Validator validator) {
      if (!this.validators.remove(validator)) {
         throw new AssertionError("Trying to remove a validator that isn't there.");
      } else {
         return this;
      }
   }

   public Validators replace(Validator oldValidator, Validator newValidator) {
      this.remove(oldValidator);
      this.add(newValidator);
      return this;
   }

   public Validators add(Validator newValidator) {
      this.validators.add(newValidator);
      return this;
   }

   public void accept(Node node, ProblemReporter problemReporter) {
      this.validators.forEach((v) -> {
         v.accept(node, problemReporter);
      });
   }
}
