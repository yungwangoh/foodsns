package sejong.foodsns.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.file.BoardFile;

@Repository
public interface BoardFileRepository extends JpaRepository<Long, BoardFile> {
}
