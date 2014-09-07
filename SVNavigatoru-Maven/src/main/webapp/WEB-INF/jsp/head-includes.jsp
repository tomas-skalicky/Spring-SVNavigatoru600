<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>

<%-- arclite --%>
<c:url var="arcliteHomeUrl" value="/arclite/" />
<link rel="stylesheet" type="text/css" href="<c:out value='${arcliteHomeUrl}' />style.css" media="all" />
<link rel="stylesheet" type="text/css" href="<c:out value='${arcliteHomeUrl}' />options/header-field.css" media="all" />
<link rel="stylesheet" type="text/css" href="<c:out value='${arcliteHomeUrl}' />options/content-grunge.css" media="all" />
<link rel="stylesheet" type="text/css" href="<c:out value='${arcliteHomeUrl}' />options/side-green.css" media="all" />
<link rel="stylesheet" type="text/css" href="<c:out value='${arcliteHomeUrl}' />options/side-merge.css" media="all" />
<!--[if lte IE 6]><link rel="stylesheet" type="text/css" href="<c:out value='${arcliteHomeUrl}' />ie6.css" media="all" /><![endif]-->

<%-- The jQuery library has to be before the arclite.js one. --%>
<script type="text/javascript" src="<c:url value='${myJsHome}' />jquery-1.7.1.js"></script>
<script type="text/javascript" src="<c:out value='${arcliteHomeUrl}' />js/arclite.js"></script>
<%-- /arclite --%>



<%-- Tigra calendar --%>
<c:url var="calendarHomeUrl" value="/tigra_calendar/" />
<script type="text/javascript" src="<c:out value='${calendarHomeUrl}' />calendar_cz.js"></script>
<link rel="stylesheet" type="text/css" href="<c:out value='${calendarHomeUrl}' />calendar.css" />
<%-- /Tigra calendar --%>



<%-- TinyMCE --%>
<c:url var="tinymceHomeUrl" value="/tinymce/jscripts/tiny_mce/" />
<script type="text/javascript" src="<c:out value='${tinymceHomeUrl}' />jquery.tinymce.js"></script>
<script type="text/javascript">
	$().ready(function() {
		<%-- "standaloneWysiwygEditor" is CSS class. --%>
		$('textarea.standaloneWysiwygEditor').tinymce({
			
			<%-- The original setting is in the file /tinymce/examples/index.html. --%>
			
			// Location of TinyMCE script
			script_url : '<c:out value="${tinymceHomeUrl}" />tiny_mce.js',

			// General options
			theme : "advanced",
			plugins : "autolink,lists,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist",

			// Theme options
			// See documentation: http://www.tinymce.com/wiki.php/Configuration:theme_advanced_buttons_1_n
			theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,formatselect,fontselect,fontsizeselect",
			theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
			theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,advhr,nonbreaking,|,print,|,fullscreen",
			theme_advanced_buttons4 : "",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "bottom",
			theme_advanced_resizing : true,
			theme_advanced_resize_horizontal : false,
			
			// URL
			// See documentation: http://www.tinymce.com/wiki.php/Configuration:document_base_url
			document_base_url : "/",
			relative_urls : false,
			
			// The last setting item must be without ending comma.
			language : "cs"
		});
		<%-- "notStandaloneWysiwygEditor" is CSS class. --%>
		$('textarea.notStandaloneWysiwygEditor').tinymce({
			
			<%-- The original setting is in the file /tinymce/examples/index.html. --%>
			
			// Location of TinyMCE script
			script_url : '<c:out value="${tinymceHomeUrl}" />tiny_mce.js',

			// General options
			theme : "advanced",
			plugins : "autolink,lists,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist",

			// Theme options
			// See documentation: http://www.tinymce.com/wiki.php/Configuration:theme_advanced_buttons_1_n
			theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,formatselect,fontselect,fontsizeselect",
			theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
			theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,advhr,nonbreaking,|,print,|,fullscreen",
			theme_advanced_buttons4 : "",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "bottom",
			theme_advanced_resizing : true,
			theme_advanced_resize_horizontal : false,
			
			// URL
			// See documentation: http://www.tinymce.com/wiki.php/Configuration:document_base_url
			document_base_url : "/",
			relative_urls : false,
			
			// The last setting item must be without ending comma.
			language : "cs"
		});
	});
</script>
<%-- /TinyMCE --%>



<%-- My --%>
<link rel="stylesheet" type="text/css" href="<c:url value='${myCssHome}' />my-styles.css" media="all" />
<script type="text/javascript" src="<c:url value='${myJsHome}' />my-scripts.js"></script>

<link rel="stylesheet" type="text/css" href="<c:url value='${myCssHome}' />html5.css" media="all" />
<!--[if IE lt 9]><script type="text/javascript" src="<c:url value='${myJsHome}' />html5.js"></script><![endif]-->
<%-- /My --%>
