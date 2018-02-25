#include <stdlib.h>
#include <assert.h>
#include <jni.h>
#include <string.h>
#include <pthread.h>

// for __android_log_print(ANDROID_LOG_INFO, "YourApp", "formatted message");
#include <android/log.h>

// for native audio
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

// for native asset manager
#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>


#include <unistd.h>


// engine interfaces
static SLObjectItf engineObject = NULL;
static SLEngineItf engineEngine;

// recorder interfaces
static SLObjectItf recorderObject = NULL;
static SLRecordItf recorderRecord;
static SLAndroidSimpleBufferQueueItf recorderBufferQueue;


// 5 seconds of recorded audio at 16 kHz mono, 16-bit signed little endian
#define RECORDER_FRAMES (16000 * 5)
static short recorderBuffer[RECORDER_FRAMES];
static unsigned recorderSize = 0;

// a mutext to guard against re-entrance to record & playback
// as well as make recording and playing back to be mutually exclusive
// this is to avoid crash at situations like:
//    recording is in session [not finished]
//    user presses record button and another recording coming in
// The action: when recording/playing back is not finished, ignore the new request
static pthread_mutex_t  audioEngineLock = PTHREAD_MUTEX_INITIALIZER;



static int pipefd[2];





// create the engine and output mix objects
void Java_com_kjipo_eartraining_recorder_Recorder_createEngine(JNIEnv* env, jclass clazz)
{

    __android_log_print(ANDROID_LOG_INFO, "C++", "Creating engine");

    SLresult result;

    // create engine
    result = slCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // realize the engine
    result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // get the engine interface, which is needed in order to create other objects
    result = (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineEngine);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // create output mix, with environmental reverb specified as a non-required interface
    //const SLInterfaceID ids[1] = {SL_IID_ENVIRONMENTALREVERB};
    //const SLboolean req[1] = {SL_BOOLEAN_FALSE};
    //result = (*engineEngine)->CreateOutputMix(engineEngine, &outputMixObject, 1, ids, req);
    //assert(SL_RESULT_SUCCESS == result);
    //(void)result;

    // realize the output mix
    //result = (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
    //assert(SL_RESULT_SUCCESS == result);
    //(void)result;

    // get the environmental reverb interface
    // this could fail if the environmental reverb effect is not available,
    // either because the feature is not present, excessive CPU load, or
    // the required MODIFY_AUDIO_SETTINGS permission was not requested and granted
    //result = (*outputMixObject)->GetInterface(outputMixObject, SL_IID_ENVIRONMENTALREVERB,
    //        &outputMixEnvironmentalReverb);
    //if (SL_RESULT_SUCCESS == result) {
    //    result = (*outputMixEnvironmentalReverb)->SetEnvironmentalReverbProperties(
    //            outputMixEnvironmentalReverb, &reverbSettings);
    //    (void)result;
    //}
    // ignore unsuccessful result codes for environmental reverb, as it is optional for this example

}


// this callback handler is called every time a buffer finishes recording
void bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context)
{

    __android_log_print(ANDROID_LOG_INFO, "C++", "Callback called");

    assert(bq == recorderBufferQueue);
    assert(NULL == context);
    // for streaming recording, here we would call Enqueue to give recorder the next buffer to fill
    // but instead, this is a one-time buffer so we stop recording
    SLresult result;
    result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_STOPPED);
    if (SL_RESULT_SUCCESS == result) {
        recorderSize = RECORDER_FRAMES * sizeof(short);
    }
    pthread_mutex_unlock(&audioEngineLock);


    write(pipefd[1], recorderBuffer, recorderSize);
}



jboolean Java_com_kjipo_eartraining_recorder_Recorder_createAudioRecorder(JNIEnv* env, jclass clazz)
{
    SLresult result;

    // configure audio source
    SLDataLocator_IODevice loc_dev = {SL_DATALOCATOR_IODEVICE, SL_IODEVICE_AUDIOINPUT,
            SL_DEFAULTDEVICEID_AUDIOINPUT, NULL};
    SLDataSource audioSrc = {&loc_dev, NULL};

    // configure audio sink
    SLDataLocator_AndroidSimpleBufferQueue loc_bq = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};
    SLDataFormat_PCM format_pcm = {SL_DATAFORMAT_PCM, 1, SL_SAMPLINGRATE_16,
        SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16,
        SL_SPEAKER_FRONT_CENTER, SL_BYTEORDER_LITTLEENDIAN};
    SLDataSink audioSnk = {&loc_bq, &format_pcm};

    // create audio recorder
    // (requires the RECORD_AUDIO permission)
    const SLInterfaceID id[1] = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE};
    const SLboolean req[1] = {SL_BOOLEAN_TRUE};
    result = (*engineEngine)->CreateAudioRecorder(engineEngine, &recorderObject, &audioSrc,
            &audioSnk, 1, id, req);
    if (SL_RESULT_SUCCESS != result) {
        return JNI_FALSE;
    }

    // realize the audio recorder
    result = (*recorderObject)->Realize(recorderObject, SL_BOOLEAN_FALSE);
    if (SL_RESULT_SUCCESS != result) {
        return JNI_FALSE;
    }

    // get the record interface
    result = (*recorderObject)->GetInterface(recorderObject, SL_IID_RECORD, &recorderRecord);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // get the buffer queue interface
    result = (*recorderObject)->GetInterface(recorderObject, SL_IID_ANDROIDSIMPLEBUFFERQUEUE,
            &recorderBufferQueue);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // register callback on the buffer queue
    result = (*recorderBufferQueue)->RegisterCallback(recorderBufferQueue, bqRecorderCallback,
            NULL);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    return JNI_TRUE;
}



