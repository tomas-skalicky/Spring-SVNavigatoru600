// default template
var DEFAULTTEMPLATE = {
	"months": ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
	"weekDays": ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
	"weekStart": 0, // the first day of week: 0-Su or 1-Mo.
	"yearsScroll": true, // if true, year scroller will be shown. Otherwise, will not.
	"yearEdge": 70, // only used if the specified year has just two digits: if the year is less than "yearEdge", 20xx is added. Othewise, 19xx is added.
};



/**
 * Returns an element with the specified ID.
 *
 * @param	id	ID of element.
 * @return		element with the specified ID.
 */
function GetElement(id) {
	return document.getElementById(id);
}

/**
 * Returns a value (in pixels) of the specified CSS property of element with the specified ID.
 *
 * @param	elementId	ID of element.
 * @param	property	CSS property
 * @return				value (in pixels) of the specified CSS property of element with the specified ID.
 */
function GetCSSPropertyInPx(elementId, property) {
	var e = false;
	
	if (e = GetElement(elementId)) {
		var num = 0;
		if (num = parseInt(e.style[property], 10)) { // 10 ... decimal system
			return num;
		}
	}
	
	return 0; // failure
}



/**
 * Parses the specified date string. Returns an object of Date.
 */
function CalendarParseDateStr(dateStr) {
	var dateRegExp = new RegExp("^\\s*(\\d{2,4})\\-(\\d{1,2})\\-(\\d{1,2})\\s*$");
	if (dateRegExp.test(dateStr) === false) {
		return alert("Invalid date: '" + dateStr + "'.\nAccepted format is yyyy-mm-dd.");
	}
	
	var yearNum = new Number(RegExp.$1);
	var monthNum = new Number(RegExp.$2);
	var dayNum = new Number(RegExp.$3);
	
	if (yearNum < 100) {
		if (yearNum < this.template.yearEdge) {
			yearNum += 2000;
		} else {
			yearNum += 1900;
		}
	}
	if (monthNum < 1 || monthNum > 12) {
		return alert("Invalid month value: '" + monthNum + "'.\nAllowed range is 1 - 12.");
	}
	if (dayNum < 1 || dayNum > GetDayCount(yearNum, monthNum)) {
		return alert("Invalid day of month value: '" + dayNum + "'.\nAllowed range for selected month is 1 - " + GetDayCount(yearNum, monthNum) + ".");
	}
	
	return new Date(yearNum, monthNum - 1, dayNum);
}

/**
 * Returns the number of days in the specified year and month-1.
 */
function GetDayCount(yearNum, monthNum) {
	return (new Date(yearNum, monthNum, 0)).getDate();
}

/**
 * Returns the specified date in database format.
 */
function CalendarGetDateInFormat(date) {
	var yearStr = date.getFullYear();
	var monthStr = "";
	var dayStr = "";
	
	if (date.getMonth() < 9) {
		monthStr += "0";
	}
	monthStr += (date.getMonth() + 1);
	
	if (date.getDate() < 10) {
		dayStr += "0";
	}
	dayStr += date.getDate();
	
	return (yearStr + "-" + monthStr + "-" + dayStr);
}

/**
 * Creates a calendar.
 */
