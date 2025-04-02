package vn.fptu.reasbe.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.service.VectorStoreService;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class VectorStoreServiceImpl implements VectorStoreService {
    private final PgVectorStore vectorStore;

    @Override
    public void addNewItem(List<Item> items) {
        for (Item item : items) {

            if (existItem(item.getId())) {
                continue; // Bỏ qua nếu đã tồn tại trong vector database
            }

            Document newItemDocs = new Document(prepareItemContent(item),
                    Map.of("itemId", item.getId(),
                            "itemName", item.getItemName(),
                            "brandName", item.getBrand().getBrandName(),
                            "categoryName", item.getCategory().getCategoryName(),
                            "price", item.getPrice(),
                            "description", item.getDescription(),
                            "conditionItem", item.getConditionItem().getCode(),
                            "ownerId", item.getOwner().getId()));

            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> docs = splitter.apply(List.of(newItemDocs));
            vectorStore.add(docs);
        }
    }

    @Override
    public List<Document> searchSimilarItems(String query, String filter, int limit) {
        return vectorStore.similaritySearch(SearchRequest.builder()
                .query(query)
                .filterExpression(filter)
                .topK(limit)
                .build()
        );
    }

    @Override
    public void deleteItem(List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new ReasApiException(HttpStatus.NOT_FOUND, "error.vectorStore.emptyItemList");
        }

        List<String> documents = getDocumentIdsByItemId(items);

        if (documents != null && !documents.isEmpty()) {
            vectorStore.delete(documents);
        }
    }

    private boolean existItem(Integer itemId) {
        String filter = "itemId == " + itemId;

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(filter) // Dummy query vì ta dùng filter để tìm
                        .filterExpression(filter)
                        .topK(1) // Chỉ cần kiểm tra xem có ít nhất 1 item hay không
                        .build()
        );

        return documents != null && !documents.isEmpty();
    }


    private List<String> getDocumentIdsByItemId(List<Item> items) {
        StringBuilder filter = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                filter.append(" || ");
            }
            filter.append("itemId == ").append(items.get(i).getId());
        }

        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                        .query(filter.toString())
                .filterExpression(filter.toString())
                        .topK(items.size())
                .build());

        if (documents != null && !documents.isEmpty()) {
            return documents.stream()
                    .map(Document::getId)
                    .toList();
        }
        return null;
    }

    private String prepareItemContent(Item item) {
        return String.format("Item: %s, Brand: %s, Category: %s, Price: %s, Description: %s, Condition: %s",
                item.getItemName(),
                item.getBrand().getBrandName(),
                item.getCategory().getCategoryName(),
                item.getPrice().toString(),
                item.getDescription(),
                item.getConditionItem().getCode());
    }
}