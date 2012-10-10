/**
 * Constants and global variables.
 * ATTENTION: I cannot use the "const" key word since otherwise, the IE ends up with an error.
 */
var $successAddMessageElement = $("#successAddMessage");
var $successDeleteMessageElement = $("#successDeleteMessage");
var $successEditMessageElement = $("#successEditMessage");
var $fatalErrorsElement = $("#fatalErrors");
var $newNewsLinkElement = $("#newNewsLink");
var $newsListElement = $("#newsList");

var $newsFormElement = $("#newEditNewsCommand");
var $inputErrorsElement = $("#inputErrors");
var $newsIdElement = $("#news\\.id");
var newsTitleFieldName = "news.title";
var $newsTitleElement = $("#news\\.title");
var newsTextFieldName = "news.text";
var $newsTextElement = $("#news\\.text");
var $formSubmitButton = $("#newsForm\\.submitButton");

/***********************************************************************************************************************
 * DISPLAY of the CREATION form
 **********************************************************************************************************************/
/**
 * Sets up and shows the form for creation of new news.
 */
function goToNewNewsForm() {
	var targetUrl = $("#newNewsLink a").attr("href");

	// $.get() does not work. I do not know why.
	$.ajax({
		type : "GET",
		url : targetUrl,
		data : {},
		success : function(response) {
			setDefaultsToInputFields();
			setFormAction(response.formAction);
			setTitleOfSubmitButton(response.localizedTitleOfSubmit);
			showNewsForm();
		},
		error : function(e) {
			// Debug method "toSource()"
			alert("Error: " + e.toSource());
		}
	});
}

/***********************************************************************************************************************
 * DISPLAY of the EDIT form
 **********************************************************************************************************************/
/**
 * Sets up and shows the form for modification of existing news.
 */
function goToEditNewsForm(newsId, urlBeginning) {
	var targetUrl = urlBeginning + newsId + "/";

	$.ajax({
		type : "GET",
		url : targetUrl,
		data : {},
		success : function(response) {
			setInputFields(response.news);
			setFormAction(response.formAction);
			setTitleOfSubmitButton(response.localizedTitleOfSubmit);
			showNewsForm();
		},
		error : function(e) {
			alert("Error: " + e.toSource());
		}
	});
}

function setDefaultsToInputFields() {
	$newsIdElement.attr("value", 0);
	$newsTitleElement.attr("value", "");
	$newsTextElement.attr("value", "");
}

function setInputFields(news) {
	$newsIdElement.attr("value", news.id);
	$newsTitleElement.attr("value", news.title);
	$newsTextElement.attr("value", news.text);
}

function setFormAction(action) {
	$newsFormElement.attr("action", action);
}

function setTitleOfSubmitButton(title) {
	$formSubmitButton.attr("value", title);
}

function showNewsForm() {
	hideAllMessages();
	$newNewsLinkElement.hide();
	$newsListElement.hide();

	setFormDefaults();

	$newsFormElement.show("fast");

	// Focuses the first input. Can be called only on visible elements.
	$newsTitleElement.focus();
}

/**
 * Hides all success and fail messages.
 */
function hideAllMessages() {
	$successAddMessageElement.hide();
	$successDeleteMessageElement.hide();
	$successEditMessageElement.hide();
	$fatalErrorsElement.hide();
	$inputErrorsElement.hide();
}

function setFormDefaults() {
	$inputErrorsElement.hide();
	$newsTitleElement.removeClass("errorInput");
	$newsTextElement.removeClass("errorInput");
}

/***********************************************************************************************************************
 * CREATION and EDITING of news
 **********************************************************************************************************************/
// Attaches a submit handler to the form.
$(document).ready(function() {
$("#newEditNewsCommand").submit(function(event) {

	// Prevents the form from the normal submit.
	event.preventDefault();

	// Gets some values from HTML elements on the page.
	var $form = $(this);
	var $action = $form.attr("action");

	// Sends the data using post and puts the results in appropriate HTML elements.
	$.ajax({
		type : "POST",
		url : $action,
		data : {
			"news.id" : $newsIdElement.val(),
			"news.title" : $newsTitleElement.val(),
			"news.text" : $newsTextElement.val()
		},
		success : function(response) {
			if (response.successful) {
				// The news has been successfully created or edited.
				treatNewEditSuccess(response);
			} else {
				// The creation or editing of news has not been successful (e.g. because of some invalid data).
				treatNewEditFail(response);
			};
		},
		error : function(e) {
			alert("Error: " + e.toSource());
		}
	});
});});

/**
 * Manages the given response when the creation or editing of news has been successful.
 */
function treatNewEditSuccess(response) {
	var news = response.command.news;
	var isNew = (news.creationTime == news.lastSaveTime);
	if (isNew) {
		// Create
		treatNewSuccess(response);
	} else {
		// Edit
		treatEditSuccess(response);
	};
}

/**
 * Manages the given response when the creation of news has been successful.
 */
function treatNewSuccess(response) {
	$newsFormElement.hide();

	insertNewNewsToList(response);
	//$successAddMessageElement.show("fast");
	$newNewsLinkElement.show("fast");
	$newsListElement.show("fast");
}

