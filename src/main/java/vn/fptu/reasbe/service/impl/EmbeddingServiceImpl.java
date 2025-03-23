package vn.fptu.reasbe.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.service.EmbeddingService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingModel embeddingModel;

    @Override
    public float[] generateEmbedding(String text) {
        return embeddingModel.embed(text); // Get the float array from the embedding model
    }
}
