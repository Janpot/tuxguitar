package org.herac.tuxguitar.player.impl.jsa.sequencer;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;

public class MidiSequencerProviderImpl implements MidiSequencerProvider{
	
	public MidiSequencerProviderImpl(){
		super();
	}
	
	public List<MidiSequencer> listSequencers() throws MidiPlayerException {
		try {
			List<MidiSequencer> sequencers = new ArrayList<MidiSequencer>();
			MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
            for (MidiDevice.Info info : infos) {
                try {
                    boolean exists = false;
                    for (MidiSequencer sequencer : sequencers) {
                        if ((sequencer).getKey().equals(info.getName())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        MidiDevice device = MidiSystem.getMidiDevice(info);
                        if (device instanceof Sequencer) {
                            sequencers.add(new MidiSequencerImpl((Sequencer) device));
                        }
                    }
                } catch (MidiUnavailableException e) {
                    throw new MidiPlayerException(TuxGuitar.getProperty("jsa.error.midi.unavailable"), e);
                }
            }
			return sequencers;
		}catch (Throwable t) {
			throw new MidiPlayerException(TuxGuitar.getProperty("jsa.error.unknown"),t);
		}
	}
	
	public void closeAll() throws MidiPlayerException {
		// Not implemented
	}
}
