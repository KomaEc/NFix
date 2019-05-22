package soot.jbco.name;

import soot.jbco.util.Rand;

public abstract class AbstractNameGenerator implements NameGenerator {
   public String generateName(int size) {
      if (size > 21845) {
         throw new IllegalArgumentException("Cannot generate junk name: too long for JVM.");
      } else {
         char[][] chars = this.getChars();
         int index = Rand.getInt(chars.length);
         int length = chars[index].length;
         char[] newName = new char[size];

         do {
            newName[0] = chars[index][Rand.getInt(length)];
         } while(!Character.isJavaIdentifierStart(newName[0]));

         for(int i = 1; i < newName.length; ++i) {
            int rand = Rand.getInt(length);
            newName[i] = chars[index][rand];
         }

         return String.valueOf(newName);
      }
   }

   protected abstract char[][] getChars();
}
