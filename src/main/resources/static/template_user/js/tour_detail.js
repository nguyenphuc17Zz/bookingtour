document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("jwtToken");
    const commentSection = document.getElementById("add-comment-section");
    let starRating = 0;

    if (!token) {
        commentSection.style.display = "none";
    } else {
        commentSection.style.display = "block";
    }

    // Đánh giá sao
    const stars = document.querySelectorAll(".rating-stars i");

    stars.forEach(star => {
        star.addEventListener("click", function () {
            const rating = parseInt(star.getAttribute("data-index"));
            starRating = rating;  // Cập nhật trực tiếp giá trị starRating
            // Cập nhật các sao đã chọn
            stars.forEach((s, index) => {
                if (index < rating) {
                    s.classList.add("selected");
                } else {
                    s.classList.remove("selected");
                }
            });
        });
    });

    // Thêm sự kiện gửi bình luận
    document.getElementById("submit-comment").addEventListener("click", function () {
        // Lấy URL hiện tại
        const url = window.location.href;

        const urlParts = url.split('/');
        const tourId = urlParts[urlParts.length - 1];
        let payload = decodeJwtPayload(token);
        let customerId = payload.id;
        let date = new Date();
        let status = 1;
        const commentText = document.getElementById("comment-input").value.trim();

        if (starRating === 0) {
            alert('Chưa chọn sao');
            return;
        }
        if (commentText === "") {
            alert('Chưa nhập bình luận');
            return;
        }



        fetch("/review/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                tour_id: tourId,
                customer_id: customerId,
                rating: starRating,
                review_comment: commentText,
                review_date: date,
                status: status
            })
        }).then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    window.location.href = `/tour_detail/${tourId}`;
                } else {
                    document.getElementById('message').style.display = "block";
                    document.getElementById('message').textContent = data.message;
                }
            }).catch(error => {
                console.error("Lỗi khi thay đổi thông tin:", error);
                document.getElementById('message').style.display = "block";
                document.getElementById('message').textContent = "Đã xảy ra lỗi khi thay đổi thông tin.";
            });
    });
});

function decodeJwtPayload(token) {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace('-', '+').replace('_', '/');
    let decoded = atob(base64);
    return JSON.parse(decoded);
}
