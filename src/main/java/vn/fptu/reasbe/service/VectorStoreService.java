package vn.fptu.reasbe.service;

import org.springframework.ai.document.Document;
import vn.fptu.reasbe.model.dto.item.ItemRunnerDTO;
import vn.fptu.reasbe.model.entity.Item;

import java.util.List;

public interface VectorStoreService {
    void addNewItem(List<Item> items);

    void addNewItemInRunner(List<ItemRunnerDTO> items);

    List<Document> searchSimilarItems(String query, String filter, int limit);

    void deleteItem(List<Item> items);
}