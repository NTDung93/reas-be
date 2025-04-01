package vn.fptu.reasbe.config;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Value("${spring.ai.openai.api-key}")
    private String OPEN_AI_KEY;

    @Value("${spring.ai.openai.embedding.options.model}")
    private String OPEN_AI_MODEL;

    @Bean
    public OpenAiEmbeddingModel embeddingModel() {
        OpenAiApi aiApi = new OpenAiApi(OPEN_AI_KEY);
        return new OpenAiEmbeddingModel(aiApi,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model(OPEN_AI_MODEL)
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE);
    }
}
