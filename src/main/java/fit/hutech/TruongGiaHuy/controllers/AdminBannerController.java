package fit.hutech.TruongGiaHuy.controllers;

import fit.hutech.TruongGiaHuy.entities.Banner;
import fit.hutech.TruongGiaHuy.services.BannerService;
import fit.hutech.TruongGiaHuy.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {
    private final BannerService bannerService;
    private final StorageService storageService;

    @GetMapping
    public String listBanners() {
        return "redirect:/admin/banners.html";
    }

    // @GetMapping("/create")
    // public String createBannerForm(Model model) {
    //     model.addAttribute("banner", new Banner());
    //     return "admin/banner/form";
    // }

    // @PostMapping("/save")
    // public String saveBanner(@ModelAttribute Banner banner,
    //                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
    //     if (imageFile != null && !imageFile.isEmpty()) {
    //         String imageUrl = storageService.store(imageFile, "Banners");
    //         banner.setImageUrl(imageUrl);
    //     } else {
    //         // Logic to keep existing image if needed, handled by hidden input or existing object state
    //         if (banner.getId() != null && (banner.getImageUrl() == null || banner.getImageUrl().isEmpty())) {
    //              Banner existingBanner = bannerService.getBannerById(banner.getId()).orElse(null);
    //              if (existingBanner != null) {
    //                  banner.setImageUrl(existingBanner.getImageUrl());
    //              }
    //         }
    //     }
    //     bannerService.saveBanner(banner);
    //     return "redirect:/admin/banners";
    // }

    // @GetMapping("/edit/{id}")
    // public String editBannerForm(@PathVariable Long id, Model model) {
    //     Banner banner = bannerService.getBannerById(id)
    //             .orElseThrow(() -> new IllegalArgumentException("Invalid banner Id:" + id));
    //     model.addAttribute("banner", banner);
    //     return "admin/banner/form";
    // }

    // @GetMapping("/delete/{id}")
    // public String deleteBanner(@PathVariable Long id) {
    //     bannerService.deleteBanner(id);
    //     return "redirect:/admin/banners";
    // }
}


