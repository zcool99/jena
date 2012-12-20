/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.riot;

import org.apache.jena.atlas.junit.BaseTest ;
import org.junit.BeforeClass ;
import org.junit.Test ;

public class TestLangRIOT extends BaseTest
{
    @BeforeClass public static void beforeClass() { RDFLanguages.init() ; } 
    
    @Test public void lang_01() { test(RDFLanguages.NTriples, "NT") ; }
    @Test public void lang_02() { test(RDFLanguages.NTriples, "N-Triples") ; }
    @Test public void lang_03() { test(RDFLanguages.NTriples, "N-TRIPLES") ; }
    @Test public void lang_04() { test(RDFLanguages.NTriples, "NTRIPLE") ; }
    @Test public void lang_05() { test(RDFLanguages.NTriples, "NTRIPLES") ; }
    
    @Test public void lang_11() { test(RDFLanguages.Turtle, "TTL") ; }
    @Test public void lang_12() { test(RDFLanguages.Turtle, "TUrtLE") ; }

    @Test public void lang_21() { test(RDFLanguages.RDFXML, "RDF/XML") ; }
    @Test public void lang_22() { test(RDFLanguages.RDFXML, "RDFXML") ; }
    @Test public void lang_23() { test(RDFLanguages.RDFXML, "RDF/XML-ABBREV") ; }
    
    @Test public void lang_30() { test(RDFLanguages.RDFJSON, "RDFJSON") ; }
    @Test public void lang_31() { test(RDFLanguages.RDFJSON, "RDF/json") ; }

    @Test public void lang_40() { test(RDFLanguages.NQuads,  "N-QUADS") ; }
    @Test public void lang_41() { test(RDFLanguages.NQuads, "NQuads") ; }
    
    @Test public void lang_50() { test(RDFLanguages.TriG,  "TriG") ; }
    @Test public void lang_51() { test(RDFLanguages.TriG, "trig") ; }
    @Test public void lang_52() { test(RDFLanguages.TriG, "TRIG") ; }
    
    @Test public void guess_01() { guess("D.nt", RDFLanguages.NTriples) ; }
    @Test public void guess_02() { guess("D.ttl.nt", RDFLanguages.NTriples) ; }

    @Test public void guess_03() { guess("D.ttl", RDFLanguages.Turtle) ; }

    @Test public void guess_04() { guess("D.rdf", RDFLanguages.RDFXML) ; }
    @Test public void guess_05() { guess("D.owl", RDFLanguages.RDFXML) ; }
    
    @Test public void guess_06() { guess("D.rj", RDFLanguages.RDFJSON) ; }
    @Test public void guess_07() { guess("D.json", RDFLanguages.RDFJSON) ; }

    @Test public void guess_08() { guess("D.nq", RDFLanguages.NQuads) ; }
    @Test public void guess_09() { guess("D.trig", RDFLanguages.TriG) ; }
    
    private void test(Lang2 expected, String string)
    {
        Lang2 lang = RDFLanguages.nameToLang(string) ;
        assertEquals(expected, lang) ;
    }

    private void guess(String filename, Lang2 expected)
    {
        Lang2 lang = RDFLanguages.filenameToLang(filename) ;
        assertEquals(expected, lang) ;
    }

}
