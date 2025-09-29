package org.example.rag.controller;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {
    private final EmbeddingModel embeddingModel;

    public RagController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("test/embed")
    public float[] embed() {
        return embeddingModel.embed("测试");
    }
}
