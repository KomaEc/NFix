package bsh;

public abstract class ReflectManager {
   private static ReflectManager rfm;

   public static ReflectManager getReflectManager() throws Capabilities.Unavailable {
      if (rfm == null) {
         try {
            Class var0 = Class.forName("bsh.reflect.ReflectManagerImpl");
            rfm = (ReflectManager)var0.newInstance();
         } catch (Exception var2) {
            throw new Capabilities.Unavailable("Reflect Manager unavailable: " + var2);
         }
      }

      return rfm;
   }

   public static boolean RMSetAccessible(Object var0) throws Capabilities.Unavailable {
      return getReflectManager().setAccessible(var0);
   }

   public abstract boolean setAccessible(Object var1);
}
