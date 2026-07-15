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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class SemanticSearchClient {
    private static final Logger LOG =
            LoggerFactory.getLogger(SemanticSearchClient.class);

    private static final HttpClient client =
            HttpClient.newHttpClient();

    private static final ObjectMapper mapper =
            new ObjectMapper();

    public static String buildQuery(String query, String text, int topK) {
        ObjectNode root = mapper.createObjectNode();
        root.put("query", query);
        root.put("text", text);
        root.put("top_k", topK);
        return root.toString();
    }
    public static String semanticSearch(
            String baseUrl,
            String query,
            String text,
            int top_k
    ) throws Exception {

        String requestBody = buildQuery(query, text, top_k);

        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create(baseUrl + "/search"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Semantic search failed");
        }

        JsonNode root = mapper.readTree(response.body());
        if (root.isArray() && !root.isEmpty()) {
            JsonNode firstHit = root.get(0);
            String content = firstHit.path("content").asText("");
            LOG.info("Search Result Content: {}", content);
            return content;
        }
        return "";
        // return response.body();
    }


    }
//    public record SemanticHit(String content, double score) {}
//    private static final Logger LOG = LoggerFactory.getLogger(SemanticSearchClient.class);
//    private static final HttpClient client = HttpClient.newHttpClient();
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    public static List<SemanticHit> parseResults(String json) throws Exception {
//        JsonNode root = mapper.readTree(json);
//        List<SemanticHit> hits = new ArrayList<>();
//
//        if (root.isArray()) {
//            for (JsonNode node : root) {
//                String content = node.path("content").asText("");
//                double score = node.path("score").asDouble(0.0);
//                hits.add(new SemanticHit(content, score));
//            }
//        }
//        return hits;
//    }
//    public static List<SemanticHit> semanticSearchRaw(
//            String baseUrl,
//            String requestBody
//    ) throws Exception {
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .version(HttpClient.Version.HTTP_1_1)
//                .uri(URI.create(baseUrl + "/search"))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        HttpResponse<String> response =
//                client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode() != 200) {
//            throw new RuntimeException(
//                    "Semantic search error: " + response.statusCode()
//            );
//        }
//
//        LOG.info("Raw Search Result: {}", response.body());
//        return parseResults(response.body());
//    }
//
//    public static String buildQuery(String queryText, int top_k) {
//        ObjectNode root = mapper.createObjectNode();
//        root.put("query", queryText);   // simple query string
//        root.put("top_k", top_k);       // integer
//
//        return root.toString();         // convert to JSON string
//    }
//
//    public static String semanticSearch(
//            String baseUrl,      // e.g. http://localhost:9200
//            String requestBody
//    ) throws Exception {
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .version(HttpClient.Version.HTTP_1_1)
//                .uri(URI.create(baseUrl + "/search"))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        HttpResponse<String> response =
//                client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode() != 200) {
//            throw new RuntimeException(
//                    "Semantic search error: " + response.statusCode() + " " + response.body()
//            );
//        }
//        LOG.info("Raw Search Result: {}",response.body());
//        JsonNode root = mapper.readTree(response.body());
//        if (root.isArray() && !root.isEmpty()) {
//            JsonNode firstHit = root.get(0);
//            String content = firstHit.path("content").asText("");
//            LOG.info("Search Result Content: {}", content);
//            return content;
//        }
//        return "";
//    }

