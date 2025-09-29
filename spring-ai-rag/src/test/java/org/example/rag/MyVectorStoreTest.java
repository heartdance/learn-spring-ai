package org.example.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MyVectorStoreTest {

    @Autowired
    private VectorStore vectorStore;

    @Test
    public void testSearch() {
        List<Document> documents = vectorStore.similaritySearch("工作时间");
        System.out.println(documents);
    }
}
