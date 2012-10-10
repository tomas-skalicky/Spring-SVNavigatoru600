<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>

<%
if (!UserUtils.isLogged()) {
	out.println(JspCodeGenerator.getMainNavigationItem("prihlaseni", "login.title", request));
} else {

	// The user is logged in; hence shows the navigation.
	User user = UserUtils.getLoggedUser();
	if (user.canSeeNews()) {
		out.println(JspCodeGenerator.getMainNavigationItem("novinky", "news.title", request));
		out.println(JspCodeGenerator.getMainNavigationItem("kalendar-akci", "event-calendar.title", request));
		out.println(JspCodeGenerator.getMainNavigationItem("forum/temata", "forum.title", request));

		String[][] subItems = new String[][] {
				new String[] { "zapisy-z-jednani/sv", "session-records-of-sv.title" },
				new String[] { "zapisy-z-jednani/vybor", "session-records-of-board.title" }
		};
		out.println(JspCodeGenerator.getMainNavigationItem("zapisy-z-jednani", "session-records.title", request, subItems));

		subItems = new String[][] {
				new String[] { "dalsi-dokumenty/ucetnictvi", "other-documents.accounting.title" },
				new String[] { "dalsi-dokumenty/smlouvy", "other-documents.contracts.title" },
				new String[] { "dalsi-dokumenty/pravidelne-revize", "other-documents.regular-revisions.title" },
				new String[] { "remostav/dokumentace", "remostav.title" },
				new String[] { "dalsi-dokumenty/ostatni", "other-documents.others.title" }
		};
		out.println(JspCodeGenerator.getMainNavigationItem("dalsi-dokumenty", "other-documents.title", request, subItems));
		
		out.println(JspCodeGenerator.getMainNavigationItem("uzitecne-odkazy", "useful-links.title", request));
		
		subItems = new String[][] {
				new String[] { "remostav/dokumentace", "remostav.documentation.title" },
				new String[] { "remostav/kontakt", "remostav.contact.title" }
		};
		out.println(JspCodeGenerator.getMainNavigationItem("remostav/kontakt", "remostav", "remostav.title", request, subItems));
		
		out.println(JspCodeGenerator.getMainNavigationItem("vybor", "board.title", request));
	}

	if (user.canSeeUsers()) {
		out.println(JspCodeGenerator.getMainNavigationItem("administrace-uzivatelu",
				"user-administration.title", request));
	}
}
%>