// set the recording state for the audio recorder
void Java_com_kjipo_eartraining_recorder_Recorder_startRecording(JNIEnv* env, jclass clazz)
{

    __android_log_print(ANDROID_LOG_INFO, "C++", "Starting recording");

    SLresult result;

    if (pthread_mutex_trylock(&audioEngineLock)) {
        return;
    }
    // in case already recording, stop recording and clear buffer queue
    result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_STOPPED);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
    result = (*recorderBufferQueue)->Clear(recorderBufferQueue);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // the buffer is not valid for playback yet
    recorderSize = 0;

    // enqueue an empty buffer to be filled by the recorder
    // (for streaming recording, we would enqueue at least 2 empty buffers to start things off)
    result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue, recorderBuffer,
            RECORDER_FRAMES * sizeof(short));
    // the most likely other result is SL_RESULT_BUFFER_INSUFFICIENT,
    // which for this code example would indicate a programming error
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // start recording
    result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
}


// shut down the native audio system
void Java_com_kjipo_eartraining_recorder_Recorder_shutdown(JNIEnv* env, jclass clazz)
{

    // destroy buffer queue audio player object, and invalidate all associated interfaces
    //if (bqPlayerObject != NULL) {
        //(*bqPlayerObject)->Destroy(bqPlayerObject);
        //bqPlayerObject = NULL;
        //bqPlayerPlay = NULL;
        //bqPlayerBufferQueue = NULL;
        //bqPlayerEffectSend = NULL;
        //bqPlayerMuteSolo = NULL;
        //bqPlayerVolume = NULL;
    //}

    // destroy file descriptor audio player object, and invalidate all associated interfaces
    //if (fdPlayerObject != NULL) {
      //  (*fdPlayerObject)->Destroy(fdPlayerObject);
       // fdPlayerObject = NULL;
        //fdPlayerPlay = NULL;
        //fdPlayerSeek = NULL;
        //fdPlayerMuteSolo = NULL;
        //fdPlayerVolume = NULL;
    //}

    // destroy URI audio player object, and invalidate all associated interfaces
    //if (uriPlayerObject != NULL) {
        //(*uriPlayerObject)->Destroy(uriPlayerObject);
        //uriPlayerObject = NULL;
        //uriPlayerPlay = NULL;
        //uriPlayerSeek = NULL;
        //uriPlayerMuteSolo = NULL;
        //uriPlayerVolume = NULL;
    //}

    // destroy audio recorder object, and invalidate all associated interfaces
    if (recorderObject != NULL) {
        (*recorderObject)->Destroy(recorderObject);
        recorderObject = NULL;
        recorderRecord = NULL;
        recorderBufferQueue = NULL;
    }

    // destroy output mix object, and invalidate all associated interfaces
    //if (outputMixObject != NULL) {
        //(*outputMixObject)->Destroy(outputMixObject);
        //outputMixObject = NULL;
        //outputMixEnvironmentalReverb = NULL;
    //}

    // destroy engine object, and invalidate all associated interfaces
    if (engineObject != NULL) {
        (*engineObject)->Destroy(engineObject);
        engineObject = NULL;
        engineEngine = NULL;
    }

    pthread_mutex_destroy(&audioEngineLock);
}






int Java_com_kjipo_eartraining_recorder_Recorder_getFD(JNIEnv* env, jclass clazz) {
    if (pipe(pipefd) == -1) {
        __android_log_print(ANDROID_LOG_VERBOSE, "C++", "Error creating pipe");
    }

    return pipefd[0];

}



jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    // Get jclass with env->FindClass.
    // Register methods with env->RegisterNatives.

    jclass class1 = env->FindClass("com/kjipo/eartraining/recorder/Recorder");

    JNINativeMethod methods[] = {
            {"createEngine", "()V", (void *)&Java_com_kjipo_eartraining_recorder_Recorder_createEngine},
            {"createAudioRecorder", "()Z", (void *)&Java_com_kjipo_eartraining_recorder_Recorder_createAudioRecorder},
            {"startRecording", "()V", (void *)&Java_com_kjipo_eartraining_recorder_Recorder_startRecording},
            {"shutdown", "()V", (void *)Java_com_kjipo_eartraining_recorder_Recorder_shutdown},
            {"getFD", "()I", (void *)Java_com_kjipo_eartraining_recorder_Recorder_getFD}
    };

    env->RegisterNatives(class1, methods, sizeof(methods)/sizeof(methods[0]));

    return JNI_VERSION_1_6;
}