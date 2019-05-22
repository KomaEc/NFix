package soot.jbco.name;

public class HorrorNameGenerator extends AbstractNameGenerator implements NameGenerator {
   private static final char[][] stringChars = new char[][]{{'S', '5', '$'}, {'l', '1', 'I', 'Ι'}, {'0', 'O', 'О', 'Ο', 'Օ'}, {'o', 'о', 'ο'}, {'T', 'Т', 'Τ'}, {'H', 'Н', 'Η'}, {'E', 'Е', 'Ε'}, {'P', 'Р', 'Ρ'}, {'B', 'В', 'Β'}, {'_'}};

   protected char[][] getChars() {
      return stringChars;
   }
}
