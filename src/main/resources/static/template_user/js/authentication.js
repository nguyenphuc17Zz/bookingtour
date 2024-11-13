    document.getElementById('loginForm').addEventListener('submit', function(event) {
        event.preventDefault(); // Ngừng hành động submit mặc định của form

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        fetch("/login/send", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email: email, password: password })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                localStorage.setItem("jwtToken", data.jwt);
                window.location.href = "/home";
            } else {
                document.getElementById('message').style.display = "block";
                document.getElementById('message').textContent = data.message;
            }
        })
        .catch(error => {
            console.error("Lỗi đăng nhập:", error);
            document.getElementById('message').style.display = "block";
            document.getElementById('message').textContent = "Đã xảy ra lỗi khi đăng nhập.";
        });
    });
