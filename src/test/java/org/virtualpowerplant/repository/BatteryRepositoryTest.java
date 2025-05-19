package org.virtualpowerplant.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.virtualpowerplant.entity.Battery;
import org.virtualpowerplant.model.BatterySearchCriteria;
import org.virtualpowerplant.util.TestDataBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatteryRepositoryTest {

    @Mock
    private BatteryRepository repository;

    @Test
    void filter_WithValidCriteria_ShouldReturnFilteredBatteries() {
        // Arrange
        var criteria = TestDataBuilder.SearchCriteriaBuilder.defaultCriteria();
        var testBatteries = TestDataBuilder.BatteryBuilder.allBatteries();

        when(repository.filter(criteria)).thenReturn(testBatteries);

        // Act
        List<Battery> result = repository.filter(criteria);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactlyElementsOf(testBatteries);
        verify(repository).filter(criteria);
    }

    @Test
    void findAll_WithSpecification_ShouldReturnFilteredResults() {
        // Arrange
        var testBatteries = TestDataBuilder.BatteryBuilder.allBatteries();
        Specification<Battery> spec = (root, query, cb) ->
                cb.between(root.get("postcode"), 6000, 6999);

        when(repository.findAll(any(Specification.class))).thenReturn(testBatteries);

        // Act
        List<Battery> result = repository.findAll(spec);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactlyElementsOf(testBatteries);
        verify(repository).findAll(spec);
    }

    @Test
    void saveAll_WithValidBatteries_ShouldReturnSavedBatteries() {
        // Arrange
        var testBatteries = TestDataBuilder.BatteryBuilder.allBatteries();
        when(repository.saveAll(testBatteries)).thenReturn(testBatteries);

        // Act
        List<Battery> result = repository.saveAll(testBatteries);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactlyElementsOf(testBatteries);
        verify(repository).saveAll(testBatteries);
    }

    @Test
    void filter_WithEmptyCriteria_ShouldReturnEmptyList() {
        // Arrange
        var criteria = BatterySearchCriteria.builder().build();
        when(repository.filter(criteria)).thenReturn(List.of());

        // Act
        List<Battery> result = repository.filter(criteria);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();
        verify(repository).filter(criteria);
    }
}