package org.apache.jena.sparql.function.library;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class SemanticSearchClientOld {
    /**
     * Executes a semantic search against Semantic search and returns
     * the top hit's content field.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SemanticSearchClient.class);
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    public static String buildQuery(String queryText, int top_k) {
        ObjectNode root = mapper.createObjectNode();
        root.put("query", queryText);   // simple query string
        root.put("top_k", top_k);       // integer

        return root.toString();         // convert to JSON string
    }

    public static String semanticSearch(
            String baseUrl,      // e.g. http://localhost:9200
            String requestBody
    ) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create(baseUrl + "/search"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Semantic search error: " + response.statusCode() + " " + response.body()
            );
        }
        LOG.info("Raw Search Result: {}",response.body());
        JsonNode root = mapper.readTree(response.body());
        if (root.isArray() && !root.isEmpty()) {
            JsonNode firstHit = root.get(0);
            String content = firstHit.path("content").asText("");
            LOG.info("Search Result Content: {}", content);
            return content;
        }
        return "";
    }
}

