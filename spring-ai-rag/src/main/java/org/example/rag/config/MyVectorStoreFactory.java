package org.example.rag.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class MyVectorStoreFactory {

    private final MyMarkdownReader reader;
    private final MyTokenTextSplitter splitter;

    public MyVectorStoreFactory(MyMarkdownReader reader, MyTokenTextSplitter splitter) {
        this.reader = reader;
        this.splitter = splitter;
    }

    @Bean
    public VectorStore myVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File file = new File("data/vectorStore.cache");
        if (file.exists()) {
            vectorStore.load(file);
        } else {
            List<Document> documents = reader.loadMarkdown();
            documents = splitter.splitDocuments(documents);
            vectorStore.add(documents);
            vectorStore.save(file);
        }
        return vectorStore;
    }
}
