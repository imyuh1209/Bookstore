package fit.hutech.TruongGiaHuy.constants;

public enum InvoiceStatus {
    PENDING("Chờ xác nhận"),
    SHIPPING("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã huỷ");

    public final String label;

    InvoiceStatus(String label) {
        this.label = label;
    }
}