function Calendar(config, template) {
	// Applies the default template if not specified.
	if (!template) {
		template = DEFAULTTEMPLATE;
	}
	
	// Initializes global collections if necessary.
	if (!window.calendarsAssoc) {
		window.calendarsAssoc = [];
	}
	if (!window.calendars) {
		window.calendars = [];
	}
	
	// Add the new calendar in collections.
	if (config.id) {
		this.id = config.id;
	} else {
		this.id = calendarsAssoc.length;
	}
	window.calendarsAssoc[this.id] = this;
	window.calendars[window.calendars.length] = this;
	
	// Assigns methods.
	this.Show = CalendarShow;
	this.Hide = CalendarHide;
	this.Toggle = CalendarToggle;
	this.Update = CalendarUpdate;
	this.RelatedDate = CalendarRelatedDate;
	this.ParseDateStr = CalendarParseDateStr;
	this.GetDateInFormat = CalendarGetDateInFormat;
	
	// Creates calendar icon.
	this.iconEId = "calendarIcon_" + this.id;
	this.iconE = GetElement(this.iconEId);
	if (!this.iconE) {
		document.write("<img src='" + config.imgPath + "calendar.png' id='" + this.iconEId + "' onclick=\"calendarsAssoc[\'" + this.id + "\'].Toggle()\" class='calendarIcon' title='Open calendar' alt='Open calendar' />");
		this.iconE = GetElement(this.iconEId);
	}
	
	// Saves received parameters.
	this.config = config;
	this.template = template;
}

/**
 * Shows a calendar.
 */
function CalendarShow() {
	// Finds input field.
	if (!this.config.controlName) {
		throw("Calendar: control name is not specified.");
	}
	if (this.config.formName) {
		var formE = document.forms[this.config.formName];
		if (!formE) {
			throw("Calendar: form '" + this.config.formName + "' can not be found.");
		}
		this.inputE = formE.elements[this.config.controlName];
	} else {
		this.inputE = GetElement(this.config.controlName);
	}
	
	if (!this.inputE || !this.inputE.tagName || this.inputE.tagName != "INPUT") {
		var str = "";
		if (this.config.formName) {
			str = "form '" + this.config.controlName + "'";
		} else {
			str = "this document";
		}
		
		throw("Calendar: element '" + this.config.controlName + "' does not exist in " + str + ".");
	}
	
	// Dynamically creates HTML elements if needed.
	this.divE = GetElement("calendar");
	if (!this.divE) {
		this.divE = document.createElement("div");
		this.divE.id = "calendar";
		document.body.appendChild(this.divE);
	}
	
	// Hides all calendars.
	CalendarHideAll();
	
	// Generates HTML and shows a calendar.
	this.iconE = GetElement(this.iconEId);
	if (!this.Update()) {
		return;
	}
	
	this.divE.style.visibility = "visible";
	
	// Changes icon and status.
	this.iconE.src = this.config.imgPath + "no_calendar.png";
	this.iconE.title = "Close calendar";
	this.visible = true;
}

/**
 * Hides a calendar.
 */
function CalendarHide(timestamp) {
	if (timestamp) {
		this.inputE.value = this.GetDateInFormat(new Date(timestamp));
	}
	
	// no action if not visible.
	if (!this.visible) {
		return;
	}
	
	// Hides elements.
	this.divE.style.visibility = "hidden";
	
	// Changes icon and status.
	this.iconE = GetElement(this.iconEId);
	this.iconE.src = this.config.imgPath + "calendar.png";
	this.iconE.title = "Open calendar";
	this.visible = false;
}

/**
 * Hides all calendars.
 */
function CalendarHideAll() {
    if (window.calendars) {
    	for (var i = 0; i < window.calendars.length; ++i) {
    		window.calendars[i].Hide();
    	}
    }
}

/**
 * Toggles visibility of a calendar.
 */
function CalendarToggle() {
	if (this.visible) {
		this.Hide();
	} else {
		this.Show();
	}
}

/**
 * Updates a calendar.
 */
