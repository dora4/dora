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
}
