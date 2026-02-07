package fit.hutech.Huy.viewmodels;

import fit.hutech.Huy.entities.Banner;

public record BannerGetVm(Long id, String title, String imageUrl, String linkUrl, Integer displayOrder, boolean active) {
    public static BannerGetVm from(Banner banner) {
        return new BannerGetVm(
            banner.getId(),
            banner.getTitle(),
            banner.getImageUrl(),
            banner.getLinkUrl(),
            banner.getDisplayOrder(),
            banner.isActive()
        );
    }
}
