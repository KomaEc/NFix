package soot.jimple.paddle;

import java.util.Map;
import soot.G;
import soot.SceneTransformer;
import soot.Singletons;

public class PaddleHook extends SceneTransformer {
   private IPaddleTransformer paddleTransformer;
   private Object paddleG;

   public PaddleHook(Singletons.Global g) {
   }

   public static PaddleHook v() {
      return G.v().soot_jimple_paddle_PaddleHook();
   }

   public IPaddleTransformer paddleTransformer() {
      if (this.paddleTransformer == null) {
         this.paddleTransformer = (IPaddleTransformer)this.instantiate("soot.jimple.paddle.PaddleTransformer");
      }

      return this.paddleTransformer;
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      this.paddleTransformer().transform(phaseName, options);
   }

   public Object instantiate(String className) {
      try {
         Object ret = Class.forName(className).newInstance();
         return ret;
      } catch (ClassNotFoundException var4) {
         throw new RuntimeException("Could not find " + className + ". Did you include Paddle on your Java classpath?");
      } catch (InstantiationException var5) {
         throw new RuntimeException("Could not instantiate " + className + ": " + var5);
      } catch (IllegalAccessException var6) {
         throw new RuntimeException("Could not instantiate " + className + ": " + var6);
      }
   }

   public Object paddleG() {
      if (this.paddleG == null) {
         this.paddleG = this.instantiate("soot.PaddleG");
      }

      return this.paddleG;
   }

   public void finishPhases() {
      if (this.paddleTransformer != null) {
         this.paddleTransformer().finishPhases();
      }

   }
}