/**
 * Inserts the new news on the top of the list of news and shows it.
 */
function insertNewNewsToList(response) {
	var news = response.command.news;
	var newsTemplateId = "template";
	var $newsTemplateElement = $("#post_" + newsTemplateId);

	// Creates a root element for the new news.
	var newNewsId = news.id;
	var newNewsDivId = "post_" + newNewsId;
	var $newNewsElement = $("<div id='" + newNewsDivId + "' class='post'></div>");
	$newNewsElement.insertAfter($newsTemplateElement);
	
	// Fills out placeholders.
	$newNewsElement.append($newsTemplateElement.children(".post-header").clone());
	$newNewsElement.find("h3").append(news.title);
	$newNewsElement.find(".month").append(response.localizedMonth);
	$newNewsElement.find(".day").append(response.localizedDay);
	$newNewsElement.find("#edit\\[" + newsTemplateId + "\\]").attr("id", "edit[" + newNewsId + "]");
	$newNewsElement.find("#edit\\[" + newNewsId + "\\]").on("click", function() {
		goToEditNewsForm(newNewsId, response.editUrlBeginning);
		return false;
	});
	$newNewsElement.find("#delete\\[" + newsTemplateId + "\\]").attr("id", "delete[" + newNewsId + "]");
	$newNewsElement.find("#delete\\[" + newNewsId + "\\]").on("click", function() {
		deleteNews(newNewsId, response.localizedDeleteQuestion, response.editUrlBeginning);
		return false;
	});
	$newNewsElement.append($newsTemplateElement.children(".post-content").clone());
	$newNewsElement.children(".post-content").append(news.text);
	
	$(document).ready(function() {
		addPostHeaderAnimatedButtons();
	});
}

/**
 * Manages the given response when the editing of news has been successful.
 */
function treatEditSuccess(response) {
	setFormDefaults();

	$successEditMessageElement.show("fast");
}

/**
 * Manages the given response when the creation or editing has failed.
 */
function treatNewEditFail(response) {
	$successEditMessageElement.hide();

	printOutAllNewEditErrors(response);
	highlightFieldErrorInputs(response.fieldErrors);
}

/**
 * Prints out both global and field errors to one particular HTML element.
 */
function printOutAllNewEditErrors(response) {
	var errorHtml = getAllErrorsPrintedToHtml(response);
	$inputErrorsElement.html(errorHtml);
	$inputErrorsElement.show("fast");
}

/**
 * Gets an HTML which contains both global and field errors.
 */
function getAllErrorsPrintedToHtml(response) {
	var errorHtml = getErrorsPrintedToHtml(response.globalErrors);
	errorHtml += getErrorsPrintedToHtml(response.fieldErrors);
	return errorHtml;
}

function getErrorsPrintedToHtml(errors) {
	if (!errors) {
		return "";
	}
	var errorHtml = "";
	for ( var errI = 0; errI < errors.length; ++errI) {
		if (errorHtml != "") {
			// Skips the first error.
			errorHtml += "<br />";
		}
		errorHtml += errors[errI].code;
	}
	return errorHtml;
}

function highlightFieldErrorInputs(errors) {
	highlightFieldErrorInput(newsTitleFieldName, $newsTitleElement, errors);
	highlightFieldErrorInput(newsTextFieldName, $newsTextElement, errors);
}

function highlightFieldErrorInput(field, fieldElement, errors) {
	// If there is no error concerning the current field, we must remove the highlighting.
	fieldElement.removeClass("errorInput");

	for ( var errI = 0; errI < errors.length; ++errI) {
		if (field == errors[errI].field) {
			fieldElement.addClass("errorInput");
		};
	};
}

/***********************************************************************************************************************
 * DELETION of news
 **********************************************************************************************************************/
/**
 * Invoked by the handler of the <b>onclick</b> event of the JSP page "list-news.jsp".
 */
function deleteNews(newsId, question, urlBeginning) {
	if (!confirm(question)) {
		// The user has not agreed with deletion of the news.
		return;
	}

	var targetUrl = urlBeginning + newsId + "/smazat/";

	// Send the data using post and put the results in appropriate HTML elements.
	$.ajax({
		type : "GET",
		url : targetUrl,
		data : {},
		success : function(response) {
			if (response.successful) {
				treatDeleteSuccess(response, newsId);
			} else {
				treatDeleteFail(response);
			};
		},
		error : function(e) {
			alert("Error: " + e.toSource());
		}
	});
}

/**
 * Manages the given response when the deletion of news has been successful.
 */
function treatDeleteSuccess(response, newsId) {
	hideAllMessages();

	//$successDeleteMessageElement.show("fast");
	removeNewsFromList(newsId);
}

/**
 * Hides news with the given ID.
 */
function removeNewsFromList(newsId) {
	$("#post_" + newsId).hide("fast");
}

/**
 * Manages the given response when the deletion of news has failed.
 */
function treatDeleteFail(response) {
	hideAllMessages();

	$fatalErrorsElement.html(response.error);
	$fatalErrorsElement.show("fast");
};
