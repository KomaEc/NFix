package org.apache.tika.parser.jdbc;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.tika.TikaTest;
import org.apache.tika.extractor.EmbeddedResourceHandler;
import org.apache.tika.extractor.ParserContainerExtractor;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Database;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.RecursiveParserWrapper;
import org.apache.tika.sax.BasicContentHandlerFactory;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.junit.Test;
import org.xml.sax.ContentHandler;

public class SQLite3ParserTest extends TikaTest {
    private final static String TEST_FILE_NAME = "testSqlite3b.db";
    private final static String TEST_FILE1 = "/test-documents/" + TEST_FILE_NAME;

    @Test
    public void testBasic() throws Exception {
        Parser p = new AutoDetectParser();

        //test different types of input streams
        //actual inputstream, memory buffered bytearray and literal file
        InputStream[] streams = new InputStream[3];
        streams[0] = getResourceAsStream(TEST_FILE1);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(getResourceAsStream(TEST_FILE1), bos);
        streams[1] = new ByteArrayInputStream(bos.toByteArray());
        streams[2] = TikaInputStream.get(getResourceAsFile(TEST_FILE1));
        int tests = 0;
        for (InputStream stream : streams) {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, TEST_FILE_NAME);
            //1) getXML closes the stream
            //2) getXML runs recursively on the contents, so the embedded docs should show up
            XMLResult result = getXML(stream, p, metadata);
            stream.close();
            String x = result.xml;
            //first table name
            assertContains("<table name=\"my_table1\"><thead><tr>\t<th>PK</th>", x);
            //non-ascii
            assertContains("<td>普林斯顿大学</td>", x);
            //boolean
            assertContains("<td>true</td>\t<td>2015-01-02</td>", x);
            //date test
            assertContains("2015-01-04", x);
            //timestamp test
            assertContains("2015-01-03 15:17:03", x);
            //first embedded doc's image tag
            assertContains("alt=\"image1.png\"", x);
            //second embedded doc's image tag
            assertContains("alt=\"A description...\"", x);
            //second table name
            assertContains("<table name=\"my_table2\"><thead><tr>\t<th>INT_COL2</th>", x);

            Metadata post = result.metadata;
            String[] tableNames = post.getValues(Database.TABLE_NAME);
            assertEquals(2, tableNames.length);
            assertEquals("my_table1", tableNames[0]);
            assertEquals("my_table2", tableNames[1]);
            tests++;
        }
        assertEquals(3, tests);
    }

}
