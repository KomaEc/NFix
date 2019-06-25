


import com.thoughtworks.xstream.mapper.Mapper.Null;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;

public class Main {
    public String createDrl(String ruleName) {
        return "package org.drools.compiler.integrationtests\n" +
                "import " + Message.class.getCanonicalName() + "\n" +
                "rule " + ruleName + " when\n" +
                "   $m : Message( message == \"Hello World\" )\n" +
                "then\n" +
                "end\n";
    }

    public String createDrlWithGlobal(String ruleName) {
        return "package org.drools.compiler.integrationtests\n" +
                "global java.util.List list\n" +
                "import " + Message.class.getCanonicalName() + "\n" +
                "rule " + ruleName + " when\n" +
                "   $m : Message( message == \"Hello World\" )\n" +
                "then\n" +
                "    list.add(\"" + ruleName + "\");" +
                "end\n";
    }
    public void testVeyifyNotExistingKieBase() throws Exception {
        // DROOLS-2757
        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem().write( "src/main/resources/r1.drl", createDrl( "R1" ) );
        ks.newKieBuilder( kfs ).buildAll();

        KieContainer kieContainer = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
        try {
            kieContainer.verify( "notexistingKB" );
            
        } catch (RuntimeException e) {
            if ( e.getMessage().contains( "notexistingKB" ) ) {
                boolean b = e.getMessage().contains( "notexistingKB" );
                System.out.println(b);
            }
        }
    }
    public static void main(String... args) throws Exception {
        Main run = new Main();
        run.testVeyifyNotExistingKieBase();
    }
}