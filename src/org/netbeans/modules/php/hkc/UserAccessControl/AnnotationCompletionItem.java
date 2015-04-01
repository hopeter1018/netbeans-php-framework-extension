/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.php.hkc.UserAccessControl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.modules.php.hkc.UserAccessControl.Bundle;
import org.netbeans.modules.php.hkc.Utils.CodeCompleterUtils;
import org.netbeans.modules.php.hkc.Utils.ProjectUtils;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author peter.ho
 */
public class AnnotationCompletionItem implements CompletionItem {

    @NbBundle.Messages({
        "AnnotationGroupName=Hkc - UAC"
    })
    private final String text;
    private final CodeCompleterUtils.PhpClassFile phpClass;
    private static final ImageIcon fieldIcon
            = new ImageIcon(ImageUtilities.loadImage("org/netbeans/modules/php/hkc/icon.png", false));
    private static final Color fieldColor = Color.decode("0x000000");
    private final int caretOffset;
    private final int dotOffset;

    public AnnotationCompletionItem(String text, CodeCompleterUtils.PhpClassFile phpClass, int dotOffset, int caretOffset) {
        this.text = text;
        this.phpClass = phpClass;
        this.dotOffset = dotOffset;
        this.caretOffset = caretOffset;
    }

    @Override
    public void defaultAction(JTextComponent jTextComponent) {
        try {
            StyledDocument doc = (StyledDocument) jTextComponent.getDocument();
            doc.remove(dotOffset, caretOffset - dotOffset);
            String suffix = "";
            if (!"".equals(this.phpClass.annoParam)) {
                suffix = "(" + this.phpClass.annoParam + ")";
            }
            doc.insertString(dotOffset, text + suffix, null);
            
            String docContent = doc.getText(0, dotOffset);
            String statementUse = "use " + this.phpClass.namespaceString + "\\" + text.substring(1) + ";";
            if (! docContent.contains(statementUse)) {
                int useOffset = docContent.indexOf(";", docContent.indexOf("namespace "));
                if (useOffset != -1) {
                    doc.insertString(2 + useOffset, "\r\n" + statementUse, null);
                }
            }

            Completion.get().hideAll();
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void processKeyEvent(KeyEvent arg0) {
    }

    @Override
    public int getPreferredWidth(Graphics graphics, Font font) {
        return CompletionUtilities.getPreferredWidth(text, Bundle.AnnotationGroupName(), graphics, font);
    }

    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor,
            Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(fieldIcon, text, Bundle.AnnotationGroupName(), g, defaultFont,
                (selected ? Color.white : fieldColor), width, height, selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int i) {
                completionResultSet.setDocumentation(new AnnotationCompletionDocumentation(AnnotationCompletionItem.this));
                completionResultSet.finish();
            }
        });
    }

    @Override
    public CompletionTask createToolTipTask() {
        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int i) {
                JToolTip toolTip = new JToolTip();
                toolTip.setTipText("Press Enter to insert \"" + text + "\"");
                completionResultSet.setToolTip(toolTip);
                completionResultSet.finish();
            }
        });
    }

    @Override
    public boolean instantSubstitution(JTextComponent arg0) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 0;
    }

    @Override
    public CharSequence getSortText() {
        return text.substring(1);
    }

    public CodeCompleterUtils.PhpClassFile getPhpClass() {
        return phpClass;
    }

    public String getText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }
}
