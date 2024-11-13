
document.getElementById('userProfileForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Ngừng hành động submit mặc định của form

    let urlParams = new URLSearchParams(window.location.search);
    let id = urlParams.get('id');

    let name = document.getElementById('name').value.trim();
    let email = document.getElementById('email').value.trim();
    let phone = document.getElementById('phone').value.trim();
    let password = document.getElementById('password').value.trim();
    let address = document.getElementById('address').value.trim();
    console.log(phone);
    fetch("/user-profile/send", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({id:id, name: name, email: email, phone: phone, password: password, address: address })
    }).then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Thay đổi thông tin thành công');
                window.location.href=`/user-profile?id=${id}`;
            } else {
                document.getElementById('message').style.display = "block";
                document.getElementById('message').textContent = data.message;
            }
        }).catch(error => {
            console.error("Lỗi khi thay đổi thông tin:", error);
            document.getElementById('message').style.display = "block";
            document.getElementById('message').textContent = "Đã xảy ra lỗi khi thay đổi thông tin.";
        })
});


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
