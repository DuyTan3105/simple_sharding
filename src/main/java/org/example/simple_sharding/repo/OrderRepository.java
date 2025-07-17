package org.example.simple_sharding.repo;

import org.example.simple_sharding.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository mẫu cho sharded table
 * ShardingSphere sẽ tự động route queries đến đúng table dựa trên tenant_id
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find orders by tenant_id - sẽ được route đến đúng table
     */
    List<Order> findByTenantId(String tenantId);

    /**
     * Find orders by tenant_id and status
     */
    List<Order> findByTenantIdAndStatus(String tenantId, String status);

    /**
     * Find orders by tenant_id and order number
     */
    List<Order> findByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    /**
     * Custom query với tenant_id - sẽ được route đến đúng table
     */
    @Query("SELECT o FROM Order o WHERE o.tenantId = :tenantId AND o.totalAmount > :minAmount")
    List<Order> findByTenantIdAndTotalAmountGreaterThan(@Param("tenantId") String tenantId,
                                                        @Param("minAmount") Double minAmount);

    /**
     * Count orders by tenant_id
     */
    long countByTenantId(String tenantId);

    /**
     * Range query - sẽ query tất cả tables và merge results
     * CHÚ Ý: Queries không có tenant_id sẽ chậm hơn vì phải query tất cả tables
     */
    @Query("SELECT o FROM Order o WHERE o.totalAmount BETWEEN :minAmount AND :maxAmount")
    List<Order> findByTotalAmountBetween(@Param("minAmount") Double minAmount,
                                         @Param("maxAmount") Double maxAmount);
}