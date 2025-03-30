package vn.fptu.reasbe;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.service.VectorStoreService;

import java.util.List;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class ReasBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReasBeApplication.class, args);
	}

	private final ItemRepository itemRepository;
	private final VectorStoreService vectorStoreService;

	boolean ingest = true;

	@Bean
	ApplicationRunner getApplicationRunner() {
		return args -> {
			List<Item> items = itemRepository.findAllByStatusItem(StatusItem.AVAILABLE);

			if (ingest) {
				vectorStoreService.addNewItem(items);
			}
		};
	}
}
