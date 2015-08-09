package org.herac.tuxguitar.player.impl.midiport.fluidsynth;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.util.List;

/**
 * This is JNA-based native library stub for FluidSynth
 *
 * @author Alexander Kochurov
 */
interface FluidSynth extends Library {
    int FLUID_INTERP_NONE = 0;
    int FLUID_OK = 0;

    Pointer new_fluid_settings();

    void delete_fluid_synth(Pointer synth);

    void delete_fluid_settings(Pointer settings);

    void delete_fluid_audio_driver(Pointer driver);

    Pointer new_fluid_synth(Pointer settings);

    Pointer new_fluid_audio_driver(Pointer settings, Pointer synth);

    int fluid_synth_set_interp_method(Pointer synth, int i, int fluidInterpNone);

    int fluid_synth_sfload(Pointer synth, String path, int i);

    int fluid_synth_program_change(Pointer synth, int channel, int program);

    int fluid_synth_sfunload(Pointer synth, int soundfont_id, int i);

    int fluid_synth_system_reset(Pointer synth);

    int fluid_synth_noteon(Pointer synth, int channel, int note, int velocity);

    int fluid_synth_noteoff(Pointer synth, int channel, int note);

    int fluid_synth_cc(Pointer synth, int channel, int control, int value);

    int fluid_synth_pitch_bend(Pointer synth, int channel, int i);

    void fluid_settings_setstr(Pointer settings, String key, String value);

    void fluid_settings_setnum(Pointer settings, String key, float value);

    void fluid_settings_getint(Pointer settings, String key, IntByReference value);

    void fluid_settings_setint(Pointer settings, String key, int value);

    void fluid_settings_getnum(Pointer settings, String key, DoubleByReference value);

    double fluid_settings_getnum_default(Pointer settings, String key);

    void fluid_settings_getnum_range(Pointer settings, String key, DoubleByReference minimum, DoubleByReference maximum);

    int fluid_settings_getint_default(Pointer settings, String key);

    int fluid_settings_is_realtime(Pointer settings, String key);

    void fluid_settings_getint_range(Pointer settings, String key, IntByReference minimum, IntByReference maximum);

    String fluid_settings_getstr_default(Pointer settings, String key);

    void fluid_settings_getstr(Pointer settings, String key, PointerByReference p);

    void fluid_settings_foreach_option(Pointer settings, String key, Object o, SettingsCallback callback);

    class SettingsCallback implements Callback {
        private final List<String> values;

        SettingsCallback(List<String> values) {
            this.values = values;
        }

        public void callback(Pointer data, String name, String option) {
            values.add(option);
        }
    }

    String fluid_synth_error(Pointer synth);
}
