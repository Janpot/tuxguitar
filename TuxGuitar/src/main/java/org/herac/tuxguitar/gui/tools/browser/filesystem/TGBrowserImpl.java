package org.herac.tuxguitar.gui.tools.browser.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.herac.tuxguitar.gui.tools.browser.base.TGBrowser;
import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserElement;

public class TGBrowserImpl extends TGBrowser {

    private File root;
    private TGBrowserElementImpl element;
    private TGBrowserDataImpl data;

    public TGBrowserImpl(TGBrowserDataImpl data) {
        this.data = data;
    }

    public void open() {
        this.root = new File(this.data.getPath());
    }

    public void close() {
        this.root = null;
    }

    public void cdElement(TGBrowserElement element) {
        this.element = (TGBrowserElementImpl) element;
    }

    public void cdRoot() {
        this.element = null;
    }

    public void cdUp() {
        if (this.element != null) {
            this.element = this.element.getParent();
        }
    }

    public List<TGBrowserElement> listElements() {
        List<TGBrowserElement> elements = new ArrayList<TGBrowserElement>();
        File file = ((this.element != null) ? this.element.getFile() : this.root);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    elements.add(new TGBrowserElementImpl(this.element, file1));
                }
            }
        }
        if (!elements.isEmpty()) {
            Collections.sort(elements, new TGBrowserElementComparator());
        }
        return elements;
    }

}
