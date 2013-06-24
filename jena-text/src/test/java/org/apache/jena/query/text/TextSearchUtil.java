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

package org.apache.jena.query.text;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TextSearchUtil {
    private static Version VER = Version.LUCENE_41 ;
    private static final Analyzer analyzer = new StandardAnalyzer(VER);
	
	public static void emptyAndDeleteDirectory(File dir) {
		File[] contents = dir.listFiles();
		if (contents != null) {
			for (File content : contents) {
				if (content.isDirectory()) {
					emptyAndDeleteDirectory(content) ;
				} else {
					content.delete();
				}
			}
		}
		dir.delete();
	}

    public static void createEmptyIndex(File indexDir) throws IOException {
		Directory directory = FSDirectory.open(indexDir);
        IndexWriterConfig wConfig = new IndexWriterConfig(VER, analyzer) ;
        IndexWriter indexWriter = new IndexWriter(directory, wConfig) ;
        indexWriter.close(); // force creation of the index files
	}

}