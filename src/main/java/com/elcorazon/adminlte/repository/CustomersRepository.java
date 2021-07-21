package com.elcorazon.adminlte.repository;
//**********************************************************************************************************************
import com.elcorazon.adminlte.model.Customers;
//**********************************************************************************************************************
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
//**********************************************************************************************************************
@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> { }
//**********************************************************************************************************************
