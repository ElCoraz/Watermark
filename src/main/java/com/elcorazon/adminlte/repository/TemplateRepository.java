package com.elcorazon.adminlte.repository;

import com.elcorazon.adminlte.model.database.Template;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**********************************************************************************************************************/
@Repository
public interface TemplateRepository extends PagingAndSortingRepository<Template, String> {
    @Override
    List<Template> findAll();
}