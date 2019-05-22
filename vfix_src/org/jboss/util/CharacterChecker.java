package org.jboss.util;

public interface CharacterChecker {
   CharacterChecker WHITESPACE = new CharacterChecker.WhitespaceChecker();

   boolean isCharacterLegal(char var1);

   public static class WhitespaceChecker implements CharacterChecker {
      public boolean isCharacterLegal(char character) {
         return Character.isWhitespace(character);
      }
   }
}
