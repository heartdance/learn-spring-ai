package org.example.rag.controller;

import org.example.rag.entity.SearchParams;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RagController {
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public RagController(EmbeddingModel embeddingModel, VectorStore vectorStore) {
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    @GetMapping("test/embed")
    public float[] embed() {
        return embeddingModel.embed("测试");
    }

    @PostMapping("test/search")
    public Map<String, List<Document>> search(@RequestBody SearchParams params) {
        List<Document> documents = vectorStore.similaritySearch(params.getQuery());
        return Map.of("documents", documents);
    }
}
