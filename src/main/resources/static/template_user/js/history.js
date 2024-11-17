document.addEventListener("DOMContentLoaded", function () {
    let token = localStorage.getItem("jwtToken");
    if (!token) {
        window.location.href = "/home";
        return;
    }

    let payload = decodeJwtPayload(token);
    let customerId = payload.id;

     let urlParams = new URLSearchParams(window.location.search);
        let queryCustomerId = urlParams.get('id');

        if (customerId != queryCustomerId) {
            window.location.href = "/home";
        }
});

function decodeJwtPayload(token) {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace('-', '+').replace('_', '/');
    let decoded = atob(base64);
    return JSON.parse(decoded);
}