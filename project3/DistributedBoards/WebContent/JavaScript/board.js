function editPostModal(postID, postContent) {

	// Einfügen des Posttextes in das Modal
	var modalTextArea = document.getElementById("editPostModalText")
	modalTextArea.innerHTML = postContent

	// Die ID des Posts in das action Attribute der Form einfügen.
	var editModalForm = document.getElementById("editPostModalForm")
	var actionAtt = editModalForm.getAttribute("action")
	var n = actionAtt.lastIndexOf("=")
	actionAtt = actionAtt.substring(0, n + 1)
	actionAtt += postID
	editModalForm.setAttribute("action", actionAtt)

	// Passe die Url des Onclick Attributes für den ausgewählten Post an.
	var deleteButton = document.getElementById("editPostDeleteButton")
	var onclickAtt = deleteButton.getAttribute("onclick")
	var n = onclickAtt.lastIndexOf("=")
	onclickAtt = onclickAtt.substring(0, n + 1)
	onclickAtt += postID + "'"
	deleteButton.setAttribute("onclick", onclickAtt)
}

function editCommentModal(postID) {

	// Passe action Attribute des Modals an
	var commentModalForm = document.getElementById("commentModalForm")
	var actionAtt = commentModalForm.getAttribute("action")
	var n = actionAtt.lastIndexOf("=")
	actionAtt = actionAtt.substring(0, n + 1)
	actionAtt += postID
	commentModalForm.setAttribute("action", actionAtt)
}

function editPushPostModal(postID) {
	var pushButton = document.getElementById("pushModalPush")
	var onclickAtt = pushButton.getAttribute("onclick")
	var n = onclickAtt.lastIndexOf("=")
	onclickAtt = onclickAtt.substring(0, n + 1)
	pushButton.setAttribute("onclick", onclickAtt + postID + "'")

	var dismissButton = document.getElementById("pushModalDismiss")
	var onclickAtt2 = dismissButton.getAttribute("onclick")
	var n2 = onclickAtt2.lastIndexOf("=")
	onclickAtt2 = onclickAtt2.substring(0, n2 + 1)
	dismissButton.setAttribute("onclick", onclickAtt2 + postID + "'")
}
