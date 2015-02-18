/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.php.hkc.UserAccessControl;

import java.net.URL;
import javax.swing.Action;
import org.netbeans.spi.editor.completion.CompletionDocumentation;

/**
 *
 * @author peter.ho
 */
public class AnnotationCompletionDocumentation implements CompletionDocumentation {

    private final AnnotationCompletionItem item;

    public AnnotationCompletionDocumentation(AnnotationCompletionItem item) {
        this.item = item;
    }

    @Override
    public String getText() {
        String phpClassName = this.item.getPhpClass().phpClass.getName();

        return "<p style=\"font-weight: bold; font-size: 1.2em\">@" + phpClassName + "</p>\n"
                + "<p style=\"font-weight: bold; font-size: 1.1em\">Description</p>\n"
                + "<p>User Access Control annotation <code>@" + phpClassName + "</code>.</p>\n"
                + "<p>Should ONLY apply to Controller class + Controller methods</p>"
                + "<p style=\"font-weight: bold; font-size: 1.1em\">Example</p>\n"
                + "<pre><code>\n"
                + "/**\n"
                + " * @" + phpClassName + "(" + this.item.getPhpClass().annoParam + ")\n"
                + " */\n"
                + "class"
                + "{\n"
                + "    return $a + $b;\n"
                + "}\n"
                + "</code></pre>\n"
                + "";
    }

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public CompletionDocumentation resolveLink(String string) {
        return null;
    }

    @Override
    public Action getGotoSourceAction() {
        return null;
    }

}
