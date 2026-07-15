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

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.jena.sparql.expr.ExprEvalException;

import org.apache.jena.sparql.expr.NodeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.jena.sparql.function.FunctionBase2;

import java.util.List;

public class FN_SemanticSearch extends FunctionBase2 {

    @Override
    public NodeValue exec(NodeValue textNode, NodeValue queryNode) {

        if (!textNode.isString() || !queryNode.isString()) {
            throw new ExprEvalException(
                    "semanticSearch(text, query) expects string arguments"
            );
        }

        try {
            String json =
                    SemanticSearchClient.semanticSearch(
                            System.getenv("SEMANTIC_SEARCH_URL"),
                            queryNode.getString(),
                            textNode.getString(),
                            3
                    );

            //JSON as xsd:string
            return NodeValue.makeString(json);

        } catch (Exception e) {
            throw new ExprEvalException("semanticSearch failed", e);
        }
    }
}

//public class FN_SemanticSearch extends FunctionBase2 {
//
//    private static final double THRESHOLD = 0.7;
//    private static final int TOP_K = 5;
//
//    @Override
//    public NodeValue exec(NodeValue textNode, NodeValue queryNode) {
//
//        if (!textNode.isString() || !queryNode.isString()) {
//            throw new ExprEvalException(
//                    "semanticSearch(text, query) expects string arguments"
//            );
//        }
//
//        try {
//            List<SemanticHit> hits =
//                    SemanticSearchClient.semanticSearchRaw(
//                            System.getenv("SEMANTIC_SEARCH_URL"),
//                            queryNode.getString(),
//                            textNode.getString(),
//                            TOP_K
//                    );
//
//            boolean match = hits.stream()
//                    .anyMatch(h -> h.score() >= THRESHOLD);
//
//            return NodeValue.makeBoolean(match);
//
//        } catch (Exception e) {
//            throw new ExprEvalException("semanticSearch failed", e);
//        }
//    }
//}
//
//public class FN_SemanticSearch extends FunctionBase1 {
//    public FN_SemanticSearch() { super(); }
//    private static final double DEFAULT_THRESHOLD = 0.7;
//    private static final Logger LOG = LoggerFactory.getLogger(FN_SemanticSearch.class);
//    private static final HttpClient httpClient =
//            HttpClient.newBuilder()
//                    .version(HttpClient.Version.HTTP_1_1)
//                    .connectTimeout(Duration.ofSeconds(255))
//                    .build();

    // private static final String SemanticSearch_URL = getElasticUrl();
//
//    private static String getElasticUrl() {
//
//        String url = System.getenv("SEMANTIC_SEARCH_URL");
//        if (url == null || url.isBlank()) {
//            throw new IllegalStateException(
//                    "SEMANTIC_SEARCH_URL environment variable is not set"
//            );
//        }
//        return url;
//    }
//    @Override
//    public NodeValue exec(NodeValue v) {
//        if (!v.isString()) {
//            throw new ExprEvalException(
//                    "semanticSearch expects a string argument"
//            );
//        }
//
//        String text = v.getString();
//        int topK = 5;
//
//        try {
//            String requestBody =
//                    SemanticSearchClient.buildQuery(text, topK);
//
//            List<SemanticSearchClient.SemanticHit> hits =
//                    SemanticSearchClient.semanticSearchRaw(
//                            System.getenv("SEMANTIC_SEARCH_URL"),
//                            requestBody
//                    );
//
//            boolean match = hits.stream()
//                    .anyMatch(h -> h.score() >= DEFAULT_THRESHOLD);
//
//            return NodeValue.makeBoolean(match);
//
//        } catch (Exception e) {
//            throw new ExprEvalException("Semantic search failed", e);
//        }
//    }
//    public NodeValue exec(NodeValue v) {
//        if (!v.isString()) {
//            throw new ExprEvalException(
//                    "semanticSearch expects a string argument"
//            );
//        }
//
//        String queryString = v.getString();
//        LOG.info("[FN_SemanticSearch] Received prompt: {}", queryString);
//        int top_k = 1;
//        try {
//            String requestBody = SemanticSearchClient.buildQuery(queryString, top_k);
//            String result = SemanticSearchClient.semanticSearch(
//                    System.getenv("SEMANTIC_SEARCH_URL"),   // http://localhost:9200
//                    requestBody
//            );
//
//            return NodeValue.makeString(result);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Semantic search failed", e);
//        }
//    }
//}
