import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.*;

public class Main {
    public void testMath369() throws Exception {
        UnivariateRealFunction f = new SinFunction();
        UnivariateRealSolver solver = new BisectionSolver();
        solver.solve(f, 3.0, 3.2, 3.1);
        solver.getAbsoluteAccuracy();
    }

    public static void main(String... args) throws Exception {
        Main run = new Main();
        run.testMath369();
    }
}