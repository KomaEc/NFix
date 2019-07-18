
import org.apache.commons.math3.geometry.euclidean.threed.*;

import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;

public class Main {
    public void testIntersectionNotIntersecting() throws MathIllegalArgumentException {
        SubLine sub1 = new SubLine(new Vector3D(1, 1, 1), new Vector3D(1.5, 1, 1));
        SubLine sub2 = new SubLine(new Vector3D(2, 3, 0), new Vector3D(2, 3, 0.5));
        sub1.intersection(sub2, true);
        sub1.intersection(sub2, false);
    }

    public static void main(String... args) {
        Main run = new Main();
        run.testIntersectionNotIntersecting();
    }
}