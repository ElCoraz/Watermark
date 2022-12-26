package com.elcorazon.adminlte.repository;

import com.elcorazon.adminlte.model.database.Watermark;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**********************************************************************************************************************/
@Repository
public interface WatermarkRepository extends PagingAndSortingRepository<Watermark, String> {
    /******************************************************************************************************************/
    @Override
    List<Watermark> findAll();
    /******************************************************************************************************************/
    @Override
    Optional<Watermark> findById(String id);
}