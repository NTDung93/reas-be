package vn.fptu.reasbe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.service.VectorStoreService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationRunnerConfig {
    private final ItemRepository itemRepository;
    private final VectorStoreService vectorStoreService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            List<Item> items = itemRepository.findAllByStatusItem(StatusItem.AVAILABLE);
            vectorStoreService.addNewItem(items);
        };
    }
}
