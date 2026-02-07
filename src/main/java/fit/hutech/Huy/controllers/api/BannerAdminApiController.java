package fit.hutech.Huy.controllers.api;

import fit.hutech.Huy.entities.Banner;
import fit.hutech.Huy.services.BannerService;
import fit.hutech.Huy.services.StorageService;
import fit.hutech.Huy.viewmodels.BannerGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/banners")
@RequiredArgsConstructor
public class BannerAdminApiController {

    private final BannerService bannerService;
    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<BannerGetVm>> getAllBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners().stream()
                .map(BannerGetVm::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerGetVm> getBanner(@PathVariable Long id) {
        return bannerService.getBannerById(id)
                .map(BannerGetVm::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BannerGetVm> createBanner(
            @RequestParam("title") String title,
            @RequestParam("linkUrl") String linkUrl,
            @RequestParam("displayOrder") Integer displayOrder,
            @RequestParam("active") boolean active,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        
        Banner banner = new Banner();
        banner.setTitle(title);
        banner.setLinkUrl(linkUrl);
        banner.setDisplayOrder(displayOrder);
        banner.setActive(active);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storageService.store(imageFile, "Banners");
            banner.setImageUrl(imageUrl);
        }

        bannerService.saveBanner(banner);
        return ResponseEntity.ok(BannerGetVm.from(banner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerGetVm> updateBanner(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("linkUrl") String linkUrl,
            @RequestParam("displayOrder") Integer displayOrder,
            @RequestParam("active") boolean active,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        
        Banner banner = bannerService.getBannerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found"));

        banner.setTitle(title);
        banner.setLinkUrl(linkUrl);
        banner.setDisplayOrder(displayOrder);
        banner.setActive(active);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storageService.store(imageFile, "Banners");
            banner.setImageUrl(imageUrl);
        }

        bannerService.saveBanner(banner);
        return ResponseEntity.ok(BannerGetVm.from(banner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }
}
