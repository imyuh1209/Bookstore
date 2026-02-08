package fit.hutech.TruongGiaHuy.repositories;

import fit.hutech.TruongGiaHuy.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
}


