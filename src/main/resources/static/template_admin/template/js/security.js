const sidebarItems = {
    admin: [".sidebar-dashboard", ".sidebar-customer", ".sidebar-admin", ".sidebar-notification", ".sidebar-review"],
    tour_manager: [".sidebar-dashboard", ".sidebar-tour", ".sidebar-notification", ".sidebar-transportation",".sidebar-review"],
    booking_manager: [".sidebar-dashboard", ".sidebar-booking", ".sidebar-notification", ".sidebar-transportation", ".sidebar-customer"]
};
function hideSideBar(){
    document.querySelectorAll('.nav-item').forEach(item => {
        item.style.display = "none";
    });
}
function showSidebar(role) {
    sidebarItems[role].forEach(selector => {
        const item = document.querySelector(selector);
        if (item) {
            item.style.display = "block";
        }
    });
}

function getCookie(name) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(name + '=')) {
            return cookie.substring(name.length + 1);
        }
    }
    return null;
}

//if (!getCookie("adminId")) {
//    window.location.href = "/admin/login";
//} else {
    hideSideBar();
    const role = getCookie("roles");
    console.log(role);
    showSidebar(role);
    const currentUrl = window.location.href;

    const accessUrls = {
        admin: [
            "http://localhost:8080/admin/customer",
            "http://localhost:8080/admin/review",
            "http://localhost:8080/admin/notification",
            "http://localhost:8080/admin/index",
            "http://localhost:8080/admin/qtv"
        ],
        tour_manager: [
            "http://localhost:8080/admin/tour",
            "http://localhost:8080/admin/index",
            "http://localhost:8080/admin/notification",
            "http://localhost:8080/admin/transportation",
            "http://localhost:8080/admin/review",


        ],
        booking_manager: [
            "http://localhost:8080/admin/booking",
            "http://localhost:8080/admin/index",
            "http://localhost:8080/admin/notification",
            "http://localhost:8080/admin/transportation",
            "http://localhost:8080/admin/customer",
        ]
    };

    function hasAccess(role, url) {
        if (!accessUrls[role]) return false; // Vai trò không tồn tại
        return accessUrls[role].some(allowedUrl => url.includes(allowedUrl));
    }

    if (!hasAccess(role, currentUrl)) {
        window.location.href = "/admin/index";
    }
//}
