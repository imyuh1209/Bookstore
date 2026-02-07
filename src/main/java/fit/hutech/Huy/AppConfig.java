package fit.hutech.Huy;

import fit.hutech.Huy.entities.Banner;
import fit.hutech.Huy.entities.Book;
import fit.hutech.Huy.entities.Category;
import fit.hutech.Huy.entities.Permission;
import fit.hutech.Huy.entities.Role;
import fit.hutech.Huy.entities.User;
import fit.hutech.Huy.repositories.IBannerRepository;
import fit.hutech.Huy.repositories.IBookRepository;
import fit.hutech.Huy.repositories.ICategoryRepository;
import fit.hutech.Huy.repositories.IPermissionRepository;
import fit.hutech.Huy.repositories.IRoleRepository;
import fit.hutech.Huy.repositories.IUserRepository;
import fit.hutech.Huy.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public CommandLineRunner initDatabase(IBookRepository bookRepository,
                                          ICategoryRepository categoryRepository,
                                          IUserRepository userRepository,
                                          IRoleRepository roleRepository,
                                          IPermissionRepository permissionRepository,
                                          IBannerRepository bannerRepository,
                                          UserService userService) {
        return args -> {
            try {
                // Init Banners
                if (bannerRepository.count() == 0) {
                    Banner banner1 = new Banner(null, "Khuyến Mãi Mùa Hè", "https://img.freepik.com/free-vector/horizontal-sale-banner-template-world-book-day-celebration_23-2150165975.jpg", "/books", 1, true);
                    Banner banner2 = new Banner(null, "Sách Mới Về", "https://img.freepik.com/free-vector/flat-world-book-day-social-media-cover-template_23-2149329762.jpg", "/books", 2, true);
                    Banner banner3 = new Banner(null, "Văn Học Kinh Điển", "https://img.freepik.com/free-vector/hand-drawn-world-book-day-social-media-cover-template_23-2149313274.jpg", "/books", 3, true);
                    bannerRepository.saveAll(Arrays.asList(banner1, banner2, banner3));
                }

                // Init Permissions
                if (permissionRepository.count() == 0) {
                    Permission p1 = new Permission(null, "MANAGE_BOOKS", "Quản lý sách (Thêm, Sửa, Xóa)", null);
                    Permission p2 = new Permission(null, "MANAGE_USERS", "Quản lý người dùng", null);
                    Permission p3 = new Permission(null, "VIEW_DASHBOARD", "Xem Dashboard", null);
                    permissionRepository.saveAll(Arrays.asList(p1, p2, p3));
                }

                // Init Roles
                if (roleRepository.count() == 0) {
                    Role adminRole = new Role(null, "ADMIN", "Administrator", null, null);
                    Role userRole = new Role(null, "USER", "User", null, null);
                    roleRepository.saveAll(Arrays.asList(adminRole, userRole));
                    
                    // Assign Permissions to ADMIN
                    List<Permission> allPerms = permissionRepository.findAll();
                    adminRole.setPermissions(allPerms);
                    roleRepository.save(adminRole);
                }

                if (categoryRepository.count() == 0) {
                    Category congNghe = new Category(null, "Công nghệ", null);
                    Category lichSu = new Category(null, "Lịch sử", null);
                    Category vanHoc = new Category(null, "Văn học", null);
                    Category kyNang = new Category(null, "Kỹ năng sống", null);

                    List<Category> categories = categoryRepository.saveAll(Arrays.asList(congNghe, lichSu, vanHoc, kyNang));
                    Category savedCongNghe = categories.get(0);
                    Category savedLichSu = categories.get(1);
                    Category savedVanHoc = categories.get(2);
                    Category savedKyNang = categories.get(3);

                    // Seed Books
                    Book b1 = new Book(null, "Clean Code", "Robert C. Martin", 300000.0, 10, savedCongNghe, "https://m.media-amazon.com/images/I/41xShlnTZTL._SX376_BO1,204,203,200_.jpg", "A Handbook of Agile Software Craftsmanship. Clean Code is divided into three parts. The first describes the principles, patterns, and practices of writing clean code. The second part consists of several case studies of increasing complexity. Each case study is an exercise in cleaning up code—of transforming a code base that has some problems into one that is sound and efficient. The third part is the payoff: a single chapter containing a list of heuristics and smells gathered while creating the case studies. The result is a knowledge base that describes the way we think when we write, read, and clean code.", null);
                    Book b2 = new Book(null, "The Pragmatic Programmer", "Andrew Hunt", 350000.0, 15, savedCongNghe, "https://m.media-amazon.com/images/I/51W1sBPO7tL._SX380_BO1,204,203,200_.jpg", "your journey to mastery. The Pragmatic Programmer cuts through the increasing specialization and technicalities of modern software development to examine the core process--taking a requirement and producing working, maintainable code that delights its users.", null);
                    Book b3 = new Book(null, "Sapiens: A Brief History of Humankind", "Yuval Noah Harari", 250000.0, 20, savedLichSu, "https://m.media-amazon.com/images/I/41yu2qXhXXL._SX324_BO1,204,203,200_.jpg", "From a renowned historian comes a groundbreaking narrative of humanity’s creation and evolution—a #1 international bestseller—that explores the ways in which biology and history have defined us and enhanced our understanding of what it means to be \"human.\"", null);
                    Book b4 = new Book(null, "Đắc Nhân Tâm", "Dale Carnegie", 150000.0, 50, savedKyNang, "https://m.media-amazon.com/images/I/51wOOMQ+F3L._SX329_BO1,204,203,200_.jpg", "Quyển sách duy nhất về thể loại self-help liên tục đứng đầu danh mục bán chạy nhất của báo The New York Times suốt 10 năm liền. Tác phẩm có sức lan toả vô cùng rộng lớn - dù bạn đi đến bất cứ đâu, ở bất kỳ quốc gia nào cũng đều có thể nhìn thấy. Tác phẩm được đánh giá là quyển sách đầu tiên và hay nhất, có ảnh hưởng làm thay đổi cuộc đời của hàng triệu người trên thế giới.", null);
                    
                    bookRepository.saveAll(Arrays.asList(b1, b2, b3, b4));
                }


                // Init Admin User
                if (userRepository.findByUsername("admin").isEmpty()) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword("123"); // Password will be encoded in UserService.save
                    admin.setEmail("admin@gmail.com");
                    admin.setPhone("1234567890");
                    userService.save(admin);
                    
                    // Assign ADMIN role
                    Role adminRole = roleRepository.findRoleByName("ADMIN").orElseThrow();
                    admin.setRoles(new ArrayList<>(List.of(adminRole)));
                    userRepository.save(admin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
