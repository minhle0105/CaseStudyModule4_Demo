package com.casestudy.service.orderDetails;

import com.casestudy.model.OrderDetails;
import com.casestudy.repository.orderDetails.IOrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderDetailsService implements IOrderDetailsService {

    @Autowired
    private IOrderDetailsRepository orderDetailsRepository;

    @Override
    public Iterable<OrderDetails> findAll() {
        return orderDetailsRepository.findAll();
    }

    @Override
    public OrderDetails save(OrderDetails orderDetails) {
        return orderDetailsRepository.save(orderDetails);
    }

    @Override
    public Page<OrderDetails> findAll(Pageable pageable) {
        return orderDetailsRepository.findAll(pageable);
    }

    @Override
    public Optional<OrderDetails> findById(Long id) {
        return orderDetailsRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        orderDetailsRepository.deleteById(id);
    }
}
