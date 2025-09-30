package org.example.chat.config;

import org.example.chat.util.MyVectorStore;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    @Bean
    public VectorStore vectorStore() {
        return new MyVectorStore();
    }

    @Bean
    public QuestionAnswerAdvisor questionAnswerAdvisor(VectorStore vectorStore) {
        PromptTemplate customPromptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
                    <query>
        
                    Context information is below.
        
                    ---------------------
                    <question_answer_context>
                    ---------------------
        
                    Given the context information and no prior knowledge, answer the query.
        
                    Follow these rules:
        
                    1. If the answer is not in the context, just say that you don't know.
                    2. Avoid statements like "Based on the context..." or "The provided information...".
                    """)
                .build();
        return QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(customPromptTemplate)
                .build();
    }
}
