package com.casestudy.service;

import java.util.Optional;

public interface IGeneralService<T>{
    Iterable<T> findAll();

    T save(T t);

    Optional<T> findById(Long id);

    void remove(Long id);

    Iterable<T> findByName(String name);
}
