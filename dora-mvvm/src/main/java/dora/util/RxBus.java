package dora.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    private static volatile RxBus rxBus;
    private final PublishSubject<Object> mEventBus = PublishSubject.create();

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void post(Object event) {
        mEventBus.onNext(event);
    }

    public Observable<?> toObservable() {
        return mEventBus;
    }

    public <T> Observable<T> toObservable(Class<T> cls) {
        return mEventBus.ofType(cls);
    }

    public <T> Observable<T> toObservable(int code, Class<T> cls) {
        return mEventBus.ofType(Message.class)
                .filter(msg -> msg.code == code && cls.isInstance(msg.event))
                .map(msg -> (T) msg.event);
    }

    public static class Message {

        int code;
        Object event;

        public Message(int code, Object event) {
            this.code = code;
            this.event = event;
        }
    }
}
