package org.sid.billingservice.repository;

import org.sid.billingservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource
@CrossOrigin("*")
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    public Collection<ProductItem> findByBillId(Long bill_id);
}
