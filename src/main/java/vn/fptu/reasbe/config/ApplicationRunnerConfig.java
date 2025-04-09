package vn.fptu.reasbe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.fptu.reasbe.model.dto.item.ItemRunnerDTO;
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
    public ApplicationRunner userLocationInitRunner() {
        return args -> {
            List<UserLocation> userLocations = userLocationRepository.findAllByPointNull();
            for (UserLocation userLocation : userLocations) {
                userLocation.setPoint(GeometryUtils.createPoint(userLocation.getLongitude(), userLocation.getLatitude()));
            }
            userLocationRepository.saveAll(userLocations);
        };
    }

    @Bean
    public ApplicationRunner vectorStoreInitRunner() {
        return args -> {
            List<ItemRunnerDTO> items = itemRepository.findAllItemRunnerByStatus(StatusItem.AVAILABLE);
            vectorStoreService.addNewItemInRunner(items);
        };
    }
}
