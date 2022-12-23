package com.elcorazon.adminlte.repository;

import com.elcorazon.adminlte.model.database.Watermark;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**********************************************************************************************************************/
@Repository
public interface TemplateRepository extends PagingAndSortingRepository<Watermark, String> {}