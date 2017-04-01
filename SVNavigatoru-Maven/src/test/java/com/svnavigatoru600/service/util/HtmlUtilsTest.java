package com.svnavigatoru600.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.test.category.UnitTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(UnitTests.class)
public final class HtmlUtilsTest {

    private static final int TEXT_MAX_LENGTH = AbstractNotificationEmailService.getTextMaxLength();

    /**
     * The returned string should be identical to the original one (except the wrapping <code>div</code> element and
     * white characters). There are many texts in attributes, but not outside them.
     */
    @Test
    public void testCropEscapedWithWellFormed() {
        String inputText = "<p>f&zeta;<strong> d<em>sS</em>fs</strong>&nbsp; <img title=\"S vyplazen&yacute;m jazykem\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-tongue-out.gif\" alt=\"S vyplazen&yacute;m jazykem\" border=\"0\" /><img title=\"Mrkaj&iacute;c&iacute;\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-wink.gif\" alt=\"Mrkaj&iacute;c&iacute;\" border=\"0\" /><img title=\"Usm&iacute;vaj&iacute;c&iacute; se\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-smile.gif\" alt=\"Usm&iacute;vaj&iacute;c&iacute; se\" border=\"0\" /> &cap;<strong>&hearts; &spades;&clubs; SSS dfdfd<br /></strong></p><p><img src=\"https://lh5.googleusercontent.com/-MrOajTq8fmc/UMnlYyDtt_I/AAAAAAAAAA8/rBgiP6CZ41w/s771/pokus+1+copy.jpg\" alt=\"\" /></p>";
        String expected = "<div><p>f&zeta;<strong> d<em>sS</em>fs</strong>&nbsp; <img title=\"S vyplazen&yacute;m jazykem\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-tongue-out.gif\" alt=\"S vyplazen&yacute;m jazykem\" border=\"0\"/><img title=\"Mrkaj&iacute;c&iacute;\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-wink.gif\" alt=\"Mrkaj&iacute;c&iacute;\" border=\"0\"/><img title=\"Usm&iacute;vaj&iacute;c&iacute; se\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-smile.gif\" alt=\"Usm&iacute;vaj&iacute;c&iacute; se\" border=\"0\"/> &cap;<strong>&hearts; &spades;&clubs; SSS dfdfd<br/></strong></p><p><img src=\"https://lh5.googleusercontent.com/-MrOajTq8fmc/UMnlYyDtt_I/AAAAAAAAAA8/rBgiP6CZ41w/s771/pokus+1+copy.jpg\" alt=\"\"/></p></div>\r\n";
        Assert.assertEquals(expected, HtmlUtils.cropEscaped(inputText, TEXT_MAX_LENGTH));
    }

    @Test
    public void testCropEscapedWithWellFormedTooLong() {
        String inputText = "<p>f&zeta;<strong>BA$up5e##s3fEC+4x+wUp!ufAJuRA_uDRapHa=ehuwujemutRunAbRa4-r$Thac+    (BRAVO - ALPHA - Dollar - uni</strong><img title=\"S vyplazen&yacute;m jazykem\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-tongue-out.gif\" alt=\"S vyplazen&yacute;m jazykem\" border=\"0\" />AB</p>";
        String expected = "<div><p>f&zeta;<strong>BA$up5e##s3fEC+4x+wUp!ufAJuRA_uDRapHa=ehuwujemutRunAbRa4-r$Thac+    (BRAVO - ALPHA - Dollar - uni</strong><img title=\"S vyplazen&yacute;m jazykem\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-tongue-out.gif\" alt=\"S vyplazen&yacute;m jazykem\" border=\"0\"/>A</p></div>\r\n";
        Assert.assertEquals(expected, HtmlUtils.cropEscaped(inputText, TEXT_MAX_LENGTH));
    }

    @Test
    public void testCropEscapedWithWellFormedTooLongElementsRemoved() {
        String inputText = "<p>f&zeta;<strong>BA$up5e##s3fEC+4x+wUp!ufAJuRA_uDRapHa=ehuwujemutRunAbRa4-r$Thac+    (BRAVO - ALPHA - Dollar - uni</strong><img title=\"S vyplazen&yacute;m jazykem\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-tongue-out.gif\" alt=\"S vyplazen&yacute;m jazykem\" border=\"0\" />AB<b>C</b><b>D</b></p>";
        String expected = "<div><p>f&zeta;<strong>BA$up5e##s3fEC+4x+wUp!ufAJuRA_uDRapHa=ehuwujemutRunAbRa4-r$Thac+    (BRAVO - ALPHA - Dollar - uni</strong><img title=\"S vyplazen&yacute;m jazykem\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-tongue-out.gif\" alt=\"S vyplazen&yacute;m jazykem\" border=\"0\"/>A</p></div>\r\n";
        Assert.assertEquals(expected, HtmlUtils.cropEscaped(inputText, TEXT_MAX_LENGTH));
    }

    @Test
    public void testCropEscapedWithPlainText() {
        String inputText = "BA$up5e##s3fEC+4x+wUp!ufAJuRA_uDRapHa=ehuwujemutRunAbRa4-r$Thac+    (BRAVO - ALPHA - Dollar - uniform - papa - Five";
        String expected = "BA$up5e##s3fEC+4x+wUp!ufAJuRA_uDRapHa=ehuwujemutRunAbRa4-r$Thac+    (BRAVO - ALPHA - Dollar - unifor";
        Assert.assertEquals(expected, HtmlUtils.cropEscaped(inputText, TEXT_MAX_LENGTH));
    }

    @Test
    public void testCropEscapedWithBadFormedP() {
        // <p> is not closed
        String inputText = "<p><img title=\"S vyplazen&yacute;m jazykem\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-tongue-out.gif\" />";
        String expected = "&lt;p&gt;&lt;img title=&quot;S vyplazen&yacute;m jazykem&quot; src=&quot;/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotion";
        Assert.assertEquals(expected, HtmlUtils.cropEscaped(inputText, TEXT_MAX_LENGTH));
    }

    @Test
    public void testCropEscapedWithBadFormedBr() {
        // <br> is not closed
        String inputText = "<img title=\"S vyplazen&yacute;m jazykem\" src=\"/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-tongue-out.gif\" /><br>";
        String expected = "&lt;img title=&quot;S vyplazen&yacute;m jazykem&quot; src=&quot;/svnavigatoru600/tinymce/jscripts/tiny_mce/plugins/emotions/i";
        Assert.assertEquals(expected, HtmlUtils.cropEscaped(inputText, TEXT_MAX_LENGTH));
    }
}
