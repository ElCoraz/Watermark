package com.elcorazon.adminlte.repository;

import com.elcorazon.adminlte.model.database.Images;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**********************************************************************************************************************/
@Repository
public interface ImagesRepository extends PagingAndSortingRepository<Images, String> {
    /******************************************************************************************************************/
    @Override
    List<Images> findAll();
}