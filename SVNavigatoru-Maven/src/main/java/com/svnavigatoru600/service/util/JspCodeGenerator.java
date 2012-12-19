package com.svnavigatoru600.service.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

/**
 * Provides a set of static functions related to generation of JSP code.
 * 
 * @author Tomas Skalicky
 */
public final class JspCodeGenerator {
    
    private JspCodeGenerator() {
    }

    /**
     * Gets the JSP code of a root item of the main navigation.
     * 
     * @param reference
     *            The reference of the navigation item.
     * @param title
     *            The title of the navigation item.
     * @param request
     */
    public static String getMainNavigationItem(String reference, String title, HttpServletRequest request) {
        return JspCodeGenerator.getMainNavigationItem(reference, reference, title, request);
    }

    /**
     * Gets the JSP code of a root item of the main navigation.
     * 
     * @param reference
     *            The reference of the navigation item.
     * @param rootAddress
     *            The part of the URL address which is common for all sub-items of the root item.
     * @param title
     *            The title of the navigation item.
     * @param request
     */
    private static String getMainNavigationItem(String reference, String rootAddress, String title,
            HttpServletRequest request) {
        return JspCodeGenerator.getMainNavigationItem(reference, rootAddress, title, request,
                new String[][] {});
    }

    /**
     * Gets the JSP code of a root item of the main navigation.
     * 
     * @param reference
     *            The reference of the navigation item.
     * @param title
     *            The title of the navigation item.
     * @param request
     * @param subItems
     *            The array of information about sub-items of the root navigation item. Each sub-item is
     *            specified by an array of {@link String}s where on the first position there is a reference of
     *            the item and on the second position there is its title.
     */
    public static String getMainNavigationItem(String reference, String title, HttpServletRequest request,
            String[][] subItems) {
        return String.format("<li>%s</li>",
                JspCodeGenerator.getMainNavigationItem(reference, reference, title, request, subItems));
    }

    /**
     * Gets the JSP code of a root item of the main navigation.
     * 
     * @param reference
     *            The reference of the navigation item.
     * @param rootAddress
     *            The part of the URL address which is common for all sub-items of the root item.
     * @param title
     *            The title of the navigation item.
     * @param request
     * @param subItems
     *            The array of information about sub-items of the root navigation item. Each sub-item is
     *            specified by an array of {@link String}s where on the first position there is a reference of
     *            the item and on the second position there is its title.
     */
    public static String getMainNavigationItem(String reference, String rootAddress, String title,
            HttpServletRequest request, String[][] subItems) {
        return String.format("<li>%s</li>", JspCodeGenerator.getMainNavigationItem(reference, rootAddress,
                title, request, true, subItems));
    }

    /**
     * Gets the JSP code of an item of the main navigation. The code does not contain <code>&lt;li></code>
     * tag.
     * 
     * @param reference
     *            The reference of the navigation item.
     * @param rootAddress
     *            The part of the URL address which is common for all sub-items of the root item.
     * @param title
     *            The title of the navigation item.
     * @param request
     * @param isRoot
     *            Indicates whether the navigation item which is to be returned is one of the root items.
     * @param subItems
     *            The array of information about sub-items of the navigation item. Each sub-item is specified
     *            by an array of {@link String} s where on the first position there is a reference of the item
     *            and on the second position there is its title.
     */
    private static String getMainNavigationItem(String reference, String rootAddress, String title,
            HttpServletRequest request, boolean isRoot, String[][] subItems) {
        StringBuilder jspCode = new StringBuilder();

        String homeUrl = request.getContextPath();
        String cssClasses = "";
        if (isRoot) {
            String servletPath = Url.getServletPath(request);
            boolean hasSameRoot = (servletPath.length() > rootAddress.length())
                    && servletPath.substring(1, rootAddress.length() + 1).equals(rootAddress);
            if (hasSameRoot) {
                cssClasses = "active";
            }
        }
        jspCode.append(String.format("<a href=\"%s/%s/\" class=\"fadeThis %s\">", homeUrl, reference,
                cssClasses));

        jspCode.append("<span>");

        MessageSource messageSource = Localization.getMessageSource(request);
        jspCode.append(Localization.findLocaleMessage(messageSource, request, title));

        jspCode.append("</span></a>");

        // Prints the sub-items.
        if (subItems.length > 0) {
            jspCode.append("<ul>");
            for (String[] subItem : subItems) {
                jspCode.append(String.format("<li>%s</li>", JspCodeGenerator.getMainNavigationItem(
                        subItem[0], "", subItem[1], request, false, new String[][] {})));
            }
            jspCode.append("</ul>");
        }

        return jspCode.toString();
    }
}
