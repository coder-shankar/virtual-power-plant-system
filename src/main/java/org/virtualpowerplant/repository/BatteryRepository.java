package org.virtualpowerplant.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.virtualpowerplant.entity.Battery;
import org.virtualpowerplant.model.BatterySearchCriteria;

import java.util.List;

public interface BatteryRepository extends JpaRepository<Battery, Long>, JpaSpecificationExecutor<Battery> {

    default List<Battery> filter(BatterySearchCriteria criteria) {
        Specification<Battery> spec = Specification.where(null);

        if (criteria.startPostcode() != null && criteria.endPostcode() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("postcode"), criteria.startPostcode(), criteria.endPostcode()));
        }

        if (criteria.minCapacity() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.ge(root.get("wattCapacity"), criteria.minCapacity()));
        }

        if (criteria.maxCapacity() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.le(root.get("wattCapacity"), criteria.maxCapacity()));
        }

        return findAll(spec, Sort.by(Sort.Order.asc("name").ignoreCase())
        );
    }

}


