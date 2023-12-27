package com.ExamPort.ExamServer.repository;

import com.ExamPort.ExamServer.Entities.exam.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long > {
}
