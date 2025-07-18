package com.dietergandalf.store_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dietergandalf.store_manager.model.Customer;
import com.dietergandalf.store_manager.model.Order;
import com.dietergandalf.store_manager.model.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    
    @Query("SELECT o FROM Order o WHERE o.customer.person_id = :customerId ORDER BY o.orderDate DESC")
    List<Order> findByCustomerPersonIdOrderByOrderDateDesc(@Param("customerId") Long customerId);
    
    List<Order> findByStatus(OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.customer.person_id = :customerId AND o.status = :status")
    List<Order> findByCustomerPersonIdAndStatus(@Param("customerId") Long customerId, @Param("status") OrderStatus status);
}
