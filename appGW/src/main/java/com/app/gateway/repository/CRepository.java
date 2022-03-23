package com.app.gateway.repository;

import com.app.gateway.domain.C;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the C entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CRepository extends ReactiveCrudRepository<C, Long>, CRepositoryInternal {
    @Override
    <S extends C> Mono<S> save(S entity);

    @Override
    Flux<C> findAll();

    @Override
    Mono<C> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CRepositoryInternal {
    <S extends C> Mono<S> save(S entity);

    Flux<C> findAllBy(Pageable pageable);

    Flux<C> findAll();

    Mono<C> findById(Long id);

    Flux<C> findAllBy(Pageable pageable, Criteria criteria);
}
