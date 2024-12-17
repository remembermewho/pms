package com.PSM.demo.repository;

import com.PSM.demo.model.PersonalTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalTaskRepository extends JpaRepository<PersonalTask, Long> {
}
