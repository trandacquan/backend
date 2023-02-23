//file này có thể sử dụng trực tiếp những biến, thẻ bên user_form.html
$(document).ready(function () {
	$("#buttonCancel").on("click", function () {
		window.location = moduleURL;//moduleURL = "[[@{/users}]]" -->khai báo bên user_form.html
	});
	$("#buttonCancelCategories").on("click", function () {
		window.location = categoryModuleURL;
	});
	$('#fileImage').charge(function () {
		if (!checkFileSize(this)) {
			return;
		}
		showImageThumbmail(this);
	})
});

function checkFileSize(fileInput) {
	fileSize = fileInput.files[0].size;

	if (fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("you must choose an image less than " + MAX_FILE_SIZE + " bytes!");
		fileInput.reportvalidity();
		return false;
	} else {
		fileInput.setCustomValidity("");
		return true;
	}
}

function showImageThumbmail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.unload = function (e) {
		$("#thumbnail").attr("scr", e.target.result);
	};
	reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();//dùng để mở modal
}

function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}

