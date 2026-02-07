package fit.hutech.Huy.services;

import fit.hutech.Huy.entities.Banner;
import fit.hutech.Huy.repositories.IBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final IBannerRepository bannerRepository;

    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    public List<Banner> getActiveBanners() {
        return bannerRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    public Optional<Banner> getBannerById(Long id) {
        return bannerRepository.findById(id);
    }

    public void saveBanner(Banner banner) {
        bannerRepository.save(banner);
    }

    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}
