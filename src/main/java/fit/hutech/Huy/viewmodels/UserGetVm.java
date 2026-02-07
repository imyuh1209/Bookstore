package fit.hutech.Huy.viewmodels;

import fit.hutech.Huy.entities.User;
import java.util.List;
import java.util.stream.Collectors;

public record UserGetVm(Long id, String username, String email, String phone, String provider, List<RoleGetVm> roles) {
    public static UserGetVm from(User user) {
        List<RoleGetVm> roleVms = user.getRoles() != null 
            ? user.getRoles().stream().map(RoleGetVm::from).collect(Collectors.toList())
            : List.of();
        return new UserGetVm(
            user.getId(), 
            user.getUsername(), 
            user.getEmail(), 
            user.getPhone(), 
            user.getProvider(), 
            roleVms
        );
    }
}
