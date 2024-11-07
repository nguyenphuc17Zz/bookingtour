 document.addEventListener("DOMContentLoaded", function () {
        const profileDropdown = document.getElementById("profileDropdown");
        const dropdownMenu = profileDropdown.nextElementSibling;

        profileDropdown.addEventListener("click", function (event) {
            event.preventDefault();
            dropdownMenu.classList.toggle("show");
        });

        // Close dropdown when clicking outside
        document.addEventListener("click", function (event) {
            if (!profileDropdown.contains(event.target) && !dropdownMenu.contains(event.target)) {
                dropdownMenu.classList.remove("show");
            }
        });
    });