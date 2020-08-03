package com.gojeck.base

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class GoLiveData<T>() : MediatorLiveData<T>() {

    constructor(value: T) : this() {
        setValue(value)
    }

    //region firstActive
    private val isFirst = AtomicBoolean(true)

    override fun onActive() {
        if (isFirst.getAndSet(false)) {
            onFirstActive()
        }
        super.onActive()
    }

    fun onFirstActive() {}
    //endregion firstActive

    //region sources

    private val sources by lazy { mutableListOf<LiveData<*>>() }

    fun removeSources() {
        //concurrent exception. so added toList()
        sources.toList().forEach {
            removeSource(it)
        }
    }

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        super.addSource(source, onChanged)
        sources.add(source)
    }

    fun addSources(vararg source: LiveData<*>, onChanged: () -> Unit) {
        source.forEach {
            addSource(it) {
                onChanged()
            }
        }
    }

    fun <S : Any?> replaceSource(other: LiveData<S>, onChanged: (S) -> Unit) {
        removeSources()
        addSource(other, onChanged)
    }

    fun replaceSource(other: LiveData<T>) {
        removeSources()
        addSource(other) {
            value = it
        }
    }


    override fun <S : Any?> removeSource(toRemote: LiveData<S>) {
        super.removeSource(toRemote)
        sources.remove(toRemote)
    }

    //endregion sources


    //region event
    var version = 0
        private set

    private var handledVersion = 0

    @MainThread
    fun observeEvent(owner: LifecycleOwner, observer: Observer<in T>) {
        //only one observer will be notified of changes.

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (version != handledVersion) {
                handledVersion = version
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    final override fun setValue(t: T?) {
        version += 1
        super.setValue(t)
    }
    //endregion event
}

fun <T> GoLiveData<T>.observeEvent(owner: LifecycleOwner, onChanged: (T) -> Unit) {
    observeEvent(owner, Observer {
        onChanged(it)
    })
}

operator fun <T> GoLiveData<T>.plusAssign(other: LiveData<out T>) {
    addSource(other, ::setValue)
}

fun <T> GoLiveData<T>.receive(other: () -> LiveData<T>) {
    addSource(other(), ::setValue)
}


@MainThread
inline fun <X, Y> GoLiveData<X>.map(crossinline transform: (X) -> Y): GoLiveData<Y> {
    val result = GoLiveData<Y>()
    result.addSource(this) { x -> result.value = transform(x) }
    return result
}

@MainThread
inline fun <X, Y> GoLiveData<X>.switchMap(
    crossinline transform: (X) -> GoLiveData<Y>
): GoLiveData<Y> {
    val result = GoLiveData<Y>()
    result.addSource(this, object : Observer<X> {
        var mSource: GoLiveData<Y>? = null
        override fun onChanged(x: X) {
            val newLiveData = transform(x)
            if (mSource === newLiveData) {
                return
            }
            if (mSource != null) {
                result.removeSource(mSource!!)
            }
            mSource = newLiveData
            if (mSource != null) {
                result.addSource(
                    mSource!!
                ) { y -> result.value = y }
            }
        }
    })
    return result
}

@Suppress("NOTHING_TO_INLINE")
inline fun <X> GoLiveData<X>.distinct(): GoLiveData<X> {
    val outputLiveData = GoLiveData<X>()
    outputLiveData.addSource(this, object : Observer<X> {
        var mFirstTime = true
        override fun onChanged(currentValue: X) {
            val previousValue = outputLiveData.value
            if (mFirstTime
                || previousValue == null && currentValue != null
                || previousValue != null && previousValue != currentValue
            ) {
                mFirstTime = false
                outputLiveData.value = currentValue
            }
        }
    })
    return outputLiveData
}


/**
 * if return is true, remove observer
 */
fun <T> GoLiveData<T>.observeUntil(@NonNull func: (T) -> Boolean) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T) {
            if (func(t)) {
                removeObserver(this)
            }
        }
    })
}

fun <T> LiveData<T>.asBase(): GoLiveData<T> = GoLiveData<T>().apply {
    plusAssign(this@asBase)
}

fun <T> GoLiveData<T>.call(value: T) {
    postValue(value)
}

fun GoLiveData<Unit>.call() {
    postValue(kotlin.Unit)
}

