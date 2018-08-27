package ex.guitartest.video;

import android.media.AudioRecord;

import java.io.IOException;

import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;

public class AudioInput implements TarsosDSPAudioInputStream {
    private AudioRecord underlyingStream;
    private final TarsosDSPAudioFormat format;
    public AudioInput(AudioRecord underlyingStream, TarsosDSPAudioFormat format){
        this.underlyingStream = underlyingStream;
        this.format = format;
    }

    @Override
    public void skip(long bytesToSkip) throws IOException {
        throw new IOException("Can not skip in audio stream");
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return underlyingStream.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        underlyingStream.stop();
        underlyingStream.release();
        underlyingStream=null;
    }

    @Override
    public TarsosDSPAudioFormat getFormat() {
        return format;
    }

    @Override
    public long getFrameLength() {
        return -1;
    }

}
