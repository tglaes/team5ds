function changeStats() {
	$("canvas").css("margin-left", "30px");
}

function checkPassword() {

	var pwd = document.getElementById("pwd")
	var pwd_rep = document.getElementById("pwd-rep")
	var pwdDiv = document.getElementById("pwdDiv")
	var pwd_repDiv = document.getElementById("pwd_repDiv")

	if (pwd.value != pwd_rep.value) {
		pwdDiv.setAttribute("class", "form-group has-error")
		pwd_repDiv.setAttribute("class", "form-group has-error")
	} else {
		if (pwd) {
			pwdDiv.setAttribute("class", "form-group")
			pwd_repDiv.setAttribute("class", "form-group")
		} else {
			pwdDiv.setAttribute("class", "form-group has-error")
			pwd_repDiv.setAttribute("class", "form-group has-error")
		}
	}
}