function CalendarUpdate(date) {
	var today = false;
	if (this.config.today) {
		today = this.ParseDateStr(this.config.today);
	} else {
		today = new Date();
	}
	
	var selectedDate = false;
	if (this.inputE.value === "") {
		if (this.config.selectedDate) {
			selectedDate = this.ParseDateStr(this.config.selectedDate);
		} else {
			selectedDate = today;
		}
	} else {
		selectedDate = this.ParseDateStr(this.inputE.value);
	}
	
	// Figures out date to display.
	if (!date) {
		// selected by default.
		date = selectedDate;
	} else if (typeof(date) == "number") {
		// Gets from number.
		date = new Date(date);
	} else if (typeof(date) == "string") {
		// Parses from string.
		this.ParseDateStr(date);
	}
	
	if (!date) {
		return false;
	}
	
	
	
	// navigation bar.
	var navHtml = "<div class='navigation'>";
	
	if (this.template.yearsScroll) {
		navHtml += "<div id='" + this.id + "_nav_prevYear'" + this.RelatedDate(date, -1, "y") + " class='scrollButton left centerAlign middleVAlign pointer' title='Previous Year'>" +
				"<img src='" + this.config.imgPath + "prev_year.png' width='9' height='8' title='Previous Year' alt='' />" +
				"</div>";
	}
	
	navHtml += "<div id='" + this.id + "_nav_prevMonth'" + this.RelatedDate(date, -1) + " class='scrollButton left centerAlign middleVAlign pointer' title='Previous Month'>" +
			"<img src='" + this.config.imgPath + "prev_month.png' width='4' height='8' title='Previous Month' alt='' />" +
			"</div>" +
			"<div id='" + this.id + "_nav_title' class='left centerAlign middleVAlign'>" +
			this.template.months[date.getMonth()] + " " + date.getFullYear() +
			"</div>" +
			"<div id='" + this.id + "_nav_nextMonth'" + this.RelatedDate(date, 1) + " class='scrollButton left centerAlign middleVAlign pointer' title='Next Month'>" +
			"<img src='" + this.config.imgPath + "next_month.png' width='4' height='8' title='Next Month' alt='' />" +
			"</div>";
	
	if (this.template.yearsScroll) {
		navHtml += "<div id='" + this.id + "_nav_nextYear'" + this.RelatedDate(date, 1, "y") + " class='scrollButton left centerAlign middleVAlign pointer' title='Next Year'>" +
				"<img src='" + this.config.imgPath + "next_year.png' width='9' height='8' title='Next Year' alt='' />" +
				"</div>";
	}
	
	navHtml += "<div class='clear'></div>" +
			"</div>";
	
	
	
	var weekDaysCount = this.template.weekDays.length;
	
	var classes;
	var tableHtml = "<table>";
	
	tableHtml += "<tr>";
	// Prints titles of days of the week.
	for (var i = 0; i < weekDaysCount; ++i) {
		var weekDayName = this.template.weekDays[(this.template.weekStart + i) % weekDaysCount];
		classes = new Array();
		
		if (i === 0) {
			classes[classes.length] = "first";
		} else if (i == weekDaysCount - 1) {
			classes[classes.length] = "last";
		}
		if (weekDayName == this.template.weekDays[0]) {
			classes[classes.length] = "sunday";
		}
		
		tableHtml += "<th";
		if (classes.length) {
			tableHtml += " class='" + classes.join(" ") + "'";
		}
		tableHtml += ">" + weekDayName + "</th>";
	}
	tableHtml += "</tr>";
	
	// The first date to display.
	var firstDate = new Date(date);
	firstDate.setDate(1);
	// "weekDaysCount +" is necessary since the modulo operation does not work for negative numbers.
	firstDate.setDate(1 - (weekDaysCount + firstDate.getDay() - this.template.weekStart) % weekDaysCount);
	
	// Prints calendar table.
	var currentDate = new Date(firstDate);
	while (currentDate.getMonth() == date.getMonth() || currentDate.getMonth() == firstDate.getMonth()) {
		
		// Prints week row.
		tableHtml += "<tr>";
		
		for (var weekDay = 0; weekDay < weekDaysCount; ++weekDay) {
			classes = new Array();
			
			// other month.
			if (currentDate.getMonth() != date.getMonth()) {
				classes[classes.length] = "otherMonth";
			}
			// weekend.
			if (currentDate.getDay() === 0 || currentDate.getDay() == 6) {
				classes[classes.length] = "weekend";
			}
			// today.
			if (currentDate.getFullYear() == today.getFullYear() &&
					currentDate.getMonth() == today.getMonth() &&
					currentDate.getDate() == today.getDate()) {
				classes[classes.length] = "today";
			}
			// selected date.
			if (currentDate.getFullYear() == selectedDate.getFullYear() &&
					currentDate.getMonth() == selectedDate.getMonth() &&
					currentDate.getDate() == selectedDate.getDate()) {
				classes[classes.length] = "selectedDate";
			}
			
			tableHtml += "<td onclick=\"calendarsAssoc[\'" + this.id + "\'].Hide(" + currentDate.valueOf() + ")\"";
			if (classes.length) {
				tableHtml += " class='" + classes.join(" ") + "'";
			}
			tableHtml += ">" + currentDate.getDate() + "</td>";
			
			currentDate.setDate(currentDate.getDate() + 1);
		}
		
		tableHtml += "</tr>";
	}
	tableHtml += "</table>";
	
	
	
	// Sets inner HTML.
	this.divE.innerHTML = "" +
			"<div class='calendar left'>" +
			navHtml +
			"<div id='" + this.id + "_inner' class='inner'>" +
			tableHtml +
			"</div>" +
			"</div>";
	
	
	
	// Modifies navigation bar.
	var navButtonWidth = 15; // px
	var navTitlePaddingVertical = 3; // px
	if (this.template.yearsScroll) {
		if (e = GetElement(this.id + "_nav_prevYear")) {
			e.style.width = navButtonWidth + "px";
		}
		if (e = GetElement(this.id + "_nav_nextYear")) {
			e.style.width = navButtonWidth + "px";
		}
		if (e = GetElement(this.id + "_nav_prevMonth")) {
			e.style.width = navButtonWidth + "px";
		}
		if (e = GetElement(this.id + "_nav_nextMonth")) {
			e.style.width = navButtonWidth + "px";
		}
	} else {
		if (e = GetElement(this.id + "_nav_prevMonth")) {
			e.style.width = (2 * navButtonWidth) + "px";
		}
		if (e = GetElement(this.id + "_nav_nextMonth")) {
			e.style.width = (2 * navButtonWidth) + "px";
		}
	}
	
	// Modifies navigation title.
	var navTitleWidth = GetElement(this.id + "_inner").offsetWidth -
			(2 * GetCSSPropertyInPx(this.id + "_inner", "border") +
			GetCSSPropertyInPx(this.id + "_nav_prevMonth", "width") +
			GetCSSPropertyInPx(this.id + "_nav_nextMonth", "width"));
	if (this.template.yearsScroll) {
		navTitleWidth -= (GetCSSPropertyInPx(this.id + "_nav_prevYear", "width") +
				GetCSSPropertyInPx(this.id + "_nav_nextYear", "width"));
	}
	var navTitleOffsetHeight = 0;
	if (e = GetElement(this.id + "_nav_title")) {
		e.style.width = navTitleWidth + "px";
		e.style.paddingTop = navTitlePaddingVertical + "px";
		e.style.paddingBottom = navTitlePaddingVertical + "px";
		navTitleOffsetHeight = e.offsetHeight;
	}
	
	// Modifies navigation buttons.
	var paddingBottom = 0;
	if (this.template.yearsScroll) {
		if (e = GetElement(this.id + "_nav_prevYear")) {
			paddingBottom = (navTitleOffsetHeight - e.offsetHeight) / 2;
			e.style.paddingTop = (navTitleOffsetHeight - e.offsetHeight - paddingBottom) + "px";
			e.style.paddingBottom = paddingBottom + "px";
		}
		if (e = GetElement(this.id + "_nav_nextYear")) {
			paddingBottom = (navTitleOffsetHeight - e.offsetHeight) / 2;
			e.style.paddingTop = (navTitleOffsetHeight - e.offsetHeight - paddingBottom) + "px";
			e.style.paddingBottom = paddingBottom + "px";
		}
	}
	if (e = GetElement(this.id + "_nav_prevMonth")) {
		paddingBottom = (navTitleOffsetHeight - e.offsetHeight) / 2;
		e.style.paddingTop = (navTitleOffsetHeight - e.offsetHeight - paddingBottom) + "px";
		e.style.paddingBottom = paddingBottom + "px";
	}
	if (e = GetElement(this.id + "_nav_nextMonth")) {
		paddingBottom = (navTitleOffsetHeight - e.offsetHeight) / 2;
		e.style.paddingTop = (navTitleOffsetHeight - e.offsetHeight - paddingBottom) + "px";
		e.style.paddingBottom = paddingBottom + "px";
	}
	
	
	
	// Saves dimensions.
	var divEWidth = this.divE.offsetWidth;
	var divEHeight = this.divE.offsetHeight;
	
	
	
	// calendar shape.
	var shapeTRHeight = 7; // px
	var shapeBLWidth = 6; // px
	this.divE.innerHTML +=
		"<div class='left' style='height: " + divEHeight + "px; width: 7px;'>" +
			"<div class='left' style='background-image: url(" + this.config.imgPath + "shade_tr.png); height: " + shapeTRHeight + "px; width: 7px;'></div>" +
			"<div class='left' style='background-image: url(" + this.config.imgPath + "shade_mr.png); height: " + (divEHeight - shapeTRHeight) + "px; width: 7px;'></div>" +
		"</div>" +
		"<div class='clear'></div>" +
		"<div class='left' style='background-image: url(" + this.config.imgPath + "shade_bl.png); height: 7px; width: " + shapeBLWidth + "px;'></div>" +
		"<div class='left' style='background-image: url(" + this.config.imgPath + "shade_bm.png); height: 7px; width: " + (divEWidth - shapeBLWidth) + "px;'></div>" +
		"<div class='left' style='background-image: url(" + this.config.imgPath + "shade_br.png); height: 7px; width: 7px;'></div>" +
		"<div class='clear'></div>";
	
	
	
	// Updates position.
	var top = GetPosition(this.iconE, "Top", this.iconE.offsetHeight, this.divE.offsetHeight);
	var left = GetPosition(this.iconE, "Left") - divEWidth + this.iconE.offsetWidth;
	if (left < 0) {
		left = 0;
	}
	
	this.divE.style.left = left + "px";
	this.divE.style.top = top + "px";
	
	return true;
}

