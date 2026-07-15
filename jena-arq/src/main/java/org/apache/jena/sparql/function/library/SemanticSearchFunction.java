/*
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

package org.apache.jena.sparql.function.library;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.binding.BindingFactory;
import org.apache.jena.sparql.engine.iterator.QueryIterPlainWrapper;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.pfunction.PropertyFunctionBase;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.KnnFloatVectorQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SemanticSearchFunction extends PropertyFunctionBase {

    private IndexSearcher searcher;
    private HuggingFaceTokenizer tokenizer;

    @Override
    public void build(PropFuncArg argSubject, Node predicate,
                      PropFuncArg argObject, ExecutionContext execCxt) {
        try {
            var dir = FSDirectory.open(Path.of("path/to/your/lucene/index"));
            searcher = new IndexSearcher(DirectoryReader.open(dir));
            tokenizer = HuggingFaceTokenizer
                    .newInstance("sentence-transformers/all-MiniLM-L6-v2");
        } catch (Exception e) {
            throw new RuntimeException("SemanticSearchFunction init failed", e);
        }
    }

    @Override
    public QueryIterator exec(Binding binding,
                              PropFuncArg argSubject,
                              Node predicate,
                              PropFuncArg argObject,
                              ExecutionContext execCxt) {
        try {
            String queryText = argObject.getArg().getLiteralLexicalForm();
            float[] queryVector = embed(queryText);

            TopDocs hits = searcher.search(
                    new KnnFloatVectorQuery("vector", queryVector, 10), 10);

            Var subjectVar = Var.alloc(argSubject.getArg());
            List<Binding> results = new ArrayList<>();

            for (ScoreDoc sd : hits.scoreDocs) {
                String uri = searcher.storedFields().document(sd.doc).get("uri");
                results.add(BindingFactory.binding(binding, subjectVar,
                        NodeFactory.createURI(uri)));
            }

            return QueryIterPlainWrapper.create(results.iterator(), execCxt);

        } catch (IOException e) {
            throw new RuntimeException("Semantic search exec failed", e);
        }
    }

    private float[] embed(String text) {
        var encoding = tokenizer.encode(text);
        // Run DJL inference → return float[384]
        return new float[384]; // replace with real inference
    }
}
