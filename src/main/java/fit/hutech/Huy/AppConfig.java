package fit.hutech.Huy;

import fit.hutech.Huy.entities.Book;
import fit.hutech.Huy.entities.Category;
import fit.hutech.Huy.entities.Role;
import fit.hutech.Huy.entities.User;
import fit.hutech.Huy.repositories.IBookRepository;
import fit.hutech.Huy.repositories.ICategoryRepository;
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
                                          UserService userService) {
        return args -> {
            try {
                // Init Roles
                if (roleRepository.count() == 0) {
                    Role adminRole = new Role(null, "ADMIN", "Administrator", null);
                    Role userRole = new Role(null, "USER", "User", null);
                    roleRepository.saveAll(Arrays.asList(adminRole, userRole));
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
