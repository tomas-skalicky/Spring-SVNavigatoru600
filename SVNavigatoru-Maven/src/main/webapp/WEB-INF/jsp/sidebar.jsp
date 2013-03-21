<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>


<my:currentUser checkIfLogged="true">

	<%-- planned events --%>
	<my:loggedUser checkIfCanSeeEvents="true">
		<c:if test="${not empty futureEvents}">
			<li class="block"><c:forEach items="${futureEvents}" var="eventWrapper">
					<div class="box">
						<div class="${eventWrapper.event.typedPriority.cssClass} small">
							<div class="titlewrap">
								<h4>
									<span><c:out value="${eventWrapper.event.name}" /> <small><em><c:out
													value="${eventWrapper.middleDateFormattedDate}" />
										</em>
									</small>
									</span>
								</h4>
							</div>
							<div class="wrapleft">
								<div class="wrapright">
									<div class="tr">
										<div class="bl">
											<div class="tl">
												<div class="br the-content">
													<p>
														<c:out value="${eventWrapper.event.description}" />
													</p>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:forEach></li>
		</c:if>
	</my:loggedUser>
	
	
	
	<%-- newest contributions in forum --%>
	<my:loggedUser checkIfCanSeeContributions="true">
		<c:if test="${not empty lastSavedContributions}">
			<li class="block">
				<h3 class="comments">
					<spring:message code="aside.last-contributions-and-ideas" />
				</h3>
				<ul id="comments">
					<c:forEach items="${lastSavedContributions}" var="contributionWrapper">
						<li class="comment">
							<div class="comment-mask small">
								<div class="comment-main">
									<div class="comment-wrap1">
										<div class="comment-wrap2">
											<div class="comment-head">
												<p>
													<strong><c:out value="${contributionWrapper.contribution.author.fullName}" />
													</strong>,
													<c:out value="${contributionWrapper.defaultDateTimeFormattedLastSaveTime}" />
												</p>
											</div>
											<div class="comment-body">
												<p>
													<c:out value="${contributionWrapper.contribution.text}" escapeXml="false" />
												</p>
											</div>
											<div class="comment-footer clearfix">
												<p>
													<spring:message code="forum.threads.thread" />
													: <a
														href="<c:url value="/forum/temata/existujici/${contributionWrapper.contribution.thread.id}/prispevky/"
															/>"><c:out
															value="${contributionWrapper.contribution.thread.name}" />
													</a>
												</p>
											</div>
										</div>
									</div>
								</div>
							</div></li>
					</c:forEach>
				</ul></li>
		</c:if>
	</my:loggedUser>
</my:currentUser>
