package com.sepehr.sepehr.repository;

import com.sepehr.sepehr.entity.Person;
import com.sepehr.sepehr.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
@Repository
public interface TestRepository extends JpaRepository<Person,Long>,JpaSpecificationExecutor {
}
