package sejong.foodsns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.foodsns.domain.file.BoardFile;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
