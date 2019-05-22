package heros;

public interface JoinLattice<V> {
   V topElement();

   V bottomElement();

   V join(V var1, V var2);
}
