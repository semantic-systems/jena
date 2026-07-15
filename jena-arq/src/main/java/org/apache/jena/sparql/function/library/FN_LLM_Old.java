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

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FN_LLM_Old extends FunctionBase1 {
    private static final String LLM_URL = "https://chat-ai.academiccloud.de/v1/chat/completions"; // getAPIUrl(); //System.getenv("CHATAI_API_KEY"); //getAuthHeader();
    private static final String LLM_MODEL = "qwen3-30b-a3b-instruct-2507"; //"qwen2.5-coder-32b-instruct"; //""llama3:8B";
    private static final String SYSTEM_INSTRUCTION = "You are a helpful research assistant.";
    private static final Logger LOG = LoggerFactory.getLogger(FN_LLM_Old.class);
    private static final String LLM_API_KEY = System.getenv("CHATAI_API_KEY");
    public FN_LLM_Old() {
        super();
    }

    @Override
    public NodeValue exec(NodeValue v) {
        String prompt = v.getString();
        LOG.info("[FN_LLM_Old] Received prompt are: {}", prompt);
        try {
            String result = LLMClient.chatCompletion(
                    LLM_API_KEY,
                    LLM_URL, // e.g. http://localhost:8000/v1
                    LLM_MODEL,    // e.g. llama3
                    prompt
            );
            return NodeValue.makeString(result);

        } catch (Exception e) {
            throw new RuntimeException("LLM call failed", e);
        }
    }
}
