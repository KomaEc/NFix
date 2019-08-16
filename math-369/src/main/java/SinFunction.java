
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.*;

/**
 * Auxillary class for testing solvers.
 *
 * The function is extraordinarily well behaved around zero roots: it
 * has an inflection point there (second order derivative is zero),
 * which means linear approximation (Regula Falsi) will converge
 * quadratically.
 *
 * @version $Revision: 811685 $ $Date: 2009-09-05 19:36:48 +0200 (sam., 05 sept. 2009) $
 */
public class SinFunction implements DifferentiableUnivariateRealFunction {

    /* Evaluate sinus fuction.
     * @see org.apache.commons.math.UnivariateRealFunction#value(double)
     */
    public double value(double x) throws FunctionEvaluationException {
        return Math.sin(x);
    }

    /* First derivative of sinus function
     */
    public UnivariateRealFunction derivative() {
        return new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.cos(x);
            }
        };
    }

}
