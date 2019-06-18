/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.compiler.integrationtests;



import org.drools.compiler.CommonTestMethodBase;
import org.drools.compiler.Message;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This is a sample class to launch a rule.
 */
public class KieHelloWorldTest extends CommonTestMethodBase {


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
    @Test
    public void testVeyifyNotExistingKieBase() throws Exception {
        // DROOLS-2757
        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem().write( "src/main/resources/r1.drl", createDrl( "R1" ) );
        ks.newKieBuilder( kfs ).buildAll();

        KieContainer kieContainer = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
        try {
            kieContainer.verify( "notexistingKB" );
            fail("Verifying a not existing KieBase should throw a RuntimeException");
        } catch (RuntimeException e) {
            assertTrue( e.getMessage().contains( "notexistingKB" ) );
        }
    }
}
