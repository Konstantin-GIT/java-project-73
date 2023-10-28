package hexlet.code.repository;

import hexlet.code.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long>, CrudRepository<TaskStatus, Long> {
    Optional<TaskStatus> findByName(String name);
}
