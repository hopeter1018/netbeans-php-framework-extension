/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.php.hkc.UserAccessControl;

import java.util.Map;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.Exceptions;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.modules.php.hkc.Utils.CodeCompleterUtils;
import org.netbeans.modules.php.hkc.Utils.CommonConstants;

/**
 *
 * @author peter.ho
 */
@MimeRegistrations({
    @MimeRegistration(mimeType = CommonConstants.NB_MIME_PHP, service = CompletionProvider.class)
})
public class AnnotationCompleter implements CompletionProvider {

    @Override
    public CompletionTask createTask(int i, JTextComponent jTextComponent) {
        if (i != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {
                String filter = null;
                String PREFIX = "@";

                Map<String, CodeCompleterUtils.PhpClassFile> options = CodeCompleterUtils.getAllPhpWithAnnotations(CodeCompleterUtils.getEditingProject(), "Annotation");
                if (CodeCompleterUtils.isPhpClass(document)) {
                    int startOffset = caretOffset - 1;
                    try {
                        final StyledDocument bDoc = (StyledDocument) document;
                        final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
                        final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                        final int whiteOffset = indexOfWhite(line);
                        filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
                        if (whiteOffset > 0) {
                            startOffset = lineStartOffset + whiteOffset + 1;
                        } else {
                            startOffset = lineStartOffset;
                        }
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    if (filter == null || !filter.startsWith(PREFIX)) {
                        completionResultSet.finish();
                        return;
                    }

                    for (Map.Entry<String, CodeCompleterUtils.PhpClassFile> option : options.entrySet()) {
                        if ((PREFIX + option.getKey()).startsWith(filter)) {
                            completionResultSet.addItem(new AnnotationCompletionItem(PREFIX + option.getKey(), option.getValue(), startOffset, caretOffset));
                        }
                    }
                }
                completionResultSet.finish();
            }
        }, jTextComponent);

    }

    @Override
    public int getAutoQueryTypes(JTextComponent arg0, String arg1) {
        return 0;
    }

    static int getRowFirstNonWhite(StyledDocument doc, int offset)
            throws BadLocationException {
        Element lineElement = doc.getParagraphElement(offset);
        int start = lineElement.getStartOffset();
        while (start + 1 < lineElement.getEndOffset()) {
            try {
                if (doc.getText(start, 1).charAt(0) != ' ') {
                    break;
                }
            } catch (BadLocationException ex) {
                throw (BadLocationException) new BadLocationException(
                        "calling getText(" + start + ", " + (start + 1)
                        + ") on doc of length: " + doc.getLength(), start).initCause(ex);
            }
            start++;
        }
        return start;
    }

    static int indexOfWhite(char[] line) {
        int i = line.length;
        while (--i > -1) {
            final char c = line[i];
            if (Character.isWhitespace(c)) {
                return i;
            }
        }
        return -1;
    }
}
