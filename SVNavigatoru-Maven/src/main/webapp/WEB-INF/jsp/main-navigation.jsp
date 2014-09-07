<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>

<my:currentUser checkIfNotLogged="true">
	<%=JspCodeGenerator.getMainNavigationItem("prihlaseni", "login.title", request)%>
</my:currentUser>
<my:currentUser checkIfLogged="true">
	<%-- The user is logged in; hence shows the navigation. --%>
	
	<my:loggedUser checkIfCanSeeHomepage="true">
		<%
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
		
		out.println(JspCodeGenerator.getMainNavigationItem("vybor", "board.title", request));%>
	</my:loggedUser>

	<my:loggedUser checkIfCanSeeUserAdministration="true">
		<%=JspCodeGenerator.getMainNavigationItem("administrace-uzivatelu", "user-administration.title", request)%>
	</my:loggedUser>
</my:currentUser>
