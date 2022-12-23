package com.elcorazon.adminlte.repository;

import com.elcorazon.adminlte.model.settings.Watermark;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
/**********************************************************************************************************************/
@Repository
public interface WatermarkRepository extends PagingAndSortingRepository<Watermark, String> {
}
