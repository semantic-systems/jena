package org.apache.jena.sparql.function.library;

public class OllamaClient {
    private static final String LLM_URL = "http://localhost:11434/api/generate";
    //    private final HttpClient httpClient = HttpClient.newBuilder()
//            .connectTimeout(Duration.ofSeconds(255))
//            .build();
//    String jsonBody = getJsonBody(prompt);
    //            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(LLM_URL))
//                    .header("Authorization", "Bearer " + API_KEY)
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
//                    .build();

//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(LLM_URL))
//                    .timeout(Duration.ofSeconds(255))
//                    .header("Content-Type", "application/json")
//                    .header("Authorization", "ApiKey " + API_KEY)
//                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
//                    .build();

//            HttpResponse<String> response =
//                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println("[FN_LLM] HTTP response code: " + response.statusCode());
//            System.out.println("[FN_LLM] Response body: " + response.body());
//            if (response.statusCode() != 200) {
//                throw new RuntimeException(
//                        "LLM HTTP " + response.statusCode() + ": " + response.body()
//                );
//            }
//            // Extract LLM Response // return extractOllamaResponse(response.body());
//            if (response.statusCode() == 200) {
//                // Simple parsing: look for "response":"..."
//                String body = response.body();
//                int index = body.indexOf("\"response\":\"");
//                if (index >= 0) {
//                    int start = index + 12;
//                    int end = body.indexOf("\"", start);
//                    if (end > start) {
//                        String llmResponse = body.substring(start, end);
//                        System.out.println("[FN_LLM] Parsed LLM response: " + llmResponse);
//                        return NodeValue.makeString(llmResponse);
//                    }
//                }
//                // return NodeValue.makeString(answer);
//                System.out.println("[FN_LLM] LLM response not found in body");
//                return NodeValue.makeString("LLM response not found");
//            } else {
//                System.out.println("[FN_LLM] Error from server: " + response.statusCode());
//                return NodeValue.makeString("Error: " + response.statusCode());
//            }
//        } catch (Exception e) {
//            System.out.println("[FN_LLM] Exception: " + e.getMessage());
//            return NodeValue.makeString("Exception: " + e.getMessage());
//        }


//
//    private static String getJsonBody(String prompt) {
//        String userPrompt = prompt.replace("\"", "\\\"");
//        String jsonBody_ollama = """
//        {
//          "model": "%s",
//          "prompt": "%s",
//          "stream": false
//        }
//        """.formatted(MODEL, userPrompt); //formatted(model, escapeJson(prompt));
//        String jsonBody_llm = """
//        {
//          "model": "%s",
//          "messages": [
//            {
//              "role": "system",
//              "content": "%s"
//            },
//            {
//              "role": "user",
//              "content": "%s"
//            }
//          ],
//          "stream": false
//        }
//        """.formatted(MODEL, SYSTEM_INSTRUCTION, userPrompt);
//        return jsonBody_llm;
//    }
}
