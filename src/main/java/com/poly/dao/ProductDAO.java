package com.poly.dao;

import com.poly.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Integer> {
    @Query(value = "select * from products where name like %?1%", nativeQuery = true)
    Page<Product> findByKeywords(String keywords,
                                              Pageable pageable);


    @Query(value = "select top 5 * from products ORDER BY NEWID()",nativeQuery = true )
    List<Product> find5Product();

    @Query(value = "select top 5 * from products where category_id = 2 ORDER BY NEWID()",
            nativeQuery = true )
    List<Product> find5SmartPhone();

    @Query(value = "select top 5 * from products where category_id = 1 ORDER BY NEWID()",
            nativeQuery = true )
    List<Product> find5Laptop();

    @Query(value = "select top 5 * from products where category_id = 3 ORDER " +
            "BY NEWID()",
            nativeQuery = true )
    List<Product> find5Accessory();

    @Query(value = "select * from products where category_id = ?1",
            nativeQuery = true )
    List<Product> fillProductOfCategory(String category_id);

    @Query(value = "SELECT * FROM products where category_id = ?1",
            nativeQuery = true )
    Page<Product> fillByCategory(String category_id,
                                 Pageable pageable);

    @Query(value = "SELECT * FROM products where category_id = ?1 and " +
            "supplier_id = ?2",
            nativeQuery = true )
    Page<Product> findBandCategory(String category_id, String supplier_id, Pageable pageable);

    @Query(value = "SELECT * FROM products where category_id = ?1 order by " +
            "price ASC",
            nativeQuery = true )
    Page<Product> SortByAscending(String category_id, Pageable pageable);

    @Query(value = "SELECT * FROM products where category_id = ?1 order by " +
            "price DESC",
            nativeQuery = true )
    Page<Product> SortByDecrease(String category_id, Pageable pageable);

    @Query(value = "SELECT * FROM products where category_id = ?1 order by " +
            "import_date ASC",
            nativeQuery = true )
    Page<Product> SortByDateAscending(String category_id, Pageable pageable);

    @Query(value = "SELECT * FROM products where category_id = ?1 order by " +
            "import_date DESC",
            nativeQuery = true )
    Page<Product> SortByDateDecrease(String category_id, Pageable pageable);

    @Query(value = "select * from products where name like %?1%",
            nativeQuery = true )
    List<Product> findByName(String name);
}

