package com.casestudy.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IGeneralService<T>{
    Iterable<T> findAll();

    T save(T t);

    Page<T> findAll(Pageable pageable);

    Optional<T> findById(Long id);

    void remove(Long id);
}
