package kz.kanagatbeken.studentapi.repository;

import kz.kanagatbeken.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmailIgnoreCase(String email);

    // Feature by Kanagat Beken: search students by last name (case-insensitive, partial match)
    List<Student> findByLastNameContainingIgnoreCaseOrderByIdAsc(String lastName);
}
