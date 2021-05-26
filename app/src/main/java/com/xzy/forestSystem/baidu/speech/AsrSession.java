package com.xzy.forestSystem.baidu.speech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import com.xzy.forestSystem.baidu.speech.AbsSession;
import com.xzy.forestSystem.baidu.speech.Console;
import com.xzy.forestSystem.baidu.speech.MergedDecoder;
import com.xzy.forestSystem.baidu.speech.Results;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* access modifiers changed from: package-private */
@SuppressLint({"DefaultLocale"})
public final class AsrSession extends AbsSession {
    public static final String ERROR_AUDIO = "#3, Audio recording error.";
    public static final String ERROR_CLIENT = "#5, Other client side errors.";
    public static final String ERROR_INSUFFICIENT_PERMISSIONS = "#9, Insufficient permissions.";
    public static final String ERROR_NETWORK = "#2, Other network related errors.";
    public static final String ERROR_NETWORK_TIMEOUT = "#1, Network operation timed out.";
    public static final String ERROR_NO_MATCH = "#7, No recognition result matched.";
    public static final String ERROR_RECOGNIZER_BUSY = "#8, RecognitionService busy.";
    public static final String ERROR_SERVER = "#4, Server sends error status.";
    public static final String ERROR_SPEECH_TIMEOUT = "#6, No speech input.";
    private static final HashMap<String, String[]> map = new HashMap<>();
    Map<String, String> ARGS_VAD_MAPPING = new HashMap();
    private OutputStream audioOutput;
    private byte[] buffer;
    boolean calledBegin;
    boolean calledEnd;
    private Decoder decoder;

    /* renamed from: in */
    VadInputStream f199in;
    Object mCancelSoundResId;
    private long mSpeechEndMills;
    private int sample;

    public interface Decoder {
        void close();

        Results.Result read() throws Exception;

        void write(byte[] bArr, int i, int i2, VadInputStream.SpeechStatus speechStatus) throws Exception;
    }

    static {
        map.put(null, new String[]{"enter", "exit"});
        map.put("enter", new String[]{Console.TYPE_ASR_MSG_READY, Console.TYPE_ASR_MSG_FINISH, "exit"});
        map.put(Console.TYPE_ASR_MSG_READY, new String[]{Console.TYPE_ASR_MSG_AUDIO, Console.TYPE_ASR_MSG_FINISH, Console.TYPE_ASR_MSG_ENGINE_TYPE, "exit"});
        map.put(Console.TYPE_ASR_MSG_AUDIO, new String[]{Console.TYPE_ASR_MSG_VOLUME, "exit", Console.TYPE_ASR_MSG_ENGINE_TYPE});
        map.put(Console.TYPE_ASR_MSG_VOLUME, new String[]{Console.TYPE_ASR_MSG_AUDIO, Console.TYPE_ASR_MSG_BEGIN, Console.TYPE_ASR_MSG_END, Console.TYPE_ASR_MSG_PARTIAL, Console.TYPE_ASR_MSG_FINISH, "exit", Console.TYPE_ASR_MSG_ENGINE_TYPE});
        map.put(Console.TYPE_ASR_MSG_BEGIN, new String[]{Console.TYPE_ASR_MSG_AUDIO, Console.TYPE_ASR_MSG_FINISH, "exit", Console.TYPE_ASR_MSG_ENGINE_TYPE});
        map.put(Console.TYPE_ASR_MSG_END, new String[]{Console.TYPE_ASR_MSG_PARTIAL, Console.TYPE_ASR_MSG_AUDIO, Console.TYPE_ASR_MSG_FINISH, "exit", Console.TYPE_ASR_MSG_ENGINE_TYPE});
        map.put(Console.TYPE_ASR_MSG_PARTIAL, new String[]{Console.TYPE_ASR_MSG_PARTIAL, Console.TYPE_ASR_MSG_AUDIO, Console.TYPE_ASR_MSG_END, Console.TYPE_ASR_MSG_FINISH, "exit", Console.TYPE_ASR_MSG_ENGINE_TYPE});
        map.put(Console.TYPE_ASR_MSG_FINISH, new String[]{"exit", Console.TYPE_ASR_MSG_EVENT_ERROR, "exit"});
        map.put(Console.TYPE_ASR_MSG_ENGINE_TYPE, new String[]{Console.TYPE_ASR_MSG_ENGINE_TYPE, Console.TYPE_ASR_MSG_BEGIN, Console.TYPE_ASR_MSG_PARTIAL, Console.TYPE_ASR_MSG_AUDIO, Console.TYPE_ASR_MSG_END, Console.TYPE_ASR_MSG_FINISH, "exit"});
    }

