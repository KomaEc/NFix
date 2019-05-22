package com.google.common.base;

final class CharMatcher$ForPredicate extends CharMatcher {
   private final Predicate<? super Character> predicate;

   CharMatcher$ForPredicate(Predicate<? super Character> predicate) {
      this.predicate = (Predicate)Preconditions.checkNotNull(predicate);
   }

   public boolean matches(char c) {
      return this.predicate.apply(c);
   }

   public boolean apply(Character character) {
      return this.predicate.apply(Preconditions.checkNotNull(character));
   }

   public String toString() {
      return "CharMatcher.forPredicate(" + this.predicate + ")";
   }
}
