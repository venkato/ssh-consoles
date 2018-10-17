package com.jpto.core

import net.sf.jremoterun.utilities.nonjdk.idwutils.Shortcuts

import javax.swing.KeyStroke
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

import groovy.transform.CompileStatic;


@CompileStatic
enum SshKeyStokesShortCuts implements Shortcuts{




    createNewHost("Create new host", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)),

    duplicate("Duplicate tab", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)),


    changeColor("Change color", KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_DOWN_MASK)),


    myPassword("My password", KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK)),

    translateFix("Translate fix", KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK )),

    openInMuCommander("open in mu commander", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK )),
    copyAll("copy all", null),

    changeFont("change font", null),
    sedView("sed view", null),
    methodInfo("method info", KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK )),

    ;



//    pageUp("Page up", KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.ALT_DOWN_MASK )),
//
//    pageDown("Page down", KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.ALT_DOWN_MASK ));





    KeyStroke keyStroke;
    String displayName;

    SshKeyStokesShortCuts(String displayname, KeyStroke keyStroke) {
        this.keyStroke = keyStroke
        this.displayName = displayname
    }

}
