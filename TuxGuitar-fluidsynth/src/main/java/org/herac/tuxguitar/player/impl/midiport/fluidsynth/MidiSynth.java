package org.herac.tuxguitar.player.impl.midiport.fluidsynth;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class MidiSynth {
    private static final FluidSynth fluidSynth = (FluidSynth) Native.loadLibrary("libfluidsynth.so.1", FluidSynth.class);

    private MidiOutputPortImpl loadedPort;

    private Pointer synth;
    private Pointer settings;
    private Pointer driver;
    private int soundfont_id;

    public MidiSynth() {
        this.settings = fluidSynth.new_fluid_settings();
        this.loadedPort = null;
    }

    public boolean isInitialized() {
        return this.settings != null;
    }

    public void finalize() {
        if (driver != null) {
            fluidSynth.delete_fluid_audio_driver(driver);
            driver = null;
        }
        if (synth != null) {
            fluidSynth.delete_fluid_synth(synth);
            synth = null;
        }
        if (settings != null) {
            fluidSynth.delete_fluid_settings(settings);
            settings = null;
        }
    }

    public boolean isConnected(MidiOutputPortImpl port) {
        return (port != null && this.loadedPort != null && this.loadedPort.equals(port));
    }

    public void connect(MidiOutputPortImpl port) {
        if (isInitialized()) {
            this.disconnect(this.loadedPort);
            this.open();
            this.loadFont(port.getSoundFont());
            this.loadedPort = port;
        }
    }

    public void disconnect(MidiOutputPortImpl port) {
        if (isInitialized() && isConnected(port)) {
            this.unloadFont();
            this.close();
            this.loadedPort = null;
        }
    }

    public void reconnect() {
        MidiOutputPortImpl connection = this.loadedPort;
        if (isConnected(connection)) {
            this.disconnect(connection);
            this.connect(connection);
        }
    }

    public void sendSystemReset() {
        if (synth != null) {
            checkSynth("fluid_synth_system_reset", fluidSynth.fluid_synth_system_reset(synth));
        }
    }

    public void sendNoteOn(int channel, int key, int velocity) {
        if (synth != null) {
            checkSynth("fluid_synth_noteon", fluidSynth.fluid_synth_noteon(synth, channel, key, velocity));
        }
    }

    public void sendNoteOff(int channel, int key, int velocity) {
        if (synth != null) {
            checkSynth("fluid_synth_noteoff", fluidSynth.fluid_synth_noteoff(synth, channel, key));
        }
    }

    public void sendControlChange(int channel, int controller, int value) {
        if (synth != null) {
            checkSynth("fluid_synth_cc", fluidSynth.fluid_synth_cc(synth, channel, controller, value));
        }
    }

    public void sendProgramChange(int channel, int value) {
        if (synth != null) {
            checkSynth("fluid_synth_program_change", fluidSynth.fluid_synth_program_change(synth, channel, value));
        }
    }

    public void sendPitchBend(int channel, int value) {
        if (synth != null) {
            checkSynth("fluid_synth_pitch_bend", fluidSynth.fluid_synth_pitch_bend(synth, channel, ((value * 128))));
        }
    }

    public void setDoubleProperty(String key, double value) {
        if (settings != null && key != null) {
            fluidSynth.fluid_settings_setnum(settings, key, (float) value);
        }
    }

    public void setIntegerProperty(String key, int value) {
        if (settings != null && key != null) {
            fluidSynth.fluid_settings_setint(settings, key, value);
        }
    }

    public void setStringProperty(String key, String value) {
        if (settings != null && key != null) {
            fluidSynth.fluid_settings_setstr(settings, key, value);
        }
    }

    public double getDoubleProperty(String key) {
        if (settings != null && key != null) {
            DoubleByReference value = new DoubleByReference();
            fluidSynth.fluid_settings_getnum(settings, key, value);
            return value.getValue();
        }
        return 0;
    }

    public int getIntegerProperty(String key) {
        if (settings != null && key != null) {
            IntByReference value = new IntByReference();
            fluidSynth.fluid_settings_getint(settings, key, value);
            return value.getValue();
        }
        return 0;
    }

    public String getStringProperty(String key) {
        if (settings != null && key != null) {
            PointerByReference p = new PointerByReference();

            fluidSynth.fluid_settings_getstr(settings, key, p);

            return p.getValue().getString(0);
        }
        return "";
    }

    public double getDoublePropertyDefault(String key) {
        if (settings != null && key != null) {
            return fluidSynth.fluid_settings_getnum_default(settings, key);
        }
        return 0;
    }

    public int getIntegerPropertyDefault(String key) {
        if (settings != null && key != null) {
            return fluidSynth.fluid_settings_getint_default(settings, key);
        }
        return 0;
    }

    public String getStringPropertyDefault(String key) {
        if (settings != null && key != null) {
            return fluidSynth.fluid_settings_getstr_default(settings, key);
        }
        return "";
    }

    public List<String> getPropertyOptions(String key) {
        List<String> options = new ArrayList<String>();
        if (settings != null) {
            fluidSynth.fluid_settings_foreach_option(settings, key, null, new FluidSynth.SettingsCallback(options));
        }
        return options;
    }

    public int[] getIntegerPropertyRange(String key) {
        if (settings != null && key != null) {
            IntByReference minimum = new IntByReference();
            IntByReference maximum = new IntByReference();

            fluidSynth.fluid_settings_getint_range(settings, key, minimum, maximum);

            return new int[]{minimum.getValue(), maximum.getValue()};
        }
        return new int[]{0, 0};
    }

    public double[] getDoublePropertyRange(String key) {
        if (settings != null && key != null) {
            DoubleByReference minimum = new DoubleByReference();
            DoubleByReference maximum = new DoubleByReference();

            fluidSynth.fluid_settings_getnum_range(settings, key, minimum, maximum);

            return new double[]{minimum.getValue(), maximum.getValue()};
        }
        return new double[]{0, 0};
    }

    public boolean isRealtimeProperty(String key) {
        if (settings != null && key != null) {
            return fluidSynth.fluid_settings_is_realtime(settings, key) != 0;
        }
        return false;
    }

    private void open() {
        if (settings != null) {
            if (driver != null) {
                fluidSynth.delete_fluid_audio_driver(driver);
            }
            if (synth != null) {
                fluidSynth.delete_fluid_synth(synth);
            }
            synth = fluidSynth.new_fluid_synth(settings);
            driver = fluidSynth.new_fluid_audio_driver(settings, synth);

            checkSynth("fluid_synth_set_interp_method", fluidSynth.fluid_synth_set_interp_method(synth, -1, FluidSynth.FLUID_INTERP_NONE));
        }
    }

    private void close() {
        if (driver != null) {
            fluidSynth.delete_fluid_audio_driver(driver);
            driver = null;
        }
        if (synth != null) {
            fluidSynth.delete_fluid_synth(synth);
            synth = null;
        }
    }

    private void loadFont(String path) {
        if (synth != null && soundfont_id <= 0) {
            soundfont_id = fluidSynth.fluid_synth_sfload(synth, path, 1);
            if (soundfont_id < 0) {
                checkSynth("fluid_synth_sfload", soundfont_id);
            }
        }
    }

    private void unloadFont() {
        if (synth != null && soundfont_id > 0) {
            checkSynth("fluid_synth_sfunload", fluidSynth.fluid_synth_sfunload(synth, soundfont_id, 1));
            soundfont_id = 0;
        }
    }

    private void checkSynth(String operation, int exitCode) {
        if (exitCode != FluidSynth.FLUID_OK && synth != null) {
            System.err.println("FluidSynth error on " + operation + ": " + fluidSynth.fluid_synth_error(synth));
        }
    }
}
