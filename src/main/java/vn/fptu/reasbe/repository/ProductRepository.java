package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.Product;
import vn.fptu.reasbe.repository.custom.ProductRepositoryCustom;

public interface ProductRepository extends JpaRepository<Product, Integer>, QuerydslPredicateExecutor<Product>, ProductRepositoryCustom {
}
