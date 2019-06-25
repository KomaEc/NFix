import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;

public class Main {
    public void testTooThinBox() {

        
        //Assert.assertEquals(0.0,
        //                    new PolygonsSet(0.0, 0.0, 0.0, 10.3206397147574, 1.0e-10).getSize(),
        //                    1.0e-10);

        double d = new PolygonsSet(0.0, 0.0, 0.0, 10.3206397147574, 1.0e-10).getSize();
        System.out.println(d);
    }

    public static void main(String... args) {
        Main run = new Main();
        run.testTooThinBox();
    }
}