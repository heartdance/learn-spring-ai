package org.example.chat.util;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyVectorStore implements VectorStore {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void add(List<Document> documents) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(List<String> idList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Filter.Expression filterExpression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Document> similaritySearch(SearchRequest request) {
        SearchRes searchRes = restTemplate.postForObject("http://localhost:8082/test/search",
                Map.of("query", request.getQuery()), SearchRes.class);
        if (searchRes.getDocuments() == null || searchRes.getDocuments().isEmpty()) {
            return List.of();
        }
        List<Document> documents = new ArrayList<>(searchRes.getDocuments().size());
        for (SearchDoc doc : searchRes.getDocuments()) {
            Document document = Document.builder()
                    .id(doc.getId())
                    .text(doc.getText())
                    .metadata(doc.getMetadata())
                    .score(doc.getScore())
                    .build();
            documents.add(document);
        }
        return documents;
    }

    private static class SearchRes {
        private List<SearchDoc> documents;

        public List<SearchDoc> getDocuments() {
            return documents;
        }

        public void setDocuments(List<SearchDoc> documents) {
            this.documents = documents;
        }
    }

    private static class SearchDoc {
        private String id;
        private String text;
        private Map<String, Object> metadata;
        private Double score;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Map<String, Object> getMetadata() {
            return metadata;
        }

        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }
    }
}
