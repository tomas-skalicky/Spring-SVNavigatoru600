/**
 * Generates a new password with the specified length.
 * 
 * @param length
 *            The length of the resulting password.
 * @param special
 *            If <code>true</code>, the special characters can be used in the
 *            resulting password. The parameter is optional.
 * @returns The generated password.
 * @see The implementation comes from the URI
 *      http://jquery-howto.blogspot.com/2009/10/javascript-jquery-password-generator.html
 */
function generatePassword(length, special) {
	var iteration = 0;
	var password = "";
	var randomNumber;

	// The undefined property indicates that the variable "special" has not been
	// assigned a value.
	if (special == undefined) {
		special = false;
	}

	while (iteration < length) {
		randomNumber = (Math.floor((Math.random() * 100)) % 94) + 33;
		if (!special) {
			if ((randomNumber >= 33) && (randomNumber <= 47)) {
				continue;
			}
			if ((randomNumber >= 58) && (randomNumber <= 64)) {
				continue;
			}
			if ((randomNumber >= 91) && (randomNumber <= 96)) {
				continue;
			}
			if ((randomNumber >= 123) && (randomNumber <= 126)) {
				continue;
			}
		}
		++iteration;
		password += String.fromCharCode(randomNumber);
	}
	return password;
}

/**
 * Synchronizes the <code>checked</code> properties of roles checkboxes with
 * the given IDs.
 * 
 * @param svCheckboxId
 *            ID of a checkbox which says whether the user has the role
 *            ROLE_MEMBER_OF_SV, or not.
 * @param boardCheckboxId
 *            ID of a checkbox which says whether the user has the role
 *            ROLE_MEMBER_OF_BOARD, or not.
 * @param changedCheckboxId
 *            ID of a checkbox which has been changed last time.
 */
function syncRoleCheckboxes(svCheckboxId, boardCheckboxId, changedCheckboxId) {
	if (changedCheckboxId == svCheckboxId) {
		if (document.getElementById(svCheckboxId).checked == false) {
			// The checkbox connected with the role ROLE_MEMBER_OF_SV has been
			// unchecked.
			document.getElementById(boardCheckboxId).checked = false;
		}
	} else if (changedCheckboxId == boardCheckboxId) {
		if (document.getElementById(boardCheckboxId).checked == true) {
			// The checkbox connected with the role ROLE_MEMBER_OF_BOARD has
			// been checked.
			document.getElementById(svCheckboxId).checked = true;
		}
	} else {
		throw "Unsupported checkbox";
	}
}

/**
 * Sets an action of the form with the given <code>formId</code> to the given
 * <code>destinationUrl</code>.
 */
function setFormAction(formId, destinationUrl) {
	document.getElementById(formId).setAttribute("action", destinationUrl);
}
