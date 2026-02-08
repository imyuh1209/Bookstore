package fit.hutech.TruongGiaHuy.repositories;

import fit.hutech.TruongGiaHuy.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}


