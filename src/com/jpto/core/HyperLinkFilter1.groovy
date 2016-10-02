package com.jpto.core

import com.jediterm.terminal.model.hyperlinks.HyperlinkFilter
import com.jediterm.terminal.model.hyperlinks.LinkInfo
import com.jediterm.terminal.model.hyperlinks.LinkResult
import com.jediterm.terminal.model.hyperlinks.LinkResultItem
import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@CompileStatic
class HyperLinkFilter1 implements HyperlinkFilter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static String prefix = " abcde"

    @Override
    LinkResult apply(String line) {
        int i = line.indexOf(prefix)
        if (i == -1) {
            return null

        }
//        log.info "found link : ${line}"
        LinkInfo linkInfo = new LinkInfo({
            log.info "hl : ${line}"
            Thread.dumpStack()
        })
        LinkResultItem item = new LinkResultItem(i + 1, i + prefix.length() , linkInfo);
        LinkResult linkResult = new LinkResult(item);
        return linkResult;
    }
}
