package kz.kanagatbeken.studentapi.service;

import kz.kanagatbeken.studentapi.dto.StudentRequest;
import kz.kanagatbeken.studentapi.exception.DuplicateEmailException;
import kz.kanagatbeken.studentapi.exception.StudentNotFoundException;
import kz.kanagatbeken.studentapi.model.Student;
import kz.kanagatbeken.studentapi.repository.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return repository.findAll(Sort.by("id"));
    }

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student create(StudentRequest request) {
        if (repository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        Student student = new Student();
        apply(student, request);
        return repository.save(student);
    }

    public Student update(Long id, StudentRequest request) {
        Student student = findById(id);
        boolean emailChanged = !student.getEmail().equalsIgnoreCase(request.email());
        if (emailChanged && repository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        apply(student, request);
        return repository.save(student);
    }

    public void delete(Long id) {
        Student student = findById(id);
        repository.delete(student);
    }

    private void apply(Student student, StudentRequest request) {
        student.setFirstName(request.firstName().trim());
        student.setLastName(request.lastName().trim());
        student.setEmail(request.email().trim().toLowerCase());
        student.setFaculty(request.faculty());
        student.setCourse(request.course());
    }
}
