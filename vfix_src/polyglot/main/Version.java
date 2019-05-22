package polyglot.main;

public abstract class Version {
   public abstract String name();

   public abstract int major();

   public abstract int minor();

   public abstract int patch_level();

   public String toString() {
      return "" + this.major() + "." + this.minor() + "." + this.patch_level();
   }
}
