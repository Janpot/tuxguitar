package org.herac.tuxguitar.gui.system;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This is a marker annotation for all services provided.
 * Add this annotation to your class in order TuxGuitar to see it. Your class should implement one of service-provider
 * interfaces listed below in order the app to recognize it:
 * <ul>
 *     <li>{@link org.herac.tuxguitar.gui.system.plugins.TGPlugin} - a custom plugin.</li>
 * </ul>
 *
 *
 * @author Alexander Kochurov (AKochurov at github.com)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}
