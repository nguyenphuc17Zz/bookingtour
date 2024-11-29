const token = localStorage.getItem("jwtToken");
if (!token) {
    alert('Bạn cần đăng nhập trước');
    window.location.href = `/login`;
}

function formatCurrencyVND(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

const numGuestsInput = document.getElementById('num_guests');
const totalPriceSpan = document.querySelector('.total_price');

// Gán giá trị mặc định cho tổng tiền
const pricePerGuest = parseInt(numGuestsInput.dataset.price, 10);
totalPriceSpan.textContent = formatCurrencyVND(pricePerGuest);

numGuestsInput.addEventListener('input', function () {
    const numGuests = parseInt(numGuestsInput.value, 10) || 1;
    const totalPrice = pricePerGuest * numGuests;
    totalPriceSpan.textContent = formatCurrencyVND(totalPrice);
});
function decodeJwtPayload(token) {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace('-', '+').replace('_', '/');
    let decoded = atob(base64);
    return JSON.parse(decoded);
}
document.getElementById("submit-btn").addEventListener('click',()=>{
    const numGuests = parseInt(numGuestsInput.value, 10) || 1;
    const pricePerGuest = parseInt(numGuestsInput.dataset.price, 10);
    const totalPrice = pricePerGuest * numGuests;
    let payment = parseInt(document.getElementById("select1").value);

    let specialRequest = document.getElementById("message").value;
    // Lấy URL hiện tại
    const url = window.location.href;

    const urlParts = url.split('/');
    const tourId = urlParts[urlParts.length - 1];
    let payload = decodeJwtPayload(token);
    let customerId = payload.id;
    let date = new Date();
    let status = 1;
    if(payment===0){
    fetch("/booking/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({customer_id:customerId, tour_id:tourId,booking_date:date, num_guests:numGuests, total_price: totalPrice,payment_status:payment, special_request:  specialRequest, booking_status:1})
        }).then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Book tour thành công');
                    window.location.href=`/tour`;
                } else {
                    document.getElementById('message').style.display = "block";
                    document.getElementById('message').textContent = data.message;
                }
            }).catch(error => {
                console.error("Lỗi khi Book tour:", error);
                document.getElementById('message').style.display = "block";
                document.getElementById('message').textContent = "Đã xảy ra lỗi khi Book tour.";
            })
    }else{
  fetch("/create_payment", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                customer_id: customerId,      // ID khách hàng
                tour_id: tourId,              // ID tour
                booking_date: date,           // Ngày đặt
                num_guests: numGuests,        // Số khách
                total_price: totalPrice,      // Tổng giá
                payment_status: payment,      // Trạng thái thanh toán
                special_request: specialRequest, // Yêu cầu đặc biệt
                booking_status: 1             // Trạng thái đặt (ví dụ: mặc định là 1)
            })
        })
            .then(response => response.text()) // Lấy response là chuỗi (URL thanh toán từ server)
            .then(paymentUrl => {
                if (paymentUrl) {
                    window.location.href = paymentUrl; // Điều hướng tới URL thanh toán trả về từ server
                } else {
                    document.getElementById('message').style.display = "block";
                    document.getElementById('message').textContent = "Không thể tạo thanh toán.";
                }
            })
            .catch(error => {
                console.error("Lỗi khi tạo thanh toán:", error);
                document.getElementById('message').style.display = "block";
                document.getElementById('message').textContent = "Đã xảy ra lỗi khi tạo thanh toán.";
            });
    }

});
