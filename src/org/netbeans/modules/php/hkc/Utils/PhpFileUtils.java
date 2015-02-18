/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.php.hkc.Utils;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.util.Exceptions;

/**
 *
 * @author peter.ho
 */
public class PhpFileUtils {

    public static String getDocumentText(Document doc)
    {
        String result = null;
        try {
             result = doc.getText(0, doc.getEndPosition().getOffset());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return result;
    }

}