    public AsrSession(Console console, String args) {
        super(console, "asr", "args-asr.xml", "args-asr-defaults.xml", map, args);
        this.ARGS_VAD_MAPPING.put(String.format("%s_%s", Constant.VAD_INPUT, 8000).toLowerCase(), "params-vad-multiple-8k.list");
        this.ARGS_VAD_MAPPING.put(String.format("%s_%s", Constant.VAD_SEARCH, 8000).toLowerCase(), "params-vad-single-8k.list");
        this.ARGS_VAD_MAPPING.put(String.format("%s_%s", "touch", 8000).toLowerCase(), "params-vad-touch-8k.list");
        this.ARGS_VAD_MAPPING.put(String.format("%s_%s", Constant.VAD_INPUT, Integer.valueOf((int) Constant.SAMPLE_16K)).toLowerCase(), "params-vad-multiple-16k.list");
        this.ARGS_VAD_MAPPING.put(String.format("%s_%s", Constant.VAD_SEARCH, Integer.valueOf((int) Constant.SAMPLE_16K)).toLowerCase(), "params-vad-single-16k.list");
        this.ARGS_VAD_MAPPING.put(String.format("%s_%s", "touch", Integer.valueOf((int) Constant.SAMPLE_16K)).toLowerCase(), "params-vad-touch-16k.list");
    }

    public InputStream createMicrophoneInputStream(Context context, Map<String, Object> params) throws Exception {
        String file = (String) params.get("audio.file");
        Long position = (Long) params.get("audio.position");
        if (file != null) {
            try {
                if (!"".equals(file)) {
                    if (file.startsWith("res://")) {
                        return getClass().getResourceAsStream(FileSelector_Dialog.sRoot + file.replaceFirst("res://", "").replaceFirst(FileSelector_Dialog.sRoot, ""));
                    } else if (file.startsWith("asset://")) {
                        return getClass().getResourceAsStream("/assets/" + file.replaceFirst("asset://", "").replaceFirst(FileSelector_Dialog.sRoot, ""));
                    } else if (!file.startsWith("#")) {
                        return new FileInputStream(file);
                    } else {
                        Matcher m = Pattern.compile("^#(.*)[#.](.*?)\\(").matcher(file);
                        log(Level.INFO, "createMicrophoneInputStream from method: " + file);
                        if (!m.find()) {
                            return null;
                        }
                        String cls = m.group(1);
                        String mtd = m.group(2);
                        log(Level.INFO, "----method: " + cls + " " + mtd);
                        try {
                            return (InputStream) Class.forName(cls).getMethod(mtd, new Class[0]).invoke(null, new Object[0]);
                        } catch (Exception e) {
                            throw new Exception("invoke " + file + " failed", e);
                        }
                    }
                }
            } catch (Exception e2) {
                throw new Exception("#3, Audio recording error., file: " + file, e2);
            }
        }
        String res = (String) params.get("basic.sound_start");
        MicrophoneInputStream mic = new MicrophoneInputStream(this.sample);
        if (res != null) {
            MediaPlayer player = res.matches("^(0x)?\\d+$") ? MediaPlayer.create(context, Integer.parseInt(res)) : MediaPlayer.create(context, Uri.parse(res));
            player.start();
            while (player.isPlaying()) {
                Thread.sleep(1);
            }
            player.release();
        }
        mic.position((long) mic.globalPosition());
        if (position != null) {
            mic.position(position.longValue());
        }
        return mic;
    }

    private void tryWriteBegin() throws Exception {
        if (!this.calledBegin && !this.calledEnd) {
            appendMsg(new Console.Msg(Console.TYPE_ASR_MSG_BEGIN, null));
            this.calledBegin = true;
        }
    }

    private void tryWriteEnd(Map<String, Object> basicParams) throws Exception {
        if (this.calledBegin && !this.calledEnd) {
            appendMsg(new Console.Msg(Console.TYPE_ASR_MSG_END, null));
            this.calledEnd = true;
            play(this.console.context(), basicParams.get("basic.sound_end"), false);
        }
    }

