//Lưu ý: brandModuleURL phải khai báo trong file html bởi vì trong đó có khai báo thymeleaf
//Không nhúng thymeleaf vào file json

$(document).ready(function () {
    dropdownBrands.change(function () {
        dropdownCategories.empty();

        getCategories();
    });
});

function getCategories() {
    brandId = dropdownBrands.val();//.val của thẻ <option> sẽ trả về value, không phải name <option th:value="${brand.id}">[[${brand.name}]]</option>

    url = brandModuleURL + "/" + brandId + "/categories";//truyền xuống RestController hoặc Controller theo đg dẫn url: /brands/{id}/categories

    $.get(url, function (response) {
        $(response).each(function (index, category) {
            // Method 2 : $.each(responseJson, function (index, category) { }
            $("<option>").val(category.id).text(category.productName).appendTo(dropdownCategories);
        });
    });
}






