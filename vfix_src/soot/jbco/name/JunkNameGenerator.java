package soot.jbco.name;

public class JunkNameGenerator extends AbstractNameGenerator implements NameGenerator {
   private static final char[][] stringChars = new char[][]{{'S', '5', '$'}, {'l', '1', 'I'}, {'_'}};

   protected char[][] getChars() {
      return stringChars;
   }
}
