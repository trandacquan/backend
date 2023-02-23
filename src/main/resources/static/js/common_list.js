moduleURL = "[[@{/users}]]";
//moduleURL = "@{/users/page/1?sortField=firstName&sortDir=asc}"
//url = "[[@{/users/check_email}]]";

function showDeleteConfirmModal(link, entityName) {
    entityId = link.attr("entityId");

    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("are you sure you want to delete this" + entityName + " ID " + entityId + "?");
    $("#confirmModal").modal();
}

function clearFilter() {
    window.location = moduleURL;
}