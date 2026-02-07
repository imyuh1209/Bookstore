package fit.hutech.Huy.repositories;

import fit.hutech.Huy.entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByActiveTrueOrderByDisplayOrderAsc();
}
