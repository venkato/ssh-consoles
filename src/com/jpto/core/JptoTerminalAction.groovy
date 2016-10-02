package com.jpto.core;

import com.google.common.base.Predicate;
import com.jediterm.terminal.ui.TerminalAction;
import groovy.lang.Closure;
import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrUtilities;
import net.sf.jremoterun.utilities.nonjdk.idwutils.IdwUtils;
import net.sf.jremoterun.utilities.nonjdk.idwutils.Shortcuts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

@CompileStatic
public class JptoTerminalAction extends TerminalAction {
    private static final Logger log = LogManager.getLogger();

    //	volatile Component component;
    Closure<Boolean> runnable;

    public JptoTerminalAction(Shortcuts name, Closure<Boolean> runnable) {
        //this(name.getDisplayName(), name.getKeyStroke(), runnable);
        super(name.getDisplayName(), createKetStroke(name.getKeyStroke()), f1(runnable));
        this.runnable = runnable
    }


//    public JptoTerminalAction(String name, KeyStroke keyStroke, Predicate<KeyEvent> runnable) {
//        super(name, createKetStroke(keyStroke), runnable);
//    }

    static KeyStroke[] createKetStroke(KeyStroke ks){
        if(ks==null){
            return new KeyStroke[0];
        }
        KeyStroke[] result = [ks];
        return result;
    }


//    public JptoTerminalAction(KeyStroke keyStroke, String name, Closure<Boolean> runnable) {
//        super(name, createKetStroke(keyStroke), f1(runnable));
//        this.runnable = runnable;
//    }

    static Predicate<KeyEvent>  f1(Closure<Boolean> runnable){
        return  new Predicate<KeyEvent>() {

            @Override
            public boolean apply(KeyEvent input) {
                Component component = input.getComponent();
                if (component == null) {
                    log.error("component is null");
                    return false;
                } else {
                    try {
                        return runnable.call(component);
                    } catch (Throwable e2) {
                        JrrUtilities.showException("", e2);
                        return false;
                    }
                }
            }
        }
    }



    @Override
    public JMenuItem toMenuItem() {
        JMenuItem menuItem = super.toMenuItem();
        ActionListener[] actionListeners = menuItem.getActionListeners();
        if (actionListeners.length == 1) {
            menuItem.removeActionListener(actionListeners[0]);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Component com = (Component) e.getSource();
                    JPopupMenu popupMenuForMenuItem1 = IdwUtils.getPopupMenuForMenuItem1((Container) com);
                    runnable.call(popupMenuForMenuItem1.getInvoker());
                }
            });
        } else {
            log.info("stange actionslist");
        }


        return menuItem;
    }


}
