package com.app.gateway.repository;

import com.app.gateway.domain.B;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the B entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BRepository extends ReactiveCrudRepository<B, Long>, BRepositoryInternal {
    @Query("SELECT * FROM b entity WHERE entity.entaaauid_id = :id")
    Flux<B> findByEntAAAuid(Long id);

    @Query("SELECT * FROM b entity WHERE entity.entaaauid_id IS NULL")
    Flux<B> findAllWhereEntAAAuidIsNull();

    @Override
    <S extends B> Mono<S> save(S entity);

    @Override
    Flux<B> findAll();

    @Override
    Mono<B> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BRepositoryInternal {
    <S extends B> Mono<S> save(S entity);

    Flux<B> findAllBy(Pageable pageable);

    Flux<B> findAll();

    Mono<B> findById(Long id);

    Flux<B> findAllBy(Pageable pageable, Criteria criteria);
}
