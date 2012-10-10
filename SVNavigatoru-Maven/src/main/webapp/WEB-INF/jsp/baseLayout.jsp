<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>

<!DOCTYPE html>
<html>
<head>
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<tiles:insertAttribute name="head-includes" />
</head>

<body>
	<div id="page" class="with-sidebar">
		<div id="header-wrap">
			<div id="header" class="block-content">
				<%-- For the information about HTML5, see http://oli.jp/2009/html5-structure4/. --%>
				<header id="pagetitle">
					<h1 class="logo">
						<tiles:insertAttribute name="logo" />
					</h1>
					<tiles:insertAttribute name="logout" />
					<div class="clear"></div>
				</header>

				<%-- main navigation --%>
				<nav id="nav-wrap1">
					<div id="nav-wrap2">
						<ul id="nav">
							<tiles:insertAttribute name="main-navigation" />
						</ul>
					</div>
				</nav>
				<%-- /main navigation --%>

			</div>

			<%-- main content and sidebar --%>
			<div id="main-wrap1">
				<div id="main-wrap2">
					<div id="main" class="block-content">
						<div class="mask-main rightdiv">
							<div class="mask-left">
								<article class="col1">
									<div id="main-content">
										<tiles:insertAttribute name="main-content" />
									</div>
								</article>
								<aside class="col2">
									<ul id="sidebar">
										<tiles:insertAttribute name="sidebar" />
									</ul>
								</aside>
							</div>
						</div>
						<div class="clear-content"></div>
					</div>
				</div>
			</div>
			<%-- /main content and sidebar --%>
		</div>

		<%-- footer --%>
		<footer id="footer">
			<div class="block-content">
				<tiles:insertAttribute name="footer" />
			</div>
		</footer>
		<%-- /footer --%>
	</div>
</body>
</html>
