dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function () {
    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrands.change(function () {
        dropdownCategories.empty();

        getCategories();
    });

    getCategoriesForNewForm();

});

function getCategoriesForNewForm() {
    catIdField = $("categoryId");
    editMode = false;

    if (catIdField.length) {
        editMode = true;
    }

    if (!editMode) getCategories();
}

function getCategories() {
    brandId = dropdownBrands.val();//.val của thẻ <option> sẽ trả về value, không phải name <option th:value="${brand.id}">[[${brand.name}]]</option>

    url = brandModuleURL + "/" + brandId + "/categories";//truyền xuống RestController hoặc Controller theo đg dẫn url: /brands/{id}/categories

    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, category) {
            // Method 1 : $.each(responseJson, function (index, category) { }
            // Method 2 : $(response).each(function (index, category) {
            //category.id and category.name which id and name are properties of CategoryDTO, not Category
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        });
    });
}