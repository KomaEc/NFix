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

package org.drools.compiler.integrationtests.incrementalcompilation;


import org.drools.compiler.CommonTestMethodBase;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.junit.Assert.assertEquals;


public class IncrementalCompilationTest extends CommonTestMethodBase {


    public static KieModule createAndDeployJar( KieServices ks,
                                                ReleaseId releaseId,
                                                String... drls ) {
        byte[] jar = createKJar( ks, releaseId, null, drls );
        return deployJar( ks, jar );
    }


    @Test
    public void testChangedPackage() {
        // DROOLS-1742
        String drl1 = "package org.a\n" +
                "rule \"RG_1\"\n" +
                "    when\n" +
                "        Boolean()\n" +
                "        Integer()\n" +
                "    then\n" +
                "        System.out.println(\"RG_1\");" +
                "end\n";

        String drl2 = "package org.b\n" +
                "rule \"RG_2\"\n" +
                "    when\n" +
                "        Boolean()\n" +
                "        String()\n" +
                "    then\n" +
                "        System.out.println(\"RG_2\");" +
                "end\n";

        KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId1 = ks.newReleaseId( "org.kie", "test-upgrade", "1.0.0" );
        ReleaseId releaseId2 = ks.newReleaseId( "org.kie", "test-upgrade", "1.1.0" );

        createAndDeployJar(ks, releaseId1, drl2, drl1);
        KieContainer kieContainer = ks.newKieContainer(releaseId1);
        KieSession kieSession = kieContainer.newKieSession();

        kieSession.insert("test");
        assertEquals(0, kieSession.fireAllRules());

        createAndDeployJar( ks, releaseId2, drl1);
        kieContainer.updateToVersion(releaseId2);

        assertEquals(0, kieSession.fireAllRules());
    }
}
