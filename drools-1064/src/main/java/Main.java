

//import org.drools.compiler.CommonTestMethodBase;


import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;

import org.kie.api.builder.Results;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {


    //private static final Logger logger = LoggerFactory.getLogger( Main.class );


    private void assertDrlHasCompilationError( String str, int errorNr ) {
        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem().write( "src/main/resources/r1.drl", str );
        Results results = ks.newKieBuilder( kfs ).buildAll().getResults();
        if ( errorNr > 0 ) {
            assert( errorNr == results.getMessages().size() );
        } else {
            assert( results.getMessages().size() > 0 );
        }
    }

    public void Drools1064 () {
        String str =
        "declare Parameter end\n" +
        "rule R when\n" +
        "    Parameter($b : $b == 0 )\n" +
        "then\n" +
        "end\n";

        assertDrlHasCompilationError( str, -1 );
    }
    public static void main(String... args) {

        Main run = new Main();
        run.Drools1064();
    }
}