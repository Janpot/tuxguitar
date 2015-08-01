package org.herac.tuxguitar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class TGServiceReader {

    private static final String SERVICE_PATH = "META-INF/services/";

    public static <T> Iterable<T> lookupProviders(Class<T> spi) {
        return lookupProviders(spi, TGClassLoader.instance().getClassLoader());
    }

    public static <T> Iterable<T> lookupProviders(final Class<T> spi, final ClassLoader loader) {
        return new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    try {
                        if (spi == null || loader == null) {
                            throw new IllegalArgumentException();
                        }
                        return new IteratorImpl<T>(spi, loader, loader.getResources(SERVICE_PATH + spi.getName()));
                    } catch (IOException ioex) {
                        return Collections.<T>emptyList().iterator();
                    }
                }
            };
    }

    private static final class IteratorImpl<T> implements Iterator<T> {
        private Class spi;
        private ClassLoader loader;
        private Enumeration<URL> urls;
        private Iterator<String> iterator;

        public IteratorImpl(Class spi, ClassLoader loader, Enumeration<URL> urls) {
            this.spi = spi;
            this.loader = loader;
            this.urls = urls;
            this.initialize();
        }

        private void initialize() {
            List<String> providers = new ArrayList<String>();
            while (this.urls.hasMoreElements()) {
                URL url = this.urls.nextElement();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        String provider = uncommentLine(line).trim();
                        if (provider.length() > 0) {
                            providers.add(provider);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.iterator = providers.iterator();
        }

        private String uncommentLine(String line) {
            int index = line.indexOf('#');
            if (index >= 0) {
                return (line.substring(0, index));
            }
            return line;
        }

        public boolean hasNext() {
            return (this.iterator != null && this.iterator.hasNext());
        }

        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                Object provider = this.loader.loadClass((String) this.iterator.next()).newInstance();
                if (this.spi.isInstance(provider)) {
                    return (T) provider;
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