    private void tryWriteFinish(Map<String, Object> basicParams, Object obj) throws Exception {
        if (this.isForceCancel) {
            log(Level.INFO, "ignore finish message, because session is canceled!");
            log(Level.INFO, obj);
            return;
        }
        if (!this.calledBegin || !this.calledEnd) {
            appendMsg(new Console.Msg(Console.TYPE_ASR_MSG_FINISH, obj));
        } else {
            appendMsg(new Console.Msg(Console.TYPE_ASR_MSG_FINISH, obj));
        }
        if (obj instanceof Results.Result) {
            play(this.console.context(), basicParams.get("basic.sound_success"), false);
        } else {
            play(this.console.context(), basicParams.get("basic.sound_error"), false);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x03c6  */
    @Override // com.baidu.speech.AbsSession
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onExecute(java.util.Map<java.lang.String, java.lang.Object> r38, java.lang.String r39) {
        /*
        // Method dump skipped, instructions count: 1094
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.speech.AsrSession.onExecute(java.util.Map, java.lang.String):void");
    }

    private boolean collectResultMessages(Map<String, Object> basicParams) throws Exception {
        long mTimeoutTime = Long.parseLong(basicParams.get("basic.timeout") + "");
        if (this.mSpeechEndMills == 0 || System.currentTimeMillis() - this.mSpeechEndMills <= mTimeoutTime) {
            boolean touchedFinalResult = false;
            Results.Result r = this.decoder.read();
            while (r != null) {
                if (r instanceof Results.FinalResult) {
                    tryWriteFinish(basicParams, r);
                    touchedFinalResult = true;
                } else if (r instanceof MergedDecoder.MessageResult) {
                    appendMsg(new Console.Msg(Console.TYPE_ASR_MSG_ENGINE_TYPE, r));
                } else {
                    appendMsg(new Console.Msg(Console.TYPE_ASR_MSG_PARTIAL, r));
                }
                r = this.decoder.read();
            }
            return touchedFinalResult;
        }
        throw new Exception("#1, Network operation timed out. waiting time out(now - endOfSpeech > " + mTimeoutTime + "ms).");
    }

    /* access modifiers changed from: protected */
    public void release() {
        if (this.f199in != null) {
            try {
                this.f199in.close();
            } catch (IOException ex) {
                log(Level.WARNING, ex);
            }
        }
        if (this.decoder != null) {
            this.decoder.close();
        }
        if (this.audioOutput != null) {
            try {
                this.audioOutput.close();
            } catch (IOException e) {
                log(Level.WARNING, e);
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsSession
    public void onCancel(boolean force) {
        if (!force && this.f199in != null) {
            this.f199in.finish();
        }
        if (force) {
            play(this.console.context(), this.mCancelSoundResId, false);
        }
    }

    public interface VadInputStream {
//        @Override // java.io.Closeable, java.lang.AutoCloseable, java.io.InputStream, com.baidu.speech.AsrSession.VadInputStream
        void close() throws IOException;

        SpeechStatus detect();

        void finish();

        boolean finished();

//        @Override // java.io.InputStream, com.baidu.speech.AsrSession.VadInputStream
        int read() throws IOException;

        public enum SpeechStatus {
            Default(-2, "default"),
            Ready(-3, "ready"),
            Begin(-4, "begin"),
            Pause(-6, "pause"),
            Resume(-7, "resume"),
            End(-5, "end");
            
            public final String name;
            public final int status;

            private SpeechStatus(int status2, String name2) {
                this.status = status2;
                this.name = name2;
            }
        }
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0042: APUT  
      (r1v0 'player' android.media.MediaPlayer[] A[D('player' android.media.MediaPlayer[])])
      (0 ??[int, short, byte, char])
      (r3v10 android.media.MediaPlayer)
     */
    private void play(Context context, Object soundRes, boolean sync) {
        log(Level.INFO, "playing: " + soundRes);
        if (soundRes != null) {
            try {
                final MediaPlayer[] player = new MediaPlayer[1];
                String res = "" + soundRes;
                player[0] = res.matches("^(0x)?\\d+$") ? MediaPlayer.create(context, Integer.parseInt(res)) : MediaPlayer.create(context, Uri.parse(res));
                player[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.baidu.speech.AsrSession.2
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public void onCompletion(MediaPlayer mp) {
                        player[0].release();
                    }
                });
                player[0].start();
                if (sync) {
                    while (player[0].isPlaying()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            } catch (Exception e2) {
                log(Level.WARNING, e2);
            }
        }
    }

    class SpeechEndFilter implements AbsSession.MsgFilter {
        SpeechEndFilter() {
        }

        @Override // com.baidu.speech.AbsSession.MsgFilter
        public boolean accept(Console.Msg msg) throws Exception {
            if (!Console.TYPE_ASR_MSG_END.equals(msg.getKey())) {
                return true;
            }
            AsrSession.this.mSpeechEndMills = System.currentTimeMillis();
            return true;
        }
    }

    static class OutfileFilter implements AbsSession.MsgFilter {
        private OutputStream out;

        public OutfileFilter(String file) throws IOException {
            if (file != null && !"".equals(file)) {
                this.out = new FileOutputStream(file);
            }
        }

        @Override // com.baidu.speech.AbsSession.MsgFilter
        public boolean accept(Console.Msg msg) throws Exception {
            byte[] data;
            if (this.out != null) {
                if (Console.TYPE_ASR_MSG_AUDIO.equals(msg.getKey()) && (data = (byte[]) msg.getValue()) != null && data.length > 0) {
                    this.out.write(data);
                }
                if ("exit".equals(msg.getKey())) {
                    this.out.close();
                    this.out = null;
                }
            }
            return true;
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            super.finalize();
            if (this.out != null) {
                this.out.close();
                this.out = null;
            }
        }
    }
}