/**
 * Returns a position of the specified element in the specified direction.
 */
function GetPosition(elementRef, direction, iconDimension, calendarDimension) {
	var position = 0;
	var offset = 0;
	var element = elementRef;
	
	while (element) {
		offset = element["offset" + direction];
		position += offset;
		element = element.offsetParent;
	}
	
	if (iconDimension && calendarDimension) {
		position += iconDimension;
		
		if (position + calendarDimension > document.body.offsetHeight) {
			position -= (iconDimension + calendarDimension);
		}
	}
	
	return position;
}

/**
 * Adds code which shows related date.
 */
function CalendarRelatedDate(date, diff, units) {
	var result = new Date(date);
	
	if (units == "y") {
		result.setFullYear(date.getFullYear() + diff);
	} else {
		result.setMonth(date.getMonth() + diff);
	}
	
	if (result.getDate() != date.getDate()) {
		result.setDate(1);
	}
	
	return " onclick=\'calendarsAssoc[\"" + this.id + "\"].Update(" + result.valueOf() + ")\'";
}



/**
 * If the user scrolls window, all calendars disappear.
 */
// Firefox
if (window.addEventListener) {
	window.addEventListener("scroll", CalendarHideAll, false);
}
// IE5+
if (window.attachEvent) {
	window.attachEvent("onscroll", CalendarHideAll);
}