$(document).ready(function() {
    loadUserInfo();
    // Sidebar toggle logic is also here to ensure it works after dynamic loading if needed, 
    // though the button is outside the sidebar so it's fine.
    // Existing pages might have their own toggle handler, we should check to avoid double binding.
    // But since we are moving towards a common JS, we can consolidate.
});

function loadUserInfo() {
    $.ajax({
        url: '/api/v1/admin/me',
        type: 'GET',
        success: function(user) {
            renderSidebar(user);
            updateUserDisplay(user);
        },
        error: function(xhr) {
            console.log("Error loading user info", xhr);
            if (xhr.status === 401 || xhr.status === 403) {
                window.location.href = '/login';
            }
        }
    });
}

function updateUserDisplay(user) {
    // Try to find the user display area in top navbar
    const userSpan = $('.navbar-top .dropdown span');
    if (userSpan.length) {
        let displayText = user.username;
        if (user.roles && user.roles.length > 0) {
            displayText += ` (${user.roles.join(', ')})`;
        }
        userSpan.text(displayText);
    }
}

function renderSidebar(user) {
    const roles = user.roles || [];
    console.log('User roles:', roles); // Debug roles

    const isAdmin = roles.includes('ADMIN');
    const isUserManage = roles.includes('UserManage');
    const isBookManage = roles.includes('BookManage');

    let menuHtml = '';

    // Helper to check active state
    const currentPath = window.location.pathname;
    const isActive = (key) => currentPath.includes(key) ? 'active' : '';
    
    // Update sidebar header title if needed (though it is static HTML in pages, we can update it here too)
    $('.sidebar-header h3').text('Quản Lý Hệ Thống');

    // Quản Lý Sách (ADMIN or BookManage)
    if (isAdmin || isBookManage) {
        menuHtml += `<a href="/admin/books" class="${isActive('book')}"><i class="bi bi-book"></i> Quản Lý Sách</a>`;
    }

    // Quản Lý User (ADMIN or UserManage)
    if (isAdmin || isUserManage) {
        menuHtml += `<a href="/admin/users" class="${isActive('user')}"><i class="bi bi-people"></i> Quản Lý User</a>`;
    }

    // Quản Lý Role (ADMIN only)
    if (isAdmin) {
        menuHtml += `<a href="/admin/roles" class="${isActive('role')}"><i class="bi bi-shield-lock"></i> Quản Lý Role</a>`;
    }

    // Quản Lý Quyền (ADMIN only)
    if (isAdmin) {
        menuHtml += `<a href="/admin/permissions" class="${isActive('permission')}"><i class="bi bi-key"></i> Quản Lý Quyền</a>`;
    }

    // Quản Lý Banner (ADMIN or BookManage)
    if (isAdmin || isBookManage) {
        menuHtml += `<a href="/admin/banners" class="${isActive('banner')}"><i class="bi bi-images"></i> Quản Lý Banner</a>`;
    }

    // Quản Lý Đơn Hàng (ADMIN only) - Explicitly requested to exclude BookManage
    if (isAdmin) {
        menuHtml += `<a href="/admin/orders" class="${isActive('order')}"><i class="bi bi-cart-check"></i> Quản Lý Đơn Hàng</a>`;
    }

    menuHtml += `<hr style="border-color: #4b545c;" />
                 <a href="/" target="_blank"><i class="bi bi-box-arrow-up-right"></i> Xem Trang Chủ</a>`;

    // Append to sidebar, removing old links first
    // Keep sidebar-header, remove everything else (including spinners or old links)
    $('.sidebar').children().not('.sidebar-header').remove();
    
    $('.sidebar').append(menuHtml);
}
