/*
 * Copyright 2005 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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


import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;

import org.kie.api.builder.Results;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Run all the tests with the ReteOO engine implementation
 */
public class Misc2Test extends CommonTestMethodBase {

    //private static final Logger logger = LoggerFactory.getLogger( Misc2Test.class );


    private void assertDrlHasCompilationError( String str, int errorNr ) {
        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem().write( "src/main/resources/r1.drl", str );
        Results results = ks.newKieBuilder( kfs ).buildAll().getResults();
        if ( errorNr > 0 ) {
            assertEquals( errorNr, results.getMessages().size() );
        } else {
            assertTrue( results.getMessages().size() > 0 );
        }
    }


    @Test
    public void testWrongVariableNameWithSameDeclarationName() {
        // DROOLS-1064
        String str =
                "declare Parameter end\n" +
                "rule R when\n" +
                "    Parameter($b : $b == 0 )\n" +
                "then\n" +
                "end\n";

        assertDrlHasCompilationError( str, -1 );
    }

}