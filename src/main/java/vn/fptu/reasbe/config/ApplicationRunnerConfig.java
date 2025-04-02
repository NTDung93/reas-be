package vn.fptu.reasbe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.UserLocation;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.repository.UserLocationRepository;
import vn.fptu.reasbe.service.VectorStoreService;
import vn.fptu.reasbe.utils.common.GeometryUtils;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationRunnerConfig {
    private final ItemRepository itemRepository;
    private final VectorStoreService vectorStoreService;
    private final UserLocationRepository userLocationRepository;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            List<Item> items = itemRepository.findAllByStatusItem(StatusItem.AVAILABLE);
            vectorStoreService.addNewItem(items);
            List<UserLocation> userLocations = userLocationRepository.findAll();

            for (UserLocation userLocation : userLocations) {
                if (userLocation.getPoint() == null) {
                    userLocation.setPoint(GeometryUtils.createPoint(userLocation.getLongitude(), userLocation.getLatitude()));
                    userLocationRepository.save(userLocation);
                }
            }
        };
    }
}
