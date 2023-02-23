// $(document).ready(function () {//Khi trang được load lên thì nhảy vào hàm document.ready
//     //Khai báo các event của thẻ html trong đây
//     $("#logoutLink").on("click", function (e) {
//         e.preventDefault();
//         document.logoutForm.submit();
//     });
//     customizeDropDownMenu()
// });

// function customizeDropDownMenu() {
//     $(".navbar .dropdown").hover(
//         function () {
//             $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
//         },
//         function () {
//             $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
//         }
//     );
//     $(".dropdown > a").click(function () {
//         location.href = this.href;
//     });
// }

$(document).ready(function () {
    $("#logoutLink").on("click", function (e) {
        e.preventDefault(); // preventDefault -> ngan nhay toi Controller, hien len popup
        document.logoutForm.submit();
    })
    customizeDropDownMenu();
})

function customizeDropDownMenu() {
    $(".navbar .dropdown").hover(
        function () {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
        },
        function () {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
        }
    )

    $(".dropdown > a").click(function () {
        location.href = this.href;
    })
}