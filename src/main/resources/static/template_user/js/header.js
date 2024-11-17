document.addEventListener("DOMContentLoaded", function() {
        const token = localStorage.getItem("jwtToken");

        if (token) {
            document.getElementById("loginBtn").style.display = "none";
            document.getElementById("userMenu").style.display = "block";
        } else {
            document.getElementById("loginBtn").style.display = "block";
            document.getElementById("userMenu").style.display = "none";
        }

        // Đăng xuất
        document.getElementById("logoutBtn")?.addEventListener("click", function() {
            localStorage.removeItem("jwtToken");
            window.location.href = "/login";
        });
    });


function decodeJwtPayload(token) {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace('-', '+').replace('_', '/');
    let decoded = atob(base64);
    return JSON.parse(decoded);
}
document.getElementById('userProfile')?.addEventListener('click', function (event) {
    let token = localStorage.getItem("jwtToken");
    if (!token) {
        window.location.href = "/home";  // Nếu không có token, chuyển về trang home
        return;
    }

    // Lấy thông tin người dùng từ token
    let payload = decodeJwtPayload(token);
    let customerId = payload.id;  // Lấy customerId từ token
    console.log(customerId);
    window.location.href = `/user-profile?id=${customerId}`;
});
document.getElementById('notification')?.addEventListener('click', function (event) {
    let token = localStorage.getItem("jwtToken");
    if (!token) {
        window.location.href = "/home";  // Nếu không có token, chuyển về trang home
        return;
    }

    // Lấy thông tin người dùng từ token
    let payload = decodeJwtPayload(token);
    let customerId = payload.id;  // Lấy customerId từ token
    console.log(customerId);
    window.location.href = `/notification?id=${customerId}`;
});
document.getElementById('history')?.addEventListener('click', function (event) {
    let token = localStorage.getItem("jwtToken");
    if (!token) {
        window.location.href = "/home";  // Nếu không có token, chuyển về trang home
        return;
    }
    // Lấy thông tin người dùng từ token
    let payload = decodeJwtPayload(token);
    let customerId = payload.id;  // Lấy customerId từ token
    console.log(customerId);
    window.location.href = `/history?id=${customerId}`;
});
